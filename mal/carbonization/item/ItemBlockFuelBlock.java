package mal.carbonization.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFuelBlock extends ItemBlock{

	public ItemBlockFuelBlock(Block block) {
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
			return this.getUnlocalizedName()+"peatblock";
		case 1:
			return this.getUnlocalizedName()+"anthraciteblock";
		case 2:
			return this.getUnlocalizedName()+"graphiteblock";
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
			name = "peatblock";
			break;
		case 1:
			name = "anthraciteblock";
			break;
		case 2:
			name = "graphiteblock";
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