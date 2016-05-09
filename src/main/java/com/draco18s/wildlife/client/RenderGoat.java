package com.draco18s.wildlife.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.wildlife.entity.EntityGoat;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderGoat extends RenderLiving {

	private static final ResourceLocation sheepTextures = new ResourceLocation("wildlife:textures/entity/sheep_fur.png");
	private static final ResourceLocation shearedSheepTextures = new ResourceLocation("wildlife:textures/entity/sheep.png");
	
	public RenderGoat(ModelBase base, ModelBase overlay, float shadowSize) {
        super(base, shadowSize);
        this.setRenderPassModel(overlay);
    }
	
	protected int shouldRenderPass(EntityGoat p_77032_1_, int p_77032_2_, float p_77032_3_) {
        if (p_77032_2_ == 0 && !p_77032_1_.getSheared()) {
            this.bindTexture(sheepTextures);
            int j = p_77032_1_.getFleeceColor();
               GL11.glColor3f(EntityGoat.fleeceColorTable[j][0], EntityGoat.fleeceColorTable[j][1], EntityGoat.fleeceColorTable[j][2]);

            return 1;
        }
        else {
            return -1;
        }
    }
	
	protected ResourceLocation getEntityTexture(EntityGoat p_110775_1_) {
        return shearedSheepTextures;
    }
	
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
        return this.shouldRenderPass((EntityGoat)p_77032_1_, p_77032_2_, p_77032_3_);
    }

	@Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityGoat)p_110775_1_);
    }
}
