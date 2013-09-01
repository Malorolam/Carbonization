package mal.carbonization.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerMultiblockFurnace extends Container{
	
	private TileEntityMultiblockFurnace furnace;
    private int lastQueueNum = 0;
    private float lastFuelTime = 0;
    private int lastSlagNum = 0;

    public ContainerMultiblockFurnace(InventoryPlayer par1InventoryPlayer, TileEntityMultiblockFurnace par2TileEntityFurnace)
    {
        this.furnace = par2TileEntityFurnace;
        int i;
        
        //input slots, so only 0-8
        for(i=0; i<3; i++)
        {
        	for(int j = 0; j < 3; j++)
        		this.addSlotToContainer(new Slot(furnace, i*3+j, 6+j*18, 17+i*18));
        }
        
        //output slots, so only 9-17
        for(i=0; i<3; i++)
        {
        	for(int j = 0; j < 3; j++)
        		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, furnace, i*3+j+9, 80+j*18, 17+i*18));
        }

        //main inventory, so 18-44
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9+9, 8 + j * 18, 84 + i * 18));
            }
        }

        //hotbar, so 45-53
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, (int)this.furnace.getFuelStack());
        par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.getNumQueueJobs());
        par1ICrafting.sendProgressBarUpdate(this, 2, this.furnace.slagTank);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.lastFuelTime != this.furnace.getFuelStack())
            {
                var2.sendProgressBarUpdate(this, 0, (int)this.furnace.getFuelStack());
            }

            if (this.lastQueueNum != this.furnace.getNumQueueJobs())
            {
                var2.sendProgressBarUpdate(this, 1, this.furnace.getNumQueueJobs());
            }
            
            if (this.lastSlagNum != this.furnace.slagTank)
            {
                var2.sendProgressBarUpdate(this, 2, this.furnace.slagTank);
            }
        }

        this.lastFuelTime = this.furnace.getFuelStack();
        this.lastQueueNum = this.furnace.getNumQueueJobs();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.furnace.setFuelStack(par2);
        }

        if (par1 == 1)
        {
            this.furnace.setNumQueueJobs(par2);
        }
        
        if (par1 == 2)
        {
        	this.furnace.slagTank = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;//this.furnace.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            //output slots
            if (par2 >= 9 && par2 < 18)
            {
                if (!this.mergeItemStack(var5, 18, 54, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (par2 >=18)//inventory
            {
            	/**
            	 * This seems a little silly, since the furnace can handle things that don't have recipes, but preventing the shift-clicking
            	 * of non-recipe things makes life simpler, since the invalid item just goes to output immediately.
            	 */
                if (CarbonizationRecipes.smelting().getMultiblockCookTime(var5) != -1)//has a multiblock recipe
                {
                    if (!this.mergeItemStack(var5, 0, 8, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityMultiblockFurnace.getItemBurnTime(var5)>0)
                {
                    if (!this.mergeItemStack(var5, 0, 8, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 18 && par2 < 45)
                {
                    if (!this.mergeItemStack(var5, 45, 53, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 45 && par2 < 54 && !this.mergeItemStack(var5, 18, 44, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 18, 53, false))
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