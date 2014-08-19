package mal.carbonization.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

import mal.carbonization.CarbonizationRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;

public class CarbonizationMultiblockFurnaceRecipeHandler extends TemplateRecipeHandler
{
	public class smeltingPair extends CachedRecipe
	{
		PositionedStack ingredient;
		PositionedStack result;
		public smeltingPair(ItemStack ingredient, ItemStack result)
		{
			ingredient.stackSize = 1;
			this.ingredient = new PositionedStack(ingredient, 22, 27);
			this.result = new PositionedStack(result,128,26);
			
		}

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(ingredient));
        }

        public PositionedStack getResult() {
            return result;
        }
		
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return mal.carbonization.gui.GuiNEIMultiblockFurnace.class;
	}
	
	@Override
	public void loadTransferRects()
	{
		//transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
		transferRects.add(new RecipeTransferRect(new Rectangle(75, 34, 27, 19), "carbonizationMultiblockSmelting"));
	}
	
	@Override
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return "Industrial Furnace";
	}
	
	@Override
	public TemplateRecipeHandler newInstance()
	{
		return super.newInstance();
	}

	@Override
	public String getGuiTexture() {
		// TODO Auto-generated method stub
		return new ResourceLocation("carbonization", "textures/gui/multiblockNEI.png").toString();
	}
	
	@Override
	public void loadCraftingRecipes(String outputID, Object... results)
	{
        if(outputID.equals("carbonizationMultiblockSmelting") && getClass() == CarbonizationMultiblockFurnaceRecipeHandler.class)
        {
            HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().generateMultiblockMap();
            
            for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            {
                arecipes.add(new smeltingPair(recipe.getKey(), recipe.getValue()));
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
        HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().generateMultiblockMap();
        
        for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new smeltingPair(recipe.getKey(), item));
            }
        }
    }
	
	@Override
	public void loadUsageRecipes(String inputID, Object... ingredients)
	{
		if(inputID.equals("fuel") && getClass() == CarbonizationMultiblockFurnaceRecipeHandler.class)
		{
			loadCraftingRecipes("carbonizationMultiblockRecipes");
		}
		else
		{
			super.loadUsageRecipes(inputID, ingredients);
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
        HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>) CarbonizationRecipeHandler.smelting().generateMultiblockMap();
        
        for(Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(recipe.getKey(), ingredient))
            {
                arecipes.add(new smeltingPair(ingredient, item));
            }
        }
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(72, 26, 176, 0, 24, 17, 48, 0);
        //drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}
	
    @Override
    public String getOverlayIdentifier()
    {
        return "carbonizationMultiblockSmelting";
    }
}
