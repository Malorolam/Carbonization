package mal.carbonization.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.api.IFuelContainer;
import mal.carbonization.carbonization;
import mal.core.ColorReference;
import mal.core.FormatReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemFuelCell extends Item implements IFuelContainer{

	private Icon[] iconArray = new Icon[2];
	
	public ItemFuelCell(int par1) {
		super(par1);
		this.setUnlocalizedName("FuelCell");
		this.maxStackSize = 1;
		this.setMaxDamage(1);
		this.setCreativeTab(carbonization.tabItems);
	}
	
	/*
	 * Set the contents of the NBTTag in a specific itemstack
	 */
	public boolean setFuel(ItemStack is, long fuelValue, boolean absolute)
	{
		if(is.stackTagCompound == null)
		{
			is.stackTagCompound = new NBTTagCompound();
		}
		
		//bit of a check to see if we are reducing the value and if it's going to be less than 0, because that's bad mmKay
		boolean flag = (fuelValue<0)?(fuelValue>is.stackTagCompound.getLong("FuelTime")):(false);
		if(flag)
		{
			is.stackTagCompound.setLong("FuelTime", 0);
			return true;
		}
		else if(absolute)
		{
			is.stackTagCompound.setLong("FuelTime", fuelValue);
			return true;
		}
		else if(is.stackTagCompound.getLong("FuelTime") < 0)
		{
			is.stackTagCompound.setLong("FuelTime", 0);
			return true;
		}
		else
		{
			is.stackTagCompound.setLong("FuelTime", is.stackTagCompound.getLong("FuelTime")+fuelValue);
			return true;
		}
	}

	/*
	 * Get the amount of fuel stored
	 */
	public long getFuelValue(ItemStack is)
	{
		if(is.stackTagCompound==null)
			setFuel(is,0,true);
		return is.stackTagCompound.getLong("FuelTime");
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:fuelCellEmptyTexture");
		iconArray[1] = ir.registerIcon("carbonization:fuelCellTexture");
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("A capsule of processed fuel.", ColorReference.DARKGREY));
		list.add(setTooltipData("Stored Fuel Time: " + ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))?(getFuelValue(is)):(FormatReference.compressLong(getFuelValue(is)))), ColorReference.DARKGREY));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
			
		return colorValue+data;
	}
	
	@Override
	public void onCreated(ItemStack is, World world, EntityPlayer player)
	{
		setFuel(is,0,true);
	}
	
	@Override
	public Icon getIconIndex(ItemStack is)
	{
		if(is.stackTagCompound==null)
			setFuel(is,0,true);
		
		if(is.stackTagCompound.getLong("FuelTime")==0)
			return iconArray[0];
		else
			return iconArray[1];
	}
	
	@Override
	public Icon getIcon(ItemStack is, int pass)
	{
		if(is.stackTagCompound==null)
			setFuel(is,0,true);
		
		if(is.stackTagCompound.getLong("FuelTime")==0)
			return iconArray[0];
		else
			return iconArray[1];
	}
	
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		return "carbonization.fuelcell";
	}
	
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		ItemStack item = new ItemStack(par1,1,0);
        setFuel(item,0,true);
        
        par3List.add(item);
        
		item = new ItemStack(par1,1,0);
		setFuel(item,Integer.MAX_VALUE,true);
        par3List.add(item);
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