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
import mal.carbonization.network.PacketHandler;
import mal.carbonization.network.PortableScannerWrapper;
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

public class GuiPortableScanner extends GuiContainer {

	private PortableScannerWrapper scanner;
	private static ContainerPortableScanner cont;
	private InventoryPlayer player;
	
	public GuiPortableScanner(InventoryPlayer iplayer, PortableScannerWrapper wrap) {
		super(cont = new ContainerPortableScanner(iplayer, wrap));
		player = iplayer;
		scanner = wrap;
	}
    
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
		GL11.glScalef(1f, 1f, 1f);
		
		int xsize = ((ItemPortableScanner) scanner.getStack().getItem()).getSizeDim(scanner.getStack(),0);
		int ysize = ((ItemPortableScanner) scanner.getStack().getItem()).getSizeDim(scanner.getStack(),1);
		int zsize = ((ItemPortableScanner) scanner.getStack().getItem()).getSizeDim(scanner.getStack(),2);
    	this.fontRenderer.drawString(Integer.toString(xsize), 10, 30, 4210752);
    	this.fontRenderer.drawString(Integer.toString(ysize), 29, 30, 4210752);
    	this.fontRenderer.drawString(Integer.toString(zsize), 47, 30, 4210752);

        this.fontRenderer.drawString("Total Volume: " + xsize*ysize*zsize, 8, 42, 4210752);
        int mode = ((ItemPortableScanner)scanner.getStack().getItem()).getMode(scanner.getStack());
        this.fontRenderer.drawString("Mode: " + ((mode==0)?("Basic"):("Extended")), 8, 52, 4210752);
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/portableScannerGui.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        int var7 = this.getFuelCapacityScaled(52);
        this.drawTexturedModalRect(var5+152, var6+63-var7, 176, 68-var7, 16, var7);
	}

	private int getFuelCapacityScaled(int i)
	{
		return (int) (((ItemPortableScanner) scanner.getStack().getItem()).getFuelValue(scanner.getStack())*i/(ItemPortableScanner.MAXFUEL+1));
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
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", ((ItemPortableScanner) scanner.getStack().getItem()).getFuelValue(scanner.getStack()))+"/3200"+"FT");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the information
		if(this.isPointInRegion(8, 30, 72, 32, par1, par2))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Current Fuel Usage:");
			list.add(ColorReference.DARKGREY.getCode() + String.format("%.2f", ((ItemPortableScanner)scanner.getStack().getItem()).getFuelUsage(scanner.getStack())) + "FT/action");
			
	        this.drawHoveringText(list, par1, par2, fontRenderer);
		}
		
		//see if the mouse is over the upgrade slots
		if(this.isPointInRegion(8, 10, 16, 16, par1, par2) && scanner.getItemInSlot(0)==null)
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