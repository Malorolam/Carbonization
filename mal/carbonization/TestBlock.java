package mal.carbonization;

import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.MultiBlockMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/*
 * A test block for debugging internal mechanisms
 * Currently set up for multiblock patterns
 */
public class TestBlock extends BlockContainer {

	public TestBlock(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setUnlocalizedName("testblock");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	if(world.isRemote)
    		return true;
    	else
    	{
    		if(par5EntityPlayer.isSneaking())
    			return false;
    		
    		TileEntityTest var10 = (TileEntityTest)world.getBlockTileEntity(x, y, z);

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
    		var10.activate(x, y, z, world);
    		
    		
    		
    		return true;
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTest();
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