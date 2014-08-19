package mal.carbonization.item;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemHHPulverizer extends Item {

	public ItemHHPulverizer() {
		super();
		this.setMaxDamage(99);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("HHPulverizer");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("This can be used to crush",ColorReference.DARKCYAN));
		list.add(setTooltipData("a fuel into dust.", ColorReference.DARKCYAN));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		this.itemIcon = ir.registerIcon("carbonization:hhPulverizerTexture");
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
    public ItemStack getContainerItem(ItemStack itemStack) 
    {
    	itemStack.setItemDamage(itemStack.getItemDamage() + 1);
     	return itemStack;
    }
    
    public String getItemNameIS(ItemStack itemstack)
	{
		return "Handheld Pulverizer";
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