package com.draco18s.wildlife.entity.ai;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.draco18s.wildlife.entity.EntityLizard;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeHooks;

public class EntityAIGetAttackableTarget extends EntityAITarget {
	public static Method dropItems;
	public int hungerLevel = 6000;
	public boolean hungerTrigger = false;
    private final Class targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    private final EntityAIGetAttackableTarget.Sorter theNearestAttackableTargetSorter;
    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    private final IEntitySelector targetEntitySelector;
    private EntityLivingBase targetEntity;

    public EntityAIGetAttackableTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1663_4_) {
        this(creature, targetClass, chance, p_i1663_4_, false);
    }

    public EntityAIGetAttackableTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1664_4_, boolean p_i1664_5_) {
        this(creature, targetClass, chance, p_i1664_4_, p_i1664_5_, (IEntitySelector)null);
    }

	public EntityAIGetAttackableTarget(EntityCreature creature, Class targetClass, int chance, boolean p_i1665_4_, boolean p_i1665_5_, final IEntitySelector selector) {
		super(creature, p_i1665_4_, p_i1665_5_);
		this.targetClass = targetClass;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new EntityAIGetAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new IEntitySelector() {
            public boolean isEntityApplicable(Entity ent) {
            	return !(ent instanceof EntityLivingBase) ? false : (selector != null && !selector.isEntityApplicable(ent) ? false : EntityAIGetAttackableTarget.this.isSuitableTarget((EntityLivingBase)ent, false));
            	//System.out.println(ent + " is valid? " + r);
            	//System.out.println("  -" + (ent instanceof EntityLivingBase));
            	//System.out.println("  -" + EntityAIGetAttackableTarget.this.isSuitableTarget((EntityLivingBase)ent, false));
                //return r;
            }
        };
	}

	@Override
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		}
		else {
			double d0 = this.getTargetDistance();

			List list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty()) {
				//System.out.println("No animals");
				return false;
			}
			else {
				List<EntityItem> list2 = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityItem.class, this.taskOwner.boundingBox.expand(5, 2, 5));
				ItemStack s;
				for(int i = list2.size()-1; i >=0; i--) {
					s = list2.get(i).getEntityItem();
					if(s.getItem() instanceof ItemFood) {
						if(s.getUnlocalizedName().toLowerCase().indexOf("raw") >= 0) {
							//System.out.println("Raw food nearby");
							return false;
						}
						else if(s.getUnlocalizedName().toLowerCase().indexOf("cooked") >= 0) {
							//System.out.println("Cooked food nearby");
							return false;
						}
						else if(s.getUnlocalizedName().toLowerCase().indexOf("flesh") >= 0) {
							//System.out.println("Spider eye nearby");
							return false;
						}
					}
				}
				
				this.targetEntity = (EntityLivingBase)list.get(0);
				if(hungerLevel < 1000) {
					hungerTrigger = true;
				}
				if(hungerLevel > 24000) {
					hungerTrigger = false;
				}
				return hungerTrigger;
			}
		}
	}

	@Override
	public void startExecuting() {
		if(this.taskOwner.getAttackTarget() == null)
			this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	@Override
	protected boolean isSuitableTarget(EntityLivingBase ent, boolean p_75296_2_) {
		try {
			return EntityAIGetAttackableTarget.Sorter.meatValue(ent) > 0;
		} catch (IllegalAccessException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
	}

	public static class Sorter implements Comparator {
		public static double meatValue(EntityLivingBase e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			double d0 = 0;
			int count = 0;
			e.captureDrops = true;
			e.capturedDrops.clear();
			dropItems.invoke(e, true, 0);
			if (!ForgeHooks.onLivingDrops(e, DamageSource.generic, e.capturedDrops, 0, true, 100)) {
                for (EntityItem itement : e.capturedDrops) {
                	Item item = itement.getEntityItem().getItem();
                	if(item instanceof ItemFood) {
                		d0 += ((ItemFood)item).func_150905_g(itement.getEntityItem());
                		count++;
                	}
                }
            }
			e.captureDrops = false;
			e.capturedDrops.clear();
			return count != 0?d0/count:0;
		}
		private final EntityLivingBase theEntity;

		public Sorter(EntityLivingBase ent) {
			this.theEntity = ent;
		}

		public int compare(EntityLivingBase comp1, EntityLivingBase comp2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			double d0 = meatValue(comp1);
			double d1 = meatValue(comp2);

			double d2 = this.theEntity.getDistanceSqToEntity(comp1);
			double d3 = this.theEntity.getDistanceSqToEntity(comp2);
			
			return d0 < d1 ? 1 : (d0 > d1 ? -1 : (d2 < d3 ? -1 : (d2 > d3 ? 1 : 0)));
		}

		public int compare(Object p_compare_1_, Object p_compare_2_) {
			int i = 0;
			try {
				 i = this.compare((EntityLivingBase)p_compare_1_, (EntityLivingBase)p_compare_2_);
			}
			catch(Exception e) {
				
			}
			return i;
		}
	}
}
