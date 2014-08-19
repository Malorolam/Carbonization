package mal.carbonization.tileentity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

import mal.core.api.IFuelContainer;
import mal.core.api.IMachineUpgrade;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.item.ItemStructureBlock;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.TunnelBoreMessageClient;
import mal.carbonization.network.TunnelBoreMessageServer;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import mal.core.util.MalLogger;
import mal.core.multiblock.MultiBlockInstantiator;
import mal.core.multiblock.MultiBlockMatcher;
import mal.core.multiblock.Multiblock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import buildcraft.api.tools.*;

public class TileEntityTunnelBore extends TileEntity implements ISidedInventory, IInventory, IFluidHandler
{
	/**
	 * Is the random generator used by furnace to drop the inventory contents in random directions.
	 */
	private Random furnaceRand = new Random();
	
	private FluidTank tank = new FluidTank(carbonizationBlocks.fluidFuelPotential, 0, 1600000);
	public static int MAXDIMENSION = 15;
	public ItemStack[] upgradeSlots = new ItemStack[3];
	public ItemStack[] dimensionStack = new ItemStack[3];
	private ArrayList<ItemStack> miningQueue = new ArrayList<ItemStack>();
	public int xSize=1;
	public int ySize=1;
	private int facing = 0;
	public int cycles = 1;
	public int maxDigCooldown = carbonization.MAXAUTOCRAFTTIME;
	public int digCooldown = 0;
	
	//upgrade flags
	public boolean digSilkTouch = false;
	public boolean digIgnoreHardness = false;
	public int digFortuneLevel = 0;
	public boolean hollowScaffold = true;
	public double fuelMultiplyer = 1;
	public boolean fixedPosition = false;
	public boolean voidUpgrade = false;
	
	public int getFacing()
	{
		return facing;
	}
	
	public void setFacing(int facing)
	{
		this.facing = facing;
	}
	
	public void constructTooltipText(ArrayList<String> list)
	{
		list.add(ColorReference.LIGHTGREY.getCode() + "Bore Information:");
		list.add(ColorReference.LIGHTGREY.getCode() + "Dig Cooldown: " + digCooldown + "/" + maxDigCooldown + ".");
		list.add(ColorReference.LIGHTGREY.getCode() + "Dig Head Distance: " + cycles + ".");
		list.add(ColorReference.LIGHTGREY.getCode() + "Creating " + ((hollowScaffold)?("Hollow Scaffold."):("Solid Scaffold.")));
		list.add(ColorReference.LIGHTGREY.getCode() + "Fuel Consumption per block: " + String.format("%.2f", getFuelMultiplyer()) + ".");
		list.add(ColorReference.LIGHTGREY.getCode() + "Fortune Level: " + digFortuneLevel + ".");
		if(digSilkTouch)
			list.add(ColorReference.LIGHTGREEN.getCode() + "Silk Touching Blocks.");
		if(digIgnoreHardness)
			list.add(ColorReference.LIGHTRED.getCode() + "Ignoring Block Hardness.");
		if(fixedPosition)
			list.add(ColorReference.LIGHTRED.getCode() + "Fixed dig head " + cycles + " blocks from bore.");
		if(voidUpgrade)
			list.add(ColorReference.LIGHTRED.getCode() + "Voiding Overflow Items.");
	}
	
	public int swapFacing(int f)
	{
		facing = f;
		if(facing==3)
			facing = 0;
		else
			facing++;
		return facing;
	}
	
	public double getFuelMultiplyer()
	{
		return fuelMultiplyer*carbonization.BASEAUTOCRAFTFUEL/10;
	}
	
	public void activate(World world, int x, int y, int z,EntityPlayer player)
	{
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench)
		{
			swapFacing(world.getBlockMetadata(x, y, z));
			if(carbonization.REPORTTOCHAT)
				MalLogger.addChatMessage(player, "Current Facing: " + facing);
			worldObj.setBlockMetadataWithNotify(x, y, z, facing,3);
		}
		else
			player.openGui(carbonization.carbonizationInstance, 3, world, x, y, z);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(worldObj != null && !worldObj.isRemote)
		{
			updateUpgrade();
			updateDimension();
			updateFuel();
			updateQueue();
			dig();
			CarbonizationPacketHandler.instance.sendToAll(new TunnelBoreMessageServer(this, false));
		}
		
	}
	
	private void updateUpgrade()
	{
		digSilkTouch=false;
		digIgnoreHardness=false;
		digFortuneLevel=0;
		maxDigCooldown=carbonization.MAXAUTOCRAFTTIME;
		fuelMultiplyer=1;
		voidUpgrade = false;
		fixedPosition = false;
		
		int digreduction = 0;
		for(int i = 0; i < upgradeSlots.length; i++)
		{
			if(upgradeSlots[i] != null && upgradeSlots[i].getItem() instanceof IMachineUpgrade)
			{
				int damage = upgradeSlots[i].getItemDamage();
				String upgrade = ((IMachineUpgrade)upgradeSlots[i].getItem()).getUpgradeName(damage);
				
				if(upgrade.equalsIgnoreCase("efficiency1"))
					fuelMultiplyer=fuelMultiplyer*0.9;
				else if(upgrade.equalsIgnoreCase("efficiency2"))
					fuelMultiplyer=fuelMultiplyer*0.7;
				else if(upgrade.equalsIgnoreCase("efficiency3"))
					fuelMultiplyer=fuelMultiplyer*0.5;
				else if(upgrade.equalsIgnoreCase("efficiency4"))
					fuelMultiplyer=fuelMultiplyer*0.3;
				else if(upgrade.equalsIgnoreCase("efficiency5"))
					fuelMultiplyer=fuelMultiplyer*0.1;
				else if(upgrade.equalsIgnoreCase("fortune1"))
					digFortuneLevel += 1;
				else if(upgrade.equalsIgnoreCase("fortune2"))
					digFortuneLevel += 2;
				else if(upgrade.equalsIgnoreCase("fortune3"))
					digFortuneLevel += 3;
				else if(upgrade.equalsIgnoreCase("fortune4"))
					digFortuneLevel += 4;
				else if(upgrade.equalsIgnoreCase("fortune5"))
					digFortuneLevel += 5;
				else if(upgrade.equalsIgnoreCase("haste1"))
					digreduction += Math.floor(0.1*maxDigCooldown);
				else if(upgrade.equalsIgnoreCase("haste2"))
					digreduction += Math.floor(0.3*maxDigCooldown);
				else if(upgrade.equalsIgnoreCase("haste3"))
					digreduction += Math.floor(0.5*maxDigCooldown);
				else if(upgrade.equalsIgnoreCase("haste4"))
					digreduction += Math.floor(0.7*maxDigCooldown);
				else if(upgrade.equalsIgnoreCase("haste5"))
					digreduction += Math.floor(0.9*maxDigCooldown);
				else if(upgrade.equalsIgnoreCase("silktouch1"))
					digSilkTouch = true;
				else if(upgrade.equalsIgnoreCase("hardness1"))
					digIgnoreHardness = true;
				else if(upgrade.equalsIgnoreCase("void1"))
					voidUpgrade = true;
				else if(upgrade.equalsIgnoreCase("fixed1"))
					fixedPosition = true;
				
			}
		}
		
		maxDigCooldown -= digreduction;
		if(maxDigCooldown < 5)
			maxDigCooldown = 5;
	}
	
	private void updateDimension()
	{
		if(dimensionStack[0] != null)
		{
			xSize = dimensionStack[0].stackSize*2+1;
			if(xSize>MAXDIMENSION)
				xSize=MAXDIMENSION;
		}
		else
			xSize = 1;
		
		if(dimensionStack[1] != null)
		{
			ySize = dimensionStack[1].stackSize*2+1;
			if(ySize>MAXDIMENSION)
				ySize=MAXDIMENSION;
		}
		else
			ySize = 1;
	}
	
	private void updateFuel()
	{
		if(dimensionStack[2] != null)
		{
			if(addFuel(dimensionStack[2]))
			{
				if(!(dimensionStack[2].getItem() instanceof IFuelContainer))
				{
					if(dimensionStack[2].stackSize>1)
						dimensionStack[2].stackSize -= 1;
					else
						dimensionStack[2] = null;
				}
			}
			else if(dimensionStack[2].getItem() instanceof IFuelContainer)
			{
				if(((IFuelContainer)dimensionStack[2].getItem()).getFuelValue(dimensionStack[2])<=0)
				{
					addItem(dimensionStack[2].copy());
					dimensionStack[2] = null;
				}
			}
		}
	}
	
  	public boolean addFuel(ItemStack item)
  	{
  		if(item == null)
  			return false;
  		int time = UtilReference.getItemBurnTime(item,(int) (tank.getCapacity()-tank.getFluidAmount()),true);
  		if(time <= 0)
  			return false;
  		
  		if(tank.getFluidAmount()+time<=tank.getCapacity())
  		{
  			tank.fill(new FluidStack(carbonizationBlocks.fluidFuelPotential, time), true);
  			return true;
  		}
  		
  		return false;
  	}
	
	private void updateQueue()
	{
		if(miningQueue.isEmpty())
			return;
		
		//check to see if there is an inventory behind us
		TileEntity te=null;
		switch(facing)
		{
			case 0:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
				if(!(te instanceof IInventory))
					return;
				break;
			case 1:
				te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
				if(!(te instanceof IInventory))
					return;
				break;
			case 2:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
				if(!(te instanceof IInventory))
					return;
				break;
			case 3:
				te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
				if(!(te instanceof IInventory))
					return;
				break;
		}
		
		if(te != null)
		{
			IInventory ii = ((IInventory)te);
			for(int i = 0; i < miningQueue.size(); i++)
			{
				ItemStack is = miningQueue.get(i).copy();
				ItemStack is2 = UtilReference.insertStack(ii, is, 1);
				
				if(is2 == null || is2.stackSize == 0)
					miningQueue.remove(i);
			}
			
			//if there is void upgrade, we just dump the queue into nothingness
			if(voidUpgrade)
				miningQueue.clear();
		}
	}
    
	private void dig()
	{
		
		//if we aren't receiving a redstone signal, don't progress anything
		if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
			return;
		
		if(digCooldown > 0)
		{
			digCooldown--;
			return;
		}
		else
			digCooldown = maxDigCooldown;
		
		//make sure the queue is empty
		if(!miningQueue.isEmpty())
		{
			if(carbonization.REPORTTOCHAT)
				MalLogger.sendMessageToClosestPlayer(worldObj, "Mining Queue has stuff in it.",xCoord,yCoord,zCoord);
			return;
		}
		
		
		//first figure out minimum point and facing of dig head
		int xsize = 0;
		int ysize = ySize;
		int zsize = 0;
		int xmin = 0;
		int ymin = 0;
		int zmin = 0;
		
		//y is easy, since it never points up or down
		ymin = yCoord-ysize/2;
		
		//x and z are dependant on the facing of the block
		switch(facing)
		{
			case 0:
			{
				xsize = xSize;
				zsize = 1;
				xmin = xCoord-xsize/2;
				zmin = zCoord-cycles;
				break;
			}
			case 1:
			{
				xsize = 1;
				zsize = xSize;
				xmin = xCoord-cycles;
				zmin = zCoord-zsize/2;
				break;
			}
			case 2:
			{
				xsize = xSize;
				zsize = 1;
				xmin = xCoord-xsize/2;
				zmin = zCoord+cycles;
				break;
			}
			case 3:
			{
				xsize = 1;
				zsize = xSize;
				xmin = xCoord+cycles;
				zmin = zCoord-zsize/2;
				break;
			}
		}
		//UtilReference.sendMessageToAllPlayers(worldObj, "xSize: " + xsize + "; ySize: " + ysize + "; zSize: " + zsize );
		//UtilReference.sendMessageToAllPlayers(worldObj, "xMin: " + xmin + "; yMin: " + ymin + "; zMin: " + zmin);
		
		if(xsize==1)
		{
			generateChunk(xmin,zmin,worldObj);
			generateChunk(xmin,zmin+zsize,worldObj);
		}
		else
		{
			generateChunk(xmin,zmin,worldObj);
			generateChunk(xmin+xsize,zmin,worldObj);
		}
		
		//now construct the dig head pattern and apply the fuel and inventory logic to it
		MultiBlockMatcher match = new MultiBlockMatcher(xsize,ysize,zsize);
		MultiBlockInstantiator.createWorldMultiBlock(match, xmin, ymin, zmin, xsize, ysize, zsize, worldObj);
		
		//process through each block, calculating the fuel usage and dug items
		double fuelusage = 0;
		Multiblock[][][] pattern = match.getPattern();
		for(int i = 0; i < pattern.length; i++)
			for(int j = 0; j < pattern[0].length; j++)
				for(int k = 0; k < pattern[0][0].length; k++)
				{
					Block b = pattern[i][j][k].toBlock();
					if(b != null && !(b instanceof BlockFluidBase) && !(b instanceof IFluidBlock))
					{
						if(b.getBlockHardness(worldObj, xmin+i, ymin+j, zmin+k)<0 && !digIgnoreHardness)
						{
							if(carbonization.REPORTTOCHAT)
								MalLogger.sendMessageToClosestPlayer(worldObj, "Industrial Bore is stuck.  Indestructible block at dig head.", xCoord, yCoord, zCoord);
							return;
						}
						int f = (int)Math.ceil(getFuelMultiplyer()*Math.pow(1+((!digIgnoreHardness)?(b.getBlockHardness(worldObj, xmin+i, ymin+j, zmin+k)):(0)),2));
						if(f>tank.getCapacity())
							f=tank.getCapacity();
						if(tank.getFluidAmount()-f<0)
						{
							if(carbonization.REPORTTOCHAT)
								MalLogger.sendMessageToClosestPlayer(worldObj, "Insufficient fuel: Need " + f + ", have " + tank.getFluidAmount() + ".", xCoord, yCoord, zCoord);
							return;
						}
					
						tank.drain(f, true);
						
						if(worldObj.getTileEntity(xmin+i, ymin+j, zmin+k)!=null)
						{
							TileEntity te = worldObj.getTileEntity(xmin+i, ymin+j, zmin+k);
							if(te instanceof IInventory)
							{
								IInventory ii = (IInventory)te;
								for(int m = 0; m < ii.getSizeInventory(); m++)
								{
									addItem(ii.getStackInSlot(m));
									ii.setInventorySlotContents(m, null);
								}
							}
						}
						
						if(digSilkTouch && b.canSilkHarvest(worldObj, null, xmin+i, ymin+j, zmin+k, pattern[i][j][k].data))
							addItem(pattern[i][j][k].toItemStack());
						else
							addAllItems(b.getDrops(worldObj, xmin+i, ymin+j, zmin+k, pattern[i][j][k].data, digFortuneLevel));
					}
					MultiBlockInstantiator.createSingleBlock(xmin+i, ymin+j, zmin+k, worldObj, (fixedPosition)?(null):(carbonizationBlocks.scaffoldBlock));
				}

		//replace the pattern with scaffold
		//MultiBlockInstantiator.createTestArea(match, xmin, ymin, zmin, worldObj, carbonization.scaffold);
		if(hollowScaffold && ysize >=3 && cycles>1)
		{
			MultiBlockMatcher hole;
			switch(facing)
			{
			case 0:
				hole = new MultiBlockMatcher(xsize-2, ysize-2, zsize);
				MultiBlockInstantiator.createTestArea(hole, xmin+1, ymin+1, zmin+1, worldObj, null);
				break;
			case 1:
				hole = new MultiBlockMatcher(xsize, ysize-2, zsize-2);
				MultiBlockInstantiator.createTestArea(hole, xmin+1, ymin+1, zmin+1, worldObj, null);
				break;
			case 2:
				hole = new MultiBlockMatcher(xsize-2, ysize-2, zsize);
				MultiBlockInstantiator.createTestArea(hole, xmin+1, ymin+1, zmin-1, worldObj, null);
				break;
			case 3:
				hole = new MultiBlockMatcher(xsize, ysize-2, zsize-2);
				MultiBlockInstantiator.createTestArea(hole, xmin-1, ymin+1, zmin+1, worldObj, null);
				break;
			}
		}
		
		//finally, increment the cycle so it digs slightly further each time unless we're fixed
		if(!fixedPosition)
			cycles++;
	}
	
	private void generateChunk(int x, int z, World world)
	{
		Chunk c = world.getChunkFromBlockCoords(x, z);
		IChunkProvider prov = world.getChunkProvider();
		
		if(!c.isTerrainPopulated)
		{
			try {
				prov.populate(prov, x >> 4, z >> 4);
			}
			catch(ConcurrentModificationException e)
			{
				MalLogger.addLogMessage(Level.WARN, "Concurrent Modification Exception at " + x + ", " + z + ".");
			}
			catch(RuntimeException e)
			{
				MalLogger.addLogMessage(Level.WARN, "Population failure at " + x + ", " + z + ".");
				e.printStackTrace();
			}
		}
	}
	
	private boolean addAllItems(ArrayList<ItemStack> list)
	{
		int count = 0;
		for(ItemStack is:list)
			count += (addItem(is))?(1):(0);
			
		if(count > 0)
			return true;
		return false;
	}
	
	//true is something got put in the queue, false is nothing did
	private boolean addItem(ItemStack is)
	{
		if(is==null)
			return false;
		//first try to put it in an inventory behind it
		TileEntity te=null;
		switch(facing)
		{
			case 0:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
				if(!(te instanceof IInventory))
					te = null;
				break;
			case 1:
				te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
				if(!(te instanceof IInventory))
					te = null;
				break;
			case 2:
				te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
				if(!(te instanceof IInventory))
					te = null;
				break;
			case 3:
				te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
				if(!(te instanceof IInventory))
					te = null;
				break;
		}
		
		if(te != null)
		{
			IInventory ii = ((IInventory)te);
			ItemStack is2 = UtilReference.insertStack(ii,is, 1);
			
			if(is2 != null && is2.stackSize > 0)
			{
				miningQueue.add(is2);
				return true;
			}
			return false;
		}
		else
		{
			miningQueue.add(is);
			return true;
		}
	}
	/*
	 * Will take all the inventory and save it to a NBT array to be loaded later.
	 * 
	 */
	public NBTTagCompound saveInventory()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList upgrade = new NBTTagList();

		for (int i = 0; i < this.upgradeSlots.length; ++i)
		{
			if (this.upgradeSlots[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.upgradeSlots[i].writeToNBT(var4);
				upgrade.appendTag(var4);
			}
		}
		nbt.setTag("upgradeItems", upgrade);
		
		NBTTagList dimension = new NBTTagList();

		for (int i = 0; i < this.dimensionStack.length; ++i)
		{
			if (this.dimensionStack[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.dimensionStack[i].writeToNBT(var4);
				dimension.appendTag(var4);
			}
		}
		nbt.setTag("dimensionStacks", dimension);
		
		NBTTagList queue = new NBTTagList();
		for(int i = 0; i<miningQueue.size(); i++)
		{
			if(miningQueue.get(i) != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.miningQueue.get(i).writeToNBT(var4);
				queue.appendTag(var4);
			}
		}
		nbt.setTag("miningQueue", queue);
		
        
        return nbt;
	}
	
	
	 //Will load up the inventory data
	 
	public void loadInventory(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("upgradeItems", 10);
		for(int i = 0; i<list.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)list.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.upgradeSlots.length)
            {
                this.upgradeSlots[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
		
		NBTTagList dim = nbt.getTagList("dimensionStacks", 10);
		for(int i = 0; i<dim.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)dim.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.dimensionStack.length)
            {
                this.dimensionStack[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
		
		NBTTagList queue = nbt.getTagList("miningQueue", 10);
		for(int i = 0; i < queue.tagCount(); i++)
		{
			NBTTagCompound var4 = (NBTTagCompound)queue.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");
			if(var5>= 0)
			{
				this.miningQueue.add(ItemStack.loadItemStackFromNBT(var4));
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		tank.writeToNBT(nbt);
		nbt.setInteger("xSize", xSize);
		nbt.setInteger("ySize", ySize);
		nbt.setInteger("facing", facing);
		nbt.setInteger("cycles", cycles);
		nbt.setInteger("digcooldown", digCooldown);
		nbt.setBoolean("hollow", hollowScaffold);

		NBTTagList upgrade = new NBTTagList();

		for (int i = 0; i < this.upgradeSlots.length; ++i)
		{
			if (this.upgradeSlots[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.upgradeSlots[i].writeToNBT(var4);
				upgrade.appendTag(var4);
			}
		}
		nbt.setTag("upgradeItems", upgrade);
		
		NBTTagList dimension = new NBTTagList();

		for (int i = 0; i < this.dimensionStack.length; ++i)
		{
			if (this.dimensionStack[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.dimensionStack[i].writeToNBT(var4);
				dimension.appendTag(var4);
			}
		}
		nbt.setTag("dimensionStacks", dimension);
		
		NBTTagList queue = new NBTTagList();
		for(int i = 0; i<miningQueue.size(); i++)
		{
			if(miningQueue.get(i) != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.miningQueue.get(i).writeToNBT(var4);
				queue.appendTag(var4);
			}
		}
		nbt.setTag("miningQueue", queue);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		
		xSize = nbt.getInteger("xSize");
		ySize = nbt.getInteger("ySize");
		facing = nbt.getInteger("facing");
		cycles = nbt.getInteger("cycles");
		digCooldown = nbt.getInteger("digcooldown");
		hollowScaffold = nbt.getBoolean("hollow");

		NBTTagList list = nbt.getTagList("upgradeItems", 10);
		for(int i = 0; i<list.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)list.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.upgradeSlots.length)
            {
                this.upgradeSlots[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
		
		NBTTagList dim = nbt.getTagList("dimensionStacks", 10);
		for(int i = 0; i<dim.tagCount(); i++)
		{
            NBTTagCompound var4 = (NBTTagCompound)dim.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.dimensionStack.length)
            {
                this.dimensionStack[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
		}
		
		NBTTagList queue = nbt.getTagList("miningQueue", 10);
		for(int i = 0; i < queue.tagCount(); i++)
		{
			NBTTagCompound var4 = (NBTTagCompound)queue.getCompoundTagAt(i);
			byte var5 = var4.getByte("Slot");
			if(var5>= 0)
			{
				this.miningQueue.add(ItemStack.loadItemStackFromNBT(var4));
			}
		}
	}

	@Override
	public int getSizeInventory() 
	{
		return upgradeSlots.length+dimensionStack.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		//System.out.println("slot: " + i);
		if(i>=0 && i<upgradeSlots.length)
		{
			return upgradeSlots[i];
		}
		else if(i<getSizeInventory()-1)
		{
			return dimensionStack[i-upgradeSlots.length];
		}
		else
		{
			//System.out.println(((worldObj.isRemote)?("client: "):("server: ")) + ((fuelSlot==null)?("null"):(fuelSlot.toString())));
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if (this.getStackInSlot(slot) != null)
        {
            ItemStack var3;

            if (this.getStackInSlot(slot).stackSize <= num)
            {
                var3 = this.getStackInSlot(slot);
                this.setInventorySlotContents(slot, null);
                return var3;
            }
            else
            {
                var3 = this.getStackInSlot(slot).splitStack(num);

                if (this.getStackInSlot(slot).stackSize == 0)
                {
                    this.setInventorySlotContents(slot, null);
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		//System.out.println("close slot: " + i);
		if(i>=0 && i<upgradeSlots.length)
		{
			return upgradeSlots[i];
		}
		else if(i<getSizeInventory()-1)
		{
			return dimensionStack[i-upgradeSlots.length];
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(i>=0 && i<upgradeSlots.length)
		{
			upgradeSlots[i] = itemstack;
		}
		else if(i<upgradeSlots.length+dimensionStack.length)
		{
			dimensionStack[i-upgradeSlots.length] = itemstack;
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return CarbonizationPacketHandler.instance.getPacketFrom(new TunnelBoreMessageServer(this, true));
	}
	
	public void dumpInventory()
	{
		if(worldObj == null || worldObj.isRemote)
			return;
		//spit out all the inventory and convert fuel time into as much fuel as possible
		float var10;
		float var11;
		float var12;

		EntityPlayer player = worldObj.getClosestPlayer(xCoord, yCoord, zCoord, -1);

		double ppx = 0;
		double ppy = 0;
		double ppz = 0;
		if(player != null)
		{
			ppx = player.posX;
			ppy = player.posY;
			ppz = player.posZ;
		}
		float px = (float) (xCoord+ppx)/2;
		float py = (float) (yCoord+ppy)/2;
		float pz = (float) (zCoord+ppz)/2;
		//unprocessed items
		for(int i = 0; i<upgradeSlots.length; i++)
		{
			ItemStack item = upgradeSlots[i];

			if (item != null)
			{
				//System.out.println("input index: " + i);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);
			}

			upgradeSlots[i] = null;
		}
		//output inventory
		for(int j = 0; j<dimensionStack.length-1; j++)
		{
			ItemStack item = dimensionStack[j];

			if (item != null)
			{
				//System.out.println("output index: " + j);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);

			}

			dimensionStack[j] = null;
		}

		//upgrade inventory
		for(int j = 0; j<miningQueue.size(); j++)
		{
			ItemStack item = miningQueue.get(j);

			if (item != null)
			{
				//System.out.println("output index: " + j);
				var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

				if (item.hasTagCompound())
				{
					eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
				}

				float var15 = 0.05F;
				eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
				eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
				eItem.delayBeforeCanPickup = 10;
				worldObj.spawnEntityInWorld(eItem);

			}

			miningQueue.remove(j);
		}
		
		//fuel item
		ItemStack item = dimensionStack[2];

		if (item != null)
		{
			//System.out.println("output index: " + j);
			var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

			EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), item);

			if (item.hasTagCompound())
			{
				eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
			}

			float var15 = 0.05F;
			eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
			eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eItem.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(eItem);

		}

		dimensionStack[2] = null;
		
		//fuel
		double tempFuel = tank.getFluidAmount();
		int numCoal = (int) Math.floor(tempFuel/1600);

		var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
		var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

		if(numCoal>0)
		{
			EntityItem eCoal = new EntityItem(worldObj,  (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(Items.coal, numCoal, 0));
			float var15 = 0.05F;
			eCoal.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
			eCoal.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
			eCoal.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(eCoal);
		}
	}

	public int getXSize()
	{
		return (dimensionStack[0]==null)?(1):(dimensionStack[0].stackSize);
	}
	
	public int getYSize()
	{
		return (dimensionStack[1]==null)?(1):(dimensionStack[1].stackSize);
	}
	
	@Override
	public String getInventoryName() {
		return "tunnelBoreInv";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(i>=0 && i<upgradeSlots.length)
		{
			if(itemstack.getItem() instanceof IMachineUpgrade)
			{
				return true;
			}
		}
		else if(i<upgradeSlots.length+dimensionStack.length)
		{
			if(itemstack.getItem() instanceof ItemStructureBlock)
			{
				return true;
			}
		}
		else if(i<getSizeInventory())
		{
			if(UtilReference.getItemBurnTime(itemstack, 0, false)>0)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int[] i = new int[1];
		i[0]=getSizeInventory()-1;
		return i;
	}

	@Override
	//Just fuel here
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if(UtilReference.getItemBurnTime(itemstack, 1, false)<=0)
			return false;
		if(i!=getSizeInventory()-1)
			return false;
		return true;
	}

	@Override
	//We autoeject items, so no automation
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}
	
    
    public void swapTagState()
    {
    	boolean hs;
    	if(hollowScaffold)
    		hs = false;
    	else
    		hs = true;
    	
    	if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			CarbonizationPacketHandler.instance.sendToServer(new TunnelBoreMessageClient(this, hs));
		}
    }

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	
	public int getFuelStack()
	{
		return tank.getFluidAmount();
	}
	
	public void setFuelStack(int fuel)
	{
		tank.setFluid(new FluidStack(carbonizationBlocks.fluidFuelPotential, fuel));
	}
	
	public int getMaxFuel()
	{
		return tank.getCapacity();
	}
	
	public void setMaxFuel(int fuel)
	{
		tank.setCapacity(fuel);
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