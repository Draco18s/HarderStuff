package com.draco18s.wildlife.entity.ai;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;

public class EntityAgeTracker {
	public int entityAge;
	public int deathAge = -1000;
	public int ageFactor = 2;
	
	public void writeEntityToNBT(NBTTagCompound nbt, EntityAnimal animal) {
		//nbt.setLong("AnimalAge"+animal.hashCode(), entityAge);
		//nbt.setLong("AnimalDeathAge"+animal.hashCode(), deathAge);
    }

    public void readEntityFromNBT(NBTTagCompound nbt, EntityAnimal animal) {
    	//entityAge = nbt.getLong("AnimalAge"+animal.hashCode());
    	//deathAge = nbt.getLong("AnimalDeathAge"+animal.hashCode());
    }
}
