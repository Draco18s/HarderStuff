package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.entities.TileEntityWindmill;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelAxel extends ModelBase {
	ModelRenderer Seal;
	ModelRenderer AxelCore;
	ModelRenderer AxelBotMid;
	ModelRenderer AxelRMid;
	ModelRenderer AxelRR;
	ModelRenderer AxelBotBot;
	ModelRenderer AxelLMid;
	ModelRenderer AxelLL;
	ModelRenderer AxelTopMid;
	ModelRenderer AxelTopTop;
	
	public ModelAxel() {
		textureWidth = 128;
		textureHeight = 64;

		Seal = new ModelRenderer(this, 0, 0);
		Seal.addBox(-8F, -8F, -4F, 16, 16, 8);
		Seal.setRotationPoint(0F, 16F, 0F);
		Seal.setTextureSize(128, 64);
		Seal.mirror = true;
		setRotation(Seal, 0F, 0F, 0F);
		AxelCore = new ModelRenderer(this, 48, 0);
		AxelCore.addBox(-6F, -6F, -8F, 12, 12, 16);
		AxelCore.setRotationPoint(0F, 16F, 0F);
		AxelCore.setTextureSize(128, 64);
		AxelCore.mirror = true;
		setRotation(AxelCore, 0F, 0F, 0F);
		AxelBotMid = new ModelRenderer(this, 80, 28);
		AxelBotMid.addBox(-4F, 5F, -8F, 8, 1, 16);
		AxelBotMid.setRotationPoint(0F, 17F, 0F);
		AxelBotMid.setTextureSize(128, 64);
		AxelBotMid.mirror = true;
		setRotation(AxelBotMid, 0F, 0F, 0F);
		AxelRMid = new ModelRenderer(this, 46, 28);
		AxelRMid.addBox(6F, -4F, -8F, 1, 8, 16);
		AxelRMid.setRotationPoint(0F, 16F, 0F);
		AxelRMid.setTextureSize(128, 64);
		AxelRMid.mirror = true;
		setRotation(AxelRMid, 0F, 0F, 0F);
		AxelRR = new ModelRenderer(this, 0, 41);
		AxelRR.addBox(6.99F, -2F, -8F, 1, 4, 16);
		AxelRR.setRotationPoint(0F, 16F, 0F);
		AxelRR.setTextureSize(128, 64);
		AxelRR.mirror = true;
		setRotation(AxelRR, 0F, 0F, 0F);
		AxelBotBot = new ModelRenderer(this, 0, 24);
		AxelBotBot.addBox(-2F, 6.99F, -8F, 4, 1, 16);
		AxelBotBot.setRotationPoint(0F, 16F, 0F);
		AxelBotBot.setTextureSize(128, 64);
		AxelBotBot.mirror = true;
		setRotation(AxelBotBot, 0F, 0F, 0F);
		AxelLMid = new ModelRenderer(this, 46, 28);
		AxelLMid.addBox(-7F, -4F, -8F, 1, 8, 16);
		AxelLMid.setRotationPoint(0F, 16F, 0F);
		AxelLMid.setTextureSize(128, 64);
		AxelLMid.mirror = true;
		setRotation(AxelLMid, 0F, 0F, 0F);
		AxelLL = new ModelRenderer(this, 0, 41);
		AxelLL.addBox(-7.99F, -2F, -8F, 1, 4, 16);
		AxelLL.setRotationPoint(0F, 16F, 0F);
		AxelLL.setTextureSize(128, 64);
		AxelLL.mirror = true;
		setRotation(AxelLL, 0F, 0F, 0F);
		AxelTopMid = new ModelRenderer(this, 80, 28);
		AxelTopMid.addBox(-4F, -7F, -8F, 8, 1, 16);
		AxelTopMid.setRotationPoint(0F, 16F, 0F);
		AxelTopMid.setTextureSize(128, 64);
		AxelTopMid.mirror = true;
		setRotation(AxelTopMid, 0F, 0F, 0F);
		AxelTopTop = new ModelRenderer(this, 0, 24);
		AxelTopTop.addBox(-2F, -7.99F, -8F, 4, 1, 16);
		AxelTopTop.setRotationPoint(0F, 16F, 0F);
		AxelTopTop.setTextureSize(128, 64);
		AxelTopTop.mirror = true;
		setRotation(AxelTopTop, 0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		AxelCore.render(f5);
		AxelBotMid.render(f5);
		AxelRMid.render(f5);
		AxelRR.render(f5);
		AxelBotBot.render(f5);
		AxelLMid.render(f5);
		AxelLL.render(f5);
		AxelTopMid.render(f5);
		AxelTopTop.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
		setRotation(Seal, f, f1, f2);
		setRotation(AxelCore, f, f1, f2);
		setRotation(AxelBotMid, f, f1, f2);
		setRotation(AxelRMid, f, f1, f2);
		setRotation(AxelRR, f, f1, f2);
		setRotation(AxelBotBot, f, f1, f2);
		setRotation(AxelLMid, f, f1, f2);
		setRotation(AxelLL, f, f1, f2);
		setRotation(AxelTopMid, f, f1, f2);
		setRotation(AxelTopTop, f, f1, f2);
	}

	public void render(TileEntity tileentity, double x, double y, double z) {
		TileEntityWindmill es = (TileEntityWindmill)tileentity;
		GL11.glPushMatrix();
		
		//GL11.glRotatef(es.getRotationX(), 1, 0, 0);
		
		GL11.glTranslatef((float)x + es.getOffsetX(), (float)y + es.getOffsetY(), (float)z + es.getOffsetZ());
		
		//GL11.glScalef(0.5f, 0.5f, 0.5f);
		//FMLClientHandler.instance().getClient().renderEngine.bindTexture(es.getModelTexture());
		ResourceLocation rl = new ResourceLocation(es.getModelTexture());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
		this.render(null, 0, es.getRotationY(), 0, 0, 0, 0.0625F);
		if(es.getAxelPosition() == 1) {
			Seal.render(0.0625F);
		}
		//this.render(null, es.getRotationX(), es.getRotationY(), es.getRotationZ(), 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}

}
