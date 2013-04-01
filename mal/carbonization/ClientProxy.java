package mal.carbonization;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings()
	{

	}
	
	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityFurnaces)
		{
			return new GuiFurnaces(player.inventory, (TileEntityFurnaces)te);
		}
		else
			return null;
	}
}
