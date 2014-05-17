package mal.carbonization.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import mal.carbonization.items.ItemPortableScanner;
import mal.carbonization.network.ContainerPortableScanner;
import mal.carbonization.network.ContainerTunnelBore;
import mal.carbonization.network.PacketHandler;
import mal.carbonization.network.PortableScannerWrapper;
import mal.carbonization.tileentity.TileEntityTunnelBore;
import mal.core.ColorReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

public class GuiTunnelBore extends GuiContainer {

	private TileEntityTunnelBore bore;
	private InventoryPlayer player;
	private GuiButton hollow;
	
	public GuiTunnelBore(InventoryPlayer iplayer, TileEntityTunnelBore b) {
		super(new ContainerTunnelBore(iplayer, b));
		player = iplayer;
		bore = b;
	}
    
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.add(hollow = new GuiButton(1, this.width/2 -75, this.height/2-31, 32, 18, (bore.hollowScaffold)?("Hollow"):("Solid")));
	}
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
		GL11.glScalef(1f, 1f, 1f);
		
		int xsize = bore.xSize;
		int ysize = bore.ySize;
    	this.fontRenderer.drawString(Integer.toString(xsize), 10, 30, 4210752);
    	this.fontRenderer.drawString(Integer.toString(ysize), 29, 30, 4210752);

        this.fontRenderer.drawString("Total Volume: " + xsize*ysize, 8, 42, 4210752);
    }
    
	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	switch(par1GuiButton.id)
    	{
    	case 1:
    		bore.swapTagState();
    		hollow.displayString = (bore.hollowScaffold)?("Hollow"):("Solid");
    		break;
    	}
        bore.closeGui();
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/tunnelBoreGui.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        int var7 = this.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+152, var6+63-var7, 176, 68-var7, 16, var7);
	}

	private int getFuelCapacityScaled(int i)
	{
		return (int) (bore.fuelTime*i/(bore.MAXFUEL+1));
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
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", bore.fuelTime)+"/"+bore.MAXFUEL+"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the information
		if(this.isPointInRegion(8, 30, 72, 22, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			bore.constructTooltipText(list);
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		/*if(this.isPointInRegion(8, 10, 16, 16, par1, par2) && scanner.getItemInSlot(0)==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "increase width and fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		if(this.isPointInRegion(26, 10, 16, 16, par1, par2) && scanner.getItemInSlot(1)==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "increase height and fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		if(this.isPointInRegion(44, 10, 16, 16, par1, par2) && scanner.getItemInSlot(2)==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Structure Blocks here to");
			list.add(ColorReference.ORANGE.getCode() + "increase depth and fuel efficiency");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		if(this.isPointInRegion(134, 10, 16, 16, par1, par2) && scanner.getItemInSlot(3)==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Put Fuel or a Fuel Cell Here");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		if(this.isPointInRegion(98, 10, 16, 16, par1, par2) && scanner.getItemInSlot(4)==null)
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.ORANGE.getCode() + "Put Machine or Furnace Structure");
			list.add(ColorReference.ORANGE.getCode() + "Blocks here to change mode");
			
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