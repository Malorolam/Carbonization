package mal.carbonization.network;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FurnacesMessage implements IMessage, IMessageHandler<FurnacesMessage, IMessage>{

	public int xpos, ypos, zpos;
	public ItemStack[] inventory;
	public int furnaceMaxCookTime;
    public double furnaceCookTimeMultiplyer;
    public int metadata;
    public boolean usesExtraTime;
    public boolean isActive;
    public byte facing;
    public int tier;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int furnaceCookTime = 0;
	
	public FurnacesMessage(){}
	
	public FurnacesMessage(TileEntityFurnaces te)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		facing = te.getFacing();
		metadata = te.metadata;
		usesExtraTime = te.usesExtraTime;
		furnaceCookTimeMultiplyer = te.furnaceCookTimeMultiplyer;
		furnaceMaxCookTime = te.furnaceMaxCookTime;
		inventory = te.getInventory();
		tier = te.tier;
		furnaceBurnTime = te.furnaceBurnTime;
		currentItemBurnTime = te.currentItemBurnTime;
		furnaceCookTime = te.furnaceCookTime;
		isActive = te.isActive;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		facing = buf.readByte();
		metadata = buf.readInt();
		usesExtraTime = buf.readBoolean();
		isActive = buf.readBoolean();
		furnaceCookTimeMultiplyer = buf.readDouble();
		furnaceMaxCookTime = buf.readInt();
		tier = buf.readInt();
		furnaceBurnTime = buf.readInt();
		currentItemBurnTime = buf.readInt();
		furnaceCookTime = buf.readInt();
		
		int l = buf.readInt();
		int c = buf.readInt();
		byte[] b = new byte[l];
		buf.readBytes(b);
		inventory = ByteArraytoItemStack(b, c);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeByte(facing);
		buf.writeInt(metadata);
		buf.writeBoolean(usesExtraTime);
		buf.writeBoolean(isActive);
		buf.writeDouble(furnaceCookTimeMultiplyer);
		buf.writeInt(furnaceMaxCookTime);
		buf.writeInt(tier);
		buf.writeInt(furnaceBurnTime);
		buf.writeInt(currentItemBurnTime);
		buf.writeInt(furnaceCookTime);
		
		byte[] b = ItemStacktoByteArray(inventory);
		buf.writeInt(b.length);
		buf.writeInt(inventory.length);
		buf.writeBytes(b);
		
	}
	@Override
	public IMessage onMessage(FurnacesMessage message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityFurnaces)
		{
			TileEntityFurnaces fte = (TileEntityFurnaces)te;
			fte.xCoord = message.xpos;
			fte.yCoord = message.ypos;
			fte.zCoord = message.zpos;
			byte oldfacing = fte.facing;
			fte.facing = message.facing;
			fte.metadata = message.metadata;
			fte.usesExtraTime = message.usesExtraTime;
			fte.furnaceCookTimeMultiplyer = message.furnaceCookTimeMultiplyer;
			fte.furnaceMaxCookTime = message.furnaceMaxCookTime;
			fte.tier = message.tier;
			fte.furnaceBurnTime = message.furnaceBurnTime;
			fte.currentItemBurnTime = message.currentItemBurnTime;
			fte.furnaceCookTime = message.furnaceCookTime;
    		boolean oldactive = fte.isActive;
			fte.isActive = message.isActive;
			fte.setInventory(message.inventory);

    		if(oldactive != message.isActive || oldfacing != message.facing)
    			Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(fte.xCoord, fte.yCoord, fte.zCoord);
		}
		
		return null;
	}

	private byte[] ItemStacktoByteArray(ItemStack[] is)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < is.length; i++)
		{
			if(is[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Slot", i);
				is[i].writeToNBT(tag);
				list.appendTag(tag);
			}
		}
		nbt.setTag("Items", list);
		
		byte[] bytearray = null;
		try {
			bytearray = CompressedStreamTools.compress(nbt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytearray;
	}
	
	private ItemStack[] ByteArraytoItemStack(byte[] bytes, int itemCount)
	{
		ItemStack[] is = new ItemStack[itemCount];
		try {
			NBTTagCompound nbt = CompressedStreamTools.func_152457_a(bytes, NBTSizeTracker.field_152451_a);
			
			NBTTagList list = nbt.getTagList("Items", 0);
			
			for(int i = 0; i < itemCount; i++)
			{
				NBTTagCompound tag = list.getCompoundTagAt(i);
				int slot = tag.getInteger("Slot");
				if(slot >= 0 && slot < is.length)
					is[i] = ItemStack.loadItemStackFromNBT(tag);
			}
			return is;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
