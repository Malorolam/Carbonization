package mal.carbonization.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Level;

import mal.carbonization.CarbonizationRecipes;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
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
import cpw.mods.fml.common.FMLLog;

public class CarbonizationFurnaceRecipeHandler extends TemplateRecipeHandler{

	public class PSmelting extends CachedRecipe
	{
		PositionedStack ingredient;
		PositionedStack result;
		
        public PSmelting(ItemStack ingred, ItemStack result)
        {
            ingred.stackSize = 1;
            this.ingredient = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
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
		
		public PositionedStack getOtherStack()
		{
			if(fuels.size() > 0)
				return fuels.get((cycleticks/48)%fuels.size()).stack;
			else
				return new PFuel(new ItemStack(Item.coal, 1, 0), TileEntityFurnace.getItemBurnTime(new ItemStack(Item.coal,1,0))).stack;
		}
		
	}
	
	public static class PFuel
	{
		public PFuel(ItemStack item, int burnTime)
		{
			this.stack = new PositionedStack(item, 51, 42, false);
			this.burnTime = burnTime;
		}
		
		public PositionedStack stack;
		public int burnTime;
	}
	
	public static ArrayList<PFuel> fuels;
	public static TreeSet<Integer> tFuels;
	
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
            HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) CarbonizationRecipes.smelting().getSmeltingList();
            HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) CarbonizationRecipes.smelting().getMetaSmeltingList();
            
            for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new PSmelting(new ItemStack(recipe.getKey(), 1, -1), item));
            }
            if(metarecipes == null)return;
            for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
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
        HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) CarbonizationRecipes.smelting().getMetaSmeltingList();
        
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
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}
	
	private static void removeFuels()
	{
        tFuels = new TreeSet<Integer>();
        tFuels.add(Block.mushroomCapBrown.blockID);
        tFuels.add(Block.mushroomCapRed.blockID);
        tFuels.add(Block.signPost.blockID);
        tFuels.add(Block.signWall.blockID);
        tFuels.add(Block.doorWood.blockID);
        tFuels.add(Block.lockedChest.blockID);
	}
	
	private static void findFuels()
	{
        fuels = new ArrayList<PFuel>();
        for(ItemStack item : ItemList.items)
        {
            if(!tFuels.contains(item.itemID))
            {
                int burnTime = TileEntityFurnace.getItemBurnTime(item);
                if(burnTime > 0)
                    fuels.add(new PFuel(item.copy(), burnTime));
            }
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
