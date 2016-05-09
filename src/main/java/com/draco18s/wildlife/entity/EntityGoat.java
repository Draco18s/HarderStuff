package com.draco18s.wildlife.entity;

import com.draco18s.wildlife.WildlifeBase;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class EntityGoat extends EntitySheep {
	public static final float[][] fleeceColorTable = new float[][] {
		{1.0F, 1.0F, 1.0F}, //white
		{0.85F, 0.5F, 0.2F}, 
		{0.7F, 0.3F, 0.85F}, //purple
		{0.4F, 0.6F, 0.85F}, 
		{0.9F, 0.9F, 0.2F}, //yellow
		{0.5F, 0.8F, 0.1F}, 
		{0.95F, 0.5F, 0.65F}, 
		{0.3F, 0.3F, 0.3F}, //dark grey
		{0.6F, 0.6F, 0.6F}, //gray
		{0.3F, 0.5F, 0.6F}, 
		{0.5F, 0.25F, 0.7F}, 
		{0.2F, 0.3F, 0.7F}, 
		{0.4F, 0.3F, 0.2F}, 
		{0.4F, 0.5F, 0.2F}, 
		{0.6F, 0.2F, 0.2F}, //red?
		{0.1F, 0.1F, 0.1F}}; //black
	
	public EntityGoat(World p_i1691_1_) {
		super(p_i1691_1_);
	}

	@Override
	public EntitySheep createChild(EntityAgeable p_90011_1_) {
		EntitySheep p = super.createChild(p_90011_1_);
		//EntityGoat entitysheep = (EntityGoat)p_90011_1_;
		EntityGoat entitysheep1 = new EntityGoat(this.worldObj);
        entitysheep1.setFleeceColor(15 - p.getFleeceColor());
        return entitysheep1;
    }

	@Override
    protected Item getDropItem() {
        return this.isBurning() ? WildlifeBase.cookedChevon : WildlifeBase.rawChevon;
    }

	@Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (int k = 0; k < j; ++k) {
            if (this.isBurning()) {
                this.dropItem(WildlifeBase.cookedChevon, 1);
            }
            else {
                this.dropItem(WildlifeBase.rawChevon, 1);
            }
        }
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor()), 0.0F);
        }
    }
}
