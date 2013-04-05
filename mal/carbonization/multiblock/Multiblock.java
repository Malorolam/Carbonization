package mal.carbonization.multiblock;

import net.minecraft.block.Block;;
//Stores a block and it's location
public class Multiblock {

	public Block block;
	public int x, y, z;
	
	public Multiblock(Block block, int x, int y, int z)
	{
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
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