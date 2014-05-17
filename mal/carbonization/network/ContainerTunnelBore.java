package mal.carbonization.network;

import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityTunnelBore;
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

public class ContainerTunnelBore extends Container {

	private TileEntityTunnelBore bore;
	
	public ContainerTunnelBore(InventoryPlayer par1InventoryPlayer, TileEntityTunnelBore par2)
	{
		bore = par2;
		
		//upgrade
		for(int i = 0; i<3; i++)
			this.addSlotToContainer(new Slot(bore, i, 116, 10+18*i));
		//dimension
		for(int i = 0; i<2; i++)
			this.addSlotToContainer(new Slot(bore, 3+i, 8+18*i, 11));
		
		//fuel
		this.addSlotToContainer(new Slot(bore, 5, 134, 10));
		
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 81 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 138));
        }
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return bore.isUseableByPlayer(entityplayer);
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

            if(slot>=0 && slot <3)//upgrade
            {
            	if (!this.mergeItemStack(var5, 6, 42, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if(slot>=3 && slot <5)//Dimension
            {
            	if (!this.mergeItemStack(var5, 6, 42, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot >=5 && slot < 6)//fuel
            {
            	if (!this.mergeItemStack(var5, 6, 42, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot >= 6 && bore.canInsertItem(5, var5, 0))//inventory
            {
            	if (!this.mergeItemStack(var5, 5, 6, false))
            	{
            		return null;
            	}
            }
            else if (slot < 33)
            {
            	if (!this.mergeItemStack(var5, 33, 42, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 6, 33, false))
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