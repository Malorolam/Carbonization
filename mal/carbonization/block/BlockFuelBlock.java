package mal.carbonization.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import mal.carbonization.carbonizationItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFuelBlock extends Block{

	private IIcon[] iconArray = new IIcon[3];
	
	public BlockFuelBlock(Material par3Material) {
		super(par3Material);
		this.setBlockName("BlockFuelBlock");
		this.setCreativeTab(carbonization.tabItems);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:peatBlockTexture");
		iconArray[1] = ir.registerIcon("carbonization:anthraciteBlockTexture");
		iconArray[2] = ir.registerIcon("carbonization:graphiteBlockTexture");
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
			return 3.0f;
		case 2:
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	par3List.add(new ItemStack(par1, 1, 0));
    	par3List.add(new ItemStack(par1, 1, 1));
    	par3List.add(new ItemStack(par1, 1, 2));
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