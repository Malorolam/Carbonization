package mal.carbonization.multiblock;

import mal.carbonization.carbonization;
import net.minecraft.block.Block;
//Stores a block and it's location, as well as it's tier if it's one of the valid multiblock blocks
public class Multiblock {

	public int blockID;
	public byte blockMetadata;
	public int tier=-1;
	
	public Multiblock(int blockID, byte metadata)
	{
		this.blockID = blockID;
		this.blockMetadata = metadata;
		if(blockID==carbonization.structureBlock.blockID || blockID==carbonization.structureFurnaceBlock.blockID)
		{
			this.tier=carbonization.structureBlock.getTier(blockMetadata);
		}
	}
	
	public Multiblock(int blockID, int metadata)
	{
		this.blockID = blockID;
		this.blockMetadata = (byte)metadata;
		if(blockID==carbonization.structureBlock.blockID || blockID==carbonization.structureFurnaceBlock.blockID)
		{
			this.tier=carbonization.structureBlock.getTier(blockMetadata);
		}
	}
	
	public Multiblock(Block block, int metadata)
	{
		this.blockID = block.blockID;
		this.blockMetadata = (byte)metadata;
		if(blockID==carbonization.structureBlock.blockID || blockID==carbonization.structureFurnaceBlock.blockID)
		{
			this.tier=carbonization.structureBlock.getTier(blockMetadata);
		}
	}
	
	public boolean compare(Multiblock multiblock, boolean useMetadata)
	{
		if(multiblock == null)
			return false;
		
		if(useMetadata)
		{
			if(this.blockID==multiblock.blockID && this.blockMetadata == multiblock.blockMetadata)
				return true;
		}
		else
			if(this.blockID==multiblock.blockID)
				return true;
		
		/*System.out.print("Blocks not the same");
		if(this.blockID!=multiblock.blockID)
			System.out.print(": BlockID: "+this.blockID+"!="+multiblock.blockID);
		if(this.blockMetadata!=multiblock.blockMetadata)
			System.out.print(": Metadata: "+this.blockMetadata+"!="+multiblock.blockMetadata);
		System.out.println(".");*/
		
		return false;
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