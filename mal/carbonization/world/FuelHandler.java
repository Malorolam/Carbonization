package mal.carbonization.world;

import mal.carbonization.carbonizationBlocks;
import mal.carbonization.carbonizationItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) 
	{
		if(fuel.getItem().equals(carbonizationItems.fuel))
		{
			switch (fuel.getItemDamage())
			{
			case 0:
				return 1200;
			case 1:
				return 2400;
			case 2:
				return 800;
			default:
				return 0;
			}
		}
		else if(fuel.getItem().equals(Item.getItemFromBlock(carbonizationBlocks.blockFuelBlock)))
		{
			switch(fuel.getItemDamage())
			{
			case 0:
				return 10800;
			case 1:
				return 21600;
			case 2:
				return 7200;
			}
		}

		return 0;
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