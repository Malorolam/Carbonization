package mal.carbonization.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mal.carbonization.containers.ContainerFluidTransport;
import mal.carbonization.tileentity.TileEntityFluidTransport;
import mal.core.gui.GuiTexturedButton;
import mal.core.reference.ColorReference;
import mal.core.reference.UtilReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiFluidTransport extends GuiContainer{

	TileEntityFluidTransport bench;
	private GuiTexturedButton upBtn;
	private GuiTexturedButton downBtn;
	private GuiTexturedButton northBtn;
	private GuiTexturedButton southBtn;
	private GuiTexturedButton eastBtn;
	private GuiTexturedButton westBtn;
	
	public GuiFluidTransport(EntityPlayer player, TileEntityFluidTransport te) {
		super(new ContainerFluidTransport(player.inventory, te));
		bench = te;
	}
	
	public void initGui()
	{
		super.initGui();
		
		String loc = "textures/gui/directionalbuttons.png";
		this.buttonList.add(downBtn = new GuiTexturedButton(0, guiLeft+145, guiTop+10, 20, 20, 105, 0, "", "carbonization", loc, 3, bench.sideStates[0]));
		this.buttonList.add(upBtn = new GuiTexturedButton(1, guiLeft+95, guiTop+10, 20, 20, 84, 0, "", "carbonization", loc, 3, bench.sideStates[1]));
		this.buttonList.add(northBtn = new GuiTexturedButton(2, guiLeft+120, guiTop+25, 20, 20, 0, 0, "", "carbonization", loc, 3, bench.sideStates[2]));
		this.buttonList.add(southBtn = new GuiTexturedButton(3, guiLeft+120, guiTop+50, 20, 20, 21, 0, "", "carbonization", loc, 3, bench.sideStates[3]));
		this.buttonList.add(westBtn = new GuiTexturedButton(4, guiLeft+145, guiTop+35, 20, 20, 63, 0, "", "carbonization", loc, 3, bench.sideStates[4]));
		this.buttonList.add(eastBtn = new GuiTexturedButton(5, guiLeft+95, guiTop+35, 20, 20, 42, 0, "", "carbonization", loc, 3, bench.sideStates[5]));
		
	}
	
	protected void actionPerformed(GuiButton par1GuiButton)
    {
		if(par1GuiButton instanceof GuiTexturedButton)
			//((GuiTexturedButton)par1GuiButton).cycleState();
		
        bench.sendChangePacket((byte) par1GuiButton.id);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("carbonization", "textures/gui/fluidTransporterGui.png"));
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        
        int var7 = (int)Math.ceil(this.bench.getFuelCapacityScaled(52));
        FluidStack fs = bench.getFluid();
        if(fs != null)
        {
        	IIcon icon = bench.getFluid().getFluid().getIcon(bench.getFluid());
        	//System.out.println(icon.getIconName());
        	if(icon != null)
        	{
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        		this.drawTexturedModelRectFromIcon(guiLeft+45, guiTop+63-var7, icon, 34, var7);
        	}
        }
        downBtn.state=bench.sideStates[0];
        upBtn.state=bench.sideStates[1];
        northBtn.state=bench.sideStates[2];
        southBtn.state=bench.sideStates[3];
        westBtn.state=bench.sideStates[4];
        eastBtn.state=bench.sideStates[5];
	}

	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {

		GL11.glScalef(0.75f, 0.75f, 0.75f);
        this.fontRendererObj.drawString("Fluid Container", 10, 5, 4210752);
		GL11.glScalef(1.335f, 1.335f, 1.35f);
    }
    
    @Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		//see if the mouse is over the fuel bar
		if(UtilReference.isPointInRegion(45, 11, 32, 52, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Stored Fuel Time:");
			list.add(ColorReference.DARKGREY.getCode() + bench.getFluidAmount()+"/"+bench.getMaxCapacity()+"FP");
			
	        this.drawHoveringText(list, par1, par2, fontRendererObj);
		}
		
		//see if the mouse is over the the name
		if(UtilReference.isPointInRegion(8, 5, 64, 5, par1, par2, guiLeft, guiTop))
		{
			ArrayList list = new ArrayList();
			
			list.add(ColorReference.DARKCYAN.getCode() + "Transport Information:");
			list.add(ColorReference.DARKGREY.getCode() + "Transferring " + bench.maxTransferAmount + "FP every " + bench.maxTransferDelay + " ticks.");
			list.add(ColorReference.DARKGREEN.getCode() + bench.getImportSides());
			list.add(ColorReference.DARKBLUE.getCode() + bench.getExportSides());
			
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