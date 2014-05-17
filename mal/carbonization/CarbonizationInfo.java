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
		case 10://fuel conversion bench page 1
			return fuelconversion1();
		case 11://fuel conversion bench page 2
			return fuelconversion2();
		case 12://fuel info page 1
			return fuelInfo1();
		case 13://fuel info page 2
			return fuelInfo2();
		case 14://fuel info page 3
			return fuelInfo3();
		case 15://recipe charm page 1
			return recipeCharmInfo();
		case 16://fuel cell page 1
			return fuelCellInfo();
		case 17://fuel cell filler page 1
			return fuelCellBenchInfo();
		case 18://portable scanner page 1
			return portableScannerInfo1();
		case 19://portable scanner page 2
			return portableScannerInfo2();
		case 20://portable scanner page 3
			return portableScannerInfo3();
		case 21://bore page 1
			return boreInfo1();
		case 22://bore page 2
			return boreInfo2();
		case 23://bore page 3
			return boreInfo3();
		case 24://bore page 4
			return boreInfo4();
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
		list.add("side of a orthorhombic hollow structure ");
		list.add("made of any furnace structure blocks for ");
		list.add("the base and any standard structure blocks ");
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
		list.add("be reduced by placing furnace structure");
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
	
	private static List<String> fuelconversion1()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Conversion Bench Info:");
		list.add("");
		list.add("This machine streamlines the process");
		list.add("of converting fuel for a nominal fuel");
		list.add("cost per process.  Like the Autocrafting");
		list.add("Bench, this machine has a cooldown");
		list.add("instead of a process time.  Similarly,");
		list.add("machine and furnace structure blocks");
		list.add("can be inserted in their respective");
		list.add("upgrade slots to improve speed and");
		list.add("fuel efficiency of the machine.  Like");
		list.add("the Industrial Furnace, upgrading one");
		list.add("value has a negative effect on the ");
		list.add("other.  The different fuel choices can");
		
		return list;
	}
	
	private static List<String> fuelconversion2()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Conversion Bench Info:");
		list.add("");
		list.add("be cycled through with the prev/next");
		list.add("buttons in the GUI, and the Fuel/Dust");
		list.add("toggle will swap between outputting");
		list.add("solid fuel or the dust form.  Skipping");
		list.add("the final compression step obviously");
		list.add("reduces the amount of fuel needed by");
		list.add("a small amount.  The machine can ");
		list.add("input from the top, extract from any ");
		list.add("side or the bottom, and insert fuel ");
		list.add("in any side or the bottom.");
		
		return list;
	}
	
	private static List<String> fuelInfo1()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Conversion Info:");
		list.add("");
		list.add("One of the core mechanics in");
		list.add("Carbonization is the ability to");
		list.add("convert between different fuels.");
		list.add("This is accomplished through three");
		list.add("steps: Mash, Remake, and Mush.");
		list.add("You Mash the fuel into dust, which");
		list.add("you then Remake into a different");
		list.add("dust, which is then Mushed into");
		list.add("a solid form that other machines");
		list.add("can accept.  ");
		
		return list;
	}
	
	private static List<String> fuelInfo2()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Conversions:");
		list.add("");
		list.add("The conversions between different ");
		list.add("dusts is as follows:");
		list.add("");
		list.add("1 Charcoal = 1 Coal");
		list.add("1 Peat = 1 Lignite");
		list.add("5 Lignite = 4 Sub-Bituminous");
		list.add("3 S-Bit = 2 Bit");
		list.add("3 Bit = 2 Coal");
		list.add("6 Coal = 5 Anthracite");
		list.add("1 Anthracite = 6 Graphite");
		
		return list;
	}
	
	private static List<String> fuelInfo3()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Values:");
		list.add("");
		list.add("All fuel has an internal worth,");
		list.add("which is used by all machines to");
		list.add("determine how long they last.  The ");
		list.add("values of fuels are as follows:");
		list.add("Each tick is one cycle of the game,");
		list.add("usually 1/20th a second.");
		list.add("");
		list.add("Charcoal = Coal = 1600 Ticks");
		list.add("Peat = 600  Lignite = 800");
		list.add("S-Bit = 1000  Bit = 1200");
		list.add("Anthracite = 2000");
		list.add("Graphite = 333");
		
		return list;
	}
	
	private static List<String> recipeCharmInfo()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Recipe Charm:");
		list.add("");
		list.add("This was created as a way to avoid");
		list.add("recipe conflicts with other mods.");
		list.add("As such it is only craftable when");
		list.add("a mod that has a known conflict is ");
		list.add("detected.");
		return list;
	}
	
	private static List<String> fuelCellInfo()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Cell:");
		list.add("");
		list.add("This item allows you to store a");
		list.add("large amount of fuel time in a");
		list.add("single item for use in other");
		list.add("machines.  Once fuel is converted");
		list.add("it cannot be converted back to");
		list.add("solid form.");
		
		return list;
	}
	
	private static List<String> fuelCellBenchInfo()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Fuel Cell Filler:");
		list.add("");
		list.add("This machine allows you to fill");
		list.add("a fuel cell, placed in the large");
		list.add("inventory slot with fuel placed");
		list.add("in the slots to the left.  The");
		list.add("machine can be sped up by placing");
		list.add("structure blocks in the upgrade");
		list.add("slots on the right.");
		
		return list;
	}
	
	private static List<String> portableScannerInfo1()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Portable Scanner:");
		list.add("");
		list.add("The portable scanner is a handheld tool");
		list.add("that when used on a block will give you");
		list.add("some information about the volume behind");
		list.add("it.  Sneak-right clicking will open a gui");
		list.add("where you can add fuel, either as a solid");
		list.add("or in a fuel cell, change the dimensions");
		list.add("of the volume by placing structure blocks");
		list.add("in the three top left slots, or change the");
		list.add("mode by placing a machine or furnace");
		list.add("structure block in the top center slot.");
		list.add("Every use consumes an amount of fuel,");
		list.add("which can be seen by mousing over the");
		
		return list;
	}
	
	private static List<String> portableScannerInfo2()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Portable Scanner:");
		list.add("");
		list.add("information area above the player");
		list.add("inventory.  Increasing the tier of the");
		list.add("structure blocks in the volume slots will");
		list.add("decrease the fuel usage significantly.");
		list.add("Keep in mind the tier of the block used to");
		list.add("change modes does not matter.");
		list.add("The two modes are Basic and Extended.");
		list.add("Basic mode will tell you the total number");
		list.add("of blocks in the volume, as well as the");
		list.add("actual number of blocks, useful for finding");
		list.add("nearby caves.  This mode will also show");
		list.add("the maximum tool level needed to harvest");
		
		return list;
	}
	
	private static List<String> portableScannerInfo3()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Portable Scanner:");
		list.add("");
		list.add("the entire volume.  Extended mode, in");
		list.add("addition to showing the Basic mode");
		list.add("information, also shows a list of");
		list.add("blocks in the volume and how many of");
		list.add("each there are.  The fuel usage for");
		list.add("Extended mode is higher as well.");
		
		return list;
	}
	
	private static List<String> boreInfo1()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Industrial Tunnel Bore:");
		list.add("");
		list.add("The Industrial Tunnel Bore is a machine ");
		list.add("designed with one purpose in mind, to ");
		list.add("remove large sections of the world and ");
		list.add("deposit them for further processing.  The ");
		list.add("energy requirements to this are rather high ");
		list.add("and dependent on the blocks in the way of ");
		list.add("the dig head.  The size of the dig head ");
		list.add("is scalable, from 1x1 to 15x15, with any ");
		list.add("odd dimension in between, with the width ");
		list.add("and height being separate.  This size is ");
		list.add("controlled from within the GUI, where the ");
		list.add("two left slots accept structure blocks and ");
		list.add("increase the width and height respectively.  ");
		list.add("As the bore digs, it leaves behind a fragile ");
		
		return list;
	}
	
	private static List<String> boreInfo2()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Industrial Tunnel Bore:");
		list.add("");
		list.add("scaffolding to maintain the integrity of the ");
		list.add("tunnel and prevent the undesired \"hall'o'mobs\" ");
		list.add("left behind other such methods.  This scaffold ");
		list.add("can be toggled to either solid or hollow, ");
		list.add("the only difference being the additional ");
		list.add("cpu load on the server with the more ");
		list.add("complicated pattern.  The bore requires an ");
		list.add("inventory to be directly behind it, on the ");
		list.add("face with the single hole, or the internal ");
		list.add("buffer fills and the bore will jam.  A redstone ");
		list.add("signal is also needed.  The bore will also jam ");
		list.add("if there is insufficient fuel to break the ");
		list.add("next block in the dig pattern or it encounters ");
		list.add("an indestructible block.  Should the Industrial ");
		
		return list;
	}
	
	private static List<String> boreInfo3()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Industrial Tunnel Bore:");
		list.add("");
		list.add("Bore be unable to dig for whatever reason, ");
		list.add("the closest player will be informed every ");
		list.add("dig cycle.  If this is found to be unbearably ");
		list.add("irritating, the messages can be disabled in ");
		list.add("the config file.  ");
		list.add("");
		list.add("The Industrial Tunnel Bore can be upgraded ");
		list.add("using a series of upgrade items.  First is ");
		list.add("the haste upgrades, which decrease the ");
		list.add("operation delay by a flat amount depending ");
		list.add("on the tier, up to a minimum of 5 ticks.  ");
		list.add("Then there is the fortune upgrades, which ");
		list.add("add levels of fortune to the dig head.  ");
		list.add("Third there is efficiency upgrades, which ");

		
		return list;
	}
	
	private static List<String> boreInfo4()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("Industrial Tunnel Bore:");
		list.add("");
		list.add("reduce the fuel consumption by a percentage.  ");
		list.add("Finally there is the silk touch upgrade, which ");
		list.add("adds the silk touch effect to the dig head, ");
		list.add("and the hardness upgrade, which makes the ");
		list.add("bore ignore block hardness when it determines ");
		list.add("fuel consumption and if it can break the ");
		list.add("block or not.  You can only use three ");
		list.add("upgrades and most will stack with each other.  ");
		list.add("Obviously one cannot silk touch or ignore ");
		list.add("hardness any more than once, so stacking ");
		list.add("those upgrades does nothing useful.  The base ");
		list.add("fuel usage and cooldown time is set in the ");
		list.add("config, under fuel usage and max cooldown ");
		list.add("time respectively.  ");
		
		return list;
	}
}

/*******************************************************************************
* Copyright (c) 2014 Malorolam.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the included license, which is also
* available at http://carbonization.wikispaces.com/License
* 
*********************************************************************************/