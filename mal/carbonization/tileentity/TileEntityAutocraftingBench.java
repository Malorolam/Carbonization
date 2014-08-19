package mal.carbonization.tileentity;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.core.api.IFuelContainer;
import mal.core.api.IMachineUpgrade;
import mal.carbonization.CarbonizationRecipeHandler;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.item.ItemStructureBlock;
import mal.carbonization.network.AutocraftingBenchMessageClient;
import mal.carbonization.network.AutocraftingBenchMessageServer;
import mal.carbonization.network.AutocraftingBenchMessageServerSync;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.core.reference.UtilReference;
import mal.core.multiblock.MultiblockWorkQueueItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
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

public class TileEntityAutocraftingBench extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory, IFluidHandler
{

	/**
	 * Is the random generator used by furnace to drop the inventory contents in random directions.
	 */
	private Random furnaceRand = new Random();
	
	private FluidTank tank = new FluidTank(carbonizationBlocks.fluidFuelPotential, 0, 6400);

	public ItemStack[] inputStacks = new ItemStack[12];
	public ItemStack[] outputStacks = new ItemStack[12];
	public ItemStack[] recipeStacks = new ItemStack[10];
	public ItemStack[] upgradeStacks = new ItemStack[3];

	public int fuelUsePercent;//percentage of fuel used per process
	public int processTime = carbonization.MAXAUTOCRAFTTIME;//time to process an item, dependant on fuel usage
	public int fuelUsage = carbonization.BASEAUTOCRAFTFUEL;
	public int craftingCooldown; //amount of time before a new item can be crafted
	
	public boolean updating = false;//disable things because a packet is doing things
	private boolean inventoryChanged = true;//recheck the inventories
	public boolean disableButtons = false;//set to true when the buttons wouldn't do anything (like max time = min time)
	
	//upgrade flags
	public boolean voidUpgrade = false;
	public int ejectSide = 0;//standard side to try to dump items into an inventory
	public double effiencyLevel = 0;//x/100 percent reduction in fuel consumption
	public int storageLevel = 0;//+1 bucket per level

	public TileEntityAutocraftingBench()
	{
		if(carbonization.MAXAUTOCRAFTTIME==carbonization.MINAUTOCRAFTTIME)
			disableButtons = true;
	}
	
	public void activate(World world, int x, int y, int z,
			EntityPlayer player) {
		//System.out.println("Component Tiers: " + componentTiers[0] + ", " + componentTiers[1] +"; queue capacity: " + queue.maxJobs + "; activated: " + properlyActivated);
		player.openGui(carbonization.carbonizationInstance, 3, world, x, y, z);
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		
		inventoryChanged = true;
	}
	public void changeFuelUsage(boolean positive)
	{
		if(positive)
		{
			if(fuelUsePercent < 100)
				fuelUsePercent += 5;
		}
		else
		{
			if(fuelUsePercent > 0)
				fuelUsePercent -= 5;
		}
		calculateProcessTime();
	}

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
			handleExtraction();
			
			//if(action)
			{
				markDirty();
			}
			//else
				//inventoryChanged=false;
			CarbonizationPacketHandler.instance.sendToAll(new AutocraftingBenchMessageServerSync(this));
		}
	}
	
	/*
	 * Looks through the upgrade slots and does the correct stuff with them
	 */
	public boolean handleUpgrade(boolean action)
	{
		if(upgradeStacks == null)
			return action;
		
		voidUpgrade = false;
		ejectSide = -1;
		effiencyLevel = 0;
		storageLevel = 0;
		
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
				else if(upgrade.equalsIgnoreCase("efficiency1"))
					effiencyLevel += (int) Math.floor((100-effiencyLevel)*0.1);
				else if(upgrade.equalsIgnoreCase("efficiency2"))
					effiencyLevel += (int) Math.floor((100-effiencyLevel)*0.3);
				else if(upgrade.equalsIgnoreCase("efficiency3"))
					effiencyLevel += (int) Math.floor((100-effiencyLevel)*0.5);
				else if(upgrade.equalsIgnoreCase("efficiency4"))
					effiencyLevel += (int) Math.floor((100-effiencyLevel)*0.7);
				else if(upgrade.equalsIgnoreCase("efficiency5"))
					effiencyLevel += (int) Math.floor((100-effiencyLevel)*0.9);
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
				action=true;
			}
		}
		
		tank.setCapacity(FluidContainerRegistry.BUCKET_VOLUME*(4+storageLevel));
		fuelUsage = (int) Math.ceil(carbonization.BASEAUTOCRAFTFUEL*(1-effiencyLevel/100));
		return action;
	}
	
	private void handleExtraction()
	{
	  		if(ejectSide != -1)
	  		{
	  			IInventory te = findExternalInventory();
	  			ItemStack ii = null;
	  			if(te != null)
	  			{
	  				for(int i = 0; i < outputStacks.length; i++)
	  				{
	  					ItemStack item = outputStacks[i];
	  					if(item != null)
	  					{
	  						ii = UtilReference.insertStack(te, item, 1);
	  						outputStacks[i] = ii;
	  					}
	  				}
	  			}
	  		}
	}
	
	/*
     * goes through the input stacks and runs appropriate functions for what they are
     * fuel gets turned into fuel time if it can
     * items with no recipe get put directly into the output if possible
     * items with recipe is queued to be processed
     */
	private int[] toolIndex = new int[9];//index of the tools if any
	private boolean[] toolInInventory = new boolean[9];//if there is a tool in the inventory, use that instead
	private int[] isTool = new int[9];//-1 is for normal items, 0 is for non-tool items that have something stay in crafting, 1 is tools

    public boolean handleInput(boolean action)
    {
    	if(inputStacks == null || outputStacks == null)
    		return action;//assume we just got a random update where the te isn't initialized yet
    	
    	boolean lookforcrafting = false;
    	int craftmaterialcount = 0;//initiate a craft when this reaches the max value
    	int maxmaterialcount = 9;

    	if(hasRecipe() && canCraft() && inventoryChanged)
    	{
    		lookforcrafting = true;
    
    		//preload empty spaces and tools/buckets/other things that stay in the crafting grid
    		for(int i = 0; i < 9; i++)
    		{
				toolIndex[i] = -1;
				isTool[i] = -1;
				toolInInventory[i]=false;
				
    			if(recipeStacks[i]==null)
    				craftmaterialcount++;
    			else if(recipeStacks[i].getItem().hasContainerItem(recipeStacks[i]) || !recipeStacks[i].getItem().doesContainerItemLeaveCraftingGrid(recipeStacks[i]))
    			{
    				toolIndex[i] = i;
    				//System.out.println("set tool index to crafting location " + i);
    				if(recipeStacks[i].getItem().isItemTool(recipeStacks[i]))
    				{
    					maxmaterialcount--;//we have a tool, so  use that if necessary
    					isTool[i] = 1;
    				}
    				else
    					isTool[i] = 0;
    			}
    		}
    	}
    	
    	ItemStack[] recipe = new ItemStack[9];//temporary itemstack for recipe items
    	
    	for(int i = 0; i<inputStacks.length; i++)
    	{
    		if(inputStacks[i]==null || inputStacks[i].stackSize <= 0)
    		{
    			//do nada
    		}
    		else if(lookforcrafting && hasOutputSpace(recipeStacks[9])!= -1)//there is a valid recipe and space
    		{
    			//look for items that match the rest of the recipe and store the itemstacks
    			for(int j = 0; j<9; j++)
    			{
    				if(recipe[j]==null)
    				{
    					if(recipeStacks[j] == null || inputStacks[i] == null)
    					{
    						
    					}
    					else if(Item.getIdFromItem(recipeStacks[j].getItem()) == Item.getIdFromItem(inputStacks[i].getItem()))
    					{
    						if(toolInInventory[j]==false && isTool[j] != -1)//we haven't found a tool in the input for this one yet
    						{
    		    				//System.out.println("set tool index to inventory location " + i);
								toolInInventory[j] = true;
								//craftmaterialcount++;
								
								recipe[j] = inputStacks[i].splitStack(1);
    							if(inputStacks[i].stackSize <= 0)
    							{
								//System.out.println("null powers activate!");
    								inputStacks[i] = null;
    							}
    								
    						}
    						else if(toolInInventory[j] == true && isTool[j] != -1)
    						{
    							
    						}
    						else if(recipeStacks[j].getItemDamage() == inputStacks[i].getItemDamage() || recipeStacks[j].getHasSubtypes())
    						{
    							craftmaterialcount++;
    							recipe[j] = inputStacks[i].splitStack(1);
    							if(inputStacks[i].stackSize <= 0)
    							{
								//System.out.println("null powers activate!");
    								inputStacks[i] = null;
    							}
    						}
    					}
    				}
    			}
    			//see if we succeed on the crafting on this pass
    			if(craftmaterialcount==9)
    				lookforcrafting = false;
    		}
    	}

    	if(craftmaterialcount >= maxmaterialcount)
    	{
    		if(craftItem())//crafting succeeded
    		{
    			
    			action = true;

    			craftingCooldown = processTime;
    			tank.drain(getFuelUsage(), true);
    			for(int i = 0; i < 9; i++)
    			{
	    			if(toolIndex[i] == i && isTool[i] != -1)
	    			{
	    				if(toolInInventory[i]==false)
	    				{
	    					recipeStacks[toolIndex[i]] = recipeStacks[toolIndex[i]].getItem().getContainerItem(recipeStacks[toolIndex[i]]);
	    					if(recipeStacks[toolIndex[i]].getItemDamage() > recipeStacks[toolIndex[i]].getMaxDamage())
	    					{
	    						//System.out.println("removed item in crafting slot " + toolIndex[i]);
	    						recipeStacks[i] = null;
	    						recipeStacks[9] = null;//since we're removing a crafting item, the recipe isn't valid anymore
	    					}
	    				}
	    				else
	    				{
	    					recipe[toolIndex[i]] = (recipe[toolIndex[i]].getItem().getContainerItem(recipe[i]));
	    					if(isTool[i]==1)
	    					{
	    						if(recipe[toolIndex[i]].getItemDamage() > recipe[toolIndex[i]].getMaxDamage())
	    						{
	    							//System.out.println("removed item in inventory slot " + i);
	    							recipe[toolIndex[i]] = null;
	    						}
	    					}
	    					
	    					insertInputItem(recipe[toolIndex[i]]);
	    				}
	    			}
    			}
    		}
    		else//return the items to the inventory
    		{
    			for(int i = 0; i<9; i++)
    			{
    				if(recipe[i] != null)
    					insertInputItem(recipe[i]);
    			}
    			action = true;
    		}
    	}
    	else
    	{
			for(int i = 0; i<9; i++)
			{
				if(recipe[i] != null)
					insertInputItem(recipe[i]);
			}
			action = true;
    	}
    	return action;
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
			if(this.upgradeStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.upgradeStacks[i].writeToNBT(var4);
				upgrade.appendTag(var4);
			}
		}
		nbt.setTag("Upgrade", upgrade);
        
        return nbt;
	}
	
	
	 //Will load up the inventory data
	 
	public void loadInventory(NBTTagCompound nbt)
	{
		NBTTagList input = nbt.getTagList("inputItems", 10);
		for (int i = 0; i < input.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)input.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inputStacks.length)
            {
                this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList output = nbt.getTagList("outputItems", 10);
		for (int i = 0; i < output.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)output.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.outputStacks.length)
            {
                this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList list = nbt.getTagList("Upgrade", 10);
		for(int i = 0; i<list.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)list.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.upgradeStacks.length)
            {
                this.upgradeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
	}
	
    private boolean canCraft() {
		if(craftingCooldown > 0 || tank.getFluidAmount()<getFuelUsage())
			return false;
		return true;
	}

	private boolean hasRecipe() {
		return (recipeStacks[9] != null);
	}
    
    /*
     * Actually make the item and put it in the output if possible
     */
    private boolean craftItem()
    {
    	if(recipeStacks[9] == null)
    		return false;
    	
    	return insertOutputItem(recipeStacks[9].copy(), -1);
    }
    
  //add fuel
  	//if this would overfill the fuel storage, don't add any fuel
  	//to preserve a fuel item
  	public boolean addFuel(int time)
  	{
  		if(getFuelStack()+time<=tank.getCapacity())
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
  		int time = UtilReference.getItemBurnTime(item,(int) (tank.getCapacity()-tank.getFluidAmount()),true);
  		if(time <= 0)
  			return false;
  		
  		if(getFuelStack()+time<=tank.getCapacity())
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
/*  		if(ejectSide != -1)
  		{
  			IInventory te = findExternalInventory();
  			if(te != null)
  			{
  				return ejectSide;
  			}
  		}*/
  		for(int i = 0; i<outputStacks.length; i++)
  		{
  			if(outputStacks[i] == null)
  				return i;
  			//see if the stack
  			if((outputStacks[i].getItem() == input.getItem() && outputStacks[i].getItemDamage()== input.getItemDamage()) && outputStacks[i].stackSize+input.stackSize <= outputStacks[i].getMaxStackSize())
  				return i;
  		}
  		return (voidUpgrade)?(-2):-1;
  	}
  	
  	/**
  	 * 
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
  	
  //check to see if the input has empty space or a fitting slot for the input
  	/**
  	 * This will find the first slot that meets the criteria of either a slot with the same item that has room (any room, stack splitting
  	 * is handled elsewhere) or an empty slot.
  	 */
  	public int hasInputSpace(ItemStack input)
  	{
  		for(int i = 0; i<inputStacks.length; i++)
  		{
  			if(inputStacks[i] == null)
  				return i;
  			//see if the stack
  			if((inputStacks[i].getItem() == input.getItem() && inputStacks[i].getItemDamage()== input.getItemDamage()) && inputStacks[i].stackSize < inputStacks[i].getMaxStackSize())
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
  			if(slot == -1 || voidUpgrade)//there is no slot free, so just dump it
  			{
  				//void upgrade will dump stuff into the VOID dun Dun DUNNN!!
  				if(voidUpgrade)
  					return true;
  				else
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
  	
  //place an itemstack in an output slot if possible
  	public boolean insertInputItem(ItemStack item)
  	{
  		if(item != null)
  		{
  			int slot = hasInputSpace(item);
  			if(slot == -1)//there is no slot free, so just dump it
  			{
  				/*this.dumpItem(item);
  				if(index != -1)
  				{
  					inputStacks[index] = null;
  				}*/
  				return false;
  			}
  			if(inputStacks[slot] == null)//the slot is empty
  			{
  				inputStacks[slot] = item;
  				return true;
  			}
  			else
  			{
  				if(item.stackSize > 0)
  				{
  					if(inputStacks[slot].stackSize + item.stackSize <= inputStacks[slot].getMaxStackSize())//the two stacks fit together
  					{
  						inputStacks[slot].stackSize += item.stackSize;
  						return true;
  					}
  					else//the total stack size is too large, so split the input stack and max the output stack
  					{
  						int diff = inputStacks[slot].getMaxStackSize() - inputStacks[slot].stackSize;
  						inputStacks[slot].stackSize = inputStacks[slot].getMaxStackSize();
  						item = item.splitStack(item.stackSize-diff);
  						return insertInputItem(item);
  					}
  				}
  			}
  		}
  		return false;
  	}

	
	public int getFuelUsagePercent()
	{
		return fuelUsePercent;
	}

	public void calculateProcessTime()
	{
		processTime = (int)Math.floor(carbonization.MAXAUTOCRAFTTIME-(carbonization.MAXAUTOCRAFTTIME-carbonization.MINAUTOCRAFTTIME)*fuelUsePercent/100);
		if(processTime < 1)
			processTime = 1;
	}

	public int getFuelUsage()
	{
		return (int) Math.ceil(fuelUsage*((double)fuelUsePercent/100));
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

			outputStacks[j] = null;
		}
		//recipe inventory
		for(int j = 0; j<9; j++)
		{
			ItemStack item = recipeStacks[j];

			if (item != null)
			{
				//System.out.println("recipe index: " + j);
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

			recipeStacks[j] = null;
		}
		recipeStacks[9] = null;
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

	public int getFuelStack() {
		return tank.getFluidAmount();
	}
	public void setFuelStack(int f) {
		tank.setFluid(new FluidStack(tank.getFluid().fluidID, f));
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
		return (int) (craftingCooldown*i/(processTime+1));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("fuelTank", tank.getFluidAmount());
		nbt.setInteger("fuelUsePercent", fuelUsePercent);
		nbt.setInteger("processTime", processTime);

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

		NBTTagList recipe = new NBTTagList();

		for (int i = 0; i < this.recipeStacks.length; ++i)
		{
			if (this.recipeStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.recipeStacks[i].writeToNBT(var4);
				recipe.appendTag(var4);
			}
		}
		nbt.setTag("recipeItems", recipe);

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

		setFuelStack(nbt.getInteger("fuelTank"));
		fuelUsePercent = nbt.getInteger("fuelUsePercent");
		processTime = nbt.getInteger("processTime");

		NBTTagList input = nbt.getTagList("inputItems", 10);
		for (int i = 0; i < input.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)input.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inputStacks.length)
			{
				this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		NBTTagList output = nbt.getTagList("outputItems", 10);
		for (int i = 0; i < output.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)output.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.outputStacks.length)
			{
				this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		NBTTagList recipe = nbt.getTagList("recipeItems", 10);
		for (int i = 0; i < recipe.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)recipe.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.recipeStacks.length)
			{
				this.recipeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		NBTTagList upgrade = nbt.getTagList("upgradeItems", 10);
		for (int i = 0; i < upgrade.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)upgrade.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.upgradeStacks.length)
			{
				this.upgradeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		
		calculateProcessTime();
	}

	/*
	 * checks to see if the item can be inserted into the slot by a machine
	 * since we don't care about which side stuff gets inserted into
	 * only allow fuel in the sides, input in the top
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int j) {
		if(j==1)
			return this.isItemValidForSlot(slot, itemstack);
		else
			return (UtilReference.getItemBurnTime(itemstack,1,false) > 0);
	}

	/*
	 * checks to see if the item can be removed from the slot by a machine
	 * basically, if it's an output slot you can, otherwise not
	 * again, we don't care about sides, you can suck stuff from whereever
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		if(slot >= inputStacks.length && slot < getSizeInventory()-recipeStacks.length-upgradeStacks.length)//output inventory
			return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inputStacks.length+outputStacks.length+recipeStacks.length+upgradeStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot>=0 && slot<inputStacks.length)
			return inputStacks[slot];
		else if(slot<getSizeInventory()-recipeStacks.length-upgradeStacks.length)
			return outputStacks[slot-inputStacks.length];
		else if(slot<getSizeInventory()-upgradeStacks.length)
			return recipeStacks[slot-inputStacks.length-outputStacks.length];
		else if(slot<getSizeInventory())
			return upgradeStacks[slot-inputStacks.length-outputStacks.length-recipeStacks.length];
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
		else if(i<getSizeInventory()-recipeStacks.length-upgradeStacks.length)
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
		else if(i<getSizeInventory()-upgradeStacks.length)
		{
			i = i-inputStacks.length-outputStacks.length;
			if (recipeStacks[i] != null)
			{
				ItemStack itemstack;

				if (recipeStacks[i].stackSize <= j)
				{
					itemstack = recipeStacks[i];
					this.recipeStacks[i] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.recipeStacks[i].splitStack(j);

					if (this.recipeStacks[i].stackSize == 0)
					{
						this.recipeStacks[i] = null;
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
			i = i-inputStacks.length-outputStacks.length-recipeStacks.length;
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
		else if(slot>= inputStacks.length && slot < this.getSizeInventory()-recipeStacks.length-upgradeStacks.length)
		{
			if(outputStacks[slot-inputStacks.length] != null)
			{
				ItemStack var2 = outputStacks[slot-inputStacks.length];
				outputStacks[slot-inputStacks.length] = null;
				return var2;	
			}
			return null;
		}
		else if(slot>= inputStacks.length && slot < this.getSizeInventory()-upgradeStacks.length)
		{
			if(recipeStacks[slot-inputStacks.length-outputStacks.length] != null)
			{
				ItemStack var2 = recipeStacks[slot-inputStacks.length-outputStacks.length];
				recipeStacks[slot-inputStacks.length-outputStacks.length] = null;
				return var2;	
			}
			return null;
		}
		else if(slot < getSizeInventory())
		{
			if(upgradeStacks[slot-inputStacks.length-outputStacks.length-recipeStacks.length] != null)
			{
				ItemStack var2 = upgradeStacks[slot-inputStacks.length-outputStacks.length-recipeStacks.length];
				upgradeStacks[slot-inputStacks.length-outputStacks.length-recipeStacks.length] = null;
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
		if(i>=0 && i<inputStacks.length)
		{
			this.inputStacks[i] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		else if(i<getSizeInventory()-recipeStacks.length-upgradeStacks.length)
		{
			this.outputStacks[i-inputStacks.length] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		else if(i<getSizeInventory()-upgradeStacks.length)
		{
			this.recipeStacks[i-inputStacks.length-outputStacks.length] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		else if(i<getSizeInventory())
		{
			this.upgradeStacks[i-inputStacks.length-outputStacks.length-recipeStacks.length] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}

		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "autocraftingbench";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
	/*
	 * Hopefully this is right
	 * will prevent items from being inserted into the output slots
	 * the table doesn't care about things being put in it (lol)
	 * and will figure out what to do with things later
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot<inputStacks.length)
			return true;
		if(slot == 35)
			if(UtilReference.getItemBurnTime(itemstack,1,false) > 0)
				return true;
		return false;
	}

	public void sendChangePacket(boolean change)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			CarbonizationPacketHandler.instance.sendToServer(new AutocraftingBenchMessageClient(this, change));
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CarbonizationPacketHandler.instance.getPacketFrom(new AutocraftingBenchMessageServer(this));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(side == 0)//only pull things out
		{
			int[] bot = new int[outputStacks.length];
			for (int i = 0; i < bot.length; i++)
			{
				bot[i]=inputStacks.length+i;
			}
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
		else//put/pull fuel
		{
			int[] sid = new int[outputStacks.length+1];
			sid[0] = 35;
			for (int i = 1; i < sid.length; i++)
			{
				sid[i]=i+inputStacks.length-1;
			}
			return sid;
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	
	public int getMaxCapacity()
	{
		return tank.getCapacity();
	}

	public void setMaxCapacity(int capacity)
	{
		tank.setCapacity(capacity);
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