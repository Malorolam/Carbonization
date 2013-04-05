package mal.carbonization.multiblock;

//Used to calculate and create the multiblock
public class MultiBlockInstantiator {

	private MBType type;
	
	public MultiBlockInstantiator(MBType type)
	{
		this.type = type;
	}
}

enum MBType {
	
	FURNACE(9,3,9, 3,3,3, true, "HollowCube");
	
	private int[] maxValues;
	private int[] minValues;
	private boolean requiresBlocks;
	private String requiredShape;
	
	private MBType(int maxX, int maxY, int maxZ, int minX, int minY, int minZ, boolean requiresSpecialBlocks, String requiredShape)
	{
		maxValues = new int[3];
		maxValues[0]=maxX;
		maxValues[1]=maxY;
		maxValues[2]=maxZ;
		
		minValues = new int[3];
		minValues[0] = minX;
		minValues[1] = minY;
		minValues[2] = minZ;
		
		requiresBlocks = requiresSpecialBlocks;
		this.requiredShape = requiredShape;
	}
	
	public String getRequiredShape()
	{
		return requiredShape;
	}
	
	public boolean getRequiresSpecialBlocks()
	{
		return requiresBlocks;
	}
	
	public int getMaxDimension(int dim)
	{
		if(dim>=0 && dim<maxValues.length)
			return maxValues[dim];
		else
			return 0;
	}
	
	public int getMinDimension(int dim)
	{
		if(dim>=0 && dim<minValues.length)
			return minValues[dim];
		else
			return 0;
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