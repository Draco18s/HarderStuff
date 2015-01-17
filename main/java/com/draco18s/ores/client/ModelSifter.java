package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.entities.TileEntityWindmill;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelSifter extends ModelBase {
	ModelRenderer Back;
	ModelRenderer Front;
	ModelRenderer Left;
	ModelRenderer Right;
	ModelRenderer Grid;
	
	public ModelSifter() {
		textureWidth = 64;
		textureHeight = 64;
		
		Back = new ModelRenderer(this, 0, 0);
		Back.addBox(0F, 0F, -1F, 16, 16, 2);
		Back.setRotationPoint(-8F, 8F, 7F);
		Back.setTextureSize(64, 64);
		Back.mirror = true;
		setRotation(Back, 0F, 0F, 0F);
		Front = new ModelRenderer(this, 0, 0);
		Front.addBox(0F, 0F, -1F, 16, 16, 2);
		Front.setRotationPoint(-8F, 8F, -7F);
		Front.setTextureSize(64, 64);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		Left = new ModelRenderer(this, 0, 18);
		Left.addBox(-1F, 0F, 0F, 2, 16, 12);
		Left.setRotationPoint(-7F, 8F, -6F);
		Left.setTextureSize(64, 64);
		Left.mirror = true;
		setRotation(Left, 0F, 0F, 0F);
		Right = new ModelRenderer(this, 0, 18);
		Right.addBox(-1F, 0F, 0F, 2, 16, 12);
		Right.setRotationPoint(7F, 8F, -6F);
		Right.setTextureSize(64, 64);
		Right.mirror = true;
		setRotation(Right, 0F, 0F, 0F);
		Grid = new ModelRenderer(this, 0, 46);
		Grid.addBox(0F, 0F, 0F, 12, 1, 12);
		Grid.setRotationPoint(-6F, 16F, -6F);
		Grid.setTextureSize(64, 64);
		Grid.mirror = true;
		setRotation(Grid, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		Back.render(f5);
		Front.render(f5);
		Left.render(f5);
		Right.render(f5);
		Grid.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5);
		setRotation(Back, f, f1, f2);
		setRotation(Front, f, f1, f2);
		setRotation(Left, f, f1, f2);
		setRotation(Right, f, f1, f2);
		setRotation(Grid, f, f1, f2);
	}

	public void render(TileEntity tileentity, double x, double y, double z) {
		TileEntitySifter es = (TileEntitySifter)tileentity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y - 0.5f, (float)z + 0.5f);
		ResourceLocation rl = new ResourceLocation(es.getModelTexture());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
		this.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}
}
