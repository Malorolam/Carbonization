package mal.carbonization;

import mal.carbonization.tileentity.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class carbonizationTileEntities {

	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityStructureBlock.class, "TileEntityStructureBlock");
		GameRegistry.registerTileEntity(TileEntityMultiblockInit.class, "TileEntityMultiblockInit");
		GameRegistry.registerTileEntity(TileEntityFurnaces.class, "TileEntityFurnaces");
		GameRegistry.registerTileEntity(TileEntityMultiblockFurnace.class, "TileEntityMultiblockFurnace");
		GameRegistry.registerTileEntity(TileEntityAutocraftingBench.class, "TileEntityAutocraftingBench");
		GameRegistry.registerTileEntity(TileEntityFuelConversionBench.class, "TileEntityFuelConversionBench");
		GameRegistry.registerTileEntity(TileEntityTunnelBore.class, "TileEntityTunnelBore");
	}
}
