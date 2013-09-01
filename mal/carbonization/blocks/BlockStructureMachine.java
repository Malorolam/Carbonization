package mal.carbonization.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import mal.carbonization.carbonization;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLogic;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStructureMachine extends Block{

	private Icon[] iconArrayTop = new Icon[14];
	private Icon[] iconArraySide = new Icon[14];
	
	public BlockStructureMachine(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setStepSound(Block.soundMetalFootstep);
		this.setHardness(3.0f);
		this.setResistance(25.0f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArrayTop[0] = ir.registerIcon("carbonization:iceStructureTexture");
		iconArrayTop[1] = ir.registerIcon("carbonization:refinedIronStructureTexture");
		iconArrayTop[2] = ir.registerIcon("carbonization:pigIronStructureTexture");
		iconArrayTop[3] = ir.registerIcon("carbonization:mildSteelStructureTexture");
		iconArrayTop[4] = ir.registerIcon("carbonization:steelStructureTexture");
		iconArrayTop[5] = ir.registerIcon("carbonization:carbonStructureTexture");
		iconArrayTop[6] = ir.registerIcon("carbonization:refCarbonStructureTexture");
		iconArrayTop[7] = ir.registerIcon("carbonization:insSteelStructureTexture");
		iconArrayTop[8] = ir.registerIcon("carbonization:insCarbonStructureTexture");
		iconArrayTop[9] = ir.registerIcon("carbonization:hdInsCarbonStructureTexture");
		iconArrayTop[10] = ir.registerIcon("carbonization:insRefCarbonStructureTexture");
		iconArrayTop[11] = ir.registerIcon("carbonization:hdInsSteelStructureTexture");
		iconArrayTop[12] = ir.registerIcon("carbonization:hdInsRefCarbonStructureTexture");
		iconArrayTop[13] = ir.registerIcon("carbonization:endStructureTexture");
		
		iconArraySide[0] = ir.registerIcon("carbonization:iceStructureMachineTexture");
		iconArraySide[1] = ir.registerIcon("carbonization:refinedIronStructureMachineTexture");
		iconArraySide[2] = ir.registerIcon("carbonization:pigIronStructureMachineTexture");
		iconArraySide[3] = ir.registerIcon("carbonization:mildSteelStructureMachineTexture");
		iconArraySide[4] = ir.registerIcon("carbonization:steelStructureMachineTexture");
		iconArraySide[5] = ir.registerIcon("carbonization:carbonStructureMachineTexture");
		iconArraySide[6] = ir.registerIcon("carbonization:refCarbonStructureMachineTexture");
		iconArraySide[7] = ir.registerIcon("carbonization:insSteelStructureMachineTexture");
		iconArraySide[8] = ir.registerIcon("carbonization:insCarbonStructureMachineTexture");
		iconArraySide[9] = ir.registerIcon("carbonization:hdInsCarbonStructureMachineTexture");
		iconArraySide[10] = ir.registerIcon("carbonization:insRefCarbonStructureMachineTexture");
		iconArraySide[11] = ir.registerIcon("carbonization:hdInsSteelStructureMachineTexture");
		iconArraySide[12] = ir.registerIcon("carbonization:hdInsRefCarbonStructureMachineTexture");
		iconArraySide[13] = ir.registerIcon("carbonization:endStructureMachineTexture");
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		if(side == 0 || side == 1)
			return iconArrayTop[metadata];
		else
			return iconArraySide[metadata];
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 0 || par2 >= this.iconArrayTop.length)
        {
            par2 = 0;
        }
        
        if(par1==0 || par2==1)
        	return this.iconArrayTop[par2];
        else
        	return iconArraySide[par2];
    }
    
    /*
     * Get the tier of the structure block
     */
    public int getTier(int metadata)
    {
    	switch(metadata)
    	{
    	case 0:
    		return 0;
    	case 1:
    		return 1;
    	case 2:
    		return 2;
    	case 3:
    		return 3;
    	case 4:
    		return 4;
    	case 5:
    		return 4;
    	case 6:
    		return 5;
    	case 7:
    		return 6;
    	case 8:
    		return 7;
    	case 9:
    		return 8;
    	case 10:
    		return 8;
    	case 11:
    		return 9;
    	case 12:
    		return 10;
    	case 13:
    		return 20;
    	default:
    		return 0;
    	}
    }

	public int idDropped(int par1, Random par2Random, int par3)
	{
		try 
		{
			return carbonization.structureMachineBlock.blockID;
		}
		catch (Exception e)
		{
			return 0;
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
     * Location sensitive version of getExplosionRestance
     *
     * @param par1Entity The entity that caused the explosion
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param explosionX Explosion source X Position
     * @param explosionY Explosion source X Position
     * @param explosionZ Explosion source X Position
     * @return The amount of the explosion absorbed.
     */
    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        switch(metadata)
        {
        case 0://ice
        	return 5f;
        case 1://refined iron
        	return 10f;
        case 2://pig iron
        	return 8f;
        case 3://HC steel
        	return 10f;
        case 4://MC steel
        	return 15f;
        case 5://carbon
        	return 5f;
        case 6://reinforced carbon
        	return 15f;
        case 7://insulated steel
        	return 15f;
        case 8://insulated carbon
        	return 5f;
        case 9://high density insulated carbon
        	return 8f;
        case 10://insulated reinforced carbon
        	return 18f;
        case 11://high density insulated steel
        	return 18f;
        case 12://high density insulated reinforced carbon
        	return 20f;
        case 13://end
        	return 40f;
        default:
        	return 1f;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	par3List.add(new ItemStack(par1, 1, 0));
    	par3List.add(new ItemStack(par1, 1, 1));
    	par3List.add(new ItemStack(par1, 1, 2));
    	par3List.add(new ItemStack(par1, 1, 3));
    	par3List.add(new ItemStack(par1, 1, 4));
    	par3List.add(new ItemStack(par1, 1, 5));
    	par3List.add(new ItemStack(par1, 1, 6));
    	par3List.add(new ItemStack(par1, 1, 7));
    	par3List.add(new ItemStack(par1, 1, 8));
    	par3List.add(new ItemStack(par1, 1, 9));
    	par3List.add(new ItemStack(par1, 1, 10));
    	par3List.add(new ItemStack(par1, 1, 11));
    	par3List.add(new ItemStack(par1, 1, 12));
    	par3List.add(new ItemStack(par1, 1, 13));
    }
}
