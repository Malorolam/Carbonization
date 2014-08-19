package mal.carbonization.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
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

public class BlockFurnaceControl extends BlockContainer {

	private IIcon[] icons = new IIcon[2];
	
	public BlockFurnaceControl(Material par2Material) {
		super(par2Material);
		this.setResistance(100f);
		this.setHardness(10f);
		this.setCreativeTab(carbonization.tabMachine);
		this.setBlockName("furnaceinit");
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
    		
    		TileEntity var10 = world.getTileEntity(x, y, z);
    		if(var10 instanceof TileEntityMultiblockInit)
    			var10 = (TileEntityMultiblockInit)var10;
    		else
    		{
    			world.setTileEntity(x, y, z, var10 = this.createNewTileEntity(world, 0));
    		}

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
    		((TileEntityMultiblockInit)var10).activate(x, y, z, world, par5EntityPlayer);
    		
    		
    		
    		return true;
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMultiblockInit("furnace");
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