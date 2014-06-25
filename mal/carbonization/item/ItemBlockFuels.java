package mal.carbonization.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFuels extends ItemBlock {

	public ItemBlockFuels(Block block) {
		super(block);
		this.setUnlocalizedName("ItemBlockFuels");
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return this.getUnlocalizedName()+"peat";
		case 1:
			return this.getUnlocalizedName()+"lignite";
		case 2:
			return this.getUnlocalizedName()+"sBitCoal";
		case 3:
			return this.getUnlocalizedName()+"bitCoal";
		case 4:
			return this.getUnlocalizedName()+"anthracite";
		case 5:
			return this.getUnlocalizedName()+"graphite";
		case 6:
			return this.getUnlocalizedName()+"peatblock";
		case 7:
			return this.getUnlocalizedName()+"ligniteblock";
		case 8:
			return this.getUnlocalizedName()+"sBitCoalblock";
		case 9:
			return this.getUnlocalizedName()+"bitCoalblock";
		case 10:
			return this.getUnlocalizedName()+"anthraciteblock";
		case 11:
			return this.getUnlocalizedName()+"graphiteblock";
		default:
			return this.getUnlocalizedName()+"BlockFuel";
		}
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0: 
			name = "peat";
			break;
		case 1: 
			name = "lignite"; 
			break;
		case 2:
			name = "sBituminous";
			break;
		case 3:
			name = "bituminous";
			break;
		case 4:
			name = "anthracite";
			break;
		case 5:
			name = "graphite";
			break;
		case 6: 
			name = "peatblock";
			break;
		case 7: 
			name = "ligniteblock"; 
			break;
		case 8:
			name = "sBituminousblock";
			break;
		case 9:
			name = "bituminousblock";
			break;
		case 10:
			name = "anthraciteblock";
			break;
		case 11:
			name = "graphiteblock";
			break;
		default: name = "blarg";
		}
		return name;
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