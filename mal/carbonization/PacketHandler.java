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
			
			if(player instanceof EntityClientPlayerMP)
			{
				World world = ((EntityClientPlayerMP)player).worldObj;
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if(te instanceof TileEntityFurnaces)
				{
					//System.out.println("And it's ours!");
					TileEntityFurnaces cte = (TileEntityFurnaces)te;
					cte.setFacing(facing);
					cte.blockMetadata = metadata;
					cte.furnaceBurnTime = burntime;
					cte.furnaceCookTime = cooktime;
					cte.furnaceCookTimeMultiplyer = multiplyer;
					cte.handlePacketData(items);
				}
			}
		}
	}
	
	public static Packet getPacket(TileEntityFurnaces te)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		
		int x = te.xCoord;
		int y = te.yCoord;
		int z = te.zCoord;
		int metadata = te.blockMetadata;
		int burntime = te.furnaceBurnTime;
		int cooktime = te.furnaceCookTime;
		double multiplyer = te.furnaceCookTimeMultiplyer;
		byte facing = te.getFacing();
		int[] items = te.buildIntList();
		
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(metadata);
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
			System.out.println("UNPOSSIBLE!");
		}
		
		Packet250CustomPayload pak = new Packet250CustomPayload();
		pak.channel = "CarbonizationChn";
		pak.data = bos.toByteArray();
		pak.length = bos.size();
		pak.isChunkDataPacket = true;
		return pak;
	}

}
