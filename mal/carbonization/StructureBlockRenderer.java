package mal.carbonization;

import mal.carbonization.tileentity.TileEntityStructureBlock;
import mal.carbonization.blocks.BlockStructureBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class StructureBlockRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
        
		Tessellator tessellator = Tessellator.instance;
        renderer.renderStandardBlock(block, x, y, z);
        
		//tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);

		TileEntity te = world.getBlockTileEntity(x, y, z);
		TileEntityStructureBlock dte = null;
		Icon baseIcon;
		Icon secondaryIcon;
		Icon purposeIcon;

		if(te instanceof TileEntityStructureBlock)
		{
			dte = (TileEntityStructureBlock)te;
		}
		else
		{
			if(te == null)
				System.out.println("TileEntity null!");
			else
				System.out.println("TileEntity not correct!");
			
			return false;
		}

		if(block instanceof BlockStructureBlock && dte != null)
		{
			baseIcon = ((BlockStructureBlock)block).getTextureFromDataandPass(dte.baseMaterial, 0);
			secondaryIcon = ((BlockStructureBlock)block).getTextureFromDataandPass(dte.secondaryMaterial, 1);
			purposeIcon = ((BlockStructureBlock)block).getTextureFromDataandPass(dte.purpose, 2);
		}
		else
		{
			return false;
		}

		Icon icon = null;
		
		//tessellator.startDrawingQuads();

		for(int i = 0; i<3; i++)
		{

			switch(i)
			{
			case 0:
				icon = baseIcon;
				break;
			case 1:
				icon = secondaryIcon;
				break;
			case 2:
				icon = purposeIcon;
				break;
			default:
				icon = null;
			}

			// xpos face textures
			if(icon != null)
			{
				renderer.renderFaceXPos(block, x, y, z, icon);
/*				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
			}

			// xneg face textures
			if(icon != null)
			{
				renderer.renderFaceXNeg(block, x, y, z, icon);
/*				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
			}

			// zneg face textures
			if(icon != null)
			{
				renderer.renderFaceZNeg(block, x, y, z, icon);
/*				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
			}

			// zpos face textures
			if(icon != null)
			{
				renderer.renderFaceZPos(block, x, y, z, icon);
/*				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
			}

			if(i<2)
			{
				// ypos face textures
				if(icon != null)
				{
					renderer.renderFaceYPos(block, x, y, z, icon);
/*					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
				}

				// yneg face textures
				if(icon != null)
				{
					renderer.renderFaceYNeg(block, x, y, z, icon);
/*					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());*/
				}
			}
		}
		//tessellator.draw();

		return true;
	}
    
	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.structureBlockRenderType;
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