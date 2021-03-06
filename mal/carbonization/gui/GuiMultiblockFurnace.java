package mal.carbonization.gui;

import java.awt.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.containers.ContainerMultiblockFurnace;
import mal.carbonization.tileentity.TileEntityMultiblockFurnace;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMultiblockFurnace extends GuiContainer{

	private TileEntityMultiblockFurnace furnaceInventory;
	private GuiButton passBtn;
	private GuiButton holdBtn;
	
	public GuiMultiblockFurnace(TileEntityMultiblockFurnace par2TileEntityFurnace, InventoryPlayer par1InventoryPlayer) {
		super(new ContainerMultiblockFurnace(par1InventoryPlayer, par2TileEntityFurnace));
		furnaceInventory = par2TileEntityFurnace;
	}
	
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(this.passBtn = new GuiButton(1, this.width / 2 - 83, this.height / 2 -13, 55, 12, "Pass Fuel"));
		this.buttonList.add(this.holdBtn = new GuiButton(2, this.width / 2 - 23, this.height / 2 -13, 55, 12, "Hold Fuel"));
		
		if(furnaceInventory.passFuel)
			passBtn.enabled = false;
		else
			holdBtn.enabled = false;
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		//furnaceInventory.closeGui();
		//PacketDispatcher.sendPacketToServer(furnaceInventory.getDescriptionPacket());
	}
	
	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 1:
        	passBtn.enabled = false;
        	holdBtn.enabled = true;
			furnaceInventory.passFuel = true;
        	break;
        case 2:
        	holdBtn.enabled = false;
        	passBtn.enabled = true;
			furnaceInventory.passFuel = false;
        	break;
        }
        furnaceInventory.closeGui();
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/multiblockFurnace.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;

        //if (this.furnaceInventory.hasWork())
        {
            var7 = this.furnaceInventory.getQueueCapacityScaled(38);
            this.drawTexturedModalRect(var5+62, var6+64 - var7, 176, 38 - var7, 14, var7);
        }
        var7 = this.furnaceInventory.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+136, var6+69-var7, 192, 91-var7, 16, var7);
        
        //var7 = this.furnaceInventory.getSlagCapacityScaled(38);
        //this.drawTexturedModalRect(136, 68 - var7, 192, 90, 16, var7 + 2);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(furnaceInventory.getNumQueueJobs() + "/" + furnaceInventory.oreCapacity, 62, 6, 4210752);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//see if the mouse is over the fuel bar
		if(UtilReference.isPointInRegion(136, 17, 16, 52, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Fuel Time:");
			list.add(ColorReference.DARKGREY.getCode() + furnaceInventory.getFuelStack()+"/"+furnaceInventory.getMaxFuelCapacity()+"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the queue bar
		if(UtilReference.isPointInRegion(62, 24, 14, 38, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Items in Queue:");
			list.add(ColorReference.DARKGREY.getCode() + furnaceInventory.getNumQueueJobs()+"/"+furnaceInventory.oreCapacity);
			list.add(ColorReference.DARKGREEN.getCode() + "Queue Ticks:");
			list.add(ColorReference.DARKGREY.getCode() + furnaceInventory.getGrossCookTime()+"/"+furnaceInventory.getGrossMaxCookTime());
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the slag bar
		if(UtilReference.isPointInRegion(154, 17, 16, 52, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Metal Slag in Tank:");
			list.addAll(furnaceInventory.getSlagAsString());
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//add in furnace specific information
		if(UtilReference.isPointInRegion(62, 6, 16, 10, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Furnace Information:");
			list.add(ColorReference.DARKGREY.getCode() + "X-Size: " + furnaceInventory.xsize + " Y-Size: " + furnaceInventory.ysize + " Z-Size: " + furnaceInventory.zsize);
			list.add(ColorReference.DARKGREY.getCode() + "Metal Slag Yield: " + furnaceInventory.getSlagDistribution());
			list.add(ColorReference.DARKGREY.getCode() + "Resource Reductions:");
			list.add(ColorReference.DARKGREY.getCode() + "Fuel Tick: " + String.format("%.2f", furnaceInventory.getFuelTimeModifier()*100) + "%  Cook Tick: " + String.format("%.2f", furnaceInventory.getCookTimeModifier()*100) + "%");
			list.add(ColorReference.DARKGREY.getCode() + "Tiers:");
			list.add(ColorReference.DARKGREY.getCode() + "Conduction: " + String.format("%.2f", furnaceInventory.componentTiers[0]) + "    Insulation: " + String.format("%.2f", furnaceInventory.componentTiers[1]));
			
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