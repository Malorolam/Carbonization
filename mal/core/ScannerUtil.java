package mal.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mal.carbonization.carbonization;
import mal.core.multiblock.MultiBlockInstantiator;
import mal.core.multiblock.MultiBlockMatcher;
import mal.core.multiblock.Multiblock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

/*
 * herp derp
 * 0:bottom
 * 1:top
 * 2:north
 * 3:south
 * 4:west
 * 5:east
 */
/*
 * Methods around scanning an area of the world based off of some parameters
 */
public class ScannerUtil {

	/**
	 * Scan a section of the world and print the relevant information to the player's chat
	 */
	public static void scanWorld(World world, EntityPlayer player, int x, int y, int z, int side, int width, int height, int depth, int mode)
	{
		//first figure out the minimum point and dimensions
		int xSize=0;
		int ySize=0;
		int zSize=0;
		int xMin=0;
		int yMin=0;
		int zMin=0;
		int playerFace = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;//0s 1w 2n 3e
		
		switch(side)
		{
		case 0://depth is y, get x and z from player
			ySize = depth;
			yMin = y;
			switch(playerFace)
			{
			case 0:
				xSize = width;
				zSize = height;
				break;
			case 1:
				xSize = height;
				zSize = width;
				break;
			case 2:
				xSize = width;
				zSize = height;
				break;
			case 3:
				xSize = height;
				zSize = width;
				break;
			}
			xMin = x-xSize/2;
			zMin = z-zSize/2;
			break;
		case 1:
			ySize = depth;
			yMin = y-ySize+1;
			switch(playerFace)
			{
			case 0:
				xSize = width;
				zSize = height;
				break;
			case 1:
				xSize = height;
				zSize = width;
				break;
			case 2:
				xSize = width;
				zSize = height;
				break;
			case 3:
				xSize = height;
				zSize = width;
				break;
			}
			xMin = x-xSize/2;
			zMin = z-zSize/2;
			break;
		case 2:
			xSize = width;
			ySize = height;
			zSize = depth;
			xMin = x-xSize/2;
			yMin = y-ySize/2;
			zMin = z;
			break;
		case 3:
			xSize = width;
			ySize = height;
			zSize = depth;
			xMin = x-xSize/2;
			yMin = y-ySize/2;
			zMin = z-zSize+1;
			break;
		case 4:
			xSize = depth;
			ySize = height;
			zSize = width;
			xMin = x;
			yMin = y-ySize/2;
			zMin = z-zSize/2;
			break;
		case 5:
			xSize = depth;
			ySize = height;
			zSize = width;
			xMin = x-xSize+1;
			yMin = y-ySize/2;
			zMin = z-zSize/2;
			break;
		}
		player.addChatMessage("xSize: " + xSize + "; ySize: " + ySize + "; zSize: " + zSize );//+ "; side: " + side + "; playerFace: " + playerFace);
		player.addChatMessage("xMin: " + xMin + "; yMin: " + yMin + "; zMin: " + zMin);
		
		//then make a pattern around using the MultiBlock code
		MultiBlockMatcher match = new MultiBlockMatcher(xSize,ySize,zSize);
		MultiBlockInstantiator.createWorldMultiBlock(match, xMin, yMin, zMin, xSize, ySize, zSize, world);
		//MultiBlockInstantiator.createTestArea(match, xMin, yMin, zMin, world, Block.blockDiamond);
		
		//then interpret the results
		List<String> list = new ArrayList<String>();
		if(mode==0)
			list = basicMode(match.getPattern(), world, xMin, yMin, zMin);
		if(mode==1)
			list = expandedMode(match.getPattern(), world, xMin, yMin, zMin);
			
		//then display the correct ones to the player
		for(int i = 0; i < list.size(); i++)
		{
			player.addChatMessage(list.get(i));
		}
	}
	
	/**
	 * Basic mode shows:
	 * total blocks, total volume, maximum tool required, average tool required
	 */
	private static List<String> basicMode(Multiblock[][][] pattern, World world, int startX, int startY, int startZ)
	{
		int totalBlocks = 0;
		int totalVolume = pattern.length*pattern[0].length*pattern[0][0].length;
		int maxToolRequired = -100;
		//int totalToolRequired = 0;
		
		for(int i = 0; i < pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					if(pattern[i][j][k]!=null && !world.isAirBlock(startX+i, startY+j, startZ+k))
					{
						totalBlocks++;
						int toolR = getBlockHarvestLevel(pattern[i][j][k].blockID, pattern[i][j][k].data, world, startX+i, startY+j, startZ+k);
						//totalToolRequired += (toolR>0)?(toolR):(0);
						if(toolR > maxToolRequired)
							maxToolRequired = toolR;
					}
				}
		double percent = ((double)totalBlocks)/((double)totalVolume)*100;
		//double avgTool = ((double)totalToolRequired)/((double)totalBlocks);
		List<String> list = new ArrayList<String>();
		list.add("Volume: " + totalBlocks + "/" + totalVolume + " (" + String.format("%.2f", percent) + "% solid)");
		list.add("Tool material required to harvest all: " + convertToolLevel(maxToolRequired));// + ((totalBlocks>1)?("; Average tool material required: " + convertToolLevel((int)Math.ceil(avgTool))):("")));
		
		return list;
	}
	
	/**
	 * Shows a list of every kind of block and it's number of instances in addition to the other information
	 */
	private static List<String> expandedMode(Multiblock[][][] pattern, World world, int startX, int startY, int startZ)
	{
		int totalBlocks = 0;
		int totalVolume = pattern.length*pattern[0].length*pattern[0][0].length;
		int maxToolRequired = -100;
		//int totalToolRequired = 0;
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		for(int i = 0; i < pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					if(pattern[i][j][k]!=null && !world.isAirBlock(startX+i, startY+j, startZ+k))
					{
						totalBlocks++;
						int toolR = getBlockHarvestLevel(pattern[i][j][k].blockID, pattern[i][j][k].data, world, startX+i, startY+j, startZ+k);
						//totalToolRequired += (toolR>0)?(toolR):(0);
						if(toolR > maxToolRequired)
							maxToolRequired = toolR;
						addBlocktoMap(pattern[i][j][k], 1, map);
					}
				}
		double percent = ((double)totalBlocks)/((double)totalVolume)*100;
		//double avgTool = ((double)totalToolRequired)/((double)totalBlocks);
		List<String> list = new ArrayList<String>();
		list.add("Volume: " + totalBlocks + "/" + totalVolume + " (" + String.format("%.2f", percent) + "% solid)");
		list.add("Tool material required to harvest all: " + convertToolLevel(maxToolRequired));// + ((totalBlocks>1)?("; Average tool material required: " + convertToolLevel((int)Math.ceil(avgTool))):("")));
		
		List<String> l = new ArrayList<String>();
		for(String s : map.keySet())
		{
			l.add(s + ": " + map.get(s));
		}
		for(int i = 0; i<l.size(); i+=2)
		{
			String s = l.get(i);
			if(i+1 < l.size())
			{
				s += "  ";
				s += l.get(i+1);
			}
			
			list.add(s);
		}
		
		return list;
	}
	
	private static void addBlocktoMap(Multiblock block, int value, HashMap<String,Integer> map)
	{	
		ItemStack is;
		if(block.isMetadata)
			is = new ItemStack(block.blockID, 1, block.data);
		else
			is = new ItemStack(carbonization.itemStructureBlock, 1, block.data);
		
		String s = "";
		try
		{
			s = is.getDisplayName();
		}
		catch (NullPointerException e)
		{
			return;
		}
		
		if(s==null || s=="")
			return;
		
		if(map.containsKey(is.getDisplayName()))
		{
			int ov = map.get(s);
			map.put(is.getDisplayName(), value+ov);
		}
		else
			map.put(is.getDisplayName(), value);
	}
	
	private static int getBlockHarvestLevel(int blockID, int metadata, World world, int x, int y, int z)
	{
		Block block = Block.blocksList[blockID]; 
		if(block==null)
			return 0;
		if(block.getBlockHardness(world, x, y, z) == -1.0f)
			return -6;
		
		int level = MinecraftForge.getBlockHarvestLevel(block, metadata, "pickaxe");
		if(level == -1)//no mapping for picks
		{
			level = MinecraftForge.getBlockHarvestLevel(block, metadata, "shovel");
			if(level == -1)
				level = MinecraftForge.getBlockHarvestLevel(block, metadata, "axe");
		}
		
		if(level == 0)
		{
			if(block.blockMaterial.isToolNotRequired())
				return -5;
			else
				return 0;
		}
		
		if(level == -1)//some exceptions for stuff we know about, like grass
		{
			if(block.isBlockReplaceable(world, x, y, z))//exception for stuff like liquids and plants
				return -5;
			if(block.isLeaves(world, x, y, z))
				return 2;
			return -100;//suppress the -1 into -100 so anything else shows sooner
		}
		
		return level;
	}
	
	/**
	 * Converts a given tool level (offset to allow for the no tool index)
	 */
	private static String convertToolLevel(int level)
	{
		String s = "";
		
		switch(level)
		{
		case -100:
			s = "Unknown Tool Required";
			break;
		case -5:
			s = "No Tool Required";
			break;
		case 0:
			s = "Wood";
			break;
		case 1:
			s = "Stone";
			break;
		case 2:
			s = "Iron";
			break;
		case 3:
			s = "Diamond";
			break;
		case -6:
			s = "Indestructable";
			break;
		default:
			s = "Greater Than Diamond Required";
		}
		return s;
	}
}
