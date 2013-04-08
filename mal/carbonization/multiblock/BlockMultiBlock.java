package mal.carbonization.multiblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;

/*
 * this is what blocks turn into to make this physically a multiblock structure
 * uses metadata to identify hopefully 16 or fewer valid block kinds, otherwise I'm going to have to get clever
 */
public class BlockMultiBlock extends Block{

	@SideOnly(Side.CLIENT)
	private Icon[] iconList;
	
	public BlockMultiBlock(int id, Material material)
	{
		super(id, material);
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