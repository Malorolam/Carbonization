package mal.carbonization.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import mal.carbonization.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
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
		try {
			if(packet.channel.equals("CarbonizationChn"))
			{
				if(packet.data == null)
					System.out.println("PACKET DATA NULL!");
				ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				int metadata = data.readInt();
				
				//System.out.println("We gots a packet!");
				World world = ((EntityPlayer) player).worldObj;
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
				else if(te instanceof TileEntityMultiblockInit)
				{
					//System.out.println("MultiblockInit packet recieved!");
					int xdiff = data.readInt();
					int ydiff = data.readInt();
					int zdiff = data.readInt();
					boolean activated = data.readBoolean();
					
					TileEntityMultiblockInit mte = (TileEntityMultiblockInit)te;
					mte.blockMetadata = metadata;
					mte.activated = activated;
					mte.xdiff = xdiff;
					mte.ydiff = ydiff;
					mte.zdiff = zdiff;
					if(activated)
					{
						mte.processFunction();
					}
				}
				else if(te instanceof TileEntityMultiblockFurnace)
				{
					//System.out.println("Recieved MultiblockFurnace packet");
					int xsize = data.readInt();
					int ysize = data.readInt();
					int zsize = data.readInt();

					int[] offset = new int[3];
					offset[0] = data.readInt();
					offset[1] = data.readInt();
					offset[2] = data.readInt();

					double[] comptT = new double[2];
					comptT[0] = data.readDouble();
					comptT[1] = data.readDouble();
					
					float fuelTime = data.readFloat();
					int slagVolume = data.readInt();
					
					int grossCookTime = data.readInt();
					int grossMaxCookTime = data.readInt();
					
					boolean passFuel = data.readBoolean();
					
					int mapSize = data.readInt();
					HashMap<String, Integer> oreSlagInQueue = new HashMap<String, Integer>();
					for(int j = 0; j<mapSize; j++)
					{
						String slagTypes = data.readUTF();
						int slagValues = data.readInt();
						oreSlagInQueue.put(slagTypes, slagValues);
					}
					short short1 = data.readShort();
					byte[] abyte = new byte[short1];
					data.readFully(abyte);
					
					
					TileEntityMultiblockFurnace fte = (TileEntityMultiblockFurnace)te;
					fte.xsize = xsize;
					fte.ysize = ysize;
					fte.zsize = zsize;
					fte.offset = offset;
					fte.componentTiers = comptT;
					fte.passFuel = passFuel;
					fte.calculateData();
					fte.slagTank = slagVolume;
					fte.setFuelStack(fuelTime);
					//System.out.println("In: " + grossCookTime + "/" + grossMaxCookTime + " Remote: " + fte.worldObj.isRemote);
					fte.setGrossCookTime(grossCookTime);
					fte.setGrossMaxCookTime(grossMaxCookTime);
					fte.loadInventory(CompressedStreamTools.decompress(abyte));
					fte.setOreMap(oreSlagInQueue);
					
				}
				else if(te instanceof TileEntityAutocraftingBench)
				{
					double fueltank = data.readDouble();
					int fueluse = data.readInt();
					double upgrade = data.readDouble();
					int process = data.readInt();
					int cooldown = data.readInt();
					
					short short1 = data.readShort();
					byte[] abyte = new byte[short1];
					data.readFully(abyte);
					
					TileEntityAutocraftingBench ate = (TileEntityAutocraftingBench) te;
					ate.updating = true;
					
					ate.fuelTank = fueltank;
					ate.fuelUsePercent = fueluse;
					ate.upgradeTier = upgrade;
					ate.processTime = process;
					ate.craftingCooldown = cooldown;
					ate.loadInventory(CompressedStreamTools.decompress(abyte));
					ate.updating = false;
				}
				else if(te instanceof TileEntityFuelConverter)
				{
					double fueltank = data.readDouble();
					double effupgrade = data.readDouble();
					double spupgrade = data.readDouble();
					double pottank = data.readDouble();
					boolean makeDust = data.readBoolean();
					int process = data.readInt();
					int cooldown = data.readInt();
					int currentIndex = data.readInt();
					
					short short1 = data.readShort();
					byte[] abyte = new byte[short1];
					data.readFully(abyte);
					
					TileEntityFuelConverter ate = (TileEntityFuelConverter) te;
					
					ate.fuelTank = fueltank;
					ate.efficiencyUpgrade = effupgrade;
					ate.speedUpgrade = spupgrade;
					ate.potentialTank = pottank;
					ate.processTime = process;
					ate.craftingCooldown = cooldown;
					ate.makeDust = makeDust;
					ate.currentIndex = currentIndex;
					ate.loadInventory(CompressedStreamTools.decompress(abyte));
					ate.calculateProcessTime();
				}
				else if(te instanceof TileEntityFuelCellFiller)
				{
					int craftingcooldown = data.readInt();
					int processtime = data.readInt();
					double speedupgrade = data.readDouble();
					int itemSize = data.readInt();
					int[] items = new int[itemSize];
					for(int i = 0; i < itemSize; i++)
					{
						items[i] = data.readInt();
					}
					
					TileEntityFuelCellFiller ate = (TileEntityFuelCellFiller) te;
					ate.craftingCooldown = craftingcooldown;
					ate.processTime = processtime;
					ate.speedUpgrade = speedupgrade;
					ate.recoverIntList(items);
					ate.calculateProcessTime();
				}
/*				else
				{
					System.out.println("Got a packet for TileEntity: " + (te==null?"null":te.getClass().toString()) + ".");
				}*/
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		
		else if(te instanceof TileEntityMultiblockInit)
		{
			//System.out.println("Writing Multiblockinit packet.");
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
		else if(te instanceof TileEntityMultiblockFurnace)
		{
	    	//System.out.println("Making MultiblockFurnace Packet");
			TileEntityMultiblockFurnace fte = (TileEntityMultiblockFurnace) te;
			int xsize = fte.xsize;
			int ysize = fte.ysize;
			int zsize = fte.zsize;

			int ox = fte.offset[0];
			int oy = fte.offset[1];
			int oz = fte.offset[2];

			double ctx = fte.componentTiers[0];
			double cty = fte.componentTiers[1];
			
			float fuelStack = fte.getFuelStack();
			int slagVolume = fte.slagTank;
			
			int grossCookTime = fte.getGrossCookTime();
			int grossMaxCookTime = fte.getGrossMaxCookTime();
			
			boolean passFuel = fte.passFuel;
			//System.out.println(passFuel);
			
/*			int[] items = fte.buildIntList();
			int itemSize = items.length;*/
			
			String[] slagTypes = new String[fte.getOreMap().keySet().size()];
			for(int i = 0; i < slagTypes.length; i++)
			{
				slagTypes[i] = (String) fte.getOreMap().keySet().toArray()[i];
			}
			int[] slagValues = new int[fte.getOreMap().keySet().size()];
			for(int j = 0; j<slagValues.length; j++)
			{
				slagValues[j] = (Integer) fte.getOreMap().values().toArray()[j];
			}
			//System.out.println("Out: " + grossCookTime + "/" + grossMaxCookTime);
			
			try
			{
				dos.writeInt(xsize);
				dos.writeInt(ysize);
				dos.writeInt(zsize);
				dos.writeInt(ox);
				dos.writeInt(oy);
				dos.writeInt(oz);
				dos.writeDouble(ctx);
				dos.writeDouble(cty);
				dos.writeFloat(fuelStack);
				dos.writeInt(slagVolume);
				dos.writeInt(grossCookTime);
				dos.writeInt(grossMaxCookTime);
				dos.writeBoolean(passFuel);
				
				dos.writeInt(slagTypes.length);
				for(int j = 0; j<slagTypes.length; j++)
				{
					dos.writeUTF(slagTypes[j]);
					dos.writeInt(slagValues[j]);
				}
				byte[] abyte = CompressedStreamTools.compress(fte.saveInventory());
				dos.writeShort((short)abyte.length);
				dos.write(abyte);
				
			}
			catch (IOException e)
			{
				System.out.println("HODOR HODOR");
			}
		}
		else if(te instanceof TileEntityAutocraftingBench)
		{
			TileEntityAutocraftingBench ate = (TileEntityAutocraftingBench) te;
			ate.updating = true;
			double fueltank = ate.fuelTank;
			int fueluse = ate.fuelUsePercent;
			double upgrade = ate.upgradeTier;
			int process = ate.processTime;
			int cooldown = ate.craftingCooldown;
			
			try
			{
				dos.writeDouble(fueltank);
				dos.writeInt(fueluse);
				dos.writeDouble(upgrade);
				dos.writeInt(process);
				dos.writeInt(cooldown);
				byte[] abyte = CompressedStreamTools.compress(ate.saveInventory());
				dos.writeShort((short)abyte.length);
				dos.write(abyte);
			}
			catch(IOException e)
			{
				System.out.println("RAAWR");
			}
			
			ate.updating = false;
		}
		else if(te instanceof TileEntityFuelConverter)
		{
			TileEntityFuelConverter ate = (TileEntityFuelConverter) te;
			double fueltank = ate.fuelTank;
			double effupgrade = ate.efficiencyUpgrade;
			double pottank = ate.potentialTank;
			double spupgrade = ate.speedUpgrade;
			int process = ate.processTime;
			int cooldown = ate.craftingCooldown;
			boolean makeDust = ate.makeDust;
			int currentIndex = ate.currentIndex;
			
			try
			{
				dos.writeDouble(fueltank);
				dos.writeDouble(effupgrade);
				dos.writeDouble(spupgrade);
				dos.writeDouble(pottank);
				dos.writeBoolean(makeDust);
				dos.writeInt(process);
				dos.writeInt(cooldown);
				dos.writeInt(currentIndex);
				byte[] abyte = CompressedStreamTools.compress(ate.saveInventory());
				dos.writeShort((short)abyte.length);
				dos.write(abyte);
			}
			catch(IOException e)
			{
				System.out.println("RAAWR");
			}
		}
		else if(te instanceof TileEntityFuelCellFiller)
		{
			TileEntityFuelCellFiller ate = (TileEntityFuelCellFiller) te;
			int craftingcooldown = ate.craftingCooldown;
			int processtime = ate.processTime;
			double speedupgrade = ate.speedUpgrade;
			int[] items = ate.buildIntList();
			int itemsize = items.length;
			
			try
			{
				dos.writeInt(craftingcooldown);
				dos.writeInt(processtime);
				dos.writeDouble(speedupgrade);
				dos.writeInt(itemsize);
				
				for(int i = 0; i < itemsize; i++)
				{
					dos.writeInt(items[i]);
				}
			}
			catch(IOException e)
			{
				System.out.println("RAWR.RAR");
			}
		}
		else
		{
			System.out.println("Trying to make packet for TileEntity: " + te.getClass().toString() + ".");
		}
		
		Packet250CustomPayload pak = new Packet250CustomPayload();
		pak.channel = "CarbonizationChn";
		pak.data = bos.toByteArray();
		pak.length = bos.size();
		pak.isChunkDataPacket = true;
		
		if(pak.data == null)
		{
			System.out.println("ERROR ERROR DATA NULL!");
		}
		//System.out.println("happy packet is happy.");
		return pak;
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