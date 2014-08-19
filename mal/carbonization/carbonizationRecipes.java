package mal.carbonization;

import cpw.mods.fml.common.registry.GameData;
import mal.core.GuidebookPage;
import mal.core.GuidebookRegistry;
import mal.core.reference.UtilReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class carbonizationRecipes {

	public static void structureBlockRecipes(boolean factorizationDetected)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,0), new Object[]{"x x", " y ", "x x", 'x', "stone", 'y', "stone"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,1), new Object[]{"x x", " y ", "x x", 'x', Blocks.ice, 'y', Blocks.ice}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,1,1), new Object[]{"x x", " y ", "x x", 'x', Items.snowball, 'y', Items.snowball}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,2), new Object[]{"x x", " y ", "x x", 'x', "ingotIron", 'y', "ingotIron"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,3), new Object[]{"x x", " y ", "x x", 'x', Items.brick, 'y', Items.brick}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,4), new Object[]{"x x", " y ", "x x", 'x', "ingotSteel", 'y', "ingotSteel"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,5), new Object[]{"x x", " y ", "x x", 'x', new ItemStack(carbonizationItems.miscItem,1,13), 'y', new ItemStack(carbonizationItems.miscItem,1,13)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,6), new Object[]{"x x", " y ", "x x", 'x', "ingotTi6Al4V", 'y', "ingotTi6Al4V"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,7), new Object[]{"x x", " y ", "x x", 'x', new ItemStack(carbonizationItems.miscItem,1,14), 'y', new ItemStack(carbonizationItems.miscItem,1,14)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,8), new Object[]{"x x", " y ", "x x", 'x', Items.nether_star, 'y', Blocks.dragon_egg}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,5,9), new Object[]{"x x", " y ", "x x", 'x', "ingotCoCr", 'y', "ingotCoCr"}));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.stone), new Object[]{new ItemStack(carbonizationItems.structureItem,1,0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.ice), new Object[]{new ItemStack(carbonizationItems.structureItem,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.iron_ingot), new Object[]{new ItemStack(carbonizationItems.structureItem,1,2)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.brick), new Object[]{new ItemStack(carbonizationItems.structureItem,1,3)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.ingots,1,3), new Object[]{new ItemStack(carbonizationItems.structureItem,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,1,13), new Object[]{new ItemStack(carbonizationItems.structureItem,1,5)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.ingots,1,4), new Object[]{new ItemStack(carbonizationItems.structureItem,1,6)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,1,14), new Object[]{new ItemStack(carbonizationItems.structureItem,1,7)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.dragon_egg), new Object[]{new ItemStack(carbonizationItems.structureItem,1,8)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.ingots,1,5), new Object[]{new ItemStack(carbonizationItems.structureItem,1,9)}));

		for(int i = 0; i < 10; i++)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,4,1000+i), new Object[]{" x ", "xyx", " x ", 'x', new ItemStack(carbonizationItems.structureItem,1,i), 'y', (i<4)?(Blocks.furnace):((i<6)?(new ItemStack(carbonizationBlocks.furnaceBlock,1,0)):((i<8)?(new ItemStack(carbonizationBlocks.furnaceBlock,1,1)):(new ItemStack(carbonizationBlocks.furnaceBlock,1,2))))}));
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.structureItem,1,i), new Object[]{new ItemStack(carbonizationItems.structureItem,1,1000+i)}));

			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.structureItem,4,2000+i), new Object[]{" x ", "xyx", " x ", 'x', new ItemStack(carbonizationItems.structureItem,1,i), 'y', (i<2)?(new ItemStack(carbonizationItems.miscItem,1,7)):((i<4)?(new ItemStack(carbonizationItems.miscItem,1,8)):((i<6)?(new ItemStack(carbonizationItems.miscItem,1,9)):((i<8)?(new ItemStack(carbonizationItems.miscItem,1,10)):(new ItemStack(carbonizationItems.miscItem,1,11)))))}));
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.structureItem,1,i), new Object[]{new ItemStack(carbonizationItems.structureItem,1,2000+i)}));
		}
	}

	public static void machineRecipes()
	{
		//singleblock furnaces
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.furnaceBlock,1,0), 
				new Object[]{"   ", "III", "IFI", 'I' ,Items.iron_ingot, 'F', Blocks.furnace}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.furnaceBlock,1,1), 
				new Object[]{"III", "IFI", "BBB", 'I' ,"ingotCRefinedIron", 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,0), 'B', Blocks.brick_block}));//TODO: rawr
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.furnaceBlock,1,2), 
				new Object[]{"SSS", "SFS", "SSS", 'S' ,"ingotSteel", 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,1)}));

		//multiblock furnace instantiator
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.furnaceControlBlock, 1), new Object[]
				{"SSS", "SFS", "SSS", 'S', new ItemStack(carbonizationItems.structureItem,1,2), 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.furnaceControlBlock, 1), new Object[]
				{"SSS", "SFS", "SSS", 'S', new ItemStack(carbonizationItems.structureItem,1,3), 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,1)}));

		//autocrafting bench
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,0), new Object[]
				{"BBB", "SCS", "SFS", 'B', new ItemStack(carbonizationItems.structureItem,1,4), 'S', new ItemStack(carbonizationItems.structureItem,1,2004), 'C', Blocks.crafting_table, 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,0), new Object[]
				{"BBB", "SCS", "SFS", 'B', new ItemStack(carbonizationItems.structureItem,1,5), 'S', new ItemStack(carbonizationItems.structureItem,1,2005), 'C', Blocks.crafting_table, 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,1)}));

		//fuel conversion bench
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,1), new Object[]
				{"SSS", "CUP", "UFU", 'S', new ItemStack(carbonizationItems.structureItem,1,2), 'P', carbonizationItems.HHPulv, 'U', new ItemStack(carbonizationItems.structureItem,1,2002), 'C', carbonizationItems.HHComp, 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,2)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,1), new Object[]
				{"SSS", "CUP", "UFU", 'S', new ItemStack(carbonizationItems.structureItem,1,3), 'P', carbonizationItems.HHPulv, 'U', new ItemStack(carbonizationItems.structureItem,1,2003), 'C', carbonizationItems.HHComp, 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,2)}));
		
		//tunnel bore
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.tunnelboreBlock), new Object[]{"CBC", "COC", "CFC", 'C', new ItemStack(carbonizationItems.miscItem,1,16), 'B', new ItemStack(carbonizationItems.miscItem,1,15), 'O', new ItemStack(carbonizationItems.miscItem,1,18), 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,2)}));
	}

	public static void toolRecipes()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.hhpulv), new Object[] {new ItemStack(Items.flint), new ItemStack(Items.bowl)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.hhcomp), new Object[] {" O ", "S O", " S ", 'O', Blocks.obsidian, 'S', Items.stick}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.screwdriverItem), new Object[]{"  I", " I ", "G  ", 'I', "ingotPigIron", 'G', "fuelGraphite"}));

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,0), new Object[]{"S","G", 'S', Items.stick, 'G', new ItemStack(carbonizationItems.fuel,1,2)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,0), new Object[]{"S","C", 'S', Items.stick, 'C', new ItemStack(Items.coal,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.writable_book), new Object[]{Items.book, new ItemStack(carbonizationItems.miscItem,1,0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,1,1), new Object[]{new ItemStack(Items.potionitem,1,0), "dustACharcoal"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,1,2), new Object[]{new ItemStack(Items.potionitem,1,0), "dustCoal"}));
		
		//efficiency
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,0), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2000), new ItemStack(carbonizationItems.structureItem,1,0));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,0), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2001), new ItemStack(carbonizationItems.structureItem,1,1));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,1), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2002), new ItemStack(carbonizationItems.structureItem,1,2));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,1), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2003), new ItemStack(carbonizationItems.structureItem,1,3));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,2), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,2), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,3), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2006), new ItemStack(carbonizationItems.structureItem,1,6));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,3), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2007), new ItemStack(carbonizationItems.structureItem,1,7));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,4), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2008), new ItemStack(carbonizationItems.structureItem,1,8));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,4), Items.glowstone_dust, new ItemStack(carbonizationItems.structureItem,1,2009), new ItemStack(carbonizationItems.structureItem,1,9));
		
		//fortune
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,5), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2000), new ItemStack(carbonizationItems.structureItem,1,0));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,5), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2001), new ItemStack(carbonizationItems.structureItem,1,1));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,6), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2002), new ItemStack(carbonizationItems.structureItem,1,2));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,6), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2003), new ItemStack(carbonizationItems.structureItem,1,3));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,7), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,7), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,8), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2006), new ItemStack(carbonizationItems.structureItem,1,6));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,8), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2007), new ItemStack(carbonizationItems.structureItem,1,7));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,9), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2008), new ItemStack(carbonizationItems.structureItem,1,8));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,9), new ItemStack(Items.dye,1,4), new ItemStack(carbonizationItems.structureItem,1,2009), new ItemStack(carbonizationItems.structureItem,1,9));
		
		//haste
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,10), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2000), new ItemStack(carbonizationItems.structureItem,1,0));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,10), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2001), new ItemStack(carbonizationItems.structureItem,1,1));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,11), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2002), new ItemStack(carbonizationItems.structureItem,1,2));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,11), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2003), new ItemStack(carbonizationItems.structureItem,1,3));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,12), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,12), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,13), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2006), new ItemStack(carbonizationItems.structureItem,1,6));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,13), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2007), new ItemStack(carbonizationItems.structureItem,1,7));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,14), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2008), new ItemStack(carbonizationItems.structureItem,1,8));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,14), new ItemStack(Items.redstone), new ItemStack(carbonizationItems.structureItem,1,2009), new ItemStack(carbonizationItems.structureItem,1,9));
		
		//storage
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,15), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2000), new ItemStack(carbonizationItems.structureItem,1,0));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,15), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2001), new ItemStack(carbonizationItems.structureItem,1,1));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,16), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2002), new ItemStack(carbonizationItems.structureItem,1,2));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,16), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2003), new ItemStack(carbonizationItems.structureItem,1,3));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,17), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,17), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,18), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2006), new ItemStack(carbonizationItems.structureItem,1,6));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,18), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2007), new ItemStack(carbonizationItems.structureItem,1,7));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,19), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2008), new ItemStack(carbonizationItems.structureItem,1,8));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,19), new ItemStack(Items.bucket), new ItemStack(carbonizationItems.structureItem,1,2009), new ItemStack(carbonizationItems.structureItem,1,9));
		
		//fixed
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,20), new ItemStack(Blocks.fence), new ItemStack(carbonizationItems.structureItem,1,2000), new ItemStack(carbonizationItems.structureItem,1,0));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,20), new ItemStack(Blocks.fence), new ItemStack(carbonizationItems.structureItem,1,2001), new ItemStack(carbonizationItems.structureItem,1,1));
		//void
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,21), new ItemStack(Blocks.chest), new ItemStack(carbonizationItems.structureItem,1,2002), new ItemStack(carbonizationItems.structureItem,1,2));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,21), new ItemStack(Blocks.chest), new ItemStack(carbonizationItems.structureItem,1,2003), new ItemStack(carbonizationItems.structureItem,1,3));
		//ejection
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,22), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,22), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		//silk touch
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,28), new ItemStack(Items.emerald), new ItemStack(carbonizationItems.structureItem,1,2006), new ItemStack(carbonizationItems.structureItem,1,6));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,28), new ItemStack(Items.emerald), new ItemStack(carbonizationItems.structureItem,1,2007), new ItemStack(carbonizationItems.structureItem,1,7));
		//hardness
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,29), new ItemStack(Items.nether_star), new ItemStack(carbonizationItems.structureItem,1,2008), new ItemStack(carbonizationItems.structureItem,1,8));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,29), new ItemStack(Items.nether_star), new ItemStack(carbonizationItems.structureItem,1,2009), new ItemStack(carbonizationItems.structureItem,1,9));
	}
	
	/**
	 * Follows the pattern "FSF", "FMF", "FSF"
	 * should be itemstacks or strings
	 */
	private static void addUpgradeRecipe(ItemStack output, Object mainItem, Object secondaryItem, Object fillerItem)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output,new Object[]{"FSF", "FMF", "FSF", 'F', fillerItem, 'S', secondaryItem, 'M', mainItem}));
	}

	public static void miscCraftingRecipes()
	{
		//handheld fuel to dust
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,0), new Object[]{carbonizationItems.HHPulv, new ItemStack(Items.coal, 1, 1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,1), new Object[]{carbonizationItems.HHPulv,"fuelPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,2), new Object[]{carbonizationItems.HHPulv, new ItemStack(Items.coal, 1, 0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,3), new Object[]{carbonizationItems.HHPulv,"fuelAnthracite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,4), new Object[]{carbonizationItems.HHPulv,"fuelGraphite"}));

		//handheld dust to fuel
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.coal,1,1),new Object[]{carbonizationItems.HHComp, "dustCharcoal"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,1,0), new Object[]{carbonizationItems.HHComp, "dustPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,1,1), new Object[]{carbonizationItems.HHComp, "dustAnthracite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,1,2), new Object[]{carbonizationItems.HHComp, "dustGraphite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.coal,1,0),new Object[]{carbonizationItems.HHComp, "dustCoal"}));

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(Blocks.torch, 8), new Object[]{"C","S",'C', new ItemStack(carbonizationItems.fuel,1,2), 'S', Items.stick}));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 3), new Object[]{carbonizationItems.HHComp, new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4), new ItemStack(carbonizationItems.dust,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,13), new Object[]{"GGG", 'G', new ItemStack(carbonizationItems.dust,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,14), new Object[]{"GGG", 'G', new ItemStack(carbonizationItems.miscItem,1,12)}));

		//CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(fuelCell,1,0), new Object[]{" S ", "SCS", " S ", 'S', "ingotSteel", 'C', new ItemStack(Item.coal,1,0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.portablescannerItem), new Object[]{"LAL", "GDG", "OFO", 'L', Items.leather, 'A', "dyeBlue", 'G', new ItemStack(carbonizationItems.miscItem,1,11), 'D', Items.diamond, 'O', Items.gold_ingot, 'F', new ItemStack(carbonizationBlocks.furnaceBlock,1,2)}));


		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.diamond), new Object[]{carbonizationItems.HHComp, new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3), 
			new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3), new ItemStack(carbonizationItems.miscItem, 1, 3)}));

		//CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(miscItem,4,5), new Object[]{"xyx", "yxy", "xyx", 'x', Blocks.sand, 'y', new ItemStack(carbonizationItems.miscItem, 1, 3)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,0), new Object[]{new ItemStack(carbonizationItems.miscItem, 1, 6), new ItemStack(carbonizationItems.miscItem, 1, 6), new ItemStack(carbonizationItems.miscItem, 1, 6), new ItemStack(carbonizationItems.miscItem, 1, 6)}));

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 7), new Object[]{"x x", " y ", "x x", 'x', Items.iron_ingot, 'y', Items.redstone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 8), new Object[]{"x x", " y ", "x x", 'x', "ingotCRefinedIron", 'y', Items.redstone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 9), new Object[]{"x x", " y ", "x x", 'x', "ingotPigIron", 'y', Items.redstone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 10), new Object[]{"x x", " y ", "x x", 'x', "ingotHCSteel", 'y', Items.redstone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem, 1, 11), new Object[]{"x x", " y ", "x x", 'x', "ingotSteel", 'y', Items.redstone}));

		//CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(misc,1,19), new Object[]{"xxx", 'x', "ingotSteel"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,2,12), new Object[]{new ItemStack(carbonizationItems.dust,1,4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,4), new Object[]{new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12)}));

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.fuelBlock,1,0), new Object[]{"xxx","xxx","xxx",'x',"fuelPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.fuelBlock,1,1), new Object[]{"xxx","xxx","xxx",'x',"fuelAnthracite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.fuelBlock,1,2), new Object[]{"xxx","xxx","xxx",'x',"fuelGraphite"}));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,0), new Object[]{new ItemStack(carbonizationBlocks.fuelBlock,1,0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,1), new Object[]{new ItemStack(carbonizationBlocks.fuelBlock,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,2), new Object[]{new ItemStack(carbonizationBlocks.fuelBlock,1,2)}));

		//bit
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,15), new Object[]{" D ", "DSD", " S ", 'D', Items.diamond, 'S', new ItemStack(carbonizationItems.structureItem,1,4)}));
		//casing
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,16), new Object[]{"SSS", "S S", "SSS", 'S', new ItemStack(carbonizationItems.structureItem,1,4)}));
		//circuit
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,17), new Object[]{"RGR", "LEL", "RGR", 'R', Items.redstone, 'G', Items.gold_ingot, 'L', "dyeBlue", 'E', Items.emerald}));
		//core
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,18), new Object[]{"MCM", "CRC", "MCM", 'M', new ItemStack(carbonizationItems.structureItem,1,2004), 'C', new ItemStack(carbonizationItems.miscItem,1,16), 'R', new ItemStack(carbonizationItems.miscItem,1,17)}));

	}

	public static void fuelConversionBenchRecipes()
	{
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(carbonizationItems.miscItem,1,12), 250);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(carbonizationItems.miscItem,1,14), 750);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("fuelGraphite", 1000);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("dustGraphite", 1000);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("fuelPeat", 1200);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("dustPeat", 1200);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(carbonizationItems.miscItem,1,13), 1500);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(Items.coal,1,1), 1600);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("dustCharcoal", 1600);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(Items.coal,1,0), 1600);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("dustCoal", 1600);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("fuelAnthracite", 2400);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("dustAnthracite", 2400);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(carbonizationItems.miscItem,1,3), 8000);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput(new ItemStack(Items.diamond,1,0), 64000);

	}

	public static void furnaceRecipes()
	{
		//iron into refined iron
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(Items.iron_ingot), new ItemStack(carbonizationItems.ingots,1,0), 5);
		CarbonizationRecipeHandler.smelting().addSmelting(new ItemStack(Items.iron_ingot), new ItemStack(carbonizationItems.ingots,1,0), 5, (25*carbonization.DIFFICULTYMOD), 1);

		//refined iron into pig iron
		CarbonizationRecipeHandler.smelting().addSmelting("ingotCRefinedIron", new ItemStack(carbonizationItems.ingots,1,1), 5, (50*carbonization.DIFFICULTYMOD), 1);
		//pig iron into mild steel
		CarbonizationRecipeHandler.smelting().addSmelting("ingotPigIron", new ItemStack(carbonizationItems.ingots,1,2), 5, (75*carbonization.DIFFICULTYMOD), 2);
		//mild steel into steel
		CarbonizationRecipeHandler.smelting().addSmelting("ingotHCSteel", new ItemStack(carbonizationItems.ingots,1,3), 5, (100*carbonization.DIFFICULTYMOD), 2);

		//Activated Charcoal
		FurnaceRecipes.smelting().func_151394_a((new ItemStack(carbonizationItems.dust,1,0)), new ItemStack(carbonizationItems.dust,1,5), 0);

		//Silktouched fuels
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(carbonizationBlocks.fuelBlock, 1, 0), new ItemStack(carbonizationItems.fuel,1,0), 5);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(carbonizationBlocks.fuelBlock, 1, 1), new ItemStack(carbonizationItems.fuel,1,1), 5);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(carbonizationBlocks.fuelBlock, 1, 2), new ItemStack(carbonizationItems.fuel,1,2), 5);
		
		//Add in the ore slag relationships
				CarbonizationRecipeHandler.smelting().addOreSlag("ashSlag", new ItemStack(carbonizationItems.miscItem, 1, 6));
				CarbonizationRecipeHandler.smelting().addOreSlag("stoneSlag", new ItemStack(Blocks.stone));
				CarbonizationRecipeHandler.smelting().addOreSlag("glassSlag", new ItemStack(Blocks.glass));
				CarbonizationRecipeHandler.smelting().addOreSlag("ironSlag", new ItemStack(Items.iron_ingot));
				CarbonizationRecipeHandler.smelting().addOreSlag("goldSlag", new ItemStack(Items.gold_ingot));
				CarbonizationRecipeHandler.smelting().addOreSlag("brickSlag", new ItemStack(Items.brick));
				CarbonizationRecipeHandler.smelting().addOreSlag("brickBlockSlag", new ItemStack(Blocks.brick_block));
				CarbonizationRecipeHandler.smelting().addOreSlag("netherBrickBlockSlag", new ItemStack(Items.netherbrick));
				CarbonizationRecipeHandler.smelting().addOreSlag("copperSlag", "ingotCopper");
				CarbonizationRecipeHandler.smelting().addOreSlag("tinSlag", "ingotTin");
				CarbonizationRecipeHandler.smelting().addOreSlag("silverSlag", "ingotSilver");
				CarbonizationRecipeHandler.smelting().addOreSlag("leadSlag", "ingotLead");
				CarbonizationRecipeHandler.smelting().addOreSlag("nickelSlag", "ingotNickel");
				boolean b = CarbonizationRecipeHandler.smelting().addOreSlag("aluminumSlag", "ingotNaturalAluminum");
				if(!b)
					CarbonizationRecipeHandler.smelting().addOreSlag("aluminumSlag", "ingotAluminum");
				
				//Add in the recipes, normal metals and blocks
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Blocks.cobblestone), 200, 100, "stoneSlag", true);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Blocks.sand), 200, 100, "glassSlag", true);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreIron", 400, 200, "ironSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreGold", 400, 200, "goldSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.clay_ball), 200, 100, "brickSlag", true);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Blocks.clay), 400, 100, "brickBlockSlag", true);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Blocks.netherrack), 400, 100, "netherBrickBlockSlag", true);
				
				//Add in common mod ores through OreDictionary
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreCopper", 400, 200, "copperSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreTin", 400, 200, "tinSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreSilver", 400, 200, "silverSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreLead", 400, 200, "leadSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreNickel", 400, 200, "nickelSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreNaturalAluminum", 400, 200, "aluminumSlag", false);
				
				//food and wood items
				//TODO: Find a way to do this easier
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.apple), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.arrow), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_axe), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.baked_potato), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cooked_beef), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.beef), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.boat), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.bow), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.bowl), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.book), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.mushroom_stew), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.bread), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cake), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.carrot_on_a_stick), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cooked_chicken), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.chicken), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cookie), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_door), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.egg), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.enchanted_book), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.feather), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cooked_fished), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.fish), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.fishing_rod), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_hoe), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.leather_helmet), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.item_frame), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.leather), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.leather_leggings), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.melon), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.melon_seeds), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.painting), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.paper), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_pickaxe), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.poisonous_potato), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.cooked_porkchop), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.porkchop), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.pumpkin_pie), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.pumpkin_seeds), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.leather_chestplate), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.reeds), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.rotten_flesh), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.saddle), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wheat_seeds), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_shovel), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.sign), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.string), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.wooden_sword), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.writable_book), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.written_book), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting(new ItemStack(Items.bow), 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("logWood", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("plankWood", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("slabWood", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("stairWood", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("stickWood", 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("treeSapling", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("treeLeaves", 100, 100, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("slimeball", 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("cropWheat", 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("cropPotato", 50, 50, "ashSlag");
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("cropCarrot", 50, 50, "ashSlag");
				
				for(Object bb: GameData.getBlockRegistry().getKeys())
				{
					
					bb = GameData.getBlockRegistry().getObject((String)bb);
					if(bb instanceof Block)
					{
						Material m = ((Block)bb).getMaterial(); 
						if(m == Material.cloth || m==Material.cactus || m==Material.carpet || m==Material.gourd)
						{
							if(((Block)bb).damageDropped(1) == 1)//simple check for damage values not 0
							{
								for(int j = 0; j<16; j++)
								{
									ItemStack is = new ItemStack((Block)bb, 1, j); 
									if(is != null && ((Block)bb).damageDropped(j) == j && !UtilReference.areItemStacksEqualItem(is, new ItemStack(Blocks.fire)))
									{
										CarbonizationRecipeHandler.smelting().addMultiblockSmelting(is, 100, 100, "ashSlag");
									}
								}
							}
							else
							{
								ItemStack is = new ItemStack((Block)bb);
								if(is != null && is.getItem() != null && !UtilReference.areItemStacksEqualItem(is, new ItemStack(Blocks.fire)))
									CarbonizationRecipeHandler.smelting().addMultiblockSmelting(is, 100, 100, "ashSlag");
							}
							//System.out.println("Added recipe for block: " + ((Block)bb).getLocalizedName());
						}
					}
				}

	}

	public static void guidebookRecipes()
	{
		GuidebookPage page = new GuidebookPage("Carbonization Terms and Basic Information", "Fuel in Minecraft has an inherent value which determines how long a single piece will last in a furnace.  This unit is called a tick, which has the equivalency of 20 ticks = 1 second.  Coal and Charcoal both are worth 1600 ticks.  Carbonization takes this unit and directly relates it to the value of how much work a unit of fuel can perform, called Fuel Tick (FT).  1 FT is equal to a single tick of work in a vanilla furnace.  Carbonization additionally adds liquid processed fuel, called Fuel Potential, which is produced by processing solid fuel items in a Fuel Conversion Bench.  Carbonization also has the concept of insulation and conduction, which for multiblock structures determine the fuel efficiency and processing speed.  Carbonization also does not have process times for non-furnace machines, but instead has cooldowns after the process is completed, allowing for faster job completion in inconsistent environments.  ");
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization World Generation", "Carbonization adds three worldgen fuels, Peat, Anthracite, and Graphite.  Peat is slightly worse than Coal, lasting 1200 ticks in a furnace compared to Coal's 1600, and spawns in upper dirt layers.  It however is harder than the surrounding dirt, requiring a pickaxe to gather.  Anthracite is significantly better than Coal, lasting 2400 ticks in a furnace, and spawns in lower stone layers.  Graphite is a rather poor fuel, only lasting 1000 ticks in a furnace, but is used in most of the crafting in the form of Carbon Thread and Carbon Nanotube.  Each of the fuels can be stored in block form, like Coal can.",
				new ItemStack(carbonizationBlocks.fuelBlock,1,2), null);
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Basic Steel Smelting", "Steel is an alloy of iron (Fe), carbon (C), and sometimes other metals, which has better material properties than only iron.  The most basic method of creating steel is to expose iron to a carbon-rich environment and continue to refine it through smelting until the desired alloy is obtained.  This process is very time consuming, but the technology required to do so is very basic, requiring only an insulated furnace and patience.  In Carbonization this process follows through 5 materials total: Iron, Refined Iron, Pig Iron, High Carbon Steel, and Medium Carbon Steel.  Each one is produced by smelting the previous ingot in a Carbonization singleblock furnace, with progressing to Pig Iron and above requiring an Insulated Iron Furnace.  ",
				new ItemStack(carbonizationItems.ingots,1,3), null);
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Singleblock Furnaces", "The most basic machines in Carbonization are the three singleblock furnaces.  First is the Iron Furnace, which is made by placing a vanilla furnace in the centre block with 5 iron ingots surrounding the top and sides.  The Iron Furnace offers improved speed and efficiency, generating 10 smelting jobs for a piece of coal rather than the vanilla 8.  The next furnace is the Insulated Iron Furnace, which is made as seen above.  The ingots are Carbonization Refined Iron, which are made by smelting iron ingots in an Iron Furnace.  Note that Carbonization smelting recipes will take priority in Carbonization furnaces, so if any other mods that add iron ingot smelting recipes are present you will have to use a non-Carbonization furnace to make those recipes.  The Insulated Iron Furnace has slightly improved smelting speeds over the Iron Furnace, but the major upgrade is that the Insulated Iron Furnace will not use any fuel when there is no work to do.  The third furnace is the Insulated Steel Furnace, which is made by surrounding an Insulated Iron Furnace with Mild Steel.  This furnace processes even faster than the Insulated Iron Furnace and also does not consume fuel when there is no work.  ",
				new ItemStack(carbonizationBlocks.furnaceBlock, 1, 1), new ItemStack[]{new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationBlocks.furnaceBlock,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(Blocks.brick_block),new ItemStack(Blocks.brick_block),new ItemStack(Blocks.brick_block)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Structure Blocks", "Structure Blocks are the core crafting component in Carbonization.  Like many other mod's machine cores, Structure Blocks come in multiple varieties.  Structure Blocks are designated by a base material and a purpose.  There are 10 base materials, which determine the base quality of the block, the rarer materials producing higher stats.  The purpose of the block determines what it can be used for.  For most recipes, any structure block can be used provided that all structure blocks used are the same kind in the recipe.  In some tools and machines, structure blocks are also used to designate effect volumes.  ",
				new ItemStack(carbonizationItems.structureItem, 1, 4), new ItemStack[]{new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Furnace Control Block", "The Furnace Control Block is the key block to building and using an Industrial Furnace.  " +
				"This block, when placed in any side of a orthorhombic hollow structure made of any furnace structure blocks for the base and any standard " +
				"structure blocks for the walls and top will allow you to initilize the structure and process a large number of items in a rather indescriminate way.  " +
				"The lengths of each side for this hollow structure can be of any value between 3 and 9 for the x and z (horizontal) axes and 3 and 6 for the y " +
				"(vertical) axis.  In addition, the precise type of structure block used in any particular space does not need to be homogeneous with any other " +
				"block provided that the base structure of furnace on the bottom, standard everywhere else is followed.  The furnace has a certain tier of quality " +
				"for both Conduction and Insulation, which along with the size of the furnace determine certain characteristics.  First, the larger the furnace the " +
				"more items can be processed at once and the more fuel can be stored.  Second, the total yield of the furnace is determined by both tiers.  Third, the " +
				"processing speed is improved by Conduction and the fuel efficiency is improved by Insulation.  Items can be inserted on any side except the bottom and " +
				"extracted from any side except the top.  The furnace has a single stream input which will process items indiscriminatly, so make sure that anything you " +
				"place in the furnace is something you wish to subject to extreme temperatures.",
				new ItemStack(carbonizationBlocks.furnaceControlBlock,1,0), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationBlocks.furnaceBlock,1,2), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Autocrafting Bench", "The Autocrafting Bench is used to automatically craft recipes while consuming some amount of fuel each process.  Unlike other machines that have cooldowns, the Autocrafting Bench does not accept Haste upgrades, instead allowing the user to scale the fuel consumption and reduce the cooldown by a similar amount.  The Autocrafting Bench also does not accept solid fuel, requiring Fuel Potential to be ejected or piped in from another machine.  The crafting grid in the top of the gui functions identical to a vanilla crafting grid, with the autocrafting materials going in the left inventory and the output being deposited in the right inventory.  ",
				new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,0), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(Blocks.crafting_table),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(carbonizationBlocks.furnaceBlock,1,1),new ItemStack(carbonizationItems.structureItem,1,2004)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Fuel Conversion Bench", "The Fuel Conversion Bench is the machine used to generate Fuel Potential (FP) from fuel items and create fuel items from existing FP.  The gui has three buttons, a Liquid/Solid toggle, and two buttons that cycle through the known items.  The toggle will indicate which resource is being consumed in its text.  In Solid mode any items in the inventory that have a known conversion recipe will be processed one at a time into a certain amount of FP dependant on their base value times a multiplier that can be seen by mousing over the cooldown arrow.  This value starts at 1 and increases by 7% for every level of Fortune there is in the upgrade slots.  The current and max cooldown for the machine can also be seen in this tooltip.  An Ejection upgrade in this mode will try to eject the liquid FP.  In Liquid mode the FP is used to create a fuel item that is selected through the Prev and Next buttons.  Mousing over the item icon will display the item and how much potential is needed to make that item.  There is also a creation cost multiplyer which starts at 2 and is reduced by 7% for each level of Fortune there is in the upgrade slots.  In this mode an Ejection upgrade will try to eject solid items.  ",
				new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,1), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.hhcomp), new ItemStack(carbonizationItems.structureItem,1,2002),new ItemStack(carbonizationItems.hhpulv),new ItemStack(carbonizationItems.structureItem,1,2002),new ItemStack(carbonizationBlocks.furnaceBlock,1,1),new ItemStack(carbonizationItems.structureItem,1,2002)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Industrial Tunnel Bore", "The Industrial Tunnel Bore is a machine designed with one purpose in mind, to remove large sections of the world and deposit them for further processing.  The fuel requirements to do this are rather high as well as dependant on the hardness of the blocks that are being dug.  The size of the dig head is scalable independently in both the horizontal and vertical from 1x1 to 15x15.  This size can be controlled by placing structure blocks in the left two inventory slots in the gui.  As the bore digs, it leaves behind a fragile scaffolding to maintain the integrity of the tunnel and prevent the undesired \"hall'o'mobs\" left behind other such methods.  This scaffolding can be toggled to either be hollow or solid, the only difference being a slightly higher cpu load with the hollow scaffold pattern.  The bore requires an inventory opposite the dig face (the face with the drill heads on it) that will accept the output of the mining process.  If the inventory does not have sufficient space for all the mined items, they will be temporarily stored in an internal buffer and the bore will stop working until the buffer is emptied.  Adding a void upgrade will cause this buffer to be cleared and any items still in it after the inventory is filled will be deleted.  The bore will also stop working if there is not enough fuel to mine a block or if it encounters an indestructible block and the Hardness upgrade is not installed.  The bore will additionally not progress the dig cycle unless a redstone signal is applied to the block.  ",
				new ItemStack(carbonizationBlocks.tunnelboreBlock), new ItemStack[]{new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,15),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,18),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationBlocks.furnaceBlock,1,2),new ItemStack(carbonizationItems.miscItem,1,16)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Upgrades", "There are 9 types of upgrades for machines at the moment: Haste, Efficiency, Fortune, Storage, Fixed, Void, Ejection, Silk Touch, and Hardness.  Haste reduces the max cooldown of machines that have cooldowns by a multiplicative percentage.  Example: I have a machine with 600 tick cooldown and put two Efficiency 1 upgrades in it (-10% cooldown).  I now have a 600*0.9*0.9=486 tick cooldown.  Efficiency reduces the fuel consumption of a process in machines that use fuel by a multiplicative percentage, similar to the Haste upgrade does for cooldown.  Fortune adds an additive fortune effect to machine outputs that have variable conditions.  Example: I have a tunnel bore and I put two Fortune 2 upgrades in it.  That bore digs a diamond ore and produces between 1 and 5 diamonds.  Storage additively increases the amount of Fuel Potential a machine can store by a unit depending on the machine.  In most cases Storage will increase the first number by 1 per level of Storage.  Fixed forces machines that have iterative effects to not iterate.  Example, the tunnel bore head moves forwards one block every time it digs.  Placing the Fixed upgrade prevents that movement and disables scaffold generation, causing the bore to dig the same area every cycle.  Fixed does not reset the counter in the machine, it only prevents that counter from changing.  Void causes machines that have output inventories or backlog queues to delete any overflow items instead of jamming operation.  Ejection will cause machines that do not already auto-eject to try to auto-eject their output to an appropriate container adjacent to a particular side of the block.  Right-clicking the upgrade will cycle through the different sides.  Silk Touch makes machines that break blocks do so as though they had the Silk Touch enchant.  Hardness causes machines that have calculations that are block hardness dependant to not do those calculations.  ",
				new ItemStack(carbonizationItems.upgradeItem,1,3), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(Items.glowstone_dust), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Carbonization Portable Scanner", "This tool, when provided with sufficient fuel, will scan a volume of blocks in the world and report back information about their composition.  It supports a volume of up to 5x5x32.  The Portable Scanner has two primary modes, Basic and Extended.  Basic mode only reports back basic demographics of the volume, such as density and maximum tool material required to mine all blocks in the volume.  Extended mode additionally reports back a list of blocks in the volume for an increased activation cost.  The ? slot doesn't do anything right now.  ",
				new ItemStack(carbonizationItems.portablescannerItem), new ItemStack[]{new ItemStack(Items.leather), new ItemStack(Items.dye,1,4), new ItemStack(Items.leather), new ItemStack(carbonizationItems.miscItem,1,11), new ItemStack(Items.diamond), new ItemStack(carbonizationItems.miscItem,1,11), new ItemStack(Items.gold_ingot), new ItemStack(carbonizationBlocks.furnaceBlock,1,2), new ItemStack(Items.gold_ingot)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("Exciting Product from Carbonization", "Ever been harassed by wayward potion effects but there are no cows for miles?  Or been exploring a mineshaft and wished you could stack milk?  Well never fear, the Cleansing Potion is here!  Simple smelt some charcoal dust in a furnace and mix it in a bottle of water and you have a stacking way to remove potion effects.  In a rush?  Try our new formula with coal dust instead!  No smelting required!  Side effects include blindness, nausea, and fatigue.  If symptoms persist for more than 60 seconds consult your doctor.",
				new ItemStack(carbonizationItems.miscItem,1,2), new ItemStack[]{new ItemStack(Items.potionitem,1,0), new ItemStack(carbonizationItems.dust,1,5)});
		GuidebookRegistry.instance.addPage(page);


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