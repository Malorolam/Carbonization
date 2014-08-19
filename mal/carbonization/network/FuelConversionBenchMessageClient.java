package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityFuelConversionBench;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FuelConversionBenchMessageClient implements IMessage, IMessageHandler<FuelConversionBenchMessageClient, IMessage>{

	public int xpos, ypos, zpos;
	public boolean newState;
	public int dindex;
	
	public FuelConversionBenchMessageClient(){}
	public FuelConversionBenchMessageClient(TileEntityFuelConversionBench te, boolean b, int index)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		newState = b;
		dindex = index;
	}
	@Override
	public IMessage onMessage(FuelConversionBenchMessageClient message,MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityFuelConversionBench)
			((TileEntityFuelConversionBench)te).setState(message.newState, message.dindex);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		newState = buf.readBoolean();
		dindex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeBoolean(newState);
		buf.writeInt(dindex);
	}

}
