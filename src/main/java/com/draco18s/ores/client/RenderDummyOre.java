package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.OresBase;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDummyOre implements ISimpleBlockRenderingHandler {
	public static int renderID;

	public RenderDummyOre() {
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
		//Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		
		//block.canRenderInPass(0);
		renderer.setOverrideBlockTexture(block.getIcon(0, 0));
		renderer.renderBlockAsItem(Blocks.glass, 0xFFFFFF, 1);
		renderer.setOverrideBlockTexture(block.getIcon(0, 1));
		GL11.glDisable(GL11.GL_LIGHTING);
		renderer.renderBlockAsItem(Blocks.red_flower, 0xFFFFFF, 1);
		renderer.clearOverrideBlockTexture();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		//System.out.println(ClientProxy.renderPass);
		
		//if(OresBase.proxy.renderPass == 0) {
			//we are on the solid block render pass, lets render the solid diamond block
			drawDiamond(block.getIcon(0, 1),x,y,z);
		//}
		//else {
			//we are on the alpha render pass, draw the ice around the diamond
			renderer.renderBlockUsingTexture(Blocks.glass, x, y, z, block.getIcon(0, 0));
		//}
		return true;
	}

	private void drawDiamond(IIcon icon, int x, int y, int z) {
		double xvert = x;
		double yvert = y + 0.25F;
		double zvert = z;
		
		//this is the scale of the squares, in blocks
		float scale = 0.5F;

		//get the tessellator instance
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1, 1, 1);
		//do texture mapping things here
		//int var12 = (icon & 15) << 4;
		//int var13 = icon & 240;

		//set the uv coordinates
		double nU = icon.getMinU();
		double mU = icon.getMaxU();
		double nV = icon.getMinV();
		double mV = icon.getMaxV();

		//here the scale is changed
		double var22 = 0.45D * scale;

		//offset the vertices from the centre of the block
		double var24 = xvert + 0.5D - var22;
		double var26 = xvert + 0.5D + var22;
		double var28 = zvert + 0.5D - var22;
		double var30 = zvert + 0.5D + var22;

		//now create the vertices
		tessellator.addVertexWithUV(var24, yvert + scale, var28, nU, nV);
		tessellator.addVertexWithUV(var24, yvert + 0.0D, var28, nU, mV);
		tessellator.addVertexWithUV(var26, yvert + 0.0D, var30, mU, mV);
		tessellator.addVertexWithUV(var26, yvert + scale, var30, mU, nV);
		tessellator.addVertexWithUV(var26, yvert + scale, var30, nU, nV);
		tessellator.addVertexWithUV(var26, yvert + 0.0D, var30, nU, mV);
		tessellator.addVertexWithUV(var24, yvert + 0.0D, var28, mU, mV);
		tessellator.addVertexWithUV(var24, yvert + scale, var28, mU, nV);
		tessellator.addVertexWithUV(var24, yvert + scale, var30, nU, nV);
		tessellator.addVertexWithUV(var24, yvert + 0.0D, var30, nU, mV);
		tessellator.addVertexWithUV(var26, yvert + 0.0D, var28, mU, mV);
		tessellator.addVertexWithUV(var26, yvert + scale, var28, mU, nV);
		tessellator.addVertexWithUV(var26, yvert + scale, var28, nU, nV);
		tessellator.addVertexWithUV(var26, yvert + 0.0D, var28, nU, mV);
		tessellator.addVertexWithUV(var24, yvert + 0.0D, var30, mU, mV);
		tessellator.addVertexWithUV(var24, yvert + scale, var30, mU, nV);
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
