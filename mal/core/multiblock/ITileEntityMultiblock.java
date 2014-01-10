package mal.core.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileEntityMultiblock extends IInventory, net.minecraft.inventory.ISidedInventory{

	public abstract void activate(World world, int x, int y, int z, EntityPlayer par5EntityPlayer);
	
	public abstract void revert();
	
	public abstract void initilize(Object[] params);

	public abstract void writeToNBT(NBTTagCompound nbt);

	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public abstract int getX();
	public abstract int getY();
	public abstract int getZ();
}

/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/