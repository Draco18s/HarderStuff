package com.draco18s.wildlife.entity.ai;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeHooks;

public class EntityAIGetFoodItemTarget extends EntityAITarget {
	public static Method dropItems;
	
    private final Class targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    private final EntityAIGetFoodItemTarget.Sorter theNearestAttackableTargetSorter;
    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    private final IEntitySelector targetEntitySelector;
    private EntityItem targetEntity;

    public EntityAIGetFoodItemTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1663_4_) {
        this(creature, targetClass, chance, p_i1663_4_, false);
    }

    public EntityAIGetFoodItemTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1664_4_, boolean p_i1664_5_) {
        this(creature, targetClass, chance, p_i1664_4_, p_i1664_5_, (IEntitySelector)null);
    }

	public EntityAIGetFoodItemTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1665_4_, boolean p_i1665_5_, final IEntitySelector selector) {
		super(creature, p_i1665_4_, p_i1665_5_);
		this.targetClass = targetClass;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new EntityAIGetFoodItemTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new IEntitySelector() {
            public boolean isEntityApplicable(Entity ent) {
            	boolean r = !(ent instanceof EntityItem) ? false : (selector != null && !selector.isEntityApplicable(ent) ? false : EntityAIGetFoodItemTarget.this.isSuitableTarget((EntityItem)ent, false));
            	//System.out.println("isEntityApplicable (" + ent + "): " + r);
                return r;
            }
        };
	}

	protected boolean isSuitableTarget(EntityItem ent, boolean p_75296_2_) {
		return EntityAIGetFoodItemTarget.Sorter.itemValue(ent) <= 1;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		}
		else {
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(4, 2, 4), this.targetEntitySelector);

			//System.out.println("List a: " + list.size());
			//Collections.sort(list, this.theNearestAttackableTargetSorter);
			//System.out.println("List b: " + list.size());
			/*for(Object o : list) {
				if(o instanceof EntityItem) {
					EntityItem ent = (EntityItem)o;
					if(EntityAIGetFoodItemTarget.Sorter.itemValue(ent) > 1) {
						list.remove(o);
					}
				}
			}*/
			//System.out.println("List empty: " + list.isEmpty());
			if (list.isEmpty()) {
				return false;
			}
			else {
				//this.targetEntity = (EntityItem)list.get(0);
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		//if(this.taskOwner.getAttackTarget() == null) {
		//	this.taskOwner.setTarget(this.targetEntity);
		//	System.out.println("set target to " + targetEntity + "," + targetEntity.isEntityAlive());
			//(new Exception()).printStackTrace();
		//}
		super.startExecuting();
	}
	
    public boolean isInterruptible()
    {
        return false;
    }

	public static class Sorter implements Comparator {
		public static double itemValue(EntityItem e) {
			double d0 = 10;
			if(e.getEntityItem().getUnlocalizedName().indexOf("raw") >= 0) {
				d0 = 0.1;
			}
			else if(e.getEntityItem().getUnlocalizedName().indexOf("cooked") >= 0) {
				d0 = 0.3;
			}
			else if(e.getEntityItem().getUnlocalizedName().indexOf("flesh") >= 0) {
				d0 = 0.12;
			}
			return d0;
		}
		
		private final EntityLivingBase theEntity;

		public Sorter(EntityLivingBase ent) {
			this.theEntity = ent;
		}

		public int compare(EntityItem comp1, EntityItem comp2) {
			double d0 = itemValue(comp1);
			double d1 = itemValue(comp2);
			
			d0 *= this.theEntity.getDistanceSqToEntity(comp1);
			d1 *= this.theEntity.getDistanceSqToEntity(comp2);
			return d0 < d1 ? 1 : (d0 > d1 ? -1 : 0);
		}

		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare((EntityItem)p_compare_1_, (EntityItem)p_compare_2_);
		}
	}
}
