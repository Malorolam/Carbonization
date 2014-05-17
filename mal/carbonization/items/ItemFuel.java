package mal.carbonization.items;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.ColorReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemFuel extends Item {

	private Icon[] iconArray = new Icon[6];
	
	public ItemFuel(int par1)
	{
		super(par1);
		this.hasSubtypes=true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemFuel");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		switch(is.getItemDamage())
		{
		case 0:
			list.add(setTooltipData("A low-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in wet areas.", ColorReference.DARKGREY));
			break;
		case 1:
			list.add(setTooltipData("A low-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in upper layers of soil.", ColorReference.DARKGREY));
			break;
		case 2:
			list.add(setTooltipData("A medium-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in middle stone layers.", ColorReference.DARKGREY));
			break;
		case 3:
			list.add(setTooltipData("A medium-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in middle stone layers.", ColorReference.DARKGREY));
			break;
		case 4:
			list.add(setTooltipData("A high-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in deep stone layers.", ColorReference.DARKGREY));
			break;
		case 5:
			list.add(setTooltipData("A solid chunk of carbon found",ColorReference.DARKGREY));
			list.add(setTooltipData("deep in the earth.", ColorReference.DARKGREY));
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
			return "item.Peat";
		case 1:
			return "item.lignite";
		case 2:
			return "item.sBitCoal";
		case 3:
			return "item.bitCoal";
		case 4:
			return "item.anthracite";
		case 5:
			return "item.graphite";
		default:
			return "item.ItemFuel";
		}
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:peatBarTexture");
		iconArray[1] = ir.registerIcon("carbonization:ligniteBarTexture");
		iconArray[2] = ir.registerIcon("carbonization:sBitBarTexture");
		iconArray[3] = ir.registerIcon("carbonization:bitBarTexture");
		iconArray[4] = ir.registerIcon("carbonization:anthraciteBarTexture");
		iconArray[5] = ir.registerIcon("carbonization:graphiteBarTexture");
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
			r="carbonization.peat";
			break;
		case 1:
			r="carbonization.lignite";
			break;
		case 2:
			r="carbonization.sbituminous";
			break;
		case 3:
			r="carbonization.bituminous";
			break;
		case 4:
			r="carbonization.anthracite";
			break;
		case 5:
			r="carbonization.graphite";
			break;
		default:
			r="carbonization.bloop";
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
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
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