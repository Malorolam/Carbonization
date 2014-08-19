package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityTunnelBore;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TunnelBoreMessageClient implements IMessage, IMessageHandler<TunnelBoreMessageClient, IMessage> {

	public int xpos, ypos, zpos;
	public boolean hollowScaffold;
	
	public TunnelBoreMessageClient(){}
	public TunnelBoreMessageClient(TileEntityTunnelBore te, boolean hs)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		hollowScaffold = hs;
	}
	@Override
	public IMessage onMessage(TunnelBoreMessageClient message,MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityTunnelBore)
		{
			((TileEntityTunnelBore)te).hollowScaffold = message.hollowScaffold;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		hollowScaffold = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeBoolean(hollowScaffold);
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