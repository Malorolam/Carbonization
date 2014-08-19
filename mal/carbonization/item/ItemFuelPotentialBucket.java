package mal.carbonization.item;

import java.util.List;

import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

public class ItemFuelPotentialBucket extends ItemBucket {

	public ItemFuelPotentialBucket(Block p_i45331_1_) {
		super(p_i45331_1_);
		
		setUnlocalizedName("fpbucket");
		setContainerItem(Items.bucket);
		this.setCreativeTab(carbonization.tabItems);
	}

	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("A bucket of potential fuel.",ColorReference.DARKGREY));
		list.add(setTooltipData("It smells rather terrible.", ColorReference.DARKGREY));
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
		this.itemIcon = ir.registerIcon("carbonization:fuelPotentialBucketTexture");
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