package mal.carbonization;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemMisc extends Item{

	private Icon[] iconArray = new Icon[3];
	
	public ItemMisc(int par1) {
		super(par1);
		this.hasSubtypes = true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemMisc");
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "Pencil";
		case 1:
			return "CleansingPotion";
		default:
			return "ItemMisc";
		}
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	public void updateIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:pencilTexture");
		iconArray[1] = ir.registerIcon("carbonization:pencilTexture");
		iconArray[2] = ir.registerIcon("carbonization:cleansingPotionTexture");
	}
	
	public Icon getIconFromDamage(int value)
	{
		return iconArray[value];
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 0 || par2 >= this.iconArray.length)
        {
            par2 = 0;
        }

        return this.iconArray[par2];
    }
    
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		String r="";
		
		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			r="graphitepencil";
			break;
		case 1:
			r="charcoalpencil";
		case 2:
			r="curepotion";
		default:
			r="blaarg";
			break;
		}
		
		return r;
	}
	
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        //par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }
	
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par3EntityPlayer.curePotionEffects(new ItemStack(Item.bucketMilk,1));

        return par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if(par1ItemStack.getItemDamage()==2)//cure potion
    	{
    		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
    	}
    		return par1ItemStack;
    }

}
