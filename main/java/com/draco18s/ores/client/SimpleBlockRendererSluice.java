package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntitySluiceBottom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class SimpleBlockRendererSluice implements ISimpleBlockRenderingHandler {
	private int renderID;
	float incval = 0.1101f;//0.09535f;

	public SimpleBlockRendererSluice(int rid) {
		renderID = rid;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		/*renderer.renderMaxY = 0.125f;
        renderer.renderAllFaces = true;
        renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
		renderer.renderStandardBlock(Blocks.planks, 0, 0, 0);
        renderer.setOverrideBlockTexture(null);
        renderer.renderAllFaces = false;*/
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileEntitySluiceBottom ts = (TileEntitySluiceBottom)world.getTileEntity(x, y, z);
		//System.out.println("Pass: " + OresBase.proxy.renderPass);
		if(OresBase.proxy.renderPass == 0) {
			renderBase(world, x, y, z, block, modelId, renderer);
        }
		else {
			if(ts.renderWater) {
				renderWaterStream(world, x, y, z, block, modelId, renderer, ts);
			}
			else {
				renderNothing(world, x, y, z, block, modelId, renderer);
			}
		}
		return true;
	}
	
	private void renderNothing(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int l = Blocks.planks.colorMultiplier(world, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
		
        renderer.renderMinY = 1f;
        renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
		renderer.renderStandardBlockWithAmbientOcclusionPartial(Blocks.planks, x, y-1, z, f, f1, f2);
        renderer.setOverrideBlockTexture(null);
	}
	
	private void renderBase(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int l = Blocks.planks.colorMultiplier(world, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
		
        renderer.renderMaxY = 0.125f;
        renderer.renderAllFaces = true;
        renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
		renderer.renderStandardBlockWithAmbientOcclusionPartial(Blocks.planks, x, y, z, f, f1, f2);
        renderer.setOverrideBlockTexture(null);
        renderer.renderAllFaces = false;
		//renderer.renderBlockAllFaces(Blocks.planks, x, y, z);
	}
	
	private void renderWaterStream(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer, TileEntitySluiceBottom ts) {
		int m = world.getBlockMetadata(x, y, z);
		
		int meta = ts.getWaterAmount();
		//System.out.println("m: " + m);
		Tessellator tessellator = Tessellator.instance;
		Material material = Material.water;
		double d2 = 0;// = (double)this.getLiquidHeight(world, x, y, z, material);
        double d3 = 0;// = (double)this.getLiquidHeight(world, x, y, z + 1, material);
        double d4 = 0;// = (double)this.getLiquidHeight(world, x + 1, y, z + 1, material);
        double d5 = 0;// = (double)this.getLiquidHeight(world, x + 1, y, z, material);
        double d6 = 0.0010000000474974513D;
        
        //IIcon iicon = getBlockIconFromSideAndMetadata(Blocks.flowing_water, 0, 0);
        IIcon iicon = Blocks.flowing_water.getLiquidIcon("water_flow");
        
        int l = block.colorMultiplier(world, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
        TileEntitySluiceBottom te;
        int xx = 0;
        int zz = 0;
        switch(m&3) {
			case 0:
				d3 = meta * incval + 0.002f;
				d4 = meta * incval + 0.002f;
				te = ((TileEntitySluiceBottom)world.getTileEntity(x, y, z-1));
				if(te != null) {
					int mm = te.getWaterAmount();
					d2 = mm * incval + 0.002f;
					d5 = mm * incval + 0.002f;
				}
				else {
					d2 = 0.125f;
					d5 = 0.125f;
				}
				zz = -1;
				break;
			case 1:
				d2 = meta * incval + 0.002f;
				d5 = meta * incval + 0.002f;
				te = ((TileEntitySluiceBottom)world.getTileEntity(x, y, z+1));
				if(te != null) {
					int mm = te.getWaterAmount();
					d3 = mm * incval + 0.002f;
					d4 = mm * incval + 0.002f;
				}
				else {
					d3 = 0.125f;
					d4 = 0.125f;
				}
				zz = 1;
				break;
			case 2:
				d5 = meta * incval + 0.002f;
				d4 = meta * incval + 0.002f;
				te = ((TileEntitySluiceBottom)world.getTileEntity(x-1, y, z));
				if(te != null) {
					int mm = te.getWaterAmount();
					d2 = mm * incval + 0.002f;
					d3 = mm * incval + 0.002f;
				}
				else {
					d2 = 0.125f;
					d3 = 0.125f;
				}
				xx = -1;
				break;
			case 3:
				d3 = meta * incval + 0.002f;
				d2 = meta * incval + 0.002f;
				te = ((TileEntitySluiceBottom)world.getTileEntity(x+1, y, z));
				if(te != null) {
					int mm = te.getWaterAmount();
					d4 = mm * incval + 0.002f;
					d5 = mm * incval + 0.002f;
				}
				else {
					d4 = 0.125f;
					d5 = 0.125f;
				}
				xx = 1;
				break;
		}
        d2 = Math.max(0.125f, d2);
        d3 = Math.max(0.125f, d3);
        d4 = Math.max(0.125f, d4);
        d5 = Math.max(0.125f, d5);
		float f7 = (float) (Math.atan2(zz, xx) - (Math.PI / 2D));
		
        double d7;
        double d8;
        double d10;
        double d12;
        double d14;
        double d16;
        double d18;
        double d20;

        float f9, f10, f11;
        float f4 = 0.7f;
        
        f9 = MathHelper.sin(f7) * 0.25F;
        f10 = MathHelper.cos(f7) * 0.25F;
        f11 = 8.0F;
        d7 = (double)iicon.getInterpolatedU((double)(8.0F + (-f10 - f9) * 16.0F));
        d14 = (double)iicon.getInterpolatedV((double)(8.0F + (-f10 + f9) * 16.0F));
        d8 = (double)iicon.getInterpolatedU((double)(8.0F + (-f10 + f9) * 16.0F));
        d16 = (double)iicon.getInterpolatedV((double)(8.0F + (f10 + f9) * 16.0F));
        d10 = (double)iicon.getInterpolatedU((double)(8.0F + (f10 + f9) * 16.0F));
        d18 = (double)iicon.getInterpolatedV((double)(8.0F + (f10 - f9) * 16.0F));
        d12 = (double)iicon.getInterpolatedU((double)(8.0F + (f10 - f9) * 16.0F));
        d20 = (double)iicon.getInterpolatedV((double)(8.0F + (-f10 - f9) * 16.0F));
        
        //GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorRGBA_F(f, f1, f2, 1.0f);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), d7, d14);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d8, d16);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d10, d18);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d12, d20);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), d7, d14);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d12, d20);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d10, d18);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d8, d16);
        
		//GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}
	
	public float getLiquidHeight(IBlockAccess world, int x, int y, int z, Material mat)
    {
        int l = 0;
        float f = 0.0F;

        for (int i1 = 0; i1 < 4; ++i1)
        {
            int j1 = x - (i1 & 1);
            int k1 = z - (i1 >> 1 & 1);
            if (world.getBlock(j1, y + 1, k1).getMaterial() == mat)
            {
                return 1.0F;
            }

            Block b = world.getBlock(j1, y, k1);
            Material material1 = b.getMaterial();

            if (material1 == mat)
            {
                int l1 = world.getBlockMetadata(j1, y, k1);
                if (l1 >= 8 || l1 == 0)
                {
                    f += BlockLiquid.getLiquidHeightPercent(l1) * 10.0F;
                    l += 10;
                }

                f += BlockLiquid.getLiquidHeightPercent(l1);
                ++l;
            }
            else if (!material1.isSolid())
            {
                ++f;
                ++l;
            }
        }

        return 1.0F - f / (float)l;
    }
	
	public IIcon getBlockIcon(Block p_147793_1_, IBlockAccess p_147793_2_, int p_147793_3_, int p_147793_4_, int p_147793_5_, int p_147793_6_)
    {
        return this.getIconSafe(p_147793_1_.getIcon(p_147793_2_, p_147793_3_, p_147793_4_, p_147793_5_, p_147793_6_));
    }

    public IIcon getBlockIconFromSideAndMetadata(Block p_147787_1_, int p_147787_2_, int p_147787_3_)
    {
        return this.getIconSafe(p_147787_1_.getIcon(p_147787_2_, p_147787_3_));
    }

    public IIcon getBlockIconFromSide(Block p_147777_1_, int p_147777_2_)
    {
        return this.getIconSafe(p_147777_1_.getBlockTextureFromSide(p_147777_2_));
    }

    public IIcon getBlockIcon(Block p_147745_1_)
    {
        return this.getIconSafe(p_147745_1_.getBlockTextureFromSide(1));
    }

    public IIcon getIconSafe(IIcon p_147758_1_)
    {
        if (p_147758_1_ == null)
        {
            p_147758_1_ = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }

        return (IIcon)p_147758_1_;
    }
}
