package mal.carbonization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

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
		return null;
	}

	//returns an instance of the Gui
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityFurnaces){
                    return new GuiFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
            }
            if(tileEntity instanceof TileEntityMultiblockInit){
            	//System.out.println("got to make the gui client side");
            	return new GuiMultiblockInit((TileEntityMultiblockInit)tileEntity, player);
            }
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