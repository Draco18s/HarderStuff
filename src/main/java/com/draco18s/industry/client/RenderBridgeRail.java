package com.draco18s.industry.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.draco18s.industry.block.BlockPoweredRailBridge;
import com.draco18s.industry.block.BlockRailBridge;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBridgeRail implements ISimpleBlockRenderingHandler {
	public int renderID = 0;

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
        int l = world.getBlockMetadata(x, y, z);
        IIcon icon = Blocks.rail.getIcon(0, l);
        boolean renderslope = true;
        if(block instanceof BlockPoweredRailBridge) {
        	icon = Blocks.golden_rail.getIcon(0, l);
        	renderslope = false;
        	l = l&7;
        }

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMinV();
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getMaxV();
        double d4 = 0.0625D;
        double d5 = (double)(x + 1);
        double d6 = (double)(x + 1);
        double d7 = (double)(x + 0);
        double d8 = (double)(x + 0);
        double d9 = (double)(z + 0);
        double d10 = (double)(z + 1);
        double d11 = (double)(z + 1);
        double d12 = (double)(z + 0);
        double d13 = (double)y + d4;
        double d14 = (double)y + d4;
        double d15 = (double)y + d4;
        double d16 = (double)y + d4;

        if (l != 1 && l != 2 && l != 3 && l != 7)
        {
            if (l == 8)
            {
                d5 = d6 = (double)(x + 0);
                d7 = d8 = (double)(x + 1);
                d9 = d12 = (double)(z + 1);
                d10 = d11 = (double)(z + 0);
            }
            else if (l == 9)
            {
                d5 = d8 = (double)(x + 0);
                d6 = d7 = (double)(x + 1);
                d9 = d10 = (double)(z + 0);
                d11 = d12 = (double)(z + 1);
            }
        }
        else
        {
            d5 = d8 = (double)(x + 1);
            d6 = d7 = (double)(x + 0);
            d9 = d10 = (double)(z + 1);
            d11 = d12 = (double)(z + 0);
        }

        if (l != 2 && l != 4)
        {
            if (l == 3 || l == 5)
            {
                ++d14;
                ++d15;
            }
        }
        else
        {
            ++d13;
            ++d16;
        }

        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        icon = Blocks.planks.getBlockTextureFromSide(0);
        d0 = (double)icon.getMinU();
        d1 = (double)icon.getMinV();
        d2 = (double)icon.getInterpolatedU(4);
        d3 = (double)icon.getMaxV();
        //System.out.println(d0 + "," + d2);
        d4 = 0.046875;
        d5 = (double)(x + 0.3125);
        d6 = (double)(x + 0.3125);
        d7 = (double)(x + 0.0625);
        d8 = (double)(x + 0.0625);
        d9 = (double)(z + 0);
        d10 = (double)(z + 1);
        d11 = (double)(z + 1);
        d12 = (double)(z + 0);
        d13 = (double)y + d4;
        d14 = (double)y + d4;
        d15 = (double)y + d4;
        d16 = (double)y + d4;
        if (l != 1 && l != 2 && l != 3 && l != 7)
        {
            if (l == 8)
            {
                d5 = d6 = (double)(x + 0);
                d7 = d8 = (double)(x + 1);
                d9 = d12 = (double)(z + 1);
                d10 = d11 = (double)(z + 0);
            }
            else if (l == 9)
            {
                d5 = d8 = (double)(x + 0);
                d6 = d7 = (double)(x + 1);
                d9 = d10 = (double)(z + 0);
                d11 = d12 = (double)(z + 1);
            }
        }
        else
        {
            d5 = d8 = (double)(x + 1);
            d6 = d7 = (double)(x + 0);
            d9 = d10 = (double)(z + 0.3125);
            d11 = d12 = (double)(z + 0.0625);
        }

        if (l != 2 && l != 4)
        {
            if (l == 3 || l == 5)
            {
                ++d14;
                ++d15;
            }
        }
        else
        {
            ++d13;
            ++d16;
        }
        //top
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d8, d16, d12, d0, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d5, d13, d9, d2, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d5, d13-0.1875, d9, d2, (double)icon.getInterpolatedV(6));
        tessellator.addVertexWithUV(d8, d16-0.1875, d12, d0, (double)icon.getInterpolatedV(6));

        tessellator.addVertexWithUV(d7, d15-0.1875, d11, d0, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d6, d14-0.1875, d10, d2, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d6, d14, d10, d2, (double)icon.getInterpolatedV(6));
        tessellator.addVertexWithUV(d7, d15, d11, d0, (double)icon.getInterpolatedV(6));
        
        d13-=0.1875;
        d14-=0.1875;
        d15-=0.1875;
        d16-=0.1875;

        //bottom
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        

        tessellator.addVertexWithUV(d5, d13+0.1875, d9, d0, d1);
        tessellator.addVertexWithUV(d6, d14+0.1875, d10, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d8, d16, d12, d2, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d2, d3);
        tessellator.addVertexWithUV(d7, d15+0.1875, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16+0.1875, d12, d0, d1);
        
        if(l == 0 || l == 4 || l == 5) {
        	d5+=0.625;
        	d6+=0.625;
        	d7+=0.625;
        	d8+=0.625;
        }
        else {
        	d9+=0.625;
        	d10+=0.625;
        	d11+=0.625;
        	d12+=0.625;
        }
        
        tessellator.addVertexWithUV(d5, d13+0.1875, d9, d0, d1);
        tessellator.addVertexWithUV(d6, d14+0.1875, d10, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);

        tessellator.addVertexWithUV(d8, d16, d12, d2, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d2, d3);
        tessellator.addVertexWithUV(d7, d15+0.1875, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16+0.1875, d12, d0, d1);
        
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        
        d13+=0.1875;
        d14+=0.1875;
        d15+=0.1875;
        d16+=0.1875;
        
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        
        tessellator.addVertexWithUV(d8, d16, d12, d0, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d5, d13, d9, d2, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d5, d13-0.1875, d9, d2, (double)icon.getInterpolatedV(6));
        tessellator.addVertexWithUV(d8, d16-0.1875, d12, d0, (double)icon.getInterpolatedV(6));
        
        tessellator.addVertexWithUV(d7, d15-0.1875, d11, d0, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d6, d14-0.1875, d10, d2, (double)icon.getInterpolatedV(3));
        tessellator.addVertexWithUV(d6, d14, d10, d2, (double)icon.getInterpolatedV(6));
        tessellator.addVertexWithUV(d7, d15, d11, d0, (double)icon.getInterpolatedV(6));
        
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
