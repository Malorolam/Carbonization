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
		int maxToolRequired = 0;
		int totalToolRequired = 0;
		
		for(int i = 0; i < pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					if(pattern[i][j][k]!=null && !world.isAirBlock(startX+i, startY+j, startZ+k))
					{
						totalBlocks++;
						int toolR = getBlockHarvestLevel(pattern[i][j][k].blockID, pattern[i][j][k].data);
						totalToolRequired += toolR;
						if(toolR > maxToolRequired)
							maxToolRequired = toolR;
					}
				}
		double percent = ((double)totalBlocks)/((double)totalVolume)*100;
		double avgTool = ((double)totalToolRequired)/((double)totalBlocks);
		List<String> list = new ArrayList<String>();
		list.add("Volume: " + totalBlocks + "/" + totalVolume + " (" + String.format("%.2f", percent) + "% solid)");
		list.add("Max tool material required: " + convertToolLevel(maxToolRequired) + ((totalBlocks>1)?("; Average tool material required: " + convertToolLevel((int)Math.ceil(avgTool))):("")));
		
		return list;
	}
	
	/**
	 * Shows a list of every kind of block and it's number of instances in addition to the other information
	 */
	private static List<String> expandedMode(Multiblock[][][] pattern, World world, int startX, int startY, int startZ)
	{
		int totalBlocks = 0;
		int totalVolume = pattern.length*pattern[0].length*pattern[0][0].length;
		int maxToolRequired = 0;
		int totalToolRequired = 0;
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		for(int i = 0; i < pattern.length; i++)
			for(int j = 0; j<pattern[0].length; j++)
				for(int k = 0; k<pattern[0][0].length; k++)
				{
					if(pattern[i][j][k]!=null && !world.isAirBlock(startX+i, startY+j, startZ+k))
					{
						totalBlocks++;
						int toolR = getBlockHarvestLevel(pattern[i][j][k].blockID, pattern[i][j][k].data);
						totalToolRequired += toolR;
						if(toolR > maxToolRequired)
							maxToolRequired = toolR;
						addBlocktoMap(pattern[i][j][k], 1, map);
					}
				}
		double percent = ((double)totalBlocks)/((double)totalVolume)*100;
		double avgTool = ((double)totalToolRequired)/((double)totalBlocks);
		List<String> list = new ArrayList<String>();
		list.add("Volume: " + totalBlocks + "/" + totalVolume + " (" + String.format("%.2f", percent) + "% solid)");
		list.add("Max tool material required: " + convertToolLevel(maxToolRequired) + ((totalBlocks>1)?("; Average tool material required: " + convertToolLevel((int)Math.ceil(avgTool))):("")));
		
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
		
		if(map.containsKey(is.getDisplayName()))
		{
			int ov = map.get(is.getDisplayName());
			map.put(is.getDisplayName(), value+ov);
		}
		else
			map.put(is.getDisplayName(), value);
	}
	
	/**
	 * A dirty way to get the harvest level of a block
	 * offset by 1 so that blocks that need no tool have a different index from blocks that need any tool of a type
	 */
	private static int getBlockHarvestLevel(int blockID, int metadata)
	{
		Block block = Block.blocksList[blockID]; 
		if(block==null)
			return 0;
		if (block.blockMaterial.isToolNotRequired())
            return 0;
		
		//try different tools until we get a successful value
		//pickaxes
		ItemStack is = new ItemStack(Item.pickaxeWood);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 1;
		is = new ItemStack(Item.pickaxeStone);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 2;
		is = new ItemStack(Item.pickaxeIron);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 3;
		is = new ItemStack(Item.pickaxeDiamond);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 4;
		//TODO: Eventually put TC tool support in
		
		//i can has shuvl
		is = new ItemStack(Item.shovelWood);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 1;
		is = new ItemStack(Item.shovelStone);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 2;
		is = new ItemStack(Item.shovelIron);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 3;
		is = new ItemStack(Item.shovelDiamond);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 4;
		
		//gotta axe you a question
		is = new ItemStack(Item.axeWood);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 1;
		is = new ItemStack(Item.axeStone);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 2;
		is = new ItemStack(Item.axeIron);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 3;
		is = new ItemStack(Item.axeDiamond);
		if(ForgeHooks.canToolHarvestBlock(block, metadata, is))
			return 4;
		
		//we found something no tool we know about will mine, soooooo yeaaaahhhhhhhh just don't consider it for now
		return 0;
	}
	
	/**
	 * Converts a given tool level (offset to allow for the no tool index)
	 */
	private static String convertToolLevel(int level)
	{
		String s = "";
		
		switch(level)
		{
		case 0:
			s = "No Tool Required";
			break;
		case 1:
			s = "Wood";
			break;
		case 2:
			s = "Stone";
			break;
		case 3:
			s = "Iron";
			break;
		case 4:
			s = "Diamond";
			break;
		default:
			s = "dunno";
		}
		return s;
	}
}
