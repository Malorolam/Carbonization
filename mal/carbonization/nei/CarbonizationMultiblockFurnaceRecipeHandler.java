package mal.carbonization.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.nei.CarbonizationFurnaceRecipeHandler.PFuel;
import mal.carbonization.nei.CarbonizationFurnaceRecipeHandler.PSmelting;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;

public class CarbonizationMultiblockFurnaceRecipeHandler extends TemplateRecipeHandler
{
	public class PSmelting extends CachedRecipe
	{
		PositionedStack ingredient;
		PositionedStack result;
		
        public PSmelting(ItemStack ingred, ItemStack result)
        {
            ingred.stackSize = 1;
            this.ingredient = new PositionedStack(ingred, 22, 27);
            this.result = new PositionedStack(result, 128, 26);
        }
        
		@Override
		public PositionedStack getResult() {
			
			return result;
		}
		
		public PositionedStack getIngredient()
		{
			int count = cycleticks/48;
			if(ingredient.item.getItemDamage() == -1)
			{
				PositionedStack stack = ingredient.copy();
				int maxDamage = 0;
				do
				{
					maxDamage += 1;
					stack.item.setItemDamage(maxDamage);
				}
				while(NEIClientUtils.isValidItem(stack.item));
				
				stack.item.setItemDamage(count % maxDamage);
				return stack;
			}
			return ingredient;
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
		return "carbonization:/textures/gui/multiblockNEI.png";
	}
	
	@Override
	public void loadCraftingRecipes(String outputID, Object... results)
	{
        if(outputID.equals("carbonizationMultiblockSmelting") && getClass() == CarbonizationMultiblockFurnaceRecipeHandler.class)
        {
            HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) CarbonizationRecipes.smelting().getMultiblockSmeltingList();
            HashMap<List<Integer>, ItemStack> slagMap = (HashMap<List<Integer>, ItemStack>) CarbonizationRecipes.smelting().generateMultiblockMap();
            
            for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new PSmelting(new ItemStack(recipe.getKey(), 1, -1), item));
            }
            if(slagMap == null)return;
            for(Entry<List<Integer>, ItemStack> recipe : slagMap.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new PSmelting(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
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
        HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) CarbonizationRecipes.smelting().getSmeltingList();
        HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) CarbonizationRecipes.smelting().generateMultiblockMap();
        
        for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new PSmelting(new ItemStack(recipe.getKey(), 1, -1), item));
            }
        }
        if(metarecipes == null)return;
        for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new PSmelting(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
            }
        }
    }
	
	@Override
	public void loadUsageRecipes(String inputID, Object... ingredients)
	{
		if(inputID.equals("fuel") && getClass() == CarbonizationMultiblockFurnaceRecipeHandler.class)
		{
			loadCraftingRecipes("carbonizationMultiblock");
		}
		else
		{
			super.loadUsageRecipes(inputID, ingredients);
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) CarbonizationRecipes.smelting().getSmeltingList();
        HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) CarbonizationRecipes.smelting().getMetaSmeltingList();
        
        for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(ingredient.itemID == recipe.getKey())
            {
                arecipes.add(new PSmelting(ingredient, item));
            }
        }
        if(metarecipes == null)return;
        for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(ingredient.itemID == recipe.getKey().get(0) && ingredient.getItemDamage() == recipe.getKey().get(1))
            {
                arecipes.add(new PSmelting(ingredient, item));
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
