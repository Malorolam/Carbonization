package mal.carbonization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

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
    private HashMap<String, ItemStack> oreSlagRegistry = new HashMap<String, ItemStack>();
    private Map multiblockSmeltingList = new HashMap();
    
    private HashMap<List<Integer>, Integer> metaMultiblockFuelTime = new HashMap<List<Integer>, Integer>();
    private HashMap<List<Integer>, Integer> metaMultiblockCookTime = new HashMap<List<Integer>, Integer>();
    private HashMap<List<Integer>, String> metaMultiblockOreSlagType = new HashMap<List<Integer>, String>();
    private HashMap<List<Integer>, Boolean> metaMultiblockForceSingle = new HashMap<List<Integer>, Boolean>();
    
    private List<ItemStack> carbonizationInfoList = new ArrayList<ItemStack>();
    private List<Integer> carbonizationIndexList = new ArrayList<Integer>();
    

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
    
    public Map getMultiblockSmeltingList()
    {
    	return multiblockSmeltingList;
    }

    /**
     * Add a multiblock smelting recipe to allow for alternate cook times and fuel needs
     * as well as ore slag return
     * 
     * oreSlagType must be in an accepted string or registered with the recipe handler, ideally in the form
     * forceSingle will force a single output worth of slag, used for things like cobble->stone and sand->glass
     * <descriptor>Slag
     * params:
     * input, output, cookTime, fuelTime, oreSlagType
     */
    public void addMultiblockSmelting(Object input, int cookTime, int fuelTime, String oreSlagType, boolean forceSingle)
    {
    	int itemID;
    	int metadata;
    	if(input instanceof ItemStack)
    	{
    		itemID = ((ItemStack) input).itemID;
    		metadata = ((ItemStack) input).getItemDamage();
            //metaMultiblockSmeltingList.put(Arrays.asList(itemID, metadata), output);
            metaMultiblockFuelTime.put(Arrays.asList(itemID, metadata), fuelTime);
            metaMultiblockCookTime.put(Arrays.asList(itemID, metadata), cookTime);
            metaMultiblockOreSlagType.put(Arrays.asList(itemID, metadata), oreSlagType);
            metaMultiblockForceSingle.put(Arrays.asList(itemID, metadata), forceSingle);
    	}
    	else if(input instanceof String)
    	{
    		for(int i = 0; i < OreDictionary.getOres((String)input).size(); i++)
    		{
    			itemID = OreDictionary.getOres((String)input).get(i).itemID;
    			metadata = OreDictionary.getOres((String)input).get(i).getItemDamage();
    	        //metaMultiblockSmeltingList.put(Arrays.asList(itemID, metadata), output);
    	        metaMultiblockFuelTime.put(Arrays.asList(itemID, metadata), fuelTime);
    	        metaMultiblockCookTime.put(Arrays.asList(itemID, metadata), cookTime);
                metaMultiblockOreSlagType.put(Arrays.asList(itemID, metadata), oreSlagType);
                metaMultiblockForceSingle.put(Arrays.asList(itemID, metadata), forceSingle);
    		}
    	}
    	else
    	{
    		FMLLog.log(Level.SEVERE, "ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
    	}

    }
    public void addMultiblockSmelting(Object input, int cookTime, int fuelTime, String oreSlagType)
    {
    	this.addMultiblockSmelting(input, cookTime, fuelTime, oreSlagType, false);
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
    		FMLLog.log(Level.SEVERE, "ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
    	}

    }
    
    /**
     * Adds a relation of an oreslag and an output
     * This should be logical, not something like chickenSlag->diamonds
     * Amount needed for the output is determined by the furnace, not the recipe
     * 
     * @param oreSlagType
     * @param output
     */
    public boolean addOreSlag(String oreSlagType, Object output)
    {
    	if(output instanceof String)//I'm lazy and want to use ore dictionary stuff
    	{
    		//Just use the first result from the list
    		if(OreDictionary.getOreID((String)output) != -1)
    		{
    			if(OreDictionary.getOres((String)output) == null)
    				FMLLog.log(Level.INFO, "OreDictionary Slag Registration of Type: " + oreSlagType + " Failed: OreDictionary entry " 
    						+ (String)output + " invalid.  Contact Mal or the mod author who added the recipe so they can fix it.");
    			else if(OreDictionary.getOres((String)output).size()<1)
    				FMLLog.log(Level.INFO, "OreDictionary Slag Registration of Type: " + oreSlagType + " Failed: OreDictionary entry " 
    						+ (String)output + " invalid.  Contact Mal or the mod author who added the recipe so they can fix it.");
    			else
    			{
    				this.oreSlagRegistry.put(oreSlagType, OreDictionary.getOres((String)output).get(0));
    				return true;
    			}
    		}
    			
    	}
    	else if(output instanceof ItemStack)
    	{
    		this.oreSlagRegistry.put(oreSlagType, (ItemStack)output);
    		return true;
    	}
    	else
    	{
    		FMLLog.log(Level.INFO, "Ore Slag Registration Failed: Output not in String or ItemStack format.  Contact Mal or the mod author" +
    				" so they can fix it.");
    	}
		return false;
    }
    
    /**
     * Adds an item to a list with an index for information
     * Why is this in recipes?  Because Pandas.
     * @param item The source ItemStack
     * @param index The index
     */
    public boolean addInfoItem(ItemStack item, int index)
    {
    	this.carbonizationInfoList.add(item);
    	this.carbonizationIndexList.add(index);
    	return true;
    }
    
    /**
     * Used to get the index of an item
     * -1 is used for failure
     * @param item
     * @result value
     */
    public int getInfoIndex(ItemStack item)
    {
    	if(item==null)
    		return -1;
    	if(carbonizationInfoList.contains(item))
    	{
    		int index = carbonizationInfoList.indexOf(item);
    		return (int)carbonizationIndexList.get(index);
    	}
    	return -1;
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
    
    public boolean getMultiblockForceSingle(ItemStack item)
    {
    	if(item == null || item.getItem() == null)
    		return false;
    	if(metaMultiblockForceSingle.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
    		return metaMultiblockForceSingle.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	return false;
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
    
    public ItemStack getOreSlagOutput(String oreSlagType)
    {
    	ItemStack ret = null;
    	if(oreSlagRegistry.containsKey(oreSlagType))
    		ret = oreSlagRegistry.get(oreSlagType);
    	return ret;
    }

    public List<ItemStack> getCarbonizationInfoList()
    {
    	return carbonizationInfoList;
    }
    
    public List<Integer> getCarbonizationIndexList()
    {
    	return carbonizationIndexList;
    }
    
    public Map<List<Integer>, ItemStack> getMetaSmeltingList()
    {
        return metaSmeltingList;
    }
    
    public Map<List<Integer>, String> getMultiblockMetaSmeltingList()
    {
    	return this.multiblockSmeltingList;
    }
    
    public Map<String, ItemStack> getOreSlagMap()
    {
    	return this.oreSlagRegistry;
    }
    
    public Map<List<Integer>, ItemStack> generateMultiblockMap()
    {
    	HashMap<List<Integer>, ItemStack> list = new HashMap<List<Integer>, ItemStack>();
    	
    	for(Entry<List<Integer>, String> entry : this.metaMultiblockOreSlagType.entrySet())
    	{
    		String s = entry.getValue();
    		if(this.oreSlagRegistry.containsKey(s))
    		{
    			list.put(entry.getKey(), this.oreSlagRegistry.get(s));
    		}
    	}
    	
    	return list;
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