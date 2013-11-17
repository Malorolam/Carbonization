package mal.carbonization.nei;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import mal.carbonization.CarbonizationInfo;
import mal.carbonization.CarbonizationRecipes;
import mal.carbonization.nei.CarbonizationMultiblockFurnaceRecipeHandler.PSmelting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;

import static codechicken.core.gui.GuiDraw.*;

public class CarbonizationItemInformationHandler extends TemplateRecipeHandler{
	
	public class PSmelting extends CachedRecipe
	{
		PositionedStack ingredient;
		PositionedStack result;
		
        public PSmelting(ItemStack ingred, int result)
        {
            ingred.stackSize = 1;
            this.ingredient = new PositionedStack(ingred, -200, -200);
            this.result = new PositionedStack(new ItemStack(result,1,-1),-200,-200);
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
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return "Carbonization Info";
	}

	@Override
	public String getGuiTexture() {
		// TODO Auto-generated method stub
		return new ResourceLocation("carbonization", "textures/gui/blank.png").toString();
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return mal.carbonization.gui.GuiCarbonizationInfo.class;
	}
	
	@Override
	public void loadTransferRects()
	{
		//transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
		//transferRects.add(new RecipeTransferRect(new Rectangle(75, 34, 27, 19), "carbonizationInformation"));
	}
	
	@Override
	public TemplateRecipeHandler newInstance()
	{
		return super.newInstance();
	}
	
	@Override
	public int recipiesPerPage()
	{
		return 1;
	}
	
	@Override
	public void loadCraftingRecipes(String outputID, Object... results)
	{
        if(outputID.equals("carbonizationInformation") && getClass() == CarbonizationItemInformationHandler.class)
        {
            List<ItemStack> recipe = CarbonizationRecipes.smelting().getCarbonizationInfoList();
            List<Integer> indices = CarbonizationRecipes.smelting().getCarbonizationIndexList();
            
            for(int i = 0; i < recipe.size(); i++)
            {
                int item = indices.get(i);
                arecipes.add(new PSmelting(recipe.get(i), item));
                //System.out.println("Added recipe index: "+ item);
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
        List<ItemStack> recipes = CarbonizationRecipes.smelting().getCarbonizationInfoList();
        List<Integer> indices = CarbonizationRecipes.smelting().getCarbonizationIndexList();
        
        for(int i = 0; i < recipes.size(); i++)
        {
            int index = indices.get(i);
            if(areStacksSame(recipes.get(i), result))
            {
                arecipes.add(new PSmelting(recipes.get(i), index));
                //System.out.println("Add recipe index: "+ index);
            }
        }
    }
	
	private boolean areStacksSame(ItemStack stack1, ItemStack stack2)
	{
		if(stack1 == null || stack2 == null)
			return stack1==stack2;
		
		if(stack1.itemID != stack2.itemID)
			return false;
		
		if(stack1.isItemStackDamageable() && stack2.isItemStackDamageable())
			return true;
		
		if(stack1.getItemDamage() == stack2.getItemDamage())
			return true;
		return false;
	}

	@Override
	public void drawExtras(int recipe)
	{
		//drawProgressBar(72, 26, 176, 0, 24, 17, 48, 0);
		
		int index = arecipes.get(recipe).getResult().item.itemID;
		List<String> list = CarbonizationInfo.getInfo(index);
		
		//System.out.println("draw recipe index: " + index);
		
		GL11.glScalef(0.75f, 0.75f, 0.75f);
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) != null)
				drawString(list.get(i), 1, 2+11*i, 4210752, false);
		}
	}
	
    @Override
    public String getOverlayIdentifier()
    {
        return "carbonizationInformation";
    }
}
