package mal.carbonization.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.containers.ContainerAutocraftingBench;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiAutocraftingBench extends GuiContainer {
	
	private TileEntityAutocraftingBench bench;
	private GuiButton upBtn;
	private GuiButton downBtn;
	
	public GuiAutocraftingBench(InventoryPlayer par1InventoryPlayer, TileEntityAutocraftingBench par2)
	{
		super(new ContainerAutocraftingBench(par1InventoryPlayer, par2));
		//System.out.println("Made a new gui");
		bench = par2;
		ySize = 220;
	}
	
	public void initGui()
	{
		super.initGui();
		
		if(!bench.disableButtons)
		{
			this.buttonList.add(this.upBtn = new GuiButton(1, this.width / 2 + 10, this.height / 2 -44, 27, 12, "+5%"));
			this.buttonList.add(this.downBtn = new GuiButton(2, this.width / 2 - 37, this.height / 2 -44, 27, 12, "-5%"));
		}
	}

	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 1:
        	bench.sendChangePacket(true);
        	break;
        case 2:
			bench.sendChangePacket(false);
        	break;
        }
    }
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	if(!bench.disableButtons)
    		this.fontRendererObj.drawString(((Integer)bench.getFuelUsagePercent()).toString(), 79, 68, 4210752);

		GL11.glScalef(0.75f, 0.75f, 0.75f);
        this.fontRendererObj.drawString("Autocrafting Bench", 64, 5, 4210752);
		GL11.glScalef(1.335f, 1.335f, 1.35f);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/autocraftingTable.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        int var7 = this.bench.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+152, var6+63-var7, 176, 68-var7, 16, var7);
        
        var7 = bench.getCooldownScaled(24);
        this.drawTexturedModalRect(var5+68, var6+29, 176, 0, 24-var7, 17);
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//see if the mouse is over the fuel bar
		if(UtilReference.isPointInRegion(152, 11, 16, 52, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Fuel Time:");
			list.add(ColorReference.DARKGREY.getCode() + bench.getFuelStack()+"/"+bench.getMaxCapacity()+"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		/*//see if the mouse is over the upgrade slot
		if(UtilReference.isPointInRegion(134, 11, 16, 16, par1, par2, guiLeft, guiTop) && bench.upgradeStacks[0]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Machine Structure Blocks");
			list.add(ColorReference.ORANGE.getCode() + "here to improve fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the fuel slot
		if(this.isPointInRegion(134, 47, 16, 16, par1, par2) && bench.upgradeStacks[1]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Goes in Here :P");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}*/
		
		//see if the mouse is over the cooldown
		if(UtilReference.isPointInRegion(66, 28, 24, 24, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Ticks until crafting: ");
			list.add(bench.craftingCooldown + "/" + bench.processTime);
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
				
		//add in furnace specific information
		if(UtilReference.isPointInRegion(52, 65, 70, 12, par1, par2, guiLeft, guiTop) && !bench.disableButtons)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Bench Information:");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Percent: " + bench.fuelUsePercent + "%");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Per Job: " + bench.getFuelUsage()+"FT");

			this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
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