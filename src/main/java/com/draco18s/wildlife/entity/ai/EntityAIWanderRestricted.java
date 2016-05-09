package com.draco18s.wildlife.entity.ai;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class EntityAIWanderRestricted extends EntityAIBase
{
    private EntityCreature entity;
    private Class targetClass;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private static final String __OBFID = "CL_00001608";

    public EntityAIWanderRestricted(EntityCreature p_i1648_1_, double p_i1648_2_)
    {
        this.entity = p_i1648_1_;
        this.speed = p_i1648_2_;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.entity.getAge() >= 100)
        {
            return false;
        }
        else if (this.entity.getRNG().nextInt(120) != 0)
        {
            return false;
        }
        else
        {
        	List<EntityItem> list2 = this.entity.worldObj.getEntitiesWithinAABB(EntityItem.class, this.entity.boundingBox.expand(5, 2, 5));
			ItemStack s;
			for(int i = list2.size()-1; i >=0; i--) {
				s = list2.get(i).getEntityItem();
				if(s.getItem() instanceof ItemFood) {
					if(s.getUnlocalizedName().toLowerCase().indexOf("raw") >= 0) {
						return false;
					}
					else if(s.getUnlocalizedName().toLowerCase().indexOf("cooked") >= 0) {
						return false;
					}
					else if(s.getUnlocalizedName().toLowerCase().indexOf("flesh") >= 0) {
						return false;
					}
				}
			}
			
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
            	this.xPosition = vec3.xCoord;
                this.yPosition = vec3.yCoord;
                this.zPosition = vec3.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}