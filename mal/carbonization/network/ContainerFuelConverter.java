package mal.carbonization.network;

import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelConverter;
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

	private TileEntityFuelConverter bench;
    
    private boolean populating = false;
	
	public ContainerFuelConverter(InventoryPlayer par1InventoryPlayer, TileEntityFuelConverter par2)
	{
		bench = par2;
		
		//input
		for(int i = 0; i<4; i++)
			for(int j = 0; j<3; j++)
				this.addSlotToContainer(new Slot(bench, i+j*4, 8+18*i, 80+18*j));
		//output
		for(int i = 0; i<4; i++)
			for(int j = 0; j<3; j++)
				this.addSlotToContainer(new Slot(bench, 12+i+j*4, 98+18*i, 80+18*j));
		
		//upgrade
		this.addSlotToContainer(new Slot(bench, 24, 134, 11));
		this.addSlotToContainer(new Slot(bench, 25, 134, 29));
		//fuel
		this.addSlotToContainer(new Slot(bench, 26, 134, 47));
		
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

            if(slot>=0 && slot <12)//input
            {
            	if (!this.mergeItemStack(var5, 35, 63, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if(slot>=12 && slot <24)//output
            {
            	if (!this.mergeItemStack(var5, 35, 63, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if (slot >= 24 && slot < 27)//upgrade
            {
                if (!this.mergeItemStack(var5, 35, 63, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot > 26)//inventory
            {
                if (slot > 26 && slot < 53)
                {
                    if (!this.mergeItemStack(var5, 0, 12, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(var5, 27, 53, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 36, 63, false))
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
 * Copyright (c) 2013 Malorolam.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * 
 *********************************************************************************/