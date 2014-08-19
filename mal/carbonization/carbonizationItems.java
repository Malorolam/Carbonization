package mal.carbonization;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import mal.carbonization.fluids.BucketHandler;
import mal.carbonization.item.*;
import mal.carbonization.network.CommonProxy;

public class carbonizationItems {

	public static final ItemFuel fuel = new ItemFuel();
	public static final ItemStructureBlock structureItem = new ItemStructureBlock();
	public static final ItemScrewdriver screwdriverItem = new ItemScrewdriver();
	public static final ItemPortableScanner portablescannerItem = new ItemPortableScanner();
	public static final ItemDust dust = new ItemDust();
	public static final ItemIngots ingots = new ItemIngots();
	public static final ItemFuelPotentialBucket fpBucket = new ItemFuelPotentialBucket(carbonizationBlocks.blockFuelPotential);
	public static final ItemMisc miscItem = new ItemMisc();
	public static final ItemHHCompressor hhcomp = new ItemHHCompressor();
	public static final ItemHHPulverizer hhpulv = new ItemHHPulverizer();
	public static final ItemUpgradeItems upgradeItem = new ItemUpgradeItems();
	
	public static final ItemStack HHPulv = new ItemStack(hhpulv, 1, OreDictionary.WILDCARD_VALUE);
	public static final ItemStack HHComp = new ItemStack(hhcomp,1,OreDictionary.WILDCARD_VALUE);
	
	public static void init()
	{
		GameRegistry.registerItem(fuel, "itemfuel");
		GameRegistry.registerItem(dust, "itemdust");
		GameRegistry.registerItem(ingots, "itemingots");
		GameRegistry.registerItem(structureItem, "structureBlock");
		GameRegistry.registerItem(screwdriverItem, "itemscrewdriver");
		GameRegistry.registerItem(portablescannerItem, "portablescanner");
		GameRegistry.registerItem(fpBucket, "fpbucket");
		GameRegistry.registerItem(miscItem, "itemmisc");
		GameRegistry.registerItem(hhcomp, "hhcomp");
		GameRegistry.registerItem(hhpulv, "hhpulv");
		GameRegistry.registerItem(upgradeItem, "itemupgrade");
		
		hhcomp.setContainerItem(hhcomp);
		hhpulv.setContainerItem(hhpulv);
		
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("fuelpotential", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(fpBucket), new ItemStack(Items.bucket));
		BucketHandler.instance.bucketMap.put(carbonizationBlocks.blockFuelPotential, fpBucket);
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