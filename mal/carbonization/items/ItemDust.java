package mal.carbonization.items;

import java.util.List;

import mal.carbonization.ColorReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDust extends Item {

	private Icon[] iconArray = new Icon[9];
	
	public ItemDust(int par1) {
		super(par1);
		this.hasSubtypes=true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemDust");
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		switch(is.getItemDamage())
		{
		case 0:
			list.add(setTooltipData("Ground up burnt wood.",ColorReference.DARKGREY));
			list.add(setTooltipData("Some impurities present.", ColorReference.DARKGREY));
			break;
		case 1:
			list.add(setTooltipData("Very moist chunks of fuel",ColorReference.DARKGREY));
			list.add(setTooltipData("with many impurities present.", ColorReference.DARKGREY));
			break;
		case 2:
			list.add(setTooltipData("Chunks of fuel with many",ColorReference.DARKGREY));
			list.add(setTooltipData("impurities present.", ColorReference.DARKGREY));
			break;
		case 3:
			list.add(setTooltipData("Ground up fuel made of some",ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with many impurities.", ColorReference.DARKGREY));
			break;
		case 4:
			list.add(setTooltipData("Ground up fuel made of mostly",ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with impurities present.", ColorReference.DARKGREY));
			break;
		case 5:
			list.add(setTooltipData("Ground up fuel made of mostly",ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with some impurities.", ColorReference.DARKGREY));
			break;
		case 6:
			list.add(setTooltipData("Ground up fuel made of mostly", ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with little impurities.", ColorReference.DARKGREY));
			break;
		case 7:
			list.add(setTooltipData("Ground up fuel made of entirely", ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with few other components.", ColorReference.DARKGREY));
			break;
		case 8:
			list.add(setTooltipData("A very fine charcoal dust.", ColorReference.DARKGREY));
			list.add(setTooltipData("Useful in filtration and", ColorReference.ORANGE));
			list.add(setTooltipData("purification applications", ColorReference.ORANGE));
			break;
		default:
			list.add(setTooltipData("This isn't a fuel!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it.", ColorReference.DARKRED));
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
			return "charcoalDust";
		case 1:
			return "peatDust";
		case 2:
			return "ligniteDust";
		case 3:
			return "sBitCoalDust";
		case 4:
			return "bitCoalDust";
		case 5:
			return "coalDust";
		case 6:
			return "anthraciteDust";
		case 7:
			return "graphiteDust";
		case 8:
			return "aCharcoalDust";
		default:
			return "ItemDust";
		}
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:charcoalDustTexture");
		iconArray[1] = ir.registerIcon("carbonization:peatClumpTexture");
		iconArray[2] = ir.registerIcon("carbonization:ligniteClumpTexture");
		iconArray[3] = ir.registerIcon("carbonization:sBitDustTexture");
		iconArray[4] = ir.registerIcon("carbonization:bitDustTexture");
		iconArray[5] = ir.registerIcon("carbonization:coalDustTexture");
		iconArray[6] = ir.registerIcon("carbonization:anthraciteDustTexture");
		iconArray[7] = ir.registerIcon("carbonization:graphiteDustTexture");
		iconArray[8] = ir.registerIcon("carbonization:charcoalDustTexture");
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
	
	public int getMetadata(int par1)
	{
		return par1;
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
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
        par3List.add(new ItemStack(par1, 1, 6));
        par3List.add(new ItemStack(par1, 1, 7));
        par3List.add(new ItemStack(par1, 1, 8));
    }
	
	public String getItemNameIS(ItemStack itemstack)
	{
		String name = "";
		switch (itemstack.getItemDamage())
		{
		case 0:
			name = "charcoaldust";
			break;
		case 1:
			name = "peatdust";
			break;
		case 2:
			name = "lignitedust";
			break;
		case 3:
			name = "sbitdust";
			break;
		case 4:
			name = "bitdust";
			break;
		case 5:
			name = "coaldust";
			break;
		case 6:
			name = "anthracitedust";
			break;
		case 7:
			name = "graphitedust";
			break;
		case 8:
			name = "acharcoaldust";
		default:
			name = "blaarg";
		}
		return name;
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