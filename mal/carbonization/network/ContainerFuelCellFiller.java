package mal.carbonization.network;

import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelCellFiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerFuelCellFiller extends Container {

	private TileEntityFuelCellFiller bench;
	
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    
    private boolean populating = false;
	
	public ContainerFuelCellFiller(InventoryPlayer par1InventoryPlayer, TileEntityFuelCellFiller par2)
	{
		bench = par2;
		
		//input
		this.addSlotToContainer(new Slot(bench, 0, 39, 52));
		for(int i = 0; i<3; i++)
			this.addSlotToContainer(new Slot(bench, i+1, 21, 52-18*i));
		
		//output
		this.addSlotToContainer(new Slot(bench, 4, 99, 53));
		
		//upgrade
		for(int i = 0; i < 3; i++)
			this.addSlotToContainer(new Slot(bench, 5+i, 145, 16+18*i));
		
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
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

            if(slot>=0 && slot <4)//input
            {
            	if (!this.mergeItemStack(var5, 8, 44, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if(slot>=4 && slot <5)//output
            {
            	if (!this.mergeItemStack(var5, 8, 44, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot >=5 && slot < 8)//upgrade
            {
            	if (!this.mergeItemStack(var5, 8, 44, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot >= 8 && bench.canInsertItem(0, var5, 0))//inventory
            {
            	if (!this.mergeItemStack(var5, 0, 4, false))
            	{
            		return null;
            	}
            }
            else if (slot < 35)
            {
            	if (!this.mergeItemStack(var5, 35, 44, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 8, 44, false))
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