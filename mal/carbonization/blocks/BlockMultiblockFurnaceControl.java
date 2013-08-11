package mal.carbonization.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.carbonization.multiblock.MultiBlockInstantiator;
import mal.carbonization.multiblock.MultiBlockMatcher;
import mal.carbonization.tileentity.TileEntityMultiblockDummy;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/*
 * A test block for debugging internal mechanisms
 * Currently set up for multiblock patterns
 */
public class BlockMultiblockFurnaceControl extends BlockContainer {

	private Icon[] icons = new Icon[2];
	
	public BlockMultiblockFurnaceControl(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setUnlocalizedName("carbonization:multiblockfurnacecontrol");
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
		icons[0] = ir.registerIcon("carbonization:multiblockFurnaceControlSideTexture");
		icons[1] = ir.registerIcon("carbonization:multiblockFurnaceControlTopTexture");
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
			return carbonization.multiblockFurnaceControl.blockID;
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	//System.out.println("We broke!");
    	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityMultiblockFurnace)
    		((TileEntityMultiblockFurnace)te).selfRevert();
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
    		if(!(world.getBlockTileEntity(x, y, z) instanceof TileEntityMultiblockFurnace))
    			return false;

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
    		((TileEntityMultiblockFurnace) var10).activate(world, x, y, z, par5EntityPlayer);
    		
    		
    		
    		return true;
    	}
    }

/*	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityMultiblockFurnace();
	}*/
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMultiblockFurnace();
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