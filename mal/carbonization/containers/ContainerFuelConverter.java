package mal.carbonization.containers;

import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelConversionBench;
import mal.carbonization.tileentity.TileEntityFurnaces;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFuelConverter extends Container {

	private TileEntityFuelConversionBench bench;

	private boolean populating = false;

	public ContainerFuelConverter(InventoryPlayer par1InventoryPlayer, TileEntityFuelConversionBench par2)
	{
		bench = par2;

		int slot = 0;
		//input
		for(int i = 0; i<9; i++)
			for(int j = 0; j<3; j++)
				this.addSlotToContainer(new Slot(bench, slot++, 8+18*i, 80+18*j));

		//upgrade
		this.addSlotToContainer(new Slot(bench, slot++, 152, 11));
		this.addSlotToContainer(new Slot(bench, slot++, 152, 29));
		this.addSlotToContainer(new Slot(bench, slot++, 152, 47));

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 139 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 196));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return bench.isUseableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot)
	{
		ItemStack var3 = null;
		Slot var4 = (Slot)this.inventorySlots.get(slot);

		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if(slot>=0 && slot <27)//input
			{
				if (!this.mergeItemStack(var5, 30, 65, true))
				{
					return null;
				}

				var4.onSlotChange(var5, var3);

			}
			else if (slot >= 27 && slot < 30)//upgrade
			{
				if (!this.mergeItemStack(var5, 30, 65, true))
				{
					return null;
				}

				var4.onSlotChange(var5, var3);
			}
			else if (slot >= 30)//inventory
			{
				if (!this.mergeItemStack(var5, 0, 26, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 30, 63, false))
			{
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack)null);
			}
			else
			{
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize)
			{
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
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