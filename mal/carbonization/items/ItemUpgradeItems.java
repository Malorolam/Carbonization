package mal.carbonization.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.api.IMachineUpgrade;
import mal.carbonization.carbonization;
import mal.core.ColorReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemUpgradeItems extends Item implements IMachineUpgrade {

	private Icon[] iconArray = new Icon[12];
			
	public ItemUpgradeItems(int par1) {
		super(par1);
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
			return "fortune1";
		case 4:
			return "fortune2";
		case 5:
			return "fortune3";
		case 6:
			return "haste1";
		case 7:
			return "haste2";
		case 8:
			return "haste3";
		case 9:
			return "haste4";
		case 10:
			return "silktouch";
		case 11:
			return "hardness";
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
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+1 additively.", ColorReference.DARKGREY));
			break;
		case 4:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+2 additively.", ColorReference.DARKGREY));
			break;
		case 5:
			list.add(setTooltipData("Increases Fortune Level by",ColorReference.DARKGREY));
			list.add(setTooltipData("+3 additively.", ColorReference.DARKGREY));
			break;
		case 6:
			list.add(setTooltipData("Decreases dig delay time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-10% additively.", ColorReference.DARKGREY));
			break;
		case 7:
			list.add(setTooltipData("Decreases dig delay time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-30% additively.", ColorReference.DARKGREY));
			break;
		case 8:
			list.add(setTooltipData("Decreases dig delay time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-50% additively.", ColorReference.DARKGREY));
			break;
		case 9:
			list.add(setTooltipData("Decreases dig delay time by",ColorReference.DARKGREY));
			list.add(setTooltipData("-70% additively.", ColorReference.DARKGREY));
			break;
		case 10:
			list.add(setTooltipData("Adds Silk Touch effect to the dig head.",ColorReference.DARKGREY));
			list.add(setTooltipData("Using this takes priority over Fortune effects.", ColorReference.DARKGREY));
			break;
		case 11:
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
			return "item.upgrade.fortune1";
		case 4:
			return "item.upgrade.fortune2";
		case 5:
			return "item.upgrade.fortune3";
		case 6:
			return "item.upgrade.haste1";
		case 7:
			return "item.upgrade.haste2";
		case 8:
			return "item.upgrade.haste3";
		case 9:
			return "item.upgrade.haste4";
		case 10:
			return "item.upgrade.silktouch1";
		case 11:
			return "item.upgrade.hardness1";
		default:
			return "item.ItemUpgrade";
		}
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:upgradeEfficiency1Texture");
		iconArray[1] = ir.registerIcon("carbonization:upgradeEfficiency2Texture");
		iconArray[2] = ir.registerIcon("carbonization:upgradeEfficiency3Texture");
		iconArray[3] = ir.registerIcon("carbonization:upgradeFortune1Texture");
		iconArray[4] = ir.registerIcon("carbonization:upgradeFortune2Texture");
		iconArray[5] = ir.registerIcon("carbonization:upgradeFortune3Texture");
		iconArray[6] = ir.registerIcon("carbonization:upgradeHaste1Texture");
		iconArray[7] = ir.registerIcon("carbonization:upgradeHaste2Texture");
		iconArray[8] = ir.registerIcon("carbonization:upgradeHaste3Texture");
		iconArray[9] = ir.registerIcon("carbonization:upgradeHaste4Texture");
		iconArray[10] = ir.registerIcon("carbonization:upgradeSilktouch1Texture");
		iconArray[11] = ir.registerIcon("carbonization:upgradeHardness1Texture");
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
			return "item.upgrade.fortune1";
		case 4:
			return "item.upgrade.fortune2";
		case 5:
			return "item.upgrade.fortune3";
		case 6:
			return "item.upgrade.haste1";
		case 7:
			return "item.upgrade.haste2";
		case 8:
			return "item.upgrade.haste3";
		case 9:
			return "item.upgrade.haste4";
		case 10:
			return "item.upgrade.silktouch1";
		case 11:
			return "item.upgrade.hardness1";
		default:
			return "item.ItemUpgrade";
		}
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
