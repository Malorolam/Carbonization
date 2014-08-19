package mal.carbonization.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityTunnelBore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTunnelBore extends BlockContainer {

	private IIcon[] iconArray = new IIcon[3];
	
	public BlockTunnelBore(Material par2Material) {
		super(par2Material);
		this.setBlockName("blockTunnelBore");
        this.setHardness(1.5f);
        this.setResistance(20f);
        this.setCreativeTab(carbonization.tabMachine);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:tunnelBoreFrontTexture");
		iconArray[1] = ir.registerIcon("carbonization:tunnelBoreBackTexture");
		iconArray[2] = ir.registerIcon("carbonization:tunnelBoreSideTexture");
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {	    	
		int frontside = par1IBlockAccess.getBlockMetadata(x, y, z);
    	
    	switch(frontside)
    	{
    	case 0:
	    	{
				switch(side)
				{
				case 2:
					return iconArray[0];
				case 3:
					return iconArray[1];
				default:
					return iconArray[2];
				}
			}
    	case 1:
	    	{
				switch(side)
				{
				case 4:
					return iconArray[0];
				case 5:
					return iconArray[1];
				default:
					return iconArray[2];
				}
			}
    	case 2:
    		{
    			switch(side)
    			{
    			case 3:
    				return iconArray[0];
    			case 2:
    				return iconArray[1];
    			default:
    				return iconArray[2];
    			}
    		}
    	case 3:
			{
				switch(side)
				{
				case 4:
					return iconArray[1];
				case 5:
					return iconArray[0];
				default:
					return iconArray[2];
				}
			}
		default:
			return iconArray[2];
    	}
    }
	
	/**
     * Returns the block texture based on the side being looked at.  Args: side, metadata
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {    	
    	switch(side)
    	{
    	case 4:
    		return iconArray[0];
    	default:
    		return iconArray[2];
    	}
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
        	if(!(var10 instanceof TileEntityTunnelBore))
            {
            	return false;
            }

        	if(var10 instanceof TileEntityTunnelBore)
        		((TileEntityTunnelBore) var10).activate(world, x, y, z, par5EntityPlayer);
            
            return true;
        }
    }
    
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
    	//System.out.println("We broke!");
    	TileEntity te = par1World.getTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityTunnelBore)
    		((TileEntityTunnelBore)te).dumpInventory();
    	
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		// TODO Auto-generated method stub
		return new TileEntityTunnelBore();
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