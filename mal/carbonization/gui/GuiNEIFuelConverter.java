package mal.carbonization.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.containers.ContainerFuelConverter;
import mal.carbonization.tileentity.TileEntityAutocraftingBench;
import mal.carbonization.tileentity.TileEntityFuelConversionBench;
import mal.carbonization.tileentity.TileEntityFurnaces;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiNEIFuelConverter extends GuiContainer {
	
	private TileEntityFuelConversionBench bench;
	private GuiButton upBtn;
	private GuiButton downBtn;
	private GuiButton dustBtn;
	
	public GuiNEIFuelConverter(InventoryPlayer par1InventoryPlayer, TileEntityFuelConversionBench par2)
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
		this.buttonList.add(this.dustBtn = new GuiButton(3, this.width/2 +9, this.height/2-64, 32, 16, (!bench.fuelState)?("Liquid"):("Solid")));
	}

	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 1:
        	bench.closeGui(0);
        	break;
        case 2:
			bench.closeGui(1);
        	break;
        case 3:
        	bench.closeGui(-1);
        	dustBtn.displayString = (bench.fuelState)?("Liquid"):("Solid");
        	break;
        }
    }
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	//this.fontRenderer.drawString(((Integer)bench.getFuelUsagePercent()).toString(), 79, 68, 4210752);
		GL11.glScalef(0.75f, 0.75f, 0.75f);
        this.fontRendererObj.drawString("Fuel Conversion Bench", 54, 6, 4210752);
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
    	this.drawTexturedModalRect(var5+54, var6+63-var7, 224, 52-var7, 16, var7);
        int var8 = bench.getCooldownScaled(24);
        if(true)
        {
            this.drawTexturedModalRect(var5+72, var6+29, 200, 17, 24, 16);
        	this.drawTexturedModalRect(var5+72+var8, var6+29, 176+var8, 17, 24-var8, 16);
        }
        else
        {
            this.drawTexturedModalRect(var5+72, var6+29, 200, 0, 24, 16);
        	this.drawTexturedModalRect(var5+72, var6+29, 176, 0, 24-var8, 16);
        }
        
        //new code for item system
        ItemStack is = bench.getCurrentItem();
        IIcon icon=null;
        if(is!=null)
        	icon = is.getIconIndex();

        if (icon != null && !bench.fuelState)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(var5+98, var6+29, icon, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//see if the mouse is over the potential bar
		if(UtilReference.isPointInRegion(54, 11, 16, 52, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Fuel Potential:");
			list.add(ColorReference.DARKGREY.getCode() + bench.getFuelStack()+"/"+bench.getMaxCapacity() +"FP");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the upgrade slot
		if(UtilReference.isPointInRegion(152, 11, 16, 16, par1, par2, guiLeft, guiTop) && bench.upgradeStacks[0]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Upgrades Here.");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the upgrade slot
		if(UtilReference.isPointInRegion(152, 29, 16, 16, par1, par2, guiLeft, guiTop) && bench.upgradeStacks[1]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Upgrades Here.");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the fuel slot
		if(UtilReference.isPointInRegion(152, 47, 16, 16, par1, par2, guiLeft, guiTop) && bench.upgradeStacks[2]==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Upgrades Here.");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the cooldown
		if(UtilReference.isPointInRegion(72, 28, 24, 24, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Ticks until conversion: ");
			list.add(bench.craftingCooldown + "/" + bench.maxCooldown);
			if(bench.fuelState)
			{
				list.add(ColorReference.DARKCYAN.getCode() + "Conversion Yield Increase: ");
				list.add(String.format("%.2f",1+bench.bonusYield));
			}
			else
			{
				list.add(ColorReference.DARKCYAN.getCode() + "Creation Cost Multiplyer: ");
				list.add(String.format("%.2f",2-bench.bonusYield));
			}
			//list.add(bench.efficiencyUpgrade + " " + bench.speedUpgrade);
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
				
		//add in furnace specific information
		if(UtilReference.isPointInRegion(98, 28, 18, 18, par1, par2, guiLeft, guiTop) && !bench.fuelState)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Current Output:");
			list.add(ColorReference.DARKCYAN.getCode() + bench.getCurrentItem().getDisplayName());
			list.add(ColorReference.DARKCYAN.getCode() + "Potential Cost: " + bench.getCurrentCost() +"FP");

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