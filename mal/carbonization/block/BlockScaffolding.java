package mal.carbonization.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class BlockScaffolding extends Block{

	public BlockScaffolding(Material par2Material) {
		super(par2Material);
		this.setBlockName("blockScaffolding");
		this.setHardness(0.1f);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		this.blockIcon = ir.registerIcon("carbonization:scaffoldingTexture");
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return null;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
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