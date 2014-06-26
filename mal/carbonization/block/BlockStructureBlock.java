package mal.carbonization.block;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import mal.carbonization.carbonization;
import mal.carbonization.carbonizationItems;
import mal.carbonization.render.StructureBlockRenderer;
import mal.carbonization.tileentity.TileEntityStructureBlock;
import mal.core.util.MalLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockStructureBlock extends BlockContainer{

	public IIcon[] baseIcon = new IIcon[10];
	public IIcon[] secondaryIcon = new IIcon[3];
	public IIcon[] purposeIcon = new IIcon[3];
	
	public BlockStructureBlock(Material par2Material) {
		super(par2Material);
		this.setBlockName("BlockStructure");
		this.setStepSound(Block.soundTypeMetal);
		this.setHardness(3.0f);
		this.setResistance(25.0f);
		//this.setCreativeTab(carbonization.tabStructure);
		this.setLightOpacity(15);
	}

	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		for(int i = 0; i<10; i++)
		{
			String baseMatName = "carbonization:";
		
			switch(i)
			{
			case 0:
				baseMatName += "stone";
				break;
			case 1:
				baseMatName += "ice";
				break;
			case 2:
				baseMatName += "iron";
				break;
			case 3:
				baseMatName += "brick";
				break;
			case 4:
				baseMatName += "steel";
				break;
			case 5:
				baseMatName += "carbonFibre";
				break;
			case 6:
				baseMatName += "titanium";
				break;
			case 7:
				baseMatName += "carbonNanotube";
				break;
			case 8:
				baseMatName += "witheredEnd";
				break;
			case 9:
				baseMatName += "cobaltChrome";
				break;
			}
			baseMatName += "StructureTexture";
			
			baseIcon[i] = ir.registerIcon(baseMatName);
		}
		
		for(int i = 1; i < 3; i++)
		{
			String secondaryMatName = null;
			
			switch(i)
			{
			case 1:
				secondaryMatName = "carbonization:highDensityInsulationLayerTexture";
				break;
			case 2:
				secondaryMatName = "carbonization:fineThreadingLayerTexture";
				break;
			}
			
			if(secondaryMatName != null)
				secondaryIcon[i] = ir.registerIcon(secondaryMatName);
		}
		
		for(int i = 1; i<3; i++)
		{
			String purposeName = null;
			switch(i)
			{
			case 1:
				purposeName = "carbonization:furnaceLayerTexture";
				break;
			case 2:
				purposeName = "carbonization:machineLayerTexture";
				break;
			}
	
			if(purposeName != null)
				purposeIcon[i] = ir.registerIcon(purposeName);
		}
		
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }
    
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
    	return false;
    }

    //And this tell it that you can see through this block, and neighbor blocks should be rendered.
    public boolean isOpaqueCube()
    {
       return true;
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        
        if(te instanceof TileEntityStructureBlock)
        {
        	if(((TileEntityStructureBlock)te).baseMaterial == 18)
        		return 10;
        	if(((TileEntityStructureBlock)te).purpose == 1)
        		return 2;
        }
        return super.getLightValue(world, x, y, z);
    }
    
	/*
	 * Get the texture from the side, and pass for a certain block
	 * data: the index of the correct layer, so base, secondary, or purpose
	 */
	public IIcon getTextureFromDataandPass(int data, int pass)
	{
		//System.out.println("Getting Icon with data: " + data + " on pass: " + pass);
		if(data < 0)
			return null;
		
		if(pass == 0)
			if(data < 10)
				return baseIcon[data];
			else
				return null;
		else if(pass == 1)
			if(data < 3)
				return secondaryIcon[data];
			else
				return null;
		else if(pass == 2)
			if(data < 3)
				return purposeIcon[data];
			else
				return null;
		else
			return null;
		
	}
	
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block block, int meta)
    {
    	//System.out.println("We broke!");
    	TileEntity te = par1World.getTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityStructureBlock)
    		((TileEntityStructureBlock)te).revert();
    	
    	par1World.setTileEntity(par2, par3, par4, null);
    }
    
    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof TileEntityStructureBlock)
        {
        	int data = ((TileEntityStructureBlock)te).getData();
            ItemStack is = new ItemStack(carbonizationItems.structureItem, 1, data);
            ret.add(is);
        }
        return ret;
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
    		if(!(world.getTileEntity(x, y, z) instanceof TileEntityStructureBlock))
    			return false;

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
            if(!((TileEntityStructureBlock) var10).isMultiblock())
            	return false;
            
    		((TileEntityStructureBlock) var10).activate(world, x, y, z, par5EntityPlayer);
    		
    		
    		
    		return true;
    	}
    }
    
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createTileEntity(World par1World, int metadata)
    {
    	try
    	{
    		//System.out.println("Made a new tile entity!");
    		return new TileEntityStructureBlock();
    	}
    	catch(Exception e)
    	{
    		MalLogger.addLogMessage(Level.INFO, "Oh dear, something broke with the tile entities, prod Mal so he can fix it.");
    	}
		return null;
    }

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
    
	@Override
	public int getRenderType()
	{
		return StructureBlockRenderer.structureBlockRenderType;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * Called when a user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
    	return null;
    }

/*	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}*/
}

/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/