package mal.carbonization;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import mal.carbonization.block.*;
import mal.carbonization.item.*;

public class carbonizationBlocks {

	public static final BlockFuel fuelBlock = new BlockFuel(Material.rock);
	public static final BlockFuelBlock blockFuelBlock = new BlockFuelBlock(Material.rock);
	public static final BlockStructureBlock structureBlock = new BlockStructureBlock(Material.iron);
	
	public static void init()
	{
		GameRegistry.registerBlock(fuelBlock, ItemBlockFuels.class, "blockFuel");
		GameRegistry.registerBlock(blockFuelBlock, ItemBlockFuelBlock.class, "blockFuelBlock");
		GameRegistry.registerBlock(structureBlock, "blockStructure");
	}
}
