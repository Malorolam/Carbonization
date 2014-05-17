package mal.core.multiblock;

import mal.carbonization.carbonization;
import net.minecraft.block.Block;
//Stores a block and it's location, as well as it's tier if it's one of the valid multiblock blocks
import net.minecraft.item.ItemStack;

public class Multiblock {

	public int blockID;
	public int baseMaterial;
	public int secondaryMaterial;
	public int purpose;
	public int data;
	public double InsulationTier;
	public double ConductionTier;
	public boolean isMetadata;
	
	public Multiblock(int blockID, int base, int sec, int purp, double it, double ct)
	{
		this.blockID = blockID;
		this.baseMaterial = base;
		this.secondaryMaterial = sec;
		this.purpose = purp;
		this.data = purpose*1000+secondaryMaterial*100+baseMaterial;
		this.InsulationTier = it;
		this.ConductionTier = ct;
		this.isMetadata = false;
	}
	
	public Multiblock(int blockID, int data, double[] tier, boolean metadata)
	{
		this.data = data;
		this.blockID = blockID;
		if(tier != null)
		{
			this.InsulationTier = tier[0];
			this.ConductionTier = tier[1];
		}
		
		if(metadata)
		{
			this.purpose = (int)Math.floor((double)(data)/1000);
			data -= purpose*1000;
			this.secondaryMaterial = (int)Math.floor((double)(data)/100);
			data -= secondaryMaterial*100;
			this.baseMaterial = data;
		}
		else
		{
			this.purpose = data;
			this.data = data;
			this.secondaryMaterial = data;
			this.baseMaterial = data;
		}
		this.isMetadata = metadata;

		//System.out.println("Making multiblock with block: " + Block.blocksList[blockID].toString());
	}
	
	public Multiblock(int blockID, int data, boolean metadata)
	{
		this.data = data;
		this.blockID = blockID;
		if(!metadata)
		{
			this.purpose = (int)Math.floor((double)(data)/1000);
			data -= purpose*1000;
			this.secondaryMaterial = (int)Math.floor((double)(data)/100);
			data -= secondaryMaterial*100;
			this.baseMaterial = data;
		}
		else
			this.purpose = data;
		this.InsulationTier = -1;
		this.ConductionTier = -1;
		this.isMetadata = metadata;
	}
	
	public double[] getTier()
	{
		if(InsulationTier == -1 || ConductionTier == -1)
			return null;
		double[] d = new double[2];
		d[0] = this.InsulationTier;
		d[1] = this.ConductionTier;
		return d;
	}
	
	/*
	 * Compare a multiblock to this one, disregarding materials if stated
	 */
	public boolean compare(Multiblock multiblock, boolean matchMaterial)
	{
		if(multiblock == null)
		{
			System.out.println("Failed due to null block");
			return false;
		}
		
		if(matchMaterial)
		{
			if(this.blockID==multiblock.blockID && this.baseMaterial == multiblock.baseMaterial && this.secondaryMaterial == multiblock.secondaryMaterial
					&& this.purpose == multiblock.purpose)
				return true;
		}
		else
			if(this.blockID==multiblock.blockID)
			{
				if(this.isMetadata || multiblock.isMetadata)
				{
					//if(this.purpose==-1 || multiblock.purpose == -1)
						return true;
				}
				else
				{
					if(this.purpose == multiblock.purpose)
						return true;
				}
			}
		
/*		System.out.print("Blocks not the same");
		if(this.blockID!=multiblock.blockID)
			System.out.print(": Block: "+ ((blockID != 0)?(Block.blocksList[this.blockID].toString()):this.blockID)+" != " + ((multiblock.blockID!=0)?(Block.blocksList[multiblock.blockID].toString()):multiblock.blockID));
		if(this.purpose!=multiblock.purpose)
			System.out.print(": data: "+this.purpose+"!="+multiblock.purpose);
		System.out.println(".");*/
		
		return false;
	}
	
	public String toString()
	{
		ItemStack is = new ItemStack(blockID,1,data);
		return is.getDisplayName();
	}
	
	public Block toBlock()
	{
		return Block.blocksList[blockID];
	}
	
	/*
	 * Returns the block as an itemstack for silk touch purposes
	 */
	public ItemStack toItemStack()
	{
		return new ItemStack(blockID,1,data);
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