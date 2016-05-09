package com.draco18s.wildlife.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLizard extends RenderLiving {
	private static final ResourceLocation lizTex = new ResourceLocation("wildlife:textures/entity/lizard.png");

	public RenderLizard(ModelBase p_i1262_1_) {
		super(p_i1262_1_, 0.7f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return lizTex;
	}
}
