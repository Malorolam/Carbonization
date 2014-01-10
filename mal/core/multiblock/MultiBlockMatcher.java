package mal.core.multiblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import mal.carbonization.carbonization;

/*
 * Will build a pattern of blocks
 */
public class MultiBlockMatcher {
	
	private int xdiff;
	private int ydiff;
	private int zdiff;
	
	//efficiency of the side/top[0] and base [1]
	private float[] efficiency = new float[2];
	
	/**
	 * patterns are related to a starting position x,y,z at 0,0,0 on the pattern
	 * the x, y, and z values for the world location increase from here, so all reference points are from the lowest, most negative
	 * point on the shape
	 */
	private Multiblock[][][] pattern;

	public MultiBlockMatcher(int xdiff, int ydiff, int zdiff)
	{
		this.xdiff=xdiff;
		this.ydiff=ydiff;
		this.zdiff=zdiff;
		
		efficiency[0]=-1;
		efficiency[1]=-1;
		
		pattern = new Multiblock[xdiff][ydiff][zdiff];
		//Initialize the pattern with "air"
		buildSolid(0,0,0,xdiff-1,ydiff-1,zdiff-1,0,(byte)0);
	}
	
	/*
	 * return the pattern for comparisons and equality
	 */
	public Multiblock[][][] getPattern()
	{
		return pattern;
	}
	
	public float[] getEfficiency(Multiblock[][][] ipattern)
	{
		if(efficiency[0]!=-1 && efficiency[1]!=-1)
			return efficiency;
		else
			return calculateEfficiency(ipattern);
	}
	
	public float[] calculateEfficiency(Multiblock[][][] ipattern)
	{
		efficiency[0]=0;
		efficiency[1]=0;
		
		for(int i=0;i<xdiff;i++)
		{
			for (int j=0;j<ydiff;j++)
			{
				for (int k=0;k<zdiff;k++)
				{
					efficiency[1]+=ipattern[i][j][k].ConductionTier;
					efficiency[0]+=ipattern[i][j][k].InsulationTier;
				}
			}
		}
		efficiency[0] = efficiency[0]/getSize(false);
		efficiency[1] = efficiency[1]/getSize(true);
		return efficiency;
	}
	
	public int getSize(boolean base)
	{
		if(base)//base area
			return pattern.length*pattern[0][0].length;
		else
			return (2*pattern.length*(pattern[0].length-2)+2*(pattern[0][0].length-2)*(pattern[0].length-2)+pattern.length*pattern[0][0].length);
	}
	
	/*
	 * Compare the input pattern with this one and return if they are equal or not
	 * Since we know that sometimes the first block won't match the "perfect" pattern, we can exclude it
	 */
	public boolean comparePattern(Multiblock[][][] test_pattern, boolean excludeFirstBlock)
	{
		//easy bits first, see if they are the same dimensions
		if(pattern.length != test_pattern.length || pattern[0].length != test_pattern[0].length || pattern[0][0].length != test_pattern[0][0].length)
		{
			System.err.println("Compare process failed: Patterns not the same size.");
			return false;
		}
		
		//now the fun bit, go through the entire (meep) pattern until we reach the end or find a different value
		for(int i = 0; i<pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					boolean exclude = false;
					if(excludeFirstBlock && i==0&&j==0&&k==0)
							exclude = true;
					if(pattern[i][j][k].compare(test_pattern[i][j][k],true) && !exclude)
					{
						System.err.println("Compare process ended at index ("+i+", "+j+", "+k+") with failed match.");
						return false;
					}
				}
		//System.out.println("Compare process completed with success.");
		return true;
	}
	
	/*
	 * Overloaded version that excludes the first block
	 */
	public boolean comparePattern(Multiblock[][][] test_pattern)
	{
		return comparePattern(test_pattern, true);
	}
	
	/*
	 * Compare a specific block of the input pattern, indicated by the index
	 */
	public boolean comparePatternBlock(Multiblock[][][] test_pattern, int i, int j, int k)
	{
		if(test_pattern[i][j][k].compare(pattern[i][j][k], true))
			return true;
		return false;
	}
	
	/*
	 * Compare the input pattern, allowing for a block to be substituted a set number of times instead of the pattern
	 */
	public boolean comparePatternWithSubstitutions(Multiblock[][][] test_pattern, Multiblock exceptionBlock, int exceptionCount, int xstart, int ystart, int zstart, World world)
	{
		//Make sure that there is an exception block in the first place
		if(exceptionBlock == null || exceptionCount==0)
		{
			System.out.println("No exceptions allowed, using basic comparison instead.");
			return comparePattern(test_pattern);
		}
		
		//easy bits first, see if they are the same dimensions
		if(pattern.length != test_pattern.length || pattern[0].length != test_pattern[0].length || pattern[0][0].length != test_pattern[0][0].length)
		{
			System.err.println("Compare process failed: Patterns not the same size.");
			return false;
		}
		
		int count = 0;
		
		//now the fun bit, go through the entire (meep) pattern until we reach the end or find a different value
		for(int i = 0; i<pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					//System.out.println(i + ", " + j + ", " + k);
					if(!pattern[i][j][k].compare(test_pattern[i][j][k],false))
					{
						if(test_pattern[i][j][k].compare(exceptionBlock,false) && count<exceptionCount)
						{
							count++;
							//System.out.println("Compare process bypassed substituted block ID: " + exceptionBlock.blockID + ", metadata: " + exceptionBlock.data
							//		+ "; instances bypassed: "+count+"/"+exceptionCount+".");
								
						}
						else
						{
							if(pattern[i][j][k].blockID == 0)
							{
								if(Block.blocksList[test_pattern[i][j][k].blockID] == null || Block.blocksList[test_pattern[i][j][k].blockID].isAirBlock(world, xstart+i, ystart+j, zstart+k))
								{
									
								}
								else
									return false;
							}
							else
								return false;
						}
					}

				}
		//System.out.println("Compare process completed with success.");
		return true;
	}
	
	/*
	 * Build a rectangular solid of a certain block type
	 * May have to change blockID to reference a special object that can be metadata specific
	 * ...or make another array for metadata... whatever, I'll figure it out later
	 */
	public boolean buildSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID, int data)
	{	
		//make sure none of the min values is less than 0, since that makes arrays explode
		if(minX < 0 || minY < 0 || minZ < 0)
		{
			System.err.println("Build Solid process failed: Minumum values less than zero.");
			return false;
		}
		
		//make sure none of the max values exceeds the length of pattern-1 in that axis, since arrays don't like that either
		if(maxX >= xdiff || maxY >= ydiff || maxZ >= zdiff)
		{
			System.err.println("Build Solid process failed: Maximum values exceed pattern size: Values are: " 
		+ maxX + ", " + maxY + ", " + maxZ + "; Pattern values are: " + xdiff + ", " + ydiff + ", " + zdiff + ".");
			return false;
		}
		
		//build the solid, since there is no variation, it's pretty easy.
		for(int i = minX; i<=maxX; i++)
		{
			for(int j=minY; j<=maxY; j++)
			{
				for(int k=minZ; k<=maxZ; k++)
				{
					pattern[i][j][k] = new Multiblock(blockID, data, false); 
				}
			}
		}
		return true;
	}
	
	/**
	 * Now we get into wacky-land...
	 * For more complex shapes we'll have to assemble the shape by section
	 * For now, just have a way to make a hollow rectangular solid, which buildSolid above will fulfill
	 **/
	
	/*
	 * This eventually will take in an array of strings, much like how recipes work, and build the correct shape
	 * Since we have 3d, things are a bit more complicated, so probably won't use a short string system with relationships
	 * and probably have key words with parameters instead.
	 * The format is, in differnt objects, String shape, String location, Multiblock block
	 * Will continue making this work at some other time
	 */
	/*public boolean buildComplexSolid(Object[] shape, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		boolean succ = false;
		if(shape.length%3 != 0)
		{
			System.err.println("Build Shape Process failed: Shape not correct format.");
			return false;
		}
		
		for(int i=0; i< shape.length; i+=3)
		{
			Multiblock block = (Multiblock)shape[2+i];
			if(((String)shape[i]).equals("hollow") )
			{
				int thickness = Integer.parseInt((String)shape[i+1]);
				succ = buildHollowSolid(minX, minY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata, thickness);
			}
			else if (((String)shape[i]).equals("plate"))
			{
				if (((String)shape[i+1]).equals("top"))
				{
					succ = buildSolid(minX, maxY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata);
				}
				else if (((String)shape[i+1]).equals("bottom"))
				{
					succ = buildSolid(minX, minY, minZ, maxX, minY, maxZ, block.blockID, block.blockMetadata);
				}
				else if (((String)shape[i+1]).equals("north"))
				{
					succ = buildSolid(minX, minY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata);
				}
				else if (((String)shape[i+1]).equals("south"))
				{
					succ = buildSolid(minX, maxY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata);
				}
				else if (((String)shape[i+1]).equals("east"))
				{
					succ = buildSolid(minX, maxY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata);
				}
				else if (((String)shape[i+1]).equals("west"))
				{
					succ = buildSolid(minX, maxY, minZ, maxX, maxY, maxZ, block.blockID, block.blockMetadata);
				}
				else
				{
					System.err.println("Build Shape Process failed: Invalid side.");
					return false;
				}
			}
			else
			{
				System.err.println("Build Shape Process failed: Invalid shape.");
				return false;
			}
			
			if(!succ)
			{
				System.err.println("build Shape Process failed: Previous process failed.");
				return false;
			}
		}
		return false;
	}*/
	
	/*
	 * Build a homogeneous hollow rectangular solid
	 */
	public boolean buildHollowSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID, int data, int wallThickness)
	{			
		//make sure none of the min values is less than 0, since that makes arrays explode
		if(minX < 0 || minY < 0 || minZ < 0)
		{
			System.err.println("Build Hollow Solid process failed: Minumum values less than zero.");
			return false;
		}
		
		//make sure none of the max values exceeds the length of pattern-1 in that axis, since arrays don't like that either
		if(maxX >= xdiff || maxY >= ydiff || maxZ >= zdiff)
		{
			System.err.println("Build Hollow Solid process failed: Maximum values exceed pattern size: Values are: " 
		+ maxX + ", " + maxY + ", " + maxZ + "; Pattern values are: " + xdiff + ", " + ydiff + ", " + zdiff + ".");
			return false;
		}
		
		//if any wall is smaller then 3 (0-2) then there can't be a hollow inside it, so don't bother and use the solid code instead
		if(maxX-minX < 2 || maxY-minY < 2 || maxZ-minZ < 2)
		{
			System.err.println("Build Hollow Solid process redundant: Geometry Invalid, using Build Solid instead.");
			return buildSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID, data);
		}
		
		//make sure that with the wallThickness there is a solid and it isn't sticking walls in itself
		if(wallThickness <= 0 || (wallThickness*2 > maxX-minX && wallThickness*2 > maxY-minY && wallThickness*2 > maxZ-minZ))
		{
			System.err.println("Build Hollow Solid process failed: Wall thickness invalid: " + wallThickness);
			return false;
		}
		
		boolean success=true;
		
		//Now build the solid positive space first
		//then negative space
		//Exterior shape
		success=buildSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID, data);
		if(success==false)
		{
			System.err.println("Build Hollow Solid process failed: Previous process failed.");
			return false;
		}
		
		//interior space
		success=buildSolid(minX+wallThickness, minY+wallThickness, minZ+wallThickness, maxX-wallThickness, maxY-wallThickness, maxZ-wallThickness, 0, (byte)0);
		if(success==false)
		{
			System.err.println("Build Hollow Solid process failed: Previous process failed.");
			return false;
		}
		
		return true;
	}
	
	/*
	 * Build a hollow rectangular solid whose base is a different block
	 */
	public boolean buildBasedHollowSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID1, int data1, int blockID2, int data2, int wallThickness)
	{
		if(blockID1==blockID2 && data1==data2)
			return buildHollowSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID1, data1, wallThickness);
		
		boolean succ = buildHollowSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID1, data1, wallThickness);
		if(!succ)
		{
			System.err.println("Build Process Failed Pass 1: Previous process failed.");
			return false;
		}
		succ = buildSolid(minX, minY, minZ, maxX, minY, maxZ, blockID2, data2);
		if(!succ)
		{
			System.err.println("Build Process Failed Pass 2: Previous process failed.");
			return false;
		}
		return true;
	}
	
	/*
	 * set a specific location in the pattern to be a certain id
	 */
	public boolean setBlock(int i, int j, int k, int blockID, int l, boolean metadata)
	{
		//Make sure that the index is within the pattern
		if(i<0||j<0||k<0||i>=pattern.length||j>=pattern[0].length||k>=pattern[0][0].length)
		{
			System.err.println("Build Single Block process failed: index outside pattern");
			return false;
		}
		
		pattern[i][j][k] = new Multiblock(blockID, l, metadata);
		
		return true;
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