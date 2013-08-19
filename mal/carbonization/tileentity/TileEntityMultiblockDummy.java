package mal.carbonization.tileentity;

import mal.carbonization.ITileEntityMultiblock;
import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.Multiblock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Mal
 * This is the tile entity for any dummy blocks for a multiblock, all it does is when activated 
 * refers to the activation method of the reference tile entity, where everything is maintained
 */
public class TileEntityMultiblockDummy extends TileEntity implements ITileEntityMultiblock, IInventory, net.minecraft.inventory.ISidedInventory{

	public ITileEntityMultiblock masterEntity;
	public Multiblock masterBlock;
	private int mx, my=-100, mz;//master location
	private boolean revert=false;//prevent revert chains
	private int mastercheckFail=0;//flag to delay revert a little while just in case stuff didn't load properly
	private boolean loaded = false;
	
	public TileEntityMultiblockDummy()
	{
		//System.out.println("Multiblock Dummy Created");		
	}
	
	public void InitData(Multiblock block, ITileEntityMultiblock master)
	{
		this.masterBlock = block;
		this.masterEntity = master;
	}
	
	public void activate(World world, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		//System.out.println("!!REMOTE ACCESS!!");
		if(masterEntity != null)
			if(this.isUseableByPlayer(par5EntityPlayer))
				masterEntity.activate(world, masterEntity.getX(), masterEntity.getY(), masterEntity.getZ(), par5EntityPlayer);
	}

	/*
	 * Clean up ourselves if there is still a block here
	 */
	@Override
	public void revert() {
		if(masterEntity != null)
			masterEntity.revert();
		else
			System.out.println("Master Entity null!");
	}

	/*
	 * FIRST PARAM MUST BE A TILE ENTITY
	 * SECOND PARAM MUST BE MULTIBLOCK OR NULL
	 * OR I WILL BREAK
	 */
	@Override
	public void initilize(Object[] params) {
		//System.out.println("Dummy Initilized");
		masterEntity = (ITileEntityMultiblock) params[0];
		mx = masterEntity.getX();
		my = masterEntity.getY();
		mz = masterEntity.getZ();
		if(params[1] != null)
			masterBlock = (Multiblock) params[1];
		loaded = true;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(worldObj != null && !loaded)
		{
			TileEntity te = worldObj.getBlockTileEntity(mx, my, mz);
			if(te instanceof ITileEntityMultiblock)
			{
				this.masterEntity = (ITileEntityMultiblock) te;
				loaded = true;
			}
		
			/*if(masterEntity==null && loaded)
			{
				if(my == -100)//something is really broken, so revert
					MultiBlockInstantiator.revertSingleMultiblock(worldObj, xCoord, yCoord, zCoord);
				
				System.out.println("Master Entity Null: Attempting Recovery");
				te = worldObj.getBlockTileEntity(mx, my, mz);
				if((mastercheckFail == 0 || mastercheckFail == 100) && te instanceof TileEntityMultiblockFurnace)
				{
					this.masterEntity = (TileEntityMultiblockFurnace) te;
					loaded = false;
				}
				else//couldn't find the correct entity at the location, so revert to the standard block
				{
					if(mastercheckFail == 0)//we haven't failed before
					{
						System.out.println("Recovery Failed: Delaying 100 ticks...");
						mastercheckFail = 1;
					}
					else if(mastercheckFail <100)
					{
						mastercheckFail++;
					}
					else
					{
						System.out.println("Recovery Failed: Reverting Block");
						MultiBlockInstantiator.revertSingleMultiblock(worldObj, xCoord, yCoord, zCoord);
					}
				}
			}*/
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		if(this.masterBlock != null)
		{
			nbt.setInteger("masterBlockID", this.masterBlock.blockID);
			nbt.setInteger("masterBlockmeta", this.masterBlock.blockMetadata);
		}
		else
		{
			nbt.setInteger("masterBlockID", -1);
			nbt.setInteger("masterBlockmeta", -1);
		}
		//save the location of the master entity, so we can try to recover it
		if(this.masterEntity instanceof TileEntity)
		{
			nbt.setInteger("masterEntityx", ((TileEntity)this.masterEntity).xCoord);
			nbt.setInteger("masterEntityy", ((TileEntity)this.masterEntity).yCoord);
			nbt.setInteger("masterEntityz", ((TileEntity)this.masterEntity).zCoord);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		int ID = nbt.getInteger("masterBlockID");
		int meta = nbt.getInteger("masterBlockmeta");
		if(ID == -1 || meta == -1)
			this.masterBlock = null;
		else
			this.masterBlock = new Multiblock(ID, meta);
		mx = nbt.getInteger("masterEntityx");
		my = nbt.getInteger("masterEntityy");
		mz = nbt.getInteger("masterEntityz");
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(masterEntity == null)
			return null;
		return masterEntity.getAccessibleSlotsFromSide(var1);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if(masterEntity == null)
			return false;
		return masterEntity.canInsertItem(i, itemstack, j);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(masterEntity == null)
			return false;
		return masterEntity.canExtractItem(i, itemstack, j);
	}

	@Override
	public int getSizeInventory() {
		if(masterEntity == null)
			return 0;
		return masterEntity.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(masterEntity == null)
			return null;
		return masterEntity.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(masterEntity == null)
			return null;
		return masterEntity.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(masterEntity == null)
			return null;
		return masterEntity.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(masterEntity != null)
			masterEntity.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInvName() {
		if(masterEntity == null)
			return "";
		return masterEntity.getInvName();
	}

	@Override
	public boolean isInvNameLocalized() {
		if(masterEntity == null)
			return false;
		return masterEntity.isInvNameLocalized();
	}

	@Override
	public int getInventoryStackLimit() {
		if(masterEntity == null)
			return 0;
		return masterEntity.getInventoryStackLimit();
	}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

	@Override
	public void openChest() {
		if(masterEntity != null)
			masterEntity.openChest();
	}

	@Override
	public void closeChest() {
		if(masterEntity != null)
			masterEntity.closeChest();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(masterEntity == null)
			return false;
		return masterEntity.isItemValidForSlot(i, itemstack);
	}

	@Override
	public int getX() {
		return xCoord;
	}

	@Override
	public int getY() {
		return yCoord;
	}

	@Override
	public int getZ() {
		return zCoord;
	}
}
/*******************************************************************************
* Copyright (c) 2013 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
* 
* 
*********************************************************************************/
