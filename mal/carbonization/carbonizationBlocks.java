package mal.carbonization;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import mal.carbonization.block.*;
import mal.carbonization.item.ItemBlockFuels;

public class carbonizationBlocks {

	public static final BlockFuel fuelBlock = new BlockFuel(Material.rock);
	
	public static void init()
	{
		GameRegistry.registerBlock(fuelBlock, ItemBlockFuels.class, "blockFuel");
	}
}
