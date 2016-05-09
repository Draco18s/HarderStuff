package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.entities.TileEntityWindvane;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelWindSail extends ModelBase {
	ModelRenderer Rib;
	ModelRenderer Sail;
	
	public ModelWindSail() {
		textureWidth = 32;
		textureHeight = 32;
		
		Rib = new ModelRenderer(this, 0, 0);
		Rib.addBox(-1F, 0F, -1F, 2, 16, 2);
		Rib.setRotationPoint(-4F, 8F, 0.5F);
		Rib.setTextureSize(32, 32);
		Rib.mirror = true;
		setRotation(Rib, 0F, 0F, 0F);
		Sail = new ModelRenderer(this, 8, 0);
		Sail.addBox(1F, 0F, -0.5F, 8, 16, 1);
		Sail.setRotationPoint(-4F, 8F, 0.5F);
		Sail.setTextureSize(32, 32);
		Sail.mirror = true;
		setRotation(Sail, 0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		Rib.render(f5);
		Sail.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
		setRotation(Rib, f, f1, f2);
		setRotation(Sail, f, f1, f2);
	}

	public void render(TileEntity tileentity, double x, double y, double z) {
		TileEntityWindvane es = (TileEntityWindvane)tileentity;
		GL11.glPushMatrix();
		
		//GL11.glRotatef(es.getRotationX(), 1, 0, 0);
		
		GL11.glTranslatef((float)x + es.getOffsetX(), (float)y + es.getOffsetY(), (float)z + es.getOffsetZ());
		
		//GL11.glScalef(0.5f, 0.5f, 0.5f);
		//FMLClientHandler.instance().getClient().renderEngine.bindTexture(es.getModelTexture());
		ResourceLocation rl = new ResourceLocation(es.getModelTexture());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
		this.render(null, es.getRotationX(), es.getRotationY(), es.getRotationZ(), 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}

}
