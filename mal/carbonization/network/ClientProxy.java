package mal.carbonization.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mal.carbonization.gui.*;
import mal.carbonization.render.FluidTransportItemRenderer;
import mal.carbonization.render.FluidTransportRenderer;
import mal.carbonization.render.StructureBlockRenderer;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.carbonizationItems;
import mal.carbonization.render.StructureItemRenderer;
import mal.carbonization.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	public static int renderPass;
	
	@Override
	public void setCustomRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(carbonizationItems.structureItem, new StructureItemRenderer());
		RenderingRegistry.registerBlockHandler(new StructureBlockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTransport.class, new FluidTransportRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(carbonizationBlocks.transportBlock), new FluidTransportItemRenderer());
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
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityFurnaces){
                return new GuiFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
        }
        if(tileEntity instanceof TileEntityMultiblockInit)
        {
        	return new GuiMultiblockFurnaceInit((TileEntityMultiblockInit)tileEntity, player);
        }
        if(tileEntity instanceof TileEntityFluidTransport)
        	return new GuiFluidTransport(player, (TileEntityFluidTransport)tileEntity);
        if(tileEntity instanceof TileEntityMultiblockFurnace){
        	//System.out.println("got to make the gui client side");
        	return new GuiMultiblockFurnace((TileEntityMultiblockFurnace)tileEntity, player.inventory);
        }
        if(tileEntity instanceof TileEntityAutocraftingBench) {
        	//System.out.println("got to make the gui client side");
        	return new GuiAutocraftingBench(player.inventory, (TileEntityAutocraftingBench)tileEntity);
        }
        if(tileEntity instanceof TileEntityFuelConversionBench) {
        	//System.out.println("got to make the gui client side");
        	return new GuiFuelConverter(player.inventory, (TileEntityFuelConversionBench)tileEntity);
        }
/*        if(tileEntity instanceof TileEntityFuelCellFiller)
        	return new GuiFuelCellFiller(player.inventory, (TileEntityFuelCellFiller)tileEntity);*/
        if(tileEntity instanceof TileEntityTunnelBore)
        	return new GuiTunnelBore(player.inventory, (TileEntityTunnelBore)tileEntity);
        
        if(id == 4 && player.getCurrentEquippedItem() != null && Item.getIdFromItem(player.getCurrentEquippedItem().getItem()) == Item.getIdFromItem(carbonizationItems.portablescannerItem))
        {
        	return new GuiPortableScanner(player.inventory, new PortableScannerWrapper(player.getCurrentEquippedItem()));
        }
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