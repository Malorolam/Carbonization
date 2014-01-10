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

public class ItemBlockFurnaces extends ItemMultiTextureTile {
	
	private static String[] string = new String[]{"ironfurnace", "insulatedfurnace", "steelfurnace"};

	public ItemBlockFurnaces(int par1, Block block) {
		super(par1, block, string);
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://iron furnace tooltips
			list.add(setTooltipData("Iron surrounds the furnace, allowing", ColorReference.LIGHTRED));
			list.add(setTooltipData("for greater speed in smelting... somehow", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier 1 Furnace", ColorReference.DARKCYAN));
			break;
		case 1://insulated furnace
			list.add(setTooltipData("The presence of ceramic and more iron", ColorReference.LIGHTRED));
			list.add(setTooltipData("improves efficiency and allows for heat", ColorReference.LIGHTRED));
			list.add(setTooltipData("to be maintained between jobs.", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier 2 Furnace", ColorReference.DARKCYAN));
			break;
		case 2://steel furnace
			list.add(setTooltipData("Wrapping the furnace in steel", ColorReference.LIGHTRED));
			list.add(setTooltipData("helps efficiency even further", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier 3 Furnace", ColorReference.DARKCYAN));
			break;
		default:
			list.add(setTooltipData("This isn't even a furnace!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier Error Furnace", ColorReference.DARKCYAN));
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
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0: 
			name = "ironfurnace";
			break;
		case 4: 
			name = "insulatedfurnace"; 
			break;
		case 8:
			name = "steelfurnace";
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