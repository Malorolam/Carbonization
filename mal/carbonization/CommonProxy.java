package mal.carbonization;

import mal.carbonization.network.*;
import mal.carbonization.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void registerRenderThings()
	{
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);
		if (tileEntity instanceof TileEntityFurnaces)
			return new ContainerFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
        if(tileEntity instanceof TileEntityMultiblockInit){
        	//System.out.println("got to make the gui server side");
        	return new ContainerMultiblockInit(player.inventory, (TileEntityMultiblockInit)tileEntity);
        }
        if(tileEntity instanceof TileEntityMultiblockFurnace){
        	//System.out.println("got to make the gui server side");
        	return new ContainerMultiblockFurnace(player.inventory, (TileEntityMultiblockFurnace)tileEntity);
        }
        if(tileEntity instanceof TileEntityAutocraftingBench)
        	return new ContainerAutocraftingBench(player.inventory, (TileEntityAutocraftingBench)tileEntity);
        if(tileEntity instanceof TileEntityFuelConverter)
        	return new ContainerFuelConverter(player.inventory, (TileEntityFuelConverter)tileEntity);
        if(tileEntity instanceof TileEntityFuelCellFiller)
        	return new ContainerFuelCellFiller(player.inventory, (TileEntityFuelCellFiller)tileEntity);
        if(ID==4 && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().itemID == carbonization.portableScanner.itemID)
        	return new ContainerPortableScanner(player.inventory, new PortableScannerWrapper(player.getCurrentEquippedItem()));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public World getClientWorld()
    {
        return null;
    }
    
    public void setCustomRenderers()
    {
    	
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