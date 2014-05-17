package mal.carbonization.items;

import java.util.List;

import mal.core.ColorReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTunnelBore extends ItemBlock{

	public ItemBlockTunnelBore(int par1) {
		super(par1);
		this.setUnlocalizedName("itemBlockTunnelBore");
	}

	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		list.add(setTooltipData("A industrial tunneling device,", ColorReference.LIGHTRED));
		list.add(setTooltipData("turning fuel into a very large", ColorReference.LIGHTRED));
		list.add(setTooltipData("hole in the ground.", ColorReference.LIGHTRED));
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
}
