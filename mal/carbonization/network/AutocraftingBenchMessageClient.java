package mal.carbonization.network;

import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/*
 * Syncs noninventory stuff from the client with the server
 */
public class AutocraftingBenchMessageClient implements IMessage, IMessageHandler<AutocraftingBenchMessageClient, IMessage>{

	public int xpos, ypos, zpos;
	public boolean fuelUseChange;
	
	public AutocraftingBenchMessageClient(){}
	public AutocraftingBenchMessageClient(TileEntityAutocraftingBench te, boolean change)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		fuelUseChange = change;
	}
	
	@Override
	public IMessage onMessage(AutocraftingBenchMessageClient message,MessageContext ctx) {
		TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityAutocraftingBench)
		{
			((TileEntityAutocraftingBench)te).changeFuelUsage(message.fuelUseChange);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		fuelUseChange = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeBoolean(fuelUseChange);
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