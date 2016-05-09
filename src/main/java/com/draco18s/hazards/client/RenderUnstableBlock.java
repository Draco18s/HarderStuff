package com.draco18s.hazards.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderUnstableBlock implements ISimpleBlockRenderingHandler {
	public static RenderUnstableBlock instance = new RenderUnstableBlock();
	public static int renderID;

	public RenderUnstableBlock() {
		
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		
		block.canRenderInPass(0);
	        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, -1.0F, 0.0F);
	        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 1.0F, 0.0F);
	        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 0.0F, -1.0F);
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 0.0F, 1.0F);
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(1.0F, 0.0F, 0.0F);
	        tessellator.addTranslation(-0, 0.0F, 0.0F);
	        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        tessellator.draw();
	        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	        
		block.canRenderInPass(1);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, -1.0F, 0.0F);
	        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 1.0F, 0.0F);
	        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 0.0F, -1.0F);
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(0.0F, 0.0F, 1.0F);
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
	        tessellator.addTranslation(0.0F, 0.0F, 0);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        tessellator.draw();
	        tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(block.getRenderColor(metadata));
	        tessellator.setNormal(1.0F, 0.0F, 0.0F);
	        tessellator.addTranslation(-0, 0.0F, 0.0F);
	        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
	        tessellator.addTranslation(0, 0.0F, 0.0F);
	        tessellator.draw();
	        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		boolean b = renderer.renderStandardBlock(block, x, y, z);
		block.canRenderInPass(0);
		return b;
		//return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}
}
