package mal.carbonization.multiblock;

/*
 * Will build a pattern of blocks
 */
public class MultiBlockMatcher {
	
	private int xdiff;
	private int ydiff;
	private int zdiff;
	
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
	
	/*
	 * Compare the input pattern with this one and return if they are equal or not
	 * Since we know that sometimes the first block won't match the "perfect" pattern, we can exclude it
	 */
	public boolean comparePattern(Multiblock[][][] test_pattern, boolean excludeFirstBlock)
	{
		//easy bits first, see if they are the same dimensions
		if(pattern.length != test_pattern.length || pattern[0].length != test_pattern[0].length || pattern[0][0].length != test_pattern[0][0].length)
			return false;
		
		//now the fun bit, go through the entire (meep) pattern until we reach the end or find a different value
		for(int i = 0; i<pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					boolean exclude = false;
					if(excludeFirstBlock && i==0&&j==0&&k==0)
							exclude = true;
					if(pattern[i][j][k].compare(test_pattern[i][j][k],true) && !exclude)
						return false;
				}
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
	 * Compare the input pattern, allowing for a block to be substituted a set number of times instead of the pattern
	 */
	public boolean comparePatternWithSubstitutions(Multiblock[][][] test_pattern, Multiblock exceptionBlock, int exceptionCount)
	{
		//Make sure that there is an exception block in the first place
		if(exceptionBlock == null)
			return comparePattern(test_pattern);
		
		//easy bits first, see if they are the same dimensions
		if(pattern.length != test_pattern.length || pattern[0].length != test_pattern[0].length || pattern[0][0].length != test_pattern[0][0].length)
			return false;
		
		int count = 0;
		
		//now the fun bit, go through the entire (meep) pattern until we reach the end or find a different value
		for(int i = 0; i<pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					if(!pattern[i][j][k].compare(test_pattern[i][j][k],true))
					{
						if(pattern[i][j][k].compare(exceptionBlock,true) && count<exceptionCount)
						{
							count++;
						}
						else
						{
							return false;
						}
					}
				}
		return true;
	}
	
	/*
	 * Build a rectangular solid of a certain block type
	 * May have to change blockID to reference a special object that can be metadata specific
	 * ...or make another array for metadata... whatever, I'll figure it out later
	 */
	public boolean buildSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID, byte metadata)
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
					pattern[i][j][k] = new Multiblock(blockID, metadata); 
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
	public boolean buildComplexSolid(Object[] shape, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
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
	}
	
	/*
	 * Build a homogeneous hollow rectangular solid
	 */
	public boolean buildHollowSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID, byte metadata, int wallThickness)
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
		
		//make sure that with the wallThickness there is a solid and it isn't sticking walls in itself
		if(wallThickness <= 0 || wallThickness >= Math.max(Math.max(xdiff/2, ydiff/2), zdiff/2))
		{
			System.err.println("Build Hollow Solid process failed: Wall thickness invalid.");
			return false;
		}
		
		boolean success=true;
		
		//Now build the solid one wall at a time
		//after each step make sure it didn't fail
		//After a corner axis is completed, other processes on that axis must be reduced to prevent issues
		//Exterior shape
		success=buildSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID, metadata);
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
	public boolean buildBasedHollowSolid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID1, byte metadata1, int blockID2, byte metadata2, int wallThickness)
	{
		if(blockID1==blockID2 && metadata1==metadata2)
			return buildHollowSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID1, metadata1, wallThickness);
		
		boolean succ = buildHollowSolid(minX, minY, minZ, maxX, maxY, maxZ, blockID1, metadata1, wallThickness);
		if(!succ)
		{
			System.err.println("Build Process Failed Pass 1: Previous process failed.");
			return false;
		}
		succ = buildSolid(minX, minY, minZ, maxX, minY, maxZ, blockID2, metadata2);
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
	public boolean setBlock(int i, int j, int k, int blockID, byte metadata)
	{
		//Make sure that the index is within the pattern
		if(i<0||j<0||k<0||i>=pattern.length||j>=pattern[0].length||k>=pattern[0][0].length)
		{
			System.err.println("Build Single Block process failed: index outside pattern");
			return false;
		}
		
		pattern[i][j][k] = new Multiblock(blockID, metadata);
		return true;
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