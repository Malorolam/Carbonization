package mal.carbonization;

import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.MultiBlockMatcher;
import mal.carbonization.multiblock.Multiblock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTest extends TileEntity {

	private MultiBlockMatcher match;
	private MultiBlockMatcher mbEmpty;
	int xdiff = 9;
	int ydiff = 6;
	int zdiff = 9;
	
	public TileEntityTest()
	{
		match = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		mbEmpty = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		match.buildBasedHollowSolid(0, 0, 0, 8, 5, 8, carbonization.structureBlock.blockID, (byte)0, carbonization.structureBlock.blockID, (byte)1, 1);
	}
	
	public void activate(int x, int y, int z, World world)
	{
		int[] value = MultiBlockInstantiator.matchPattern(match, x, y, z, world, new Multiblock(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z)));
		
		System.out.println(value[0]+", "+value[1]+", "+value[2]);
		if(value != null)
			MultiBlockInstantiator.createMultiBlock(mbEmpty, x+value[0], y+value[1], z+value[2], world);
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