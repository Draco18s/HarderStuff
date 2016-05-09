package com.draco18s.wildlife.client.model;

import org.lwjgl.opengl.GL11;

import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.entity.TileEntityTanner;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ModelRack extends ModelBase {
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg4;
	ModelRenderer topBar;
	ModelRenderer leather1;
	ModelRenderer leather2;
	ModelRenderer leather3;
	ModelRenderer leather4;

	public ModelRack() {
		textureWidth = 64;
		textureHeight = 32;

		topBar = new ModelRenderer(this, 0, 0);
		topBar.addBox(-8F, 0F, -0.5F, 16, 1, 1);
		topBar.setRotationPoint(0F, 23F, 0F);
		topBar.setTextureSize(64, 32);
		topBar.mirror = true;
		setRotation(topBar, 0F, 0F, 0F);
		leg1 = new ModelRenderer(this, 0, 2);
		leg1.addBox(0F, -17F, 0F, 1, 17, 1);
		leg1.setRotationPoint(7F, 24F, 0F);
		leg1.setTextureSize(64, 32);
		leg1.mirror = true;
		setRotation(leg1, -0.3141593F, 0F, 0F);
		leg4 = new ModelRenderer(this, 4, 2);
		leg4.addBox(0F, -17F, 0F, 1, 17, 1);
		leg4.setRotationPoint(-8F, 24F, 0F);
		leg4.setTextureSize(64, 32);
		leg4.mirror = true;
		setRotation(leg4, -0.3141593F, 0F, 0F);
		leg2 = new ModelRenderer(this, 8, 2);
		leg2.addBox(0F, -17F, -1F, 1, 17, 1);
		leg2.setRotationPoint(7F, 24F, 0F);
		leg2.setTextureSize(64, 32);
		leg2.mirror = true;
		setRotation(leg2, 0.3141593F, 0F, 0F);
		leg3 = new ModelRenderer(this, 12, 2);
		leg3.addBox(0F, -17F, -1F, 1, 17, 1);
		leg3.setRotationPoint(-8F, 24F, 0F);
		leg3.setTextureSize(64, 32);
		leg3.mirror = true;
		setRotation(leg3, 0.3141593F, 0F, 0F);

		leather1 = new ModelRenderer(this, 32, 0);
		leather1.addBox(0F, -17F, 0F, 16, 16, 0);
		leather1.setRotationPoint(-8F, 24F, 0F);
		leather1.setTextureSize(64, 32);
		leather1.mirror = true;
		setRotation(leather1, 0.3141593F, 0F, 0F);
		leather2 = new ModelRenderer(this, 32, 0);
		leather2.addBox(0F, -17F, 0F, 16, 16, 0);
		leather2.setRotationPoint(-8F, 24F, 0F);
		leather2.setTextureSize(64, 32);
		leather2.mirror = true;
		setRotation(leather2, -0.3141593F, 0F, 0F);

		leather3 = new ModelRenderer(this, 32, 16);
		leather3.addBox(0F, -17F, 0F, 16, 16, 0);
		leather3.setRotationPoint(-8F, 24F, 0F);
		leather3.setTextureSize(64, 32);
		leather3.mirror = true;
		setRotation(leather3, 0.3141593F, 0F, 0F);
		leather4 = new ModelRenderer(this, 32, 16);
		leather4.addBox(0F, -17F, 0F, 16, 16, 0);
		leather4.setRotationPoint(-8F, 24F, 0F);
		leather4.setTextureSize(64, 32);
		leather4.mirror = true;
		setRotation(leather4, -0.3141593F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		topBar.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	public void render(TileEntity tileentity, double x, double y, double z) {
		TileEntityTanner es = (TileEntityTanner)tileentity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5f, (float)y - 0.5f, (float)z + 0.5f);

		//GL11.glPushMatrix();
		if(tileentity.getWorldObj() != null && tileentity.getBlockMetadata() != 0) {
			GL11.glRotatef(90, 0, 1, 0);
		}
		ResourceLocation rl = new ResourceLocation(es.getModelTexture());
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
		this.render(null, (float)Math.PI, 0, 0, 0, 0, 0.0625F);
		ItemStack stack = es.getStackInSlot(0);
		if(stack != null) {
			if(stack.getItem() == Items.leather) {
				leather3.setTextureOffset(32, 16);
				leather3.render(0.0625F);
			}
			if(stack.getItem() == WildlifeBase.itemRawLeather) {
				leather1.setTextureOffset(32, 0);
				leather1.render(0.0625F);
			}
		}
		stack = es.getStackInSlot(1);
		if(stack != null) {
			if(stack.getItem() == Items.leather) {
				leather4.setTextureOffset(32, 16);
				leather4.render(0.0625F);
			}
			if(stack.getItem() == WildlifeBase.itemRawLeather) {
				leather2.setTextureOffset(32, 0);
				leather2.render(0.0625F);
			}
		}
		//GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
