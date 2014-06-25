package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MultiblockInitMessage implements IMessage, IMessageHandler<MultiblockInitMessage, IMessage>{

	public int xpos, ypos, zpos, xdiff, ydiff, zdiff;
	public boolean activated;
	public String type;
	
	public MultiblockInitMessage(){}
	
	public MultiblockInitMessage(TileEntityMultiblockInit te)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		xdiff = te.xdiff;
		ydiff = te.ydiff;
		zdiff = te.zdiff;
		activated = te.activated;
		type = te.type;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		xdiff = buf.readInt();
		ydiff = buf.readInt();
		zdiff = buf.readInt();
		activated = buf.readBoolean();
		int typelength = buf.readInt();
		type = new String(buf.readBytes(typelength).array());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeInt(xdiff);
		buf.writeInt(ydiff);
		buf.writeInt(zdiff);
		buf.writeBoolean(activated);
		buf.writeBytes(type.getBytes());
	}
	@Override
	public IMessage onMessage(MultiblockInitMessage message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityMultiblockInit && ((TileEntityMultiblockInit)te).type.equalsIgnoreCase(message.type))
		{
			((TileEntityMultiblockInit)te).xdiff = message.xdiff;
			((TileEntityMultiblockInit)te).ydiff = message.ydiff;
			((TileEntityMultiblockInit)te).zdiff = message.zdiff;
			((TileEntityMultiblockInit)te).activated = message.activated;
		}
		
		return null;
	}

}
