package mal.carbonization;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemTestBlock extends ItemBlock {
	
	public ItemTestBlock(int par1)
	{
		super(par1);
		this.setUnlocalizedName("ItemTestBlock");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		return "carbonization.testblock";
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