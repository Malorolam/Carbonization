package mal.carbonization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class CarbonizationRecipes
{
    private static final CarbonizationRecipes smeltingBase = new CarbonizationRecipes();

    /** The list of smelting results. */
    private Map smeltingList = new HashMap();
    private Map experienceList = new HashMap();
    private Map cookTimeList = new HashMap();
    private Map minTierList = new HashMap();
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<List<Integer>, ItemStack>();
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>();
    private HashMap<List<Integer>, Integer> metaCookTime = new HashMap<List<Integer>, Integer>();
    private HashMap<List<Integer>, Integer> metaMinTier = new HashMap<List<Integer>, Integer>();
    
    /** Multiblock smelting maps*/
    private HashMap<List<Integer>, ItemStack> metaMultiblockSmeltingList = new HashMap<List<Integer>, ItemStack>();
    private HashMap<List<Integer>, Integer> metaMultiblockFuelTime = new HashMap<List<Integer>, Integer>();
    private HashMap<List<Integer>, Integer> metaMultiblockCookTime = new HashMap<List<Integer>, Integer>();
    private HashMap<List<Integer>, String> metaMultiblockOreSlagType = new HashMap<List<Integer>, String>();
    

    /**
     * Used to call methods addSmelting and getSmeltingResult.
     */
    public static final CarbonizationRecipes smelting()
    {
        return smeltingBase;
    }

    private CarbonizationRecipes()
    {

    }
    
    public Map getSmeltingList()
    {
        return this.smeltingList;
    }

    /**
     * Add a multiblock smelting recipe to allow for alternate cook times and fuel needs
     * as well as ore slag return
     * params:
     * input, output, cookTime, fuelTime, oreSlagType
     */
    public void addMultiblockSmelting(Object input, ItemStack output, int cookTime, int fuelTime, String oreSlagType)
    {
    	int itemID;
    	int metadata;
    	if(input instanceof ItemStack)
    	{
    		itemID = ((ItemStack) input).itemID;
    		metadata = ((ItemStack) input).getItemDamage();
            metaMultiblockSmeltingList.put(Arrays.asList(itemID, metadata), output);
            metaMultiblockFuelTime.put(Arrays.asList(itemID, metadata), fuelTime);
            metaMultiblockCookTime.put(Arrays.asList(itemID, metadata), cookTime);
            metaMultiblockOreSlagType.put(Arrays.asList(itemID, metadata), oreSlagType);
    	}
    	else if(input instanceof String)
    	{
    		for(int i = 0; i < OreDictionary.getOres((String)input).size(); i++)
    		{
    			itemID = OreDictionary.getOres((String)input).get(i).itemID;
    			metadata = OreDictionary.getOres((String)input).get(i).getItemDamage();
    	        metaMultiblockSmeltingList.put(Arrays.asList(itemID, metadata), output);
    	        metaMultiblockFuelTime.put(Arrays.asList(itemID, metadata), fuelTime);
    	        metaMultiblockCookTime.put(Arrays.asList(itemID, metadata), cookTime);
                metaMultiblockOreSlagType.put(Arrays.asList(itemID, metadata), oreSlagType);
    		}
    	}
    	else
    	{
    		System.err.println("ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
    	}

    }
    
    /**
     * Add a special smelting recipe with alternate cook time
     * params:
     * input, output, experience, cookTime, minTier
     * input may be an ItemStack or a String for use with the OreDictionary
     * cookTime is how long the recipe takes to complete in ticks
     * minTier is the minimum tier of machine that will accept the recipe, where a value of 0 will work with all machines
     */
    public void addSmelting(Object input, ItemStack output, float experience, int cookTime, int minTier)
    {
    	int itemID;
    	int metadata;
    	if(input instanceof ItemStack)
    	{
    		itemID = ((ItemStack) input).itemID;
    		metadata = ((ItemStack) input).getItemDamage();
            metaSmeltingList.put(Arrays.asList(itemID, metadata), output);
            metaExperience.put(Arrays.asList(itemID, metadata), experience);
            metaCookTime.put(Arrays.asList(itemID, metadata), cookTime);
            metaMinTier.put(Arrays.asList(itemID,metadata), minTier);
    	}
    	else if(input instanceof String)
    	{
    		for(int i = 0; i < OreDictionary.getOres((String)input).size(); i++)
    		{
    			itemID = OreDictionary.getOres((String)input).get(i).itemID;
    			metadata = OreDictionary.getOres((String)input).get(i).getItemDamage();
    	        metaSmeltingList.put(Arrays.asList(itemID, metadata), output);
    	        metaExperience.put(Arrays.asList(itemID, metadata), experience);
    	        metaCookTime.put(Arrays.asList(itemID, metadata), cookTime);
    	        metaMinTier.put(Arrays.asList(itemID,metadata), minTier);
    		}
    	}
    	else
    	{
    		System.err.println("ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
    	}

    }

    /**
     * Used to get the resulting ItemStack form a source ItemStack
     * @param item The Source ItemStack
     * @return The result ItemStack
     */
    public ItemStack getSmeltingResult(ItemStack item) 
    {
        if (item == null)
        {
            return null;
        }
        ItemStack ret = (ItemStack)metaSmeltingList.get(Arrays.asList(item.itemID, item.getItemDamage()));
        if (ret != null) 
        {
            return ret;
        }
        return (ItemStack)smeltingList.get(Integer.valueOf(item.itemID));
    }
    
    public ItemStack getMultiblockSmeltingResult(ItemStack item)
    {
    	if (item == null)
    		return null;
    	ItemStack ret = (ItemStack)metaMultiblockSmeltingList.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	return ret;
    }

    /**
     * Grabs the amount of base experience for this item to give when pulled from the furnace slot.
     */
    public float getExperience(ItemStack item)
    {
        if (item == null || item.getItem() == null)
        {
            return 0;
        }
        float ret = item.getItem().getSmeltingExperience(item);
        if (ret < 0 && metaExperience.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
        {
            ret = metaExperience.get(Arrays.asList(item.itemID, item.getItemDamage()));
        }
        if (ret < 0 && experienceList.containsKey(item.itemID))
        {
            ret = ((Float)experienceList.get(item.itemID)).floatValue();
        }
        return (ret < 0 ? 0 : ret);
    }
    
    /**
     * Get the cook time of an item
     * This number should be the amount of fuel time needed, not the amount of furnace time needed
     */
    public int getCookTime(ItemStack item)
    {
    	if (item == null || item.getItem() == null)
        {
            return -1;
        }
        int ret = -1;
        if (metaCookTime.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
        {
            ret = metaCookTime.get(Arrays.asList(item.itemID, item.getItemDamage()));
        }
        if (cookTimeList.containsKey(item.itemID))
        {
            ret = ((Integer)cookTimeList.get(item.itemID)).intValue();
        }
        return (ret < 0 ? -1 : ret);
    }
    
    public int getMultiblockCookTime(ItemStack item)
    {
    	if (item == null || item.getItem() == null)
    		return -1;
    	int ret = -1;
    	if(metaMultiblockCookTime.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
    		ret = metaMultiblockCookTime.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	return (ret < 0 ? -1 : ret);
    }
    
    public int getMinTier(ItemStack item)
    {
    	if(item == null || item.getItem() == null)
    	{
    		return -1;
    	}
    	int ret = -1;
    	if (metaMinTier.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
    	{
    		ret = metaMinTier.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	}
    	if (minTierList.containsKey(item.itemID))
    	{
    		ret = ((Integer)minTierList.get(item.itemID)).intValue();
    	}
    	return (ret < 0?-1:ret);
    }
    
    public int getMultiblockFuelTime(ItemStack item)
    {
    	if (item == null || item.getItem() == null)
    		return -1;
    	int ret = -1;
    	if(metaMultiblockFuelTime.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
    		ret = metaMultiblockFuelTime.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	return (ret < 0 ? -1 : ret);
    }
    
    public String getMultiblockOreSlagType(ItemStack item)
    {
    	if (item == null || item.getItem() == null)
    		return null;
    	String ret = null;
    	if(metaMultiblockOreSlagType.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
    		ret = metaMultiblockOreSlagType.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	return ret;
    }

    public Map<List<Integer>, ItemStack> getMetaSmeltingList()
    {
        return metaSmeltingList;
    }
}
/*******************************************************************************
* Copyright (c) 2013 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
* 
* 
*********************************************************************************/