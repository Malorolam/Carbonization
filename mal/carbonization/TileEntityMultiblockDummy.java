package mal.carbonization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Mal
 * This is the tile entity for any dummy blocks for a multiblock, all it does is when activated 
 * refers to the activation method of the reference tile entity, where everything is maintained
 */
public class TileEntityMultiblockDummy extends TileEntity implements ITileEntityMultiblock{

	public ITileEntityMultiblock masterEntity;
	
	public TileEntityMultiblockDummy(ITileEntityMultiblock master)
	{
		this.masterEntity = master;
	}
	
	public void activate(World world, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		masterEntity.activate(world, z, z, z, par5EntityPlayer);
	}

	/*
	 * Clean up ourselves if there is still a block here
	 */
	@Override
	public void revert() {
		if(worldObj.blockExists(xCoord, yCoord, zCoord))
			if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord).equals(this))//make sure it's us
				worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, null);
	}

	@Override
	public void initilize(Object[] params) {
		// TODO Auto-generated method stub
		
	}
}
