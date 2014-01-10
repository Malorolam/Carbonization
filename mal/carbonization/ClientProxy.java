package mal.carbonization;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mal.carbonization.gui.GuiAutocraftingBench;
import mal.carbonization.gui.GuiFuelCellFiller;
import mal.carbonization.gui.GuiFuelConverter;
import mal.carbonization.gui.GuiFurnaces;
import mal.carbonization.gui.GuiMultiblockFurnace;
import mal.carbonization.gui.GuiMultiblockInit;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelCellFiller;
import mal.carbonization.tileentity.TileEntityFuelConverter;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	public static int renderPass;
	
	@Override
	public void setCustomRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(carbonization.itemStructureBlock.itemID, new StructureItemRenderer());
	}
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
        if(tileEntity instanceof TileEntityAutocraftingBench) {
        	//System.out.println("got to make the gui client side");
        	return new GuiAutocraftingBench(player.inventory, (TileEntityAutocraftingBench)tileEntity);
        }
        if(tileEntity instanceof TileEntityFuelConverter) {
        	//System.out.println("got to make the gui client side");
        	return new GuiFuelConverter(player.inventory, (TileEntityFuelConverter)tileEntity);
        }
        if(tileEntity instanceof TileEntityFuelCellFiller)
        	return new GuiFuelCellFiller(player.inventory, (TileEntityFuelCellFiller)tileEntity);
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