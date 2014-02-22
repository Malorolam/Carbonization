package mal.carbonization.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.network.ContainerAutocraftingBench;
import mal.carbonization.network.ContainerFuelConverter;
import mal.carbonization.network.ContainerFurnaces;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelConverter;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.core.ColorReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiFuelConverter extends GuiContainer {
	
	private TileEntityFuelConverter bench;
	private GuiButton upBtn;
	private GuiButton downBtn;
	private GuiButton dustBtn;
	
	public GuiFuelConverter(InventoryPlayer par1InventoryPlayer, TileEntityFuelConverter par2)
	{
		super(new ContainerFuelConverter(par1InventoryPlayer, par2));
		//System.out.println("Made a new gui");
		bench = par2;
		ySize = 220;
	}
	
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.add(this.upBtn = new GuiButton(1, this.width / 2 - 42, this.height / 2 -44, 32, 12, "Prev."));
		this.buttonList.add(this.downBtn = new GuiButton(2, this.width / 2 + 9, this.height / 2 -44, 32, 12, "Next"));
		this.buttonList.add(this.dustBtn = new GuiButton(3, this.width/2 -15, this.height/2-81, 32, 16, bench.currentTag));
	}

	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 1:
        	bench.changeTargetFuel(false);
        	break;
        case 2:
			bench.changeTargetFuel(true);
        	break;
        case 3:
        	bench.swapTagState();
        	dustBtn.displayString = bench.currentTag;
        	break;
        }
        bench.closeGui();
    }
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	//this.fontRenderer.drawString(((Integer)bench.getFuelUsagePercent()).toString(), 79, 68, 4210752);
		GL11.glScalef(0.75f, 0.75f, 0.75f);
        this.fontRenderer.drawString("Fuel Conversion Bench", 54, 6, 4210752);
		GL11.glScalef(1.335f, 1.335f, 1.35f);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/fuelConversionTable.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        
        
        int var7 = this.bench.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+152, var6+63-var7, 176, 68-var7, 16, var7);
        
        var7 = this.bench.getPotentialCapacityScaled(52);
        this.drawTexturedModalRect(var5+8, var6+63-var7, 176, 68-var7, 16, var7);
        
        var7 = bench.getCooldownScaled(24);
        this.drawTexturedModalRect(var5+25, var6+29, 176, 0, 24-var7, 17);
        
        //new code for item system
        ItemStack is = bench.getCurrentItem();
        Icon icon=null;
        if(is!=null)
        	icon = is.getIconIndex();

        if (icon != null)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(var5+52, var6+29, icon, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
        //old code for hardcoded toggle
/*        var7 = bench.currentIndex;
        boolean dust = bench.makeDust;
        this.drawTexturedModalRect(var5+51, var6+28, 200 + 18*((dust)?(1):(0)), 18*var7, 18, 18);*/
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
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", bench.getFuelStack())+"/"+bench.maxFuelCapacity +"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the potential bar
		if(this.isPointInRegion(8, 11, 16, 52, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Potential Fuel:");
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", bench.potentialTank)+"/"+bench.maxPotentialTank +"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slot
		if(this.isPointInRegion(134, 11, 16, 16, par1, par2) && bench.upgradeStacks[0]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Machine Structure Blocks");
			list.add(ColorReference.ORANGE.getCode() + "here to improve processing speed");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slot
		if(this.isPointInRegion(134, 29, 16, 16, par1, par2) && bench.upgradeStacks[1]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Furnace Structure Blocks");
			list.add(ColorReference.ORANGE.getCode() + "here to improve fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the fuel slot
		if(this.isPointInRegion(134, 47, 16, 16, par1, par2) && bench.upgradeStacks[2]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Goes in Here :P");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the cooldown
		if(this.isPointInRegion(25, 28, 24, 24, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Ticks until crafting: ");
			list.add(bench.craftingCooldown + "/" + bench.processTime);
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage per Job: ");
			list.add(String.format("%.2f",bench.getFuelUsage()) + "FT");
			//list.add(bench.efficiencyUpgrade + " " + bench.speedUpgrade);
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
				
		//add in furnace specific information
		if(this.isPointInRegion(51, 28, 18, 18, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Current Output:");
			list.add(ColorReference.DARKCYAN.getCode() + bench.getCurrentFuel(bench.currentIndex, bench.currentTag).getDisplayName());
			list.add(ColorReference.DARKCYAN.getCode() + "Potential Cost: " + bench.getCurrentCost() +"FT");

			this.drawHoveringText(list, par1, par2, fontRenderer);
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