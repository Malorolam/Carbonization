package mal.carbonization.item;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDust extends Item {

	private IIcon[] iconArray = new IIcon[6];
	
	public ItemDust() {
		super();
		this.hasSubtypes=true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemDust");
		this.setCreativeTab(carbonization.tabItems);
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
			list.add(setTooltipData("Ground up fuel made of mostly",ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with some impurities.", ColorReference.DARKGREY));
			break;
		case 3:
			list.add(setTooltipData("Ground up fuel made of mostly", ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with little impurities.", ColorReference.DARKGREY));
			break;
		case 4:
			list.add(setTooltipData("Ground up fuel made of entirely", ColorReference.DARKGREY));
			list.add(setTooltipData("carbon with few other components.", ColorReference.DARKGREY));
			break;
		case 5:
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
			return "item.charcoalDust";
		case 1:
			return "item.peatDust";
		case 2:
			return "item.coalDust";
		case 3:
			return "item.anthraciteDust";
		case 4:
			return "item.graphiteDust";
		case 5:
			return "item.aCharcoalDust";
		default:
			return "item.ItemDust";
		}
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:charcoalDustTexture");
		iconArray[1] = ir.registerIcon("carbonization:peatClumpTexture");
		iconArray[2] = ir.registerIcon("carbonization:coalDustTexture");
		iconArray[3] = ir.registerIcon("carbonization:anthraciteDustTexture");
		iconArray[4] = ir.registerIcon("carbonization:graphiteDustTexture");
		iconArray[5] = ir.registerIcon("carbonization:charcoalDustTexture");
	}
	
	public IIcon getIconFromDamage(int value)
	{
		return iconArray[value];
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getBlockTextureFromSideAndMetadata(int par1, int par2)
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
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
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
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/