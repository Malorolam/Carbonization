package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.core.util.MalLogger;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.server.FMLServerHandler;

public class MultiblockInitMessageServer implements IMessage, IMessageHandler<MultiblockInitMessageServer, IMessage>{

	public int xpos, ypos, zpos, xdiff, ydiff, zdiff;
	public boolean activated;
	public String type;
	
	public MultiblockInitMessageServer(){}
	
	public MultiblockInitMessageServer(TileEntityMultiblockInit te)
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
		byte[] b = type.getBytes();
		buf.writeInt(b.length);
		buf.writeBytes(b);
	}
	@Override
	public IMessage onMessage(MultiblockInitMessageServer message, MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		System.out.println(te.getWorldObj().isRemote);
		
		if(te instanceof TileEntityMultiblockInit && ((TileEntityMultiblockInit)te).type.equalsIgnoreCase(message.type))
		{
			((TileEntityMultiblockInit)te).xdiff = message.xdiff;
			((TileEntityMultiblockInit)te).ydiff = message.ydiff;
			((TileEntityMultiblockInit)te).zdiff = message.zdiff;
			((TileEntityMultiblockInit)te).activated = message.activated;
			if(message.activated)
			{
				((TileEntityMultiblockInit)te).processFunction();
			}
		}
		
		return null;
	}

}
