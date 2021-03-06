package mal.carbonization.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFuels extends ItemBlock {

	public ItemBlockFuels(Block block) {
		super(block);
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
			return this.getUnlocalizedName()+"anthracite";
		case 2:
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
			name = "anthracite";
			break;
		case 2:
			name = "graphite";
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