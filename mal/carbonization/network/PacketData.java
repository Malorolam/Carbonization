package mal.carbonization.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.Player;

import mal.carbonization.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

//helper to make the packet handler not as large and messy
public class PacketData {

	//construct a packet
	public Packet generatePacket(TileEntity te, String type)
	{
		//Basic stuff in every packet to identify it
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
			dos.writeUTF(type);
			dos.writeUTF(te.toString().split("@")[0]);
			//System.out.println("Write: " + te.toString() + ": " + type);


			//tile entity specific things
			if(te instanceof TileEntityFurnaces)
				getFurnacePacket((TileEntityFurnaces)te, type, dos);
			if(te instanceof TileEntityMultiblockInit)
				getMultiblockInitPacket((TileEntityMultiblockInit)te, type, dos);
			if(te instanceof TileEntityMultiblockFurnace)
				getMultiblockFurnacePacket((TileEntityMultiblockFurnace)te, type, dos);
			if(te instanceof TileEntityAutocraftingBench)
				getAutocraftingBenchPacket((TileEntityAutocraftingBench)te, type, dos);
			if(te instanceof TileEntityFuelConverter)
				getFuelConverterPacket((TileEntityFuelConverter)te, type, dos);
			if(te instanceof TileEntityFuelCellFiller)
				getFuelCellFillerPacket((TileEntityFuelCellFiller)te, type, dos);
			if(te instanceof TileEntityTunnelBore)
				getTunnelBorePacket((TileEntityTunnelBore)te, type, dos);



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
		catch (IOException e)
		{
			System.out.println("UNPOSSIBLE!");
		}

		return null;
	}

	public void processPacket(ByteArrayDataInput data, Player player)
	{
		try {

			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			int metadata = data.readInt();
			String type = data.readUTF();
			String name = data.readUTF();

			World world = ((EntityPlayer) player).worldObj;
			TileEntity te = world.getBlockTileEntity(x, y, z);
			String rname = te.toString().split("@")[0];
			//System.out.println("Read: " + te.toString() + ": " + type);
			if(name.equals(rname))
			{

				//tile entity specific things
				if(te instanceof TileEntityFurnaces)
					setFurnacePacket((TileEntityFurnaces)te, data, player, metadata, type);
				if(te instanceof TileEntityMultiblockInit)
					setMultiblockInitPacket((TileEntityMultiblockInit)te, data, player, metadata, type);
				if(te instanceof TileEntityMultiblockFurnace)
					setMultiblockFurnacePacket((TileEntityMultiblockFurnace)te, data, player, metadata, type);
				if(te instanceof TileEntityAutocraftingBench)
					setAutocraftingBenchPacket((TileEntityAutocraftingBench)te, data, player, metadata, type);
				if(te instanceof TileEntityFuelConverter)
					setFuelConverterPacket((TileEntityFuelConverter)te, data, player, metadata, type);
				if(te instanceof TileEntityFuelCellFiller)
					setFuelCellFillerPacket((TileEntityFuelCellFiller)te, data, player, metadata, type);
				if(te instanceof TileEntityTunnelBore)
					setTunnelBorePacket((TileEntityTunnelBore)te, data, player, metadata, type);

			}
			else
			{
				FMLLog.log(Level.WARNING, "Packet expecting tile entity of type: " + name + " and found tile entity of type: " + rname + " at location" +
						x + ", " + y + ", " + z +".  Don't be alarmed unless this is happening a lot.");
			}
		}
		catch(Exception e)
		{
			//FMLLog.log(Level.SEVERE, "Carbonization Packet Broke Somewhere.  If this persists, contact Mal about it.");

		}

	}

	private void setFurnacePacket(TileEntityFurnaces te, ByteArrayDataInput data, Player player, int metadata, String type)
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
		te.handlePacketData(items);
		//System.out.println("And it's ours!");
		te.setFacing(facing);
		te.blockMetadata = metadata;
		te.furnaceBurnTime = burntime;
		te.furnaceCookTime = cooktime;
		te.furnaceCookTimeMultiplyer = multiplyer;
	}

	private void setMultiblockInitPacket(TileEntityMultiblockInit te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		//System.out.println("MultiblockInit packet recieved!");
		int xdiff = data.readInt();
		int ydiff = data.readInt();
		int zdiff = data.readInt();
		boolean activated = data.readBoolean();

		te.blockMetadata = metadata;
		te.activated = activated;
		te.xdiff = xdiff;
		te.ydiff = ydiff;
		te.zdiff = zdiff;
		if(activated)
		{
			te.processFunction();
		}
	}

	private void setMultiblockFurnacePacket(TileEntityMultiblockFurnace te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		//System.out.println("Recieved MultiblockFurnace packet");
		if(!type.equalsIgnoreCase("passonly"))
		{
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

			te.xsize = xsize;
			te.ysize = ysize;
			te.zsize = zsize;
			te.offset = offset;
			te.componentTiers = comptT;
			te.slagTank = slagVolume;
			te.setFuelStack(fuelTime);
			te.setGrossCookTime(grossCookTime);
			te.setGrossMaxCookTime(grossMaxCookTime);
			te.calculateData();
		}

		boolean passFuel = data.readBoolean();

		if(!type.equalsIgnoreCase("passonly"))
		{
			int mapSize = data.readInt();
			HashMap<String, Integer> oreSlagInQueue = new HashMap<String, Integer>();
			for(int j = 0; j<mapSize; j++)
			{
				String slagTypes = data.readUTF();
				int slagValues = data.readInt();
				oreSlagInQueue.put(slagTypes, slagValues);
			}

			te.setOreMap(oreSlagInQueue);

			if(!type.equalsIgnoreCase("noinventory"))
			{
				int short1 = data.readInt();
				byte[] abyte = new byte[short1];
				data.readFully(abyte);

				try {
					te.loadInventory(CompressedStreamTools.decompress(abyte));
				}
				catch(Exception e)
				{
					System.out.println("AAAAAAHHHH!!!");
				}
			}
		}


		te.passFuel = passFuel;
	}

	private void setAutocraftingBenchPacket(TileEntityAutocraftingBench te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		double fueltank = data.readDouble();
		int fueluse = data.readInt();
		double upgrade = data.readDouble();
		int process = data.readInt();
		int cooldown = data.readInt();

		if(!type.equalsIgnoreCase("noinventory"))
		{
			int short1 = data.readInt();
			byte[] abyte = new byte[short1];
			data.readFully(abyte);
			try {
				te.loadInventory(CompressedStreamTools.decompress(abyte));
			}
			catch(Exception e)
			{
				System.out.println("ERROR ERROR WILL ROBINSON!");
			}
		}

		te.updating = true;

		te.fuelTank = fueltank;
		te.fuelUsePercent = fueluse;
		te.upgradeTier = upgrade;
		te.processTime = process;
		te.craftingCooldown = cooldown;
		te.updating = false;
	}

	private void setFuelConverterPacket(TileEntityFuelConverter te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		double fueltank = data.readDouble();
		double effupgrade = data.readDouble();
		double spupgrade = data.readDouble();
		double pottank = data.readDouble();
		//boolean makeDust = data.readBoolean();
		int process = data.readInt();
		int cooldown = data.readInt();
		int currentIndex = data.readInt();
		String currentTag = data.readUTF();

		if(!type.equalsIgnoreCase("noinventory"))
		{
			int short1 = data.readInt();
			byte[] abyte = new byte[short1];
			data.readFully(abyte);
			try {
				te.loadInventory(CompressedStreamTools.decompress(abyte));
			}
			catch(Exception e)
			{

			}
		}

		te.fuelTank = fueltank;
		te.efficiencyUpgrade = effupgrade;
		te.speedUpgrade = spupgrade;
		te.potentialTank = pottank;
		te.processTime = process;
		te.craftingCooldown = cooldown;
		//te.makeDust = makeDust;
		te.currentIndex = currentIndex;
		te.currentTag = currentTag;
		te.calculateProcessTime();
	}

	private void setFuelCellFillerPacket(TileEntityFuelCellFiller te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		int craftingcooldown = data.readInt();
		int processtime = data.readInt();
		double speedupgrade = data.readDouble();

		TileEntityFuelCellFiller ate = (TileEntityFuelCellFiller) te;

		if(!type.equalsIgnoreCase("noinventory"))
		{
			int itemSize = data.readInt();
			byte[] abyte = new byte[itemSize];
			data.readFully(abyte);
			try {
				ate.loadInventory(CompressedStreamTools.decompress(abyte));
			}
			catch(Exception e)
			{}
		}

		ate.craftingCooldown = craftingcooldown;
		ate.processTime = processtime;
		ate.speedUpgrade = speedupgrade;
		ate.calculateProcessTime();
	}

	private void setTunnelBorePacket(TileEntityTunnelBore te, ByteArrayDataInput data, Player player, int metadata, String type)
	{
		//TODO:Finish
		//System.out.println("packet set");
		te.xSize = data.readInt();
		te.ySize = data.readInt();
		te.cycles = data.readInt();
		te.digCooldown = data.readInt();
		te.digFortuneLevel = data.readInt();
		te.maxDigCooldown = data.readInt();
		te.fuelTime = data.readDouble();
		te.fuelMultiplyer = data.readDouble();
		te.hollowScaffold = data.readBoolean();
		te.digSilkTouch = data.readBoolean();
		te.digIgnoreHardness = data.readBoolean();

		if(!type.equalsIgnoreCase("noinventory"))
		{
			int itemSize = data.readInt();
			byte[] abyte = new byte[itemSize];
			data.readFully(abyte);
			try {
				te.loadInventory(CompressedStreamTools.decompress(abyte));
			}
			catch(Exception e)
			{}
		}
	}

	private void getFurnacePacket(TileEntityFurnaces te, String type, DataOutputStream dos)
	{
		int burntime = te.furnaceBurnTime;
		int cooktime = te.furnaceCookTime;
		double multiplyer = te.furnaceCookTimeMultiplyer;
		byte facing = te.getFacing();
		int[] items = te.buildIntList();

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

	private void getMultiblockInitPacket(TileEntityMultiblockInit te, String type, DataOutputStream dos)
	{
		int xdiff = te.xdiff;
		int ydiff = te.ydiff;
		int zdiff = te.zdiff;
		boolean activate = te.activated;

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

	/*
	 * types:
	 * passonly: only send passFuel value
	 * noinventory: no inventories
	 */
	private void getMultiblockFurnacePacket(TileEntityMultiblockFurnace te, String type, DataOutputStream dos)
	{
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
			if(!type.equalsIgnoreCase("passonly"))
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
			}

			dos.writeBoolean(passFuel);

			if(!type.equalsIgnoreCase("passonly"))
			{
				dos.writeInt(slagTypes.length);
				for(int j = 0; j<slagTypes.length; j++)
				{
					dos.writeUTF(slagTypes[j]);
					dos.writeInt(slagValues[j]);
				}

				if(!type.equalsIgnoreCase("noinventory"))
				{
					byte[] abyte = CompressedStreamTools.compress(fte.saveInventory());
					dos.writeInt(abyte.length);
					dos.write(abyte);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("HODOR HODOR");
		}
	}

	/*
	 * types:
	 * noinventory: no inventory
	 */
	private void getAutocraftingBenchPacket(TileEntityAutocraftingBench te, String type, DataOutputStream dos)
	{
		te.updating = true;
		double fueltank = te.fuelTank;
		int fueluse = te.fuelUsePercent;
		double upgrade = te.upgradeTier;
		int process = te.processTime;
		int cooldown = te.craftingCooldown;

		try
		{
			dos.writeDouble(fueltank);
			dos.writeInt(fueluse);
			dos.writeDouble(upgrade);
			dos.writeInt(process);
			dos.writeInt(cooldown);

			if(!type.equalsIgnoreCase("noinventory"))
			{
				byte[] abyte = CompressedStreamTools.compress(te.saveInventory());
				dos.writeInt(abyte.length);
				dos.write(abyte);
			}
		}
		catch(IOException e)
		{
			System.out.println("RAAWR");
		}

		te.updating = false;
	}

	/*
	 * types:
	 * noinventory: no inventories
	 */
	private void getFuelConverterPacket(TileEntityFuelConverter te, String type, DataOutputStream dos)
	{
		double fueltank = te.fuelTank;
		double effupgrade = te.efficiencyUpgrade;
		double pottank = te.potentialTank;
		double spupgrade = te.speedUpgrade;
		int process = te.processTime;
		int cooldown = te.craftingCooldown;
		//boolean makeDust = te.makeDust;
		int currentIndex = te.currentIndex;
		String currentTag = te.currentTag;

		try
		{
			dos.writeDouble(fueltank);
			dos.writeDouble(effupgrade);
			dos.writeDouble(spupgrade);
			dos.writeDouble(pottank);
			//dos.writeBoolean(makeDust);
			dos.writeInt(process);
			dos.writeInt(cooldown);
			dos.writeInt(currentIndex);
			dos.writeUTF(currentTag);

			if(!type.equalsIgnoreCase("noinventory"))
			{
				byte[] abyte = CompressedStreamTools.compress(te.saveInventory());
				dos.writeInt(abyte.length);
				dos.write(abyte);
			}
		}
		catch(IOException e)
		{
			System.out.println("RAAWR");
		}
	}

	/*
	 * types:
	 * noinventory: no inventories
	 */
	private void getFuelCellFillerPacket(TileEntityFuelCellFiller te, String type, DataOutputStream dos)
	{
		int craftingcooldown = te.craftingCooldown;
		int processtime = te.processTime;
		double speedupgrade = te.speedUpgrade;

		try
		{
			dos.writeInt(craftingcooldown);
			dos.writeInt(processtime);
			dos.writeDouble(speedupgrade);

			if(!type.equalsIgnoreCase("noinventory"))
			{
				byte[] abyte = CompressedStreamTools.compress(te.saveInventory());
				dos.writeInt(abyte.length);
				dos.write(abyte);
			}
		}
		catch(IOException e)
		{
			System.out.println("RAWR.RAR");
		}
	}

	private void getTunnelBorePacket(TileEntityTunnelBore te, String type, DataOutputStream dos)
	{
		//TODO: finish
		//System.out.println("packet get");
		try
		{
			dos.writeInt(te.xSize);
			dos.writeInt(te.ySize);
			dos.writeInt(te.cycles);
			dos.writeInt(te.digCooldown);
			dos.writeInt(te.digFortuneLevel);
			dos.writeInt(te.maxDigCooldown);
			dos.writeDouble(te.fuelTime);
			dos.writeDouble(te.fuelMultiplyer);
			dos.writeBoolean(te.hollowScaffold);
			dos.writeBoolean(te.digSilkTouch);
			dos.writeBoolean(te.digIgnoreHardness);

			if(!type.equalsIgnoreCase("noinventory"))
			{
				byte[] abyte = CompressedStreamTools.compress(te.saveInventory());
				dos.writeInt(abyte.length);
				dos.write(abyte);
			}
		}
		catch(IOException e)
		{
			System.out.println("RAR.RAWR");
		}
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