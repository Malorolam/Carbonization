package mal.carbonization.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.ColorReference;
import mal.carbonization.network.ContainerAutocraftingBench;
import mal.carbonization.network.ContainerFurnaces;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFurnaces;
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
		
		this.buttonList.add(this.upBtn = new GuiButton(1, this.width / 2 + 10, this.height / 2 -44, 27, 12, "+5%"));
		this.buttonList.add(this.downBtn = new GuiButton(2, this.width / 2 - 37, this.height / 2 -44, 27, 12, "-5%"));
	}

	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 1:
        	bench.changeFuelUsage(true);
        	break;
        case 2:
			bench.changeFuelUsage(false);
        	break;
        }
        bench.closeGui();
    }
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    		this.fontRenderer.drawString(((Integer)bench.getFuelUsagePercent()).toString(), 79, 68, 4210752);
        //this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
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
		if(this.isPointInRegion(152, 11, 16, 52, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Fuel Time:");
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", bench.getFuelStack())+"/"+bench.maxFuelCapacity);
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slot
		if(this.isPointInRegion(134, 11, 16, 16, par1, par2) && bench.upgradeStacks[0]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Machine Structure Blocks");
			list.add(ColorReference.ORANGE.getCode() + "here to improve fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the fuel slot
		if(this.isPointInRegion(134, 47, 16, 16, par1, par2) && bench.upgradeStacks[1]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Goes in Here :P");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the cooldown
		if(this.isPointInRegion(66, 28, 24, 24, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Ticks until crafting: ");
			list.add(bench.craftingCooldown + "/" + bench.processTime);
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
				
		//add in furnace specific information
		if(this.isPointInRegion(52, 65, 70, 12, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Bench Information:");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Percent: " + bench.fuelUsePercent + "%");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Per Job: " + String.format("%.2f", bench.getFuelUsage()));

			this.drawHoveringText(list, par1, par2, fontRenderer);
		}
	}
}
/*******************************************************************************
 * Copyright (c) 2013 Malorolam.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * 
 *********************************************************************************/