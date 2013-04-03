package mal.carbonization.multiblock;

import net.minecraft.block.Block;;
//Stores a block and it's location
public class Multiblock {

	public Block block;
	public int x, y, z;
	
	public Multiblock(Block block, int x, int y, int z)
	{
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
