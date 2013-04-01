package mal.carbonization;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeDummyContainer;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityFurnaces extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory, ISidedInventory
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
    private int furnaceMaxCookTime=200;
    //scale for how fast the furnace will cook an item
    public double furnaceCookTimeMultiplyer;
    public int metadata;
    private boolean usesExtraTime=false;
    private byte facing=2;

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
    	super();
    	this.metadata = metadata;
    	
    	switch (metadata)
    	{
    	case 0://iron
    		furnaceCookTimeMultiplyer=0.75;
    		//furnaceMaxCookTime=150;
    		break;
    	case 1://insulated
    		furnaceCookTimeMultiplyer=0.625;
    		//furnaceMaxCookTime=125;
    		break;
    	case 2://steel
    		furnaceCookTimeMultiplyer=0.5;
    		//furnaceMaxCookTime=100;
    		break;
    	default:
    		furnaceCookTimeMultiplyer=10;
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
    public void setFacing(byte facing)
    {
    	this.facing = facing;
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
        NBTTagList var2 = nbt.getTagList("Items");
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
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
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
        this.facing = nbt.getByte("facing");
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
    	
        boolean var1 = this.furnaceBurnTime > 0;
        boolean var2 = false;

        if (this.furnaceBurnTime > 0 && ((this.metadata > 0)?this.canSmelt():true))//don't use fuel if we have no work and are metadata 1-2
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                if (this.furnaceBurnTime > 0)
                {
                    var2 = true;

                    if (this.furnaceItemStacks[1] != null)
                    {
                        --this.furnaceItemStacks[1].stackSize;

                        if (this.furnaceItemStacks[1].stackSize == 0)
                        {
                            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItemStack(furnaceItemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt())
            {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == getCookTime())
                {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    var2 = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (var1 != this.furnaceBurnTime > 0)
            {
                var2 = true;
                //this for icon change for burning
                BlockFurnaces.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (var2)
        {
            this.onInventoryChanged();
        }
        
        //PacketDispatcher.sendPacketToAllPlayers(getDescriptionPacket());
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

            if(var1 != null)//prefer default recipes over special ones
            {
            	if (this.furnaceItemStacks[2] == null) return true;
            	if (!this.furnaceItemStacks[2].isItemEqual(var1)) return false;
            	int result = furnaceItemStacks[2].stackSize + var1.stackSize;
            	return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
            }
            else
            	return canSmeltSpecial();
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
	        ItemStack var2 = CarbonizationRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
	        if (var2 != null)
	        {
	        	if (this.furnaceItemStacks[2] == null) return true;
	        	if (!this.furnaceItemStacks[2].isItemEqual(var2)) return false;
	        	int result = furnaceItemStacks[2].stackSize + var2.stackSize;
	        	return (result <= getInventoryStackLimit() && result <= var2.getMaxStackSize());
	        }
	        else
	        	return false;
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
    		if(CarbonizationRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]) == null)
    			cookTime = -1;
    		else
    			cookTime = CarbonizationRecipes.smelting().getCookTime(this.furnaceItemStacks[0]);
    		
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
        if (this.canSmelt())
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
        	ItemStack var2 = CarbonizationRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if(var1 != null)
	        {
	            if (this.furnaceItemStacks[2] == null)
	            {
	                this.furnaceItemStacks[2] = var1.copy();
	            }
	            else if (this.furnaceItemStacks[2].isItemEqual(var1))
	            {
	                furnaceItemStacks[2].stackSize += var1.stackSize;
	            }
	
	            --this.furnaceItemStacks[0].stackSize;
	
	            if (this.furnaceItemStacks[0].stackSize <= 0)
	            {
	                this.furnaceItemStacks[0] = null;
	            }
	        }
            else if(var2 != null)
        	{
	        	if (this.furnaceItemStacks[2] == null)
	        		this.furnaceItemStacks[2] = var2.copy();
	        	else if (this.furnaceItemStacks[2].isItemEqual(var2))
	        		furnaceItemStacks[2].stackSize += var2.stackSize;
	        	
	        	--this.furnaceItemStacks[0].stackSize;
	        	
	        	if (this.furnaceItemStacks[0].stackSize <= 0)
	        		this.furnaceItemStacks[0] = null;
        	}
        }
    }
    
    public int[] buildIntList()
    {
    	int[] list = new int[furnaceItemStacks.length*3];
    	int pos = 0;
    	for(ItemStack is : furnaceItemStacks)
    	{
    		if(is != null)
    		{
    			list[pos++] = is.itemID;
    			list[pos++] = is.getItemDamage();
    			list[pos++] = is.stackSize;
    		}
    		else
    		{
    			list[pos++] = 0;
    			list[pos++] = 0;
    			list[pos++] = 0;
    		}
    	}
    	return list;
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
                    return 150;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 300;
                }
            }

            if (var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD")) return 200;
            if (var2 instanceof ItemSword && ((ItemSword) var2).getToolMaterialName().equals("WOOD")) return 200;
            if (var2 instanceof ItemHoe && ((ItemHoe) var2).func_77842_f().equals("WOOD")) return 200;
            if (var1 == Item.stick.itemID) return 100;
            if (var1 == Item.coal.itemID) return 1600;
            if (var1 == Item.bucketLava.itemID) return 20000;
            if (var1 == Block.sapling.blockID) return 100;
            if (var1 == Item.blazeRod.itemID) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }

    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public static boolean isItemFuel(ItemStack par0ItemStack)
    {
        return getItemBurnTime(par0ItemStack) > 0;
    }
    
    public void handlePacketData(int[] items)
    {
    	if(items != null)
    	{
    		int pos = 0;
    		for(int i = 0; i < this.furnaceItemStacks.length; i++)
    		{
    			if(pos+2 < items.length && items[pos+2]!=0)
    			{
    				ItemStack is = new ItemStack(items[pos], items[pos+2], items[pos+1]);
    				this.furnaceItemStacks[i]=is;
    			}
    			else
    			{
    				this.furnaceItemStacks[i] = null;
    			}
    			pos+= 3;
    		}
    	}
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return i == 2 ? false : (i == 1 ? isItemFuel(itemstack) : true);
	}
	
	 @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (ForgeDummyContainer.legacyFurnaceSides)
        {
            if (side == ForgeDirection.DOWN) return 1;
            if (side == ForgeDirection.UP) return 0;
            return 2;
        }
        else
        {
            if (side == ForgeDirection.DOWN) return 2;
            if (side == ForgeDirection.UP) return 0;
            return 1;
        }
    }

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
	    return 1;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return PacketHandler.getPacket(this);
	}

	@Override
	public int[] getSizeInventorySide(int par1) {
		return par1 == 0 ? field_102011_e : (par1 == 1 ? field_102010_d : field_102009_f);
	}

	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) {
		return this.isStackValidForSlot(j, itemstack);
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) {
		return j != 0 || i != 1 || itemstack.itemID == Item.bucketEmpty.itemID;
	}
}
