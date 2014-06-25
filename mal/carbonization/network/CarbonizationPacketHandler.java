package mal.carbonization.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import mal.carbonization.carbonization;
import mal.core.network.MalCorePacketHandler;

public class CarbonizationPacketHandler extends MalCorePacketHandler{
public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(carbonization.MODID.toLowerCase());
	
	public static void init()
	{
		instance.registerMessage(MultiblockInitMessage.class, MultiblockInitMessage.class, 0, Side.CLIENT);
		instance.registerMessage(StructureBlockMessage.class, StructureBlockMessage.class, 1, Side.CLIENT);
	}

}
