package mal.carbonization.blocks;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.FurnaceTypes;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelCellFiller;
import mal.carbonization.tileentity.TileEntityFuelConverter;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAutocraftingBench extends BlockContainer {

	private Icon[] iconArray = new Icon[9];
	
	public BlockAutocraftingBench(int par1, Material par2Material) {
        super(par1, Material.ground);
        this.setUnlocalizedName("autocraftingbench");
        this.setHardness(1.5f);
        this.setResistance(20f);
        this.setCreativeTab(carbonization.tabMachine);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	iconArray[0] = ir.registerIcon("carbonization:autocraftingBenchBottomTexture");
    	iconArray[1] = ir.registerIcon("carbonization:autocraftingBenchSideTexture");
    	iconArray[2] = ir.registerIcon("carbonization:autocraftingBenchTopTexture");
    	iconArray[3] = ir.registerIcon("carbonization:fuelMachineBottomTexture");
    	iconArray[4] = ir.registerIcon("carbonization:fuelMachineSideTexture");
    	iconArray[5] = ir.registerIcon("carbonization:fuelMachineTopTexture");
    	iconArray[6] = ir.registerIcon("carbonization:fuelMachineBottomTexture");
    	iconArray[7] = ir.registerIcon("carbonization:cellFillerSideTexture");
    	iconArray[8] = ir.registerIcon("carbonization:cellFillerTopTexture");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {	    	
    	
    	//Next, we need the metadata
    	int metadata = par1IBlockAccess.getBlockMetadata(x, y, z);
    	
    	switch(side)
    	{
    	case 0:
    		return iconArray[metadata*3+0];
    	case 1:
    		return iconArray[metadata*3+2];
    	default:
    		return iconArray[metadata*3+1];
    	}
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
	    	par3List.add(new ItemStack(par1, 1, 0));
	    	par3List.add(new ItemStack(par1, 1, 1));
	    	par3List.add(new ItemStack(par1, 1, 2));
    }
    
    /**
     * Returns the block texture based on the side being looked at.  Args: side, metadata
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {    	
    	switch(side)
    	{
    	case 0:
    		return iconArray[metadata*3+0];
    	case 1:
    		return iconArray[metadata*3+2];
    	default:
    		return iconArray[metadata*3+1];
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
        	TileEntity var10 = world.getBlockTileEntity(x,y,z);
        	if((!(var10 instanceof TileEntityAutocraftingBench) && !(var10 instanceof TileEntityFuelConverter) && !(var10 instanceof TileEntityFuelCellFiller)) || var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }

        	if(var10 instanceof TileEntityAutocraftingBench)
        		((TileEntityAutocraftingBench) var10).activate(world, x, y, z, par5EntityPlayer);
        	if(var10 instanceof TileEntityFuelConverter)
        		((TileEntityFuelConverter) var10).activate(world, x, y, z, par5EntityPlayer);
        	if(var10 instanceof TileEntityFuelCellFiller)
        		((TileEntityFuelCellFiller)var10).activate(world,x,y,z,par5EntityPlayer);
            
            return true;
        }
    }
    
	/**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
		//System.out.println("Someone is asking for my damage dropped! " + par1);
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
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	//System.out.println("We broke!");
    	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityAutocraftingBench)
    		((TileEntityAutocraftingBench)te).dumpInventory();
    	if(te instanceof TileEntityFuelConverter)
    		((TileEntityFuelConverter)te).dumpInventory();
    	if(te instanceof TileEntityFuelCellFiller)
    		((TileEntityFuelCellFiller)te).dumpInventory();
    	
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
    	switch(metadata)
    	{
    	case 0:
    		return new TileEntityAutocraftingBench();
    	case 1:
    		return new TileEntityFuelConverter();
    	case 2:
        	return new TileEntityFuelCellFiller();
    	default:
    		//System.out.println("metadata is " + metadata);
    		return null;
    	}
    }
	@Override
	public TileEntity createNewTileEntity(World world) {
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