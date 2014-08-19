package mal.carbonization.item;

import java.util.List;

import buildcraft.api.tools.IToolWrench;
import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemScrewdriver extends Item implements IToolWrench{

	public ItemScrewdriver() {
		super();
		this.setCreativeTab(carbonization.tabItems);
		this.setUnlocalizedName("screwdriver");
		this.setContainerItem(this);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("It's a screwdriver.", ColorReference.LIGHTCYAN));
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
		this.itemIcon = ir.registerIcon("carbonization:screwdriverTexture");
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
    
    public String getItemNameIS(ItemStack itemstack)
	{
		return "Screwdriver";
	}

	@Override
	public boolean canWrench(EntityPlayer paramEntityPlayer, int paramInt1,
			int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer paramEntityPlayer, int paramInt1,
			int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub
		
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