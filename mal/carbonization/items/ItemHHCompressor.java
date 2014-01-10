package mal.carbonization.items;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.ColorReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHHCompressor extends Item 
{

	public ItemHHCompressor(int par1) {
		super(par1);
		this.setMaxDamage(999);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("HHCompressor");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("This can be used to turn dust",ColorReference.DARKCYAN));
		list.add(setTooltipData("into solid fuel... somehow.", ColorReference.DARKCYAN));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		this.itemIcon = ir.registerIcon("carbonization:hhCompressorTexture");
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
		return "Handheld Compressor";
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