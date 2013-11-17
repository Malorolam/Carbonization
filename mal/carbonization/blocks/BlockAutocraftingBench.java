package mal.carbonization.blocks;

import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.FurnaceTypes;
import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
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

	private Icon[] iconArray = new Icon[3];
	
	public BlockAutocraftingBench(int par1, Material par2Material) {
        super(par1, Material.ground);
        this.setUnlocalizedName("AutocraftingTable");
        this.setHardness(1.5f);
        this.setResistance(20f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	iconArray[0] = ir.registerIcon("carbonization:autocraftingBenchBottomTexture");
    	iconArray[1] = ir.registerIcon("carbonization:autocraftingBenchSideTexture");
    	iconArray[2] = ir.registerIcon("carbonization:autocraftingBenchTopTexture");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {	    	
    	switch(side)
    	{
    	case 0:
    		return iconArray[0];
    	case 1:
    		return iconArray[2];
    	default:
    		return iconArray[1];
    	}
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
	    	par3List.add(new ItemStack(par1, 1, 0));
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
    		return iconArray[0];
    	case 1:
    		return iconArray[2];
    	default:
    		return iconArray[1];
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
            TileEntityAutocraftingBench var10 = (TileEntityAutocraftingBench)world.getBlockTileEntity(x, y, z);

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	//System.out.println("oh noes, the bench isn't a bench.");
            	return false;
            }
            var10.activate(world, x, y, z, par5EntityPlayer);

            return true;
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
    	if(te instanceof TileEntityAutocraftingBench)
    		((TileEntityAutocraftingBench)te).dumpInventory();
    }
    
	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileEntityAutocraftingBench();
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