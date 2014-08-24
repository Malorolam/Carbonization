package mal.carbonization.tileentity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.MultiblockInitMessage;
import mal.carbonization.network.StructureBlockMessage;
import mal.core.api.ITieredItem;
import mal.core.api.ITileEntityMultiblock;
import mal.core.multiblock.Multiblock;
import mal.core.tileentity.ITileEntityMultiblockSlave;
import mal.core.tileentity.TileEntityMultiblockMaster;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityStructureBlock extends TileEntity implements IInventory, net.minecraft.inventory.ISidedInventory, ITileEntityMultiblockSlave, ITieredItem, ITileEntityMultiblock, IFluidHandler{

	/*Materials List
	 * Base:
	 * 0:Ice
	 * 1:Stone
	 * 2:Iron
	 * 3:Brick
	 * 4:Steel
	 * 5:Carbon Fibre
	 * 6:Titanium
	 * 7:Carbon Nanotube
	 * 8:Withered End
	 * 9:Cobalt Chrome
	 * 
	 * Secondary:
	 * 0:No Insulation
	 * 1:High Density Insulation
	 * 2:Fine Threading
	 * 
	 * Purpose:
	 * 0:Structure
	 * 1:Furnace
	 * 2:Machine
	 */
	
	public int baseMaterial;//0-9 for base materials
	public int secondaryMaterial;//0-2 for secondary materials
	public int purpose;//0-2 for purpose
	
	private double InsulationTier;
	private double ConductionTier;
	
	private boolean revert=false;//prevent revert chains
	private int mastercheckFail=0;//flag to delay revert a little while just in case stuff didn't load properly
	private ITileEntityMultiblock masterEntity;
	public boolean loaded=true;
	public int mx,my,mz;
	protected int data;
	
	public TileEntityStructureBlock()
	{
		//System.out.println("Plonked down the TE");
		masterEntity = null;
	}
	
	public TileEntityStructureBlock(int baseMaterial, int secondaryMaterial, int purpose)
	{
		this.baseMaterial = baseMaterial;
		this.secondaryMaterial = secondaryMaterial;
		this.purpose = purpose;
		this.data = purpose*1000+secondaryMaterial*100+baseMaterial;
		
		//System.out.println("Plonked down the TE, data: " + baseMaterial + ", " + secondaryMaterial + ", " + purpose);
		
		calculateTiers();
	}
	/*
	 * Initilize the structure block and identify what kind it is
	 */
	public void materialInitilize(int baseMaterial, int secondaryMaterial, int purpose)
	{
		this.baseMaterial = baseMaterial;
		this.secondaryMaterial = secondaryMaterial;
		this.purpose = purpose;
		
		//System.out.println("Initilized the TE, data: " + baseMaterial + ", " + secondaryMaterial + ", " + purpose);
		
		calculateTiers();
	}
	
	public int getData()
	{
		return 1000*purpose+100*secondaryMaterial+baseMaterial;
	}
	
	public boolean isMultiblock()
	{
		return (masterEntity!=null);
	}
	
	public double[] getTier()
	{
		double[] i = new double[2];
		i[0] = this.InsulationTier;
		i[1] = this.ConductionTier;
		return i;
	}
	
	public void activate(World world, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
		{
			//System.out.println("Master Entity " + masterEntity.toString() + " at: " + masterEntity.getX() + ", " + masterEntity.getY() + ", " + masterEntity.getZ());
			if(this.isUseableByPlayer(par5EntityPlayer))
				masterEntity.activate(world, this.getX()+mx, this.getY()+my, this.getZ()+mz, par5EntityPlayer);
		}
	}
	
	public void InitMultiblock(ITileEntityMultiblock master)
	{
		this.masterEntity = master;
		mx = masterEntity.getX() - this.xCoord;
		my = masterEntity.getY() - this.yCoord;
		mz = masterEntity.getZ() - this.zCoord;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(worldObj != null && !loaded)
		{
			this.calculateTiers();
			TileEntity te = worldObj.getTileEntity(this.getX()+mx, this.getY()+my, this.getZ()+mz);
			if(te instanceof ITileEntityMultiblock && (mx!=0||my!=0||mz!=0))
			{
				this.masterEntity = (ITileEntityMultiblock) te;
				loaded = true;
			}
		}
	}
	
	public double getInsulationTier()
	{
		return InsulationTier;
	}
	
	public double getConductionTier()
	{
		return ConductionTier;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
	   return CarbonizationPacketHandler.instance.getPacketFrom(new StructureBlockMessage(this));
	}
	
	public IMessage getDescriptionMessage()
	{
		return new StructureBlockMessage(this);
	}

	/**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    	
    	this.baseMaterial = nbt.getInteger("baseMaterial");
    	this.secondaryMaterial = nbt.getInteger("secondaryMaterial");
    	this.purpose = nbt.getInteger("purpose");
    	this.InsulationTier = nbt.getDouble("insulationTier");
    	this.ConductionTier = nbt.getDouble("conductionTier");
		mx = nbt.getInteger("masterX");
		my = nbt.getInteger("masterY");
		mz = nbt.getInteger("masterZ");
		
		loaded = false;
    	//System.out.println("Reading Data: " + baseMaterial + ", " + secondaryMaterial + ", " + purpose + "; " + InsulationTier + ", " + ConductionTier);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setInteger("baseMaterial", baseMaterial);
    	nbt.setInteger("secondaryMaterial", secondaryMaterial);
    	nbt.setInteger("purpose", purpose);
    	nbt.setDouble("insulationTier", InsulationTier);
    	nbt.setDouble("conductionTier", ConductionTier);
		nbt.setInteger("masterX", mx);
		nbt.setInteger("masterY", my);
		nbt.setInteger("masterZ", mz);
		
    	//System.out.println("Writing Data: " + baseMaterial + ", " + secondaryMaterial + ", " + purpose + "; " + InsulationTier + ", " + ConductionTier);
    }
	
	/*
	 * Calculate the tier values based on the materials
	 */
	private void calculateTiers()
	{
		worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, carbonizationBlocks.structureBlock, 2);
		double InsTier = 0;
		double ConTier = 0;
		
		//get the base values
		switch(baseMaterial)
		{
		case 0:
			InsTier=1;
			ConTier=2;
			break;
		case 1:
			InsTier=2;
			ConTier=1;
			break;
		case 2:
			InsTier=2;
			ConTier=4;
			break;
		case 3:
			InsTier=4;
			ConTier=2;
			break;
		case 4:
			InsTier=3;
			ConTier=6;
			break;
		case 5:
			InsTier=6;
			ConTier=3;
			break;
		case 6:
			InsTier=4;
			ConTier=8;
			break;
		case 7:
			InsTier=8;
			ConTier=4;
			break;
		case 8:
			InsTier=5;
			ConTier=10;
			break;
		case 9:
			InsTier=10;
			ConTier=5;
			break;
		}
		
		//Apply secondary material modifiers
		switch(secondaryMaterial)
		{
		case 0:
			break;
		case 1:
			InsTier += InsTier;
			ConTier -= ConTier*0.5;
			break;
		case 2:
			InsTier -= InsTier*0.5;
			ConTier += ConTier;
			break;
		}
		
		this.ConductionTier = ConTier;
		this.InsulationTier = InsTier;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.getAccessibleSlotsFromSide(var1);
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.canInsertItem(i, itemstack, j);
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.canExtractItem(i, itemstack, j);
		return false;
	}

	@Override
	public int getSizeInventory() {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.getSizeInventory();
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.getStackInSlot(i);
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.decrStackSize(i, j);
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			return masterEntity.getStackInSlotOnClosing(i);
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			masterEntity.setInventorySlotContents(i, itemstack);
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
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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

	@Override
	public String getInventoryName() {
		if(masterEntity != null)
			return masterEntity.getInventoryName();
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		if(masterEntity != null)
			return masterEntity.hasCustomInventoryName();
		return false;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {
	
	}

	@Override
	public double[] getTier(int damage) {
		double[] d = new double[2];
		d[1] = this.ConductionTier;
		d[0] = this.InsulationTier;
		return d;
	}

	@Override
	public int getXSize() {
		return (masterEntity!=null && (mx!=0||my!=0||mz!=0))?(masterEntity.getXSize()):(0);
	}

	@Override
	public int getYSize() {
		return (masterEntity!=null && (mx!=0||my!=0||mz!=0))?(masterEntity.getYSize()):(0);
	}

	@Override
	public int getZSize() {
		return (masterEntity!=null && (mx!=0||my!=0||mz!=0))?(masterEntity.getZSize()):(0);
	}

	@Override
	public String getType() {
		return (masterEntity!=null && (mx!=0||my!=0||mz!=0))?(masterEntity.getType()):("");
	}

	@Override
	public void setMaster(TileEntityMultiblockMaster m) {
		masterEntity = m;
		if(m != null)
		{
			mx = masterEntity.getX() - this.xCoord;
			my = masterEntity.getY() - this.yCoord;
			mz = masterEntity.getZ() - this.zCoord;
		}
		
		loaded = true;
	}

	@Override
	public ITileEntityMultiblock getMaster() {
		return masterEntity;
	}

	@Override
	public void initilize(Object[] params) {
		//System.out.println("Dummy Initilized");
		masterEntity = (ITileEntityMultiblock) params[0];
		mx = masterEntity.getX() - this.xCoord;
		my = masterEntity.getY() - this.yCoord;
		mz = masterEntity.getZ() - this.zCoord;

		loaded = true;
	}

	@Override
	public void revert() {
		//System.out.println("structure revert");
		if(masterEntity != null && (mx!=0||my!=0||mz!=0))
			masterEntity.revert();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.fill(from, resource, doFill)):(0);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,boolean doDrain) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.drain(from, resource, doDrain)):(null);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.drain(from, maxDrain, doDrain)):(null);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.canFill(from, fluid)):(false);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.canDrain(from, fluid)):(false);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return(masterEntity != null && (mx!=0||my!=0||mz!=0))?(masterEntity.getTankInfo(from)):(null);
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