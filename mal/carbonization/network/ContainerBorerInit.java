package mal.carbonization.network;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.tileentity.TileEntityMultiblockBoreInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerBorerInit extends Container
{
    private TileEntityMultiblockBoreInit test;
    private int xdiff;
    private int ydiff;

    public ContainerBorerInit(InventoryPlayer par1InventoryPlayer, TileEntityMultiblockBoreInit partest)
    {
        this.test = partest;
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.test.isUseableByPlayer(par1EntityPlayer);
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