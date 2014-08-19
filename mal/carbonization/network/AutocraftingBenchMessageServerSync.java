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

//Just stuff to sync every tick, no need to go crazy
public class AutocraftingBenchMessageServerSync implements IMessage, IMessageHandler<AutocraftingBenchMessageServerSync, IMessage>{

	public int xpos, ypos, zpos;
	public int fuelUsePercent;//percentage of fuel used per process
	public int processTime;//time to process an item, dependant on fuel usage
	public int fuelUsage;
	public int craftingCooldown; //amount of time before a new item can be crafted
	
	public boolean disableButtons;//set to true when the buttons wouldn't do anything (like max time = min time)
	public int tankLevel;
	public int tankCapacity;

	public AutocraftingBenchMessageServerSync(){}
	public AutocraftingBenchMessageServerSync(TileEntityAutocraftingBench te)
	{
		xpos = te.xCoord;
		ypos = te.yCoord;
		zpos = te.zCoord;
		fuelUsePercent = te.fuelUsePercent;
		processTime = te.processTime;
		fuelUsage = te.fuelUsage;
		craftingCooldown = te.craftingCooldown;
		disableButtons = te.disableButtons;
		tankLevel = te.getFuelStack();
		tankCapacity = te.getMaxCapacity();
	}
	
	@Override
	public IMessage onMessage(AutocraftingBenchMessageServerSync message,
			MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getWorldClient().getTileEntity(message.xpos, message.ypos, message.zpos);
		
		if(te instanceof TileEntityAutocraftingBench)
		{
			TileEntityAutocraftingBench ate = (TileEntityAutocraftingBench) te;
			
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
		buf.writeInt(fuelUsePercent);
		buf.writeInt(processTime);
		buf.writeInt(fuelUsage);
		buf.writeInt(craftingCooldown);
		buf.writeBoolean(disableButtons);
		buf.writeInt(tankLevel);
		buf.writeInt(tankCapacity);
	}
}
