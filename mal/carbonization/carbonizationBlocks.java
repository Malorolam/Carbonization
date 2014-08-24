package mal.carbonization;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import mal.carbonization.block.*;
import mal.carbonization.fluids.BlockFluidFuelPotential;
import mal.carbonization.item.*;

public class carbonizationBlocks {

	public static final BlockFuel fuelBlock = new BlockFuel(Material.rock);
	public static final BlockFuelBlock blockFuelBlock = new BlockFuelBlock(Material.rock);
	public static final BlockStructureBlock structureBlock = new BlockStructureBlock(Material.iron);
	public static final BlockFurnaces furnaceBlock = new BlockFurnaces();
	public static final BlockScaffolding scaffoldBlock = new BlockScaffolding(Material.glass);
	public static final BlockFurnaceControl furnaceControlBlock = new BlockFurnaceControl(Material.iron);
	public static final BlockMultiblockFurnaceControl multiblockFurnaceControlBlock = new BlockMultiblockFurnaceControl(Material.iron);
	public static final BlockAutocraftingBench autocraftingbenchBlock = new BlockAutocraftingBench(Material.iron);
	public static final BlockTunnelBore tunnelboreBlock = new BlockTunnelBore(Material.iron);
	public static final BlockTransport transportBlock = new BlockTransport(Material.iron);
	
	public static Fluid fluidFuelPotential = new Fluid("fuelpotential").setViscosity(5000);
	public static BlockFluidFuelPotential blockFuelPotential;
	
	public static void init()
	{
		GameRegistry.registerBlock(fuelBlock, ItemBlockFuels.class, "blockFuel");
		GameRegistry.registerBlock(blockFuelBlock, ItemBlockFuelBlock.class, "blockFuelBlock");
		GameRegistry.registerBlock(structureBlock, "blockStructure");
		GameRegistry.registerBlock(furnaceBlock, ItemBlockFurnaces.class, "blockFurnaces");
		GameRegistry.registerBlock(scaffoldBlock, ItemBlockScaffold.class, "blockScaffold");
		GameRegistry.registerBlock(furnaceControlBlock, ItemBlockMultiblockFurnaceControl.class, "blockfurnacecontrol");
		GameRegistry.registerBlock(multiblockFurnaceControlBlock, "blockmultiblockfurnacecontrol");
		GameRegistry.registerBlock(autocraftingbenchBlock, ItemBlockAutocraftingBench.class, "blockautocraftingbench");
		GameRegistry.registerBlock(tunnelboreBlock, ItemBlockTunnelBore.class, "blocktunnelbore");
		GameRegistry.registerBlock(transportBlock, ItemBlockTransport.class, "transportBlock");
		
		FluidRegistry.registerFluid(fluidFuelPotential);
		blockFuelPotential = new BlockFluidFuelPotential(fluidFuelPotential, Material.water);
		GameRegistry.registerBlock(blockFuelPotential, "blockFuelPotential");
	}
	
	public static void postinit()
	{
		fluidFuelPotential.setUnlocalizedName(blockFuelPotential.getUnlocalizedName()).setStillIcon(blockFuelPotential.getIcon(0, 0)).setFlowingIcon(blockFuelPotential.getIcon(2, 0));
		System.out.println(fluidFuelPotential.getIcon().getIconName());
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