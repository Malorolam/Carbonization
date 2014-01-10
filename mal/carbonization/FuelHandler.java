package mal.carbonization;

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
		if(fuel.itemID == carbonization.fuel.itemID)
		{
			switch (fuel.getItemDamage())
			{
			case 0:
				return 600;
			case 1:
				return 800;
			case 2:
				return 1000;
			case 3:
				return 1200;
			case 4:
				return 2000;
			case 5:
				return 333;
			default:
				return 0;
			}
		}
		else
		{
			int i = fuel.getItem().itemID;
			Item item = fuel.getItem();

			if (fuel.getItem() instanceof ItemBlock && Block.blocksList[i] != null)
			{
				Block block = Block.blocksList[i];

				if (block == Block.woodSingleSlab)
				{
					return 150;
				}
				
				if (block.blockMaterial == Material.wood)
				{
					return 300;
				}

				if (block == Block.coalBlock)
				{
					return 16000;
				}
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