package mal.carbonization;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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
import ic2.api.Ic2Recipes;
import ic2.api.Items;

@Mod(modid="carbonization", name="Carbonization", version="0.65")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"CarbonizationChn"}, packetHandler = PacketHandler.class)
public class carbonization {

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
	int fuelID=9540;
	int dustID = 9541;
	int itemID = 9542;
	int itemID2 = 9543;
	int itemID3 = 9544;
	int miscID = 9545;
	int ingotID = 9546;
	int blockID=560;
	int furnaceID = 561;
	int furnaceID2 = 562;
	
	//Difficulty modifier, the higher the number, the more time the metals take to bake
	private int difficultyMod = 10;
	
	private static ItemStack HHPulv;
	private static ItemStack HHComp;
	private static ItemStack HHPure;
	
	@Instance
	public static carbonization instance;
	
	@SidedProxy(clientSide = "mal.carbonization.ClientProxy", serverSide = "mal.carbonization.CommonProxy")
	public static CommonProxy prox;
	
	@PreInit
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
    		blockID = config.getBlock("Block ID", 560).getInt();
    		furnaceID = config.getBlock("Furnace ID", 561).getInt();
    		furnaceID2 = config.getBlock("Active Furnace ID", 562).getInt();
    		difficultyMod = config.get("Modifiers", "Metal Cook Time", 10).getInt();
    		if (difficultyMod<=0)
    		{
    			System.out.println("Metal Cook time modifier is a invalid number, using the default value instead.");
    			difficultyMod = 10;
    		}
    		
    		config.save();
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{	
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
		furnaceBlock = new BlockFurnaces(furnaceID,false);
		furnaceBlockActive = new BlockFurnaces(furnaceID2, true);
		Item.itemsList[blockID] = new ItemBlockFuels(blockID-256);
		Item.itemsList[furnaceID] = new ItemBlockFurnaces(furnaceID-256,furnaceBlock);
		
		hhpulv.setContainerItem(hhpulv);
		hhcomp.setContainerItem(hhcomp);
		hhpure.setContainerItem(hhpure);
		
		//Basic stuff
		GameRegistry.registerFuelHandler(new FuelHandler());
		GameRegistry.registerWorldGenerator(new WorldgeneratorCarbonization());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		GameRegistry.registerTileEntity(TileEntityFurnaces.class, "TileEntityFurnaces");
		
		GameRegistry.registerBlock(fuelBlock, ItemBlockFuels.class, "fuelBlock");
		
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
		//LanguageRegistry.addName(new ItemStack(misc, 1, 1), "Charcoal Pencil");
		LanguageRegistry.addName(new ItemStack(misc, 1, 2), "Cleansing Potion");
		
		//Machines
		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,0), "Iron Furnace");
		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,1), "Insulated Iron Furnace");
		LanguageRegistry.addName(new ItemStack(furnaceBlock,1,2), "Insulated Steel Furnace");
		
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
		OreDictionary.registerOre("ingotRefinedIron", new ItemStack(ingots,1,3));
		
		prox.registerRenderThings();
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		GameRegistry.addShapelessRecipe(new ItemStack(carbonization.hhpulv), new Object[] {new ItemStack(Item.flint), new ItemStack(Item.bowlEmpty)});
		GameRegistry.addRecipe(new ItemStack(carbonization.hhcomp), new Object[] {" O ", "S O", " S ", 'O', Block.obsidian, 'S', Item.stick});
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonization.hhpure), new Object[]{" I ", "ICI", "SI ", 'I', Item.ingotIron, 'C', "dustACharcoal", 'S', Item.stick}));
		
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
		
		//Activated Charcoal
		FurnaceRecipes.smelting().addSmelting((new ItemStack(dust,1,0)).itemID, 0, new ItemStack(dust,1,8), 0);
		
		//Simple Recipes
		GameRegistry.addRecipe(new ItemStack(misc,1,0), new Object[]{"S","G", 'S', Item.stick, 'G', new ItemStack(fuel,1,5)});
		GameRegistry.addRecipe(new ItemStack(misc,1,0), new Object[]{"S","C", 'S', Item.stick, 'C', new ItemStack(Item.coal,1,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.writableBook,1), new Object[]{Item.book, new ItemStack(misc,1,0)});
		//GameRegistry.addShapelessRecipe(new ItemStack(Item.writableBook,1), new Object[]{Item.book, new ItemStack(misc,1,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(misc,1,2), new Object[]{new ItemStack(Item.potion,1,0), new ItemStack(dust, 1, 8)});
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 2), new Object[]{"C","S",'C', new ItemStack(fuel,1,2), 'S', Item.stick});
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"C","S",'C', new ItemStack(fuel,1,3), 'S', Item.stick});
		GameRegistry.addRecipe(new ItemStack(Block.torchWood, 8), new Object[]{"C","S",'C', new ItemStack(fuel,1,4), 'S', Item.stick});
		
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
		
		if(Loader.isModLoaded("ThermalExpansion"))
		{
			try
			{
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
				System.out.println("Oh dear, something broke with Thermal Expansion.  Prod Mal so he can fix it.");
			}
		}
		
		if(Loader.isModLoaded("IC2"))
		{
			try
			{
				//Use IC2 refined iron
				//don't make an iron furnace recipe since there is already one, just use IC2's furnace instead
				GameRegistry.addRecipe(new ItemStack(furnaceBlock,1,1), new Object[]{"III", "IFI", "BBB", 'I', new ItemStack(ingots,1,1), 'F', Items.getItem("ironFurnace"), 'B', Block.brick});
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,8), new Object[]{"SSS", "SFS", "SSS", 'S' ,"ingotRefinedIron", 'F', new ItemStack(furnaceBlock,1,1)}));
				
				
				//refined iron into pig iron
				CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 1, Items.getItem("refinedIronIngot"), 5, (50*difficultyMod));
				//pig iron into mild steel
				CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 2, new ItemStack(ingots,1,1), 5, (75*difficultyMod));
				//mild steel into steel
				CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 3, new ItemStack(ingots,1,2), 5, (100*difficultyMod));
				
				//mash
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,0), new ItemStack(dust,1,1));
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,1), new ItemStack(dust,1,2));
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,2), new ItemStack(dust,1,3));
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,3), new ItemStack(dust,1,4));
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,4), new ItemStack(dust,1,6));
				Ic2Recipes.addMaceratorRecipe(new ItemStack(fuel,1,5), new ItemStack(dust,1,7));
				
				//cleanse
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,1,5), new ItemStack(dust,1,0));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,1,5), new ItemStack(dust,1,0));
				
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,1,2), new ItemStack(dust,1,1));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,1,1), new ItemStack(dust,1,2));
				
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,4,3), new ItemStack(dust,5,2));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,5,2), new ItemStack(dust,4,3));
				
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,2,4), new ItemStack(dust,3,3));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,3,3), new ItemStack(dust,2,4));
				
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,2,5), new ItemStack(dust,3,4));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,3,4), new ItemStack(dust,2,5));
				
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,5,6), new ItemStack(dust,6,5));
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,6,5), new ItemStack(dust,5,6));
				
				Ic2Recipes.addExtractorRecipe(new ItemStack(dust,1,6), new ItemStack(dust,6,7));
				//Ic2Recipes.addExtractorRecipe(new ItemStack(dust,6,7), new ItemStack(dust,1,6));
				
				
				//mush
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,0), new ItemStack(Item.coal, 1,1));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,1), new ItemStack(fuel,1,0));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,2), new ItemStack(fuel,1,1));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,3), new ItemStack(fuel,1,2));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,4), new ItemStack(fuel,1,3));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,5), new ItemStack(Item.coal, 1,0));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,6), new ItemStack(fuel,1,4));
				Ic2Recipes.addCompressorRecipe(new ItemStack(dust,1,7), new ItemStack(fuel,1,5));
			}
			catch(Exception e)
			{
				System.out.println("Oh dear, something broke with IC2.  Prod Mal so he can fix it.");
			}
		}
		else
		{
			//no IC2, so we use our iron furnace and refined iron
			GameRegistry.addRecipe(new ItemStack(furnaceBlock,1,0), new Object[]{" I ", "I I", "IFI", 'I', Item.ingotIron, 'F', Block.furnaceIdle});
			GameRegistry.addRecipe(new ItemStack(furnaceBlock,1,1), new Object[]{"III", "IFI", "BBB", 'I', new ItemStack(ingots,1,0), 'F', new ItemStack(furnaceBlock,1,0), 'B', Block.brick});
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(furnaceBlock,1,2), new Object[]{"SSS", "SFS", "SSS", 'S' ,"ingotRefinedIron", 'F', new ItemStack(furnaceBlock,1,1)}));
			//TODO: FIX ME
			//iron into refined iron
			FurnaceRecipes.smelting().addSmelting(new ItemStack(Item.ingotIron).itemID, new ItemStack(ingots,1,0), 5);
			//refined iron into pig iron
			CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 0, new ItemStack(ingots,1,1), 5, (50*difficultyMod));
			//pig iron into mild steel
			CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 1, new ItemStack(ingots,1,2), 5, (75*difficultyMod));
			//mild steel into steel
			CarbonizationRecipes.smelting().addSmelting(ingots.itemID, 2, new ItemStack(ingots,1,3), 5, (100*difficultyMod));
		}
	}

}
