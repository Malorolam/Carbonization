package mal.carbonization.network;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class AutocraftingBenchMessageServer implements IMessage, IMessageHandler<AutocraftingBenchMessageServer, IMessage>{

	public int xpos, ypos, zpos;
	public ItemStack[] inputStacks = new ItemStack[12];
	public ItemStack[] outputStacks = new ItemStack[12];
	public ItemStack[] recipeStacks = new ItemStack[10];
	public ItemStack[] upgradeStacks = new ItemStack[3];

	public int fuelUsePercent;//percentage of fuel used per process
	public int processTime;//time to process an item, dependant on fuel usage
	public int fuelUsage;
	public int craftingCooldown; //amount of time before a new item can be crafted
	
	public boolean disableButtons;//set to true when the buttons wouldn't do anything (like max time = min time)
	public int tankLevel;
	public int tankCapacity;

	public AutocraftingBenchMessageServer(){}
	public AutocraftingBenchMessageServer(TileEntityAutocraftingBench te)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		inputStacks = te.inputStacks;
		outputStacks = te.outputStacks;
		recipeStacks = te.recipeStacks;
		upgradeStacks = te.upgradeStacks;
		fuelUsePercent = te.fuelUsePercent;
		processTime = te.processTime;
		fuelUsage = te.fuelUsage;
		craftingCooldown = te.craftingCooldown;
		disableButtons = te.disableButtons;
		tankLevel = te.getFuelStack();
		tankCapacity = te.getMaxCapacity();
	}
	
	@Override
	public IMessage onMessage(AutocraftingBenchMessageServer message,
			MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getWorldClient().getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityAutocraftingBench)
		{
			TileEntityAutocraftingBench ate = (TileEntityAutocraftingBench) te;
			
			ate.inputStacks = message.inputStacks;
			ate.outputStacks = message.outputStacks;
			ate.recipeStacks = message.recipeStacks;
			ate.upgradeStacks = message.upgradeStacks;
			ate.fuelUsePercent = message.fuelUsePercent;
			ate.processTime = message.processTime;
			ate.fuelUsage = message.fuelUsage;
			ate.craftingCooldown = message.craftingCooldown;
			ate.disableButtons = message.disableButtons;
			ate.setFuelStack(message.tankLevel);
			ate.setMaxCapacity(message.tankCapacity);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		xpos = buf.readInt();
		ypos = buf.readInt();
		zpos = buf.readInt();
		
		int inputsize = buf.readInt();
		int iitemcount = buf.readInt();
		byte[] inputbyte = new byte[inputsize];
		buf.readBytes(inputbyte);
		inputStacks = ByteArraytoItemStack(inputbyte, iitemcount);
		
		int outputsize = buf.readInt();
		int oitemcount = buf.readInt();
		byte[] outputbyte = new byte[outputsize];
		buf.readBytes(outputbyte);
		outputStacks = ByteArraytoItemStack(outputbyte, oitemcount);
		
		int recipesize = buf.readInt();
		int ritemcount = buf.readInt();
		byte[] recipebyte = new byte[recipesize];
		buf.readBytes(recipebyte);
		recipeStacks = ByteArraytoItemStack(recipebyte, ritemcount);
		
		int upgradesize = buf.readInt();
		int uitemcount = buf.readInt();
		byte[] upgradebyte = new byte[upgradesize];
		buf.readBytes(upgradebyte);
		upgradeStacks = ByteArraytoItemStack(upgradebyte, uitemcount);
		
		fuelUsePercent = buf.readInt();
		processTime = buf.readInt();
		fuelUsage = buf.readInt();
		craftingCooldown = buf.readInt();
		disableButtons = buf.readBoolean();
		tankLevel = buf.readInt();
		tankCapacity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xpos);
		buf.writeInt(ypos);
		buf.writeInt(zpos);
		byte[] i = ItemStacktoByteArray(inputStacks);
		buf.writeInt(i.length);
		buf.writeInt(inputStacks.length);
		buf.writeBytes(i);
		byte[] o = ItemStacktoByteArray(outputStacks);
		buf.writeInt(o.length);
		buf.writeInt(outputStacks.length);
		buf.writeBytes(o);
		byte[] r = ItemStacktoByteArray(recipeStacks);
		buf.writeInt(r.length);
		buf.writeInt(recipeStacks.length);
		buf.writeBytes(r);
		byte[] u = ItemStacktoByteArray(upgradeStacks);
		buf.writeInt(u.length);
		buf.writeInt(upgradeStacks.length);
		buf.writeBytes(u);
		buf.writeInt(fuelUsePercent);
		buf.writeInt(processTime);
		buf.writeInt(fuelUsage);
		buf.writeInt(craftingCooldown);
		buf.writeBoolean(disableButtons);
		buf.writeInt(tankLevel);
		buf.writeInt(tankCapacity);
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
