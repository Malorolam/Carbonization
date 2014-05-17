package mal.carbonization.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockScaffolding extends Block{

	public BlockScaffolding(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setUnlocalizedName("blockScaffolding");
		this.setHardness(0.1f);
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		this.blockIcon = ir.registerIcon("carbonization:scaffoldingTexture");
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
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