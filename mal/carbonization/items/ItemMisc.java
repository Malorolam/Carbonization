package mal.carbonization.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.ColorReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemMisc extends Item{

	private Icon[] iconArray = new Icon[20];
	
	public ItemMisc(int par1) {
		super(par1);
		this.hasSubtypes = true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemMisc");
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://pencil
			list.add(setTooltipData("A pencil.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Maybe write with it?", ColorReference.ORANGE));
			break;
		case 1://cleansing potion
			list.add(setTooltipData("Fine carbon particles cleanse", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and purge toxins for your health.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Made from 100% charcoal.", ColorReference.DARKCYAN));
			break;
		case 2://"cleansing" potion
			list.add(setTooltipData("Fine carbon particles cleanse", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and purge toxins for your health.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Made from 100% coal.", ColorReference.DARKCYAN));
			break;
		case 3://carbon chunk
			list.add(setTooltipData("A partially compressed chunk of", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("pure carbon.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Perhaps I should compress it further", ColorReference.ORANGE));
			list.add(setTooltipData("in case it becomes useful?", ColorReference.ORANGE));
			break;
		case 4://glass insulation
			list.add(setTooltipData("Glass fibre suspended in clay filler.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Useful for insulating.", ColorReference.LIGHTGREEN));
			break;
		case 5://high density insulation
			list.add(setTooltipData("Graphite fibre suspended in sand filler.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Useful for insulating at high temperatures.", ColorReference.LIGHTGREEN));
			break;
		case 6://ash
			list.add(setTooltipData("It's what happens when you put flammable", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("items in an industrial furnace.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Good Job...", ColorReference.ORANGE));
			break;
		case 7://iron gear
			list.add(setTooltipData("It's a few gears made of iron.", ColorReference.LIGHTGREEN));
			break;
		case 8://refined iron gear
			list.add(setTooltipData("It's a few gears made of refined iron.", ColorReference.LIGHTGREEN));
			break;
		case 9://pig iron gear
			list.add(setTooltipData("It's a few gears made of pig iron.", ColorReference.LIGHTGREEN));
			break;
		case 10://mild steel gear
			list.add(setTooltipData("It's a few gears made of a high carbon steel.", ColorReference.LIGHTGREEN));
			break;
		case 11://steel gear
			list.add(setTooltipData("It's a few gears made of medium carbon steel.", ColorReference.LIGHTGREEN));
			break;
		case 12://small graphite dust pile
			list.add(setTooltipData("It's a small pile of graphite dust.", ColorReference.LIGHTGREEN));
			break;
		case 13://carbon flake
			list.add(setTooltipData("It's a flake of carbon.", ColorReference.LIGHTGREEN));
			break;
		case 14://carbon thread
			list.add(setTooltipData("It's a strand of carbon fibre.", ColorReference.LIGHTGREEN));
			break;
		case 15://carbon fibre
			list.add(setTooltipData("It's several strands of carbon fibre.", ColorReference.LIGHTGREEN));
			break;
		case 16://carbon nanoflake
			list.add(setTooltipData("A small pile of carbon nanoflakes.", ColorReference.LIGHTGREEN));
			break;
		case 17://carbon nanotube
			list.add(setTooltipData("A few carbon nanotubes.", ColorReference.LIGHTGREEN));
			break;
		case 18://coarse threading
			list.add(setTooltipData("Coarse Iron threading to improve conduction.", ColorReference.LIGHTGREEN));
			break;
		case 19://fine threading
			list.add(setTooltipData("Fine Steel threading to improve conduction.", ColorReference.LIGHTGREEN));
			break;
		default:
			list.add(setTooltipData("This isn't even an item!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
		}
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "Pencil";
		case 1:
			return "CleansingPotion";
		case 2:
			return "pCleansingPotion";
		case 3:
			return "carbonChunk";
		case 4:
			return "glassInsulation";
		case 5:
			return "highDensityInsulation";
		case 6:
			return "ash";
		case 7:
			return "ironGear";
		case 8:
			return "refinedIronGear";
		case 9:
			return "pigIronGear";
		case 10:
			return "mildSteelGear";
		case 11:
			return "steelGear";
		case 12:
			return "smallCarbonPile";
		case 13:
			return "carbonFlake";
		case 14:
			return "carbonThread";
		case 15:
			return "carbonFibre";
		case 16:
			return "carbonNanoflake";
		case 17:
			return "carbonNanotube";
		case 18:
			return "coarseThreading";
		case 19:
			return "fineThreading";
		default:
			return "ItemMisc";
		}
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:pencilTexture");
		iconArray[1] = ir.registerIcon("carbonization:cleansingPotionTexture");
		iconArray[2] = ir.registerIcon("carbonization:cleansingPotionTexture");
		iconArray[3] = ir.registerIcon("carbonization:carbonChunkTexture");
		iconArray[4] = ir.registerIcon("carbonization:glassInsulationTexture");
		iconArray[5] = ir.registerIcon("carbonization:highDensityInsulationTexture");
		iconArray[6] = ir.registerIcon("carbonization:ashTexture");
		iconArray[7] = ir.registerIcon("carbonization:ironGearTexture");
		iconArray[8] = ir.registerIcon("carbonization:refinedIronGearTexture");
		iconArray[9] = ir.registerIcon("carbonization:pigIronGearTexture");
		iconArray[10] = ir.registerIcon("carbonization:mildSteelGearTexture");
		iconArray[11] = ir.registerIcon("carbonization:steelGearTexture");
		iconArray[12] = ir.registerIcon("carbonization:smallGraphiteDustTexture");
		iconArray[13] = ir.registerIcon("carbonization:carbonFlakeTexture");
		iconArray[14] = ir.registerIcon("carbonization:carbonThreadTexture");
		iconArray[15] = ir.registerIcon("carbonization:carbonFibreTexture");
		iconArray[16] = ir.registerIcon("carbonization:carbonNanoflakeTexture");
		iconArray[17] = ir.registerIcon("carbonization:carbonNanotubeTexture");
		iconArray[18] = ir.registerIcon("carbonization:coarseThreadingTexture");
		iconArray[19] = ir.registerIcon("carbonization:fineThreadingTexture");
	}
	
	public Icon getIconFromDamage(int value)
	{
		return iconArray[value];
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 0 || par2 >= this.iconArray.length)
        {
            par2 = 0;
        }

        return this.iconArray[par2];
    }
    
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		String r="";
		
		switch (par1ItemStack.getItemDamage())
		{
		case 0:
			r="graphitepencil";
			break;
		case 1:
			r="curepotion";
			break;
		case 2:
			r="pcurepotion";
			break;
		case 3:
			r="carbonchunk";
			break;
		case 4:
			r="glassinsulation";
			break;
		case 5:
			r="highdensityinsulation";
			break;
		case 6:
			r="ash";
			break;
		case 7:
			r="irongear";
			break;
		case 8:
			r="refinedirongear";
			break;
		case 9:
			r="pigirongear";
			break;
		case 10:
			r="mildsteelgear";
			break;
		case 11:
			r="steelgear";
			break;
		case 12:
			r="smallgraphitedust";
			break;
		case 13:
			r="carbonflake";
			break;
		case 14:
			r="carbonthread";
			break;
		case 15:
			r="carbonfibre";
			break;
		case 16:
			r="carbonnanoflake";
			break;
		case 17:
			r="carbonnanotube";
			break;
		case 18:
			r="coarsethreading";
			break;
		case 19:
			r="finethreading";
			break;
		default:
			r="blaarg";
			break;
		}
		
		return r;
	}
	
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
        par3List.add(new ItemStack(par1, 1, 6));
        par3List.add(new ItemStack(par1, 1, 7));
        par3List.add(new ItemStack(par1, 1, 8));
        par3List.add(new ItemStack(par1, 1, 9));
        par3List.add(new ItemStack(par1, 1, 10));
        par3List.add(new ItemStack(par1, 1, 11));
        par3List.add(new ItemStack(par1, 1, 12));
        par3List.add(new ItemStack(par1, 1, 13));
        par3List.add(new ItemStack(par1, 1, 14));
        par3List.add(new ItemStack(par1, 1, 15));
        par3List.add(new ItemStack(par1, 1, 16));
        par3List.add(new ItemStack(par1, 1, 17));
        par3List.add(new ItemStack(par1, 1, 18));
        par3List.add(new ItemStack(par1, 1, 19));
    }
	
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        if(!par2World.isRemote)
        {
        	par3EntityPlayer.curePotionEffects(new ItemStack(Item.bucketMilk,1));
        	if(par1ItemStack.getItemDamage()==2)
        	{
        		par3EntityPlayer.addPotionEffect(new PotionEffect(15,1200));
        		par3EntityPlayer.addPotionEffect(new PotionEffect(4,1200));
        		par3EntityPlayer.addPotionEffect(new PotionEffect(9,1200));
        	}
        }

        return par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if(par1ItemStack.getItemDamage()==2 || par1ItemStack.getItemDamage()==1)//cure potions
    	{
    		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
    	}
    		return par1ItemStack;
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