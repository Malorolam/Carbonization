package mal.carbonization.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.core.multiblock.MultiblockWorkQueueItem;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/*
 * Message to sync the tile entity to the client from the server
 * Unlike the client version, this sends all information, since we trust the server, not the dirty dirty client, who lies to us precioussss
 */
public class MultiblockFurnaceMessageServer implements IMessage, IMessageHandler<MultiblockFurnaceMessageServer, IMessage>{

	public int xpos, ypos, zpos;
	public int xsize, ysize, zsize;
	public int[] offset;
	public boolean properlyActivated;
	public int oreCapacity;//number of ore blocks that can be processed at one time
	public int slagTank;//amount of slag currently in the tank
	public double[] componentTiers;//average efficiency of the insulation [0] and conduction[1] 
	public ItemStack[] inputStacks;
	public ItemStack[] outputStacks;
	public int fuelStack;//the amount of fuel time available
	public int numQueueJobs;//number of jobs in the queue
	public int grossCookTime;
	public int grossMaxCookTime;
	public int maxFuelCapacity;
	public boolean passFuel;//pass unneeded fuel through the inventory
	public List<MultiblockWorkQueueItem> queue;
	public HashMap<String, Integer> oreSlagInQueue;
	public boolean useInventory;
	
	public MultiblockFurnaceMessageServer(){}
	
	public MultiblockFurnaceMessageServer(TileEntityMultiblockFurnace furnace, boolean inventory)
	{
		xpos = furnace.xCoord;
		ypos = furnace.yCoord;
		zpos = furnace.zCoord;
		xsize = furnace.xsize;
		ysize = furnace.ysize;
		zsize = furnace.zsize;
		offset = furnace.offset;
		properlyActivated = furnace.properlyActivated;
		oreCapacity = furnace.oreCapacity;
		slagTank = furnace.slagTank;
		componentTiers = furnace.componentTiers;
		fuelStack = furnace.getFuelStack();
		numQueueJobs = furnace.getNumQueueJobs();
		grossCookTime = furnace.getGrossCookTime();
		grossMaxCookTime = furnace.getGrossMaxCookTime();
		maxFuelCapacity = furnace.getMaxFuelCapacity();
		passFuel = furnace.passFuel;
		queue = furnace.getQueue();
		oreSlagInQueue = furnace.getOreMap();
		useInventory = inventory;
		if(inventory)
		{
			inputStacks = furnace.inputStacks;
			outputStacks = furnace.outputStacks;
		}
	}
	
	@Override
	public IMessage onMessage(MultiblockFurnaceMessageServer message,
			MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityMultiblockFurnace)
		{
			TileEntityMultiblockFurnace fte = (TileEntityMultiblockFurnace)te;
			
			fte.xsize = message.xsize;
			fte.ysize = message.ysize;
			fte.zsize = message.zsize;
			fte.offset = message.offset;
			fte.properlyActivated = message.properlyActivated;
			fte.oreCapacity = message.oreCapacity;
			fte.slagTank = message.slagTank;
			fte.componentTiers = message.componentTiers;
			fte.setMaxFuelCapacity(message.maxFuelCapacity);
			fte.setFuelStack(message.fuelStack);
			fte.setNumQueueJobs(message.numQueueJobs);
			fte.setGrossCookTime(message.grossCookTime);
			fte.setGrossMaxCookTime(message.grossMaxCookTime);
			fte.passFuel = message.passFuel;
			fte.setQueue(message.queue);
			fte.setOreMap(message.oreSlagInQueue);
			if(message.useInventory)
			{
				fte.inputStacks = message.inputStacks;
				fte.outputStacks = message.outputStacks;
			}
			fte.calculateData();
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		xsize = buf.readInt();
		ysize = buf.readInt();
		zsize = buf.readInt();
		useInventory = buf.readBoolean();
		byte offsetsize = buf.readByte();
		offset = new int[offsetsize];
		for(int i = 0; i < offset.length; i++)
			offset[i] = buf.readInt();
		properlyActivated = buf.readBoolean();
		oreCapacity = buf.readInt();
		slagTank = buf.readInt();
		byte tiersize = buf.readByte();
		componentTiers = new double[tiersize];
		for(int j = 0; j < componentTiers.length; j++)
			componentTiers[j] = buf.readDouble();
		if(useInventory)
		{
			int l = buf.readInt();
			int c = buf.readInt();
			byte[] b = new byte[l];
			buf.readBytes(b);
			inputStacks = ByteArraytoItemStack(b, c);
			int ll = buf.readInt();
			int cc = buf.readInt();
			byte[] bb = new byte[ll];
			buf.readBytes(bb);
			outputStacks = ByteArraytoItemStack(bb, cc);
		}
		fuelStack = buf.readInt();
		numQueueJobs = buf.readInt();
		grossCookTime = buf.readInt();
		grossMaxCookTime = buf.readInt();
		maxFuelCapacity = buf.readInt();
		passFuel = buf.readBoolean();
		int queuenum = buf.readInt();
		queue = new ArrayList<MultiblockWorkQueueItem>();
		for(int i = 0; i < queuenum; i++)
		{
			int by = buf.readInt();
			byte[] ba = new byte[by];
			buf.readBytes(ba);
			MultiblockWorkQueueItem q = queueItemfromBytes(ba);
			if(q!=null)
				queue.add(q);
		}
		try {
		int slagnum = buf.readInt();
		byte[] slagbyte = new byte[slagnum];
		buf.readBytes(slagbyte);
		ByteArrayInputStream bin = new ByteArrayInputStream(slagbyte);
		ObjectInputStream in = new ObjectInputStream(bin);
		oreSlagInQueue = (HashMap<String, Integer>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeInt(xsize);
		buf.writeInt(ysize);
		buf.writeInt(zsize);
		buf.writeBoolean(useInventory);
		buf.writeByte(offset.length);
		for(int i = 0; i < offset.length; i++)
			buf.writeInt(offset[i]);
		buf.writeBoolean(properlyActivated);
		buf.writeInt(oreCapacity);
		buf.writeInt(slagTank);
		buf.writeByte(componentTiers.length);
		for(int i = 0; i < componentTiers.length; i++)
			buf.writeDouble(componentTiers[i]);
		if(useInventory)
		{
			byte[] b = ItemStacktoByteArray(inputStacks);
			buf.writeInt(b.length);
			buf.writeInt(inputStacks.length);
			buf.writeBytes(b);
			byte[] bb = ItemStacktoByteArray(outputStacks);
			buf.writeInt(bb.length);
			buf.writeInt(outputStacks.length);
			buf.writeBytes(bb);
		}
		buf.writeInt(fuelStack);
		buf.writeInt(numQueueJobs);
		buf.writeInt(grossCookTime);
		buf.writeInt(grossMaxCookTime);
		buf.writeInt(maxFuelCapacity);
		buf.writeBoolean(passFuel);
		buf.writeInt(queue.size());
		for(MultiblockWorkQueueItem item : queue)
		{
			byte[] qb = queueItemtoBytes(item);
			buf.writeInt(qb.length);
			buf.writeBytes(qb);
		}
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		    ObjectOutputStream out = new ObjectOutputStream(byteOut);
		    out.writeObject(oreSlagInQueue);
		    byte[] ob = byteOut.toByteArray();
		    buf.writeInt(ob.length);
		    buf.writeBytes(ob);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private byte[] ItemStacktoByteArray(ItemStack[] is)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < is.length; i++)
		{
			if(is[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Slot", i);
				is[i].writeToNBT(tag);
				list.appendTag(tag);
			}
		}
		nbt.setTag("Items", list);
		
		byte[] bytearray = null;
		try {
			bytearray = CompressedStreamTools.compress(nbt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytearray;
	}
	
	private ItemStack[] ByteArraytoItemStack(byte[] bytes, int itemCount)
	{
		ItemStack[] is = new ItemStack[itemCount];
		try {
			NBTTagCompound nbt = CompressedStreamTools.func_152457_a(bytes, NBTSizeTracker.field_152451_a);
			
			NBTTagList list = nbt.getTagList("Items", 0);
			
			for(int i = 0; i < itemCount; i++)
			{
				NBTTagCompound tag = list.getCompoundTagAt(i);
				int slot = tag.getInteger("Slot");
				if(slot >= 0 && slot < is.length)
					is[i] = ItemStack.loadItemStackFromNBT(tag);
			}
			return is;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private MultiblockWorkQueueItem queueItemfromBytes(byte[] bArray)
	{
		MultiblockWorkQueueItem item = new MultiblockWorkQueueItem();
		try {
			NBTTagCompound nbt = CompressedStreamTools.func_152457_a(bArray, NBTSizeTracker.field_152451_a);
			item.retreiveNBT(nbt);
			return item;
		} catch (IOException e) {
		e.printStackTrace();
		}
		return null;
	}
	
	private byte[] queueItemtoBytes(MultiblockWorkQueueItem item)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		item.generateNBT(nbt);
		byte[] bytearray = null;
		try {
			bytearray = CompressedStreamTools.compress(nbt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytearray;
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