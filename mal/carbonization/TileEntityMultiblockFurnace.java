package mal.carbonization;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMultiblockFurnace extends TileEntity implements ITileEntityMultiblock, IInventory, net.minecraft.inventory.ISidedInventory {

	private TileEntity original;//Needed to revert when the multiblock is broken
	
	/**
     * Is the random generator used by furnace to drop the inventory contents in random directions.
     */
    private Random furnaceRand = new Random();
    
	//furnace data
	private int oreCapacity;//number of ore blocks that can be processed at one time
	private int slagCapacity;//amount of slag that can be held at one time in millibuckets
	private float slagDistribution;//amount of ore slag per bucket
	private float fuelTimeModifier;//Multiplier to fuel time
	private float cookTimeModifier;//Multiplier to cook time
	private int maxFuelCapacity;//Maximum fuel held
	
	private float[] componentTiers = new float[2];//average efficiency of the side blocks [0] and base blocks[1] 
	private int xsize;
	private int ysize;
	private int zsize;
	private int[] offset;//the coordinate difference from our position to the lowest coordinate corner of the multiblock
	private ItemStack[] inputStacks;
	private ItemStack[] outputStacks;
	private float fuelStack;
	
	/*
	 * We are the master tile entity, so we initialize the gui and do the snazzy things from here
	 */
	@Override
	public void activate(World world, int x, int y, int z,
			EntityPlayer par5EntityPlayer) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * this is the master revert, it will tell all the tile entities in the area to revert themselves
	 * then clean itself up
	 */
	@Override
	public void revert() {
		//go through the volume and reset everyone
		for(int i = 0; i<xsize;i++)
		{
			for(int j=0; j<ysize; j++)
			{
				for(int k=0; k<zsize; k++)
				{
					TileEntity te = worldObj.getBlockTileEntity(xCoord+offset[0]+i,yCoord+offset[1]+j,zCoord+offset[2]+k);
					if(te instanceof ITileEntityMultiblock)
					{
						((ITileEntityMultiblock) te).revert();//tell it to revert.
					}
					else
					{
						System.out.println("This isn't our tile entity!");
					}
				}
			}
		}
		if(worldObj.blockExists(xCoord, yCoord, zCoord))//someone else broke and told us to tell everyone else :(
		{
			//spit out all the inventory and convert fuel time into as much fuel as possible
			float var10;
            float var11;
            float var12;
			//unprocessed items
			for(int i = 0; i<inputStacks.length; i++)
			{
				ItemStack item = inputStacks[i];

                if (item != null)
                {
                    var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                    while (item.stackSize > 0)
                    {
                        int var13 = this.furnaceRand.nextInt(21) + 10;

                        if (var13 > item.stackSize)
                        {
                            var13 = item.stackSize;
                        }

                        item.stackSize -= var13;
                        EntityItem eItem = new EntityItem(worldObj, (double)((float)xCoord + var10), (double)((float)yCoord + var11), (double)((float)zCoord + var12), new ItemStack(item.itemID, var13, item.getItemDamage()));

                        if (item.hasTagCompound())
                        {
                            eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
                        eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
                        eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
                        worldObj.spawnEntityInWorld(eItem);
                        
                    }
                }
                worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, this.original);
			}
			//output inventory
			for(int j = 0; j<outputStacks.length; j++)
			{
				ItemStack item = inputStacks[j];

                if (item != null)
                {
                    var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                    while (item.stackSize > 0)
                    {
                        int var13 = this.furnaceRand.nextInt(21) + 10;

                        if (var13 > item.stackSize)
                        {
                            var13 = item.stackSize;
                        }

                        item.stackSize -= var13;
                        EntityItem eItem = new EntityItem(worldObj, (double)((float)xCoord + var10), (double)((float)yCoord + var11), (double)((float)zCoord + var12), new ItemStack(item.itemID, var13, item.getItemDamage()));

                        if (item.hasTagCompound())
                        {
                            eItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        eItem.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
                        eItem.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
                        eItem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
                        worldObj.spawnEntityInWorld(eItem);
                        
                    }
                }
                
                //fuel
                float tempFuel = fuelStack;
                int numCoal = 0;
                int numPeat = 0;
                while (tempFuel > 0)
                {   
                	if(tempFuel>1600)
                	{
                		//add in a coal
                		tempFuel -= 1600;
                		numCoal++;
                	}
                	else if(tempFuel>600)
                	{
                		//add in a peat
                		tempFuel -= 600;
                		numPeat++;
                	}
                	else//not enough fuel anymore
                	{
                		tempFuel = -1;
                	}
                }
                
            	var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                
                if(numCoal>0)
                {
                	EntityItem eCoal = new EntityItem(worldObj,  (double)((float)xCoord + var10), (double)((float)yCoord + var11), (double)((float)zCoord + var12), new ItemStack(Item.coal, numCoal, 0));
                	float var15 = 0.05F;
                    eCoal.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
                    eCoal.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
                    eCoal.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
                    worldObj.spawnEntityInWorld(eCoal);
                }
                
                if(numPeat>0)
                {
	                EntityItem ePeat = new EntityItem(worldObj,  (double)((float)xCoord + var10), (double)((float)yCoord + var11), (double)((float)zCoord + var12), new ItemStack(carbonization.fuel, numPeat, 0));
	                float var15 = 0.05F;
	                ePeat.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
	                ePeat.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
	                ePeat.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
	                worldObj.spawnEntityInWorld(ePeat);
                }
                
                worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, this.original);
			}
		}
		
	}

	@Override
	public void initilize(Object[] params) {
		//takes in an array of parameters that are in a certain format
		this.original = (TileEntity)params[0];//the parent entity is always first
		xsize = Integer.parseInt((String)params[1]);
		ysize = Integer.parseInt((String)params[2]);
		zsize = Integer.parseInt((String)params[3]);
		offset[0] = Integer.parseInt((String)params[4]);
		offset[1] = Integer.parseInt((String)params[5]);
		offset[2] = Integer.parseInt((String)params[6]);
		componentTiers[0] = Float.parseFloat((String)params[7]);
		componentTiers[1] = Float.parseFloat((String)params[8]);
		calculateData();
	}
	
	//TODO: Make it not suck
	private void calculateData()
	{
		oreCapacity = (xsize-2)*(ysize-2)*(zsize-2);
		slagCapacity = oreCapacity*1000;
		maxFuelCapacity = (int)componentTiers[1]*xsize*zsize;
		slagDistribution = (300+(componentTiers[0]+componentTiers[1])*20);
		fuelTimeModifier = (float) (1+1.92*Math.log10(componentTiers[1]+1));
		cookTimeModifier = (float) (1-0.075*componentTiers[0]);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		
	}
	
	//check to see if there is fuel
	public boolean hasFuel()
	{
		if(fuelStack>0)
			return true;
		return false;
	}
	
	//check to see if the current input has an output
	public boolean hasRecipe(ItemStack input)
	{
		if(CarbonizationRecipes.smelting().getMultiblockSmeltingResult(input) != null)
			return true;
		return false;
	}
	
	//check to see if the output has empty space or a fitting slot for the input
	public int hasOutputSpace(ItemStack input)
	{
		for(int i = 0; i<outputStacks.length; i++)
		{
			if(outputStacks[i].getItem() == input.getItem())
				return i;
			if(outputStacks[i] == null)
				return i;
		}
		return -1;
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
	public int[] getAccessibleSlotsFromSide(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

}
