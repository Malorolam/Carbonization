package mal.carbonization;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

public class StructureItemRenderer implements IItemRenderer{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		switch (type) {
		case ENTITY: {
			return (helper == ItemRendererHelper.ENTITY_BOBBING ||
					helper == ItemRendererHelper.ENTITY_ROTATION ||
					helper == ItemRendererHelper.BLOCK_3D);
		}
		case EQUIPPED: {
			return (helper == ItemRendererHelper.BLOCK_3D ||
					helper == ItemRendererHelper.EQUIPPED_BLOCK);
		}
		case EQUIPPED_FIRST_PERSON: {
			return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
		}
		case INVENTORY: {
			return (helper == ItemRendererHelper.INVENTORY_BLOCK);
		}
		default: {
			return false;
		}
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) 
	{
		Tessellator tessellator = Tessellator.instance;

		//verify that the item is a ItemStructureBlock
		if(item.itemID != carbonization.itemStructureBlock.itemID)
			return;
		
		tessellator.startDrawingQuads();

		// adjust rendering space to match what caller expects
		boolean mustundotranslate = false;
		switch (type) {
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON: {
			break; // caller expects us to render over [0,0,0] to [1,1,1], no translation necessary
		}
		case ENTITY:
		case INVENTORY: {
			// translate our coordinates so that [0,0,0] to [1,1,1] translates to the [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5] expected by the caller.
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			mustundotranslate = true; // must undo the translation when we're finished rendering
			break;
		}
		default:
			break; // never here
		}
		
		for(int i = 0; i<3; i++)
		{


			Icon icon;
			// xpos face textures
			icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
			if(icon != null)
			{
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}

			// xneg face textures
			icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
			if(icon != null)
			{
				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}

			// zneg face textures
			icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
			if(icon != null)
			{
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}

			// zpos face textures
			icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
			if(icon != null)
			{
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
				tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV());
				tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());
			}

			if(i<2)
			{
				// ypos face textures
				icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
				if(icon != null)
				{
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());
				}
	
				// yneg face textures
				icon = item.getItem().getIconFromDamageForRenderPass(item.getItemDamage(),i);
				if(icon != null)
				{
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV());
					tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
					tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV());
				}
			}

		}
		tessellator.draw();

		if (mustundotranslate) GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		
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