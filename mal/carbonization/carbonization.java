package mal.carbonization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;

//import ic2.api.item.*;
import mal.carbonization.blocks.BlockFuel;
import mal.carbonization.blocks.BlockFurnaceControl;
import mal.carbonization.blocks.BlockFurnaces;
import mal.carbonization.blocks.BlockMultiblockFurnaceControl;
import mal.carbonization.blocks.BlockMultiblockStructure;
import mal.carbonization.blocks.BlockMultiblockStructureFurnace;
import mal.carbonization.blocks.BlockStructure;
import mal.carbonization.blocks.BlockStructureFurnace;
import mal.carbonization.blocks.BlockStructureMachine;
import mal.carbonization.blocks.TestBlock;
import mal.carbonization.items.ItemBlockFuels;
import mal.carbonization.items.ItemBlockFurnaces;
import mal.carbonization.items.ItemBlockMultiblockFurnaceControl;
import mal.carbonization.items.ItemBlockStructure;
import mal.carbonization.items.ItemBlockStructureFurnace;
import mal.carbonization.items.ItemBlockStructureMachine;
import mal.carbonization.items.ItemDust;
import mal.carbonization.items.ItemFuel;
import mal.carbonization.items.ItemHHCompressor;
import mal.carbonization.items.ItemHHPulverizer;
import mal.carbonization.items.ItemHHPurifyer;
import mal.carbonization.items.ItemIngots;
import mal.carbonization.items.ItemMisc;
import mal.carbonization.items.ItemTestBlock;
import mal.carbonization.network.PacketHandler;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockDummy;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.carbonization.tileentity.TileEntityTest;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import thermalexpansion.api.crafting.CraftingHelpers;
import thermalexpansion.api.crafting.CraftingManagers;
//import ic2.api.recipe.*;

@Mod(modid="carbonization", name="Carbonization", version="0.7.4.3", dependencies = "required-after:Forge@[9.10,);required-after:FML@[6.2.43,)")
@NetworkMod(clientSideRequired=true, channels={"CarbonizationChn"}, packetHandler = PacketHandler.class)
public class carbonization {

	public static int ORESLAGRATIO = 300;//number of millibuckets needed for an item
	public static boolean FAKEAIR = false;//override to assume there is a fake air issue
	public static ItemFuel fuel;
	public static ItemDust dust;
	public static ItemMisc misc;
	public static ItemIngots ingots;
	public static ItemHHPulverizer hhpulv;
	public static ItemHHPurifyer hhpure;
	public static ItemHHCompressor hhcomp;
	public static Block fuelBlock;
	public static BlockFurnaces furnaceBlock;
	public static BlockFurnaces furnaceBlockActive;
	public static BlockStructure structureBlock;
	public static BlockStructureFurnace structureFurnaceBlock;
	public static BlockStructureMachine structureMachineBlock;
	public static BlockFurnaceControl FurnaceControl;
	public static BlockMultiblockStructure structureMultiblock;
	public static BlockMultiblockStructureFurnace structureFurnaceMultiblock;
	public static BlockMultiblockFurnaceControl multiblockFurnaceControl;
	//public static TestBlock testBlock;
	
	int fuelID=9540;
	int dustID = 9541;
	int itemID = 9542;
	int itemID2 = 9543;
	int itemID3 = 9544;
	int miscID = 9545;
	int ingotID = 9546;
	int blockID=1560;
	int structureID=1563;
	int structureFurnaceID = 1564;
	int furnaceID = 1561;
	int furnaceID2 = 1562;
	int multiblockfurnaceID = 1565;
	int multiblockstructureID = 1566;
	int multiblockstructurefurnaceID = 1567;
	int multiblockfurnacecontrolID = 1568;
	int structureMachineID = 1569;
	
	//Difficulty modifier, the higher the number, the more time the metals take to bake
	private int difficultyMod = 10;
	
	private static ItemStack HHPulv;
	private static ItemStack HHComp;
	private static ItemStack HHPure;
	
	@Instance
	public static carbonization instance;
	
	@SidedProxy(clientSide = "mal.carbonization.ClientProxy", serverSide = "mal.carbonization.CommonProxy")
	public static CommonProxy prox;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());

            config.load();
            
    		fuelID = config.getItem("Fuel ID", 9540).getInt();
    		dustID = config.getItem("Dust ID", 9541).getInt();
    		itemID = config.getItem("Handheld Pulverizer ID", 9542).getInt();
    		itemID2 = config.getItem("Handheld Compressor ID", 9543).getInt();
    		itemID3 = config.getItem("Handheld Purifyer", 9544).getInt();
    		miscID = config.getItem("Misc ID", 9545).getInt();
    		ingotID = config.getItem("Ingot ID", 9546).getInt();
    		blockID = config.getBlock("Block ID", 1560).getInt();
    		furnaceID = config.getBlock("Furnace ID", 1561).getInt();
    		furnaceID2 = config.getBlock("Active Furnace ID", 1562).getInt();
    		structureID = config.getBlock("Structure Block ID", 1563).getInt();
    		structureFurnaceID = config.getBlock("Structure Furnace ID", 1564).getInt();
    		multiblockfurnaceID = config.getBlock("Multiblock Furnace ID", 1565).getInt();
    		multiblockstructureID = config.getBlock("Multiblock Structure ID", 1566).getInt();
    		multiblockstructurefurnaceID = config.getBlock("Multiblock Structure Furnace ID", 1567).getInt();
    		multiblockfurnacecontrolID = config.getBlock("Multiblock Furnace Control ID", 1568).getInt();
    		structureMachineID = config.getBlock("Structure Machine ID", 1569).getInt();
    		
    		difficultyMod = config.get("Modifiers", "Metal Cook Time", 10).getInt();
    		if (difficultyMod<=0)
    		{
    			FMLLog.log(Level.WARNING, "Metal Cook time modifier is a invalid number, using the default value instead.");
    			difficultyMod = 10;
    		}
    		
    		FAKEAIR = config.get("Modifiers", "Fake Air Override", false).getBoolean(false);
    		
    		config.save();
    		
    		
    		//blarg
    		fuel = new ItemFuel(fuelID);
    		dust = new ItemDust(dustID);
    		misc = new ItemMisc(miscID);
    		ingots = new ItemIngots(ingotID);
    		hhpulv = new ItemHHPulverizer(itemID);
    		HHPulv  = new ItemStack(hhpulv, 1, OreDictionary.WILDCARD_VALUE);
    		hhpure = new ItemHHPurifyer(itemID3);
    		HHPure = new ItemStack(hhpure, 1, OreDictionary.WILDCARD_VALUE);
    		hhcomp = new ItemHHCompressor(itemID2);
    		HHComp = new ItemStack(hhcomp,1,OreDictionary.WILDCARD_VALUE);
    		fuelBlock = new BlockFuel(blockID,0,Material.rock).setStepSound(Block.soundStoneFootstep).setHardness(3F).setResistance(1.0F);
    		structureBlock = new BlockStructure(structureID,0,Material.iron);
    		structureFurnaceBlock = new BlockStructureFurnace(structureFurnaceID, Material.iron);
    		structureMachineBlock = new BlockStructureMachine(structureMachineID, Material.iron);
    		structureMultiblock = new BlockMultiblockStructure(multiblockstructureID,Material.iron);
    		structureFurnaceMultiblock = new BlockMultiblockStructureFurnace(multiblockstructurefurnaceID, Material.iron);
    		furnaceBlock = new BlockFurnaces(furnaceID,false);
    		furnaceBlockActive = new BlockFurnaces(furnaceID2, true);
    		FurnaceControl = new BlockFurnaceControl(multiblockfurnaceID, Material.iron);
    		multiblockFurnaceControl = new BlockMultiblockFurnaceControl(multiblockfurnacecontrolID, Material.iron);
    		//Item.itemsList[blockID] = new ItemBlockFuels(blockID-256);
    		Item.itemsList[furnaceID] = new ItemBlockFurnaces(furnaceID-256,furnaceBlock);
    		//Item.itemsList[structureID] = new ItemBlockStructure(structureID-256);
    		
    		//TODO: remember to disable on releases
    		//testBlock = new TestBlock(structureID+200,Material.rock);
    		//GameRegistry.registerBlock(testBlock, ItemTestBlock.class, "testBlock");
    		
    		hhpulv.setContainerItem(hhpulv);
    		hhcomp.setContainerItem(hhcomp);
    		hhpure.setContainerItem(hhpure);
    		
    		//Basic stuff
    		GameRegistry.registerFuelHandler(new FuelHandler());
    		GameRegistry.registerWorldGenerator(new WorldgeneratorCarbonization());
    		NetworkRegistry.instance().registerGuiHandler(instance, prox);
    		GameRegistry.registerTileEntity(TileEntityFurnaces.class, "TileEntityFurnaces");
    		GameRegistry.registerTileEntity(TileEntityTest.class, "TileEntityTest");
    		GameRegistry.registerTileEntity(TileEntityMultiblockInit.class, "TileEntityMultiblockInit");
    		GameRegistry.registerTileEntity(TileEntityMultiblockDummy.class, "TileEntityMultiblockDummy");
    		GameRegistry.registerTileEntity(TileEntityMultiblockFurnace.class, "TileEntityMultiblockFurnace");
    		
    		GameRegistry.registerBlock(fuelBlock, ItemBlockFuels.class, "fuelBlock");
    		GameRegistry.registerBlock(structureBlock, ItemBlockStructure.class, "structureBlock");
    		//GameRegistry.registerBlock(structureMultiblock, ItemBlockStructure.class, "structureMultiblock");
    		GameRegistry.registerBlock(structureFurnaceBlock, ItemBlockStructureFurnace.class, "structureFurnaceBlock");
    		GameRegistry.registerBlock(structureMachineBlock, ItemBlockStructureMachine.class, "structureMachineBlock");
    		GameRegistry.registerBlock(FurnaceControl, ItemBlockMultiblockFurnaceControl.class, "furnacecontrol");
    		//GameRegistry.registerBlock(multiblockFurnaceControl, null, "furnacemultiblockcontrol");
    		
    		//Names
    		//Fuels
    		LanguageRegistry.addName(new ItemStack(fuel,1,0), "Peat");
    		LanguageRegistry.addName(new ItemStack(fuel,1,1), "Lignite");
    		LanguageRegistry.addName(new ItemStack(fuel,1,2), "Sub-Bituminous Coal");
    		LanguageRegistry.addName(new ItemStack(fuel,1,3), "Bituminous Coal");
    		LanguageRegistry.addName(new ItemStack(fuel,1,4), "Anthracite");
    		LanguageRegistry.addName(new ItemStack(fuel,1,5), "Graphite");
    		
    		//Dusts
    		LanguageRegistry.addName(new ItemStack(dust,1,0), "Charcoal Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,1), "Peat Clumps");
    		LanguageRegistry.addName(new ItemStack(dust,1,2), "Lignite Clumps");
    		LanguageRegistry.addName(new ItemStack(dust,1,3), "Sub-Bituminous Coal Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,4), "Bituminous Coal Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,5), "Coal Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,6), "Anthracite Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,7), "Graphite Dust");
    		LanguageRegistry.addName(new ItemStack(dust,1,8), "Activated Charcoal");
    				
    		//Deposits
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,0), "Peat Deposit");
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,1), "Lignite Deposit");
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,2), "Sub-Bituminous Coal Deposit");
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,3), "Bituminous Coal Deposit");
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,4), "Anthracite Deposit");
    		LanguageRegistry.addName(new ItemStack(fuelBlock,1,5), "Graphite Cluster");
    		
    		//Ingots
    		LanguageRegistry.addName(new ItemStack(ingots,1,0), "Refined Iron");
    		LanguageRegistry.addName(new ItemStack(ingots,1,1), "Pig Iron");
    		LanguageRegistry.addName(new ItemStack(ingots,1,2), "Mild Steel");
    		LanguageRegistry.addName(new ItemStack(ingots,1,3), "Steel");
    		
    		//Tools
    		LanguageRegistry.addName(HHPulv, "Handheld Pulverizer");
    		LanguageRegistry.addName(HHComp, "Handheld Compressor");
    		LanguageRegistry.addName(HHPure, "Handheld Purifyer");
    		
    		LanguageRegistry.addName(new ItemStack(misc, 1, 0), "Pencil");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 1), "Cleansing Potion");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 2), "\"Cleansing\" Potion");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 3), "Carbon Chunk");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 4), "Glass Fibre Insulation");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 5), "High Density Insulation");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 6), "Ash");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 7), "Iron Gears");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 8), "Refined Iron Gears");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 9), "Pig Iron Gears");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 10), "Mild Steel Gears");
    		LanguageRegistry.addName(new ItemStack(misc, 1, 11), "Steel Gears");
    		
    		//Machines
    		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,0), "Iron Furnace");
    		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,1), "Insulated Iron Furnace");
    		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,2), "Insulated Steel Furnace");
    		//Multiblock controls
    		LanguageRegistry.addName(new ItemStack(FurnaceControl), "Furnace Control System");
    		
    		//Structure
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,0), "Ice Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,1), "Refined Iron Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,2), "Pig Iron Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,3), "Mild Steel Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,4), "Steel Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,5), "Carbon Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,6), "Reinforced Carbon Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,7), "Insulated Steel Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,8), "Insulated Carbon Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,9), "HMD Insulated Carbon Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,10), "Insulated Reinforced Carbon Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,11), "HMD Ins. Steel Structure");
    		LanguageRegistry.addName(new ItemStack(structureBlock,1,12), "HMD Ins. Reinforced Carbon Structure");
    		
    		//Structure Furnaces
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,0), "Ice Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,1), "Refined Iron Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,2), "Pig Iron Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,3), "Mild Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,4), "Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,5), "Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,6), "Reinforced Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,7), "Insulated Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,8), "Insulated Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,9), "HMD Ins. Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,10), "Ins. Reinf Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,11), "HMD Ins. Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureFurnaceBlock,1,12), "HMD Ins. Reinf Carbon Furnace Structure");
    		
    		//Structure Machines
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,0), "Ice Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,1), "Refined Iron Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,2), "Pig Iron Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,3), "Mild Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,4), "Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,5), "Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,6), "Reinforced Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,7), "Insulated Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,8), "Insulated Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,9), "HMD Ins. Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,10), "Ins. Reinf Carbon Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,11), "HMD Ins. Steel Furnace Structure");
    		LanguageRegistry.addName(new ItemStack(structureMachineBlock,1,12), "HMD Ins. Reinf Carbon Furnace Structure");
    		
    		//Localizations
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.peat.name", "Peat Deposit");
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.lignite.name", "Lignite Deposit");
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.sBituminous.name", "Sub-Bituminous Coal Deposit");
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.bituminous.name", "Bituminous Coal Deposit");
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.anthracite.name", "Anthracite Deposit");
    		LanguageRegistry.instance().addStringLocalization("tile.fuelBlock.graphite.name", "Graphite Cluster");
    		
    		LanguageRegistry.instance().addStringLocalization("tile.furnaceBlock.ironfurnace.name", "Iron Furnace");
    		LanguageRegistry.instance().addStringLocalization("tile.furnaceBlock.refinedironfurnace.name", "Insulated Iron Furnace");
    		LanguageRegistry.instance().addStringLocalization("tile.furnaceBlock.steelfurnace.name", "Insulated Steel Furnace");
    		
    		prox.registerRenderThings();
	}
	
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{	
		
		
		
		//Ore dictionary
		OreDictionary.registerOre("brickPeat", new ItemStack(fuel, 1, 0));
		OreDictionary.registerOre("brickLignite", new ItemStack(fuel, 1, 1));
		OreDictionary.registerOre("brickSubBituminous", new ItemStack(fuel, 1, 2));
		OreDictionary.registerOre("brickBituminous", new ItemStack(fuel, 1, 3));
		OreDictionary.registerOre("brickAnthracite", new ItemStack(fuel, 1, 4));
		OreDictionary.registerOre("brickGraphite", new ItemStack(fuel, 1, 5));
		OreDictionary.registerOre("dustCharcoal", new ItemStack(dust, 1, 0));
		OreDictionary.registerOre("dustPeat", new ItemStack(dust, 1, 1));
		OreDictionary.registerOre("dustLignite", new ItemStack(dust, 1, 2));
		OreDictionary.registerOre("dustSubBituminous", new ItemStack(dust, 1, 3));
		OreDictionary.registerOre("dustBituminous", new ItemStack(dust, 1, 4));
		OreDictionary.registerOre("dustCoal", new ItemStack(dust, 1, 5));
		OreDictionary.registerOre("dustAnthracite", new ItemStack(dust, 1, 6));
		OreDictionary.registerOre("dustGraphite", new ItemStack(dust, 1, 7));
		OreDictionary.registerOre("dustACharcoal", new ItemStack(dust,1,8));
		OreDictionary.registerOre("ingotRefinedIron", new ItemStack(ingots,1,0));
		OreDictionary.registerOre("ingotPigIron", new ItemStack(ingots,1,1));
		OreDictionary.registerOre("ingotHCSteel", new ItemStack(ingots,1,2));
		OreDictionary.registerOre("ingotSteel", new ItemStack(ingots,1,3));
		
		//make the fuel work with the correct tools
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 0, "shovel", 0);
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 1, "shovel", 0);
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 2, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 3, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 4, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(fuelBlock, 5, "pickaxe", 1);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		boolean te=false;
		boolean ic=false;


		if(Loader.isModLoaded("ThermalExpansion"))
		{
			te=true;
		}

		if(Loader.isModLoaded("IC2"))
		{
			ic=true;
		}
		
		generateMash();
		generateTools(ic);
		generateSmithing(ic);
		generateConversions(ic, te);
		generateMachines(ic);
		generateStructure();
		generateFurnaceStructure();
		generateMultiblockFurnaceRecipes();
	}
	
	private void generateMash()
	{
		//handheld fuel to dust
		GameRegistry.addShapelessRecipe(new ItemStack(dust,1,0), new Object[]{HHPulv, new ItemStack(Item.coal, 1, 1)});
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,1), new Object[]{HHPulv,"brickPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,2), new Object[]{HHPulv,"brickLignite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,3), new Object[]{HHPulv,"brickSubBituminous"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,4), new Object[]{HHPulv,"brickBituminous"}));
		GameRegistry.addShapelessRecipe(new ItemStack(dust,1,5), new Object[]{HHPulv, new ItemStack(Item.coal, 1, 0)});
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,6), new Object[]{HHPulv,"brickAnthracite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,7), new Object[]{HHPulv,"brickGraphite"}));
		
		//handheld dust to fuel
		//GameRegistry.addShapelessRecipe(new ItemStack(Item.coal,1,1), new Object[]{HHComp, new ItemStack(dust,1,0)});
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Item.coal,1,1),new Object[]{HHComp, "dustCharcoal"}));
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,0), new Object[]{HHComp, new ItemStack(dust,1,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,1), new Object[]{HHComp, new ItemStack(dust,1,2)});
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,2), new Object[]{HHComp, new ItemStack(dust,1,3)});
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,3), new Object[]{HHComp, new ItemStack(dust,1,4)});
		//GameRegistry.addShapelessRecipe(new ItemStack(Item.coal,1,0), new Object[]{HHComp, new ItemStack(dust,1,5)});
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Item.coal,1,0),new Object[]{HHComp, "dustCoal"}));
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,4), new Object[]{HHComp, new ItemStack(dust,1,6)});
		GameRegistry.addShapelessRecipe(new ItemStack(fuel,1,5), new Object[]{HHComp, new ItemStack(dust,1,7)});
	}
	
	private void generateSmithing(boolean ic)
	{
		//we have ic2, so use their iron instead
		if(!ic)
		{
			//iron into refined iron
			FurnaceRecipes.smelting().addSmelting(new ItemStack(Item.ingotIron).itemID, new ItemStack(ingots,1,0), 5);
		}

		//refined iron into pig iron
		CarbonizationRecipes.smelting().addSmelting("ingotRefinedIron", new ItemStack(ingots,1,1), 5, (50*difficultyMod), 1);
		//pig iron into mild steel
		CarbonizationRecipes.smelting().addSmelting("ingotPigIron", new ItemStack(ingots,1,2), 5, (75*difficultyMod), 2);
		//mild steel into steel
		CarbonizationRecipes.smelting().addSmelting("ingotHCSteel", new ItemStack(ingots,1,3), 5, (100*difficultyMod), 2);
		
		//Activated Charcoal
		FurnaceRecipes.smelting().addSmelting((new ItemStack(dust,1,0)).itemID, 0, new ItemStack(dust,1,8), 0);
	}
	
	private void generateConversions(boolean ic, boolean te)
	{
		//1 charcoal -> 1 coal
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,5), new Object[]{HHPure, "dustCharcoal"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,0), new Object[]{HHPure, "dustCoal"}));
		
		//1 peat -> 1 lignite
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,2), new Object[]{HHPure, "dustPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,1), new Object[]{HHPure, "dustLignite"}));
		
		//5 lignite -> 4 s-bit
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,4,3), new Object[]{HHPure, "dustLignite","dustLignite","dustLignite","dustLignite","dustLignite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,5,2), new Object[]{HHPure, "dustSubBituminous","dustSubBituminous","dustSubBituminous","dustSubBituminous"}));
		
		//3 s-bit -> 2 bit
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,2,4), new Object[]{HHPure, "dustSubBituminous","dustSubBituminous","dustSubBituminous"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,3,3), new Object[]{HHPure, "dustBituminous","dustBituminous"}));
		
		//3 bit -> 2 coal
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,2,5), new Object[]{HHPure, "dustBituminous","dustBituminous","dustBituminous"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,3,4), new Object[]{HHPure, "dustCoal","dustCoal"}));
		
		//6 coal -> 5 anthracite
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,5,6), new Object[]{HHPure, "dustCoal","dustCoal","dustCoal","dustCoal","dustCoal","dustCoal"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,6,5), new Object[]{HHPure, "dustAnthracite","dustAnthracite","dustAnthracite","dustAnthracite","dustAnthracite"}));
		
		//1 anthracite -> 6 graphite
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,6), new Object[]{HHPure, "dustGraphite","dustGraphite","dustGraphite","dustGraphite","dustGraphite","dustGraphite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,6,7), new Object[]{HHPure, "dustAnthracite"}));
		
		/*if(ic)//ic2 so make the recipes for that too
		{
			try {
				//mash
				
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,0), new ItemStack(dust,1,1));
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,1), new ItemStack(dust,1,2));
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,2), new ItemStack(dust,1,3));
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,3), new ItemStack(dust,1,4));
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,4), new ItemStack(dust,1,6));
				ic2.api.recipe.Recipes.macerator.addRecipe(new ItemStack(fuel,1,5), new ItemStack(dust,1,7));
				
				//cleanse
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,1,5), new ItemStack(dust,1,0));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,1,5), new ItemStack(dust,1,0));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,1,2), new ItemStack(dust,1,1));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,1,1), new ItemStack(dust,1,2));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,4,3), new ItemStack(dust,5,2));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,5,2), new ItemStack(dust,4,3));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,2,4), new ItemStack(dust,3,3));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,3,3), new ItemStack(dust,2,4));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,2,5), new ItemStack(dust,3,4));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,3,4), new ItemStack(dust,2,5));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,5,6), new ItemStack(dust,6,5));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,6,5), new ItemStack(dust,5,6));
				
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,1,6), new ItemStack(dust,6,7));
				ic2.api.recipe.Recipes.extractor.addRecipe(new ItemStack(dust,6,7), new ItemStack(dust,1,6));
				
				
				//mush
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,0), new ItemStack(Item.coal, 1,1));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,1), new ItemStack(fuel,1,0));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,2), new ItemStack(fuel,1,1));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,3), new ItemStack(fuel,1,2));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,4), new ItemStack(fuel,1,3));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,5), new ItemStack(Item.coal, 1,0));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,6), new ItemStack(fuel,1,4));
				ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust,1,7), new ItemStack(fuel,1,5));
			}
			catch(Exception e)
			{
				FMLLog.log(Level.INFO, "Oh dear, something broke with IC2.  Prod Mal so he can fix it.");
			}
		}*/
		
		if(te)//thermal expansion recipes
		{
			try {
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,0), new ItemStack(dust,1,1), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,1), new ItemStack(dust,1,2), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,2), new ItemStack(dust,1,3), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,3), new ItemStack(dust,1,4), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,4), new ItemStack(dust,1,6), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(fuel,1,5), new ItemStack(dust,1,7), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(Item.coal,1,1), new ItemStack(dust,1,0), false);
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(Item.coal,1,0), new ItemStack(dust,1,5), false);
			}
			catch(Exception e)
			{
				FMLLog.log(Level.INFO, "Oh dear, something broke with Thermal Expansion.  Prod Mal so he can fix it.");
			}
		}
	}
	
	private void generateTools(boolean ic)
	{
		//Handheld tools
		GameRegistry.addShapelessRecipe(new ItemStack(carbonization.hhpulv), new Object[] {new ItemStack(Item.flint), new ItemStack(Item.bowlEmpty)});
		GameRegistry.addRecipe(new ItemStack(carbonization.hhcomp), new Object[] {" O ", "S O", " S ", 'O', Block.obsidian, 'S', Item.stick});
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonization.hhpure), new Object[]{" I ", "ICI", "SI ", 'I', Item.ingotIron, 'C', "dustACharcoal", 'S', Item.stick}));
		
		//Simple Recipes
		GameRegistry.addRecipe(new ItemStack(misc,1,0), new Object[]{"S","G", 'S', Item.stick, 'G', new ItemStack(fuel,1,5)});
		GameRegistry.addRecipe(new ItemStack(misc,1,0), new Object[]{"S","C", 'S', Item.stick, 'C', new ItemStack(Item.coal,1,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.writableBook,1), new Object[]{Item.book, new ItemStack(misc,1,0)});
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(misc,1,1), new Object[]{new ItemStack(Item.potion,1,0), "dustACharcoal"}));
		if(!ic)//prevent a known conflict by changing the recipe if IC2 is installed
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(misc,1,2), new Object[]{new ItemStack(Item.potion,1,0), "dustCoal"}));
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 2), new Object[]{"C","S",'C', new ItemStack(fuel,1,2), 'S', Item.stick});
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"C","S",'C', new ItemStack(fuel,1,3), 'S', Item.stick});
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 8), new Object[]{"C","S",'C', new ItemStack(fuel,1,4), 'S', Item.stick});
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(misc, 1, 3), new Object[]{HHComp, "dustGraphite", "dustGraphite", 
			"dustGraphite", "dustGraphite", "dustGraphite", "dustGraphite", "dustGraphite", "dustGraphite"}));
		GameRegistry.addShapelessRecipe(new ItemStack(Item.diamond), new Object[]{HHComp, new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3), 
			new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3), new ItemStack(misc, 1, 3)});
		
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(misc,1,4), new Object[]{Item.clay, Block.gravel, Block.gravel, Block.thinGlass}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(misc,1,5), new Object[]{Block.sand, Block.sand, "dustGraphite", "dustGraphite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(dust,1,0), new Object[]{new ItemStack(misc, 1, 6), new ItemStack(misc, 1, 6), new ItemStack(misc, 1, 6), new ItemStack(misc, 1, 6)}));
		
		/*if(ic)//IC2, so add in compressor recipes for the carbon chunk and alternate "cleansing" potion
		{
			ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(dust, 8, 7), new ItemStack(misc, 1, 3));
			ic2.api.recipe.Recipes.compressor.addRecipe(new ItemStack(misc, 8, 3), new ItemStack(Item.diamond));
			
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(misc, 1, 2), new Object[]{"WC", 'W', new ItemStack(Item.potion, 1, 0), 'C', "dustCoal"}));
		}*/
	}
	
	private void generateMachines(boolean ic)
	{
		if(!ic)
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,0), 
					new Object[]{" I ", "I I", "IFI", 'I' ,Item.ingotIron, 'F', Block.furnaceIdle}));
		else
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,0), 
					new Object[]{"   ", "III", "IFI", 'I' ,Item.ingotIron, 'F', Block.furnaceIdle}));
/*			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,1), 
					new Object[]{"III", "IFI", "BBB", 'I' ,"ingotRefinedIron", 'F', Items.getItem("ironFurnace"), 'B', Block.brick}));*/

		}
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,1), 
				new Object[]{"III", "IFI", "BBB", 'I' ,"ingotRefinedIron", 'F', new ItemStack(furnaceBlock,1,0), 'B', Block.brick}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,2), 
				new Object[]{"SSS", "SFS", "SSS", 'S' ,"ingotSteel", 'F', new ItemStack(furnaceBlock,1,1)}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(FurnaceControl, 1), new Object[]
				{"SSS", "SFS", "SSS", 'S', new ItemStack(structureBlock,1,OreDictionary.WILDCARD_VALUE), 'F', new ItemStack(furnaceBlock,1,1)}));
	}

	private void generateStructure()
	{
		//make the structure blocks
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,0), new Object[]{"x x", " x ", "x x", 'x', Block.ice}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,1,0), new Object[]{"x x", " x ", "x x", 'x', Item.snowball}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,1), new Object[]{"x x", " x ", "x x", 'x', "ingotRefinedIron"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,2), new Object[]{"x x", " x ", "x x", 'x', "ingotPigIron"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,3), new Object[]{"x x", " x ", "x x", 'x', "ingotHCSteel"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,4), new Object[]{"x x", " x ", "x x", 'x', "ingotSteel"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,5), new Object[]{"x x", " x ", "x x", 'x', new ItemStack(misc, 1, 3)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,6), new Object[]{"x x", " y ", "x x", 'x', new ItemStack(misc, 1, 3), 'y', Item.ingotIron}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,7), new Object[]{"xyx", "yxy", "xyx", 'x', "ingotSteel", 'y', new ItemStack(misc,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,8), new Object[]{"xyx", "yxy", "xyx", 'x', new ItemStack(misc, 1, 3), 'y', new ItemStack(misc,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,9), new Object[]{"xyx", "yxy", "xyx", 'x', new ItemStack(misc, 1, 3), 'y', new ItemStack(misc,1,5)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,10), new Object[]{"xyx", "yzy", "xyx", 'x', new ItemStack(misc, 1, 3), 'z', "ingotSteel", 'y', new ItemStack(misc,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,11), new Object[]{"xyx", "yxy", "xyx", 'x', "ingotSteel", 'y', new ItemStack(misc,1,5)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureBlock,5,12), new Object[]{"xyx", "yzy", "xyx", 'x', new ItemStack(misc, 1, 3), 'z', "ingotSteel", 'y', new ItemStack(misc,1,5)}));
		
		//take them apart
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Block.ice), new Object[]{new ItemStack(structureBlock,1,0)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,0), new Object[]{new ItemStack(structureBlock,1,1)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,1), new Object[]{new ItemStack(structureBlock,1,2)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,2), new Object[]{new ItemStack(structureBlock,1,3)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,3), new Object[]{new ItemStack(structureBlock,1,4)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,5)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,6)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,3), new Object[]{new ItemStack(structureBlock,1,7)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,8)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,9)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,10)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ingots,1,3), new Object[]{new ItemStack(structureBlock,1,11)});
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(misc,1,3), new Object[]{new ItemStack(structureBlock,1,12)});
	}
	
	private void generateFurnaceStructure()
	{
		//make the furnace structure blocks
		for(int i = 0; i<13; i++)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(structureFurnaceBlock,4,i), new Object[]{" x ", "xyx", " x ", 'x', new ItemStack(structureBlock,1,i), 
				'y', (i<2)?(Block.furnaceIdle):((i<6)?(new ItemStack(furnaceBlock,1,0)):((i<11)?(new ItemStack(furnaceBlock,1,1)):(new ItemStack(furnaceBlock,1,2))))}));
			//take them apart
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(structureBlock,1,i), new Object[]{new ItemStack(structureFurnaceBlock,1,i)});
		}	
	}
	
	private void generateMultiblockFurnaceRecipes()
	{
		//Add in the ore slag relationships
		CarbonizationRecipes.smelting().addOreSlag("ashSlag", new ItemStack(misc, 2, 6));
		CarbonizationRecipes.smelting().addOreSlag("stoneSlag", new ItemStack(Block.stone));
		CarbonizationRecipes.smelting().addOreSlag("glassSlag", new ItemStack(Block.glass));
		CarbonizationRecipes.smelting().addOreSlag("ironSlag", new ItemStack(Item.ingotIron));
		CarbonizationRecipes.smelting().addOreSlag("goldSlag", new ItemStack(Item.ingotGold));
		CarbonizationRecipes.smelting().addOreSlag("brickSlag", new ItemStack(Item.brick));
		CarbonizationRecipes.smelting().addOreSlag("brickBlockSlag", new ItemStack(Block.brick));
		CarbonizationRecipes.smelting().addOreSlag("copperSlag", "ingotCopper");
		CarbonizationRecipes.smelting().addOreSlag("tinSlag", "ingotTin");
		CarbonizationRecipes.smelting().addOreSlag("silverSlag", "ingotSilver");
		CarbonizationRecipes.smelting().addOreSlag("leadSlag", "ingotLead");
		CarbonizationRecipes.smelting().addOreSlag("nickelSlag", "ingotNickel");
		
		//Add in the recipes, normal metals and blocks
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Block.cobblestone), 200, 1, "stoneSlag", true);
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Block.sand), 200, 1, "glassSlag", true);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreIron", 400, 1, "ironSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreGold", 400, 1, "goldSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.clay), 200, 1, "brickSlag", true);
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Block.blockClay), 400, 1, "brickBlockSlag", true);
		
		//Add in common mod ores through OreDictionary
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreCopper", 400, 1, "copperSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreTin", 400, 1, "tinSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreSilver", 400, 1, "silverSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreLead", 400, 1, "leadSlag", false);
		CarbonizationRecipes.smelting().addMultiblockSmelting("oreNicked", 400, 1, "nickelSlag", false);
		
		//food and wood items
		//TODO: Find a way to do this easier
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.appleRed), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.arrow), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.axeWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bakedPotato), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.beefCooked), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.beefRaw), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.boat), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bow), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bowlEmpty), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.book), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bowlSoup), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bread), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.cake), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.carrot), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.carrotOnAStick), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.chickenCooked), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.chickenRaw), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.cookie), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.doorWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.egg), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.enchantedBook), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.feather), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.fishCooked), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.fishRaw), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.fishingRod), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.hoeWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.helmetLeather), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.itemFrame), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.leather), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.legsLeather), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.melon), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.melonSeeds), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.painting), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.paper), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.pickaxeWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.poisonousPotato), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.porkCooked), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.porkRaw), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.potato), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.pumpkinPie), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.pumpkinSeeds), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.plateLeather), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.reed), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.rottenFlesh), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.saddle), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.seeds), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.shovelWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.sign), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.silk), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.stick), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.swordWood), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.wheat), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.writableBook), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.writtenBook), 50, 1, "ashSlag");
		CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Item.bow), 50, 1, "ashSlag");
		
		for(int i = 0; i<Block.blocksList.length; i++)
		{
			if(Block.blocksList[i] != null)
			{
				Material m = Block.blocksList[i].blockMaterial; 
				if(m == Material.cloth || m==Material.pumpkin || m==Material.wood || m==Material.cactus)
				{
					if(Block.blocksList[i].damageDropped(1) == 1)//simple check for damage values not 0
						for(int j = 0; j<16; j++)
						{
							if(new ItemStack(Block.blocksList[i], 1, j) != null)
							{
								CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Block.blocksList[i], 1, j), 100, 1, "ashSlag");
							}
						}
					CarbonizationRecipes.smelting().addMultiblockSmelting(new ItemStack(Block.blocksList[i]), 100, 1, "ashSlag");
				}
			}
		}

	}
}
/*******************************************************************************
* Copyright (c) 2013 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
* 
* 
*********************************************************************************/