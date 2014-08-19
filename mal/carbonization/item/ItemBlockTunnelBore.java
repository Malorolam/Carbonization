package mal.carbonization.item;

import java.util.List;

import mal.core.reference.ColorReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTunnelBore extends ItemBlock{

	public ItemBlockTunnelBore(Block block) {
		super(block);
		this.setUnlocalizedName("itemBlockTunnelBore");
	}

	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("A industrial tunneling device,", ColorReference.LIGHTRED));
		list.add(setTooltipData("turning fuel into a very large", ColorReference.LIGHTRED));
		list.add(setTooltipData("hole in the ground.", ColorReference.LIGHTRED));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
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