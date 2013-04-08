package mal.carbonization.multiblock;

import net.minecraft.world.World;

//Used to calculate and create the multiblock
//Totally doesn't work right...
public class MultiBlockInstantiator {

	private MBType type;
	
	public MultiBlockInstantiator(MBType type)
	{
		this.type = type;
	}
	
	/*
	 * Build a multiblock from a successful pattern
	 */
	public static boolean createMultiBlock(MultiBlockMatcher mbMatch, int x, int y, int z, World world)
	{
		int[][][] pattern = mbMatch.getPattern();
		//For testing purposes, just start at 1,1,1 from the initiation location 
		for(int i = 0; i<pattern.length; i++)
			for(int j=0;j<pattern[0].length;j++) 
				for(int k=0;k<pattern[0][0].length;k++)
				{
					world.setBlock(x+i, y+j, z+k, pattern[i][j][k], 0, 2);
				}
		return true;
	}
	
	/*
	 * Build a multibBlockMatcher following the blocks in a volume
	 */
	public static boolean createWorldMultiBlock(MultiBlockMatcher mbMatch, int startX, int startY, int startZ, int xSize, int ySize, int zSize, World world)
	{
		for(int i=0;i<mbMatch.getPattern().length;i++)
			for(int j=0; j<mbMatch.getPattern()[0].length;j++)
				for(int k=0; k<mbMatch.getPattern()[0][0].length;k++)
				{
					//We don't want to pick up a tile entity'd block, so make sure there isn't one here
					if(world.getBlockTileEntity(i+startX, j+startY, k+startZ) != null)
					{
						System.err.println("Detect process failed at indices: "+i+", "+j+", "+k+": Tile Entity Present.");
						return false;
					}
					boolean succ = mbMatch.setBlock(i, j, k, world.getBlockId(i+startX, j+startY, k+startZ));
					if(succ==false)
					{
						System.err.println("Detect process failed at indices: "+i+", "+j+", "+k+": Previous process failed.");
						return succ;
					}
				}
		return true;
	}
}

/*
 * This stores all the different structures and any requirements that they may have
 */
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
	
	/*
	 * x => 0
	 * y => 1
	 * z => 2
	 */
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