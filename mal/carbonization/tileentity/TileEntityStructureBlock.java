package mal.carbonization.tileentity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.MultiblockInitMessage;
import mal.carbonization.network.StructureBlockMessage;
import mal.core.api.ITileEntityMultiblock;
import mal.core.multiblock.Multiblock;
import mal.core.tileentity.TileEntityMultiblockSlave;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityStructureBlock extends TileEntityMultiblockSlave implements IInventory, net.minecraft.inventory.ISidedInventory{

	/*Materials List
	 * Base:
	 * 0:Ice
	 * 1:Wood
	 * 2:Stone
	 * 3:Iron
	 * 4:Carbon Flake
	 * 5:Carbon-plated Iron
	 * 6:Refined Iron
	 * 7:Carbon Thread
	 * 8:Carbon-infused Rebar
	 * 9:Pig Iron
	 * 10:Carbon Fibre
	 * 11:Reinforced Carbon Fibre
	 * 12:HC Steel
	 * 13:Carbon Nanoflake
	 * 14:Carbon-plated Steel
	 * 15:Steel
	 * 16:Carbon Nanotube
	 * 17:Reinforced Carbon Nanotube
	 * 18:Withered End
	 * 
	 * Secondary:
	 * 0:No Insulation
	 * 1:Basic Insulation
	 * 2:High Density Insulation
	 * 3:Coarse Threading
	 * 4:Fine Threading
	 * 
	 * Purpose:
	 * 0:Structure
	 * 1:Furnace
	 * 2:Machine
	 */
	
	public int baseMaterial;//0-19 for base materials
	public int secondaryMaterial;//0-4 for secondary materials
	public int purpose;//0-2 for purpose
	
	private double InsulationTier;
	private double ConductionTier;
	
	private boolean revert=false;//prevent revert chains
	private int mastercheckFail=0;//flag to delay revert a little while just in case stuff didn't load properly
	
	public TileEntityStructureBlock()
	{
		//System.out.println("Plonked down the TE");
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
	public void initilize(int baseMaterial, int secondaryMaterial, int purpose)
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
		if(masterEntity != null)
		{
			//System.out.println("Master Entity " + masterEntity.toString() + " at: " + masterEntity.getX() + ", " + masterEntity.getY() + ", " + masterEntity.getZ());
			if(this.isUseableByPlayer(par5EntityPlayer))
				masterEntity.activate(world, this.getX()+mx, this.getY()+my, this.getZ()+mz, par5EntityPlayer);
		}
	}
	
	public void InitMultiblock(ITileEntityMultiblock master)
	{
		this.masterEntity = master;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(worldObj != null && !loaded)
		{
			TileEntity te = worldObj.getTileEntity(this.getX()+mx, this.getY()+my, this.getZ()+mz);
			if(te instanceof ITileEntityMultiblock)
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
			ConTier=0;
			break;
		case 1:
			InsTier=0.5;
			ConTier=0.5;
			break;
		case 2:
			InsTier=0;
			ConTier=1;
			break;
		case 3:
			InsTier=0.5;
			ConTier=1.5;
			break;
		case 4:
			InsTier=1.5;
			ConTier=0.5;
			break;
		case 5:
			InsTier=1;
			ConTier=1;
			break;
		case 6:
			InsTier=1.5;
			ConTier=2.5;
			break;
		case 7:
			InsTier=2.5;
			ConTier=1.5;
			break;
		case 8:
			InsTier=2;
			ConTier=2;
			break;
		case 9:
			InsTier=2.5;
			ConTier=3.5;
			break;
		case 10:
			InsTier=3.5;
			ConTier=2.5;
			break;
		case 11:
			InsTier=3;
			ConTier=3;
			break;
		case 12:
			InsTier=3.5;
			ConTier=4.5;
			break;
		case 13:
			InsTier=4.5;
			ConTier=3.5;
			break;
		case 14:
			InsTier=4;
			ConTier=4;
			break;
		case 15:
			InsTier=4.5;
			ConTier=5.5;
			break;
		case 16:
			InsTier=5.5;
			ConTier=4.5;
			break;
		case 17:
			InsTier=5;
			ConTier=5;
			break;
		case 18:
			InsTier=10;
			ConTier=10;
			break;
		}
		
		//Apply secondary material modifiers
		switch(secondaryMaterial)
		{
		case 0:
			break;
		case 1:
			InsTier += InsTier*0.6;
			ConTier -= ConTier*0.5;
			break;
		case 2:
			InsTier += InsTier;
			ConTier -= ConTier*0.5;
			break;
		case 3:
			ConTier += ConTier*0.6;
			InsTier -= InsTier*0.5;
			break;
		case 4:
			ConTier += ConTier;
			InsTier -= InsTier*0.5;
			break;
		}
		
		this.ConductionTier = ConTier;
		this.InsulationTier = InsTier;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(masterEntity == null)
			return new int[0];
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] getTier(int damage) {
		// TODO Auto-generated method stub
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