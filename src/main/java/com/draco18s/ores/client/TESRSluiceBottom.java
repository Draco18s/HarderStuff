package com.draco18s.ores.client;

import com.draco18s.hardlib.TileEntityInventoryRenderer;
import com.draco18s.ores.entities.TileEntitySluice;
import com.draco18s.ores.entities.TileEntitySluiceBottom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public class TESRSluiceBottom extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelSluice sluice = new ModelSluice();
	private RenderBlocks rb = RenderBlocks.getInstance();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		//rb.blockAccess = tileentity.getWorldObj();
		
		//rb.renderBlockAllFaces(Blocks.wool, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		//sluice.render(tileentity, d0, d1, d2, 0, ((TileEntitySluiceBottom)tileentity).getRotationY());
		
		//float f7 = (float)BlockLiquid.getFlowDirection(rb.blockAccess, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, Material.water);
		//System.out.println("render top? " + f7);
		//rb.blockAccess = null;
		
		renderWater(tileentity, Blocks.wool, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
	}
	
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		//sluice.render(tileentity, d0, d1, d2, 0, f);
	}
	
	private void renderWater(TileEntity tileentity, Block block, int x, int y, int z) {
		IBlockAccess blockAccess = tileentity.getWorldObj();
		Tessellator tessellator = Tessellator.instance;
		
		int m = blockAccess.getBlockMetadata(x, y, z);
		
		int meta = (m>>2)&3*2;
		
		Material material = Material.water;
		IIcon iicon = getBlockIconFromSideAndMetadata(block, 1, meta);

        int l = block.colorMultiplier(blockAccess, x, y, z);
		float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
		
        int xx = 0;
        int zz = 0;
        switch(m&3) {
			case 0:
				zz = -1;
				break;
			case 1:
				zz = 1;
				break;
			case 2:
				xx = -1;
				break;
			case 3:
				xx = 1;
				break;
		}
		float f7 = (float) (Math.atan2(zz, xx) - (Math.PI / 2D));
        
        double d2 = (double)this.getLiquidHeight(blockAccess, x, y, z, material);
        double d3 = (double)this.getLiquidHeight(blockAccess, x, y, z + 1, material);
        double d4 = (double)this.getLiquidHeight(blockAccess, x + 1, y, z + 1, material);
        double d5 = (double)this.getLiquidHeight(blockAccess, x + 1, y, z, material);
        double d6 = 0.0010000000474974513D;
        
        d2 -= d6;
        d3 -= d6;
        d4 -= d6;
        d5 -= d6;
        double d7;
        double d8;
        double d10;
        double d12;
        double d14;
        double d16;
        double d18;
        double d20;

        float f9, f10, f11;
        
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

        tessellator.startDrawingQuads();
        tessellator.setTranslation(0,0,0);
        
        d2 = 0;
        d3 = 1;
        d4 = 1;
        d5 = 0;
        
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1,1,1);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), d7, d14);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d8, d16);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d10, d18);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d12, d20);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), d7, d14);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d12, d20);
        tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d10, d18);
        tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d8, d16);
        
        tessellator.draw();
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
    
    public float getLiquidHeight(IBlockAccess blockAccess, int p_147729_1_, int p_147729_2_, int p_147729_3_, Material p_147729_4_)
    {
        int l = 0;
        float f = 0.0F;

        for (int i1 = 0; i1 < 4; ++i1)
        {
            int j1 = p_147729_1_ - (i1 & 1);
            int k1 = p_147729_3_ - (i1 >> 1 & 1);

            if (blockAccess.getBlock(j1, p_147729_2_ + 1, k1).getMaterial() == p_147729_4_)
            {
                return 1.0F;
            }

            Material material1 = blockAccess.getBlock(j1, p_147729_2_, k1).getMaterial();

            if (material1 == p_147729_4_)
            {
                int l1 = blockAccess.getBlockMetadata(j1, p_147729_2_, k1);

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
}
