package mal.carbonization;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import mal.carbonization.block.*;
import mal.carbonization.item.*;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.CommonProxy;
import mal.carbonization.world.FuelHandler;
import mal.carbonization.world.WorldgeneratorCarbonization;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=carbonization.MODID, version=carbonization.VERSION, dependencies="required-after:MalCore")
public class carbonization {
    public static final String MODID = "Carbonization";
    public static final String VERSION = "1.1.0";
    public static final String VersionURL = "https://www.dropbox.com/s/ao6p1iye6w2suec/malcoreVersion.info?dl=1";
    
	public static int ORESLAGRATIO = 300;//number of millibuckets needed for an item
	public static int MAXAUTOCRAFTTIME = 600;//maximum amount of time to cooldown
	public static int MINAUTOCRAFTTIME = 5;//minimum  amount of time to cooldown
	public static int BASEAUTOCRAFTFUEL = 800;//maximum amount of fuel per craft
	public static int MAXSCANNERDEPTH = 32;//maximum depth the scanner will scan
	public static boolean VERBOSEMODE = false;//enable verbose logging of stuff like version info
	public static boolean REPORTTOCHAT = false;//report some information to chat
	
	@SidedProxy(clientSide = "mal.carbonization.network.ClientProxy", serverSide = "mal.carbonization.network.CommonProxy")
	public static CommonProxy prox;
	
	/*
	public static ItemDust dust;
	public static ItemMisc misc;
	public static ItemIngots ingots;
	public static ItemHHPulverizer hhpulv;
	public static ItemHHPurifyer hhpure;
	public static ItemHHCompressor hhcomp;
	public static ItemPortableScanner portableScanner;

	public static BlockFurnaces furnaceBlock;
	public static BlockFurnaces furnaceBlockActive;
	public static BlockFurnaceControl FurnaceControl;
	public static BlockMultiblockFurnaceControl multiblockFurnaceControl;
	public static BlockStructureBlock structure;
	public static BlockScaffolding scaffold;
	public static BlockTunnelBore tunnelBore;
	public static ItemStructureBlock itemStructureBlock;
	public static BlockAutocraftingBench autocraftingBench;
	public static ItemRecipeCharm recipeCharm;
	public static ItemFuelCell fuelCell;
	public static ItemScrewdriver screwdriver;
	public static ItemUpgradeItems upgradeItems;
    

	public static CreativeTabs tabMachine = new CreativeTabs("tabMachine"){
		public ItemStack getIconItemStack() { return new ItemStack(autocraftingBench,1,0);}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return null;
		}
	};*/
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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	CarbonizationPacketHandler.init();
    	
		GameRegistry.registerFuelHandler(new FuelHandler());
		GameRegistry.registerWorldGenerator(new WorldgeneratorCarbonization(), 0);
		
    	carbonizationItems.init();
    	carbonizationBlocks.init();
    	carbonizationTileEntities.init();
    	
		prox.setCustomRenderers();
    }
}
