
package mal.carbonization.network;

import mal.carbonization.tileentity.TileEntityFluidTransport;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FluidTransportMessageServer implements IMessage, IMessageHandler<FluidTransportMessageServer, IMessage>{

	public int xpos, ypos, zpos;
	public FluidTank tank = new FluidTank(null,0);
	public int maxcooldown, cooldown, transferAmount;
	public byte[] sidestates;
	
	public FluidTransportMessageServer(){}
	public FluidTransportMessageServer(TileEntityFluidTransport te)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		tank = te.getTank();
		maxcooldown = te.maxTransferDelay;
		cooldown = te.delayCooldown;
		transferAmount = te.maxTransferAmount;
		sidestates = te.sideStates;
		//System.out.println(sidestates[0] + " " + te.sideStates[0]);
	}
	
	@Override
	public IMessage onMessage(FluidTransportMessageServer message,
			MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getWorldClient().getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityFluidTransport)
		{
			TileEntityFluidTransport fte = (TileEntityFluidTransport) te;
			fte.setTank(message.tank);
			fte.maxTransferAmount = message.transferAmount;
			fte.maxTransferDelay = message.maxcooldown;
			fte.delayCooldown = message.cooldown;
			fte.sideStates = message.sidestates;
			//System.out.println(fte.sideStates[0] + " " + message.sidestates[1] + " " + message.sidestates[2] + " ");
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		
		int fluidID = buf.readShort();
		if(fluidID>0)
			tank.setFluid(new FluidStack(fluidID, buf.readInt()));
		else
			tank.setFluid(null);
		int capacity = buf.readInt();
		tank.setCapacity(capacity);
		
		maxcooldown = buf.readInt();
		cooldown = buf.readInt();
		transferAmount = buf.readInt();
		
		int bsize = buf.readInt();
		sidestates = new byte[bsize];
		for(int i = 0; i < bsize; i++)
		{
			sidestates[i]=buf.readByte();
		}
		//System.out.println(sidestates[0]);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		
		FluidStack stack = tank.getFluid();
		if(stack != null && stack.getFluid() != null)
		{
			buf.writeShort(stack.getFluid().getID());
			buf.writeInt(stack.amount);
		}
		else
			buf.writeShort(-1);
		buf.writeInt(tank.getCapacity());
		
		buf.writeInt(maxcooldown);
		buf.writeInt(cooldown);
		buf.writeInt(transferAmount);
		buf.writeInt(sidestates.length);
		for(int i = 0; i < sidestates.length; i++)
		{
			buf.writeByte(sidestates[i]);
		}
		//System.out.println(sidestates[0]);
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