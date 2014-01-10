package mal.carbonization.items;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.core.ColorReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;

public class ItemBlockAutocraftingBench extends ItemBlock {
	

	public ItemBlockAutocraftingBench(int par1, Block block) {
		super(par1);
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		switch(is.getItemDamage())
		{
		case 0:
			list.add(setTooltipData("A series of powered assemblies", ColorReference.LIGHTRED));
			list.add(setTooltipData("work to construct items without", ColorReference.LIGHTRED));
			list.add(setTooltipData("your input", ColorReference.LIGHTRED));
			break;
		case 1:
			list.add(setTooltipData("This machine allows for fuel", ColorReference.LIGHTRED));
			list.add(setTooltipData("to be converted without", ColorReference.LIGHTRED));
			list.add(setTooltipData("your input", ColorReference.LIGHTRED));
			break;
		case 2:
			list.add(setTooltipData("This machine allows for fuel", ColorReference.LIGHTRED));
			list.add(setTooltipData("to be compressed into a", ColorReference.LIGHTRED));
			list.add(setTooltipData("fuel cell", ColorReference.LIGHTRED));
			break;
		default:
			list.add(setTooltipData("This isn't even a workbench!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier Error Material", ColorReference.DARKCYAN));
		}
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
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "tile.autocraftingbench";
		case 1:
			return "tile.fuelconverter";
		default:
			return "blaarg";
		}
	}
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		switch(itemstack.getItemDamage())
		{
		case 0:
			return "autocraftingbench";
		case 1:
			return "fuelconverter";
		default:
			return "blaarg";
		}
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