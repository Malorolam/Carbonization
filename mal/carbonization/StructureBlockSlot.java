package mal.carbonization;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class StructureBlockSlot extends Slot {

	private int allowedPurpose;
	private boolean onlyStatedPurpose;
	private boolean greaterThan;
	
	public StructureBlockSlot(IInventory inv, int slot, int x, int y, int purpose) 
	{
		this(inv,slot,x,y,purpose,true,false);
	}
	public StructureBlockSlot(IInventory inv, int slot, int x, int y, int purpose, boolean onlyStatedPurpose, boolean greaterThan)
	{
		super(inv, slot, x, y);
		allowedPurpose = purpose;
		this.onlyStatedPurpose = onlyStatedPurpose;
		this.greaterThan = greaterThan;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		if(is.getItem().itemID != carbonization.itemStructureBlock.itemID)
			return false;
		if(onlyStatedPurpose)
		{
			if(is.getItemDamage()/1000 == allowedPurpose)
				return true;
		}
		else
		{
			if(greaterThan)//if purposes larger are allowed
			{
				if(is.getItemDamage()/1000 >= allowedPurpose)
					return true;
			}
			else
				if(is.getItemDamage()/1000 <= allowedPurpose)
					return true;
		}
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