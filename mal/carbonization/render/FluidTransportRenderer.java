package mal.carbonization.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import mal.carbonization.block.BlockTransport;
import mal.carbonization.tileentity.TileEntityFluidTransport;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;

public class FluidTransportRenderer extends TileEntitySpecialRenderer {

	public final FluidTransporterModel model;
	
	public FluidTransportRenderer()
	{
		model = new FluidTransporterModel();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glScalef(1.6f, 1.6f, 1.6f);
		ResourceLocation texture = new ResourceLocation("carbonization", "textures/blocks/fluidTransportTexture.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		GL11.glPushMatrix();
		model.render(null, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
		Tessellator tessellator = Tessellator.instance;
		
		if(te instanceof TileEntityFluidTransport)
		{
			TileEntityFluidTransport fte = (TileEntityFluidTransport) te;
			
			FluidStack fs = fte.getFluid();
	        if(fs != null)
	        {
	        	IIcon icon = fte.getFluid().getFluid().getIcon(fte.getFluid());
	        	//System.out.println(icon.getIconName());
	        	if(icon != null)
	        	{
	        		double yscale = fte.getFuelCapacityScaled(90)/100;
	        		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	        		GL11.glPushMatrix();
	        		GL11.glEnable(GL11.GL_BLEND);
	        		GL11.glEnable(GL11.GL_CULL_FACE);
	        		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        		tessellator.startDrawingQuads();
	        		// xpos face textures
	    			if(icon != null)
	    			{
	    				//renderer.renderFaceXPos(block, x, y, z, icon);
	    				tessellator.setNormal(1.0F, 1.0F, 0.0F);
	    				tessellator.addVertexWithUV(x+0.91, y+0.1, z+0.1, (double)icon.getMaxU(), (double)icon.getMaxV());
	    				tessellator.addVertexWithUV(x+0.91, y+yscale, z+0.1, (double)icon.getMaxU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.91, y+yscale, z+0.9, (double)icon.getMinU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.91, y+0.1, z+0.9, (double)icon.getMinU(), (double)icon.getMaxV());
	    			}
	    			tessellator.draw();

	    			tessellator.startDrawingQuads();
	    			// xneg face textures
	    			if(icon != null)
	    			{
//	    				renderer.renderFaceXNeg(block, x, y, z, icon);
	    				tessellator.setNormal(-1.0F, 1.0F, 0.0F);
	    				tessellator.addVertexWithUV(x+0.09, y+0.1, z+0.9, (double)icon.getMaxU(), (double)icon.getMaxV());
	    				tessellator.addVertexWithUV(x+0.09, y+yscale, z+0.9, (double)icon.getMaxU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.09, y+yscale, z+0.1, (double)icon.getMinU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.09, y+0.1, z+0.1, (double)icon.getMinU(), (double)icon.getMaxV());
	    			}
	    			tessellator.draw();

	    			tessellator.startDrawingQuads();
	    			// zneg face textures
	    			if(icon != null)
	    			{
//	    				renderer.renderFaceZNeg(block, x, y, z, icon);
	    				tessellator.setNormal(0.0F, 1.0F, -1.0F);
	    				tessellator.addVertexWithUV(x+0.1, y+0.1, z+0.09, (double)icon.getMaxU(), (double)icon.getMaxV());
	    				tessellator.addVertexWithUV(x+0.1, y+yscale, z+0.09, (double)icon.getMaxU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.9, y+yscale, z+0.09, (double)icon.getMinU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.9, y+0.1, z+0.09, (double)icon.getMinU(), (double)icon.getMaxV());
	    			}
	    			tessellator.draw();

	    			tessellator.startDrawingQuads();
	    			// zpos face textures
	    			if(icon != null)
	    			{
//	    				renderer.renderFaceZPos(block, x, y, z, icon);
	    				tessellator.setNormal(0.0F, 1.0F, 1.0F);
	    				tessellator.addVertexWithUV(x+0.9, y+0.1, z+0.91, (double)icon.getMaxU(), (double)icon.getMaxV());
	    				tessellator.addVertexWithUV(x+0.9, y+yscale, z+0.91, (double)icon.getMaxU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.1, y+yscale, z+0.91, (double)icon.getMinU(), (double)icon.getMinV());
	    				tessellator.addVertexWithUV(x+0.1, y+0.1, z+0.91, (double)icon.getMinU(), (double)icon.getMaxV());
	    			}
	    			tessellator.draw();
	    			GL11.glPopMatrix();
	        	}
	        }
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