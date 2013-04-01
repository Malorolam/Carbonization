package mal.carbonization;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemHHPulverizer extends Item {

	public ItemHHPulverizer(int par1) {
		super(par1);
		this.setMaxDamage(99);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("HHPulverizer");
		this.setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public void updateIcons(IconRegister ir)
	{
		this.iconIndex = ir.registerIcon("carbonization:hhPulverizerTexture");
	}
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack)
	{
		return false;
	}
	
	@Override
	public boolean getShareTag()
	{
		return true;
	}
	
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack) 
    {
    	itemStack.setItemDamage(itemStack.getItemDamage() + 1);
     	return itemStack;
    }
    
    public String getItemNameIS(ItemStack itemstack)
	{
		return "Handheld Pulverizer";
	}

}
