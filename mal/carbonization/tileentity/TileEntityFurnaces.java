package mal.carbonization.tileentity;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.CarbonizationRecipeHandler;
import mal.carbonization.carbonization;
import mal.carbonization.block.BlockFurnaces;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.FurnacesMessage;
import mal.carbonization.network.MultiblockInitMessage;
import mal.core.api.IFuelContainer;
import mal.core.reference.UtilReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityFurnaces extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory
{
    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    
    private static final int[] field_102010_d = new int[] {0};
    private static final int[] field_102011_e = new int[] {2, 1};
    private static final int[] field_102009_f = new int[] {1};
    
    /**
     * The cook time for each furnace type
     * Iron = 150;
     * Insulated Iron = 125;
     * Steel = 100;
     */
    public int furnaceMaxCookTime=200;
    //scale for how fast the furnace will cook an item
    public double furnaceCookTimeMultiplyer;
    public int metadata;
    public boolean usesExtraTime=false;
    public byte facing=2;
    public boolean isActive=false;
    
    //The tier of the machine, used to limit recipes to certain tech tiers
    public int tier;

    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime = 0;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime = 0;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime = 0;

    public TileEntityFurnaces()
    {
    	this(3);
    }
    
    //function that allows for different cook times
    public TileEntityFurnaces(int metadata)
    {
    	this.metadata = metadata;
    	
    	switch (metadata)
    	{
    	case 0://iron
    		furnaceCookTimeMultiplyer=0.75;
    		tier = 1;
    		//furnaceMaxCookTime=150;
    		break;
    	case 1://insulated
    		furnaceCookTimeMultiplyer=0.625;
    		tier = 2;
    		//furnaceMaxCookTime=125;
    		break;
    	case 2://steel
    		furnaceCookTimeMultiplyer=0.5;
    		tier = 3;
    		//furnaceMaxCookTime=100;
    		break;
    	default:
    		furnaceCookTimeMultiplyer=10;
    		tier = 1;
    		//furnaceMaxCookTime=2000;
    	}
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }
    
    /**
     * Sets the facing
     */
    public void setFacingandMetadata(byte facing, byte metadata)
    {
    	this.facing = facing;
    	this.metadata = metadata;
    }
    
    /***
     * Get Facing
     */
    public byte getFacing()
    {
    	return facing;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.furnaceItemStacks[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var3;

            if (this.furnaceItemStacks[par1].stackSize <= par2)
            {
                var3 = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.furnaceItemStacks[par1].splitStack(par2);

                if (this.furnaceItemStacks[par1].stackSize == 0)
                {
                    this.furnaceItemStacks[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack var2 = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
    	if(metadata==0)
    		return "ironfurnace";
    	else if(metadata ==1)
    		return "insulatedfurnace";
    	else if(metadata==2)
    		return "steelfurnace";
    	else
    		return "hurrdurr";
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList var2 = nbt.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.metadata = nbt.getShort("Metadata");
        this.furnaceBurnTime = nbt.getShort("BurnTime");
        this.furnaceCookTime = nbt.getShort("CookTime");
        this.furnaceCookTimeMultiplyer = nbt.getDouble("CookTimeMultiplyer");
        this.currentItemBurnTime = UtilReference.getItemBurnTime(this.furnaceItemStacks[1],1600,false);
        this.facing = nbt.getByte("facing");
        this.tier = nbt.getInteger("Tier");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("Metadata", (short)metadata);
        nbt.setShort("BurnTime", (short)this.furnaceBurnTime);
        nbt.setShort("CookTime", (short)this.furnaceCookTime);
        nbt.setDouble("CookTimeMultiplyer", this.furnaceCookTimeMultiplyer);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3)
        {
            if (this.furnaceItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.furnaceItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
        nbt.setByte("facing", facing);
        nbt.setInteger("Tier", tier);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1)
    {
        return this.furnaceCookTime * par1 / getCookTime();
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled(int par1)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
    }

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {	
    	super.updateEntity();
    	
    	if(!this.worldObj.isRemote)
    	{
    		boolean smelting = canSmeltSpecial();//check to see if there can be any smelting
    		if(furnaceBurnTime>0 && ((metadata>0)?smelting:true))
    			furnaceBurnTime -= 1;
    		
    		if(smelting)
    		{
    			if(furnaceBurnTime == 0)
    			{
                    this.currentItemBurnTime = this.furnaceBurnTime = UtilReference.getItemBurnTime(this.furnaceItemStacks[1],1600,true);

                    if (this.furnaceBurnTime > 0)
                    {
                        if (this.furnaceItemStacks[1] != null && !(this.furnaceItemStacks[1].getItem() instanceof IFuelContainer))
                        {
                            --this.furnaceItemStacks[1].stackSize;

                            if (this.furnaceItemStacks[1].stackSize == 0)
                            {
                                this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                            }
                        }
                    }
    			}
    			
    			if(isBurning() && smelting)
    			{
                    ++this.furnaceCookTime;

                    if (this.furnaceCookTime == getCookTime())
                    {
                        this.furnaceCookTime = 0;
                        this.smeltItem();
                    }
    			}
    			else
    				furnaceCookTime=0;
    		}

    		isActive = isBurning();
    		
    		//CarbonizationPacketHandler.instance.sendToAll(getDescriptionMessage());
    	}
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
    	if (this.furnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if(var1 != null)
            {
            	if (this.furnaceItemStacks[2] == null) return true;
            	if (!this.furnaceItemStacks[2].isItemEqual(var1)) return false;
            	int result = furnaceItemStacks[2].stackSize + var1.stackSize;
            	return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
            }
            else
            	return false;
        }
    }
    
    //Check for special recipes
    private boolean canSmeltSpecial()
    {
    	if (this.furnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
	        ItemStack var2 = CarbonizationRecipeHandler.smelting().getSmeltingResult(this.furnaceItemStacks[0].copy());
	        int tier = CarbonizationRecipeHandler.smelting().getMinTier(this.furnaceItemStacks[0]);
	        if (var2 != null && this.tier>= tier)
	        {
	        	if (this.furnaceItemStacks[2] == null) return true;
	        	if (!this.furnaceItemStacks[2].isItemEqual(var2)) return false;
	        	int result = furnaceItemStacks[2].stackSize + var2.stackSize;
	        	return (result <= getInventoryStackLimit() && result <= var2.getMaxStackSize());
	        }
	        else
	        	return canSmelt();
        }
    }
    
    /**
     * Get the cook time for a special recipe
     * 
     */
    private int getCookTime()
    {
    	if (this.furnaceItemStacks[0] == null)
    		return (int) Math.floor(furnaceMaxCookTime*furnaceCookTimeMultiplyer);
    	else
    	{
    		int cookTime;
    		if(CarbonizationRecipeHandler.smelting().getSmeltingResult(this.furnaceItemStacks[0].copy()) == null)
    			cookTime = -1;
    		else
    			cookTime = CarbonizationRecipeHandler.smelting().getCookTime(this.furnaceItemStacks[0]);
    		
    		if(cookTime != -1)
    			return (int) (cookTime*furnaceCookTimeMultiplyer);
    		else
    			return (int) Math.floor(furnaceMaxCookTime*furnaceCookTimeMultiplyer);
    	}
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmeltSpecial())
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
        	ItemStack var2 = CarbonizationRecipeHandler.smelting().getSmeltingResult(this.furnaceItemStacks[0].copy());

            if(var2 != null)
	        {
	            if (this.furnaceItemStacks[2] == null)
	            {
	                this.furnaceItemStacks[2] = var2.copy();
	            }
	            else if (this.furnaceItemStacks[2].isItemEqual(var2))
	            {
	                furnaceItemStacks[2].stackSize += var2.stackSize;
	            }
	
	            --this.furnaceItemStacks[0].stackSize;
	
	            if (this.furnaceItemStacks[0].stackSize <= 0)
	            {
	                this.furnaceItemStacks[0] = null;
	            }
	        }
            else if(var1 != null)
        	{
	        	if (this.furnaceItemStacks[2] == null)
	        		this.furnaceItemStacks[2] = var1.copy();
	        	else if (this.furnaceItemStacks[2].isItemEqual(var1))
	        		furnaceItemStacks[2].stackSize += var1.stackSize;
	        	
	        	--this.furnaceItemStacks[0].stackSize;
	        	
	        	if (this.furnaceItemStacks[0].stackSize <= 0)
	        		this.furnaceItemStacks[0] = null;
        	}
        }
    }
    
    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public boolean isItemFuel(ItemStack par0ItemStack)
    {
        return UtilReference.getItemBurnTime(par0ItemStack,1600,false) > 0;
    }

	public int swapFacing()
	{
		if(facing==5)
			facing = 2;
		else
			facing++;
		return facing;
	}
	
	public void activate(World world, int x, int y, int z,EntityPlayer player)
	{
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench)
		{
			swapFacing();
			System.out.println(facing);

			CarbonizationPacketHandler.instance.sendToAll(getDescriptionMessage());
		}
		else
			player.openGui(carbonization.carbonizationInstance, 0, world, x, y, z);
	}
	
    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 2 ? false : (i == 1 ? isItemFuel(itemstack) : true);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return CarbonizationPacketHandler.instance.getPacketFrom(new FurnacesMessage(this));
	}
	
	public IMessage getDescriptionMessage()
	{
		return new FurnacesMessage(this);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return var1 == 0 ? field_102011_e : (var1 == 1 ? field_102010_d : field_102009_f);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return this.isItemValidForSlot(i, itemstack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j != 0 || i != 1;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}
	
	public ItemStack[] getInventory()
	{
		return furnaceItemStacks;
	}
	
	public void setInventory(ItemStack[] is)
	{
		if(is == null)
			return;
		for(int i = 0; (i < furnaceItemStacks.length && i<is.length); i++)
		{
			furnaceItemStacks[i] = is[i];
		}
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