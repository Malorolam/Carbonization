package mal.carbonization.items;

import java.util.List;

import mal.carbonization.ColorReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockStructureMachine extends ItemBlock{
	public ItemBlockStructureMachine(int par1) {
		super(par1);
		this.setUnlocalizedName("ItemBlockStructure");
		this.setHasSubtypes(true);
	}
	
	public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean bool)
	{
		//find the right metadata value
		switch(is.getItemDamage())
		{
		case 0://ice
			list.add(setTooltipData("Made from ice", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Dwarven engineering allows ", ColorReference.ORANGE));
			list.add(setTooltipData("for unmelting ice, even in ", ColorReference.ORANGE));
			list.add(setTooltipData("the hottest of temperatures.", ColorReference.ORANGE));
			list.add(setTooltipData("Tier 0 Material", ColorReference.DARKCYAN));
			break;
		case 1://refined Iron
			list.add(setTooltipData("Made from refined iron.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 1 Structure", ColorReference.DARKCYAN));
			break;
		case 2://pig iron
			list.add(setTooltipData("Made from pig iron.", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Oink oink.", ColorReference.PINK));
			list.add(setTooltipData("Tier 2 Structure", ColorReference.DARKCYAN));
			break;
		case 3://mild steel
			list.add(setTooltipData("Made from a high-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 3 Structure", ColorReference.DARKCYAN));
			break;
		case 4://steel
			list.add(setTooltipData("Made from a medium-carbon steel", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 4 Material", ColorReference.DARKCYAN));
			break;
		case 5://carbon
			list.add(setTooltipData("Made from carbon fibre", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 4 Material", ColorReference.DARKCYAN));
			break;
		case 6://reinforced carbon
			list.add(setTooltipData("Made from carbon fibre, ", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("reinforced with iron", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 5 Material", ColorReference.DARKCYAN));
			break;
		case 7://insulated steel
			list.add(setTooltipData("Made from steel, ", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with glass and clay insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 6 Material", ColorReference.DARKCYAN));
			break;
		case 8://insulated carbon
			list.add(setTooltipData("Made from carbon fibre, ", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("with glass and clay insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 7 Material", ColorReference.DARKCYAN));
			break;
		case 9://high density insulated carbon
			list.add(setTooltipData("Made from carbon fibre, with", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("high density graphite insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 8 Material", ColorReference.DARKCYAN));
			break;
		case 10://insulated reinforced carbon
			list.add(setTooltipData("Made from steel reinforced", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("carbon fibre, with glass", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("and clay insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 8 Material", ColorReference.DARKCYAN));
			break;
		case 11://high molecular insulated steel
			list.add(setTooltipData("Made from steel, with high", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("density graphite insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 9 Material", ColorReference.DARKCYAN));
			break;
		case 12://high molecular reinforced carbon
			list.add(setTooltipData("Made from steel reinforced,", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("carbon fibre with high density", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("graphite insulation", ColorReference.LIGHTGREEN));
			list.add(setTooltipData("Tier 10 Material", ColorReference.DARKCYAN));
			break;
		case 13://end
			list.add(setTooltipData("Made from the essence of", ColorReference.PURPLE));
			list.add(setTooltipData("withers and dragons.", ColorReference.PURPLE));
			list.add(setTooltipData("Tier 20 Material", ColorReference.DARKCYAN));
			break;
		default:
			list.add(setTooltipData("This isn't even an item!",ColorReference.DARKRED));
			list.add(setTooltipData("Tell Mal about it so he can fix it.", ColorReference.LIGHTRED));
		}
	}
	
	//The tool tip information
	private String setTooltipData(String data, ColorReference cr)
	{
		String colorValue = cr.getCode();
		
		return colorValue+data;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		switch(is.getItemDamage())
		{
		case 0:
			return this.getUnlocalizedName()+"ice";
		case 1:
			return this.getUnlocalizedName()+"refinedIron";
		case 2:
			return this.getUnlocalizedName()+"pigIron";
		case 3:
			return this.getUnlocalizedName()+"mildSteel";
		case 4:
			return this.getUnlocalizedName()+"steel";
		case 5:
			return this.getUnlocalizedName()+"carbon";
		case 6:
			return this.getUnlocalizedName()+"refCarbon";
		case 7:
			return this.getUnlocalizedName()+"insSteel";
		case 8:
			return this.getUnlocalizedName()+"insCarbon";
		case 9:
			return this.getUnlocalizedName()+"hdinsCarbon";
		case 10:
			return this.getUnlocalizedName()+"insrefCarbon";
		case 11:
			return this.getUnlocalizedName()+"hdinsSteel";
		case 12:
			return this.getUnlocalizedName()+"hdinsrefCarbon";
		case 13:
			return this.getUnlocalizedName()+"end";

		default:
			return this.getUnlocalizedName()+"BlockStructure";
		}
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public String getItemNameIS(ItemStack is) 
	{
		switch(is.getItemDamage())
		{
		case 0:
			return "ice";
		case 1:
			return "refinedIron";
		case 2:
			return "pigIron";
		case 3:
			return "mildSteel";
		case 4:
			return "steel";
		case 5:
			return "carbon";
		case 6:
			return "refCarbon";
		case 7:
			return "insSteel";
		case 8:
			return "insCarbon";
		case 9:
			return "hdinsCarbon";
		case 10:
			return "insrefCarbon";
		case 11:
			return "hdinsSteel";
		case 12:
			return "hdinsrefCarbon";
		case 13:
			return "end";

		default:
			return this.getUnlocalizedName()+"BlockStructure";
		}
	}

}
