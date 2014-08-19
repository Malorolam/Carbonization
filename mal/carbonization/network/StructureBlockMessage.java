package mal.carbonization.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import mal.core.util.MalLogger;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class StructureBlockMessage implements IMessage, IMessageHandler<StructureBlockMessage, IMessage>{

	int xpos, ypos, zpos;
	int mx, my, mz;
	int baseMaterial, secondaryMaterial, purpose;
	boolean loaded;
	
	public StructureBlockMessage(){}
	
	public StructureBlockMessage(TileEntityStructureBlock te)
	{
		xpos = te.getX();
		ypos = te.getY();
		zpos = te.getZ();
		mx = te.mx;
		my = te.my;
		mz = te.mz;
		baseMaterial = te.baseMaterial;
		secondaryMaterial = te.secondaryMaterial;
		purpose = te.purpose;
		loaded = te.loaded;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		mx = buf.readInt();
		my = buf.readInt();
		mz = buf.readInt();
		baseMaterial = buf.readInt();
		secondaryMaterial = buf.readInt();
		purpose = buf.readInt();
		loaded = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeInt(mx);
		buf.writeInt(my);
		buf.writeInt(mz);
		buf.writeInt(baseMaterial);
		buf.writeInt(secondaryMaterial);
		buf.writeInt(purpose);
		buf.writeBoolean(loaded);
	}
	@Override
	public IMessage onMessage(StructureBlockMessage message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityStructureBlock)
		{
			boolean flag = false;
			if(((TileEntityStructureBlock)te).baseMaterial != message.baseMaterial || ((TileEntityStructureBlock)te).secondaryMaterial != message.secondaryMaterial || ((TileEntityStructureBlock)te).purpose != message.purpose)
				flag = true;
			((TileEntityStructureBlock)te).mx = message.mx;
			((TileEntityStructureBlock)te).my = message.my;
			((TileEntityStructureBlock)te).mz = message.mz;
			((TileEntityStructureBlock)te).loaded = message.loaded;
			((TileEntityStructureBlock)te).baseMaterial = message.baseMaterial;
			((TileEntityStructureBlock)te).secondaryMaterial = message.secondaryMaterial;
			((TileEntityStructureBlock)te).purpose = message.purpose;
			
			if(flag)
			{
				//MalLogger.addLogMessage("Re-rendering block at: " + message.xpos + ", " + message.ypos + ", " + message.zpos + ".");
				Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(message.xpos, message.ypos, message.zpos);
			}
		}
		
		return null;
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