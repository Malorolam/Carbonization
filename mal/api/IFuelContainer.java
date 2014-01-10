package mal.api;

import net.minecraft.item.ItemStack;

/**
 * @author Mal
 * 
 * Basically, this interface allows other machines to interact with fuel cells properly instead of ignoring the
 * or treating them like fuel, which would consume them
 * 
 * An example TileEntity in the form of a modified TileEntityFurnace is provided as an example
 */
public interface IFuelContainer {

	/**
	 * 
	 * @param is, 		the itemstack of the inventory item to interact with
	 * @param fuelTime	the amount of fuel to add/remove
	 * @param absolute	if this is true, the fuelTime value will be the new value, not added to the existing value
	 */
	public boolean setFuel(ItemStack is, int fuelTime, boolean absolute);
	
	/**
	 * 
	 * @param is		the itemstack of the inventory item to interact with
	 * @return			the amount of fuel in that itemstack
	 */
	public int getFuelValue(ItemStack is);
}

/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/