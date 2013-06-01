package mal.carbonization;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockStructure extends ItemBlock {

	public ItemBlockStructure(int par1) {
		super(par1);
		this.setUnlocalizedName("ItemBlockStructure");
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://refined Iron
			list.add(setTooltipData("Made from refined iron.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 1 Structure", ColorReference.DARKCYAN));
			break;
		case 1://pig iron
			list.add(setTooltipData("Made from pig iron.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Oink oink.", ColorReference.PINK));
			list.add(setTooltipData("Tier 2 Structure", ColorReference.DARKCYAN));
			break;
		case 2://mild steel
			list.add(setTooltipData("Made from a high-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 3 Structure", ColorReference.DARKCYAN));
			break;
		case 3://steel
			list.add(setTooltipData("Made from a medium-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 4 Material", ColorReference.DARKCYAN));
			break;
		case 4://carbon
			list.add(setTooltipData("Made from carbon fibre", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 4 Material", ColorReference.DARKCYAN));
			break;
		case 5://reinforced carbon
			list.add(setTooltipData("Made from carbon fibre, ", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("reinforced with iron", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 5 Material", ColorReference.DARKCYAN));
			break;
		case 6://ice
			list.add(setTooltipData("Made from ice", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Dwarven engineering allows ", ColorReference.ORANGE));
			list.add(setTooltipData("for unmelting ice, even in ", ColorReference.ORANGE));
			list.add(setTooltipData("the hottest of temperatures.", ColorReference.ORANGE));
			list.add(setTooltipData("Tier 0 Material", ColorReference.DARKCYAN));
			break;
		default:
			list.add(setTooltipData("This isn't even an item!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
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
			return this.getUnlocalizedName()+"refinedIron";
		case 1:
			return this.getUnlocalizedName()+"pigIron";
		case 2:
			return this.getUnlocalizedName()+"mildSteel";
		case 3:
			return this.getUnlocalizedName()+"steel";
		case 4:
			return this.getUnlocalizedName()+"carbon";
		case 5:
			return this.getUnlocalizedName()+"refCarbon";
		case 6:
			return this.getUnlocalizedName()+"ice";
		default:
			return this.getUnlocalizedName()+"BlockStructure";
		}
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
			name = "refinedIron";
			break;
		case 1: 
			name = "pigIron"; 
			break;
		case 2:
			name = "mildSteel";
			break;
		case 3:
			name = "steel";
			break;
		case 4:
			name = "carbon";
			break;
		case 5:
			name = "reinforcedCarbon";
			break;
		case 6:
			name = "ice";
			break;
		default: name = "blarg";
		}
		return name;
	}

}
/*******************************************************************************
* Copyright (c) 2013 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
* 
* 
*********************************************************************************/