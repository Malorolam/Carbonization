package mal.carbonization.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.core.reference.ColorReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemIngots extends Item {

	private IIcon[] iconArray = new IIcon[10];
	public ItemIngots() {
		super();
		this.hasSubtypes = true;
		this.setMaxDamage(0);
		this.setUnlocalizedName("ItemIngots");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://refined iron
			list.add(setTooltipData("A purified iron", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with better properties", ColorReference.LIGHTGREEN));
			break;
		case 1://pig iron
			list.add(setTooltipData("The presence of extra carbon", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("makes an intermediate material", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("that isn't too useful.", ColorReference.LIGHTGREEN));
			break;
		case 2://mild steel
			list.add(setTooltipData("Purification of excess carbon", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and impurities improves properties", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("in this high carbon material.", ColorReference.LIGHTGREEN));
			break;
		case 3://steel
			list.add(setTooltipData("A typical mid-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with multiple applications.", ColorReference.LIGHTGREEN));
			break;
		case 4://Ti6AL4V
			list.add(setTooltipData("An alloy of Titanium, Aluminum,", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and Vanadium used in biomedical", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("applications. (Ti6Al4V)", ColorReference.LIGHTGREEN));
			break;
		case 5://CoCr
			list.add(setTooltipData("An alloy of Cobalt and Chromium", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("used in biomedical applications. (CoCr)", ColorReference.LIGHTGREEN));
			break;
		case 6://titanium
			list.add(setTooltipData("A high-strength material that", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("when alloyed with Aluminum produces", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("an alloy suitable for many", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("biological applications.", ColorReference.LIGHTGREEN));
			break;
		case 7://Cobalt
			list.add(setTooltipData("A metal that is alloyed with various", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("other metals to produce materials", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("suitable for many applications.", ColorReference.LIGHTGREEN));
			break;
		case 8://Chromium
			list.add(setTooltipData("An additive metal used to improve", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("corrosion resistance in other metals.", ColorReference.LIGHTGREEN));
			break;
		case 9://Aluminum
			list.add(setTooltipData("An easy-to-work metal that can be", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("alloyed with other metals or used alone.", ColorReference.LIGHTGREEN));
			break;
		default:
			list.add(setTooltipData("This isn't even an ingot!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
			list.add(setTooltipData("Tier Error Material", ColorReference.DARKCYAN));
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
			return "item.refinedIron";
		case 1:
			return "item.pigIron";
		case 2:
			return "item.mildSteel";
		case 3:
			return "item.steel";
		case 4:
			return "item.ti6al4v";
		case 5:
			return "item.cocr";
		case 6:
			return "item.titanium";
		case 7:
			return "item.cobalt";
		case 8:
			return "item.chromium";
		case 9:
			return "item.aluminum";
		default:
			return "ItemIngot";
		}
	}
	
	@Override
	public void registerIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:refinedIronTexture");
		iconArray[1] = ir.registerIcon("carbonization:pigIronTexture");
		iconArray[2] = ir.registerIcon("carbonization:mildSteelTexture");
		iconArray[3] = ir.registerIcon("carbonization:steelTexture");
		iconArray[4] = ir.registerIcon("carbonization:titaniumIngotTexture");
		iconArray[5] = ir.registerIcon("carbonization:cobaltChromeIngotTexture");
		iconArray[6] = ir.registerIcon("carbonization:titaniumIngotTexture");
		iconArray[7] = ir.registerIcon("carbonization:cobaltIngotTexture");
		iconArray[8] = ir.registerIcon("carbonization:chromiumIngotTexture");
		iconArray[9] = ir.registerIcon("carbonization:aluminumIngotTexture");
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
	
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		switch(par1ItemStack.getItemDamage())
		{
		case 0:
			return "item.refinedIron";
		case 1:
			return "item.pigIron";
		case 2:
			return "item.mildSteel";
		case 3:
			return "item.steel";
		case 4:
			return "item.ti6al4v";
		case 5:
			return "item.cocr";
		case 6:
			return "item.titanium";
		case 7:
			return "item.cobalt";
		case 8:
			return "item.chromium";
		case 9:
			return "item.aluminum";
		default:
			return "ItemIngot";
		}
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