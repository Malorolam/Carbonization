package mal.carbonization.block;

import java.util.Random;

import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityFluidTransport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTransport extends BlockContainer{

	public BlockTransport(Material p_i45386_1_) {
		super(p_i45386_1_);
        this.setBlockName("transportblock");
        this.setHardness(1.5f);
        this.setResistance(20f);
        this.setCreativeTab(carbonization.tabMachine);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		
		if(meta==0)
			return new TileEntityFluidTransport();
		return null;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		this.blockIcon = ir.registerIcon("carbonization:fluidTransportTexture");
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
        	TileEntity var10 = world.getTileEntity(x,y,z);
        	if((!(var10 instanceof TileEntityFluidTransport)))
            {
            	return false;
            }

        	if(var10 instanceof TileEntityFluidTransport)
        		((TileEntityFluidTransport) var10).activate(world, x, y, z, par5EntityPlayer);
            
            return true;
        }
    }
    
	/**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1;
    }
    
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
    
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
    	//System.out.println("We broke!");
/*    	TileEntity te = par1World.getTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityFuelConversionBench)
    		((TileEntityFuelConversionBench)te).dumpInventory();*/
    	
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
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