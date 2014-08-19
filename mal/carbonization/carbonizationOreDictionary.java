package mal.carbonization;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class carbonizationOreDictionary {

	public static void init()
	{
		OreDictionary.registerOre("fuelPeat", new ItemStack(carbonizationItems.fuel, 1, 0));
		OreDictionary.registerOre("fuelAnthracite", new ItemStack(carbonizationItems.fuel, 1, 1));
		OreDictionary.registerOre("fuelGraphite", new ItemStack(carbonizationItems.fuel, 1, 2));
		OreDictionary.registerOre("fuelCoal", new ItemStack(Items.coal,1,0));
		OreDictionary.registerOre("fuelCharcoal", new ItemStack(Items.coal,1,1));
		OreDictionary.registerOre("dustCharcoal", new ItemStack(carbonizationItems.dust, 1, 0));
		OreDictionary.registerOre("dustPeat", new ItemStack(carbonizationItems.dust, 1, 1));
		OreDictionary.registerOre("dustCoal", new ItemStack(carbonizationItems.dust, 1, 2));
		OreDictionary.registerOre("dustAnthracite", new ItemStack(carbonizationItems.dust, 1, 3));
		OreDictionary.registerOre("dustGraphite", new ItemStack(carbonizationItems.dust, 1, 4));
		OreDictionary.registerOre("dustACharcoal", new ItemStack(carbonizationItems.dust,1,5));
		OreDictionary.registerOre("ingotCRefinedIron", new ItemStack(carbonizationItems.ingots,1,0));
		OreDictionary.registerOre("ingotPigIron", new ItemStack(carbonizationItems.ingots,1,1));
		OreDictionary.registerOre("ingotHCSteel", new ItemStack(carbonizationItems.ingots,1,2));
		OreDictionary.registerOre("ingotSteel", new ItemStack(carbonizationItems.ingots,1,3));
		OreDictionary.registerOre("ingotTi6Al4V", new ItemStack(carbonizationItems.ingots,1,4));
		OreDictionary.registerOre("ingotCoCr", new ItemStack(carbonizationItems.ingots,1,5));
		OreDictionary.registerOre("ingotTitanium", new ItemStack(carbonizationItems.ingots,1,6));
		OreDictionary.registerOre("ingotCobalt", new ItemStack(carbonizationItems.ingots,1,7));
		OreDictionary.registerOre("ingotChromium", new ItemStack(carbonizationItems.ingots,1,8));
		OreDictionary.registerOre("ingotAluminum", new ItemStack(carbonizationItems.ingots,1,9));
		OreDictionary.registerOre("blockPeat", new ItemStack(carbonizationBlocks.blockFuelBlock,1,0));
		OreDictionary.registerOre("blockAnthracite", new ItemStack(carbonizationBlocks.blockFuelBlock,1,1));
		OreDictionary.registerOre("blockGraphite", new ItemStack(carbonizationBlocks.blockFuelBlock,1,2));
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