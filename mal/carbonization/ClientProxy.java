package mal.carbonization;

import cpw.mods.fml.client.FMLClientHandler;
import mal.carbonization.gui.GuiFurnaces;
import mal.carbonization.gui.GuiMultiblockFurnace;
import mal.carbonization.gui.GuiMultiblockInit;
import mal.carbonization.gui.GuiTest;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.carbonization.tileentity.TileEntityTest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings()
	{

	}
	
	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityFurnaces){
                return new GuiFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
        }
        if(tileEntity instanceof TileEntityMultiblockInit){
        	//System.out.println("got to make the gui client side");
        	return new GuiMultiblockInit((TileEntityMultiblockInit)tileEntity, player);
        }
        if(tileEntity instanceof TileEntityMultiblockFurnace){
        	//System.out.println("got to make the gui client side");
        	return new GuiMultiblockFurnace((TileEntityMultiblockFurnace)tileEntity, player.inventory);
        }
        if(tileEntity instanceof TileEntityTest)
        	return new GuiTest((TileEntityTest)tileEntity, player);
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