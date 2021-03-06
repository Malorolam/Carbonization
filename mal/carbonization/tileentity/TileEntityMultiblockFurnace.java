package mal.carbonization.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.core.api.IFuelContainer;
import mal.core.api.ITileEntityMultiblock;
import mal.core.tileentity.ITileEntityMultiblockSlave;
import mal.carbonization.CarbonizationRecipeHandler;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.MultiblockFurnaceMessageClient;
import mal.carbonization.network.MultiblockFurnaceMessageServer;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import mal.core.tileentity.TileEntityMultiblockMaster;
import mal.core.util.MalLogger;
import mal.core.item.ItemFuelPotentialBucket;
import mal.core.multiblock.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMultiblockFurnace extends TileEntityMultiblockMaster implements ITileEntityMultiblock, IInventory, net.minecraft.inventory.ISidedInventory, IFluidHandler {

	//Original tile entity data
	public int xsize;
	public int ysize;
	public int zsize;
	public int[] offset = new int[3];//the coordinate difference from our position to the lowest coordinate corner of the multiblock
	public boolean properlyActivated = true;
	private boolean reverting = false;
	private boolean fromLoad = false;
	
	/**
     * Is the random generator used by furnace to drop the inventory contents in random directions.
     */
    private Random furnaceRand = new Random();
    
	//furnace data
	public int oreCapacity;//number of ore blocks that can be processed at one time
	private int slagCapacity;//amount of slag that can be held at one time in millibuckets
	private int slagDistribution;//amount of ore slag per bucket
	public int slagTank;//amount of slag currently in the tank
	private float fuelTimeModifier;//Multiplier to fuel time
	private float cookTimeModifier;//Multiplier to cook time
	public double[] componentTiers = new double[2];//average efficiency of the insulation [0] and conduction[1] 
	public ItemStack[] inputStacks = new ItemStack[9];
	public ItemStack[] outputStacks = new ItemStack[9];
	private int numQueueJobs;//number of jobs in the queue
	private int grossCookTime=0;
	private int grossMaxCookTime=0;
	public boolean passFuel = false;//pass unneeded fuel through the inventory
	private FluidTank tank = new FluidTank(carbonizationBlocks.fluidFuelPotential, 0, FluidContainerRegistry.BUCKET_VOLUME);
	
	private List<MultiblockWorkQueueItem> queue = new ArrayList();
	private HashMap<String, Integer> oreSlagInQueue = new HashMap<String, Integer>();
	
	public TileEntityMultiblockFurnace()
	{
	}
	/*
	 * We are the master tile entity, so we initialize the gui and do the snazzy things from here
	 */
	@Override
	public void activate(World world, int x, int y, int z,
			EntityPlayer player) {
		//System.out.println("Component Tiers: " + componentTiers[0] + ", " + componentTiers[1] +"; queue capacity: " + queue.maxJobs + "; activated: " + properlyActivated);
		player.openGui(carbonization.carbonizationInstance, 2, world, x, y, z);
	}

	/*
	 * this is the master revert, it will tell all the tile entities in the area to revert themselves
	 * then clean itself up
	 */
	@Override
	public void revert() 
	{
		if(!reverting)
		{
			reverting = true;
			MultiBlockInstantiator.revertMultiBlock(offset, worldObj, xCoord, yCoord, zCoord, xsize, ysize, zsize, false, carbonizationBlocks.furnaceControlBlock, carbonizationBlocks.structureBlock);
		}
	}
	
	/*
	 * Called when our block breaks to bypass a block break issue
	 */
	public void selfRevert() 
	{
		if(!reverting)
		{
			reverting = true;
			MultiBlockInstantiator.revertMultiBlock(offset, worldObj, xCoord, yCoord, zCoord, xsize, ysize, zsize, true, carbonizationBlocks.furnaceControlBlock, carbonizationBlocks.structureBlock);
		}
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
		for(int i = 0; i<inputStacks.length; i++)
		{
			ItemStack item = inputStacks[i];

            if (item != null)
            {
            	//System.out.println("input index: " + i);
                var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

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
            
            inputStacks[i] = null;
		}
		//output inventory
		for(int j = 0; j<outputStacks.length; j++)
		{
			ItemStack item = outputStacks[j];

            if (item != null)
            {
            	//System.out.println("output index: " + j);
                var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                EntityItem eItem = new EntityItem(worldObj, (double)(px + var10), (double)(py + var11), (double)(pz + var12), new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

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
            
            outputStacks[j] = null;
		}
        //fuel
        float tempFuel = getFuelStack();
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
	
	/*
	 * Dump a single item stack into the world, trying to aim closer to the closest player
	 * to avoid the annoying appearing in the multiblock issue
	 */
	public void dumpItem(ItemStack item)
	{
		if(worldObj == null || worldObj.isRemote)
			return;

		float var10;
        float var11;
        float var12;
        
        EntityPlayer player = worldObj.getClosestPlayer(xCoord, yCoord, zCoord, -1);

        double ppx = (offset[0]==0)?(offset[0]==xsize?1:0):-1;
        double ppy = (offset[1]==0)?(offset[1]==xsize?1:0):-1;
        double ppz = (offset[2]==0)?(offset[2]==xsize?1:0):-1;
        /*if(player != null)
        {
            ppx = player.posX;
            ppy = player.posY;
            ppz = player.posZ;
        }*/
        float px = (float) (xCoord+ppx);
        float py = (float) (yCoord+ppy);
        float pz = (float) (zCoord+ppz);

        if (item != null)
        {
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
        
        item = null;
	}
	
	/*
	 * Will take all the inventory and save it to a NBT array to be loaded later.
	 * Unused right now, doesn't work right
	 */
	public NBTTagCompound saveInventory()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList input = new NBTTagList();

		for (int i = 0; i < this.inputStacks.length; ++i)
		{
			if (this.inputStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.inputStacks[i].writeToNBT(var4);
				input.appendTag(var4);
			}
		}
			
		nbt.setTag("inputItems", input);
			
		NBTTagList output = new NBTTagList();

    	for (int i = 0; i < this.outputStacks.length; ++i)
    	{
    		if (this.outputStacks[i] != null)
    		{
    			NBTTagCompound var4 = new NBTTagCompound();
    			var4.setByte("Slot", (byte)i);
    			this.outputStacks[i].writeToNBT(var4);
    			output.appendTag(var4);
    		}
    	}

        nbt.setTag("outputItems", output);
        
        NBTTagList list = new NBTTagList();
		if(queue != null && !queue.isEmpty())
		{
			//System.out.println("Building Queue Data");
			for(MultiblockWorkQueueItem item : queue)//cycle through each item in the queue
			{
				//System.out.println("Added item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
				NBTTagCompound job = new NBTTagCompound();
				item.generateNBT(job);
				list.appendTag(job);
			}
			nbt.setTag("Jobs", list);
		}
		
/*		NBTTagList slaglist = new NBTTagList();
		for(String s: this.oreSlagInQueue.keySet())
		{
			NBTTagCompound slag = new NBTTagCompound();
			slag.setString("SlagType", s);
			slag.setInteger("Value", oreSlagInQueue.get(s));
			slaglist.appendTag(slag);
		}
		nbt.setTag("Slag", slaglist);*/
        
        return nbt;
	}
	
	
	 //Will load up the inventory data
	 
	public void loadInventory(NBTTagCompound nbt)
	{
		NBTTagList input = nbt.getTagList("inputItems", 10);
		for (int i = 0; i < input.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)input.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inputStacks.length)
            {
                this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList output = nbt.getTagList("outputItems", 10);
		for (int i = 0; i < output.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)output.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.outputStacks.length)
            {
                this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList list = nbt.getTagList("Jobs", 10);
		for(int i = 0; i<list.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) list.getCompoundTagAt(i);
			MultiblockWorkQueueItem item = new MultiblockWorkQueueItem();
			item.retreiveNBT(tag);
			queue.add(item);
			//System.out.println("Rebuilt item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
		}
		
/*		NBTTagList slaglist = nbt.getTagList("Slag");
		if(slaglist.tagCount() > 0)
			for(int j = 0; j<slaglist.tagCount(); j++)
			{
				NBTTagCompound tag = (NBTTagCompound)slaglist.tagAt(j);
				String s = tag.getString("SlagType");
				int value = tag.getInteger("Value");
				this.addSlagToMap(s, value);
			}*/
	}

	@Override
	public void initilize(Object[] params) {
		//System.out.println("Master initilized");
		//takes in an array of parameters that are in a certain format
		xsize = (Integer) params[0];
		ysize = (Integer) params[1];
		zsize = (Integer) params[2];
		offset[0] = (Integer) params[3];
		offset[1] = (Integer) params[4];
		offset[2] = (Integer) params[5];
		componentTiers[0] = (Double)params[6];
		componentTiers[1] = (Double)params[7];
		calculateData();
	}
	
	//TODO: Make it not suck
	public void calculateData()
	{
		oreCapacity = (xsize-2)*(ysize-2)*(zsize-2);//how much stuff the queue can hold
		slagCapacity = oreCapacity*1000;//how much liquid slag can be held
		tank.setCapacity(xsize*zsize*1500);//how much FP can be held
		
		slagDistribution = (int) Math.floor(300+(componentTiers[0]+componentTiers[1])*100);//how much slag is produced
		double lv = Math.pow(xsize*ysize*zsize, 0.2);//a scaling factor based off of volume
		//fuelTimeModifier = (float) (1.6/Math.pow(componentTiers[0]-0.15*componentTiers[1]+1,0.8)*Math.log10(lv));
		fuelTimeModifier = (float)(1-0.145*componentTiers[0]/lv);
		
		cookTimeModifier = (float)(1-0.145*componentTiers[1]/lv);
		
		if(worldObj != null)//make sure the data is all loaded properly before we try to initialize everything
		{	
			if(fromLoad)
			{
				//verify it is still a multiblock if this is from load
				MultiBlockMatcher match = new MultiBlockMatcher(xsize, ysize, zsize);
				match.buildBasedHollowSolid(0, 0, 0, xsize-1, ysize-1, zsize-1, carbonizationBlocks.structureBlock, 0, carbonizationBlocks.structureBlock, 1000, 1);
	
				int[] value = MultiBlockInstantiator.matchPatternWithOffset(match, xCoord, yCoord, zCoord, worldObj, new Multiblock(worldObj.getBlock(xCoord, yCoord, zCoord), worldObj.getBlockMetadata(xCoord, yCoord, zCoord), true), offset);
				if(value == null || value != offset)//the multiblock isn't properly set-up, so revert everything that can be reverted
				{
					System.out.println("Multiblock erronionus, reverting...");
					MultiBlockInstantiator.revertMultiBlock(offset, worldObj, xCoord, yCoord, zCoord, xsize, ysize, zsize, false, carbonizationBlocks.furnaceControlBlock, carbonizationBlocks.structureBlock);
					return;
				}
				fromLoad = false;
			}
			
			//go through the volume and initialize everyone
			for(int i = 0; i<xsize;i++)
			{
				for(int j=0; j<ysize; j++)
				{
					for(int k=0; k<zsize; k++)
					{
						TileEntity te = worldObj.getTileEntity(xCoord-offset[0]+i,yCoord-offset[1]+j,zCoord-offset[2]+k);
						if(te instanceof ITileEntityMultiblockSlave)
						{
							((ITileEntityMultiblockSlave) te).initilize(new Object[]{this, null});//tell it to initialize
						}
						else
						{
							//System.out.println("This isn't our tile entity! - Skipping");
							//return;
						}
					}
				}
			}
			properlyActivated = true;
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
	
		if(worldObj != null && !this.worldObj.isRemote)
		{
			if(!properlyActivated)
				this.calculateData();
			
			int action = 0;
			action = this.handleInput(action);
			action = this.handleQueue(action);
			
			this.markDirty();
			CarbonizationPacketHandler.instance.sendToAllAround(new MultiblockFurnaceMessageServer(this, false), new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 64));
			
			//System.out.println("Jobs in Queue: " + this.numQueueJobs + ": " + worldObj.isRemote);
			if(this.numQueueJobs <= 0)
			{
				this.grossCookTime = 0;
				this.grossMaxCookTime = 0;
			}
		}
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		this.handleStacks(0);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		xsize = nbt.getInteger("xsize");
		ysize = nbt.getInteger("ysize");
		zsize = nbt.getInteger("zsize");
		offset = nbt.getIntArray("offset");
		componentTiers[0] = nbt.getDouble("compTier0");
		componentTiers[1] = nbt.getDouble("compTier1");
		setFuelStack(nbt.getInteger("fuelStack"));
		slagTank = nbt.getInteger("slagTank");
		numQueueJobs = nbt.getInteger("numJobs");
		grossCookTime = nbt.getInteger("grossCookTime");
		grossMaxCookTime = nbt.getInteger("grossMaxCookTime");
		passFuel = nbt.getBoolean("passFuel");
		
		NBTTagList input = nbt.getTagList("inputItems", 10);
		for (int i = 0; i < input.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)input.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inputStacks.length)
            {
                this.inputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList output = nbt.getTagList("outputItems", 10);
		for (int i = 0; i < output.tagCount(); ++i)
        {
            NBTTagCompound var4 = (NBTTagCompound)output.getCompoundTagAt(i);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.outputStacks.length)
            {
                this.outputStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
		
		NBTTagList list = nbt.getTagList("Jobs", 10);
		for(int i = 0; i<list.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) list.getCompoundTagAt(i);
			MultiblockWorkQueueItem item = new MultiblockWorkQueueItem();
			item.retreiveNBT(tag);
			queue.add(item);
			//System.out.println("Rebuilt item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
		}
		
		NBTTagList slaglist = nbt.getTagList("Slag", 10);
		if(slaglist.tagCount() > 0)
			for(int j = 0; j<slaglist.tagCount(); j++)
			{
				NBTTagCompound tag = (NBTTagCompound)slaglist.getCompoundTagAt(j);
				String s = tag.getString("SlagType");
				int value = tag.getInteger("Value");
				this.addSlagToMap(s, value);
			}
		
		
		properlyActivated = false;
		fromLoad = true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setInteger("xsize", xsize);
		nbt.setInteger("ysize", ysize);
		nbt.setInteger("zsize", zsize);
		nbt.setIntArray("offset", offset);
		nbt.setDouble("compTier0", componentTiers[0]);
		nbt.setDouble("compTier1", componentTiers[1]);
		nbt.setInteger("fuelStack", getFuelStack());
		nbt.setInteger("slagTank", slagTank);
		nbt.setInteger("numJobs", numQueueJobs);
		nbt.setInteger("grossCookTime", grossCookTime);
		nbt.setInteger("grossMaxCookTime", grossMaxCookTime);
		nbt.setBoolean("passFuel", passFuel);
		
		NBTTagList input = new NBTTagList();

		for (int i = 0; i < this.inputStacks.length; ++i)
		{
			if (this.inputStacks[i] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)i);
				this.inputStacks[i].writeToNBT(var4);
				input.appendTag(var4);
			}
		}
			
		nbt.setTag("inputItems", input);
			
		NBTTagList output = new NBTTagList();

    	for (int i = 0; i < this.outputStacks.length; ++i)
    	{
    		if (this.outputStacks[i] != null)
    		{
    			NBTTagCompound var4 = new NBTTagCompound();
    			var4.setByte("Slot", (byte)i);
    			this.outputStacks[i].writeToNBT(var4);
    			output.appendTag(var4);
    		}
    	}

        nbt.setTag("outputItems", output);
        
        NBTTagList list = new NBTTagList();
		if(queue != null && !queue.isEmpty())
		{
			//System.out.println("Building Queue Data");
			for(MultiblockWorkQueueItem item : queue)//cycle through each item in the queue
			{
				//System.out.println("Added item: " + item.input.getDisplayName() + " (" + item.cookTime + "/" + item.maxCookTime + ")");
				NBTTagCompound job = new NBTTagCompound();
				item.generateNBT(job);
				list.appendTag(job);
			}
			nbt.setTag("Jobs", list);
		}
		
		NBTTagList slaglist = new NBTTagList();
		for(String s: this.oreSlagInQueue.keySet())
		{
			NBTTagCompound slag = new NBTTagCompound();
			slag.setString("SlagType", s);
			slag.setInteger("Value", oreSlagInQueue.get(s));
			slaglist.appendTag(slag);
		}
		nbt.setTag("Slag", slaglist);
	}
	
	//check to see if there is fuel
	public boolean hasFuel()
	{
		if(getFuelStack()>0)
			return true;
		return false;
	}
	
	//Check to see if there is anything in the queue
	public boolean hasWork()
	{
		if(this.numQueueJobs > 0)
			return true;
		return false;
	}
	
	//return the scaled percentage of the queue
	@SideOnly(Side.CLIENT)
	public int getQueueCapacityScaled(int i)
	{
		//System.out.println(this.grossCookTime + "/" + this.grossMaxCookTime);
		return (this.getGrossMaxCookTime()>0)?(this.getGrossCookTime()*i/this.getGrossMaxCookTime()):0;
	}
	
	//return the scaled percentage of the slag tank
	@SideOnly(Side.CLIENT)
	public int getSlagCapacityScaled(int i)
	{
		return this.slagTank*i/(this.slagCapacity+1);
	}
	
	//return the scaled percentage of the fuel tank
	@SideOnly(Side.CLIENT)
	public int getFuelCapacityScaled(int i)
	{
		return (int) (this.getFuelStack()*i/(getMaxFuelCapacity()+1));
	}
	
	//add fuel
	//if this would overfill the fuel storage, don't add any fuel
	//to preserve a fuel item
	public boolean addFuel(int time)
	{
		if(getFuelStack()+time<=getMaxFuelCapacity())
		{
			setFuelStack(getFuelStack() + time);
			return true;
		}
		return false;
	}
	
	//overloaded version for a fuel item itself
	public boolean addFuel(ItemStack item)
	{
		if(item == null)
			return false;
		int time = UtilReference.getItemBurnTime(item, (int)(getMaxFuelCapacity()-tank.getFluidAmount()), false);
		if(getFuelStack()+time<=getMaxFuelCapacity())
		{
			time = UtilReference.getItemBurnTime(item, time, true);
			if(time <= 0)
				return false;
		
			setFuelStack(getFuelStack() + time);
			return true;
		}
		
		return false;
	}
	
	/**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    /*public int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else if(par0ItemStack.getItem() instanceof IFuelContainer)
        {
        	//get the value
        	long fuelValue = ((IFuelContainer)par0ItemStack.getItem()).getFuelValue(par0ItemStack);
        	int value = (int) (maxFuelCapacity-fuelStack);
        	
        	//if it's a number, reduce it by some amount, we're using standard coal or the value, whichever is smaller
        	if(fuelValue == 0)
        		return 0;
        	else if(fuelValue >= value)
        	{
        		((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, -value, false);
        		return value;
        	}
        	else
        	{
        		((IFuelContainer)par0ItemStack.getItem()).setFuel(par0ItemStack, 0, true);
        		return (int)fuelValue;
        	}
        }
        else
        {
            int var1 = par0ItemStack.getItem().itemID;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    return 150;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 300;
                }
                
                if (var3 == Block.coalBlock)
                {
                    return 16000;
                }
            }

            if (var1 == Item.stick.itemID) return 100;
            if (var1 == Item.coal.itemID) return 1600;
            if (var1 == Item.bucketLava.itemID) return 20000;
            if (var1 == Block.sapling.blockID) return 100;
            if (var1 == Item.blazeRod.itemID) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }
    
    public int checkItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else if(par0ItemStack.getItem() instanceof IFuelContainer)
        {
        	//get the value
        	long fuelValue = ((IFuelContainer)par0ItemStack.getItem()).getFuelValue(par0ItemStack);
        	int value = (int) (maxFuelCapacity-fuelStack);
        	
        	//if it's a number, reduce it by some amount, we're using standard coal or the value, whichever is smaller
        	if(fuelValue == 0)
        		return 0;
        	else if(fuelValue >= value)
        	{
        		return value;
        	}
        	else
        	{
        		return (int)fuelValue;
        	}
        }
        else
        {
            int var1 = par0ItemStack.getItem().itemID;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    return 150;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 300;
                }
                
                if (var3 == Block.coalBlock)
                {
                    return 16000;
                }
            }

            if (var1 == Item.stick.itemID) return 100;
            if (var1 == Item.coal.itemID) return 1600;
            if (var1 == Item.bucketLava.itemID) return 20000;
            if (var1 == Block.sapling.blockID) return 100;
            if (var1 == Item.blazeRod.itemID) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }
*/    
    /*
     * Clean up the stacks
     */
    public int handleStacks(int action)
    {
    	boolean inc = false;
    	for(int i = 0; i<inputStacks.length; i++)
    	{
    		if(inputStacks[i] != null)
    			if(inputStacks[i].stackSize == 0)
    			{
    				inputStacks[i] = null;
    		    	//System.out.println("Cleared 0 value input at " + i);
    				inc = true;
    			}
    	}
    	
    	for(int i = 0; i<outputStacks.length; i++)
    	{
    		if(outputStacks[i] != null)
    			if(outputStacks[i].stackSize == 0)
    			{
    				outputStacks[i] = null;
    		    	//System.out.println("Cleared 0 value output at " + i);
    				inc = true;
    			}
    	}
    	if(inc)
    		action +=1;
    	return action;
    }
    
    /*
     * goes through the input stacks and runs appropriate functions for what they are
     * fuel gets turned into fuel time if it can
     * items with no recipe get put directly into the output if possible
     * items with recipe is queued to be processed
     */
    public int handleInput(int action)
    {
    	if(inputStacks == null || outputStacks == null || queue == null)
    		return action;//assume we just got a random update where the te isn't initialized yet
    	
    	for(int i = 0; i<inputStacks.length; i++)
    	{
    		if(inputStacks[i]==null || inputStacks[i].stackSize == 0)
    		{
    			//do nada
    		}
    		else if(hasRecipe(inputStacks[i]))//there is a recipe and space to put it
    		{
    			if(queue.size() < oreCapacity)
    			{
	    			int fuelTick = (int) Math.floor(CarbonizationRecipeHandler.smelting().getMultiblockFuelTime(this.getStackInSlot(i))*this.fuelTimeModifier);
	    			//System.out.println(fuelTick);
	    			if(getFuelStack() >= fuelTick)
	    			{
	    				tank.drain(fuelTick, true);
	    				//put the item in the queue
	    				ItemStack input = this.decrStackSize(i, 1);
	    				int cookTime = (int) Math.ceil(CarbonizationRecipeHandler.smelting().getMultiblockCookTime(input)*this.cookTimeModifier);
	    				String oreSlagType = CarbonizationRecipeHandler.smelting().getMultiblockOreSlagType(input);
	    				boolean b = CarbonizationRecipeHandler.smelting().getMultiblockForceSingle(input);
	    				queue.add(new MultiblockWorkQueueItem(input, fuelTick, cookTime, cookTime, oreSlagType, b?carbonization.ORESLAGRATIO:this.slagDistribution));
	    				//System.out.println("cookTime: " + cookTime + "; fuelTick: " + fuelTick + "; slagDistribution: " + slagDistribution);
	    				this.numQueueJobs = queue.size();
	    			}
	    			action++;
	    		}
    		}
    		else if(UtilReference.getItemBurnTime(inputStacks[i], 1, false)>0)//it's a bit of fuel
    		{
    			if(addFuel(inputStacks[i]))
    			{
    				if((inputStacks[i].getItem() instanceof IFuelContainer))
    				{}
    				else if(inputStacks[i].getItem() instanceof ItemFuelPotentialBucket)
    				{
    					insertOutputItem(new ItemStack(Items.bucket), i);
    				}
    				else
    				{
    					if(inputStacks[i].stackSize>1)
    						inputStacks[i].stackSize -= 1;
    					else
    						inputStacks[i] = null;
    				}
    				action++;
    			}
    			else if(passFuel)
    			{
    				if(insertOutputItem(inputStacks[i].copy(), i))
        				action++;
    			}
    		}
    		else//we don't know what to do, so just send the item to the output and let that deal with it
    		{
    			if(inputStacks[i].getItem() instanceof IFuelContainer)
    			{
    				if(((IFuelContainer)inputStacks[i].getItem()).getFuelValue(inputStacks[i])<=0)
    				{
    	    			if(insertOutputItem(inputStacks[i].copy(), i))
    	    				action++;
    				}
    			}
    			else if(insertOutputItem(inputStacks[i].copy(), i))
    				action++;
    		}
    	}
    	return action;
    }
    
    //handle information in the queue
    public int handleQueue(int action)
    {
    	if(inputStacks == null || outputStacks == null)
    		return action;
    	
    	tickJobs();
    	action++;
    	
		//System.out.println(this.getGrossCookTime() + "/" + this.grossMaxCookTime);
    	return action;
    }
    
  //tick each job and remove finished ones
  	//finished jobs are returned as a list of the input items so they can be removed elsewhere
  	//the current fuel is passed in to ensure that the job can be ticked
  	public void tickJobs()
  	{
  		if(queue == null)
  			return;
  		
  		List<MultiblockWorkQueueItem> doneItems = new ArrayList();
    	this.setGrossCookTime(0);
    	this.grossMaxCookTime = 0;
  		if(!queue.isEmpty())
  		{
  			for(MultiblockWorkQueueItem item:queue)
  			{
				//System.out.println(item.cookTime);
				boolean b = item.tickItem();
				if(b)
				{
					this.addSlagToMap(item.oreSlagType, item.slagYield);
					
					doneItems.add(item);
				}
				else
				{
					this.grossCookTime += item.cookTime;
					this.grossMaxCookTime += item.maxCookTime;
				}
  			}
  			
  			for(MultiblockWorkQueueItem item:doneItems)
  			{
  				boolean b = removeJob(item);
  				if(!b)
  					MalLogger.addLogMessage(Level.FATAL, "Queue Job mismanaged.");
  			}
  		}
  		
  		tickSlag();
  	}
  	
  	/*
  	 * Attempt number 2 at outputting the correct items in the correct quantities
  	 */
  	private void tickSlag()
  	{
  		
  		//find all the slag types with enough stuff to make something and put the output values in a map
  		HashMap<String, Integer> cslag = new HashMap<String, Integer>();
  		for(String s: oreSlagInQueue.keySet())
  		{
  			//System.out.println("Slag type: " + s + " with value of: " + oreSlagInQueue.get(s));
  			if(oreSlagInQueue.get(s) >= carbonization.ORESLAGRATIO)
  			{
  				int i = (int)Math.floor(oreSlagInQueue.get(s)/carbonization.ORESLAGRATIO);
  				int outputSize =CarbonizationRecipeHandler.smelting().getOreSlagOutput(s).stackSize; 
  				if(i>Math.floor(64/outputSize))
  					i=(int) Math.floor(64/outputSize);
  				int value = outputSize * i;
  				
  	  			Item id = CarbonizationRecipeHandler.smelting().getOreSlagOutput(s).getItem();
  	  			int damage = CarbonizationRecipeHandler.smelting().getOreSlagOutput(s).getItemDamage();
  	  			
  	  			if(insertOutputItem(new ItemStack(id, value, damage), -1))
  	  			{
  	  				if(cslag.containsKey(s))
  	  					cslag.put(s, cslag.get(s)+carbonization.ORESLAGRATIO*i);
  	  				else
  	  					cslag.put(s, carbonization.ORESLAGRATIO*i);
  	  			}
  				
  				//System.out.println("Adding item: " + value + "x" + CarbonizationRecipes.smelting().getOreSlagOutput(s).getDisplayName());
  			}
  			
  		}
  		
  		for(String s: cslag.keySet())
		{
			if(oreSlagInQueue.get(s) > cslag.get(s))
  				oreSlagInQueue.put(s, oreSlagInQueue.get(s)-cslag.get(s));
  			else
  				oreSlagInQueue.remove(s);
		}
  	}
  	
	//remove the successfully added finished job from the queue
	public boolean removeJob(MultiblockWorkQueueItem job)
	{
		this.numQueueJobs--;
		if(job == null)
			return false;
		else
			return queue.remove(job);
	}
	
	//check to see if the current input has an output
	public boolean hasRecipe(ItemStack input)
	{
		if(CarbonizationRecipeHandler.smelting().getMultiblockCookTime(input) != -1)
			return true;
		return false;
	}
	
	//check to see if the output has empty space or a fitting slot for the input
	/**
	 * This will find the first slot that meets the criteria of either a slot with the same item that has room (any room, stack splitting
	 * is handled elsewhere) or an empty slot.
	 */
	public int hasOutputSpace(ItemStack input)
	{
		for(int i = 0; i<outputStacks.length; i++)
		{
			if(outputStacks[i] == null)
				return i;
			//see if the stack
			if((outputStacks[i].getItem() == input.getItem() && outputStacks[i].getItemDamage()== input.getItemDamage()) && outputStacks[i].stackSize < outputStacks[i].getMaxStackSize())
				return i;
		}
		return -1;
	}
	
	//place an itemstack in an output slot if possible
	public boolean insertOutputItem(ItemStack item, int index)
	{
		if(item != null)
		{
			int slot = hasOutputSpace(item);
			if(slot == -1)//there is no slot free, so just dump it
			{
				/*this.dumpItem(item);
				if(index != -1)
				{
					inputStacks[index] = null;
				}*/
				return false;
			}
			if(outputStacks[slot] == null)//the slot is empty
			{
				outputStacks[slot] = item;
				if(index != -1)
					inputStacks[index] = null;
				return true;
			}
			else
			{
				if(item.stackSize > 0)
				{
					if(outputStacks[slot].stackSize + item.stackSize <= outputStacks[slot].getMaxStackSize())//the two stacks fit together
					{
						outputStacks[slot].stackSize += item.stackSize;
						if(index != -1)
							inputStacks[index] = null;
						return true;
					}
					else//the total stack size is too large, so split the input stack and max the output stack
					{
						int diff = outputStacks[slot].getMaxStackSize() - outputStacks[slot].stackSize;
						outputStacks[slot].stackSize = outputStacks[slot].getMaxStackSize();
						item = item.splitStack(item.stackSize-diff);
						return insertOutputItem(item, index);
					}
				}
			}
		}
		return false;
	}
	
	//check to see if the furnace can process a job
	public boolean canSmelt(int index)
	{
		if (inputStacks[index]==null)//no item
			return false;
		if(!hasFuel())//no fuel
			return false;
		if(!hasRecipe(inputStacks[index]))//no output
			return false;
		if(hasOutputSpace(inputStacks[index]) != -1)//there is space to put the output
			return false;
		
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(side == 0)
		{
			int[] bot = new int[outputStacks.length];
			for (int i = 0; i < bot.length; i++)
			{
				bot[i]=inputStacks.length+i;
			}
			return bot;
		}
		else if(side == 1)
		{
			int[] top = new int[inputStacks.length];
			for (int i = 0; i < top.length; i++)
			{
				top[i]=i;
			}
			return top;
		}
		else
		{
			int[] sid = new int[getSizeInventory()];
			for (int i = 0; i < sid.length; i++)
			{
				sid[i]=i;
			}
			return sid;
		}
	}

	/*
	 * checks to see if the item can be inserted into the slot by a machine
	 * since we don't care about which side stuff gets inserted into
	 * j doesn't matter
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int j) {
		return this.isItemValidForSlot(slot, itemstack);
	}

	/*
	 * checks to see if the item can be removed from the slot by a machine
	 * basically, if it's an output slot you can, otherwise not
	 * again, we don't care about sides, you can suck stuff from whereever
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		if(slot >= inputStacks.length)//output inventory
			return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inputStacks.length+outputStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot>=0 && slot<inputStacks.length)
			return inputStacks[slot];
		else if(slot>=inputStacks.length && slot<getSizeInventory())
			return outputStacks[slot-inputStacks.length];
		return null;
	}

	/*
	 * decreases the amount of an item in a stack
	 * first has to find out which inventory it is
	 * then split the stack
	 */
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i>=0 && i<inputStacks.length)
		{
			if (inputStacks[i] != null)
	        {
	            ItemStack itemstack;
	
	            if (inputStacks[i].stackSize <= j)
	            {
	            	itemstack = inputStacks[i];
	                this.inputStacks[i] = null;
	                return itemstack;
	            }
	            else
	            {
	                itemstack = this.inputStacks[i].splitStack(j);
	
	                if (this.inputStacks[i].stackSize == 0)
	                {
	                    this.inputStacks[i] = null;
	                }
	
	                return itemstack;
	            }
	        }
	        else
	        {
	            return null;
	        }
		}
		else if(i>=inputStacks.length && i<getSizeInventory())
		{
			i = i-inputStacks.length;
			if (outputStacks[i] != null)
	        {
	            ItemStack itemstack;
	
	            if (outputStacks[i].stackSize <= j)
	            {
	            	itemstack = outputStacks[i];
	                this.outputStacks[i] = null;
	                return itemstack;
	            }
	            else
	            {
	                itemstack = this.outputStacks[i].splitStack(j);
	
	                if (this.outputStacks[i].stackSize == 0)
	                {
	                    this.outputStacks[i] = null;
	                }
	
	                return itemstack;
	            }
	        }
	        else
	        {
	            return null;
	        }
		}		
		else
			return null;//outside the inventory
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot>=0 && slot < inputStacks.length)
		{
			if(inputStacks[slot] != null)
			{
				ItemStack var2 = inputStacks[slot];
				inputStacks[slot] = null;
				return var2;
			}
			return null;
		}
		else if(slot>= inputStacks.length && slot < this.getSizeInventory())
		{
			if(outputStacks[slot-inputStacks.length] != null)
			{
				ItemStack var2 = outputStacks[slot-inputStacks.length];
				outputStacks[slot-inputStacks.length] = null;
				return var2;	
			}
			return null;
		}
		else
			return null;
	}

	/*
	 * sets the inventory to the contents of the itemstack given
	 */
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(i>=0 && i<inputStacks.length)
		{
	        this.inputStacks[i] = itemstack;

	        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
	        {
	            itemstack.stackSize = this.getInventoryStackLimit();
	        }
		}
		else if(i>=inputStacks.length && i<getSizeInventory())
		{
	        this.outputStacks[i-inputStacks.length] = itemstack;

	        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
	        {
	            itemstack.stackSize = this.getInventoryStackLimit();
	        }
		}
		
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "multiblockfurnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

	/*
	 * Hopefully this is right
	 * will prevent items from being inserted into the output slots
	 * the furnace doesn't care about things being put in it (lol)
	 * and will figure out what to do with things later
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot<inputStacks.length)
			return true;
		return false;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return CarbonizationPacketHandler.instance.getPacketFrom(new MultiblockFurnaceMessageServer(this, true));
	}

	
	public void closeGui()
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			CarbonizationPacketHandler.instance.sendToServer(new MultiblockFurnaceMessageClient(this));
		}
	}
	
	public int getX()
	{
		return xCoord;
	}
	
	public int getY()
	{
		return yCoord;
	}
	
	public int getZ()
	{
		return zCoord;
	}
	public int getNumQueueJobs() {
		return numQueueJobs;
	}
	public void setNumQueueJobs(int numQueueJobs) {
		this.numQueueJobs = numQueueJobs;
	}
	public int getGrossCookTime() {
		return grossCookTime;
	}
	public void setGrossCookTime(int grossCookTime) {
		this.grossCookTime = grossCookTime;
	}
	public int getGrossMaxCookTime() {
		return grossMaxCookTime;
	}
	public void setGrossMaxCookTime(int grossMaxCookTime) {
		this.grossMaxCookTime = grossMaxCookTime;
	}
	public int getFuelStack() {
		return tank.getFluidAmount();
	}
	public void setFuelStack(int i) {
		tank.setFluid(new FluidStack(tank.getFluid().fluidID, i));
	}
	
	public List getQueue()
	{
		return queue;
	}
	
	public void setQueue(List queue)
	{
		this.queue = queue;
	}
	
	private void addSlagToMap(String slag, int value)
	{
		if(oreSlagInQueue.containsKey(slag))
		{
			int ov = oreSlagInQueue.get(slag);
			oreSlagInQueue.put(slag, value+ov);
		}
		else
			oreSlagInQueue.put(slag, value);
	}
	
	public HashMap getOreMap()
	{
		return this.oreSlagInQueue;
	}
	
	public void setOreMap(HashMap<String, Integer> oreSlagInQueue)
	{
		this.oreSlagInQueue = oreSlagInQueue;
	}
	
	public int getMaxFuelCapacity() {
		return tank.getCapacity();
	}
	
	public List<String> getSlagAsString()
	{
		List<String> list = new ArrayList();
		
		for(String s : oreSlagInQueue.keySet())
		{
			list.add(ColorReference.DARKGREY.getCode() + s + ": " + oreSlagInQueue.get(s));
		}
		
		return list;
	}
	public int getSlagDistribution() {
		return slagDistribution;
	}
	public float getFuelTimeModifier() {
		return fuelTimeModifier;
	}
	public float getCookTimeModifier() {
		return cookTimeModifier;
	}

	@Override
	public void openInventory() {

	}
	@Override
	public void closeInventory() {
		
	}
	@Override
	public int getXSize() {
		return xsize;
	}
	@Override
	public int getYSize() {
		return ysize;
	}
	@Override
	public int getZSize() {
		return zsize;
	}
	@Override
	public String getType() {
		return "furnace";
	}
	@Override
	public void writeInventorytoNBT(NBTTagCompound nbt) {
		
		
	}
	@Override
	public void buildInventoryfromNBT(NBTTagCompound nbt) {
		
		
	}
	public void setMaxFuelCapacity(int fc) {
		tank.setCapacity(fc);
	}

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;//(tank.getFluid().isFluidEqual(new FluidStack(fluid, 1)));
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
    	System.out.println("bloopbloop");
        return new FluidTankInfo[] { tank.getInfo() };
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