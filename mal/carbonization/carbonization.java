package mal.carbonization;

import org.apache.logging.log4j.Level;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import mal.carbonization.block.*;
import mal.carbonization.fluids.BucketHandler;
import mal.carbonization.item.*;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.CommonProxy;
import mal.carbonization.world.FuelHandler;
import mal.carbonization.world.WorldgeneratorCarbonization;
import mal.core.util.MalLogger;
import mal.core.util.ModList;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=carbonization.MODID, version=carbonization.VERSION, dependencies="required-after:MalCore")
public class carbonization {
    public static final String MODID = "Carbonization";
    public static final String VERSION = "1.1.5";
    public static final String VersionURL = "https://www.dropbox.com/s/7k0i9pbxmywhe2i/carbonizationVersion.info?dl=1";
    
	public static int ORESLAGRATIO = 300;//number of millibuckets needed for an item
	public static int MAXAUTOCRAFTTIME = 600;//maximum amount of time to cooldown
	public static int MINAUTOCRAFTTIME = 5;//minimum  amount of time to cooldown
	public static int BASEAUTOCRAFTFUEL = 800;//maximum amount of fuel per craft
	public static int MAXSCANNERDEPTH = 32;//maximum depth the scanner will scan
	public static boolean VERBOSEMODE = false;//enable verbose logging of stuff like version info
	public static boolean REPORTTOCHAT = false;//report some information to chat
	public static int FPUNITVALUE = 1;//amount of FP per millibucket
	//Difficulty modifier, the higher the number, the more time the metals take to bake
	public static int DIFFICULTYMOD = 10;
	
	@SidedProxy(clientSide = "mal.carbonization.network.ClientProxy", serverSide = "mal.carbonization.network.CommonProxy")
	public static CommonProxy prox;

	public static CreativeTabs tabMachine = new CreativeTabs("tabMachine"){
		public ItemStack getIconItemStack() { return new ItemStack(carbonizationBlocks.furnaceBlock,1,0);}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	public static CreativeTabs tabItems = new CreativeTabs("tabItems"){
		public ItemStack getIconItemStack() { return new ItemStack(carbonizationItems.fuel,1,2);}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	public static CreativeTabs tabStructure = new CreativeTabs("tabStructure"){
		public ItemStack getIconItemStack() { return new ItemStack(carbonizationItems.structureItem,1,0);}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	@Instance(value = carbonization.MODID)
	public static carbonization carbonizationInstance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        DIFFICULTYMOD = config.get("Modifiers", "Metal Cook Time", 10).getInt();
		if (DIFFICULTYMOD<=0)
		{
			MalLogger.addLogMessage(Level.WARN, "Metal Cook time modifier is a invalid number, using the default value instead.");
			DIFFICULTYMOD = 10;
		}
		MAXAUTOCRAFTTIME = config.get("Modifiers", "Max Machine Cooldown Time", 300).getInt();
		if (MAXAUTOCRAFTTIME<=0)
		{
			MalLogger.addLogMessage(Level.WARN, "Max Autocrafting cooldown time modifier is a invalid number, using the default value instead.");
			MAXAUTOCRAFTTIME = 300;
		}
		MINAUTOCRAFTTIME = config.get("Modifiers", "Min Autocrafting Cooldown Time", 5).getInt();
		if(MINAUTOCRAFTTIME<=0)
		{
			MalLogger.addLogMessage(Level.WARN, "Min Autocrafting cooldown time modifier is a invalid number, using the default value instead.");
			MINAUTOCRAFTTIME = 5;
		}
		if(MINAUTOCRAFTTIME > MAXAUTOCRAFTTIME)
		{
			MalLogger.addLogMessage(Level.WARN, "Min Autocrafting cooldown less then Max, setting minimum value to maximum");
			MINAUTOCRAFTTIME = MAXAUTOCRAFTTIME;
		}
		BASEAUTOCRAFTFUEL = config.get("Modifiers", "Max Machine Fuel Usage", 800).getInt();
		if (BASEAUTOCRAFTFUEL<=0)
		{
			MalLogger.addLogMessage(Level.WARN, "Autocrafting fuel usage modifier is a invalid number, using the default value instead.");
			DIFFICULTYMOD = 800;
		}
		
		REPORTTOCHAT = config.get("Options", "Report to Chat", true).getBoolean(true);
		
		config.save();
    	CarbonizationPacketHandler.init();
    	
		GameRegistry.registerFuelHandler(new FuelHandler());
		GameRegistry.registerWorldGenerator(new WorldgeneratorCarbonization(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(carbonizationInstance, prox);
		MinecraftForge.EVENT_BUS.register(BucketHandler.instance);
		
    	carbonizationBlocks.init();
    	carbonizationItems.init();
    	carbonizationTileEntities.init();
    	
		prox.setCustomRenderers();
		ModList.addMod(this.MODID, this.VERSION, VersionURL);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	carbonizationOreDictionary.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	carbonizationRecipes.furnaceRecipes();
    	carbonizationRecipes.miscCraftingRecipes();
    	carbonizationRecipes.toolRecipes();
    	carbonizationRecipes.structureBlockRecipes(false);//TODO: add in factorization detection
    	carbonizationRecipes.fuelConversionBenchRecipes();
    	carbonizationRecipes.machineRecipes();
    	carbonizationRecipes.guidebookRecipes();
    	
    	carbonizationBlocks.postinit();
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