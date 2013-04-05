package mal.carbonization;

import java.util.Random;

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
			//make gen for each fuel
			//peat is > 50
			//lignite is 30-50
			//sub-bituminous is 25-40
			//bituminous is 20-30
			//anthracite is 10-20
			//graphite is 0-15
			//limit peat to "wet" biomes
			BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
			if (b.biomeName=="Ocean"||b.biomeName=="Swampland"||b.biomeName=="FrozenOcean"||b.biomeName=="FrozenRiver"||b.biomeName=="Jungle")
				generateDirtSurface(world, random, chunkX*16, 50, 30, chunkZ*16, 0, 30, 5);
			generateDirtSurface(world, random, chunkX*16, 30, 20, chunkZ*16, 1, 20, 4);
			generateSurface(world, random, chunkX*16, 25, 15, chunkZ*16, 2, 15, 4);
			generateSurface(world, random, chunkX*16, 20, 10, chunkZ*16, 3, 10, 3);
			generateSurface(world, random, chunkX*16, 10, 10, chunkZ*16, 4, 8, 4);
			generateSurface(world, random, chunkX*16, 0, 15, chunkZ*16, 5, 10, 3);
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

			boolean suc = (new CarbonizationGenMinable(carbonization.instance.fuelBlock.blockID, meta, genSize, diff)).generateStone(world, random, Xcoord, Ycoord, Zcoord);
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

			(new CarbonizationGenMinable(carbonization.instance.fuelBlock.blockID, meta, genSize, diff)).generateDirt(world, random, Xcoord, Ycoord, Zcoord);
		}

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
