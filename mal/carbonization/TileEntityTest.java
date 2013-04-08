package mal.carbonization;

import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.MultiBlockMatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTest extends TileEntity {

	private MultiBlockMatcher match;
	int xdiff = 9;
	int ydiff = 6;
	int zdiff = 9;
	
	public TileEntityTest()
	{
		match = new MultiBlockMatcher(xdiff, ydiff, zdiff);
	}
	
	public void activate(int x, int y, int z, World world)
	{
		boolean build = MultiBlockInstantiator.createWorldMultiBlock(match, x+1, y, z, xdiff, ydiff, zdiff, world);
		
		if(build)
			MultiBlockInstantiator.createMultiBlock(match, x+50, y, z, world);
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