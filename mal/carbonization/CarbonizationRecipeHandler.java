package mal.carbonization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

import mal.core.util.MalLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class CarbonizationRecipeHandler
{
    private static final CarbonizationRecipeHandler smeltingBase = new CarbonizationRecipeHandler();

    //New Recipies
    //smelting
    private HashMap<ItemStack,ItemStack> smeltingMap = new HashMap<ItemStack,ItemStack>();
    private HashMap<ItemStack,Float> smeltingExperienceMap = new HashMap<ItemStack,Float>();
    private HashMap<ItemStack,Integer> smeltingCookTimeMap = new HashMap<ItemStack,Integer>();
    private HashMap<ItemStack,Integer> smeltingMinTierMap = new HashMap<ItemStack,Integer>();
    
    /** Multiblock smelting maps*/
    private HashMap<String, ItemStack> oreSlagRegistry = new HashMap<String, ItemStack>();    
    private HashMap<ItemStack, Integer> metaMultiblockFuelTime = new HashMap<ItemStack, Integer>();
    private HashMap<ItemStack, Integer> metaMultiblockCookTime = new HashMap<ItemStack, Integer>();
    private HashMap<ItemStack, String> metaMultiblockOreSlagType = new HashMap<ItemStack, String>();
    private HashMap<ItemStack, Boolean> metaMultiblockForceSingle = new HashMap<ItemStack, Boolean>();
    
    private List<ItemStack> carbonizationInfoList = new ArrayList<ItemStack>();
    private List<Integer> carbonizationIndexList = new ArrayList<Integer>();
    
    /** Fuel Conversion Bench maps*/
    /* Because maps are inconsistent in loading, we have to use lists*/
    private List<ItemStack> fuelConversionRegistry = new ArrayList<ItemStack>();//name of the mapping
    private List<Integer> fuelConversionCosts = new ArrayList<Integer>();
    

    /**
     * Used to call methods addSmelting and getSmeltingResult.
     */
    public static final CarbonizationRecipeHandler smelting()
    {
        return smeltingBase;
    }

    private CarbonizationRecipeHandler()
    {

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
    	ItemStack item;
    	int metadata;
    	if(input instanceof ItemStack)
    	{
    		item = (ItemStack)input;
            metaMultiblockFuelTime.put(item, fuelTime);
            metaMultiblockCookTime.put(item, cookTime);
            metaMultiblockOreSlagType.put(item, oreSlagType);
            metaMultiblockForceSingle.put(item, forceSingle);
    	}
    	else if(input instanceof String)
    	{
    		for(int i = 0; i < OreDictionary.getOres((String)input).size(); i++)
    		{
    			item = OreDictionary.getOres((String)input).get(i);
                metaMultiblockFuelTime.put(item, fuelTime);
                metaMultiblockCookTime.put(item, cookTime);
                metaMultiblockOreSlagType.put(item, oreSlagType);
                metaMultiblockForceSingle.put(item, forceSingle);
    		}
    	}
    	else
    	{
    		MalLogger.addLogMessage(Level.ERROR, "ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
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
    	ItemStack item;
    	int metadata;
    	if(input instanceof ItemStack)
    	{
    		item = ((ItemStack) input);
    		smeltingMap.put(item, output);
    		smeltingExperienceMap.put(item, experience);
    		smeltingCookTimeMap.put(item, cookTime);
    		smeltingMinTierMap.put(item, minTier);
    	}
    	else if(input instanceof String)
    	{
    		for(int i = 0; i < OreDictionary.getOres((String)input).size(); i++)
    		{
    			item = OreDictionary.getOres((String)input).get(i);
        		smeltingMap.put(item, output);
        		smeltingExperienceMap.put(item, experience);
        		smeltingCookTimeMap.put(item, cookTime);
        		smeltingMinTierMap.put(item, minTier);
    		}
    	}
    	else
    	{
    		MalLogger.addLogMessage(Level.ERROR, "ERROR LEVEL: DAFUQ...  Tell Mal to fix his recipe handler...");
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
    						+ (String)output + " invalid.  Ensure that the material is installed and if it is contact Mal or the mod author who added the recipe so they can fix it.  ");
    			else if(OreDictionary.getOres((String)output).size()<1)
    				FMLLog.log(Level.INFO, "OreDictionary Slag Registration of Type: " + oreSlagType + " Failed: OreDictionary entry " 
    						+ (String)output + " invalid.  Ensure that the material is installed and if it is contact Mal or the mod author who added the recipe so they can fix it.");
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
    		MalLogger.addLogMessage(Level.INFO, "Ore Slag Registration Failed: Output not in String or ItemStack format.  Contact Mal or the mod author" +
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
     * Adds an output to the fuel conversion bench mapping so it shows up in the correct list
     * 
     * item - the resulting item
     * requiredPotential - how much potential is needed to make the item
     * 
     * returns false if no mapping was added, true if one was
     */
    public boolean addFuelConversionOutput(ItemStack item, int requiredPotential)
    {
    	if(item==null||requiredPotential<=0)//not a recipe, or a recipe that will cause issues
    		return false;
    	
    	//make sure the mapping isn't already here
    	if(fuelConversionRegistry.contains(item))
    	{
    		FMLLog.log(Level.INFO,"Fuel Conversion " + item.getDisplayName() + " already in mapping.");
    		return false;
    	}
    	
    	fuelConversionRegistry.add(item);
    	fuelConversionCosts.add(requiredPotential);
    	
    	return true;
    }
    
    /*
     * yaay oredictionary
     */
    public boolean addFuelConversionOutput(String item, int requiredPotential)
    {
    	//Just use the first result from the list
		if(OreDictionary.getOreID((String)item) != -1)
		{
			if(OreDictionary.getOres((String)item) == null)
				FMLLog.log(Level.INFO, "OreDictionary Fuel Conversion Registration of Type: " + item + " Failed: OreDictionary entry " 
						+ (String)item + " invalid.  Contact Mal or the mod author who added the recipe so they can fix it.  ");
			else if(OreDictionary.getOres((String)item).size()<1)
				FMLLog.log(Level.INFO, "OreDictionary Fuel Conversion Registration of Type: " + item + " Failed: OreDictionary entry " 
						+ (String)item + " invalid.  Contact Mal or the mod author who added the recipe so they can fix it.");
			else
			{
				ItemStack is = OreDictionary.getOres((String)item).get(0);
				return addFuelConversionOutput(is,requiredPotential);
			}
		}
		return false;
    }
    
    public int getFuelConversionOutputCost(ItemStack i)
    {
		int val = fuelConversionRegistry.indexOf(i);
    	if(val > -1)
    	{
    		return fuelConversionCosts.get(val);
    	}
    	return -1;
    }
    
    public List<ItemStack> getFuelConversionRegistry()
    {
    	return fuelConversionRegistry;
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
        Iterator iterator = this.smeltingMap.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }
    
    private boolean areStacksEqual(ItemStack is1, ItemStack is2)
    {
        return is2.getItem() == is1.getItem() && (is2.getItemDamage() == OreDictionary.WILDCARD_VALUE || is1.getItemDamage() == is2.getItemDamage());
    }

    /**
     * Grabs the amount of base experience for this item to give when pulled from the furnace slot.
     */
    public float getExperience(ItemStack item)
    {
    	if (item == null)
        {
            return 0;
        }
        Iterator iterator = this.smeltingExperienceMap.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return 0;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Float)entry.getValue();
    }
    
    /**
     * Get the cook time of an item
     * This number should be the amount of fuel time needed, not the amount of furnace time needed
     */
    public int getCookTime(ItemStack item)
    {
    	if (item == null)
        {
            return -1;
        }
        Iterator iterator = this.smeltingCookTimeMap.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return -1;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Integer)entry.getValue();
    }
    
    public int getMultiblockCookTime(ItemStack item)
    {
    	if (item == null)
        {
            return -1;
        }
        Iterator iterator = this.metaMultiblockCookTime.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return -1;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Integer)entry.getValue();
    	/*if (item == null || item.getItem() == null)
    		return -1;
    	int ret = -1;
    	if(metaMultiblockCookTime.containsKey(item))
    		ret = metaMultiblockCookTime.get(item);
    	return (ret < 0 ? -1 : ret);*/
    }
    
    public boolean getMultiblockForceSingle(ItemStack item)
    {
    	if (item == null)
        {
            return false;
        }
        Iterator iterator = this.metaMultiblockForceSingle.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Boolean)entry.getValue();
    	/*if(item == null || item.getItem() == null)
    		return false;
    	if(metaMultiblockForceSingle.containsKey(item))
    		return metaMultiblockForceSingle.get(item);
    	return false;*/
    }
    
    public int getMinTier(ItemStack item)
    {
    	if (item == null)
        {
            return -1;
        }
        Iterator iterator = this.smeltingMinTierMap.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return -1;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Integer)entry.getValue();
    }
    
    public int getMultiblockFuelTime(ItemStack item)
    {
    	if (item == null)
        {
            return -1;
        }
        Iterator iterator = this.metaMultiblockFuelTime.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return -1;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (Integer)entry.getValue();
    }
    
    public String getMultiblockOreSlagType(ItemStack item)
    {
    	if (item == null)
        {
            return null;
        }
        Iterator iterator = this.metaMultiblockOreSlagType.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.areStacksEqual(item, (ItemStack)entry.getKey()));

        return (String)entry.getValue();
        
        /*
    	if (item == null || item.getItem() == null)
    		return null;
    	String ret = null;
    	if(metaMultiblockOreSlagType.containsKey(item))
    		ret = metaMultiblockOreSlagType.get(item);
    	return ret;*/
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
    
    public Map<ItemStack, ItemStack> getMetaSmeltingList()
    {
        return smeltingMap;
    }
    
    public Map<String, ItemStack> getOreSlagMap()
    {
    	return this.oreSlagRegistry;
    }
    
    public Map<ItemStack, ItemStack> generateMultiblockMap()
    {
    	HashMap<ItemStack, ItemStack> list = new HashMap<ItemStack, ItemStack>();
    	
    	for(Entry<ItemStack, String> entry : this.metaMultiblockOreSlagType.entrySet())
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