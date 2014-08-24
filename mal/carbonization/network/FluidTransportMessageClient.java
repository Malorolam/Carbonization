package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import mal.carbonization.tileentity.TileEntityFluidTransport;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FluidTransportMessageClient implements IMessage, IMessageHandler<FluidTransportMessageClient, IMessage>{

	public int xpos, ypos, zpos;
	public byte side;
	
	public FluidTransportMessageClient(){}
	public FluidTransportMessageClient(TileEntityFluidTransport te, byte side)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		this.side = side;
	}
	@Override
	public IMessage onMessage(FluidTransportMessageClient message,
			MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityFluidTransport)
			((TileEntityFluidTransport)te).changeDirectionState(message.side);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		side = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeByte(side);
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