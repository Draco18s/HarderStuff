package com.draco18s.wildlife.entity.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIAging extends EntityAIBase {
    private EntityAnimal entity;
	private Random rand;
	private Class species;
	private EntityAgeTracker age;

	public EntityAIAging(Random random, EntityAnimal ent, Class spec, EntityAgeTracker ageTracker) {
		rand = random;
		entity = ent;
		species = spec;
		age = ageTracker;
	}
	@Override
	public boolean shouldExecute() {
		return true;
	}

	public void updateTask() {
		EntityPlayer nearby = entity.worldObj.getClosestPlayerToEntity(entity, 3);
		if(nearby != null) {
			System.out.println(age.entityAge + ">" + age.deathAge);
		}
		
		if(age.deathAge <= 0) {
			age.entityAge = entity.getEntityData().getInteger("AnimalAge");
			age.deathAge = entity.getEntityData().getInteger("AnimalDeathAge");
			if(age.deathAge <= 0) {
				age.deathAge = 480000 + rand.nextInt(60000);
			}
		}
		if(age.entityAge > age.deathAge) {
			entity.setDead();
		}
		else if(!entity.isInLove() && entity.getGrowingAge() == 0 && age.entityAge % 150 == 0) {
			List ents = entity.worldObj.getEntitiesWithinAABB(species, getAABB(entity.posX, entity.posY, entity.posZ));
			if(ents.size() <= 4 && rand.nextInt(200) == 0) {
				entity.func_146082_f(null);
			}
			else if(ents.size() <= 12 && rand.nextInt(450) == 0) {
				entity.func_146082_f(null);
			}
			else if(rand.nextInt(600) == 0) {
				entity.func_146082_f(null);
			}
			if(ents.size() > 15) {
				age.deathAge -= 9;
			}
		}
		else if(entity.isInLove()) {
			EntityPlayer player = entity.worldObj.getClosestPlayerToEntity(entity, 32);
			if(player == null && rand.nextInt(100) == 0) {
				List ents = entity.worldObj.getEntitiesWithinAABB(species, getAABB(entity.posX, entity.posY, entity.posZ));
				if(ents.size() > 0) {
					EntityAnimal mate = null;
					EntityAnimal anim;
					for(Object obj : ents) {
						if(obj != entity) {
							anim = (EntityAnimal)obj;
							if(anim != entity && mate == null && anim.isInLove()) {
								mate = anim;
							}
						}
					}
					if(mate != null) {
						procreate();
						mate.resetInLove();
					}
				}
			}
			else if(this.entity.getAttackTarget() == null) {
				if(rand.nextInt(200) == 0) {
					List ents = entity.worldObj.getEntitiesWithinAABB(species, getAABB(entity.posX, entity.posY, entity.posZ));
					if(ents.size() > 0) {
						EntityAnimal animal = (EntityAnimal) ents.get(rand.nextInt(ents.size()));
						this.entity.setTarget(animal);
					}
				}
			}
			else {
				EntityAnimal animal = (EntityAnimal) entity.getAttackTarget();
				double dx = entity.posX - animal.posX;
				double dy = entity.posY - animal.posY;
				double dz = entity.posZ - animal.posZ;
				dx *= dx;
				dy *= dy;
				dz *= dz;
				dx += dy +dz;
				if(dx < 100) {
					this.entity.setTarget(null);
				}
			}
		}
		
		if(age.entityAge % 100 == 0) {
			entity.getEntityData().setInteger("AnimalAge", age.entityAge);
			entity.getEntityData().setInteger("AnimalDeathAge", age.deathAge);
		}
	}
	
	private void procreate() {
        EntityAgeable entityageable = entity.createChild(entity);

        if (entityageable != null) {
            entity.setGrowingAge(6000);
            entity.setGrowingAge(6000);
            entity.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

            for (int i = 0; i < 7; ++i) {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                entity.worldObj.spawnParticle("heart", entity.posX + (double)(rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(rand.nextFloat() * entity.height), entity.posZ + (double)(rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, d0, d1, d2);
            }

            entity.worldObj.spawnEntityInWorld(entityageable);
        }
	}
	
	private AxisAlignedBB getAABB(double x, double y, double z) {
		return AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1).expand(32, 12, 32);
	}
}
