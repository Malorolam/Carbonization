package mal.carbonization.tileentity;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.carbonization;
import mal.carbonization.items.ItemStructureBlock;
import mal.carbonization.multiblock.MultiblockWorkQueueItem;
import mal.carbonization.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityAutocraftingBench extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory
{

	/**
	 * Is the random generator used by furnace to drop the inventory contents in random directions.
	 */
	private Random furnaceRand = new Random();

	public int maxFuelCapacity = 6400;

	public ItemStack[] inputStacks = new ItemStack[12];
	public ItemStack[] outputStacks = new ItemStack[12];
	public ItemStack[] recipeStacks = new ItemStack[10];
	public ItemStack[] upgradeStacks = new ItemStack[2];

	public double fuelTank;//fuel stored
	public int fuelUsePercent;//percentage of fuel used per process
	public double upgradeTier;//upgrade tier, dependant on the upgrade item
	public int processTime = carbonization.MAXAUTOCRAFTTIME;//time to process an item, dependant on fuel usage
	public int fuelUsage = carbonization.BASEAUTOCRAFTFUEL;
	public int craftingCooldown; //amount of time before a new item can be crafted
	
	public boolean updating = false;//disable things because a packet is doing things
	private boolean inventoryChanged = true;//recheck the inventories
	public boolean disableButtons = false;//set to true when the buttons wouldn't do anything (like max time = min time)

	public TileEntityAutocraftingBench()
	{
		if(carbonization.MAXAUTOCRAFTTIME==carbonization.MINAUTOCRAFTTIME)
			disableButtons = true;
	}
	
	public void activate(World world, int x, int y, int z,
			EntityPlayer player) {
		//System.out.println("Component Tiers: " + componentTiers[0] + ", " + componentTiers[1] +"; queue capacity: " + queue.maxJobs + "; activated: " + properlyActivated);
		player.openGui(carbonization.instance, 3, world, x, y, z);
	}

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		
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
			
			if(action)
			{
				onInventoryChanged();
			}
			else
				inventoryChanged=false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64, worldObj.provider.dimensionId, getDescriptionPacket());
		}
	}
	
	/*
	 * Looks through the upgrade slots and does the correct stuff with them
	 */
	public boolean handleUpgrade(boolean action)
	{
		if(upgradeStacks == null)
			return action;
		
		if(upgradeStacks[0] != null)//something in the upgrade slot
		{
			if(upgradeStacks[0].itemID == carbonization.itemStructureBlock.itemID)
			{
				if(upgradeStacks[0].getItemDamage() >= 2000)
				{
					double[] d = ItemStructureBlock.getTier(upgradeStacks[0].getItemDamage());
					upgradeTier = (d[0]+d[1])/2*Math.sqrt(upgradeStacks[0].stackSize);
					action = true;
				}
				else
					upgradeTier = 0;
			}
			else
				upgradeTier = 0;
		}
		else
			upgradeTier = 0;
		
		if(upgradeStacks[1] != null)
		{
			if(getItemBurnTime(upgradeStacks[1])>0)//it's a bit of fuel
			{
				if(addFuel(upgradeStacks[1]))
				{
					if(upgradeStacks[1].stackSize>1)
						upgradeStacks[1].stackSize -= 1;
					else
						upgradeStacks[1] = null;
					
					action = true;
				}
			}
		}
		return action;
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
    			else if(recipeStacks[i].getItem().hasContainerItem() || !recipeStacks[i].getItem().doesContainerItemLeaveCraftingGrid(recipeStacks[i]))
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
    					else if(recipeStacks[j].itemID == inputStacks[i].itemID)
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
    			fuelTank -= getFuelUsage();
    			for(int i = 0; i < 9; i++)
    			{
	    			if(toolIndex[i] == i && isTool[i] != -1)
	    			{
	    				if(toolInInventory[i]==false)
	    				{
	    					recipeStacks[toolIndex[i]] = recipeStacks[toolIndex[i]].getItem().getContainerItemStack(recipeStacks[toolIndex[i]]);
	    					if(recipeStacks[toolIndex[i]].getItemDamage() > recipeStacks[toolIndex[i]].getMaxDamage())
	    					{
	    						//System.out.println("removed item in crafting slot " + toolIndex[i]);
	    						recipeStacks[i] = null;
	    						recipeStacks[9] = null;//since we're removing a crafting item, the recipe isn't valid anymore
	    					}
	    				}
	    				else
	    				{
	    					recipe[toolIndex[i]] = (recipe[toolIndex[i]].getItem().getContainerItemStack(recipe[i]));
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
    
    private boolean canCraft() {
		if(craftingCooldown > 0 || fuelTank < getFuelUsage())
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

	/**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
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

	public double getFuelUsage()
	{
		return fuelUsage*((double)fuelUsePercent)/100/(upgradeTier+1);
	}

	//build an int list of the inventory
	public int[] buildIntList()
	{
		int[] list = new int[(inputStacks.length+outputStacks.length+recipeStacks.length+upgradeStacks.length)*3+1];
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

		for(ItemStack rs : recipeStacks)
		{
			if(rs != null)
			{
				//System.out.println("Putting output item: " + os.toString() + " in list");
				list[pos++] = rs.itemID;
				list[pos++] = rs.getItemDamage();
				list[pos++] = rs.stackSize;
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

			for(int i = 0; i < this.recipeStacks.length; i++)
			{
				if(pos+2 < items.length && items[pos+2]!=0)
				{
					ItemStack is = new ItemStack(items[pos], items[pos+2], items[pos+1]);
					this.recipeStacks[i]=is;
				}
				else
				{
					this.recipeStacks[i] = null;
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

	public double getFuelStack() {
		return fuelTank;
	}
	public void setFuelStack(double f) {
		this.fuelTank = f;
	}

	//return the scaled percentage of the fuel tank
	@SideOnly(Side.CLIENT)
	public int getFuelCapacityScaled(int i)
	{
		return (int) (this.getFuelStack()*i/(this.maxFuelCapacity+1));
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

		nbt.setDouble("fuelTank", fuelTank);
		nbt.setInteger("fuelUsePercent", fuelUsePercent);
		nbt.setDouble("upgradeTier", upgradeTier);
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

		fuelTank = nbt.getDouble("fuelTank");
		fuelUsePercent = nbt.getInteger("fuelUsePercent");
		upgradeTier = nbt.getDouble("upgradeTier");
		processTime = nbt.getInteger("processTime");

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

		NBTTagList recipe = nbt.getTagList("recipeItems");
		for (int i = 0; i < recipe.tagCount(); ++i)
		{
			NBTTagCompound var4 = (NBTTagCompound)recipe.tagAt(i);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.recipeStacks.length)
			{
				this.recipeStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
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

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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
			if(getItemBurnTime(itemstack) > 0)
				return true;
		return false;
	}

		@Override
	public Packet getDescriptionPacket()
	{
	   return PacketHandler.getPacket(this);
	}

	public void closeGui()
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			PacketDispatcher.sendPacketToServer(getDescriptionPacket());
		}
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub

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
		else//put/pull anything and fuel
		{
			int[] sid = new int[inputStacks.length+outputStacks.length+1];
			sid[0] = 35;
			for (int i = 1; i < sid.length; i++)
			{
				sid[i]=i-1;
			}
			return sid;
		}
	}

}
/*******************************************************************************
 * Copyright (c) 2013 Malorolam.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * 
 *********************************************************************************/