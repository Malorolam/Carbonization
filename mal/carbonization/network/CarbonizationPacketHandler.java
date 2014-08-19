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
		instance.registerMessage(MultiblockInitMessageServer.class, MultiblockInitMessageServer.class, 1, Side.SERVER);
		instance.registerMessage(StructureBlockMessage.class, StructureBlockMessage.class, 2, Side.CLIENT);
		instance.registerMessage(FurnacesMessage.class, FurnacesMessage.class, 3, Side.CLIENT);
		instance.registerMessage(MultiblockFurnaceMessageClient.class, MultiblockFurnaceMessageClient.class, 4, Side.SERVER);
		instance.registerMessage(MultiblockFurnaceMessageServer.class, MultiblockFurnaceMessageServer.class, 5, Side.CLIENT);
		instance.registerMessage(AutocraftingBenchMessageClient.class, AutocraftingBenchMessageClient.class, 6, Side.SERVER);
		instance.registerMessage(AutocraftingBenchMessageServer.class, AutocraftingBenchMessageServer.class, 7, Side.CLIENT);
		instance.registerMessage(AutocraftingBenchMessageServerSync.class, AutocraftingBenchMessageServerSync.class, 8, Side.CLIENT);
		instance.registerMessage(FuelConversionBenchMessageClient.class, FuelConversionBenchMessageClient.class, 9, Side.SERVER);
		instance.registerMessage(FuelConversionBenchMessageServer.class, FuelConversionBenchMessageServer.class, 10, Side.CLIENT);
		instance.registerMessage(TunnelBoreMessageClient.class, TunnelBoreMessageClient.class, 11, Side.SERVER);
		instance.registerMessage(TunnelBoreMessageServer.class, TunnelBoreMessageServer.class, 12, Side.CLIENT);
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