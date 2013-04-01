package mal.carbonization;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);
		if (tileEntity instanceof TileEntityFurnaces)
			return new ContainerFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
		return null;
	}

	//returns an instance of the Gui
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityFurnaces){
                    return new GuiFurnaces(player.inventory, (TileEntityFurnaces) tileEntity);
            }
            return null;

    }

}
