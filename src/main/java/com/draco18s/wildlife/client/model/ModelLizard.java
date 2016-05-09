package com.draco18s.wildlife.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelLizard extends ModelQuadruped {
	//ModelRenderer hindRightLeg;
	ModelRenderer tail;
	ModelRenderer skull;
	ModelRenderer neck;
	ModelRenderer tailLower;
	ModelRenderer foreLeftFoot;
	ModelRenderer hindLeftFoot;
	ModelRenderer hindRightFoot;
	ModelRenderer foreRightFoot;
	ModelRenderer jaw;

	public ModelLizard() {
		super(12, 0.0F);
		
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this, 0, 0);
		body.mirror = false;
		body.addBox(-4.0F, 0F, -8.0F, 8, 3, 16);
		body.setRotationPoint(0.0F, -6.0F, 0.0F);
		//body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body.setTextureSize(64, 32);

		tail = new ModelRenderer(this, 14, 20);
		tail.mirror = false;
		tail.addBox(-2.0F, -1.5F, -6.0F, 4, 3, 6);
		tail.setRotationPoint(0.0F, 1.5F, -8.0F);
		//tail.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tail.setTextureSize(64, 32);
		body.addChild(tail);

		tailLower = new ModelRenderer(this, 0, 19);
		tailLower.mirror = false;
		tailLower.addBox(-1.0F, -1.0F, -10.0F, 2, 2, 10);
		tailLower.setRotationPoint(0.0F, 0.0F, -6.0F);
		//tailLower.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tailLower.setTextureSize(64, 32);
		tail.addChild(tailLower);
		
		leg3 = new ModelRenderer(this, 32, 0);
		leg3.mirror = false;
		leg3.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3);
		leg3.setRotationPoint(-4.0F, 1.5F, -5.0F);
		//hindRightLeg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leg3.setTextureSize(64, 32);
		body.addChild(leg3);

		leg2 = new ModelRenderer(this, 32, 6);
		leg2.mirror = true;
		leg2.addBox(0.0F, -1.5F, -1.5F, 3, 3, 3);
		leg2.setRotationPoint(4.0F, 1.5F, 6.0F);
		//leg2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leg2.setTextureSize(64, 32);
		body.addChild(leg2);

		leg4 = new ModelRenderer(this, 32, 0);
		leg4.mirror = true;
		leg4.addBox(0.0F, -1.5F, -1.5F, 3, 3, 3);
		leg4.setRotationPoint(4.0F, 1.5F, -5.0F);
		//leg4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leg4.setTextureSize(64, 32);
		body.addChild(leg4);

		leg1 = new ModelRenderer(this, 32, 6);
		leg1.mirror = false;
		leg1.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3);
		leg1.setRotationPoint(-4.0F, 1.5F, 6.0F);
		//leg1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leg1.setTextureSize(64, 32);
		body.addChild(leg1);/**/
		
		neck = new ModelRenderer(this, 44, 8);
		neck.mirror = false;
		neck.addBox(-2.0F, -2.0F, 0.0F, 4, 3, 5);
		neck.setRotationPoint(0.0F, 1.5F, 8.0F);
		neck.rotateAngleX = 0.35f;
		//neck.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.17364818F, 0.0F, 0.0F, 0.9848077F)).transpose());
		neck.setTextureSize(64, 32);
		body.addChild(neck);
		
		skull = new ModelRenderer(this, 0, 0);
		skull.mirror = false;
		skull.setRotationPoint(0, 0, 0);
		skull.rotateAngleX = -0.35f;
		//neck.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.17364818F, 0.0F, 0.0F, 0.9848077F)).transpose());
		skull.setTextureSize(64, 32);
		neck.addChild(skull);
		
		head = new ModelRenderer(this, 34, 19);
		head.mirror = false;
		head.addBox(-2.0F, -4.55F, -1.0F, 4, 2, 6);
		head.setRotationPoint(0.0F, 1F, 4F);
		head.rotateAngleX = 0f;
		//head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.17364818F, 0.0F, 0.0F, 0.9848077F)).transpose());
		head.setTextureSize(64, 32);
		skull.addChild(head);
		//body.addChild(head);

		jaw = new ModelRenderer(this, 44, 0);
		jaw.mirror = false;
		jaw.addBox(-2.0F, -2.5F, -1.5F, 4, 1, 6);
		jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		jaw.rotateAngleX = -0.2f;
		//jaw.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.09584575F, 0.0F, 0.0F, 0.9953962F)).transpose());
		jaw.setTextureSize(64, 32);
		head.addChild(jaw);

		hindRightFoot = new ModelRenderer(this, 0, 0);
		hindRightFoot.mirror = false;
		hindRightFoot.addBox(-1.5F, 2.0F, -1.5F, 3, 3, 1);
		hindRightFoot.setRotationPoint(-1.5F, -1.0F, 1.0F);
		hindRightFoot.rotateAngleX = 0.5f;
		//leg3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.30900523F, 0.008299429F, 0.0026966478F, 0.9510203F)).transpose());
		hindRightFoot.setTextureSize(64, 32);
		leg3.addChild(hindRightFoot);

		hindLeftFoot = new ModelRenderer(this, 0, 0);
		hindLeftFoot.mirror = true;
		hindLeftFoot.addBox(-1.5F, 2.0F, -1.5F, 3, 3, 1);
		hindLeftFoot.setRotationPoint(1.5F, -1.0F, 1.0F);
		hindLeftFoot.rotateAngleX = 0.5f;
		//hindLeftFoot.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.30900523F, 0.008299429F, 0.0026966478F, 0.9510203F)).transpose());
		hindLeftFoot.setTextureSize(64, 32);
		leg4.addChild(hindLeftFoot);
		
		foreLeftFoot = new ModelRenderer(this, 0, 0);
		foreLeftFoot.mirror = true;
		foreLeftFoot.addBox(-1.5F, 2.0F, -1.5F, 3, 3, 1);
		foreLeftFoot.setRotationPoint(1.5F, -1.0F, 1.0F);
		foreLeftFoot.rotateAngleX = 0.5f;
		//foreLeftFoot.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.30900523F, 0.008299429F, 0.0026966478F, 0.9510203F)).transpose());
		foreLeftFoot.setTextureSize(64, 32);
		leg2.addChild(foreLeftFoot);

		foreRightFoot = new ModelRenderer(this, 0, 0);
		foreRightFoot.mirror = false;
		foreRightFoot.addBox(-1.5F, 2.0F, -1.5F, 3, 3, 1);
		foreRightFoot.setRotationPoint(-1.5F, -1.0F, 1.0F);
		foreRightFoot.rotateAngleX = 0.5f;
		//foreRightFoot.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(-0.30900523F, 0.008299429F, 0.0026966478F, 0.9510203F)).transpose());
		foreRightFoot.setTextureSize(64, 32);
		leg1.addChild(foreRightFoot);
	}
	
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.5F, 0.0F);
        this.body.render(p_78088_7_);
        GL11.glPopMatrix();
    }
	
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        float f6 = (180F / (float)Math.PI);
        this.head.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
        this.head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.body.rotateAngleY = ((float)Math.PI);
        this.leg1.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.leg2.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.leg3.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.leg4.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        
        this.tail.rotateAngleY = this.leg1.rotateAngleX*0.5f;
        this.tailLower.rotateAngleY = this.leg2.rotateAngleX;
    }
}
