package mal.carbonization;

import mal.core.reference.UtilReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) 
	{
		if(UtilReference.areItemStacksEqualItem(fuel, new ItemStack(carbonizationItems.fuel), false, false))
		{
			switch (fuel.getItemDamage())
			{
			case 0:
				return 1200;
			case 1:
				return 2400;
			case 2:
				return 1000;
			default:
				return 0;
			}
		}
		else if(UtilReference.areItemStacksEqualItem(fuel, new ItemStack(carbonizationBlocks.blockFuelBlock), false, false))
		{
			switch(fuel.getItemDamage())
			{
			case 6:
				return 10800;
			case 7:
				return 21600;
			case 8:
				return 9000;
			}
		}
/*		else
		{
			int i = Item.getIdFromItem(fuel.getItem());
			Item item = fuel.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != null)
			{
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab)
				{
					return 150;
				}
				
				if (block.blockMaterial == Material.wood)
				{
					return 300;
				}

				if (block == Blocks.coal_block)
				{
					return 16000;
				}
			}
        }*/
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