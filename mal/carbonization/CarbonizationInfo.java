/*
 * Contains all the information for different carbonization items
 */

package mal.carbonization;

import java.util.ArrayList;
import java.util.List;

public class CarbonizationInfo {

	public static CarbonizationInfo instance;
	
	public static List<String> getInfo(int index)
	{
		switch(index)
		{
		case 1://empty list
			return emptyList();
		case 2://structure block page 1
			return structureBlockInfo();
		case 3://furnace control block page 1
			return furnaceControlInfo1();
		case 4://furnace control block page 2
			return furnaceControlInfo2();
		case 5://furnace control block page 3
			return furnaceControlInfo3();
		case 6://furnace control block page 4
			return furnaceControlInfo4();
		case 7://autocrafting bench page 1
			return autocraftingBenchInfo1();
		case 8://autocrafting bench page 2
			return autocraftingBenchInfo2();
		case 9://furnaces
			return furnaceInfo();
		default:
			return defaultInfo();
		}
	}
	
	private static List<String> emptyList()
	{
		//bypass the nei issue of putting two recipes per page
		List<String> list = new ArrayList<String>();
		
		list.add("");
		
		return list;
	}
	
	private static List<String> structureBlockInfo()
	{
		List<String> list = new ArrayList<String>();
		list.add("Structure Block:");
		list.add("");
		list.add("Structure Blocks are multipurpose blocks,");
		list.add("which make up the bulk of all advanced");
		list.add("recipes in Carbonization.  They come");
		list.add("in three varieties: Standard, Furnace,");
		list.add("and Machine; each with specific uses");
		list.add("in other machines.  Each Structure");
		list.add("Block has two values that indicate");
		list.add("thier relative value, Conduction and");
		list.add("Insulation.  These values relate to");
		list.add("upgrading advanced single block machines");
		list.add("and quality of multiblock structures.");
		list.add("Structure blocks can be upgraded with");
		list.add("either threading to inprove conduction,");
		list.add("or insulation to improve insulation.");
		
		return list;
	}
	
	private static List<String> furnaceControlInfo1()
	{
		List<String> list = new ArrayList<String>();
		list.add("Furnace Control Block:");
		list.add("");
		list.add("The Furnace Control Block is the key block ");
		list.add("to building and using an Industrial ");
		list.add("Furnace.  This block, when placed in any ");
		list.add("side of a tetragonal hollow structure made ");
		list.add("of any furnace structure blocks for the ");
		list.add("base and any standard structure blocks ");
		list.add("for the walls and top will allow you to ");
		list.add("initilize the structure and process a");
		list.add("large number of items in a rather ");
		list.add("indescriminate way.  The lengths of each");
		list.add("side for this hollow structure can be of ");
		
		return list;
	}
	
	private static List<String> furnaceControlInfo2()
	{
		List<String> list = new ArrayList<String>();
		list.add("Furnace Control Block:");
		list.add("");
		list.add("any value between 3 and 9 for the x and z ");
		list.add("(horizontal) axes and 3 and 6 for the y ");
		list.add("(vertical) axis.  In addition, the precise ");
		list.add("type of structure block used in any ");
		list.add("particular space does not need to be ");
		list.add("homogeneous with any other block provided ");
		list.add("that the base structure of furnace on the ");
		list.add("bottom, standard everywhere else is ");
		list.add("followed.  The furnace has a certain tier ");
		list.add("of quality for both Conduction and ");
		
		return list;
	}
	
	private static List<String> furnaceControlInfo3()
	{
		List<String> list = new ArrayList<String>();
		list.add("Furnace Control Block:");
		list.add("");
		list.add("Insulation, which along with the size of ");
		list.add("the furnace determine certain ");
		list.add("characteristics.  First, the larger the ");
		list.add("furnace the more items can be processed ");
		list.add("at once and the more fuel can be stored.");
		list.add("Second, the total yield of the furnace ");
		list.add("is determined by both tiers.  Third, the ");
		list.add("processing speed is improved by ");
		list.add("Conduction and impaired by Insulation, ");
		list.add("while the reverse is true for the fuel ");
		list.add("efficiency.  Items can be inserted on ");
		
		return list;
	}
	
	private static List<String> furnaceControlInfo4()
	{
		List<String> list = new ArrayList<String>();
		list.add("Furnace Control Block:");
		list.add("");
		list.add("any side except the bottom and extracted ");
		list.add("from any side except the top.  The ");
		list.add("furnace has a single stream input which ");
		list.add("will process items indiscriminatly, so ");
		list.add("make sure that anything you place in the ");
		list.add("furnace is something you wish to subject");
		list.add("to extreme temperatures.");
		
		return list;
	}
	
	private static List<String> autocraftingBenchInfo1()
	{
		List<String> list = new ArrayList<String>();
		list.add("Autocrafting Bench:");
		list.add("");
		list.add("The Autocrafting Bench allows you to ");
		list.add("automatically create a single recipe");
		list.add("without input, just like other ");
		list.add("autocrafting methods from other mods.");
		list.add("There are some differences though.  ");
		list.add("First, the bench has a cooldown after");
		list.add("crafting, instead of taking an amount");
		list.add("of time to craft.  This mainly allows");
		list.add("for more inconsistant crafting jobs ");
		list.add("to happen on demand.  Second, the ");
		list.add("amount of time to cooldown is variable,");
		list.add("though keep in mind that the shorter");
		
		return list;
	}
	
	private static List<String> autocraftingBenchInfo2()
	{
		List<String> list = new ArrayList<String>();
		list.add("Autocrafting Bench:");
		list.add("");
		list.add("the cooldown, the more fuel is needed");
		list.add("to craft.  Third, this fuel cost can");
		list.add("be reduced by placing machine structure");
		list.add("blocks in the upgrade slot, up to 64,");
		list.add("using the average of the two tiers to");
		list.add("determine the effectiveness.  Finally,");
		list.add("the bench can input items from the top");
		list.add("or sides, extract from the bottom and");
		list.add("sides, and insert fuel in the sides.");
		list.add("Keep in mind that fuel will attempt");
		list.add("to be placed in the fuel slot first,");
		list.add("so recipes that use fuel should insert");
		list.add("the fuel from the top.");
		
		return list;
	}
	
	private static List<String> furnaceInfo()
	{
		List<String> list = new ArrayList<String>();
		list.add("Furnace Info:");
		list.add("");
		list.add("The single block furnaces are the first");
		list.add("machines you may make.  There are three,");
		list.add("Iron, Insulated Iron, and Insulated Steel.");
		list.add("Fans of IC2 will recognize the Iron ");
		list.add("furnace, which is mechanically identical");
		list.add("to the IC2 version.  The Insulated");
		list.add("furnaces will maintain thier heat between");
		list.add("jobs, removing wasted fuel from smelting");
		list.add("only a few items.");
		
		return list;
	}
	
	private static List<String> defaultInfo()
	{
		List<String> list = new ArrayList<String>();
		list.add("Carbonization Info:");
		list.add("");
		list.add("Since you are reading this, this means ");
		list.add("that Mal put a default index on something.");
		list.add("Carbonization is a mod that focuses on");
		list.add("carbon and large-scale processing.");
		list.add("There are many features to come, so stay");
		list.add("a while.  The most fundamental aspect of");
		list.add("the mod is the addition of solid fuel,");
		list.add("which is plentiful in the world and can");
		list.add("be converted into other fuel.  In");
		list.add("addition, the mod adds several furnaces,");
		list.add("a multiblock industrial furnace, an");
		list.add("autocrafting bench, and multiple");
		list.add("small tools and items.");
		
		return list;
	}
}
