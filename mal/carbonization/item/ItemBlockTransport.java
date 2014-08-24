package mal.carbonization.item;

import java.util.List;

import mal.core.reference.ColorReference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTransport extends ItemBlock{

	public ItemBlockTransport(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		switch(is.getItemDamage())
		{
		case 0:
			list.add(setTooltipData("This serves as an intermediate", ColorReference.LIGHTRED));
			list.add(setTooltipData("storage container for a fluid.", ColorReference.LIGHTRED));
			break;
		case 1:
			list.add(setTooltipData("This serves as an intermediate", ColorReference.LIGHTRED));
			list.add(setTooltipData("storage container for solid items.", ColorReference.LIGHTRED));
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
			return "tile.fluidtransport";
		case 1:
			return "tile.solidtransport";
		default:
			return "blaarg";
		}
	}
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		switch(itemstack.getItemDamage())
		{
		case 0:
			return "fluidtransport";
		case 1:
			return "solidtransport";
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