package com.draco18s.ores.client;

import org.lwjgl.opengl.GL11;

import com.draco18s.ores.entities.TileEntitySluice;
import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.entities.TileEntityWindmill;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelSluice extends ModelBase {
  //fields
    ModelRenderer WoodSlope;
    ModelRenderer LeftSide;
    ModelRenderer RightSide;
    ModelRenderer BackFace;
    ModelRenderer WaterSlope;
    ModelRenderer Bar1;
    ModelRenderer Bar3;
    ModelRenderer Bar2;
  
  public ModelSluice() {
	  textureWidth = 64;
	  textureHeight = 64;
    
      WoodSlope = new ModelRenderer(this, 0, 0);
      WoodSlope.addBox(-7F, 0.1F, -11F, 14, 1, 21);
      WoodSlope.setRotationPoint(0F, 16F, 0F);
      WoodSlope.setTextureSize(64, 64);
      WoodSlope.mirror = true;
      setRotation(WoodSlope, 0.7853982F, 0F, 0F);
      LeftSide = new ModelRenderer(this, 0, 7);
      LeftSide.addBox(-8F, -8F, -8F, 1, 16, 16);
      LeftSide.setRotationPoint(0F, 16F, 0F);
      LeftSide.setTextureSize(64, 64);
      LeftSide.mirror = true;
      setRotation(LeftSide, 0F, 0F, 0F);
      RightSide = new ModelRenderer(this, 0, 7);
      RightSide.addBox(7F, -8F, -8F, 1, 16, 16);
      RightSide.setRotationPoint(0F, 16F, 0F);
      RightSide.setTextureSize(64, 64);
      RightSide.mirror = true;
      setRotation(RightSide, 0F, 0F, 0F);
      BackFace = new ModelRenderer(this, 34, 22);
      BackFace.addBox(-7F, -8F, 7F, 14, 16, 1);
      BackFace.setRotationPoint(0F, 16F, 0F);
      BackFace.setTextureSize(64, 64);
      BackFace.mirror = true;
      setRotation(BackFace, 0F, 0F, 0F);
      WaterSlope = new ModelRenderer(this, 0, 39);
      WaterSlope.addBox(-7F, 0F, -11F, 14, 1, 21);
      WaterSlope.setRotationPoint(0F, 16F, 0F);
      WaterSlope.setTextureSize(64, 64);
      WaterSlope.mirror = true;
      setRotation(WaterSlope, 0.7853982F, 0F, 0F);
      Bar1 = new ModelRenderer(this, 0, 62);
      Bar1.addBox(-7F, -1F, 4F, 14, 1, 1);
      Bar1.setRotationPoint(0F, 16F, 0F);
      Bar1.setTextureSize(64, 64);
      Bar1.mirror = true;
      setRotation(Bar1, 0.7853982F, 0F, 0F);
      Bar3 = new ModelRenderer(this, 0, 62);
      Bar3.addBox(-7F, -1F, -6F, 14, 1, 1);
      Bar3.setRotationPoint(0F, 16F, 0F);
      Bar3.setTextureSize(64, 64);
      Bar3.mirror = true;
      setRotation(Bar3, 0.7853982F, 0F, 0F);
      Bar2 = new ModelRenderer(this, 0, 62);
      Bar2.addBox(-7F, -1F, -1F, 14, 1, 1);
      Bar2.setRotationPoint(0F, 16F, 0F);
      Bar2.setTextureSize(64, 64);
      Bar2.mirror = true;
      setRotation(Bar2, 0.7853982F, 0F, 0F);
  }
  
  @Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		WoodSlope.render(f5);
		LeftSide.render(f5);
		RightSide.render(f5);
		BackFace.render(f5);
		Bar1.render(f5);
		Bar2.render(f5);
		Bar3.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		float p = (float)(Math.PI/-4 + Math.PI);
		//super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
		setRotation(WoodSlope, p, f1, f2);
		setRotation(LeftSide, f, f1, f2);
		setRotation(RightSide, f, f1, f2);
		setRotation(BackFace, f, f1, f2);
		setRotation(Bar1, p, f1, f2);
		setRotation(Bar2, p, f1, f2);
		setRotation(Bar3, p, f1, f2);
		setRotation(WaterSlope, p, f1, f2);
	}

	public void render(TileEntity tileentity, double x, double y, double z, float f, float yRotation) {
		if(tileentity instanceof TileEntitySluice) {
			TileEntitySluice es = (TileEntitySluice)tileentity;
			GL11.glPushMatrix();
			
			GL11.glTranslatef((float)x + 0.5f, (float)y-0.5f, (float)z + 0.5f);
			
			ResourceLocation rl = new ResourceLocation(es.getModelTexture());
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
			
			this.render(null, 0, yRotation, 0, 0, 0, 0.0625F);
			if(es.checkWater()) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				WaterSlope.render(0.0625F);
				GL11.glDisable(GL11.GL_BLEND);
			}
			GL11.glPopMatrix();
		}
		else if(tileentity instanceof TileEntitySluiceBottom) {
			TileEntitySluiceBottom es = (TileEntitySluiceBottom)tileentity;
			GL11.glPushMatrix();
			
			GL11.glTranslatef((float)x + 0.5f, (float)y-0.5f, (float)z + 0.5f);
			
			ResourceLocation rl = new ResourceLocation(es.getModelTexture());
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
			
			this.render(null, 0, yRotation, 0, 0, 0, 0.0625F);
			GL11.glPopMatrix();
		}
	}

}
