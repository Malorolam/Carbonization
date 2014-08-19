package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/*
 * Message to sync client gui data to the server
 * Only sends things like button states, inventory is synced through the container and the queue is managed serverside only
 */
public class MultiblockFurnaceMessageClient implements IMessage, IMessageHandler<MultiblockFurnaceMessageClient, IMessage>{

	public boolean passFuel;
	public int xpos, ypos, zpos;
	
	public MultiblockFurnaceMessageClient(){}
	
	public MultiblockFurnaceMessageClient(TileEntityMultiblockFurnace furnace)
	{
		passFuel = furnace.passFuel;
		xpos = furnace.xCoord;
		ypos = furnace.yCoord;
		zpos = furnace.zCoord;
	}
	
	@Override
	public IMessage onMessage(MultiblockFurnaceMessageClient message,MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityMultiblockFurnace)
		{
			TileEntityMultiblockFurnace fte = (TileEntityMultiblockFurnace)te;
			
			fte.passFuel = message.passFuel;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		passFuel = buf.readBoolean();
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(passFuel);
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
	}

}
