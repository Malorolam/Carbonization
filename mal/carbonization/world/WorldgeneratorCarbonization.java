package mal.carbonization.world;

import java.util.Random;

import mal.carbonization.carbonizationBlocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldgeneratorCarbonization implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		switch(world.provider.dimensionId)
		{
		case -1:
			break;
		case 1:
			break;
		default:
		{
			BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
			generateDirtSurface(world, random, chunkX*16, 40, 50, chunkZ*16, 0, 40, 5);
			generateSurface(world, random, chunkX*16, 0, 50, chunkZ*16, 1, 16, 4);
			generateSurface(world, random, chunkX*16, 0, 30, chunkZ*16, 2, 24, 3);
		}
		}

	}
	
	public void generateSurface(World world, Random random, int blockX, int yMin, int yDiff, int blockZ, int meta, int genSize, int diff)
	{
		//System.out.println("Generating metadata normally: " + meta);
		for(int i = 0; i<diff;i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = yMin + random.nextInt(yDiff);
			int Zcoord = blockZ + random.nextInt(16);

			boolean suc = (new CarbonizationGenMinable(carbonizationBlocks.fuelBlock, meta, genSize, diff)).generateStone(world, random, Xcoord, Ycoord, Zcoord);
			//System.out.println(suc);
		}

	}
	
	public void generateDirtSurface(World world, Random random, int blockX, int yMin, int yDiff, int blockZ, int meta, int genSize, int diff)
	{
		//System.out.println("Generating metadata specially: " + meta);
		for(int i = 0; i<diff; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = yMin + random.nextInt(yDiff);
			int Zcoord = blockZ + random.nextInt(16);

			(new CarbonizationGenMinable(carbonizationBlocks.fuelBlock, meta, genSize, diff)).generateDirt(world, random, Xcoord, Ycoord, Zcoord);
		}

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
