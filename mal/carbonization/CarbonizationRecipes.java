package mal.carbonization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class CarbonizationRecipes
{
    private static final CarbonizationRecipes smeltingBase = new CarbonizationRecipes();

    /** The list of smelting results. */
    private Map smeltingList = new HashMap();
    private Map experienceList = new HashMap();
    private Map cookTimeList = new HashMap();
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<List<Integer>, ItemStack>();
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>();
    private HashMap<List<Integer>, Integer> metaCookTime = new HashMap<List<Integer>, Integer>();

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
     * Add a special smelting recipe with alternate cook time
     */
    public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience, int cookTime)
    {
        metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
        metaExperience.put(Arrays.asList(itemID, metadata), experience);
        metaCookTime.put(Arrays.asList(itemID, metadata), cookTime);
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

    public Map<List<Integer>, ItemStack> getMetaSmeltingList()
    {
        return metaSmeltingList;
    }
}