package mal.carbonization.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import mal.carbonization.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	private static PacketData packetData = new PacketData();
	private static packetItemData packetItemData = new packetItemData();
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			if(packet.data == null)
			{
				System.out.println("PACKET DATA NULL!");
				return;
			}
			if(packet.channel.equals("CarbonizationChn"))
			{
				ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			
				packetData.processPacket(data, player);
			}
			if(packet.channel.equals("CarbonItemChn"))
			{
				System.out.println("Gotsa happy packet");
				ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
				if(player instanceof EntityPlayer)
				{
					System.out.println("Packet party is packed");
					InventoryPlayer p = ((EntityPlayer)player).inventory;
					packetItemData.processPacket(data, p, p.getCurrentItem());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Packet getItemPacket(ItemStack is, InventoryPlayer player, String type)
	{
		return packetItemData.generatePacket(is, player, type);
	}
	
	public static Packet getPacket(TileEntity te, String type)
	{
		return packetData.generatePacket(te, type);
	}
	
	public static Packet getPacket(TileEntity te)
	{
		return getPacket(te, "nothing");
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