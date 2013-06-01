package mal.carbonization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.channel.equals("CarbonizationChn"))
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
		
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			int metadata = data.readInt();
			
			if(player instanceof EntityClientPlayerMP)
			{
				System.out.println("We gots a packet!");
				World world = ((EntityClientPlayerMP)player).worldObj;
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if(te instanceof TileEntityFurnaces)
				{
					int burntime = data.readInt();
					int cooktime = data.readInt();
					byte facing = data.readByte();
					double multiplyer = data.readDouble();
					byte hasValue = data.readByte();
					int[] items = new int[0];
					if(hasValue == 1)
					{
						items = new int[9];
						for(int i = 0; i < items.length; i++)
						{
							items[i] = data.readInt();
						}
					}
					//System.out.println("And it's ours!");
					TileEntityFurnaces cte = (TileEntityFurnaces)te;
					cte.setFacing(facing);
					cte.blockMetadata = metadata;
					cte.furnaceBurnTime = burntime;
					cte.furnaceCookTime = cooktime;
					cte.furnaceCookTimeMultiplyer = multiplyer;
					cte.handlePacketData(items);
				}
				
				if(te instanceof TileEntityMultiblockInit)
				{
					System.out.println("MultiblockInit packet recieved!");
					int xdiff = data.readInt();
					int ydiff = data.readInt();
					int zdiff = data.readInt();
					boolean activated = data.readBoolean();
					
					TileEntityMultiblockInit mte = (TileEntityMultiblockInit)te;
					mte.blockMetadata = metadata;
					mte.activated = activated;
					mte.initilizeFunction(xdiff, ydiff, zdiff);
					if(activated)
					{
						mte.processFunction();
					}
				}
			}
		}
	}
	
	public static Packet getPacket(TileEntity te)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		
		int x = te.xCoord;
		int y = te.yCoord;
		int z = te.zCoord;
		int metadata = te.blockMetadata;
		
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(metadata);
		}
		catch (IOException e)
		{
			System.out.println("UNPOSSIBLE!");
		}
		
		if(te instanceof TileEntityFurnaces)
		{
			int burntime = ((TileEntityFurnaces)te).furnaceBurnTime;
			int cooktime = ((TileEntityFurnaces)te).furnaceCookTime;
			double multiplyer = ((TileEntityFurnaces)te).furnaceCookTimeMultiplyer;
			byte facing = ((TileEntityFurnaces)te).getFacing();
			int[] items = ((TileEntityFurnaces)te).buildIntList();
			
			try
			{
				dos.writeInt(burntime);
				dos.writeInt(cooktime);
				dos.writeByte(facing);
				dos.writeDouble(multiplyer);
				dos.writeByte((items!=null)?1:0);
				if(items!= null)
				{
					for(int i = 0; i<9; i++)
					{
						dos.writeInt(items[i]);
					}
				}
			}
			catch (IOException e)
			{
				System.out.println("HERPA DERPA");
			}
		}
		
		if(te instanceof TileEntityMultiblockInit)
		{
			System.out.println("Writing Multiblockinit packet.");
			int xdiff = ((TileEntityMultiblockInit)te).xdiff;
			int ydiff = ((TileEntityMultiblockInit)te).ydiff;
			int zdiff = ((TileEntityMultiblockInit)te).zdiff;
			boolean activate = ((TileEntityMultiblockInit)te).activated;
			
			try
			{
				dos.writeInt(xdiff);
				dos.writeInt(ydiff);
				dos.writeInt(zdiff);
				dos.writeBoolean(activate);
			}
			catch (IOException e)
			{
				System.out.println("HURRR DURRR");
			}
		}
		
		Packet250CustomPayload pak = new Packet250CustomPayload();
		pak.channel = "CarbonizationChn";
		pak.data = bos.toByteArray();
		pak.length = bos.size();
		pak.isChunkDataPacket = true;
		return pak;
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