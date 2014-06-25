package mal.carbonization.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mal.carbonization.carbonization;
import mal.carbonization.carbonizationItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;



public class BlockFuel extends Block {

	private IIcon[] iconArray = new IIcon[12];
	
	public BlockFuel(Material par3Material) {
		super(par3Material);
		this.setBlockName("BlockFuel");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:peatTexture");
		iconArray[1] = ir.registerIcon("carbonization:ligniteTexture");
		iconArray[2] = ir.registerIcon("carbonization:sBitTexture");
		iconArray[3] = ir.registerIcon("carbonization:bitTexture");
		iconArray[4] = ir.registerIcon("carbonization:anthraciteTexture");
		iconArray[5] = ir.registerIcon("carbonization:graphiteTexture");
		iconArray[6] = ir.registerIcon("carbonization:peatBlockTexture");
		iconArray[7] = ir.registerIcon("carbonization:ligniteBlockTexture");
		iconArray[8] = ir.registerIcon("carbonization:sBitBlockTexture");
		iconArray[9] = ir.registerIcon("carbonization:bitBlockTexture");
		iconArray[10] = ir.registerIcon("carbonization:anthraciteBlockTexture");
		iconArray[11] = ir.registerIcon("carbonization:graphiteBlockTexture");
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		switch(meta)
		{
		case 0:
			return 0.5f;
		case 1:
			return 0.5f;
		case 2:
			return 3.0f;
		case 3:
			return 3.0f;
		case 4:
			return 3.0f;
		case 5:
			return 3.0f;
		default:
			return 3.0f;
		}
	}
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		//System.out.println("Someone is asking for my damage! " + value);
		return iconArray[metadata];
	}

	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return carbonizationItems.fuel;
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
     * Drops the block items with a specified chance of dropping the specified items
     */
    //TODO: Verify this works
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);
            int var8 = 0;

            switch(par5)//hope this is metadata
            {
            case 0:
            	var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 0, 1);
            case 1:
            	var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 0, 1);
            case 2:
            	var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 0, 2);
            case 3:
            	var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 1, 2);
            case 4:
            	var8 = MathHelper.getRandomIntegerInRange(par1World.rand, 2, 4);
            case 5:
            	var8 = 0;
            }

            this.dropXpOnBlockBreak(par1World, par2, par3, par4, var8);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	par3List.add(new ItemStack(par1, 1, 0));
    	par3List.add(new ItemStack(par1, 1, 1));
    	par3List.add(new ItemStack(par1, 1, 2));
    	par3List.add(new ItemStack(par1, 1, 3));
    	par3List.add(new ItemStack(par1, 1, 4));
    	par3List.add(new ItemStack(par1, 1, 5));
    	
/*    	par3List.add(new ItemStack(par1, 1, 6));
    	par3List.add(new ItemStack(par1, 1, 7));
    	par3List.add(new ItemStack(par1, 1, 8));
    	par3List.add(new ItemStack(par1, 1, 9));
    	par3List.add(new ItemStack(par1, 1, 10));
    	par3List.add(new ItemStack(par1, 1, 11));*/
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