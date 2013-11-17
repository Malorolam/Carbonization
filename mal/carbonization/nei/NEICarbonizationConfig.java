package mal.carbonization.nei;

import mal.carbonization.gui.GuiCarbonizationInfo;
import mal.carbonization.gui.GuiFurnaces;
import mal.carbonization.gui.GuiNEIMultiblockFurnace;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEICarbonizationConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new CarbonizationFurnaceRecipeHandler());
		API.registerUsageHandler(new CarbonizationFurnaceRecipeHandler());
		API.setGuiOffset(GuiFurnaces.class, 0, 0);
		
		API.registerRecipeHandler(new CarbonizationMultiblockFurnaceRecipeHandler());
		API.registerUsageHandler(new CarbonizationMultiblockFurnaceRecipeHandler());
		API.setGuiOffset(GuiNEIMultiblockFurnace.class, 0, 0);
		
		API.registerRecipeHandler(new CarbonizationItemInformationHandler());
		API.registerUsageHandler(new CarbonizationItemInformationHandler());
		API.setGuiOffset(GuiCarbonizationInfo.class, 0, 0);
	}

	@Override
	public String getName() {
		return "Carbonization plugin";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0.0";
	}

}
