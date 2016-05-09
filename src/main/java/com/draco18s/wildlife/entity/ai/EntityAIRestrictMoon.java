package com.draco18s.wildlife.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRestrictMoon extends EntityAIBase {

	private EntityCreature theEntity;

    public EntityAIRestrictMoon(EntityCreature p_i1652_1_)
    {
        this.theEntity = p_i1652_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return !this.theEntity.worldObj.isDaytime();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theEntity.getNavigator().setAvoidSun(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theEntity.getNavigator().setAvoidSun(false);
    }

}
