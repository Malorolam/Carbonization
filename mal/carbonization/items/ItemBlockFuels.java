package mal.carbonization.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFuels extends ItemBlock {

	public ItemBlockFuels(int par1) {
		super(par1);
		this.setUnlocalizedName("ItemBlockFuels");
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return this.getUnlocalizedName()+"peat";
		case 1:
			return this.getUnlocalizedName()+"lignite";
		case 2:
			return this.getUnlocalizedName()+"sBitCoal";
		case 3:
			return this.getUnlocalizedName()+"bitCoal";
		case 4:
			return this.getUnlocalizedName()+"anthracite";
		case 5:
			return this.getUnlocalizedName()+"graphite";
		default:
			return this.getUnlocalizedName()+"BlockFuel";
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
			name = "peat";
			break;
		case 1: 
			name = "lignite"; 
			break;
		case 2:
			name = "sBituminous";
			break;
		case 3:
			name = "bituminous";
			break;
		case 4:
			name = "anthracite";
			break;
		case 5:
			name = "graphite";
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