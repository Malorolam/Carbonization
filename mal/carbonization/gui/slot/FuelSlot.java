package mal.carbonization.gui.slot;

import mal.core.api.IFuelContainer;
import mal.core.reference.UtilReference;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FuelSlot extends Slot {

	private boolean allowFuelCells;
	public FuelSlot(IInventory par1iInventory, int par2, int par3, int par4, boolean allowCell) 
	{
		super(par1iInventory, par2, par3, par4);
		allowFuelCells = allowCell;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		if(!allowFuelCells && is.getItem() instanceof IFuelContainer)
			return false;
		if(UtilReference.getItemBurnTime(is, 1, false)>0)
			return true;
		return false;
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