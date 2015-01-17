package com.draco18s.wildlife.entity.ai;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;

public class EntityAgeTracker {
	public int entityAge;
	public int deathAge = -1000;
	
	public void writeEntityToNBT(NBTTagCompound nbt, EntityAnimal animal) {
		nbt.setInteger("AnimalAge_"+animal.hashCode(), entityAge);
		nbt.setInteger("AnimalDeathAge_"+animal.hashCode(), deathAge);
    }

    public void readEntityFromNBT(NBTTagCompound nbt, EntityAnimal animal) {
    	entityAge = nbt.getInteger("AnimalAge_"+animal.hashCode());
    	deathAge = nbt.getInteger("AnimalDeathAge_"+animal.hashCode());
    }
}
