package mal.carbonization;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerTest extends Container
{
    private TileEntityMultiblockInit test;
    private int xdiff;
    private int ydiff;
    private int zdiff;

    public ContainerTest(InventoryPlayer par1InventoryPlayer, TileEntityMultiblockInit partest)
    {
        this.test = partest;
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if(xdiff != test.xdiff)
        	xdiff = test.xdiff;
        if(ydiff != test.ydiff)
        	ydiff = test.ydiff;
        if(zdiff != test.zdiff)
        	zdiff = test.zdiff;
    }

 

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.test.isUseableByPlayer(par1EntityPlayer);
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