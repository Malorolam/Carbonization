package mal.carbonization.network;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityFuelConversionBench;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FuelConversionBenchMessageServer implements IMessage, IMessageHandler<FuelConversionBenchMessageServer, IMessage>{

	public int xpos, ypos, zpos;
	public boolean fuelState;//false=solid->liquid, true=liquid->solid
	public int currentIndex;
	public int craftingCooldown;//cooldown until next process
	public int maxCooldown;//max cooldown
	public double bonusYield;
	public boolean voidUpgrade;
	public int ejectSide;
	public int fortuneLevel;
	public int fuelStack;
	public int maxFuel;
	public ItemStack[] inventoryStacks;
	public ItemStack[] upgradeStacks;
	public boolean inventory;
	
	public FuelConversionBenchMessageServer(){}
	public FuelConversionBenchMessageServer(TileEntityFuelConversionBench te, boolean wInventory)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		fuelState = te.fuelState;
		currentIndex = te.currentIndex;
		craftingCooldown = te.craftingCooldown;
		maxCooldown = te.maxCooldown;
		bonusYield = te.bonusYield;
		voidUpgrade = te.voidUpgrade;
		ejectSide = te.ejectSide;
		fuelStack = te.getFuelStack();
		maxFuel = te.getMaxCapacity();
		if(wInventory)
		{
			inventoryStacks = te.inventoryStacks;
			upgradeStacks = te.upgradeStacks;
		}
		inventory = wInventory;
	}
	
	@Override
	public IMessage onMessage(FuelConversionBenchMessageServer message,MessageContext ctx) {
		if(FMLClientHandler.instance().getWorldClient() == null)
			return null;
		TileEntity te = FMLClientHandler.instance().getWorldClient().getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityFuelConversionBench)
		{
			TileEntityFuelConversionBench fte = (TileEntityFuelConversionBench) te;
			fte.fuelState = message.fuelState;
			fte.currentIndex = message.currentIndex;
			fte.craftingCooldown = message.craftingCooldown;
			fte.maxCooldown = message.maxCooldown;
			fte.bonusYield = message.bonusYield;
			fte.voidUpgrade = message.voidUpgrade;
			fte.ejectSide = message.ejectSide;
			fte.setMaxCapacity(message.maxFuel);
			fte.setFuelStack(message.fuelStack);
			if(message.inventory)
			{
				fte.inventoryStacks = message.inventoryStacks;
				fte.upgradeStacks = message.upgradeStacks;
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		fuelState = buf.readBoolean();
		currentIndex = buf.readInt();
		craftingCooldown = buf.readInt();
		maxCooldown = buf.readInt();
		bonusYield = buf.readDouble();
		voidUpgrade = buf.readBoolean();
		ejectSide = buf.readInt();
		maxFuel = buf.readInt();
		fuelStack = buf.readInt();
		inventory = buf.readBoolean();
		if(inventory)
		{
			int inputsize = buf.readInt();
			int iitemcount = buf.readInt();
			byte[] inputbyte = new byte[inputsize];
			buf.readBytes(inputbyte);
			inventoryStacks = ByteArraytoItemStack(inputbyte, iitemcount);
			
			int outputsize = buf.readInt();
			int oitemcount = buf.readInt();
			byte[] outputbyte = new byte[outputsize];
			buf.readBytes(outputbyte);
			upgradeStacks = ByteArraytoItemStack(outputbyte, oitemcount);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeBoolean(fuelState);
		buf.writeInt(currentIndex);
		buf.writeInt(craftingCooldown);
		buf.writeInt(maxCooldown);
		buf.writeDouble(bonusYield);
		buf.writeBoolean(voidUpgrade);
		buf.writeInt(ejectSide);
		buf.writeInt(maxFuel);
		buf.writeInt(fuelStack);
		buf.writeBoolean(inventory);
		if(inventory)
		{
			byte[] i = ItemStacktoByteArray(inventoryStacks);
			buf.writeInt(i.length);
			buf.writeInt(inventoryStacks.length);
			buf.writeBytes(i);
			byte[] o = ItemStacktoByteArray(upgradeStacks);
			buf.writeInt(o.length);
			buf.writeInt(upgradeStacks.length);
			buf.writeBytes(o);
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
}
/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/