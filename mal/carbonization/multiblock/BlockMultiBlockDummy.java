package mal.carbonization.multiblock;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mal.carbonization.carbonization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;

public class BlockMultiBlockDummy extends Block{

	private Block masterBlock;
	
	public BlockMultiBlockDummy(int par1, Material par2Material) {
		super(par1, par2Material);
		// TODO Auto-generated constructor stub
	}
	
	public void copyMaster(Block master)
	{
		masterBlock = master;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata)
	{
		return masterBlock.getIcon(side, metadata);
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		try 
		{
			return masterBlock.idDropped(par1, par2Random, par3);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
}
