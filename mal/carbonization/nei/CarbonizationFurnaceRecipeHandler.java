package mal.carbonization.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Level;

import mal.carbonization.CarbonizationRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.FurnaceRecipeHandler.FuelPair;
import codechicken.nei.recipe.FurnaceRecipeHandler.SmeltingPair;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import cpw.mods.fml.common.FMLLog;

public class CarbonizationFurnaceRecipeHandler extends TemplateRecipeHandler{

	public class SmeltingPair extends CachedRecipe
    {
        public SmeltingPair(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
        }

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
        }

        public PositionedStack getResult() {
            return result;
        }

        public PositionedStack getOtherStack() {
            return afuels.get((cycleticks / 48) % afuels.size()).stack;
        }

        PositionedStack ingred;
        PositionedStack result;
    }

    public static class FuelPair
    {
        public FuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<FuelPair> afuels;
    public static HashSet<Block> fuels;

	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return mal.carbonization.gui.GuiFurnaces.class;
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(56, 36, 14, 14), "fuel"));
		transferRects.add(new RecipeTransferRect(new Rectangle(79, 36, 24, 18), "carbonizationSmelting"));
	}
	
	@Override
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return "Carbonization Furnace";
	}
	
	@Override
	public TemplateRecipeHandler newInstance()
	{
		if(fuels == null)
			findFuels();
		return super.newInstance();
	}

	@Override
	public String getGuiTexture() {
		// TODO Auto-generated method stub
		return new ResourceLocation("carbonization", "textures/gui/furnace.png").toString();
	}
	
	@Override
	public void loadCraftingRecipes(String outputID, Object... results)
	{
        if(outputID.equals("carbonizationSmelting") && getClass() == CarbonizationFurnaceRecipeHandler.class)
        {
            HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().getMetaSmeltingList();
            
            for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new SmeltingPair(recipe.getKey(), item));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputID, results);
        }
    }
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
        HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().getMetaSmeltingList();
        
        for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new SmeltingPair(recipe.getKey(), item));
            }
        }
    }
	
	@Override
	public void loadUsageRecipes(String inputID, Object... ingredients)
	{
		if(inputID.equals("fuel") && getClass() == CarbonizationFurnaceRecipeHandler.class)
		{
			loadCraftingRecipes("carbonizationSmelting");
		}
		else
		{
			super.loadUsageRecipes(inputID, ingredients);
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
        HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().getMetaSmeltingList();
        
        for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(recipe.getKey(), ingredient))
            {
                arecipes.add(new SmeltingPair(ingredient, item));
            }
        }
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}
	
	private static Set<Item> removeFuels()
	{
        Set<Item> efuels = new HashSet<Item>();
        efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        return efuels;
	}
	
	private static void findFuels()
	{
		afuels = new ArrayList<FuelPair>();
		Set<Item> efuels = removeFuels();
		for (ItemStack item : ItemList.items)
			if (!efuels.contains(item.getItem())) {
				int burnTime = TileEntityFurnace.getItemBurnTime(item);
				if (burnTime > 0)
					afuels.add(new FuelPair(item.copy(), burnTime));
			}
	}
	
    @Override
    public String getOverlayIdentifier()
    {
        return "carbonizationSmelting";
    }
    
    static
    {
        removeFuels();
    }
}
