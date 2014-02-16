package mal.carbonization.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.FuelSlot;
import mal.carbonization.StructureBlockSlot;
import mal.carbonization.multiblockFurnaceSlot;
import mal.carbonization.items.ItemPortableScanner;
import mal.core.UtilReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPortableScanner extends Container{
	
	private PortableScannerWrapper stack;
	public InventoryPlayer player;
	private int islot;
	private int mode;

	public ContainerPortableScanner(InventoryPlayer player, PortableScannerWrapper wrap)
	{
		this.stack = wrap;
		this.player = player;
		islot = player.currentItem;
		mode = ((ItemPortableScanner)wrap.getStack().getItem()).getMode(wrap.getStack());
		//System.out.println("const: "+mode);
        int i;
        
        //upgrade slots, 0-2
        for(i=0; i<3; i++)
        {
        	this.addSlotToContainer(new StructureBlockSlot(stack,i,8+i*18, 11,0));
        }
        
        //fuel slots, 3
        this.addSlotToContainer(new FuelSlot(stack,3, 134, 10,true));
        
        //mode slot, 4
        this.addSlotToContainer(new StructureBlockSlot(stack,4,98,10,1,false,true));

        //main inventory
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(player, j + i * 9+9, 8 + j * 18, 81 + i * 18));
            }
        }

        //hotbar, so 45-53
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 138));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		if(UtilReference.compareStacks(player.inventory.mainInventory[islot], stack.getStack(), false))
		{
			player.inventory.mainInventory[islot] = stack.getStack();
		}
		else
		{
			player.dropPlayerItem(((Slot)this.inventorySlots.get(0)).getStack());
		}
		super.onContainerClosed(player);
	}
	/**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
	//TODO: Make it actually do stuff instead of being lazy
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
    	return null;
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