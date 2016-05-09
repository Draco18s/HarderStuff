package com.draco18s.wildlife.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;

@SideOnly(Side.CLIENT)
public class ModelGoat2 extends ModelQuadruped
{
    private float field_78153_i;
    private ModelRenderer horn1;
    private ModelRenderer horn2;
    private ModelRenderer bhorn1;
    private ModelRenderer bhorn2;
    private ModelRenderer beard;

    /*This is the base portion*/
    public ModelGoat2()
    {
        super(12, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
        //I do not understand this
        horn1 = new ModelRenderer(this, 20, 0);
        horn1.addBox(1,-4.25f,1,1,1,4,0);
        horn1.rotateAngleX = 0.4f;
        head.addChild(horn1);
        horn2 = new ModelRenderer(this, 20, 0);
        horn2.addBox(-2,-4.25f,1,1,1,4,0);
        horn2.rotateAngleX = 0.4f;
        head.addChild(horn2);

        bhorn1 = new ModelRenderer(this, 23, 3);
        bhorn1.addBox(1,-4.25f,1,1,1,1,0);
        bhorn1.rotateAngleX = 0.4f;
        head.addChild(bhorn1);
        bhorn2 = new ModelRenderer(this, 23, 3);
        bhorn2.addBox(-2,-4.25f,1,1,1,1,0);
        bhorn2.rotateAngleX = 0.4f;
        head.addChild(bhorn2);

        beard = new ModelRenderer(this, 12, 14);
        beard.addBox(-0.5f,2,-5.85f,1,3,2,0);
        head.addChild(beard);
        
        //TODO:
        //goat beard
        //tail?
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-4.0F, -8.0F, -7.0F, 8, 16, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 3, 12, 3, 0.0F);
        this.leg1.setRotationPoint(-2.0F, (float)(24 - 12), 9.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 3, 12, 3, 0.0F);
        this.leg2.setRotationPoint(3.0F, (float)(24 - 12), 9.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 3, 12, 3, 0.0F);
        this.leg3.setRotationPoint(-2.0F, (float)(24 - 12), -4.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 3, 12, 3, 0.0F);
        this.leg4.setRotationPoint(3.0F, (float)(24 - 12), -4.0F);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        this.head.rotationPointY = 6.0F + ((EntitySheep)p_78086_1_).func_70894_j(p_78086_4_) * 9.0F;
        this.field_78153_i = ((EntitySheep)p_78086_1_).func_70890_k(p_78086_4_);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleX = this.field_78153_i;
    }
    
    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.isChild) {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, this.field_78145_g * p_78088_7_, this.field_78151_h * p_78088_7_);
            this.beard.showModel = false;
            this.horn1.showModel = false;
            this.horn2.showModel = false;
            this.bhorn1.showModel = true;
            this.bhorn2.showModel = true;
            this.head.render(p_78088_7_);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.body.render(p_78088_7_);
            this.leg1.render(p_78088_7_);
            this.leg2.render(p_78088_7_);
            this.leg3.render(p_78088_7_);
            this.leg4.render(p_78088_7_);
            GL11.glPopMatrix();
        }
        else {
        	this.beard.showModel = true;
            this.horn1.showModel = true;
            this.horn2.showModel = true;
            this.bhorn1.showModel = false;
            this.bhorn2.showModel = false;
            this.head.render(p_78088_7_);
            this.body.render(p_78088_7_);
            this.leg1.render(p_78088_7_);
            this.leg2.render(p_78088_7_);
            this.leg3.render(p_78088_7_);
            this.leg4.render(p_78088_7_);
        }
    }
}