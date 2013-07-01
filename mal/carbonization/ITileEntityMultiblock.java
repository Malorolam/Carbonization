package mal.carbonization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileEntityMultiblock {
	
	public abstract void activate(World world, int x, int y, int z, EntityPlayer par5EntityPlayer);
	
	public abstract void revert();
	
	public abstract void initilize(Object[] params);
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