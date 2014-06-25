package mal.carbonization;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import mal.carbonization.item.*;
import mal.carbonization.network.CommonProxy;

public class carbonizationItems {

	public static final ItemFuel fuel = new ItemFuel();
	public static final ItemStructureBlock structureItem = new ItemStructureBlock();
	
	public static void init()
	{
		GameRegistry.registerItem(fuel, "itemfuel");
		GameRegistry.registerItem(structureItem, "structureBlock");
	}
}
