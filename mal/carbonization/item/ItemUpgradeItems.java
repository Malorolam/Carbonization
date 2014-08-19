package mal.carbonization.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.core.api.IMachineUpgrade;
import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemUpgradeItems extends Item implements IMachineUpgrade {

	private IIcon[] iconArray = new IIcon[30];
			
	public ItemUpgradeItems() {
		super();
		this.hasSubtypes=true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemUpgrade");
		this.setCreativeTab(carbonization.tabItems);
		this.maxStackSize = 1;
	}

	@Override
	public String getUpgradeName(int damage) {
		switch(damage)
		{
		case 0:
			return "efficiency1";
		case 1:
			return "efficiency2";
		case 2:
			return "efficiency3";
		case 3:
			return "efficiency4";
		case 4:
			return "efficiency5";
		case 5:
			return "fortune1";
		case 6:
			return "fortune2";
		case 7:
			return "fortune3";
		case 8:
			return "fortune4";
		case 9:
			return "fortune5";
		case 10:
			return "haste1";
		case 11:
			return "haste2";
		case 12:
			return "haste3";
		case 13:
			return "haste4";
		case 14:
			return "haste5";
		case 15:
			return "storage1";
		case 16:
			return "storage2";
		case 17:
			return "storage3";
		case 18:
			return "storage4";
		case 19:
			return "storage5";
		case 20:
			return "fixed1";
		case 21:
			return "void1";
		case 22:
			return "ejection0";//y+1
		case 23:
			return "ejection1";//y-1
		case 24:
			return "ejection2";//z+1
		case 25:
			return "ejection3";//x+1
		case 26:
			return "ejection4";//z-1
		case 27:
			return "ejection5";//x-1
		case 28:
			return "silktouch1";
		case 29:
			return "hardness1";
		default:
			return null;
		}
	}
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		switch(is.getItemDamage())
		{
		case 0:
			list.add(setTooltipData("Decreases Fuel Consumption by",ColorReference.DARKGREY));
			list.add(setTooltipData("10% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 1:
			list.add(setTooltipData("Decreases Fuel Consumption by",ColorReference.DARKGREY));
			list.add(setTooltipData("30% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 2:
			list.add(setTooltipData("Decreases Fuel Consumption by",ColorReference.DARKGREY));
			list.add(setTooltipData("50% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 3:
			list.add(setTooltipData("Decreases Fuel Consumption by",ColorReference.DARKGREY));
			list.add(setTooltipData("70% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 4:
			list.add(setTooltipData("Decreases Fuel Consumption by",ColorReference.DARKGREY));
			list.add(setTooltipData("90% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 5:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+1 additively.", ColorReference.DARKGREY));
			break;
		case 6:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+2 additively.", ColorReference.DARKGREY));
			break;
		case 7:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+3 additively.", ColorReference.DARKGREY));
			break;
		case 8:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+4 additively.", ColorReference.DARKGREY));
			break;
		case 9:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+5 additively.", ColorReference.DARKGREY));
			break;
		case 10:
			list.add(setTooltipData("Decreases cooldown time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-10% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 11:
			list.add(setTooltipData("Decreases cooldown time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-30% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 12:
			list.add(setTooltipData("Decreases cooldown time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-50% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 13:
			list.add(setTooltipData("Decreases cooldown time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-70% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 14:
			list.add(setTooltipData("Decreases cooldown time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-90% multiplicatively.", ColorReference.DARKGREY));
			break;
		case 15:
			list.add(setTooltipData("Increases fuel tank size", ColorReference.DARKGREY));
			list.add(setTooltipData("by 1 bucket additively.", ColorReference.DARKGREY));
			break;
		case 16:
			list.add(setTooltipData("Increases fuel tank size", ColorReference.DARKGREY));
			list.add(setTooltipData("by 2 buckets additively.", ColorReference.DARKGREY));
			break;
		case 17:
			list.add(setTooltipData("Increases fuel tank size", ColorReference.DARKGREY));
			list.add(setTooltipData("by 3 buckets additively.", ColorReference.DARKGREY));
			break;
		case 18:
			list.add(setTooltipData("Increases fuel tank size", ColorReference.DARKGREY));
			list.add(setTooltipData("by 4 buckets additively.", ColorReference.DARKGREY));
			break;
		case 19:
			list.add(setTooltipData("Increases fuel tank size", ColorReference.DARKGREY));
			list.add(setTooltipData("by 5 buckets additively.", ColorReference.DARKGREY));
			break;
		case 20:
			list.add(setTooltipData("Prevents machines that interact", ColorReference.DARKGREY));
			list.add(setTooltipData("with the world from iterating position.", ColorReference.DARKGREY));
			break;
		case 21:
			list.add(setTooltipData("Prevents machine jamming by", ColorReference.DARKGREY));
			list.add(setTooltipData("voiding any overflow items.", ColorReference.DARKGREY));
			break;
		case 22:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting +1 on the y-axis.", ColorReference.LIGHTGREEN));
			break;
		case 23:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting -1 on the y-axis.", ColorReference.LIGHTGREEN));
			break;
		case 24:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting +1 on the z-axis.", ColorReference.LIGHTGREEN));
			break;
		case 25:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting +1 on the x-axis.", ColorReference.LIGHTGREEN));
			break;
		case 26:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting -1 on the z-axis.", ColorReference.LIGHTGREEN));
			break;
		case 27:
			list.add(setTooltipData("Automatically tries to eject", ColorReference.DARKGREY));
			list.add(setTooltipData("items to a specific face.", ColorReference.DARKGREY));
			list.add(setTooltipData("Currently ejecting -1 on the x-axis.", ColorReference.LIGHTGREEN));
			break;
		case 28:
			list.add(setTooltipData("Adds Silk Touch effect to the dig head.",ColorReference.DARKGREY));
			list.add(setTooltipData("Using this takes priority over Fortune effects.", ColorReference.DARKGREY));
			break;
		case 29:
			list.add(setTooltipData("Ignores block hardness for purposes of",ColorReference.DARKGREY));
			list.add(setTooltipData("determining fuel consumption and if a block", ColorReference.DARKGREY));
			list.add(setTooltipData("can be mined.", ColorReference.DARKGREY));
			break;
		default:
			list.add(setTooltipData("This isn't an upgrade item!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it.", ColorReference.DARKRED));
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
			return "item.upgrade.efficiency1";
		case 1:
			return "item.upgrade.efficiency2";
		case 2:
			return "item.upgrade.efficiency3";
		case 3:
			return "item.upgrade.efficiency4";
		case 4:
			return "item.upgrade.efficiency5";
		case 5:
			return "item.upgrade.fortune1";
		case 6:
			return "item.upgrade.fortune2";
		case 7:
			return "item.upgrade.fortune3";
		case 8:
			return "item.upgrade.fortune4";
		case 9:
			return "item.upgrade.fortune5";
		case 10:
			return "item.upgrade.haste1";
		case 11:
			return "item.upgrade.haste2";
		case 12:
			return "item.upgrade.haste3";
		case 13:
			return "item.upgrade.haste4";
		case 14:
			return "item.upgrade.haste5";
		case 15:
			return "item.upgrade.storage1";
		case 16:
			return "item.upgrade.storage2";
		case 17:
			return "item.upgrade.storage3";
		case 18:
			return "item.upgrade.storage4";
		case 19:
			return "item.upgrade.storage5";
		case 20:
			return "item.upgrade.fixed1";
		case 21:
			return "item.upgrade.void1";
		case 22:
			return "item.upgrade.ejection0";
		case 23:
			return "item.upgrade.ejection1";
		case 24:
			return "item.upgrade.ejection2";
		case 25:
			return "item.upgrade.ejection3";
		case 26:
			return "item.upgrade.ejection4";
		case 27:
			return "item.upgrade.ejection5";
		case 28:
			return "item.upgrade.silktouch1";
		case 29:
			return "item.upgrade.hardness1";
		default:
			return "item.ItemUpgrade";
		}
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:upgradeEfficiency1Texture");
		iconArray[1] = ir.registerIcon("carbonization:upgradeEfficiency2Texture");
		iconArray[2] = ir.registerIcon("carbonization:upgradeEfficiency3Texture");
		iconArray[3] = ir.registerIcon("carbonization:upgradeEfficiency4Texture");
		iconArray[4] = ir.registerIcon("carbonization:upgradeEfficiency5Texture");
		iconArray[5] = ir.registerIcon("carbonization:upgradeFortune1Texture");
		iconArray[6] = ir.registerIcon("carbonization:upgradeFortune2Texture");
		iconArray[7] = ir.registerIcon("carbonization:upgradeFortune3Texture");
		iconArray[8] = ir.registerIcon("carbonization:upgradeFortune4Texture");
		iconArray[9] = ir.registerIcon("carbonization:upgradeFortune5Texture");
		iconArray[10] = ir.registerIcon("carbonization:upgradeHaste1Texture");
		iconArray[11] = ir.registerIcon("carbonization:upgradeHaste2Texture");
		iconArray[12] = ir.registerIcon("carbonization:upgradeHaste3Texture");
		iconArray[13] = ir.registerIcon("carbonization:upgradeHaste4Texture");
		iconArray[14] = ir.registerIcon("carbonization:upgradeHaste5Texture");
		iconArray[15] = ir.registerIcon("carbonization:upgradeStorage1Texture");
		iconArray[16] = ir.registerIcon("carbonization:upgradeStorage2Texture");
		iconArray[17] = ir.registerIcon("carbonization:upgradeStorage3Texture");
		iconArray[18] = ir.registerIcon("carbonization:upgradeStorage4Texture");
		iconArray[19] = ir.registerIcon("carbonization:upgradeStorage5Texture");
		iconArray[20] = ir.registerIcon("carbonization:upgradeFixed1Texture");
		iconArray[21] = ir.registerIcon("carbonization:upgradeVoid1Texture");
		iconArray[22] = ir.registerIcon("carbonization:upgradeEjection0Texture");
		iconArray[23] = ir.registerIcon("carbonization:upgradeEjection1Texture");
		iconArray[24] = ir.registerIcon("carbonization:upgradeEjection2Texture");
		iconArray[25] = ir.registerIcon("carbonization:upgradeEjection3Texture");
		iconArray[26] = ir.registerIcon("carbonization:upgradeEjection4Texture");
		iconArray[27] = ir.registerIcon("carbonization:upgradeEjection5Texture");
		iconArray[28] = ir.registerIcon("carbonization:upgradeSilktouch1Texture");
		iconArray[29] = ir.registerIcon("carbonization:upgradeHardness1Texture");
	}
	
	public IIcon getIconFromDamage(int value)
	{
		return iconArray[value];
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 0 || par2 >= this.iconArray.length)
        {
            par2 = 0;
        }

        return this.iconArray[par2];
    }
	
	public String getItemNameIS(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "item.upgrade.efficiency1";
		case 1:
			return "item.upgrade.efficiency2";
		case 2:
			return "item.upgrade.efficiency3";
		case 3:
			return "item.upgrade.efficiency4";
		case 4:
			return "item.upgrade.efficiency5";
		case 5:
			return "item.upgrade.fortune1";
		case 6:
			return "item.upgrade.fortune2";
		case 7:
			return "item.upgrade.fortune3";
		case 8:
			return "item.upgrade.fortune4";
		case 9:
			return "item.upgrade.fortune5";
		case 10:
			return "item.upgrade.haste1";
		case 11:
			return "item.upgrade.haste2";
		case 12:
			return "item.upgrade.haste3";
		case 13:
			return "item.upgrade.haste4";
		case 14:
			return "item.upgrade.haste5";
		case 15:
			return "item.upgrade.storage1";
		case 16:
			return "item.upgrade.storage2";
		case 17:
			return "item.upgrade.storage3";
		case 18:
			return "item.upgrade.storage4";
		case 19:
			return "item.upgrade.storage5";
		case 20:
			return "item.upgrade.fixed1";
		case 21:
			return "item.upgrade.void1";
		case 22:
			return "item.upgrade.ejection0";
		case 23:
			return "item.upgrade.ejection1";
		case 24:
			return "item.upgrade.ejection2";
		case 25:
			return "item.upgrade.ejection3";
		case 26:
			return "item.upgrade.ejection4";
		case 27:
			return "item.upgrade.ejection5";
		case 28:
			return "item.upgrade.silktouch1";
		case 29:
			return "item.upgrade.hardness1";
		default:
			return "item.ItemUpgrade";
		}
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
    {
    	if(is.getItemDamage() >= 22 && is.getItemDamage()<27)
    	{
    		is.setItemDamage(is.getItemDamage()+1);
    	}
    	else if(is.getItemDamage() == 27)
    	{
    		is.setItemDamage(22);
    	}
    	return is;
    }
	
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
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
        par3List.add(new ItemStack(par1, 1, 20));
        par3List.add(new ItemStack(par1, 1, 21));
        par3List.add(new ItemStack(par1, 1, 22));
        par3List.add(new ItemStack(par1, 1, 23));
        par3List.add(new ItemStack(par1, 1, 24));
        par3List.add(new ItemStack(par1, 1, 25));
        par3List.add(new ItemStack(par1, 1, 26));
        par3List.add(new ItemStack(par1, 1, 27));
        par3List.add(new ItemStack(par1, 1, 28));
        par3List.add(new ItemStack(par1, 1, 29));
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
