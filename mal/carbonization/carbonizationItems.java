package mal.carbonization;

import cpw.mods.fml.common.registry.GameRegistry;
import mal.carbonization.item.*;

public class carbonizationItems {

	public static final ItemFuel fuel = new ItemFuel();
	
	public static void init()
	{
		GameRegistry.registerItem(fuel, "itemfuel");
	}
}
