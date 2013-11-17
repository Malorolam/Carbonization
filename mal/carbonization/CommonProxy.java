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
        if(tileEntity instanceof TileEntityTest)
        	return new ContainerTest(player.inventory, (TileEntityTest)tileEntity);
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