package mal.carbonization.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.MultiblockInitMessage;
import mal.carbonization.network.MultiblockInitMessageServer;
import mal.core.MalCore;
import mal.core.multiblock.MultiBlockInstantiator;
import mal.core.multiblock.MultiBlockMatcher;
import mal.core.multiblock.Multiblock;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.core.tileentity.ITileEntityMultiblockInit;
import mal.core.tileentity.ITileEntityMultiblockSlave;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * The general TileEntity to create the multiblock structure
 * Currently linked to the test block and locked to furnace
 * @author Mal
 *
 */
public class TileEntityMultiblockInit extends TileEntity implements ITileEntityMultiblockInit{

	private Block masterBlock = carbonizationBlocks.multiblockFurnaceControlBlock;
	private Block slaveBlock = carbonizationBlocks.structureBlock;
	
	private MultiBlockMatcher match;
	private MultiBlockMatcher mbEmpty;
	//dimensions of the multiblock, set through GUI
	public int xdiff;
	public int ydiff;
	public int zdiff;
	public boolean activated=false;
	public int[] offset;
	//lowercase representation of what this multiblock is going to become
	public String type;
	
	public TileEntityMultiblockInit(String type)
	{
		this(3,3,3,null,null,type);
	}
	
	public TileEntityMultiblockInit()
	{
		this("none");
	}
	
	public TileEntityMultiblockInit(int xdiff, int ydiff, int zdiff, int[] offset, NBTTagCompound nbt, String type)
	{
		this.xdiff = xdiff;
		this.ydiff = ydiff;
		this.zdiff = zdiff;
		if(offset == null)
		{
			offset = new int[3];
			offset[0] = -1000;//unrealistic value for checking data, I'm never going
			offset[1] = -1000;//to make a multiblock that allows itself to be 1000 blocks long
			offset[2] = -1000;//too many chunk load problems...
		}
		this.offset = offset;
		this.type = type;
	}
	
	public void initData(int xdiff, int ydiff, int zdiff, int[] offset, String type)
	{
		this.xdiff = xdiff;
		this.ydiff = ydiff;
		this.zdiff = zdiff;
		if(offset == null)
		{
			offset = new int[3];
			offset[0] = -1000;//unrealistic value for checking data, I'm never going
			offset[1] = -1000;//to make a multiblock that allows itself to be 1000 blocks long
			offset[2] = -1000;//too many chunk load problems...
		}
		this.offset = offset;
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
    	this.type = nbt.getString("type");
    	this.offset = nbt.getIntArray("offset");
    	/*this.savedData = nbt.getCompoundTag("savedData");
    	if(savedData.hasNoTags())
    		savedData = null;*/
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setInteger("xdiff", xdiff);
    	nbt.setInteger("ydiff", ydiff);
    	nbt.setInteger("zdiff", zdiff);
    	nbt.setBoolean("activated", activated);
    	nbt.setString("type", type);
    	nbt.setIntArray("offset", offset);
    	/*if(savedData != null)
    	{
    		System.out.println("saving nbt backup");
    		nbt.setCompoundTag("savedData", savedData);
    	}*/
    	//System.out.println("Writing Data: " + xdiff + ", " + ydiff + ", " + zdiff + "; " + activated + ", " + type);
    }
	
	/*
	 * Called when the block is activated
	 */
	public void activate(int x, int y, int z, World world, EntityPlayer player)
	{
		player.openGui(carbonization.carbonizationInstance, 2, world, x, y, z);
    }
	
	public void closeGui(EntityPlayer player, int xd, int yd, int zd, boolean activated)
	{
		xdiff = xd;
		ydiff = yd;
		zdiff = zd;
		this.activated = activated;
		//if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			CarbonizationPacketHandler.instance.sendToServer(getDescriptionMessage());
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return CarbonizationPacketHandler.instance.getPacketFrom(new MultiblockInitMessage(this));
	}
	
	public IMessage getDescriptionMessage()
	{
		return new MultiblockInitMessageServer(this);
	}
	
	/*
	 * Currently called after the multiblock is initialized fully and the block was activated
	 */
	public void processFunction()
	{	
		match = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		mbEmpty = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		match.buildBasedHollowSolid(0, 0, 0, xdiff-1, ydiff-1, zdiff-1, slaveBlock, 0, slaveBlock, 1000, 1);

		int[] value;
		TileEntity te = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		ITileEntityMultiblockSlave ste = null;
		TileEntityMultiblockInit ite = null;
		if(te instanceof ITileEntityMultiblockSlave)
			ste = (ITileEntityMultiblockSlave) te;
		if(te instanceof TileEntityMultiblockInit)
			ite = (TileEntityMultiblockInit) te;
		
		
		if(offset[1] == -1000)
			value = MultiBlockInstantiator.matchPattern(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlock(xCoord, yCoord, zCoord), te.blockMetadata, true));
		else//an actual offset
			value = MultiBlockInstantiator.matchPatternWithOffset(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlock(xCoord, yCoord, zCoord),te.blockMetadata, true), offset);
		
		System.out.println("Offset: " + offset[0] +", "+ offset[1] +", "+ offset[2]);
		
		if(value != null)
		{
			System.out.println("Value: " + value[0] +", "+ value[1] +", "+ value[2]);
			MultiBlockInstantiator.createMultiBlock(match, xCoord, yCoord, zCoord, worldObj, value, masterBlock, slaveBlock);
		}
		else
		{
			//System.out.println("Value is Null");
		}
	}
	
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj == null)
        {
            return true;
        }
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
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