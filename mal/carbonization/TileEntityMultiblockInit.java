package mal.carbonization;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.MultiBlockMatcher;
import mal.carbonization.multiblock.Multiblock;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * The general TileEntity to create the multiblock structure
 * Currently linked to the test block and locked to furnace
 * @author Mal
 *
 */
public class TileEntityMultiblockInit extends TileEntity {

	private MultiBlockMatcher match;
	private MultiBlockMatcher mbEmpty;
	//dimensions of the multiblock, set through GUI
	int xdiff = 0;
	int ydiff = 0;
	int zdiff = 0;
	boolean activated=false;
	//lowercase representation of what this multiblock is going to become
	String type;
	
	public TileEntityMultiblockInit(String type)
	{
		this.type = type;
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    	
    	this.xdiff = nbt.getInteger("xdiff");
    	this.ydiff = nbt.getInteger("ydiff");
    	this.zdiff = nbt.getInteger("zdiff");
    	this.activated = nbt.getBoolean("activated");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setInteger("xdiff", xdiff);
    	nbt.setInteger("ydiff", ydiff);
    	nbt.setInteger("zdiff", zdiff);
    	nbt.setBoolean("activated", activated);
    }
	
    /*
     * Creates the base multiblock, currently partially implemented
     */
	public void initilizeFunction(int xd, int yd, int zd)
	{
		System.out.println("xd: " + xd + ", xdiff: " + xdiff);
		System.out.println("yd: " + yd + ", ydiff: " + ydiff);
		System.out.println("zd: " + zd + ", zdiff: " + zdiff);
		
		if(xd != xdiff || yd != ydiff ||zd != zdiff)
		{
			xdiff = xd;
			ydiff = yd;
			zdiff = zd;
		}
	}
	
	/*
	 * Called when the block is activated
	 */
	public void activate(int x, int y, int z, World world, EntityPlayer player)
	{
		System.out.println("The tile entity is activated!");
        player.openGui(carbonization.instance, 1, world, x, y, z);
    }
	
	public void closeGui(boolean activated, EntityPlayer player)
	{
		this.activated = activated;

        if(player instanceof EntityClientPlayerMP)
        	((EntityClientPlayerMP) player).sendQueue.addToSendQueue(getDescriptionPacket());
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return PacketHandler.getPacket(this);
	}
	
	/*
	 * Currently called after the multiblock is initialized fully and the block was activated
	 */
	public void processFunction()
	{
		match = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		mbEmpty = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		match.buildBasedHollowSolid(0, 0, 0, xdiff-1, ydiff-1, zdiff-1, carbonization.structureBlock.blockID, (byte)0, carbonization.structureBlock.blockID, (byte)1, 1);

		int[] value = MultiBlockInstantiator.matchPattern(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlockId(xCoord, yCoord, zCoord), worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
		
		if(value != null)
		{
			MultiBlockInstantiator.createMultiBlock(mbEmpty, xCoord-value[0], yCoord-value[1], zCoord-value[2], worldObj);
			System.out.println(value[0]+", "+value[1]+", "+value[2]);
		}
		else
			System.out.println("Null");
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		// TODO Auto-generated method stub
		return true;
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