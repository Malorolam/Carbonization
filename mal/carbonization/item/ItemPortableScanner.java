package mal.carbonization.item;

import java.util.List;

import mal.core.api.IFuelContainer;
import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import mal.core.util.MalLogger;
import mal.core.util.ScannerUtil;
import mal.core.reference.UtilReference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ItemPortableScanner extends Item {

	public static final int MAXFUEL = 6400;
	
	public ItemPortableScanner() {
		super();
		this.setUnlocalizedName("PortableScanner");
		this.maxStackSize = 1;
		this.setMaxDamage(1);
		this.setCreativeTab(carbonization.tabItems);
	}

	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value, currently doesn't do anything, since there is no metadata
		list.add(setTooltipData("This handheld scanner", ColorReference.LIGHTRED));
		list.add(setTooltipData("allows you to determine", ColorReference.LIGHTRED));
		list.add(setTooltipData("the composition of a volume.", ColorReference.LIGHTRED));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	private static void setTagCompound(ItemStack is, ItemStack[] inventory, int mode, int xSize, int ySize, int zSize, double fuelValue, double fuelMult)
	{	
		NBTTagCompound nbt = new NBTTagCompound();
		
		//inventory
		NBTTagList nbtinventory = new NBTTagList();

		if(inventory != null)
		{
			for (int i = 0; i < 5; ++i)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setInteger("Slot", i);
				if (inventory[i] != null)
				{
					inventory[i].writeToNBT(var4);
				}
				nbtinventory.appendTag(var4);
			}
		}
			
		nbt.setTag("inventory", nbtinventory);
		nbt.setInteger("mode", mode);
		nbt.setInteger("width", xSize);
		nbt.setInteger("height", ySize);
		nbt.setInteger("depth", zSize);
		nbt.setDouble("fuelvalue", fuelValue);
		nbt.setDouble("fuelmultiplier", fuelMult);
		
		is.setTagCompound(nbt);
	}
	
	public static ItemStack[] getInventory(ItemStack is)
	{
		ItemStack[] inventory = new ItemStack[5];
		
		NBTTagList items = is.stackTagCompound.getTagList("inventory", 10);

		for (int i = 0; i < items.tagCount(); ++i)
		{
			NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			if (slot >= 0 && slot < inventory.length)
			{
				ItemStack ii = ItemStack.loadItemStackFromNBT(item);
				inventory[i] = ii;
			}
		}
		
		return inventory;
	}
	
	public static int getMode(ItemStack is)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		
		//System.out.println("Mode get: " + is.stackTagCompound.getInteger("mode"));
		return is.stackTagCompound.getInteger("mode");
	}
	
	public static void setMode(ItemStack is, int mode)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		
		is.stackTagCompound.setInteger("mode", mode);
		//System.out.println("Mode set: "+mode);
	}
	
	public static int[] getSize(ItemStack is)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		
		int[] i = new int[3];
		i[0]=is.stackTagCompound.getInteger("width");
		i[1]=is.stackTagCompound.getInteger("height");
		i[2]=is.stackTagCompound.getInteger("depth");
		return i;
	}
	
	public static double getFuelMultiplier(ItemStack is)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		return is.stackTagCompound.getDouble("fuelmultiplier");
	}
	
	public static int getSizeDim(ItemStack is, int dim)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		
		if(dim==0)
			return is.stackTagCompound.getInteger("width");
		if(dim==1)
			return is.stackTagCompound.getInteger("height");

		if(dim==2)
			return is.stackTagCompound.getInteger("depth");
		return -1;
	}
	
	public static double getFuelValue(ItemStack is)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		double d =is.stackTagCompound.getDouble("fuelvalue"); 
		return d;
	}
	
	public static void setFuelValue(ItemStack is, double fuelvalue)
	{
		if(is.stackTagCompound == null)
			setTagCompound(is,null,0,1,1,1,0,1);
		is.stackTagCompound.setDouble("fuelvalue", fuelvalue);
	}
	
	//do magic things to the inventory to update the other numbers
	//the inventory is just to be lazy
	public static void processItems(ItemStack is, ItemStack[] inventory)
	{
		//size and upgrades
		int width = (inventory[0] != null)?(inventory[0].stackSize*2+1):(1);
		int height = (inventory[1] != null)?(inventory[1].stackSize*2+1):(1);
		int depth = (inventory[2] != null)?(inventory[2].stackSize+1):(1);
		if(width > 5)
			width = 5;
		if(height > 5)
			height = 5;
		if(depth > carbonization.MAXSCANNERDEPTH)
			depth = carbonization.MAXSCANNERDEPTH;
		
		//upgrade stuff
		double xTier = UtilReference.getTier(inventory[0])+1;
		double yTier = UtilReference.getTier(inventory[1])+1;
		double zTier = UtilReference.getTier(inventory[2])+1;
		double avTier = (xTier+yTier+zTier)/3;
		double fuelMult = 1-avTier/8;
		if(fuelMult <= 0)
			fuelMult = 0.01;
		int mode = 0;
		if((inventory[4] != null) &&(inventory[4].getItem() instanceof ItemStructureBlock))
		{
			int[] i = ((ItemStructureBlock)inventory[4].getItem()).deconstructDamage(inventory[4].getItemDamage());
			if(i[0] > 0)
				mode = 1;
		}

		//fuel
		double fuel = getFuelValue(is);
		if(inventory[3]!=null && UtilReference.getItemBurnTime(inventory[3], (int)(MAXFUEL-fuel), false)>0)
		{
			if(inventory[3].getItem() instanceof IFuelContainer)
			{
				fuel += UtilReference.getItemBurnTime(inventory[3], (int)(MAXFUEL-fuel), true);
			}
			else
			{
				if(fuel+UtilReference.getItemBurnTime(inventory[3],1,false)<= MAXFUEL)
				{
					fuel+=UtilReference.getItemBurnTime(inventory[3],1,false);
					inventory[3].stackSize -= 1;
					if(inventory[3].stackSize <= 0)
						inventory[3] = null;
				}
			}
		}
/*		ItemStack[] stack = new ItemStack[3];
		stack[0]=(inventory[3]!=null)?(inventory[3].copy()):(null);
		stack[1]=(inventory[4]!=null)?(inventory[4].copy()):(null);
		stack[2]=(inventory[5]!=null)?(inventory[5].copy()):(null);
		stack = transferItems(stack);
		inventory[3]=stack[0];
		inventory[4]=stack[1];
		inventory[5]=stack[2];*/
		
		setTagCompound(is,inventory,mode,width,height,depth,fuel,fuelMult);
	}
	
	public static double getFuelUsage(ItemStack is)
	{
		double mult = getFuelMultiplier(is);
		double baseUse = carbonization.BASEAUTOCRAFTFUEL/132;
		int volume = getSizeDim(is,0)*getSizeDim(is,1)*getSizeDim(is,2);
		double modeMult = (getMode(is)==0)?(0.1):(0.2);
		return baseUse*mult*volume*modeMult;
	}
	/*
	 * Move empty spaces to the higher slots so the last slot is always filled if possible
	 */
	private static ItemStack[] transferItems(ItemStack[] is)
	{
		for(int i = 2; i>0; i--)
		{
			if(is[i] == null)
			{
				if(is[i-1] != null)
				{
					is[i] = is[i-1].copy();
					is[i-1] = null;
				}
			}
			else if(is[i].stackSize<is[i].getMaxStackSize())
			{
				if(is[i-1]!=null)
				{
					if(is[i].isItemEqual(is[i-1]))
					{
						if(is[i].stackSize+is[i-1].stackSize<=is[i].getMaxStackSize())
						{
							is[i].stackSize += is[i-1].stackSize;
							is[i-1] = null;
						}
						else
						{
							int value = is[i].getMaxStackSize()-is[i].stackSize;
							is[i].stackSize = is[i].getMaxStackSize();
							is[i-1].stackSize -= value;
						}
					}
				}

			}
		}
		return is;
	}
	
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		if(player.isSneaking() && !world.isRemote)
		{
			player.openGui(carbonization.carbonizationInstance, 4, world, 0, 0, 0);
		}
		
		return is;
	}
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz)
	{
		if(!world.isRemote)
		{
			if(player.isSneaking())
				return false;
		
			//do stoof eventually, but remove fuel for now
			double fuelUse = getFuelUsage(is);
			double fuelValue = getFuelValue(is); 
			if(fuelValue < fuelUse)
			{
				MalLogger.addChatMessage(player, "Insufficient Fuel: " + String.format("%.2f",fuelValue) + "/" + String.format("%.2f",fuelUse));
				return false;
			}
			else
			{
				ItemStack[] inv = getInventory(is);
				setFuelValue(is, fuelValue-fuelUse);
				processItems(is,inv);
				int width = getSizeDim(is, 0);
				int height = getSizeDim(is,1);
				int depth = getSizeDim(is,2);
				int mode = getMode(is);
				ScannerUtil.scanWorld(world, player, x, y, z, side, width, height, depth, mode);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		this.itemIcon = ir.registerIcon("carbonization:portableScannerTexture");
	}
	
	@Override
	public boolean getShareTag()
	{
		return true;
	}
	
    public String getItemNameIS(ItemStack itemstack)
	{
		return "Portable Scanner";
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