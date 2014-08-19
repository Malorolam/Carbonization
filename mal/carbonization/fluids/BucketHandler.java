package mal.carbonization.fluids;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketHandler {

	public static BucketHandler instance = new BucketHandler();
	public HashMap<Block, Item> bucketMap = new HashMap<Block, Item>();
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		ItemStack output = fillCustomBucket(event.world, event.target);
		
		if(output == null)
			return;
		
		event.result = output;
		event.setResult(Result.ALLOW);
	}
	
	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		Block b = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		
		Item bucket = bucketMap.get(b);
		if(bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
			return new ItemStack(bucket);
		}
		return null;
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