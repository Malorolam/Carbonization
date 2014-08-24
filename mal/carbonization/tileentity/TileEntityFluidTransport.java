package mal.carbonization.tileentity;

import java.util.ArrayList;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.network.CarbonizationPacketHandler;
import mal.carbonization.network.FluidTransportMessageClient;
import mal.carbonization.network.FluidTransportMessageServer;
import mal.core.api.IMachineUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFluidTransport extends TileEntity implements IFluidHandler, IInventory{

	private FluidTank tank;
	public ItemStack[] upgrades = new ItemStack[3];
	public byte[] sideStates = new byte[6];//the sides the transport attempts to export or import
	public int maxTransferAmount = 1000;//maximum amount of fluid transferred per period
	public int maxTransferDelay = 20;//max delay to transfer
	public int delayCooldown = 0;//delay until next transfer attempt
	
	public TileEntityFluidTransport()
	{
		tank = new FluidTank(null,20000);
		
		sideStates[0]=0;
		sideStates[1]=0;
		sideStates[2]=0;
		sideStates[3]=0;
		sideStates[4]=0;
		sideStates[5]=0;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(worldObj != null && !worldObj.isRemote)
		{
			updateUpgrade();
			
			if(delayCooldown>0) {
				delayCooldown--;
				return;
			}
			else {
				//scale the transfer based off of transfer directions
				int scalingfactor = 0;
				for(int i = 0; i<sideStates.length; i++)
				{
					if(sideStates[i] != 0)
						scalingfactor++;
				}
				//obtain arrays of TEs for import and export
				for(int i = 0; i<sideStates.length; i++)
				{
					if(sideStates[i]==0)//state for nothing
					{}
					else if(sideStates[i]==1)//state for export
					{
						IFluidHandler te = getFluidTE(ForgeDirection.getOrientation(i));
						if(te != null)
						{
							int diff = te.fill(ForgeDirection.getOrientation(i).getOpposite(), tank.drain(maxTransferAmount/scalingfactor, false), true);
							tank.drain(diff,true);
							if(tank.getFluidAmount()==0)
								tank.setFluid(null);
						}
					}
					else if(sideStates[i]==2)//state for import
					{
						IFluidHandler te = getFluidTE(ForgeDirection.getOrientation(i));
						if(te != null)
						{
							
							int j = tank.fill(te.drain(ForgeDirection.getOrientation(i).getOpposite(), maxTransferAmount/scalingfactor, false),true);
							if(j>0)
								te.drain(ForgeDirection.getOrientation(i).getOpposite(), j, true);
							//System.out.println(j + " " + tank.getFluidAmount() + "/" + tank.getCapacity());
						}
					}
				}
				delayCooldown=maxTransferDelay;
			}
			
			//System.out.println(sideStates[0]);
			CarbonizationPacketHandler.instance.sendToAll(new FluidTransportMessageServer(this));//, new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 64));
		}
		else {

		}
		//System.out.println(worldObj.isRemote + " " + sideStates[1]);
	}
	
	private void updateUpgrade()
	{
		if(upgrades == null)
			return;
		
		maxTransferAmount = 1000;
		maxTransferDelay = 20;
		int storageval = 0;
		double hastemultiplyer = 1;
		
		for(ItemStack is: upgrades)
		{
			if(is==null){}
			else if(is.getItem() instanceof IMachineUpgrade)
			{
				int damage = is.getItemDamage();
				String upgrade = ((IMachineUpgrade)is.getItem()).getUpgradeName(damage);
				
				if(upgrade.equalsIgnoreCase("storage1"))
					storageval+=1;
				else if(upgrade.equalsIgnoreCase("storage2"))
					storageval+=2;
				else if(upgrade.equalsIgnoreCase("storage3"))
					storageval+=3;
				else if(upgrade.equalsIgnoreCase("storage4"))
					storageval+=4;
				else if(upgrade.equalsIgnoreCase("storage5"))
					storageval+=5;
				else if(upgrade.equalsIgnoreCase("haste1"))
					hastemultiplyer -= hastemultiplyer*0.1;
				else if(upgrade.equalsIgnoreCase("haste2"))
					hastemultiplyer -= hastemultiplyer*0.3;
				else if(upgrade.equalsIgnoreCase("haste3"))
					hastemultiplyer -= hastemultiplyer*0.5;
				else if(upgrade.equalsIgnoreCase("haste4"))
					hastemultiplyer -= hastemultiplyer*0.7;
				else if(upgrade.equalsIgnoreCase("haste5"))
					hastemultiplyer -= hastemultiplyer*0.9;
				else if(upgrade.equalsIgnoreCase("efficiency1"))
					maxTransferAmount = (int) (maxTransferAmount*1.1);
				else if(upgrade.equalsIgnoreCase("efficiency2"))
					maxTransferAmount = (int) (maxTransferAmount*1.3);
				else if(upgrade.equalsIgnoreCase("efficiency3"))
					maxTransferAmount = (int) (maxTransferAmount*1.5);
				else if(upgrade.equalsIgnoreCase("efficiency4"))
					maxTransferAmount = (int) (maxTransferAmount*1.7);
				else if(upgrade.equalsIgnoreCase("efficiency5"))
					maxTransferAmount = (int) (maxTransferAmount*1.9);
			}
		}
		
		tank.setCapacity(20000+1000*storageval);
		maxTransferDelay = (int)Math.ceil(20*hastemultiplyer);
		if(maxTransferDelay<1)
			maxTransferDelay = 1;
	}
	
	public void changeDirectionState(int direction)
	{
		System.out.println(worldObj.isRemote + " " + direction);
		if(sideStates[direction]<2)
			sideStates[direction]++;
		else
			sideStates[direction]=0;
	}
	
	public void changeDirectionState(int direction, byte state)
	{
		sideStates[direction] = state;
	}
	
	@SideOnly(Side.CLIENT)
	public double getFuelCapacityScaled(int i)
	{
		return tank.getFluidAmount()*i/(tank.getCapacity());
	}
	
	private IFluidHandler getFluidTE(ForgeDirection direction)
	{
		TileEntity te = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
		if(te instanceof IFluidHandler)
			return (IFluidHandler) te;
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tank.writeToNBT(nbt);
		
		nbt.setByteArray("sideStates", sideStates);
		nbt.setInteger("maxTransferAmount", maxTransferAmount);
		nbt.setInteger("maxTransferDelay", maxTransferDelay);
		nbt.setInteger("delayCooldown", delayCooldown);
		
		NBTTagList upgradestack = new NBTTagList();
		for(int i = 0; i < upgrades.length; i++)
		{
			if(upgrades[i] != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				upgrades[i].writeToNBT(item);
				upgradestack.appendTag(item);
			}
		}
		
		nbt.setTag("upgradeStack", upgradestack);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		
		sideStates = nbt.getByteArray("sideStates");
		maxTransferAmount = nbt.getInteger("maxTransferAmount");
		maxTransferDelay = nbt.getInteger("maxTransferDelay");
		delayCooldown = nbt.getInteger("delayCooldown");
		
		NBTTagList upgradestack = nbt.getTagList("upgradeStack", 10);
		for(int i = 0; i < upgradestack.tagCount(); i++)
		{
			NBTTagCompound item = upgradestack.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");
			if(slot>=0 && slot<upgrades.length)
				upgrades[slot] = ItemStack.loadItemStackFromNBT(item);
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CarbonizationPacketHandler.instance.getPacketFrom(new FluidTransportMessageServer(this));
	}
	
	public void sendChangePacket(byte side)
	{
		CarbonizationPacketHandler.instance.sendToServer(new FluidTransportMessageClient(this, side));
	}
	
	public FluidTank getTank(){
		return tank;
	}
	
	public void setTank(FluidTank tank){
		this.tank = tank;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;//lazy
	}

	@Override
	public int getSizeInventory() {
		return upgrades.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot>=0 && slot<upgrades.length)
			return upgrades[slot];
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int dec) {
		if(slot>=0 && slot<upgrades.length)
		{
			if (upgrades[slot] != null)
			{
				ItemStack itemstack;

				if (upgrades[slot].stackSize <= dec)
				{
					itemstack = upgrades[slot];
					this.upgrades[slot] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.upgrades[slot].splitStack(dec);

					if (this.upgrades[slot].stackSize == 0)
					{
						this.upgrades[slot] = null;
					}

					return itemstack;
				}
			}
		}
		
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot>=0 && slot<upgrades.length)
			return upgrades[slot];
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		if(slot>=0&&slot<upgrades.length)
			upgrades[slot]=is;
	}

	@Override
	public String getInventoryName() {
		return "Fluidtransport";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack is) {
		if(is.getItem() instanceof IMachineUpgrade)
			return true;
		return false;
	}

	public void activate(World world, int x, int y, int z, EntityPlayer player)
	{
		player.openGui(carbonization.carbonizationInstance, 1, world, x, y, z);
	}
	
	public FluidStack getFluid()
	{
		return tank.getFluid();
	}
	
	public int getMaxCapacity()
	{
		return tank.getCapacity();
	}
	
	public int getFluidAmount()
	{
		return tank.getFluidAmount();
	}
	
	public String getExportSides()
	{
		String s = "Exporting to: ";
		for(int i = 0; i < sideStates.length; i++)
			if(sideStates[i] == 1)
				s += ForgeDirection.getOrientation(i).name() + " ";
		return s;
	}
	
	public String getImportSides()
	{
		String s = "Importing from: ";
		for(int i = 0; i < sideStates.length; i++)
			if(sideStates[i] == 2)
				s += ForgeDirection.getOrientation(i).name() + " ";
		return s;
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