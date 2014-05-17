package mal.carbonization.tileentity;

import cpw.mods.fml.common.network.PacketDispatcher;
import mal.carbonization.carbonization;
import mal.carbonization.network.PacketHandler;
import mal.core.multiblock.MultiBlockInstantiator;
import mal.core.multiblock.MultiBlockMatcher;
import mal.core.multiblock.Multiblock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMultiblockBoreInit extends TileEntity {

	private MultiBlockMatcher match;
	private MultiBlockMatcher mbEmpty;
	//dimensions of the multiblock, set through GUI
	public int width;
	public int height;
	public int depth;
	public boolean activated=false;
	public int[] offset;
	
	public TileEntityMultiblockBoreInit()
	{
		this(3,3,3,null,null);
	}
	
	public TileEntityMultiblockBoreInit(int width, int height, int depth, int[] offset, NBTTagCompound nbt)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		if(offset == null)
		{
			offset = new int[3];
			offset[0] = -1000;//unrealistic value for checking data, I'm never going
			offset[1] = -1000;//to make a multiblock that allows itself to be 1000 blocks long
			offset[2] = -1000;//too many chunk load problems...
		}
		this.offset = offset;
	}
	
	public void initData(int width, int height, int depth, int[] offset, String type)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		if(offset == null)
		{
			offset = new int[3];
			offset[0] = -1000;//unrealistic value for checking data, I'm never going
			offset[1] = -1000;//to make a multiblock that allows itself to be 1000 blocks long
			offset[2] = -1000;//too many chunk load problems...
		}
		this.offset = offset;
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    	
    	this.width = nbt.getInteger("width");
    	this.height = nbt.getInteger("height");
    	this.depth = nbt.getInteger("depth");
    	this.activated = nbt.getBoolean("activated");
    	this.offset = nbt.getIntArray("offset");
    	/*this.savedData = nbt.getCompoundTag("savedData");
    	if(savedData.hasNoTags())
    		savedData = null;*/
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setInteger("width", width);
    	nbt.setInteger("height", height);
    	nbt.setInteger("depth", depth);
    	nbt.setBoolean("activated", activated);
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
		//System.out.println("The tile entity is activated!");
		player.openGui(carbonization.instance, 1, world, x, y, z);
    }
	
	public void closeGui(EntityPlayer player, int xd, int yd, int zd, boolean activated)
	{
		width = xd;
		height = yd;
		depth = zd;
		this.activated = activated;
		//if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			PacketDispatcher.sendPacketToServer(getDescriptionPacket());
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return PacketHandler.getPacket(this);
	}
	
	/*
	 * Currently called after the multiblock is initialized fully and the block was activated
	 */
	public void processFunction(EntityPlayer player)
	{	
		//figure out dimensions from the relative position of things to the block
		//we know a valid structure has the control in a side using depth somewhere
		//so first figure out which axis to look on
		int sideAxis;
		if(worldObj.isAirBlock(xCoord+1, yCoord, zCoord) && worldObj.isAirBlock(xCoord-1, yCoord, zCoord))//x axis alignment
			sideAxis = 0;
		else if(worldObj.isAirBlock(xCoord, yCoord+1, zCoord) && worldObj.isAirBlock(xCoord, yCoord-1, zCoord))//y axis alignment
			sideAxis = 1;
		else if(worldObj.isAirBlock(xCoord, yCoord, zCoord+1) && worldObj.isAirBlock(xCoord, yCoord, zCoord-1))//z axis alignment
			sideAxis = 2;
		else
		{
			//failed somehow, so tell the player
			player.addChatMessage("Multiblock Failed: Ensure the control block is in a side and not an edge or corner.");
			return;
		}
		
		//Now figure out where the machine structure block is to determine which dimensions are which
		int xdiff=0;
		int ydiff=height;
		int zdiff=0;
		int facing = -1;
		if(sideAxis == 0)//x axis
		{
			xdiff = width;
			zdiff = depth;
			if(worldObj.getBlockId(xCoord, yCoord+1, zCoord)==carbonization.structure.blockID && worldObj.getBlockId(xCoord, yCoord-1, zCoord)==carbonization.structure.blockID
					&& worldObj.getBlockId(xCoord, yCoord, zCoord+1)==carbonization.structure.blockID && worldObj.getBlockId(xCoord, yCoord, zCoord-1)==carbonization.structure.blockID)
			{
				TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						player.addChatMessage("Multiblock Failed: Machine structure blocks shouldn't be above the control block.");
						return;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						player.addChatMessage("Multiblock Failed: Machine structure blocks shouldn't be below the control block.");
						return;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 4;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 5;
					}
				}
			}
			else
			{
				//failed somehow, so tell the player
				player.addChatMessage("Multiblock Failed: Ensure the control block is in the wall of the structure and surrounded by structure blocks.");
				return;
			}
		}
		if(sideAxis == 1)//y axis
		{
			if(worldObj.getBlockId(xCoord+1, yCoord, zCoord)==carbonization.structure.blockID && worldObj.getBlockId(xCoord-1, yCoord, zCoord)==carbonization.structure.blockID
					&& worldObj.getBlockId(xCoord, yCoord, zCoord+1)==carbonization.structure.blockID && worldObj.getBlockId(xCoord, yCoord, zCoord-1)==carbonization.structure.blockID)
			{
				TileEntity te = worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 0;
						xdiff = depth;
						zdiff = width;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 1;
						xdiff = depth;
						zdiff = width;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 4;
						xdiff = width;
						zdiff = depth;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 5;
						xdiff = width;
						zdiff = depth;
					}
				}
			}
			else
			{
				//failed somehow, so tell the player
				player.addChatMessage("Multiblock Failed: Ensure the control block is in the wall of the structure and surrounded by structure blocks.");
				return;
			}
		}
		if(sideAxis == 2)//z axis
		{
			xdiff = depth;
			zdiff = width;
			if(worldObj.getBlockId(xCoord, yCoord+1, zCoord)==carbonization.structure.blockID && worldObj.getBlockId(xCoord, yCoord-1, zCoord)==carbonization.structure.blockID
					&& worldObj.getBlockId(xCoord+1, yCoord, zCoord)==carbonization.structure.blockID && worldObj.getBlockId(xCoord-1, yCoord, zCoord)==carbonization.structure.blockID)
			{
				TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						player.addChatMessage("Multiblock Failed: Machine structure blocks shouldn't be above the control block.");
						return;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						player.addChatMessage("Multiblock Failed: Machine structure blocks shouldn't be below the control block.");
						return;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 0;
					}
				}
				te = worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);
				if(te instanceof TileEntityStructureBlock)
				{
					if(((TileEntityStructureBlock)te).purpose == 2)
					{
						facing = 1;
					}
				}
			}
			else
			{
				//failed somehow, so tell the player
				player.addChatMessage("Multiblock Failed: Ensure the control block is in the wall of the structure and surrounded by structure blocks.");
				return;
			}
		}
		
		
		match = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		mbEmpty = new MultiBlockMatcher(xdiff, ydiff, zdiff);
		match.buildFacedHollowSolid(0, 0, 0, xdiff-1, ydiff-1, zdiff-1, carbonization.structure.blockID, 0, carbonization.structure.blockID, 1000, 1, facing);

		int[] value;
		TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
		TileEntityStructureBlock ste = null;
		TileEntityMultiblockInit ite = null;
		if(te instanceof TileEntityStructureBlock)
			ste = (TileEntityStructureBlock) te;
		if(te instanceof TileEntityMultiblockInit)
			ite = (TileEntityMultiblockInit) te;
		
		
		if(offset[1] == -1000)
			value = MultiBlockInstantiator.matchPattern(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlockId(xCoord, yCoord, zCoord), te.blockMetadata, true));
		else//an actual offset
			value = MultiBlockInstantiator.matchPatternWithOffset(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlockId(xCoord, yCoord, zCoord),te.blockMetadata, true), offset);
		
		//System.out.println("Offset: " + offset[0] +", "+ offset[1] +", "+ offset[2]);
		
		if(value != null)
		{
			//System.out.println("Value: " + value[0] +", "+ value[1] +", "+ value[2]);
			MultiBlockInstantiator.createMultiBlock(match, xCoord, yCoord, zCoord, worldObj, value);
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
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
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