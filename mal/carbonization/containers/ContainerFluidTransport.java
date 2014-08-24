package mal.carbonization.containers;

import mal.carbonization.tileentity.TileEntityFluidTransport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFluidTransport extends Container{

	TileEntityFluidTransport bench;
	
	public ContainerFluidTransport(InventoryPlayer player, TileEntityFluidTransport te)
	{
		bench = te;
		
		//upgrade
				this.addSlotToContainer(new Slot(bench, 0, 8, 11));
				this.addSlotToContainer(new Slot(bench, 1, 8, 29));
				this.addSlotToContainer(new Slot(bench, 2, 8, 47));
				
				for (int i = 0; i < 3; ++i)
		        {
		            for (int j = 0; j < 9; ++j)
		            {
		                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 81 + i * 18));
		            }
		        }

		        for (int i = 0; i < 9; ++i)
		        {
		            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 138));
		        }
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return bench.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot)
	{
		Slot itemslot = (Slot) this.inventorySlots.get(slot);
		ItemStack out = null;
		
		if(itemslot != null && itemslot.getHasStack())
		{
			ItemStack is = itemslot.getStack();
			out = is.copy();
			
			if(slot>=0 && slot<3)//upgrade
			{
				if(!this.mergeItemStack(is, 3, 39, true))
					return null;
				itemslot.onSlotChange(is, out);
			}
			else if(slot < 39)
			{
				if(!this.mergeItemStack(is, 0, 3, true))
					return null;
				itemslot.onSlotChange(is, out);
			}
			
			if(is.stackSize==0)
				itemslot.putStack(null);
			else
				itemslot.onSlotChanged();
			
			if(is.stackSize == out.stackSize)
				return null;
			
			itemslot.onPickupFromSlot(par1EntityPlayer, is);
		}
		return out;
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