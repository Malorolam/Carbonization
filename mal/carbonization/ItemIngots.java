package mal.carbonization;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://refined iron
			list.add(setTooltipData("A purified iron", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with better properties", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 0 Material", ColorReference.DARKCYAN));
			break;
		case 1://pig iron
			list.add(setTooltipData("The presence of extra carbon", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("makes an intermediate material", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("that isn't too useful.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 1 Material", ColorReference.DARKCYAN));
			break;
		case 2://mild steel
			list.add(setTooltipData("Purification of excess carbon", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and impurities improves properties", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("in this high carbon material.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 2 Material", ColorReference.DARKCYAN));
			break;
		case 3://steel
			list.add(setTooltipData("A typical mid-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with multiple applications.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 2 Material", ColorReference.DARKCYAN));
			break;
		default:
			list.add(setTooltipData("This isn't even an ingot!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier Error Material", ColorReference.DARKCYAN));
		}
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
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