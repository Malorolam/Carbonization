package mal.carbonization.containers;

import mal.carbonization.tileentity.TileEntityAutocraftingBench;
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

public class ContainerAutocraftingBench extends Container {

	private TileEntityAutocraftingBench bench;
	
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    
    private boolean populating = false;
	
	public ContainerAutocraftingBench(InventoryPlayer par1InventoryPlayer, TileEntityAutocraftingBench par2)
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
		
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 8 + i1 * 18, 11 + l * 18));
            }
        }
        
        this.addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 102, 29));
/*		//crafting
		for(int i = 0; i<3; i++)
			for(int j = 0; j<3; j++)
				this.addSlotToContainer(new Slot(bench, 24+i+j*3, 7+18*i, 10+18*j));
		//output
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, bench, 33, 97, 25));*/
		
		//upgrade
		this.addSlotToContainer(new Slot(bench, 34, 134, 11));
		this.addSlotToContainer(new Slot(bench, 35, 134, 29));
		this.addSlotToContainer(new Slot(bench, 36, 134, 47));
		
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
        
        populateCraftingMatrix();
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return bench.isUseableByPlayer(entityplayer);
	}
	
	private void populateCraftingMatrix()
	{
		populating = true;
		for(int i = 0; i < 3; i++)
        	for(int j=0; j<3; j++)
        	{
        		//System.out.println("Set crafting slot " + (i+j*3) + " with item stack: " + ((bench.getStackInSlot(24+i+j*3)==null)?"null":bench.getStackInSlot(24+i+j*3).toString()));
        		craftMatrix.setInventorySlotContents(i+j*3, bench.getStackInSlot(24+i+j*3));
        	}
        
		craftResult.setInventorySlotContents(0, bench.getStackInSlot(33));
		populating = false;
	}
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
    	if(populating)
    		return;
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, bench.getWorldObj()));
        
        //update the tile entity
        for(int i = 0; i < 3; i++)
        	for(int j=0; j<3; j++)
        	{
        		//System.out.println("Set tile entity slot " + (24+i+j*3) + " with item stack: " + ((craftMatrix.getStackInSlot(i+3*j)==null)?"null":craftMatrix.getStackInSlot(i+3*j).toString()));
        		bench.setInventorySlotContents(24+i+j*3, craftMatrix.getStackInSlot(i+3*j));
        	}
        
        bench.setInventorySlotContents(33, this.craftResult.getStackInSlot(0));
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
            	if (!this.mergeItemStack(var5, 35, 70, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if(slot>=12 && slot <24)//output
            {
            	if (!this.mergeItemStack(var5, 35, 70, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);

            }
            else if (slot >= 24 && slot < 36)//crafting
            {
                if (!this.mergeItemStack(var5, 35, 70, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slot > 35)//inventory
            {
                if (slot > 35 && slot < 62)
                {
                    if (!this.mergeItemStack(var5, 0, 12, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(var5, 36, 62, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 36, 70, false))
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