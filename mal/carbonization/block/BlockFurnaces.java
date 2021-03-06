package mal.carbonization.block;

import java.util.List;
import java.util.Random;

import javax.swing.Icon;

import org.apache.logging.log4j.Level;

import mal.carbonization.FurnaceTypes;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationBlocks;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.core.util.MalLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFurnaces extends BlockContainer
{
    /**
     * Is the random generator used by furnace to drop the inventory contents in random directions.
     */
    private Random furnaceRand = new Random();

    /**
     * This flag is used to prevent the furnace inventory to be dropped upon block removal, is used internally when the
     * furnace block changes from idle to active and vice-versa.
     */
    private static boolean keepFurnaceInventory = false;
    
    /**
     * The icon references for the block are kept in here
     */
    @SideOnly(Side.CLIENT)
    private Icon[] iconList;

    public BlockFurnaces()
    {
        super(Material.iron);
        this.setBlockName("IronFurnace");
        this.setHardness(1.5f);
        this.setResistance(20f);
        this.setCreativeTab(carbonization.tabMachine);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
    	for(FurnaceTypes type : FurnaceTypes.values())
    	{
    		type.makeIcons(ir);
    	}
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    	super.onBlockAdded(par1World, par2, par3, par4);
    	par1World.markBlockForUpdate(par2, par3, par4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
    	//First get the tile entity for the block, since we need that to find the facing now
    	TileEntityFurnaces te = (TileEntityFurnaces)par1IBlockAccess.getTileEntity(x, y, z);
    	if(te==null)
    		MalLogger.addLogMessage(Level.WARN, "We have no tile entity at: " + x + " " + y + " " + z);
    	
    	byte facing = te.getFacing();
    	
    	//Next, we need the metadata
    	int metadata = te.metadata;
    	boolean isActive = te.isActive;
    	
    	//Now we go through the directions and use the correct image for each side
    	//This is the front face
    	//yeah magic numbers are bad, but I'm lazy
    	if(side==facing)
    		return FurnaceTypes.values()[metadata*2+((isActive)?1:0)].getIcon(3);
    	else if (side==0)
    		return FurnaceTypes.values()[metadata*2+((isActive)?1:0)].getIcon(0);
    	else if (side == 1)
    		return FurnaceTypes.values()[metadata*2+((isActive)?1:0)].getIcon(1);
    	else
    		return FurnaceTypes.values()[metadata*2+((isActive)?1:0)].getIcon(2);
    }
    
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
    public int damageDropped(int par1)
    {
    	return par1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
	    	par3List.add(new ItemStack(par1, 1, 0));
	    	par3List.add(new ItemStack(par1, 1, 1));
	    	par3List.add(new ItemStack(par1, 1, 2));
    }


    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
    {
    	boolean isActive=false;
    	int metadata = par1World.getBlockMetadata(x, y, z);
    	byte value = 2;
    	try {
    		value = ((TileEntityFurnaces)par1World.getTileEntity(x, y, z)).getFacing();
    		isActive = ((TileEntityFurnaces)par1World.getTileEntity(x, y, z)).isActive;
    	}
    	catch (Exception e)
    	{
    		FMLLog.log(Level.WARN, "Oh noes, no tile entity for our toes!");
    	}

    	if(isActive)
    	{
    		float var7 = (float)x + 0.5F;
    		float var8 = (float)y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
    		float var9 = (float)z + 0.5F;
    		float var10 = 0.52F;
    		float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

    		if (value == 4)
    		{
    			par1World.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
    			par1World.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
    		}
    		else if (value == 5)
    		{
    			par1World.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
    			par1World.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
    		}
    		else if (value == 2)
    		{
    			par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
    			par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
    		}
    		else if (value == 3)
    		{
    			par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
    			par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
    		}
    	}
    }
    
    /**
     * Returns the block texture based on the side being looked at.  Args: side, metadata
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {    	
    	//get the correct metadata for the furnace type
    	metadata = metadata*2;
    	
    	if(metadata < FurnaceTypes.values().length)
    	{
    		FurnaceTypes type = FurnaceTypes.values()[metadata];
    		return type.getIcon(side);
    	}
    	return null;
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
            TileEntity var10 = world.getTileEntity(x, y, z);
            if(!(var10 instanceof TileEntityFurnaces))
            	return false;

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
            ((TileEntityFurnaces)var10).activate(world, x, y, z, par5EntityPlayer);

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
    		return new TileEntityFurnaces(metadata);
    	}
    	catch(Exception e)
    	{
    		FMLLog.log(Level.INFO, "Oh dear, something broke with the tile entities, prod Mal so he can fix it.");
    	}
		return null;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack itemstack)
    {
    	byte Furnacefacing = 0;
    	byte metadata = (byte) itemstack.getItemDamage();
        int facing = MathHelper.floor_double((double) ((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0)
        {
            Furnacefacing = 2;
        }
        if (facing == 1)
        {
            Furnacefacing = 5;
        }
        if (facing == 2)
        {
            Furnacefacing = 3;
        }
        if (facing == 3)
        {
            Furnacefacing = 4;
        }
        TileEntity te = par1World.getTileEntity(par2, par3, par4);
        //System.out.println("We got to the facing bit and it's " + Furnacefacing);
        if (te != null && te instanceof TileEntityFurnaces)
        {
        	//System.out.println("The tile entity exists correctly and the data is sent");
            ((TileEntityFurnaces) te).setFacingandMetadata(Furnacefacing, metadata);
            par1World.markBlockForUpdate(par2, par3, par4);
        }
/*        else
        {
        	System.out.println("Creating a tile entity!");
        	par1World.setTileEntity(par2, par3, par4, this.createNewTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4)));
        	te = par1World.getTileEntity(par2, par3, par4);
        	((TileEntityFurnaces) te).setFacingandMetadata(Furnacefacing, metadata);
        	par1World.markBlockForUpdate(par2, par3, par4);
        }*/
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        if (!keepFurnaceInventory)
        {
        	TileEntity te = par1World.getTileEntity(par2, par3, par4);
        	TileEntityFurnaces var7 = null;
        	if(te instanceof TileEntityFurnaces)
        		var7 = (TileEntityFurnaces)te;
        		

            if (var7 != null)
            {
                for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
                {
                    ItemStack var9 = var7.getStackInSlot(var8);

                    if (var9 != null)
                    {
                        float var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                        float var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                        float var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                        while (var9.stackSize > 0)
                        {
                            int var13 = this.furnaceRand.nextInt(21) + 10;

                            if (var13 > var9.stackSize)
                            {
                                var13 = var9.stackSize;
                            }

                            var9.stackSize -= var13;
                            EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.getItem(), var13, var9.getItemDamage()));

                            if (var9.hasTagCompound())
                            {
                                var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                            }

                            float var15 = 0.05F;
                            var14.motionX = (double)((float)this.furnaceRand.nextGaussian() * var15);
                            var14.motionY = (double)((float)this.furnaceRand.nextGaussian() * var15 + 0.2F);
                            var14.motionZ = (double)((float)this.furnaceRand.nextGaussian() * var15);
                            par1World.spawnEntityInWorld(var14);
                            
                        }
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((IInventory)par1World.getTileEntity(par2, par3, par4));
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