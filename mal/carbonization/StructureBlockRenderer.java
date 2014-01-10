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
import cpw.mods.fml.client.registry.RenderingRegistry;

public class StructureBlockRenderer implements ISimpleBlockRenderingHandler {

	public static int structureBlockRenderType;
	
	public StructureBlockRenderer()
	{
		structureBlockRenderType = RenderingRegistry.getNextAvailableRenderId();
		//System.out.println("made new renderer");
	}
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
        
		Tessellator tessellator = Tessellator.instance;
        
		//int lightValue = Block.blocksList[Block.stone.blockID].getMixedBrightnessForBlock(world, x, y, z);
		//tessellator.setBrightness(lightValue);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

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
			System.out.println("block isn't right!");
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
			//System.out.println("pass " + i + " with icon: " + ((icon!=null)?(icon.toString()):"null"));

			// xpos face textures
			if(icon != null)
			{
				//renderer.renderFaceXPos(block, x, y, z, icon);
				tessellator.setNormal(1.0F, 1.0F, 0.0F);
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x+1, y, z));
				tessellator.addVertexWithUV(x+1.0, y+0.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(x+1.0, y+1.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+1.0, y+1.0, z+1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+1.0, y+0.0, z+1.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}
			tessellator.draw();

			tessellator.startDrawingQuads();
			// xneg face textures
			if(icon != null)
			{
//				renderer.renderFaceXNeg(block, x, y, z, icon);
				tessellator.setNormal(-1.0F, 1.0F, 0.0F);
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x-1, y, z));
				tessellator.addVertexWithUV(x+0.0, y+0.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(x+0.0, y+1.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+0.0, y+1.0, z+0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+0.0, y+0.0, z+0.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}
			tessellator.draw();

			tessellator.startDrawingQuads();
			// zneg face textures
			if(icon != null)
			{
//				renderer.renderFaceZNeg(block, x, y, z, icon);
				tessellator.setNormal(0.0F, 1.0F, -1.0F);
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z-1));
				tessellator.addVertexWithUV(x+0.0, y+0.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(x+0.0, y+1.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+1.0, y+1.0, z+0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+1.0, y+0.0, z+0.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}
			tessellator.draw();

			tessellator.startDrawingQuads();
			// zpos face textures
			if(icon != null)
			{
//				renderer.renderFaceZPos(block, x, y, z, icon);
				tessellator.setNormal(0.0F, 1.0F, 1.0F);
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z+1));
				tessellator.addVertexWithUV(x+1.0, y+0.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(x+1.0, y+1.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+0.0, y+1.0, z+1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(x+0.0, y+0.0, z+1.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}

			if(i<2)
			{
				tessellator.draw();

				tessellator.startDrawingQuads();
				// ypos face textures
				if(icon != null)
				{
//					renderer.renderFaceYPos(block, x, y, z, icon);
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y+1, z));
					tessellator.addVertexWithUV(x+1.0, y+1.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(x+1.0, y+1.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(x+0.0, y+1.0, z+0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(x+0.0, y+1.0, z+1.0, (double)icon.getMinU(), (double)icon.getMaxV());
				}
				tessellator.draw();

				tessellator.startDrawingQuads();
				// yneg face textures
				if(icon != null)
				{
//					renderer.renderFaceYNeg(block, x, y, z, icon);
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y-1, z));
					tessellator.addVertexWithUV(x+0.0, y+0.0, z+1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(x+0.0, y+0.0, z+0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(x+1.0, y+0.0, z+0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(x+1.0, y+0.0, z+1.0, (double)icon.getMinU(), (double)icon.getMaxV());
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
		return structureBlockRenderType;
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