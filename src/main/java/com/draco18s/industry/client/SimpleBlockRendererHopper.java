package com.draco18s.industry.client;

import com.draco18s.industry.block.BlockCartLoader;
import com.draco18s.industry.block.BlockDistributor;
import com.draco18s.industry.block.BlockWoodHopper;
import com.draco18s.industry.block.IHopperExtender;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class SimpleBlockRendererHopper implements ISimpleBlockRenderingHandler {
	private int renderID;

	public SimpleBlockRendererHopper(int r) {
		renderID = r;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,Block block, int modelId, RenderBlocks renderer) {
		//renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
		return renderBlockHopper(renderer, (BlockHopper)block, x, y, z);
		//return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}
	
	public boolean renderBlockHopper(RenderBlocks renderer, BlockHopper p_147803_1_, int p_147803_2_, int p_147803_3_, int p_147803_4_)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(p_147803_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147803_2_, p_147803_3_, p_147803_4_));
        int l = p_147803_1_.colorMultiplier(renderer.blockAccess, p_147803_2_, p_147803_3_, p_147803_4_);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        return renderBlockHopperMetadata(renderer, p_147803_1_, p_147803_2_, p_147803_3_, p_147803_4_, renderer.blockAccess.getBlockMetadata(p_147803_2_, p_147803_3_, p_147803_4_), false);
    }

    public boolean renderBlockHopperMetadata(RenderBlocks renderer, BlockHopper p_147799_1_, int p_147799_2_, int p_147799_3_, int p_147799_4_, int p_147799_5_, boolean p_147799_6_)
    {
        Tessellator tessellator = Tessellator.instance;
        int i1 = p_147799_1_.getDirectionFromMetadata(p_147799_5_);
        double d0 = 0.625D;
        renderer.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);

        if (p_147799_6_)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 0, p_147799_5_));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 1, p_147799_5_));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 2, p_147799_5_));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 3, p_147799_5_));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 4, p_147799_5_));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(p_147799_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147799_1_, 5, p_147799_5_));
            tessellator.draw();
        }
        else
        {
        	/*renders top blob*/
            renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
        }

        float f1;

        if (!p_147799_6_)
        {
            tessellator.setBrightness(p_147799_1_.getMixedBrightnessForBlock(renderer.blockAccess, p_147799_2_, p_147799_3_, p_147799_4_));
            int j1 = p_147799_1_.colorMultiplier(renderer.blockAccess, p_147799_2_, p_147799_3_, p_147799_4_);
            float f = (float)(j1 >> 16 & 255) / 255.0F;
            f1 = (float)(j1 >> 8 & 255) / 255.0F;
            float f2 = (float)(j1 & 255) / 255.0F;

            if (EntityRenderer.anaglyphEnable)
            {
                float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }

            tessellator.setColorOpaque_F(f, f1, f2);
        }
        IIcon iicon = null;// = p_147799_1_.hopperIcon("hopper_outside");
        IIcon iicon1 = null;// = p_147799_1_.hopperIcon("hopper_inside");
        IIcon iicon2 = null;// = p_147799_1_.hopperIcon("hopper_inside");
        if(p_147799_1_ instanceof IHopperExtender) {
        	iicon = ((IHopperExtender)p_147799_1_).hopperIcon("hopper_outside");
        	iicon1 = ((IHopperExtender)p_147799_1_).hopperIcon("hopper_inside");
        	iicon2 = ((IHopperExtender)p_147799_1_).hopperIcon("hopper_bottom");
        }
        if(iicon == null) {
        	iicon = p_147799_1_.getHopperIcon("hopper_outside");
        }
        if(iicon1 == null) {
        	iicon1 = p_147799_1_.getHopperIcon("hopper_outside");
        }
        if(iicon2 == null) {
        	iicon2 = p_147799_1_.getHopperIcon("hopper_outside");
        }
        f1 = 0.125F;

        if (p_147799_6_)
        {
        	/*Renders the inside of the top blob*/
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(p_147799_1_, (double)(-1.0F + f1), 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(p_147799_1_, (double)(1.0F - f1), 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(p_147799_1_, 0.0D, 0.0D, (double)(-1.0F + f1), iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, (double)(1.0F - f1), iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(p_147799_1_, 0.0D, -1.0D + d0, 0.0D, iicon1);
            tessellator.draw();
        }
        else
        {
        	/*Renders the inside of the top blob*/
            renderer.renderFaceXPos(p_147799_1_, (double)((float)p_147799_2_ - 1.0F + f1), (double)p_147799_3_, (double)p_147799_4_, iicon);
            renderer.renderFaceXNeg(p_147799_1_, (double)((float)p_147799_2_ + 1.0F - f1), (double)p_147799_3_, (double)p_147799_4_, iicon);
            renderer.renderFaceZPos(p_147799_1_, (double)p_147799_2_, (double)p_147799_3_, (double)((float)p_147799_4_ - 1.0F + f1), iicon);
            renderer.renderFaceZNeg(p_147799_1_, (double)p_147799_2_, (double)p_147799_3_, (double)((float)p_147799_4_ + 1.0F - f1), iicon);
            renderer.renderFaceYPos(p_147799_1_, (double)p_147799_2_, (double)((float)p_147799_3_ - 1.0F) + d0, (double)p_147799_4_, iicon1);
        }

        double d3 = 0.25D;
        double d4 = 0.25D;
        renderer.setRenderBounds(d3, d4, d3, 1.0D - d3, d0 - 0.002D, 1.0D - d3);

        if (p_147799_6_)
        {
        	/*Renders the center blob*/
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
            tessellator.draw();
        }
        else
        {
        	/*Renders the center blob*/
            renderer.setOverrideBlockTexture(iicon);
            renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
        }

        if (!p_147799_6_)
        {
        	/*Renders the bottom blob*/
            double d1 = 0.375D;
            double d2 = 0.25D;
            renderer.setOverrideBlockTexture(iicon2);

            if (i1 == 0)
            {
                renderer.setRenderBounds(d1, 0.0D, d1, 1.0D - d1, 0.25D, 1.0D - d1);
                renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
            }

            if (i1 == 2)
            {
                renderer.setRenderBounds(d1, d4, 0.0D, 1.0D - d1, d4 + d2, d3);
                renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
            }

            if (i1 == 3)
            {
                renderer.setRenderBounds(d1, d4, 1.0D - d3, 1.0D - d1, d4 + d2, 1.0D);
                renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
            }

            if (i1 == 4)
            {
                renderer.setRenderBounds(0.0D, d4, d1, d3, d4 + d2, 1.0D - d1);
                renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
            }

            if (i1 == 5)
            {
                renderer.setRenderBounds(1.0D - d3, d4, d1, 1.0D, d4 + d2, 1.0D - d1);
                renderer.renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
            }
        }

        renderer.clearOverrideBlockTexture();
        return true;
    }
}
