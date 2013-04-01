package mal.carbonization;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemIngots extends Item {

	private Icon[] iconArray = new Icon[4];
	public ItemIngots(int par1) {
		super(par1);
		this.hasSubtypes = true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemIngots");
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "refinedIron";
		case 1:
			return "pigIron";
		case 2:
			return "mildSteel";
		case 3:
			return "steel";
		default:
			return "ItemIngot";
		}
	}
	
	@Override
	public void updateIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:refinedIronTexture");
		iconArray[1] = ir.registerIcon("carbonization:pigIronTexture");
		iconArray[2] = ir.registerIcon("carbonization:mildSteelTexture");
		iconArray[3] = ir.registerIcon("carbonization:steelTexture");
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
			r="carbonization.refinediron";
			break;
		case 1:
			r="carbonization.pigiron";
			break;
		case 2:
			r="carbonization.mildsteel";
			break;
		case 3:
			r="carbonization.steel";
			break;
		default:
			r="carbonization.peat";
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
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}
