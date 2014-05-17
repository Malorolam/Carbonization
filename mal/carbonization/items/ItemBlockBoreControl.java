package mal.carbonization.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.core.ColorReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;

public class ItemBlockBoreControl extends ItemMultiTextureTile {
	
	private static String[] string = new String[]{"borecontrol"};
	
	public ItemBlockBoreControl(int par1, Block block) {
		super(par1, block, string);
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value, currently doesn't do anything, since there is no metadata
		list.add(setTooltipData("A control system for a", ColorReference.LIGHTRED));
		list.add(setTooltipData("large bore.  Lets you", ColorReference.LIGHTRED));
		list.add(setTooltipData("instantiate and control the machine.", ColorReference.LIGHTRED));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0: 
			name = "borecontrol";
			break;
		default: name = "blarg";
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