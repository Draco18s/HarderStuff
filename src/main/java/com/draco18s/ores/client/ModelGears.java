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

public class ModelGears extends ModelBase {
	ModelRenderer Corner1;
	ModelRenderer Corner2;
	ModelRenderer Corner3;
	ModelRenderer Corner4;
	ModelRenderer Bottom1;
	ModelRenderer Bottom2;
	ModelRenderer Bottom3;
	ModelRenderer Bottom4;
	ModelRenderer Top1;
	ModelRenderer Top2;
	ModelRenderer Top3;
	ModelRenderer Top4;
	ModelRenderer GearCenterVert1;
	ModelRenderer GearCrossVert1;
	ModelRenderer GearAxisVert1;
	ModelRenderer GearCenterVert2;
	ModelRenderer GearCrossVert2;
	ModelRenderer GearAxisVert2;
	ModelRenderer GearCenterVert3;
	ModelRenderer GearCrossVert3;
	ModelRenderer GearAxisVert3;
	ModelRenderer GearCenterVert4;
	ModelRenderer GearCrossVert4;
	ModelRenderer GearAxisVert4;
	ModelRenderer GearCenterHorz;
	ModelRenderer GearAxisHorz;
	ModelRenderer GearCrossHorz;
	
	public ModelGears() {
		textureWidth = 32;
		textureHeight = 32;
		
		textureWidth = 64;
		textureHeight = 32;

		Corner1 = new ModelRenderer(this, 30, 0);
		Corner1.addBox(7F, -8F, 7F, 1, 16, 1);
		Corner1.setRotationPoint(0F, 16F, 0F);
		Corner1.setTextureSize(64, 32);
		Corner1.mirror = true;
		setRotation(Corner1, 0F, 0F, 0F);
		Corner2 = new ModelRenderer(this, 30, 0);
		Corner2.addBox(-8F, -8F, 7F, 1, 16, 1);
		Corner2.setRotationPoint(0F, 16F, 0F);
		Corner2.setTextureSize(64, 32);
		Corner2.mirror = true;
		setRotation(Corner2, 0F, 0F, 0F);
		Corner3 = new ModelRenderer(this, 30, 0);
		Corner3.addBox(-8F, -8F, -8F, 1, 16, 1);
		Corner3.setRotationPoint(0F, 16F, 0F);
		Corner3.setTextureSize(64, 32);
		Corner3.mirror = true;
		setRotation(Corner3, 0F, 0F, 0F);
		Corner4 = new ModelRenderer(this, 30, 0);
		Corner4.addBox(7F, -8F, -8F, 1, 16, 1);
		Corner4.setRotationPoint(0F, 16F, 0F);
		Corner4.setTextureSize(64, 32);
		Corner4.mirror = true;
		setRotation(Corner4, 0F, 0F, 0F);
		Bottom1 = new ModelRenderer(this, 0, 0);
		Bottom1.addBox(7F, 7F, -7F, 1, 1, 14);
		Bottom1.setRotationPoint(0F, 16F, 0F);
		Bottom1.setTextureSize(64, 32);
		Bottom1.mirror = true;
		setRotation(Bottom1, 0F, 0F, 0F);
		Bottom2 = new ModelRenderer(this, 0, 15);
		Bottom2.addBox(-7F, 7F, 7F, 14, 1, 1);
		Bottom2.setRotationPoint(0F, 16F, 0F);
		Bottom2.setTextureSize(64, 32);
		Bottom2.mirror = true;
		setRotation(Bottom2, 0F, 0F, 0F);
		Bottom3 = new ModelRenderer(this, 0, 0);
		Bottom3.addBox(-8F, 7F, -7F, 1, 1, 14);
		Bottom3.setRotationPoint(0F, 16F, 0F);
		Bottom3.setTextureSize(64, 32);
		Bottom3.mirror = true;
		setRotation(Bottom3, 0F, 0F, 0F);
		Bottom4 = new ModelRenderer(this, 0, 15);
		Bottom4.addBox(-7F, 7F, -8F, 14, 1, 1);
		Bottom4.setRotationPoint(0F, 16F, 0F);
		Bottom4.setTextureSize(64, 32);
		Bottom4.mirror = true;
		setRotation(Bottom4, 0F, 0F, 0F);
		Top1 = new ModelRenderer(this, 0, 0);
		Top1.addBox(7F, -8F, -7F, 1, 1, 14);
		Top1.setRotationPoint(0F, 16F, 0F);
		Top1.setTextureSize(64, 32);
		Top1.mirror = true;
		setRotation(Top1, 0F, 0F, 0F);
		Top2 = new ModelRenderer(this, 0, 15);
		Top2.addBox(-7F, -8F, 7F, 14, 1, 1);
		Top2.setRotationPoint(0F, 16F, 0F);
		Top2.setTextureSize(64, 32);
		Top2.mirror = true;
		setRotation(Top2, 0F, 0F, 0F);
		Top3 = new ModelRenderer(this, 0, 0);
		Top3.addBox(-8F, -8F, -7F, 1, 1, 14);
		Top3.setRotationPoint(0F, 16F, 0F);
		Top3.setTextureSize(64, 32);
		Top3.mirror = true;
		setRotation(Top3, 0F, 0F, 0F);
		Top4 = new ModelRenderer(this, 0, 15);
		Top4.addBox(-7F, -8F, -8F, 14, 1, 1);
		Top4.setRotationPoint(0F, 16F, 0F);
		Top4.setTextureSize(64, 32);
		Top4.mirror = true;
		setRotation(Top4, 0F, 0F, 0F);
		GearCenterVert1 = new ModelRenderer(this, 28, 16);
		GearCenterVert1.addBox(-4F, 5F, -4F, 8, 3, 8);
		GearCenterVert1.setRotationPoint(0F, 16F, 0F);
		GearCenterVert1.setTextureSize(64, 32);
		GearCenterVert1.mirror = true;
		setRotation(GearCenterVert1, 0F, 0F, 1.570796F);
		GearCrossVert1 = new ModelRenderer(this, 0, 17);
		GearCrossVert1.addBox(-1F, 5F, -6F, 2, 3, 12);
		GearCrossVert1.setRotationPoint(0F, 16F, 0F);
		GearCrossVert1.setTextureSize(64, 32);
		GearCrossVert1.mirror = true;
		setRotation(GearCrossVert1, 0F, 0F, 1.570796F);
		GearAxisVert1 = new ModelRenderer(this, 28, 27);
		GearAxisVert1.addBox(-6F, 5F, -1F, 12, 3, 2);
		GearAxisVert1.setRotationPoint(0F, 16F, 0F);
		GearAxisVert1.setTextureSize(64, 32);
		GearAxisVert1.mirror = true;
		setRotation(GearAxisVert1, 0F, 0F, 1.570796F);
		GearCenterHorz = new ModelRenderer(this, 28, 16);
		GearCenterHorz.addBox(-4F, 5F, -4F, 8, 3, 8);
		GearCenterHorz.setRotationPoint(0F, 16F, 0F);
		GearCenterHorz.setTextureSize(64, 32);
		GearCenterHorz.mirror = true;
		setRotation(GearCenterHorz, 3.1415926F, 0.4363323F, 0F);
		GearAxisHorz = new ModelRenderer(this, 28, 27);
		GearAxisHorz.addBox(-6F, 5F, -1F, 12, 3, 2);
		GearAxisHorz.setRotationPoint(0F, 16F, 0F);
		GearAxisHorz.setTextureSize(64, 32);
		GearAxisHorz.mirror = true;
		setRotation(GearAxisHorz, 3.1415926F, 0.4363323F, 0F);
		GearCrossHorz = new ModelRenderer(this, 0, 17);
		GearCrossHorz.addBox(-1F, 5F, -6F, 2, 3, 12);
		GearCrossHorz.setRotationPoint(0F, 16F, 0F);
		GearCrossHorz.setTextureSize(64, 32);
		GearCrossHorz.mirror = true;
		setRotation(GearCrossHorz, 3.1415926F, 0.4363323F, 0F);
		
		GearCenterVert2 = new ModelRenderer(this, 28, 16);
		GearCenterVert2.addBox(-4F, 5F, -4F, 8, 3, 8);
		GearCenterVert2.setRotationPoint(0F, 16F, 0F);
		GearCenterVert2.setTextureSize(64, 32);
		GearCenterVert2.mirror = true;
		setRotation(GearCenterVert2, 1.570796F, 0, 0);
		GearCrossVert2 = new ModelRenderer(this, 0, 17);
		GearCrossVert2.addBox(-1F, 5F, -6F, 2, 3, 12);
		GearCrossVert2.setRotationPoint(0F, 16F, 0F);
		GearCrossVert2.setTextureSize(64, 32);
		GearCrossVert2.mirror = true;
		setRotation(GearCrossVert2, 1.570796F, 0, 0);
		GearAxisVert2 = new ModelRenderer(this, 28, 27);
		GearAxisVert2.addBox(-6F, 5F, -1F, 12, 3, 2);
		GearAxisVert2.setRotationPoint(0F, 16F, 0F);
		GearAxisVert2.setTextureSize(64, 32);
		GearAxisVert2.mirror = true;
		setRotation(GearAxisVert2, 1.570796F, 0, 0);
		
		GearCenterVert3 = new ModelRenderer(this, 28, 16);
		GearCenterVert3.addBox(-4F, 5F, -4F, 8, 3, 8);
		GearCenterVert3.setRotationPoint(0F, 16F, 0F);
		GearCenterVert3.setTextureSize(64, 32);
		GearCenterVert3.mirror = true;
		setRotation(GearCenterVert3, 0F, 0F, -1.570796F);
		GearCrossVert3 = new ModelRenderer(this, 0, 17);
		GearCrossVert3.addBox(-1F, 5F, -6F, 2, 3, 12);
		GearCrossVert3.setRotationPoint(0F, 16F, 0F);
		GearCrossVert3.setTextureSize(64, 32);
		GearCrossVert3.mirror = true;
		setRotation(GearCrossVert3, 0F, 0F, -1.570796F);
		GearAxisVert3 = new ModelRenderer(this, 28, 27);
		GearAxisVert3.addBox(-6F, 5F, -1F, 12, 3, 2);
		GearAxisVert3.setRotationPoint(0F, 16F, 0F);
		GearAxisVert3.setTextureSize(64, 32);
		GearAxisVert3.mirror = true;
		setRotation(GearAxisVert3, 0F, 0, -1.570796F);
		
		GearCenterVert4 = new ModelRenderer(this, 28, 16);
		GearCenterVert4.addBox(-4F, 5F, -4F, 8, 3, 8);
		GearCenterVert4.setRotationPoint(0F, 16F, 0F);
		GearCenterVert4.setTextureSize(64, 32);
		GearCenterVert4.mirror = true;
		setRotation(GearCenterVert4, -1.570796F, 0, 0);
		GearCrossVert4 = new ModelRenderer(this, 0, 17);
		GearCrossVert4.addBox(-1F, 5F, -6F, 2, 3, 12);
		GearCrossVert4.setRotationPoint(0F, 16F, 0F);
		GearCrossVert4.setTextureSize(64, 32);
		GearCrossVert4.mirror = true;
		setRotation(GearCrossVert4, -1.570796F, 0, 0);
		GearAxisVert4 = new ModelRenderer(this, 28, 27);
		GearAxisVert4.addBox(-6F, 5F, -1F, 12, 3, 2);
		GearAxisVert4.setRotationPoint(0F, 16F, 0F);
		GearAxisVert4.setTextureSize(64, 32);
		GearAxisVert4.mirror = true;
		setRotation(GearAxisVert4, -1.570796F, 0, 0);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		Corner1.render(f5);
		Corner2.render(f5);
		Corner3.render(f5);
		Corner4.render(f5);
		Bottom1.render(f5);
		Bottom2.render(f5);
		Bottom3.render(f5);
		Bottom4.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Top4.render(f5);
		GearCenterVert1.render(f5);
		GearCrossVert1.render(f5);
		GearAxisVert1.render(f5);
		/*GearCenterVert2.render(f5);
		GearCrossVert2.render(f5);
		GearAxisVert2.render(f5);
		GearCenterVert3.render(f5);
		GearCrossVert3.render(f5);
		GearAxisVert3.render(f5);
		GearCenterVert4.render(f5);
		GearCrossVert4.render(f5);
		GearAxisVert4.render(f5);*/
		GearCenterHorz.render(f5);
		GearAxisHorz.render(f5);
		GearCrossHorz.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
		//setRotation(Rib, f, f1, f2);
		//setRotation(Sail, f, f1, f2);
		//System.out.println(f1);
		/*setRotation(GearCenterVert, 0, f1*2, 1.570796F);
		setRotation(GearCenterVert, 0F, 0, 1.570796F);
		setRotation(GearCrossVert, 0F, 0, 1.570796F);
		setRotation(GearAxisVert, 0F, 0, 1.570796F);
		setRotation(GearCenterHorz, 3.1415926F, 0.4363323F, 0F);
		setRotation(GearAxisHorz, 3.1415926F, 0.4363323F, 0F);
		setRotation(GearCrossHorz, 3.1415926F, 0.4363323F, 0F);*/
		if(f1 == -1) {
			setRotation(GearCenterVert1,  -1.570796F, 0, 0);
			setRotation(GearAxisVert1,  -1.570796F, 0, 0);
			setRotation(GearCrossVert1,  -1.570796F, 0, 0);
		}
		else if(f1 == 0) {
			setRotation(GearCenterVert1, 1.570796F, 0, 0);
			setRotation(GearAxisVert1, 1.570796F, 0, 0);
			setRotation(GearCrossVert1, 1.570796F, 0, 0);
		}
		else if(f1 == 1) {
			setRotation(GearCenterVert1, 0, 0,  1.570796F);
			setRotation(GearAxisVert1, 0, 0,  1.570796F);
			setRotation(GearCrossVert1, 0, 0,  1.570796F);
		}
		else {
			setRotation(GearCenterVert1, 0, 0,  -1.570796F);
			setRotation(GearAxisVert1, 0, 0,  -1.570796F);
			setRotation(GearCrossVert1, 0, 0,  -1.570796F);
		}
	}

	public void render(TileEntity tileentity, double x, double y, double z) {
		TileEntityWindmill es = (TileEntityWindmill)tileentity;
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float)x + es.getOffsetX(), (float)y + es.getOffsetY(), (float)z + es.getOffsetZ());
		
		//GL11.glScalef(0.5f, 0.5f, 0.5f);
		//FMLClientHandler.instance().getClient().renderEngine.bindTexture(es.getModelTexture());
		ResourceLocation rl = new ResourceLocation(es.getModelTexture());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
		this.render(null, 0, es.getRotationY(), 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}

}
