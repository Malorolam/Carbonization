package mal.carbonization.network;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityTunnelBore;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TunnelBoreMessageServer implements IMessage, IMessageHandler<TunnelBoreMessageServer, IMessage>{

	public int xpos, ypos, zpos;
	public int xSize;
	public int ySize;
	public int facing;
	public int cycles;
	public int maxDigCooldown;
	public int digCooldown;
	public int fuelStack;
	public int maxFuel;
	
	//upgrade flags
	public boolean digSilkTouch;
	public boolean digIgnoreHardness;
	public int digFortuneLevel;
	public boolean hollowScaffold;
	public double fuelMultiplyer;
	public boolean fixedPosition;
	public boolean voidUpgrade;
	
	public boolean inventory;
	public ItemStack[] upgradeSlots;
	public ItemStack[] dimensionStack;
	
	public TunnelBoreMessageServer(){}
	public TunnelBoreMessageServer(TileEntityTunnelBore te, boolean useInventory)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		xSize = te.xSize;
		ySize = te.ySize;
		facing = te.getFacing();
		cycles = te.cycles;
		maxDigCooldown = te.maxDigCooldown;
		digCooldown = te.digCooldown;
		fuelStack = te.getFuelStack();
		maxFuel = te.getMaxFuel();
		digSilkTouch = te.digSilkTouch;
		digIgnoreHardness = te.digIgnoreHardness;
		digFortuneLevel = te.digFortuneLevel;
		hollowScaffold = te.hollowScaffold;
		fuelMultiplyer = te.fuelMultiplyer;
		fixedPosition = te.fixedPosition;
		voidUpgrade = te.voidUpgrade;
		inventory = useInventory;
		if(inventory)
		{
			upgradeSlots = te.upgradeSlots;
			dimensionStack = te.dimensionStack;
		}
	}
	@Override
	public IMessage onMessage(TunnelBoreMessageServer message,MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getWorldClient().getTileEntity(message.xpos, message.ypos, message.zpos);
		if(te instanceof TileEntityTunnelBore)
		{
			TileEntityTunnelBore fte = (TileEntityTunnelBore) te;
			fte.xSize = message.xSize;
			fte.ySize = message.ySize;
			fte.setFacing(message.facing);
			fte.cycles = message.cycles;
			fte.maxDigCooldown = message.maxDigCooldown;
			fte.digCooldown = message.digCooldown;
			fte.setMaxFuel(message.maxFuel);
			fte.setFuelStack(message.fuelStack);
			fte.digSilkTouch = message.digSilkTouch;
			fte.digIgnoreHardness = message.digIgnoreHardness;
			fte.digFortuneLevel = message.digFortuneLevel;
			fte.hollowScaffold = message.hollowScaffold;
			fte.fuelMultiplyer = message.fuelMultiplyer;
			fte.fixedPosition = message.fixedPosition;
			fte.voidUpgrade = message.voidUpgrade;
			if(message.inventory)
			{
				fte.upgradeSlots = message.upgradeSlots;
				fte.dimensionStack = message.dimensionStack;
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		xSize = buf.readInt();
		ySize = buf.readInt();
		facing = buf.readInt();
		cycles = buf.readInt();
		maxDigCooldown = buf.readInt();
		digCooldown = buf.readInt();
		fuelStack = buf.readInt();
		maxFuel = buf.readInt();
		digSilkTouch = buf.readBoolean();
		digIgnoreHardness = buf.readBoolean();
		digFortuneLevel = buf.readInt();
		hollowScaffold = buf.readBoolean();
		fuelMultiplyer = buf.readDouble();
		fixedPosition = buf.readBoolean();
		voidUpgrade = buf.readBoolean();
		inventory = buf.readBoolean();
		if(inventory)
		{
			int upgradeSize = buf.readInt();
			int iitemcount = buf.readInt();
			byte[] upgradebyte = new byte[upgradeSize];
			buf.readBytes(upgradebyte);
			upgradeSlots = ByteArraytoItemStack(upgradebyte, iitemcount);
			
			int dimensionsize = buf.readInt();
			int ditemcount = buf.readInt();
			byte[] inputbyte = new byte[dimensionsize];
			buf.readBytes(inputbyte);
			dimensionStack = ByteArraytoItemStack(inputbyte, ditemcount);
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		buf.writeInt(xSize);
		buf.writeInt(ySize);
		buf.writeInt(facing);
		buf.writeInt(cycles);
		buf.writeInt(maxDigCooldown);
		buf.writeInt(digCooldown);
		buf.writeInt(fuelStack);
		buf.writeInt(maxFuel);
		buf.writeBoolean(digSilkTouch);
		buf.writeBoolean(digIgnoreHardness);
		buf.writeInt(digFortuneLevel);
		buf.writeBoolean(hollowScaffold);
		buf.writeDouble(fuelMultiplyer);
		buf.writeBoolean(fixedPosition);
		buf.writeBoolean(voidUpgrade);
		buf.writeBoolean(inventory);
		if(inventory)
		{
			byte[] i = ItemStacktoByteArray(upgradeSlots);
			buf.writeInt(i.length);
			buf.writeInt(upgradeSlots.length);
			buf.writeBytes(i);
			byte[] o = ItemStacktoByteArray(dimensionStack);
			buf.writeInt(o.length);
			buf.writeInt(dimensionStack.length);
			buf.writeBytes(o);
		}
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
