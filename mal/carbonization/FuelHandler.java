package mal.carbonization;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
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
			default:
				return 0;
			}
		}
		else
			return 0;
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