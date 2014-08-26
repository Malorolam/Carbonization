package mal.carbonization.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.CarbonizationRecipeHandler;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.AutocraftingBenchMessageServer;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.FuelConversionBenchMessageClient;
import mal.carbonization.network.FuelConversionBenchMessageServer;
import mal.core.api.IMachineUpgrade;
import mal.core.reference.UtilReference;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFuelConversionBench extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory, IFluidHandler{

	private FluidTank tank = new FluidTank(carbonizationBlocks.fluidFuelPotential, 0, 4000);
	private Fluid tankFluid = carbonizationBlocks.fluidFuelPotential;
	public boolean fuelState = false;//false=solid->liquid, true=liquid->solid
	public int currentIndex = 0;
	public int craftingCooldown = 0;//cooldown until next process
	public int maxCooldown = carbonization.MAXAUTOCRAFTTIME;//max cooldown
	public double bonusYield = 0;
	public ItemStack[] inventoryStacks = new ItemStack[27];
	public ItemStack[] upgradeStacks = new ItemStack[3];
	private Random furnaceRand = new Random();
	
	//Lists
	//private HashMap<ItemStack, Integer> itemMap = new HashMap<ItemStack, Integer>();
	//private ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
	
	//upgrades
	public boolean voidUpgrade = false;
	public int ejectSide = -1;
	public int storageLevel = 0;
	public double fortuneLevel = 0;
	public double hasteLevel = 0;
	
	public TileEntityFuelConversionBench()
	{
		//updateLists();
	}
	/*
	 * Add all the recipes to the correct lists
	 */
/*	private void updateLists()
	{
		for(ItemStack i : CarbonizationRecipeHandler.smelting().getFuelConversionRegistry())
		{
			itemMap.put(i, CarbonizationRecipeHandler.smelting().getFuelConversionOutputCost(i));
			itemList.add(i);
		}
	}*/
	
	private boolean listContainsItem(ItemStack is)
	{	
		for(ItemStack ii : CarbonizationRecipeHandler.smelting().getFuelConversionRegistry())
		{
			if(ii.isItemEqual(is))
				return true;
		}
		return false;
	}
	
	private int getItemPotential(ItemStack is)
	{
		for(ItemStack ii : CarbonizationRecipeHandler.smelting().getFuelConversionRegistry())
		{
			if(ii.isItemEqual(is))
			{
				return CarbonizationRecipeHandler.smelting().getFuelConversionOutputCost(ii);
			}
		}
		return 0;
	}
	
	public ItemStack getCurrentItem()
	{
		return CarbonizationRecipeHandler.smelting().getFuelConversionRegistry().get(currentIndex);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CarbonizationPacketHandler.instance.getPacketFrom(new FuelConversionBenchMessageServer(this, true));
	}
	
	private int incrementIndex(boolean pos)
	{
		if(pos)
		{
			if(currentIndex < CarbonizationRecipeHandler.smelting().getFuelConversionRegistry().size()-1)
				return currentIndex+1;
			else
				return 0;
		}
		else
		{
			if(currentIndex > 0)
				return currentIndex-1;
			else
				return CarbonizationRecipeHandler.smelting().getFuelConversionRegistry().size()-1;
		}
	}
	
	public int getCurrentCost()
	{
		double multiplyer = (fuelState)?(1):(2*(1-bonusYield/2));
		if(CarbonizationRecipeHandler.smelting().getFuelConversionRegistry().contains(getCurrentItem()))
			return (int)Math.ceil(CarbonizationRecipeHandler.smelting().getFuelConversionOutputCost(getCurrentItem())*multiplyer);
		return -1;
	}
	
	public void setState(boolean newState, int index)
	{
		fuelState = newState;
		if(index != -1)
			currentIndex = index;
	}
	
	public void closeGui(int effect)
	{
		if(worldObj.isRemote)
		{
			boolean changeState = fuelState;
			int index = -1;
			if(effect==-1)
				changeState = !fuelState;
			else if(effect==1)
				index = incrementIndex(true);
			else
				index = incrementIndex(false);
			CarbonizationPacketHandler.instance.sendToServer(new FuelConversionBenchMessageClient(this, changeState, index));
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tank.writeToNBT(nbt);
		nbt.setBoolean("fuelState", fuelState);
		nbt.setInteger("currentIndex", currentIndex);
		nbt.setInteger("craftingCooldown", craftingCooldown);
		nbt.setInteger("maxCooldown", maxCooldown);
		nbt.setDouble("bonusYield", bonusYield);
		
		NBTTagList input = new NBTTagList();
		for (int i = 0; i < this.inventoryStacks.length; ++i)
		{
			if (this.inventoryStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.inventoryStacks[i].writeToNBT(var4);
				input.appendTag(var4);
			}
		}
		nbt.setTag("inventoryStack", input);
		
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
		nbt.setTag("upgradeStack", upgrade);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		fuelState = nbt.getBoolean("fuelState");
		currentIndex = nbt.getInteger("currentIndex");
		craftingCooldown = nbt.getInteger("craftingCooldown");
		maxCooldown = nbt.getInteger("maxCooldown");
		bonusYield = nbt.getDouble("bonusYield");
		
		NBTTagList input = nbt.getTagList("inventoryStack", 10);
		for (int i = 0; i < input.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)input.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inventoryStacks.length)
			{
				this.inventoryStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		
		NBTTagList upgrade = nbt.getTagList("upgradeStack", 10);
		for (int i = 0; i < upgrade.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)upgrade.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.upgradeStacks.length)
			{
				this.upgradeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(worldObj != null && !worldObj.isRemote)
		{
			if(craftingCooldown>0)
				craftingCooldown--;
			
			handleUpgrade();
			//System.out.println(fuelState);
			if(fuelState)
				handleInventory();
			else
				handlePotential();
			handleExtraction();
			
			CarbonizationPacketHandler.instance.sendToAll(new FuelConversionBenchMessageServer(this, false));
		}
	}
	
	//Handle the upgrade slots
	private void handleUpgrade()
	{
		if(upgradeStacks == null)
			return;
		
		voidUpgrade = false;
		ejectSide = -1;
		fortuneLevel = 0;
		storageLevel = 0;
		hasteLevel = 0;
		
		for(int i = 0; i < upgradeStacks.length; i++)
		{
			if(upgradeStacks[i] == null)
			{
				
			}
			else if(upgradeStacks[i].getItem() instanceof IMachineUpgrade)
			{
				int damage = upgradeStacks[i].getItemDamage();
				String upgrade = ((IMachineUpgrade)upgradeStacks[i].getItem()).getUpgradeName(damage);
				
				if(upgrade.equalsIgnoreCase("void1"))
					voidUpgrade = true;
				else if(upgrade.equalsIgnoreCase("ejection0"))
					ejectSide=0;
				else if(upgrade.equalsIgnoreCase("ejection1"))
					ejectSide=1;
				else if(upgrade.equalsIgnoreCase("ejection2"))
					ejectSide=2;
				else if(upgrade.equalsIgnoreCase("ejection3"))
					ejectSide=3;
				else if(upgrade.equalsIgnoreCase("ejection4"))
					ejectSide=4;
				else if(upgrade.equalsIgnoreCase("ejection5"))
					ejectSide=5;
				else if(upgrade.equalsIgnoreCase("fortune1"))
					fortuneLevel += 1;
				else if(upgrade.equalsIgnoreCase("fortune2"))
					fortuneLevel += 2;
				else if(upgrade.equalsIgnoreCase("fortune3"))
					fortuneLevel += 3;
				else if(upgrade.equalsIgnoreCase("fortune4"))
					fortuneLevel += 4;
				else if(upgrade.equalsIgnoreCase("fortune5"))
					fortuneLevel += 5;
				else if(upgrade.equalsIgnoreCase("storage1"))
					storageLevel += 1;
				else if(upgrade.equalsIgnoreCase("storage2"))
					storageLevel += 2;
				else if(upgrade.equalsIgnoreCase("storage3"))
					storageLevel += 3;
				else if(upgrade.equalsIgnoreCase("storage4"))
					storageLevel += 4;
				else if(upgrade.equalsIgnoreCase("storage5"))
					storageLevel += 5;
				else if(upgrade.equalsIgnoreCase("haste1"))
					hasteLevel += ((1-hasteLevel)*0.1);
				else if(upgrade.equalsIgnoreCase("haste2"))
					hasteLevel += ((1-hasteLevel)*0.3);
				else if(upgrade.equalsIgnoreCase("haste3"))
					hasteLevel += ((1-hasteLevel)*0.5);
				else if(upgrade.equalsIgnoreCase("haste4"))
					hasteLevel += ((1-hasteLevel)*0.7);
				else if(upgrade.equalsIgnoreCase("haste5"))
					hasteLevel += ((1-hasteLevel)*0.9);
			}
		}
		
		tank.setCapacity(FluidContainerRegistry.BUCKET_VOLUME*(4+storageLevel)*10);
		maxCooldown = (int)Math.ceil(carbonization.MAXAUTOCRAFTTIME*(1-hasteLevel));
		if(maxCooldown < carbonization.MINAUTOCRAFTTIME)
			maxCooldown = carbonization.MINAUTOCRAFTTIME;
		bonusYield = 0.07*fortuneLevel;
	}
	
	//Handle the process of turning inventory stuff into FP
	private void handleInventory()
	{
		if(craftingCooldown>0 || inventoryStacks == null)
			return;
		
		for(int i = 0; i < inventoryStacks.length; i++)
		{
			if(inventoryStacks[i] == null)
			{}
			else if(listContainsItem(inventoryStacks[i]))
			{
				int fp = (int)Math.floor(getItemPotential(inventoryStacks[i])*(1+bonusYield));
				//System.out.println(i + ": " + inventoryStacks[i].getDisplayName() + ": " + fp);
				if(fp>0 && (tank.getCapacity()>=tank.getFluidAmount()+fp || voidUpgrade))
				{
					inventoryStacks[i].stackSize-=1;
				
					if(inventoryStacks[i].stackSize<=0)
						inventoryStacks[i]=null;
					if(!voidUpgrade)
						tank.fill(new FluidStack(tankFluid, fp), true);
					
					craftingCooldown = maxCooldown;
					return;
				}
			}
		}
	}
	
	//Handle the process of turning FP into items
	private void handlePotential()
	{
		if(craftingCooldown>0 || inventoryStacks==null)
			return;
		
		int cost = getCurrentCost();
		
		if(cost <0)
			return;
		if(tank.getFluidAmount()>=cost)
		{
			ItemStack is = getCurrentItem();
			is.stackSize = 1;
			
			int slot = hasOutputSpace(is);
			if(slot != -1)
				if(insertOutputItem(is, slot))
				{
					tank.drain(cost, true);
					craftingCooldown = maxCooldown;
				}
		}
	}
	
	private void handleExtraction()
	{
	  		if(ejectSide != -1)
	  		{
	  			if(!fuelState)
	  			{
	  				IInventory te = findExternalInventory();
	  				ItemStack ii = null;
	  				if(te != null)
	  				{
	  					for(int i = 0; i < inventoryStacks.length; i++)
	  					{
	  						ItemStack item = inventoryStacks[i];
	  						if(item != null)
	  						{
	  							ii = UtilReference.insertStack(te, item, 1);
	  							inventoryStacks[i] = ii;
	  						}
	  					}
	  				}
	  			}
	  			else
	  			{
	  				IFluidHandler fe = findExternalFluidStorage();
	  				if(fe != null && tank.getFluidAmount()>0)
	  				{
	  					ForgeDirection direction=ForgeDirection.UP;
	  					switch(ejectSide)
	  					{
	  					case 0:
	  						direction = ForgeDirection.UP;
	  						break;
	  					case 1:
	  						direction = ForgeDirection.DOWN;
	  						break;
	  					case 2:
	  						direction = ForgeDirection.SOUTH;
	  						break;
	  					case 3:
	  						direction = ForgeDirection.EAST;
	  						break;
	  					case 4:
	  						direction = ForgeDirection.NORTH;
	  						break;
	  					case 5:
	  						direction = ForgeDirection.WEST;
	  						break;
	  					}
	  					int fill = fe.fill(direction, new FluidStack(tankFluid, tank.getFluidAmount()), true);
	  					tank.drain(fill, true);
	  				}
	  			}
	  		}
	}
	
	/**
  	 * This will find the first slot that meets the criteria of either a slot with the same item that has room (any room, stack splitting
  	 * is handled elsewhere) or an empty slot.
  	 */
  	public int hasOutputSpace(ItemStack input)
  	{
  		for(int i = 0; i<inventoryStacks.length; i++)
  		{
  			if(inventoryStacks[i] == null)
  				return i;
  			//see if the stack
  			if(UtilReference.areItemStacksEqualItem(inventoryStacks[i], input, true, false) && inventoryStacks[i].stackSize+input.stackSize <= inventoryStacks[i].getMaxStackSize())
  				return i;
  		}
  		return (voidUpgrade)?(-2):-1;
  	}
  	
  	/**
  	 * Find an external inventory to extract to
  	 */
  	private IInventory findExternalInventory()
  	{
  		if(ejectSide == -1)
  			return null;
  		
  		TileEntity te = null;
  		
  		switch(ejectSide)
		{
			case 0:
				te = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
			case 1:
				te = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
			case 2:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
			case 3:
				te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
			case 4:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
			case 5:
				te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
				if((te instanceof IInventory))
					return (IInventory) te;
				break;
		}
  		return null;
  	}
  	
  	/**
  	 * Find an external inventory to extract to
  	 */
  	private IFluidHandler findExternalFluidStorage()
  	{
  		if(ejectSide == -1)
  			return null;
  		
  		TileEntity te = null;
  		
  		switch(ejectSide)
		{
			case 0:
				te = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
			case 1:
				te = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
			case 2:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
			case 3:
				te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
			case 4:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
			case 5:
				te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
				if((te instanceof IFluidHandler))
					return (IFluidHandler) te;
				break;
		}
  		return null;
  	}
  	
  	//place an itemstack in an output slot if possible
  	public boolean insertOutputItem(ItemStack item, int index)
  	{
  		if(item != null)
  		{
  			int slot = hasOutputSpace(item);
  			if(slot == -1 || voidUpgrade)//there is no slot free, so just dump it
  			{
  				//void upgrade will dump stuff into the VOID dun Dun DUNNN!!
  				if(voidUpgrade)
  					return true;
  				else
  					return false;
  			}
  			if(inventoryStacks[slot] == null)//the slot is empty
  			{
  				inventoryStacks[slot] = item.copy();
  				return true;
  			}
  			else
  			{
  				if(item.stackSize > 0)
  				{
  					if(inventoryStacks[slot].stackSize + item.stackSize <= inventoryStacks[slot].getMaxStackSize())//the two stacks fit together
  					{
  						inventoryStacks[slot].stackSize += item.stackSize;
  						return true;
  					}
  					else//the total stack size is too large, so split the input stack and max the output stack
  					{
  						int diff = inventoryStacks[slot].getMaxStackSize() - inventoryStacks[slot].stackSize;
  						inventoryStacks[slot].stackSize = inventoryStacks[slot].getMaxStackSize();
  						item = item.splitStack(item.stackSize-diff);
  						return insertOutputItem(item, index);
  					}
  				}
  			}
  		}
  		return false;
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
		for(int i = 0; i<inventoryStacks.length; i++)
		{
			ItemStack item = inventoryStacks[i];

			if (item != null)
			{
				//System.out.println("input index: " + i);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

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

			inventoryStacks[i] = null;
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

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

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
		int numCoal = (int) Math.floor(tempFuel/1600);

		var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

		if(numCoal>0)
		{
			EntityItem eCoal = new EntityItem(worldObj,  (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(Items.coal, numCoal, 0));
			float var15 = 0.05F;
			eCoal.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
			eCoal.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(eCoal);
		}
	}

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { tank.getInfo() };
    }
    
  //return the scaled percentage of the fuel tank
  	@SideOnly(Side.CLIENT)
  	public int getFuelCapacityScaled(int i)
  	{
  		return (int) Math.ceil(this.getFuelStack()*i/(tank.getCapacity()));
  	}
  	
  	//return the scaled percentage of the progress bar
  	@SideOnly(Side.CLIENT)
  	public int getCooldownScaled(int i)
  	{
  		//return (int)(0.2*i);
  		return (int) (craftingCooldown*i/(maxCooldown+1));
  	}
	
    public int getFuelStack()
    {
    	return tank.getFluidAmount();
    }
    
    public void setFuelStack(int i)
    {
    	tank.setFluid(new FluidStack(tankFluid, i));
    }
    
	public int getMaxCapacity()
	{
		return tank.getCapacity();
	}

	public void setMaxCapacity(int capacity)
	{
		tank.setCapacity(capacity);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] i = new int[inventoryStacks.length];
		for(int j = 0; j < i.length; j++)
			i[j]=j;
		return i;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack is, int j) {
		if(slot>= 0 && slot<inventoryStacks.length)
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack is, int side) {
		if(slot>= 0 && slot<inventoryStacks.length)
			return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inventoryStacks.length+upgradeStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot >= 0 && slot<inventoryStacks.length)
		{
			//System.out.println(slot + ": " + ((inventoryStacks[slot]==null)?("null"):(inventoryStacks[slot].getDisplayName())));
			return inventoryStacks[slot];
		}
		else if(slot<getSizeInventory())
			return upgradeStacks[slot-inventoryStacks.length];
		else
			return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int dec) {
		if(slot>=0 && slot<inventoryStacks.length)
		{
			if(inventoryStacks[slot]==null)
				return null;
			
			ItemStack itemstack;

			if (inventoryStacks[slot].stackSize <= dec)
			{
				itemstack = inventoryStacks[slot];
				inventoryStacks[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = inventoryStacks[slot].splitStack(dec);

				if (inventoryStacks[slot].stackSize == 0)
				{
					inventoryStacks[slot] = null;
				}

				return itemstack;
			}

		}
		else if(slot<getSizeInventory())
		{
			slot = slot-inventoryStacks.length;
			
			if(upgradeStacks[slot] == null)
				return null;
			
			ItemStack itemstack;
			
			if(upgradeStacks[slot].stackSize <= dec)
			{
				itemstack = upgradeStacks[slot];
				upgradeStacks[slot]=null;
				return itemstack;
			}
			else
			{
				itemstack = upgradeStacks[slot].splitStack(dec);
				
				if(upgradeStacks[slot].stackSize == 0)
					upgradeStacks[slot]=null;
				
				return itemstack;
			}
		}
		else
			return null;
		
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot >= 0 && slot<inventoryStacks.length)
		{
			ItemStack is = inventoryStacks[slot];
			inventoryStacks[slot]=null;
			return is;
		}
		else if(slot<getSizeInventory())
		{
			ItemStack is = upgradeStacks[slot-inventoryStacks.length];
			upgradeStacks[slot-inventoryStacks.length] = null;
			return is;
		}
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		if(slot>=0 && slot<inventoryStacks.length)
			inventoryStacks[slot] = is;
		else if(slot<getSizeInventory())
			upgradeStacks[slot-inventoryStacks.length] = is;
		markDirty();
	}

	@Override
	public String getInventoryName() {
		return "FuelConversionBench";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		return (slot>=0&&slot<inventoryStacks.length)?(true):(false);
	}

	public void activate(World world, int x, int y, int z, EntityPlayer player) {
		player.openGui(carbonization.carbonizationInstance, 3, world, x, y, z);
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