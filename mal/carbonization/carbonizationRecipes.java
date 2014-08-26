package mal.carbonization;

import cpw.mods.fml.common.registry.GameData;
import mal.core.guidebook.GuidebookPage;
import mal.core.guidebook.GuidebookRegistry;
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
		
		//transport blocks
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.transportBlock,8,0), new Object[]{"SGS", "GCG", "SGS", 'S', new ItemStack(carbonizationItems.structureItem,1,4), 'G', "blockGlass", 'C', new ItemStack(carbonizationItems.miscItem,1,17)}));
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
		
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,23), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,23), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,24), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,24), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,25), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,25), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,26), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,26), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,27), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4));
		addUpgradeRecipe(new ItemStack(carbonizationItems.upgradeItem,1,27), new ItemStack(Blocks.hopper), new ItemStack(carbonizationItems.structureItem,1,2005), new ItemStack(carbonizationItems.structureItem,1,5));
		
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
		//CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,14), new Object[]{"GGG", 'G', new ItemStack(carbonizationItems.miscItem,1,12)}));

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
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.miscItem,2,12), new Object[]{new ItemStack(carbonizationItems.dust,1,4)}));
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.dust,1,4), new Object[]{new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12), new ItemStack(carbonizationItems.miscItem,1,12)}));

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.blockFuelBlock,1,0), new Object[]{"xxx","xxx","xxx",'x',"fuelPeat"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.blockFuelBlock,1,1), new Object[]{"xxx","xxx","xxx",'x',"fuelAnthracite"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationBlocks.blockFuelBlock,1,2), new Object[]{"xxx","xxx","xxx",'x',"fuelGraphite"}));

		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,0), new Object[]{new ItemStack(carbonizationBlocks.blockFuelBlock,1,0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,1), new Object[]{new ItemStack(carbonizationBlocks.blockFuelBlock,1,1)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(carbonizationItems.fuel,9,2), new Object[]{new ItemStack(carbonizationBlocks.blockFuelBlock,1,2)}));

		//bit
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,15), new Object[]{" D ", "DSD", " S ", 'D', Items.diamond, 'S', new ItemStack(carbonizationItems.structureItem,1,4)}));
		//casing
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,16), new Object[]{"SSS", "S S", "SSS", 'S', new ItemStack(carbonizationItems.structureItem,1,4)}));
		//circuit
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(carbonizationItems.miscItem,1,17), new Object[]{"RGR", "LEL", "RGR", 'R', Items.redstone, 'G', Items.gold_ingot, 'L', "dyeBlue", 'E', Items.ender_pearl}));
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
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("blockGraphite", 9000);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("blockPeat", 10800);
		CarbonizationRecipeHandler.smelting().addFuelConversionOutput("blockAnthracite", 21600);
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
				CarbonizationRecipeHandler.smelting().addOreSlag("platinumSlag", "ingotPlatinum");
				CarbonizationRecipeHandler.smelting().addOreSlag("osmiumSlag", "ingotOsmium");
				CarbonizationRecipeHandler.smelting().addOreSlag("tungstenSlag", "ingotElnTungsten");
				CarbonizationRecipeHandler.smelting().addOreSlag("arditeSlag", "ingotArdite");
				CarbonizationRecipeHandler.smelting().addOreSlag("cobaltSlag", "ingotCobalt");
				//boolean b = CarbonizationRecipeHandler.smelting().addOreSlag("aluminumSlag", "ingotNaturalAluminum");
				//if(!b)
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
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("orePlatinum", 400, 200, "platinumSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreOsmium", 400, 200, "osmiumSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreElnTungsten", 400, 200, "tungstenSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreArdite", 400, 200, "arditeSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreCobalt", 400, 200, "cobaltSlag", false);
				CarbonizationRecipeHandler.smelting().addMultiblockSmelting("oreAluminum", 400, 200, "aluminumSlag", false);
				
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
									if(is != null && ((Block)bb).damageDropped(j) == j && !UtilReference.areItemStacksEqualItem(is, new ItemStack(Blocks.fire), true, true))
									{
										CarbonizationRecipeHandler.smelting().addMultiblockSmelting(is, 100, 100, "ashSlag");
									}
								}
							}
							else
							{
								ItemStack is = new ItemStack((Block)bb);
								if(is != null && is.getItem() != null && !UtilReference.areItemStacksEqualItem(is, new ItemStack(Blocks.fire), false, true))
									CarbonizationRecipeHandler.smelting().addMultiblockSmelting(is, 100, 100, "ashSlag");
							}
							//System.out.println("Added recipe for block: " + ((Block)bb).getLocalizedName());
						}
					}
				}

	}

	public static void guidebookRecipes()
	{
		GuidebookPage page = new GuidebookPage("mal.page.basicinfo.title", "mal.page.basicinfo.lower");
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.worldgen.title", "mal.page.worldgen.lower",
				new ItemStack(carbonizationBlocks.fuelBlock,1,2), null);
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.basicsteel.title", "mal.page.basicsteel.lower",
				new ItemStack(carbonizationItems.ingots,1,3), null);
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.singleblockfurnaces.title", "mal.page.singleblockfurnaces.lower",
				new ItemStack(carbonizationBlocks.furnaceBlock, 1, 1), new ItemStack[]{new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(carbonizationBlocks.furnaceBlock,1,0),new ItemStack(carbonizationItems.ingots,1,0),new ItemStack(Blocks.brick_block),new ItemStack(Blocks.brick_block),new ItemStack(Blocks.brick_block)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.structureblock.title", "mal.page.structureblock.lower",
				new ItemStack(carbonizationItems.structureItem, 1, 4), new ItemStack[]{new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2),null,new ItemStack(carbonizationItems.ingots,1,2)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.furnacecontrol.title", "mal.page.furnacecontrol.lower",
				new ItemStack(carbonizationBlocks.furnaceControlBlock,1,0), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationBlocks.furnaceBlock,1,2), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3), new ItemStack(carbonizationItems.structureItem,1,3)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.autocraftingbench.title", "mal.page.autocraftingbench.lower",
				new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,0), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,4),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(Blocks.crafting_table),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(carbonizationItems.structureItem,1,2004),new ItemStack(carbonizationBlocks.furnaceBlock,1,1),new ItemStack(carbonizationItems.structureItem,1,2004)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.fuelconversionbench.title", "mal.page.fuelconversionbench.lower",
				new ItemStack(carbonizationBlocks.autocraftingbenchBlock,1,1), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.structureItem,1,2),new ItemStack(carbonizationItems.hhcomp), new ItemStack(carbonizationItems.structureItem,1,2002),new ItemStack(carbonizationItems.hhpulv),new ItemStack(carbonizationItems.structureItem,1,2002),new ItemStack(carbonizationBlocks.furnaceBlock,1,1),new ItemStack(carbonizationItems.structureItem,1,2002)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.tunnelbore.title", "mal.page.tunnelbore.lower",
				new ItemStack(carbonizationBlocks.tunnelboreBlock), new ItemStack[]{new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,15),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,18),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationItems.miscItem,1,16),new ItemStack(carbonizationBlocks.furnaceBlock,1,2),new ItemStack(carbonizationItems.miscItem,1,16)});
		GuidebookRegistry.instance.addPage(page);
		
		page = new GuidebookPage("mal.page.fluidtransport.title", "mal.page.fluidtransport.lower", new ItemStack(carbonizationBlocks.transportBlock,1,0),
				new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(Blocks.glass), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(Blocks.glass), new ItemStack(carbonizationItems.miscItem,1,17), new ItemStack(Blocks.glass), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(Blocks.glass), new ItemStack(carbonizationItems.structureItem,1,4)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.upgrades.title", "mal.page.upgrades.lower",
				new ItemStack(carbonizationItems.upgradeItem,1,3), new ItemStack[]{new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(Items.glowstone_dust), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,4), new ItemStack(carbonizationItems.structureItem,1,2004), new ItemStack(carbonizationItems.structureItem,1,4)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.scanner.title", "mal.page.scanner.lower",
				new ItemStack(carbonizationItems.portablescannerItem), new ItemStack[]{new ItemStack(Items.leather), new ItemStack(Items.dye,1,4), new ItemStack(Items.leather), new ItemStack(carbonizationItems.miscItem,1,11), new ItemStack(Items.diamond), new ItemStack(carbonizationItems.miscItem,1,11), new ItemStack(Items.gold_ingot), new ItemStack(carbonizationBlocks.furnaceBlock,1,2), new ItemStack(Items.gold_ingot)});
		GuidebookRegistry.instance.addPage(page);

		page = new GuidebookPage("mal.page.cleansingpot.title", "mal.page.cleansingpot.lower", "mal.page.cleansingpot.upper",
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