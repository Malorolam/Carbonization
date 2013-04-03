package mal.carbonization.multiblock;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
//The base multiblock functionality
//All other multiblocks should be children of this
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMulti extends TileEntity {

	private LinkedList<Multiblock> blocks = new LinkedList<Multiblock>();
	private int masterBlockID;
	
	public TileEntityMulti()
	{
		
	}
	
	public boolean generateMultiBlock(World world, Block blocktype)
	{
		//Dunno how the world is kept properly, using a param for now
		//We use a specific blocktype as the parent for our body
		
		//First figure out our volume through connected blocks around an empty space
		//We know the shape of the multiblock, for this general case nxnx3, where n%2=1 hollow cube of blocks
		//this case will not consider the kind of specific block, just that the blocks follow the correct geometry
		//So we need to know where we are as a entity
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		
		//First, make sure that we are located in the centre of a wall, meaning the blocks +-1 on the x or z and the y are blocks and the other
		//axis is air
		if(world.getBlockId(x, y, z)==masterBlockID)//make sure our reference is the correct initiator block
		{
			//One of these statements must be true or the function returns false
			//First is we are not a wall or floor, so the blocks above and below are not air
			if(world.getBlockId(x, y+1, z)==0 || world.getBlockId(x, y-1, z)==0)
				return false;
			//that statement will get rid of any top or bottom corner/edge cases nicely
			//make sure that we aren't a vertical corner either
			if(world.getBlockId(x+1, y, z)==0 || world.getBlockId(x-1, y, z)==0 || world.getBlockId(x, y, z+1)==0 || world.getBlockId(x, y, z-1)==0)
				return false;
			
			//Going back to our knowledge of the geometry we can find size of the multiblock by examining block ids as we step through certain directions
			//we can have an exclusion for y first, since the max height is 3
			//we know the source block is in the centre of the wall vertically, so make sure this face is valid
			//We use the top side, knowing that it has to be the block type in the direction of the structure
		}
		return false;
	}
}
