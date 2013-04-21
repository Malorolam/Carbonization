package mal.carbonization.multiblock;

import net.minecraft.world.World;

//Used to calculate and create the multiblock
//Totally doesn't work right...
public class MultiBlockInstantiator {

	
	public MultiBlockInstantiator()
	{
	}
	
	/*
	 * Continuously compare our pattern to what is in the world until there is a match and we return the offset
	 */
	public static int[] matchPattern(MultiBlockMatcher mbMatch, int startX, int startY, int startZ, World world, Multiblock parentBlock)
	{
		/**
		 * There are only a limited number of orientations that we will consider valid
		 * Mainly, the command block (starting position) must be on a side
		 * We can also figure out which face based off of where the air is, since we know that a common valid position will have
		 * air on opposite sides
		 * 
		 * this may be able to be made more efficient
		 */
		int[] offset = new int[3];
		Multiblock[][][] pattern = mbMatch.getPattern();
		
		//Figure out if we have an easy calculation
		int sideAxis;
		if(world.getBlockId(startX+1, startY, startZ)==0 && world.getBlockId(startX-1, startY, startZ)==0)//x axis alignment
			sideAxis = 0;
		else if(world.getBlockId(startX, startY+1, startZ)==0 && world.getBlockId(startX, startY-1, startZ)==0)//y axis alignment
			sideAxis = 1;
		else if(world.getBlockId(startX, startY, startZ+1)==0 && world.getBlockId(startX, startY, startZ-1)==0)//z axis alignment
			sideAxis = 2;
		else//we are sad and have to do lots of calculations now :[
			sideAxis = -1;
		
		System.out.println(sideAxis);
		
		//now we have limited the options to hopefully 2 planes to iterate through
		//We loop through the two axises that are used
		if(sideAxis == 0)//x axis calculations
		{
			offset[0] = 0;
			
			for(int j=0; j<mbMatch.getPattern()[0].length; j++)
				for(int k=0; k<mbMatch.getPattern()[0][0].length; k++)
				{
					MultiBlockMatcher test_matcher = new MultiBlockMatcher(pattern.length, pattern[0].length, pattern[0][0].length);
					createWorldMultiBlock(test_matcher, startX, startY+j, startZ+k, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[1] = j;
						offset[2] = k;
						return offset;
					}
					
					createWorldMultiBlock(test_matcher, startX-pattern.length, startY+j, startZ+k, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[0] = pattern.length;
						offset[1] = j;
						offset[2] = k;
						return offset;
					}
				}
		}
		if(sideAxis == 1)//y axis calculations
		{
			offset[0] = 0;
			
			for(int i=0; i<mbMatch.getPattern().length; i++)
				for(int k=0; k<mbMatch.getPattern()[0][0].length; k++)
				{
					MultiBlockMatcher test_matcher = new MultiBlockMatcher(pattern.length, pattern[0].length, pattern[0][0].length);
					createWorldMultiBlock(test_matcher, startX+i, startY, startZ+k, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[0] = i;
						offset[1] = 0;
						offset[2] = k;
						return offset;
					}
					
					createWorldMultiBlock(test_matcher, startX+i, startY-pattern[0].length, startZ+k, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[0] = i;
						offset[1] = pattern[0].length;
						offset[2] = k;
						return offset;
					}
				}
		}
		if(sideAxis == 2)//z axis calculations
		{
			offset[0] = 0;
			
			for(int i=0; i<mbMatch.getPattern().length; i++)
				for(int j=0; j<mbMatch.getPattern()[0].length; j++)
				{
					MultiBlockMatcher test_matcher = new MultiBlockMatcher(pattern.length, pattern[0].length, pattern[0][0].length);
					createWorldMultiBlock(test_matcher, startX+i, startY+j, startZ, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[0] = i;
						offset[1] = j;
						offset[2] = 0;
						return offset;
					}
					
					createWorldMultiBlock(test_matcher, startX+i, startY+j, startZ-pattern[0][0].length, pattern.length, pattern[0].length, pattern[0][0].length, world);
					if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
					{
						offset[0] = i;
						offset[1] = j;
						offset[2] = pattern[0][0].length;
						return offset;
					}
				}
		}
		if(sideAxis == -1)//the dreaded full axis calculations, since there is no information we have to examine every possible position
		{
			for(int i = 1-pattern.length; i<pattern.length; i++)
				for(int j=1-pattern[0].length; j<pattern[0].length; j++)
					for(int k=1-pattern[0][0].length; k<pattern[0][0].length; k++)
					{
						MultiBlockMatcher test_matcher = new MultiBlockMatcher(pattern.length, pattern[0].length, pattern[0][0].length);
						createWorldMultiBlock(test_matcher, startX+i, startY+j, startZ+k, pattern.length, pattern[0].length, pattern[0][0].length, world);
						if(mbMatch.comparePatternWithSubstitutions(test_matcher.getPattern(), parentBlock, 1))
						{
							offset[0] = i;
							offset[1] = j;
							offset[2] = k;
							return offset;
						}
					}
					
		}
		return null;
	}
	/*
	 * Build a multiblock from a successful pattern
	 */
	public static boolean createMultiBlock(MultiBlockMatcher mbMatch, int x, int y, int z, World world)
	{
		Multiblock[][][] pattern = mbMatch.getPattern();
		//For testing purposes, just start at 1,1,1 from the initiation location 
		for(int i = 0; i<pattern.length; i++)
			for(int j=0;j<pattern[0].length;j++) 
				for(int k=0;k<pattern[0][0].length;k++)
				{
					boolean succ = world.setBlock(x+i, y+j, z+k, pattern[i][j][k].blockID, pattern[i][j][k].blockMetadata, 2);
					//System.out.println(succ);
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
					/*if(world.getBlockTileEntity(i+startX, j+startY, k+startZ) != null)
					{
						System.err.println("Detect process failed at indices: "+i+", "+j+", "+k+": Tile Entity Present.");
						return false;
					}*/
					boolean succ = mbMatch.setBlock(i, j, k, world.getBlockId(i+startX, j+startY, k+startZ), (byte)world.getBlockMetadata(i+startX, j+startY, k+startZ));
					if(succ==false)
					{
						System.err.println("Detect process failed at indices: "+i+", "+j+", "+k+": Previous process failed.");
						return succ;
					}
				}
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