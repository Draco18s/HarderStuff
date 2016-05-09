package com.draco18s.wildlife.client;

import com.draco18s.wildlife.entity.TileEntityGrassSnow;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderSnowyGrass implements ISimpleBlockRenderingHandler {
	private int renderID;
	public RenderSnowyGrass(int id) {
		renderID = id;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		renderer.renderBlockAsItem(Blocks.snow_layer, 0, 1);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderBlockByRenderType(Blocks.snow_layer, x, y, z);
		//renderer.renderBlockByRenderType(Blocks.tallgrass, x, y, z);
		Tessellator tessellator = Tessellator.instance;

		TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x, y, z);

		if(te != null ) {
			if(te.oBlock != null) {
				if(te.oBlock.getRenderType() == 1) {
					tessellator.setBrightness(te.oBlock.getMixedBrightnessForBlock(world, x, y, z));
					int l = 16777215;
					if(16777215 != te.oBlock.getRenderColor(te.oMeta)) {
						l = world.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
					}
					float f = (float)(l >> 16 & 255) / 255.0F;
					float f1 = (float)(l >> 8 & 255) / 255.0F;
					float f2 = (float)(l & 255) / 255.0F;
		
					if (EntityRenderer.anaglyphEnable) {
						float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
						float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
						float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
						f = f3;
						f1 = f4;
						f2 = f5;
					}
		
					tessellator.setColorOpaque_F(f, f1, f2);
					double d1 = (double)x;
					double d2 = (double)y;
					double d0 = (double)z;
					long i1;
		
					i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
					i1 = i1 * i1 * 42317861L + i1 * 11L;
					d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
					d2 += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
					d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		
					IIcon iicon = renderer.getBlockIconFromSideAndMetadata(te.oBlock, 0, te.oMeta);
					renderer.drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
				}
				else {
					IIcon ico = te.oBlock.getIcon(0, te.oMeta);
					renderer.renderBlockUsingTexture(te.oBlock, x, y, z, ico);
				}
			}
			else {
				//System.out.println("TE Block was null");
			}
		}
		return false;
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
