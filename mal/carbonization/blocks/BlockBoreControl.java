package mal.carbonization.blocks;

import java.util.Random;

import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityMultiblockBoreInit;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBoreControl extends BlockContainer{

	private Icon[] icons = new Icon[2];
	
	public BlockBoreControl(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setResistance(100f);
		this.setHardness(10f);
		this.setCreativeTab(carbonization.tabMachine);
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
		icons[0] = ir.registerIcon("carbonization:multiblockBoreSideTexture");
		icons[1] = ir.registerIcon("carbonization:multiblockBoreTopTexture");
    }
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		if(side == 0 || side == 1)//top or bottom
			return icons[1];
		else
			return icons[0];
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		try 
		{
			return 0;//carbonization.boreControl.blockID;
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	/**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	//System.out.println("I've been activated!");
    	if(world.isRemote)
    		return true;
    	else
    	{
    		if(par5EntityPlayer.isSneaking())
    			return false;
    		
    		TileEntity var10 = world.getBlockTileEntity(x, y, z);
    		if(var10 instanceof TileEntityMultiblockBoreInit)
    			var10 = (TileEntityMultiblockBoreInit)var10;
    		else
    		{
    			world.setBlockTileEntity(x, y, z, var10 = this.createNewTileEntity(world));
    		}

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
    		((TileEntityMultiblockBoreInit)var10).activate(x, y, z, world, par5EntityPlayer);
    		
    		
    		
    		return true;
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMultiblockBoreInit();
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