package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class StructureBlockMessage implements IMessage, IMessageHandler<StructureBlockMessage, IMessage>{

	int xpos, ypos, zpos;
	int mx, my, mz;
	boolean loaded;
	
	public StructureBlockMessage(){}
	
	public StructureBlockMessage(TileEntityStructureBlock te)
	{

	}
	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}
	@Override
	public IMessage onMessage(StructureBlockMessage message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityStructureBlock)
		{
			((TileEntityStructureBlock)te).mx = message.mx;
			((TileEntityStructureBlock)te).my = message.my;
			((TileEntityStructureBlock)te).mz = message.mz;
			((TileEntityStructureBlock)te).loaded = message.loaded;
		}
		
		return null;
	}

}
