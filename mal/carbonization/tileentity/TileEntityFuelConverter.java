package mal.carbonization.tileentity;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.api.IFuelContainer;
import mal.carbonization.carbonization;
import mal.carbonization.items.ItemStructureBlock;
import mal.carbonization.network.PacketHandler;
import mal.core.multiblock.MultiblockWorkQueueItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityFuelConverter extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory{

	//TODO: make this not hardcoded...
	public enum FuelTypes {
		CHARCOAL(new ItemStack(Item.coal,1,1), new ItemStack(carbonization.dust,1,0), 0),
		PEAT(new ItemStack(carbonization.fuel,1,0), new ItemStack(carbonization.dust,1,1), 1),
		LIGNITE(new ItemStack(carbonization.fuel,1,1), new ItemStack(carbonization.dust,1,2), 2),
		SUBBIT(new ItemStack(carbonization.fuel,1,2), new ItemStack(carbonization.dust,1,3), 3),
		BIT(new ItemStack(carbonization.fuel,1,3), new ItemStack(carbonization.dust,1,4), 4),
		COAL(new ItemStack(Item.coal,1,0), new ItemStack(carbonization.dust,1,5), 5),
		ANTHRACITE(new ItemStack(carbonization.fuel,1,4), new ItemStack(carbonization.dust,1,6), 6),
		GRAPHITE(new ItemStack(carbonization.fuel,1,5), new ItemStack(carbonization.dust,1,7), 7);

		private ItemStack item;
		private ItemStack dust;
		private int index;

		private FuelTypes(ItemStack item, ItemStack dust, int index)
		{
			this.item = item;
			this.index = index;
			this.dust = dust;
		}

		public ItemStack getItem()
		{
			return item;
		}
		
		public ItemStack getDust()//514
		{
			return dust;
		}
		
		public int getIndex()
		{
			return index;
		}
	}

	public int craftingCooldown;
	public int processTime = carbonization.MAXAUTOCRAFTTIME;
	public double fuelTank;

	private Random furnaceRand = new Random();

	public int maxFuelCapacity = 6400;

	public double speedUpgrade;
	public double efficiencyUpgrade;
	
	public boolean makeDust = false;
	public int currentIndex = 0;

	public double potentialMultiplyer;//reduces the amount of potential fuel by this percentage
	public double fuelMultiplyer;//reduces the amount of fuel per job by this percentage

	public double potentialTank;
	public int maxPotentialTank = 640000;

	public ItemStack[] inputStacks = new ItemStack[12];
	public ItemStack[] outputStacks = new ItemStack[12];
	public ItemStack[] upgradeStacks = new ItemStack[3];


	public void changeTargetFuel(boolean pos)
	{
		if(pos)
		{
			if(currentIndex < 7)
			{
				currentIndex++;
			}
			else
			{
				currentIndex = 0;
			}
		}
		else
		{
			if(currentIndex>0)
				currentIndex--;
			else
				currentIndex=7;
		}
	}
	
	public ItemStack getCurrentFuel()
	{
		return getCurrentFuel(currentIndex, false);
	}
	
	public ItemStack getCurrentFuel(boolean dust)
	{
		return getCurrentFuel(currentIndex, dust);
	}
	
	public ItemStack getCurrentFuel(int index)
	{
		return getCurrentFuel(index,false);
	}

	public ItemStack getCurrentFuel(int index, boolean dust)
	{
		switch(currentIndex)
		{
		case 0:
			return (dust)?(FuelTypes.CHARCOAL.getDust()):(FuelTypes.CHARCOAL.getItem());
		case 1:
			return (dust)?(FuelTypes.PEAT.getDust()):(FuelTypes.PEAT.getItem());
		case 2:
			return (dust)?(FuelTypes.LIGNITE.getDust()):(FuelTypes.LIGNITE.getItem());
		case 3:
			return (dust)?(FuelTypes.SUBBIT.getDust()):(FuelTypes.SUBBIT.getItem());
		case 4:
			return (dust)?(FuelTypes.BIT.getDust()):(FuelTypes.BIT.getItem());
		case 5:
			return (dust)?(FuelTypes.COAL.getDust()):(FuelTypes.COAL.getItem());
		case 6:
			return (dust)?(FuelTypes.ANTHRACITE.getDust()):(FuelTypes.ANTHRACITE.getItem());
		case 7:
			return (dust)?(FuelTypes.GRAPHITE.getDust()):(FuelTypes.GRAPHITE.getItem());
		default:
			return null;
		}
	}

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();

		inventoryChanged = true;
	}

	private boolean inventoryChanged;
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if(worldObj != null && !this.worldObj.isRemote)
		{
			if(craftingCooldown > 0)
				craftingCooldown--;
			if(craftingCooldown == 0)
				inventoryChanged = true;


			boolean action = false;

			action = handleUpgrade(action);
			action = handleInput(action);
			action = handlePotential(action);

			if(action)
			{
				onInventoryChanged();
			}
			else
				inventoryChanged=false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64, worldObj.provider.dimensionId, getDescriptionPacket());
		}
	}

	public boolean handleInput(boolean action)
	{
		if(inputStacks == null || outputStacks == null)
			return action;

		if(potentialTank < maxPotentialTank)//don't do anything if the potential tank is full
		{
			for(int i = 0; i < inputStacks.length; i++)
			{
				if(inputStacks[i]==null)
				{

				}
				else if(getPotentialTime(inputStacks[i])==0)
				{
					insertOutputItem(inputStacks[i],i);
					action = true;
				}
				else if(potentialTank+getPotentialTime(inputStacks[i])<=maxPotentialTank)
				{
					potentialTank += getPotentialTime(inputStacks[i]);
					inputStacks[i].stackSize -= 1;
					if(inputStacks[i].stackSize == 0)
						inputStacks[i]=null;
					action = true;
				}
			}
		}
		return action;
	}

	/*
	 * Looks through the upgrade slots and does the correct stuff with them
	 */
	public boolean handleUpgrade(boolean action)
	{
		if(upgradeStacks == null)
			return action;

		if(upgradeStacks[0] != null)//something in the speed upgrade slot
		{
			if(upgradeStacks[0].itemID == carbonization.itemStructureBlock.itemID)
			{
				if(upgradeStacks[0].getItemDamage() >= 2000)
				{
					double[] d = ItemStructureBlock.getTier(upgradeStacks[0].getItemDamage());
					double a = 1.0/3.0;
					speedUpgrade = (d[0]+d[1])/2*Math.pow(upgradeStacks[0].stackSize, a);
					action = true;
				}
				else
					speedUpgrade = 0;
			}
			else
				speedUpgrade = 0;
		}
		else
			speedUpgrade = 0;

		if(upgradeStacks[1] != null)
		{
			if(upgradeStacks[1].itemID == carbonization.itemStructureBlock.itemID)
			{
				if(upgradeStacks[1].getItemDamage() >= 1000 && upgradeStacks[1].getItemDamage() < 2000)
				{
					double[] d = ItemStructureBlock.getTier(upgradeStacks[1].getItemDamage());
					double a = 1.0/3.0;
					efficiencyUpgrade = (d[0]+d[1])/2*Math.pow(upgradeStacks[1].stackSize, a);
					action = true;
				}
				else
					efficiencyUpgrade = 0;
			}
			else
				efficiencyUpgrade = 0;
		}
		else
			efficiencyUpgrade = 0;

		if(upgradeStacks[2] != null)
		{
			if(checkItemBurnTime(upgradeStacks[2])>0)//it's a bit of fuel
			{
				if(addFuel(upgradeStacks[2]))
				{
					if(!(this.upgradeStacks[2].getItem() instanceof IFuelContainer))
					{
						if(upgradeStacks[2].stackSize>1)
							upgradeStacks[2].stackSize -= 1;
						else
							upgradeStacks[2] = null;
					}

					action = true;
				}
			}
		}
		calculateProcessTime();
		
		return action;
	}
	
	//handle crafting
	public boolean handlePotential(boolean action)
	{
		if(potentialTank == 0)
			return action;
		
		int time = getPotentialTime(getCurrentFuel(currentIndex));
		if(time==0 || craftingCooldown > 0)
			return action;
		if(time <= potentialTank && fuelTank >= getFuelUsage())
		{
			if(insertOutputItem(getCurrentFuel(makeDust).copy(),-1))
			{
				potentialTank -= time;
				fuelTank -= getFuelUsage();
				action = true;
				craftingCooldown = processTime;
				
			}
		}
		
		return action;
	}

	private int getPotentialTime(ItemStack is)
	{
		if(!(is.getItem() instanceof IFuelContainer))
		{
			if(checkItemBurnTime(is)>0)
				return checkItemBurnTime(is);
		}
		
		if(is.itemID == carbonization.dust.itemID)
		{
			switch(is.getItemDamage())
			{
			case 0:
				return 1600;
			case 1:
				return 600;
			case 2:
				return 800;
			case 3:
				return 1000;
			case 4:
				return 1200;
			case 5:
				return 1600;
			case 6:
				return 2000;
			case 7:
				return 333;
			}
		}
		
		return 0;
	}
	//return the scaled percentage of the fuel tank
	@SideOnly(Side.CLIENT)
	public int getFuelCapacityScaled(int i)
	{
		return (int) (this.getFuelStack()*i/(this.maxFuelCapacity+1));
	}

	@SideOnly(Side.CLIENT)
	public int getPotentialCapacityScaled(int i)
	{
		return (int) (potentialTank*i)/(maxPotentialTank);
	}
	
	public void swapDustState()
	{
		if(makeDust)
			makeDust = false;
		else
			makeDust = true;
	}

	//return the scaled percentage of the progress bar
	@SideOnly(Side.CLIENT)
	public int getCooldownScaled(int i)
	{
		//return (int)(0.2*i);
		return (int) (craftingCooldown*i/(processTime+1));
	}

	public double getFuelStack() {
		return fuelTank;
	}
	public void setFuelStack(double f) {
		this.fuelTank = f;
	}

	public double getPotentialTank()
	{
		return potentialTank;
	}

	public int getMaxPotentialTank()
	{
		return maxPotentialTank;
	}

	//figure out all the values here
	public void calculateProcessTime()
	{
		fuelMultiplyer = 1/(efficiencyUpgrade-0.01*speedUpgrade+1);
		potentialMultiplyer = 3.2/(efficiencyUpgrade-0.01*speedUpgrade+3.2);
		processTime = carbonization.MAXAUTOCRAFTTIME - (int) ((Math.pow(speedUpgrade,1.2)-0.01*efficiencyUpgrade)*(carbonization.MAXAUTOCRAFTTIME-carbonization.MINAUTOCRAFTTIME)/47.3);
		if(processTime < carbonization.MINAUTOCRAFTTIME)
			processTime = carbonization.MINAUTOCRAFTTIME;
	}
	
	public double getFuelUsage()
	{
		return carbonization.BASEAUTOCRAFTFUEL*(fuelMultiplyer)*(1-((makeDust)?(1):(0))*0.25);
	}

	//add fuel
	//if this would overfill the fuel storage, don't add any fuel
	//to preserve a fuel item
	public boolean addFuel(int time)
	{
		if(getFuelStack()+time<=maxFuelCapacity)
		{
			setFuelStack(getFuelStack() + time);
			return true;
		}
		return false;
	}

	//overloaded version for a fuel item itself
	public boolean addFuel(ItemStack item)
	{
		if(item == null)
			return false;
		int time = getItemBurnTime(item);
		if(time <= 0)
			return false;

		if(getFuelStack()+time<=maxFuelCapacity)
		{
			setFuelStack(getFuelStack() + time);
			return true;
		}

		return false;
	}

	//check to see if the output has empty space or a fitting slot for the input
	/**
	 * This will find the first slot that meets the criteria of either a slot with the same item that has room (any room, stack splitting
	 * is handled elsewhere) or an empty slot.
	 */
	public int hasOutputSpace(ItemStack input)
	{
		for(int i = 0; i<outputStacks.length; i++)
		{
			if(outputStacks[i] == null)
				return i;
			//see if the stack
			if((outputStacks[i].getItem() == input.getItem() && outputStacks[i].getItemDamage()== input.getItemDamage()) && outputStacks[i].stackSize < outputStacks[i].getMaxStackSize())
				return i;
		}
		return -1;
	}

	//place an itemstack in an output slot if possible
	public boolean insertOutputItem(ItemStack item, int index)
	{
		if(item != null)
		{
			int slot = hasOutputSpace(item);
			if(slot == -1)//there is no slot free, so just dump it
			{
				/*this.dumpItem(item);
  				if(index != -1)
  				{
  					inputStacks[index] = null;
  				}*/
				return false;
			}
			if(outputStacks[slot] == null)//the slot is empty
			{
				outputStacks[slot] = item;
				if(index != -1)
				{
					inputStacks[index] = null;
					//System.out.println("null powers activate!");
				}
				return true;
			}
			else
			{
				if(item.stackSize > 0)
				{
					if(outputStacks[slot].stackSize + item.stackSize <= outputStacks[slot].getMaxStackSize())//the two stacks fit together
					{
						outputStacks[slot].stackSize += item.stackSize;
						if(index != -1)
							inputStacks[index] = null;
						return true;
					}
					else//the total stack size is too large, so split the input stack and max the output stack
					{
						int diff = outputStacks[slot].getMaxStackSize() - outputStacks[slot].stackSize;
						outputStacks[slot].stackSize = outputStacks[slot].getMaxStackSize();
						item = item.splitStack(item.stackSize-diff);
						return insertOutputItem(item, index);
					}
				}
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setDouble("fuelTank", fuelTank);
		nbt.setDouble("speedUpgrade", speedUpgrade);
		nbt.setDouble("efficiencyUpgrade", efficiencyUpgrade);
		nbt.setInteger("processTime", processTime);
		nbt.setBoolean("makeDust", makeDust);
		nbt.setInteger("currentIndex", currentIndex);
		nbt.setDouble("potentialTank", potentialTank);

		NBTTagList input = new NBTTagList();

		for (int i = 0; i < this.inputStacks.length; ++i)
		{
			if (this.inputStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.inputStacks[i].writeToNBT(var4);
				input.appendTag(var4);
			}
		}
		nbt.setTag("inputItems", input);

		NBTTagList output = new NBTTagList();

		for (int i = 0; i < this.outputStacks.length; ++i)
		{
			if (this.outputStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.outputStacks[i].writeToNBT(var4);
				output.appendTag(var4);
			}
		}
		nbt.setTag("outputItems", output);

		NBTTagList upgrade = new NBTTagList();

		for (int i = 0; i < this.upgradeStacks.length; ++i)
		{
			if (this.upgradeStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.upgradeStacks[i].writeToNBT(var4);
				upgrade.appendTag(var4);
			}
		}
		nbt.setTag("upgradeItems", upgrade);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		fuelTank = nbt.getDouble("fuelTank");
		speedUpgrade = nbt.getDouble("speedUpgrade");
		efficiencyUpgrade = nbt.getDouble("efficiencyUpgrade");
		processTime = nbt.getInteger("processTime");
		makeDust = nbt.getBoolean("makeDust");
		currentIndex = nbt.getInteger("currentIndex");
		potentialTank = nbt.getDouble("potentialTank");

		NBTTagList input = nbt.getTagList("inputItems");
		for (int i = 0; i < input.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)input.tagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inputStacks.length)
			{
				this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		NBTTagList output = nbt.getTagList("outputItems");
		for (int i = 0; i < output.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)output.tagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.outputStacks.length)
			{
				this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		NBTTagList upgrade = nbt.getTagList("upgradeItems");
		for (int i = 0; i < upgrade.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)upgrade.tagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.upgradeStacks.length)
			{
				this.upgradeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		calculateProcessTime();
	}

	/*
	 * Will take all the inventory and save it to a NBT array to be loaded later.
	 * Unused right now, doesn't work right
	 */
	public NBTTagCompound saveInventory()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList input = new NBTTagList();

		for (int i = 0; i < this.inputStacks.length; ++i)
		{
			if (this.inputStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.inputStacks[i].writeToNBT(var4);
				input.appendTag(var4);
			}
		}
			
		nbt.setTag("inputItems", input);
			
		NBTTagList output = new NBTTagList();

    	for (int i = 0; i < this.outputStacks.length; ++i)
    	{
    		if (this.outputStacks[i] != null)
    		{
    			NBTTagCompound var4 = new NBTTagCompound();
    			var4.setByte("Slot", (byte)i);
    			this.outputStacks[i].writeToNBT(var4);
    			output.appendTag(var4);
    		}
    	}

        nbt.setTag("outputItems", output);
        
        NBTTagList upgrade = new NBTTagList();
		for(int i = 0; i < upgradeStacks.length; i++)
		{
			if(upgradeStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.upgradeStacks[i].writeToNBT(var4);
				output.appendTag(var4);
			}
		}
		nbt.setTag("Upgrade", upgrade);
        
        return nbt;
	}
	
	
	 //Will load up the inventory data
	 
	public void loadInventory(NBTTagCompound nbt)
	{
		NBTTagList input = nbt.getTagList("inputItems");
		for (int i = 0; i < input.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)input.tagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inputStacks.length)
            {
                this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList output = nbt.getTagList("outputItems");
		for (int i = 0; i < output.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)output.tagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.outputStacks.length)
            {
                this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList list = nbt.getTagList("Upgrade");
		for(int i = 0; i<list.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)list.tagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.upgradeStacks.length)
            {
                this.upgradeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(side == 0)//only pull things out or put fuel in
		{
			int[] bot = new int[outputStacks.length+1];
			for (int i = 0; i < bot.length-1; i++)
			{
				bot[i]=inputStacks.length+i;
			}
			bot[bot.length-1] = inputStacks.length+outputStacks.length+upgradeStacks.length;
			return bot;
		}
		else if(side == 1)//only put things in, no fuel
		{
			int[] top = new int[inputStacks.length];
			for (int i = 0; i < top.length; i++)
			{
				top[i]=i;
			}
			return top;
		}
		else//pull anything and fuel
		{
			int[] sid = new int[outputStacks.length+1];
			sid[0] = 26;
			for (int i = 1; i < sid.length; i++)
			{
				sid[i]=inputStacks.length+i-1;
			}
			return sid;
		}
	}

	/*
	 * checks to see if the item can be inserted into the slot by a machine
	 * since we don't care about which side stuff gets inserted into
	 * j doesn't matter
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int j) {
		return this.isItemValidForSlot(slot, itemstack);
	}

	/*
	 * checks to see if the item can be removed from the slot by a machine
	 * basically, if it's an output slot you can, otherwise not
	 * again, we don't care about sides, you can suck stuff from whereever
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		if(slot >= inputStacks.length && slot < getSizeInventory()-upgradeStacks.length)//output inventory
			return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inputStacks.length+outputStacks.length+upgradeStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot>=0 && slot<inputStacks.length)
			return inputStacks[slot];
		else if(slot<getSizeInventory()-upgradeStacks.length)
			return outputStacks[slot-inputStacks.length];
		else if(slot<getSizeInventory())
			return upgradeStacks[slot-inputStacks.length-outputStacks.length];
		return null;
	}

	/*
	 * decreases the amount of an item in a stack
	 * first has to find out which inventory it is
	 * then split the stack
	 */
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i>=0 && i<inputStacks.length)
		{
			if (inputStacks[i] != null)
			{
				ItemStack itemstack;

				if (inputStacks[i].stackSize <= j)
				{
					itemstack = inputStacks[i];
					this.inputStacks[i] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.inputStacks[i].splitStack(j);

					if (this.inputStacks[i].stackSize == 0)
					{
						this.inputStacks[i] = null;
					}

					return itemstack;
				}
			}
			else
			{
				return null;
			}
		}
		else if(i<getSizeInventory()-upgradeStacks.length)
		{
			i = i-inputStacks.length;
			if (outputStacks[i] != null)
			{
				ItemStack itemstack;

				if (outputStacks[i].stackSize <= j)
				{
					itemstack = outputStacks[i];
					this.outputStacks[i] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.outputStacks[i].splitStack(j);

					if (this.outputStacks[i].stackSize == 0)
					{
						this.outputStacks[i] = null;
					}

					return itemstack;
				}
			}
			else
			{
				return null;
			}
		}
		else if(i<getSizeInventory())
		{
			i = i-inputStacks.length-outputStacks.length;
			if (upgradeStacks[i] != null)
			{
				ItemStack itemstack;

				if (upgradeStacks[i].stackSize <= j)
				{
					itemstack = upgradeStacks[i];
					this.upgradeStacks[i] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.upgradeStacks[i].splitStack(j);

					if (this.upgradeStacks[i].stackSize == 0)
					{
						this.upgradeStacks[i] = null;
					}

					return itemstack;
				}
			}
			else
			{
				return null;
			}
		}		
		else
			return null;//outside the inventory
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot>=0 && slot < inputStacks.length)
		{
			if(inputStacks[slot] != null)
			{
				ItemStack var2 = inputStacks[slot];
				inputStacks[slot] = null;
				return var2;
			}
			return null;
		}
		else if(slot>= inputStacks.length && slot < this.getSizeInventory()-upgradeStacks.length)
		{
			if(outputStacks[slot-inputStacks.length] != null)
			{
				ItemStack var2 = outputStacks[slot-inputStacks.length];
				outputStacks[slot-inputStacks.length] = null;
				return var2;	
			}
			return null;
		}
		else if(slot < getSizeInventory())
		{
			if(upgradeStacks[slot-inputStacks.length-outputStacks.length] != null)
			{
				ItemStack var2 = upgradeStacks[slot-inputStacks.length-outputStacks.length];
				upgradeStacks[slot-inputStacks.length-outputStacks.length] = null;
				return var2;
			}
			return null;
		}
		else
			return null;
	}
	/*
	 * sets the inventory to the contents of the itemstack given
	 */
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		//System.out.println("put item: " + ((itemstack!=null)?itemstack.toString():"null") + " in slot " + i);
		if(i>=0 && i<inputStacks.length)
		{
			this.inputStacks[i] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		else if(i<getSizeInventory()-upgradeStacks.length)
		{
			this.outputStacks[i-inputStacks.length] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		else if(i<getSizeInventory())
		{
			this.upgradeStacks[i-inputStacks.length-outputStacks.length] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "autocraftingbench";
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot<inputStacks.length || slot == 26)
			if(getPotentialTime(itemstack) > 0)
				return true;
		return false;
	}

	//build an int list of the inventory
	public int[] buildIntList()
	{
		int[] list = new int[(inputStacks.length+outputStacks.length+upgradeStacks.length)*3+1];
		//System.out.println("Created list of size " + list.length);
		int pos = 0;
		for(ItemStack is : inputStacks)
		{
			if(is != null)
			{
				//System.out.println("Putting input item: " + is.toString() + " in list");
				list[pos++] = is.itemID;
				list[pos++] = is.getItemDamage();
				list[pos++] = is.stackSize;
			}
			else
			{
				//System.out.println("Putting null input item in list");
				list[pos++] = 0;
				list[pos++] = 0;
				list[pos++] = 0;
			}
		}
		for(ItemStack os : outputStacks)
		{
			if(os != null)
			{
				//System.out.println("Putting output item: " + os.toString() + " in list");
				list[pos++] = os.itemID;
				list[pos++] = os.getItemDamage();
				list[pos++] = os.stackSize;
			}
			else
			{
				//System.out.println("Putting null output item in list");
				list[pos++] = 0;
				list[pos++] = 0;
				list[pos++] = 0;
			}
		}

		for(ItemStack us : upgradeStacks)
		{
			if(us != null)
			{
				//System.out.println("Putting output item: " + os.toString() + " in list");
				list[pos++] = us.itemID;
				list[pos++] = us.getItemDamage();
				list[pos++] = us.stackSize;
			}
			else
			{
				//System.out.println("Putting null output item in list");
				list[pos++] = 0;
				list[pos++] = 0;
				list[pos++] = 0;
			}
		}

		return list;
	}

	public void recoverIntList(int[] items)
	{
		if(items != null)
		{
			int pos = 0;
			for(int i = 0; i < this.inputStacks.length; i++)
			{
				if(pos+2 < items.length && items[pos+2]!=0)
				{
					ItemStack is = new ItemStack(items[pos], items[pos+2], items[pos+1]);
					this.inputStacks[i]=is;
				}
				else
				{
					this.inputStacks[i] = null;
				}
				pos+= 3;
			}
			for(int i = 0; i < this.outputStacks.length; i++)
			{
				if(pos+2 < items.length && items[pos+2]!=0)
				{
					ItemStack is = new ItemStack(items[pos], items[pos+2], items[pos+1]);
					this.outputStacks[i]=is;
				}
				else
				{
					this.outputStacks[i] = null;
				}
				pos+= 3;
			}

			for(int i = 0; i < this.upgradeStacks.length; i++)
			{
				if(pos+2 < items.length && items[pos+2]!=0)
				{
					ItemStack is = new ItemStack(items[pos], items[pos+2], items[pos+1]);
					this.upgradeStacks[i]=is;
				}
				else
				{
					this.upgradeStacks[i] = null;
				}
				pos+= 3;
			}
		}
	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
	 * fuel
	 */
	public int getItemBurnTime(ItemStack par0ItemStack)
	{
		if (par0ItemStack == null)
		{
			return 0;
		}
		else if(par0ItemStack.getItem() instanceof IFuelContainer)
        {
        	//get the value
        	int fuelValue = ((IFuelContainer)par0ItemStack.getItem()).getFuelValue(par0ItemStack);
        	int value = (int) (maxFuelCapacity-fuelTank);
        	
        	//if it's a number, reduce it by some amount, we're using standard coal or the value, whichever is smaller
        	if(fuelValue == 0)
        		return 0;
        	else if(fuelValue >= value)
        	{
        		((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, -value, false);
        		return value;
        	}
        	else
        	{
        		((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, 0, true);
        		return fuelValue;
        	}
        }
		else
		{
			int var1 = par0ItemStack.getItem().itemID;
			Item var2 = par0ItemStack.getItem();

			if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
			{
				Block var3 = Block.blocksList[var1];

				if (var3 == Block.woodSingleSlab)
				{
					return 0;
				}

				if (var3.blockMaterial == Material.wood)
				{
					return 0;
				}

				if (var3 == Block.coalBlock)
				{
					return 16000;
				}
			}

			if (var1 == Item.stick.itemID) return 0;
			if (var1 == Item.coal.itemID) return 1600;
			if (var1 == Item.bucketLava.itemID) return 0;
			if (var1 == Block.sapling.blockID) return 0;
			if (var1 == Item.blazeRod.itemID) return 0;
			return GameRegistry.getFuelValue(par0ItemStack);
		}
	}
	
	 public int checkItemBurnTime(ItemStack par0ItemStack)
	    {
	        if (par0ItemStack == null)
	        {
	            return 0;
	        }
	        else if(par0ItemStack.getItem() instanceof IFuelContainer)
	        {
	        	//get the value
	        	int fuelValue = ((IFuelContainer)par0ItemStack.getItem()).getFuelValue(par0ItemStack);
	        	int value = (int) (maxFuelCapacity-fuelTank);
	        	
	        	//if it's a number, reduce it by some amount, we're using standard coal or the value, whichever is smaller
	        	if(fuelValue == 0)
	        		return 0;
	        	else if(fuelValue >= value)
	        	{
	        		return value;
	        	}
	        	else
	        	{
	        		return fuelValue;
	        	}
	        }
	        else
	        {
	            int var1 = par0ItemStack.getItem().itemID;
	            Item var2 = par0ItemStack.getItem();

	            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
	            {
	                Block var3 = Block.blocksList[var1];

	                if (var3 == Block.woodSingleSlab)
	                {
	                    return 150;
	                }

	                if (var3.blockMaterial == Material.wood)
	                {
	                    return 300;
	                }
	            }

	            if (var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD")) return 200;
	            if (var2 instanceof ItemSword && ((ItemSword) var2).getToolMaterialName().equals("WOOD")) return 200;
	            if (var2 instanceof ItemHoe && ((ItemHoe) var2).getMaterialName().equals("WOOD")) return 200;
	            if (var1 == Item.stick.itemID) return 100;
	            if (var1 == Item.coal.itemID) return 1600;
	            if (var1 == Item.bucketLava.itemID) return 20000;
	            if (var1 == Block.sapling.blockID) return 100;
	            if (var1 == Item.blazeRod.itemID) return 2400;
	            return GameRegistry.getFuelValue(par0ItemStack);
	        }
	    }


	public void activate(World world, int x, int y, int z,
			EntityPlayer player) {
		player.openGui(carbonization.instance, 4, world, x, y, z);

	}
	public void dumpInventory()
	{
		if(worldObj == null || worldObj.isRemote)
			return;
		//spit out all the inventory and convert fuel time into as much fuel as possible
		float var10;
		float var11;
		float var12;

		EntityPlayer player = worldObj.getClosestPlayer(xCoord, yCoord, zCoord, -1);

		double ppx = 0;
		double ppy = 0;
		double ppz = 0;
		if(player != null)
		{
			ppx = player.posX;
			ppy = player.posY;
			ppz = player.posZ;
		}
		float px = (float) (xCoord+ppx)/2;
		float py = (float) (yCoord+ppy)/2;
		float pz = (float) (zCoord+ppz)/2;
		//unprocessed items
		for(int i = 0; i<inputStacks.length; i++)
		{
			ItemStack item = inputStacks[i];

			if (item != null)
			{
				//System.out.println("input index: " + i);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);
			}

			inputStacks[i] = null;
		}
		//output inventory
		for(int j = 0; j<outputStacks.length; j++)
		{
			ItemStack item = outputStacks[j];

			if (item != null)
			{
				//System.out.println("output index: " + j);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);

			}

			outputStacks[j] = null;
		}

		//upgrade inventory
		for(int j = 0; j<upgradeStacks.length; j++)
		{
			ItemStack item = upgradeStacks[j];

			if (item != null)
			{
				//System.out.println("output index: " + j);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);

			}

			upgradeStacks[j] = null;
		}
		//fuel
		double tempFuel = getFuelStack();
		int numCoal = 0;
		int numPeat = 0;
		while (tempFuel > 0)
		{   
			if(tempFuel>=1600)
			{
				//add in a coal
				tempFuel -= 1600;
				numCoal++;
			}
			else if(tempFuel>=600)
			{
				//add in a peat
				tempFuel -= 600;
				numPeat++;
			}
			else//not enough fuel anymore
			{
				tempFuel = -1;
				setFuelStack(0);
			}
		}

		var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

		if(numCoal>0)
		{
			EntityItem eCoal = new EntityItem(worldObj,  (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(Item.coal, numCoal, 0));
			float var15 = 0.05F;
			eCoal.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
			eCoal.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(eCoal);
		}

		if(numPeat>0)
		{
			EntityItem ePeat = new EntityItem(worldObj,  (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(carbonization.fuel, numPeat, 0));
			float var15 = 0.05F;
			ePeat.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
			ePeat.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
			ePeat.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
			ePeat.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(ePeat);
		}
	}


	public void closeGui()
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			PacketDispatcher.sendPacketToServer(getDescriptionPacket());
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacket(this);
	}
}

/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/