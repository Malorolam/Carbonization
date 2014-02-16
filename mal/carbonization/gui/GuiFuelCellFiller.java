package mal.carbonization.gui;

import java.util.ArrayList;

import mal.carbonization.network.ContainerAutocraftingBench;
import mal.carbonization.network.ContainerFuelCellFiller;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelCellFiller;
import mal.core.ColorReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiFuelCellFiller extends GuiContainer {
	
	private TileEntityFuelCellFiller bench;
	
	
	public GuiFuelCellFiller(InventoryPlayer par1InventoryPlayer, TileEntityFuelCellFiller par2) {
		super(new ContainerFuelCellFiller(par1InventoryPlayer, par2));
		//System.out.println("Made a new gui");
		bench = par2;
		ySize = 220;
	}
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
		GL11.glScalef(0.75f, 0.75f, 0.75f);
        this.fontRenderer.drawString("Fuel Cell Filler", 84, 8, 4210752);
		GL11.glScalef(1.335f, 1.335f, 1.35f);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/fuelCellFiller.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
/*        int var7 = this.bench.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+152, var6+63-var7, 176, 68-var7, 16, var7);*/
        
        int var7 = bench.getCooldownScaled(24);
        this.drawTexturedModalRect(var5+62, var6+52, 176, 0, 24-var7, 17);
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//see if the mouse is over the upgrade slot
		if(this.isPointInRegion(144, 15, 16, 16, par1, par2) && bench.upgradeStacks[0]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "improve process efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		if(this.isPointInRegion(144, 35, 16, 16, par1, par2) && bench.upgradeStacks[1]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "improve process efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		if(this.isPointInRegion(144, 52, 16, 16, par1, par2) && bench.upgradeStacks[2]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "improve process efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the cooldown
		if(this.isPointInRegion(66, 46, 24, 24, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Ticks until crafting: ");
			list.add(bench.craftingCooldown + "/" + bench.processTime);
			list.add(ColorReference.DARKCYAN.getCode() + "Bonus Yield Multiplyer: " + String.format("%.2f", bench.getBonusYield()));
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
				
		//add in furnace specific information
		/*if(this.isPointInRegion(52, 65, 70, 12, par1, par2) && !bench.disableButtons)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Bench Information:");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Percent: " + bench.fuelUsePercent + "%");
			list.add(ColorReference.DARKCYAN.getCode() + "Fuel Usage Per Job: " + String.format("%.2f", bench.getFuelUsage()));

			this.drawHoveringText(list, par1, par2, fontRenderer);
		}*/
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