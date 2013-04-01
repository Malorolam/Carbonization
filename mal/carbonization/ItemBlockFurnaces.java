package mal.carbonization;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;

public class ItemBlockFurnaces extends ItemMultiTextureTile {
	
	private static String[] string = new String[]{"ironfurnace", "insulatedfurnace", "steelfurnace"};

	public ItemBlockFurnaces(int par1, Block block) {
		super(par1, block, string);
		this.setHasSubtypes(true);
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public String getItemNameIS(ItemStack itemstack) 
	{
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0: 
			name = "ironfurnace";
			break;
		case 4: 
			name = "insulatedfurnace"; 
			break;
		case 8:
			name = "steelfurnace";
			break;
		default: name = "blarg";
		}
		return name;
	}

}
