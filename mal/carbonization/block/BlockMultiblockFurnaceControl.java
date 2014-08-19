package mal.carbonization.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import mal.core.multiblock.MultiBlockInstantiator;
import mal.core.multiblock.MultiBlockMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/*
 * A test block for debugging internal mechanisms
 * Currently set up for multiblock patterns
 */
public class BlockMultiblockFurnaceControl extends BlockContainer {

	private IIcon[] icons = new IIcon[2];
	
	public BlockMultiblockFurnaceControl(Material par2Material) {
		super(par2Material);
		this.setResistance(100f);
		this.setHardness(10f);
		this.setBlockName("multiblockfurnacecontrol");
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
		icons[0] = ir.registerIcon("carbonization:multiblockFurnaceControlSideTexture");
		icons[1] = ir.registerIcon("carbonization:multiblockFurnaceControlTopTexture");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(side == 0 || side == 1)//top or bottom
			return icons[1];
		else
			return icons[0];
	}
	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		try 
		{
			return carbonizationBlocks.furnaceControlBlock.getItemDropped(par1, par2Random, par3);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
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
    	TileEntity te = par1World.getTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityMultiblockFurnace)
    		((TileEntityMultiblockFurnace)te).selfRevert();
    	
    	par1World.setTileEntity(par2, par3, par4, null);
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
    		TileEntity var10 = world.getTileEntity(x, y, z);
    		if(!(world.getTileEntity(x, y, z) instanceof TileEntityMultiblockFurnace))
    			return false;

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
    		((TileEntityMultiblockFurnace) var10).activate(world, x, y, z, par5EntityPlayer);
    		
    		
    		
    		return true;
    	}
    }

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityMultiblockFurnace();
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
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