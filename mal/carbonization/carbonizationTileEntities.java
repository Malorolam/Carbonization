package mal.carbonization;

import mal.carbonization.tileentity.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class carbonizationTileEntities {

	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityStructureBlock.class, "TileEntityStructureBlock");
		GameRegistry.registerTileEntity(TileEntityMultiblockInit.class, "TileEntityMultiblockInit");
	}
}
