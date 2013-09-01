package mal.carbonization.blocks;

import java.util.Random;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import mal.carbonization.carbonization;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.carbonization.tileentity.TileEntityMultiblockDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockMultiblockStructure extends BlockContainer {

	private Icon[] iconArray = new Icon[14];
	
	public BlockMultiblockStructure(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setUnlocalizedName("BlockStructure");
		this.setStepSound(Block.soundMetalFootstep);
		this.setHardness(3.0f);
		this.setResistance(25.0f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		iconArray[0] = ir.registerIcon("carbonization:iceStructureTexture");
		iconArray[1] = ir.registerIcon("carbonization:refinedIronStructureTexture");
		iconArray[2] = ir.registerIcon("carbonization:pigIronStructureTexture");
		iconArray[3] = ir.registerIcon("carbonization:mildSteelStructureTexture");
		iconArray[4] = ir.registerIcon("carbonization:steelStructureTexture");
		iconArray[5] = ir.registerIcon("carbonization:carbonStructureTexture");
		iconArray[6] = ir.registerIcon("carbonization:refCarbonStructureTexture");
		iconArray[7] = ir.registerIcon("carbonization:insSteelStructureTexture");
		iconArray[8] = ir.registerIcon("carbonization:insCarbonStructureTexture");
		iconArray[9] = ir.registerIcon("carbonization:hdInsCarbonStructureTexture");
		iconArray[10] = ir.registerIcon("carbonization:insRefCarbonStructureTexture");
		iconArray[11] = ir.registerIcon("carbonization:hdInsSteelStructureTexture");
		iconArray[12] = ir.registerIcon("carbonization:hdInsRefCarbonStructureTexture");
		iconArray[13] = ir.registerIcon("carbonization:endStructureTexture");
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		//System.out.println("Someone is asking for my damage! " + value);
		return iconArray[metadata];
	}
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
	{
		return false;
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 0 || par2 >= this.iconArray.length)
        {
            par2 = 0;
        }

        return this.iconArray[par2];
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
			return carbonization.structureBlock.blockID;
		}
		catch (Exception e)
		{
			return 0;
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
            TileEntityMultiblockDummy var10 = (TileEntityMultiblockDummy)world.getBlockTileEntity(x, y, z);

            if (var10 == null || par5EntityPlayer.isSneaking())
            {
            	return false;
            }
            var10.activate(world, x, y, z, par5EntityPlayer);

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
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createTileEntity(World par1World, int metadata)
    {
    	try
    	{
    		//System.out.println("Made a new tile entity!");
    		return new TileEntityMultiblockDummy();
    	}
    	catch(Exception e)
    	{
    		FMLLog.log(Level.INFO, "Oh dear, something broke with the tile entities, prod Mal so he can fix it.");
    	}
		return null;
    }
    
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	//System.out.println("We broke!");
    	TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
    	if(te instanceof TileEntityMultiblockDummy)
    		((TileEntityMultiblockDummy)te).revert();
    }
    
    @Override
    public boolean hasTileEntity()
    {
    	return true;
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

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileEntityMultiblockDummy();
	}

}
