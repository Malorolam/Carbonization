package mal.carbonization.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mal.carbonization.items.ItemPortableScanner;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelCellFiller;
import mal.carbonization.tileentity.TileEntityFuelConverter;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

//helper to make the packet handler not as large and messy
public class packetItemData {
	//construct a packet
		public Packet generatePacket(ItemStack item, InventoryPlayer player, String type)
		{
			//Basic stuff in every packet to identify it
			ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
			DataOutputStream dos = new DataOutputStream(bos);
			
			try
			{
				dos.writeUTF(type);
			}
			catch (IOException e)
			{
				System.out.println("UNPOSSIBLE!");
			}
			
			//tile entity specific things
			if(item.getItem() instanceof ItemPortableScanner)
				getScannerPacket(item, type, dos);
			
			
			Packet250CustomPayload pak = new Packet250CustomPayload();
			pak.channel = "CarbonItemChn";
			pak.data = bos.toByteArray();
			pak.length = bos.size();
			pak.isChunkDataPacket = true;
			
			if(pak.data == null)
			{
				System.out.println("ERROR ERROR DATA NULL!");
			}
			return pak;
		}
		
		public void processPacket(ByteArrayDataInput data, InventoryPlayer player, ItemStack stack)
		{
			String type = data.readUTF();
			
			//tile entity specific things
			if(type.equalsIgnoreCase("portablescanner"))
			{
				stack = setScannerPacket(stack, data, type);
			}
		}
		
		private void getScannerPacket(ItemStack item, String type, DataOutputStream dos)
		{
			try
			{
				int mode = ((ItemPortableScanner)item.getItem()).getMode(item);
				dos.writeInt(mode);
			}
			catch(Exception e)
			{
				
			}
		}
		
		private ItemStack setScannerPacket(ItemStack item, ByteArrayDataInput data, String type)
		{
			int mode = data.readInt();
			((ItemPortableScanner)item.getItem()).setMode(item, mode);
			
			return item;
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