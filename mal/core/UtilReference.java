package mal.core;

import cpw.mods.fml.common.registry.GameRegistry;
import mal.api.IFuelContainer;
import mal.carbonization.carbonization;
import mal.carbonization.items.ItemStructureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author Mal
 *
 * A bunch of random methods that I use a lot
 */
public class UtilReference {

	public static boolean compareStacks(ItemStack stack1, ItemStack stack2, boolean useNBT)
	{
		if(stack1==null || stack2==null)//null stacks
			return false;
		if(!stack1.isItemEqual(stack2))//the items are different
			return false;
		
		if(useNBT)
		{
			if((stack1.getTagCompound() == null) && (stack2.getTagCompound()==null))//both don't have nbt data
				return true;
			if((stack1.getTagCompound() == null) || (stack2.getTagCompound()==null))//one doesn't have nbt data
				return false;
			return stack1.getTagCompound().equals(stack2.getTagCompound());//tag compounds are the same
		}
		return true;
	}
	
	/**
	 * For some reason it doesn't like it when value==0.... whatever
	 */
	public static int getItemBurnTime(ItemStack par0ItemStack, int value, boolean useItem)
	{
		if (par0ItemStack == null)
		{
			return 0;
		}
		else if(par0ItemStack.getItem() instanceof IFuelContainer)
        {
        	//get the value
        	long fuelValue = ((IFuelContainer)par0ItemStack.getItem()).getFuelValue(par0ItemStack);
        	
        	//if it's a number, reduce it by some amount, we're using standard coal or the value, whichever is smaller
        	if(fuelValue == 0)
        		return 0;
        	else if(fuelValue >= value)
        	{
        		if(useItem)
        			((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, fuelValue-value, true);
        		return (int) value;
        	}
        	else
        	{
        		if(useItem)
        			((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, 0, true);
        		return (int) fuelValue;
        	}
        }
		else
		{
			int var1 = par0ItemStack.getItem().itemID;
			Item var2 = par0ItemStack.getItem();

			if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
			{
				Block var3 = Block.blocksList[var1];

				if (var3 == Block.woodSingleSlab)
				{
					return 0;
				}

				if (var3.blockMaterial == Material.wood)
				{
					return 0;
				}

				if (var3 == Block.coalBlock)
				{
					return 16000;
				}
			}

			if (var1 == Item.stick.itemID) return 0;
			if (var1 == Item.coal.itemID) return 1600;
			if (var1 == Item.bucketLava.itemID) return 0;
			if (var1 == Block.sapling.blockID) return 0;
			if (var1 == Item.blazeRod.itemID) return 0;
			return GameRegistry.getFuelValue(par0ItemStack);
		}
	}
	
	/*
	 * Helper method to get the tier of a structure block, either average or cond/ins
	 */
	public static double getTier(ItemStack item, boolean useAverage, boolean conduction)
	{
		if(item == null)
			return 0;
		if(!(item.getItem() instanceof 	ItemStructureBlock))
			return 0;
		double[] tier = ItemStructureBlock.getTier(item.getItemDamage());
		if(useAverage)
			return (tier[0]+tier[1])/2;
		if(conduction)
			return tier[1];
		return tier[0];
	}
	
	public static double getTier(ItemStack item)
	{
		return getTier(item, true, false);
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