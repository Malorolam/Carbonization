package mal.carbonization.item;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemFuel extends Item {

	private IIcon[] iconArray = new IIcon[3];
	
	public ItemFuel()
	{
		super();
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
			list.add(setTooltipData("A high-value fuel found",ColorReference.DARKGREY));
			list.add(setTooltipData("in deep stone layers.", ColorReference.DARKGREY));
			break;
		case 2:
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
			return "item.peat";
		case 1:
			return "item.anthracite";
		case 2:
			return "item.graphite";
		default:
			return "item.ItemFuel";
		}
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:peatBarTexture");
		iconArray[1] = ir.registerIcon("carbonization:anthraciteBarTexture");
		iconArray[2] = ir.registerIcon("carbonization:graphiteBarTexture");
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
	
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		String r="";
		
		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			r="carbonization.peat";
			break;
		case 1:
			r="carbonization.anthracite";
			break;
		case 2:
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
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(item, 1, 0));
        par3List.add(new ItemStack(item, 1, 1));
        par3List.add(new ItemStack(item, 1, 2));
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