package com.draco18s.wildlife.entity.ai;

import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.entity.EntityGoat;
import com.draco18s.wildlife.util.AnimalUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

public class EntityAIAging extends EntityAIBase {
	private EntityAnimal entity;
	private Random rand;
	private Class species;
	private EntityAgeTracker age;
	private boolean wasGrassNearRecently;
	private int milkReabsorbed = 0;

	public EntityAIAging(Random random, EntityAnimal ent, Class spec, EntityAgeTracker ageTracker) {
		rand = random;
		entity = ent;
		species = spec;
		age = ageTracker;
		age.ageFactor = HardLibAPI.animalManager.getAgeSpeed(ent.getClass());
	}

	/*public EntityAgeTracker getAgeTracker() {
		return age;
	}*/

	@Override
	public boolean shouldExecute() {
		if(entity instanceof EntityHorse) {
			return !((EntityHorse)entity).isTame();
		}
		else if(entity instanceof IEntityOwnable) {
			return ((IEntityOwnable)entity).getOwner() == null;
		}
		return true;
	}

	public void updateTask() {
		//EntityPlayer nearby = entity.worldObj.getClosestPlayerToEntity(entity, 3);
		if(age.deathAge <= 0) {
			age.entityAge = entity.getEntityData().getInteger("AnimalAge");
			age.deathAge = entity.getEntityData().getInteger("AnimalDeathAge");
			if(age.deathAge <= 0) {
				if(HardLibAPI.animalManager.isUnaging(entity.getClass())) {
					age.entityAge = (int)(AnimalUtil.animalMaxAge * 2.5f) + 1000;
					age.deathAge = age.entityAge * 5;
				}
				else {
					if(entity.isChild()) {
						age.entityAge = 0;
					}
					else {
						age.entityAge = (48000 + rand.nextInt(120000));
					}
					age.deathAge = (AnimalUtil.animalMaxAge + rand.nextInt(60000)) / age.ageFactor;
				}
			}
		}
		/*if(nearby != null) {
			System.out.println("HasTag 'AnimalDeathAge': " + entity.getEntityData().hasKey("AnimalDeathAge"));
			System.out.println("Stored AnimalDeathAge:   " + entity.getEntityData().getInteger("AnimalDeathAge"));
			System.out.println("This animal's deathAge:  " + age.deathAge);
		}*/
		
		if(HardLibAPI.animalManager.isUnaging(entity.getClass())) {
			/*Unaging animals do not age, do not die, and do not procreate*/
			return;
		}
		
		for(int a=0;a<AnimalUtil.animalGlobalAgeRate;a++) {
			age.entityAge++;
	
			if(age.entityAge % (100) == 0) {
				entity.getEntityData().setInteger("AnimalAge", age.entityAge);
				entity.getEntityData().setInteger("AnimalDeathAge", age.deathAge);
			}
			if(age.entityAge > age.deathAge && entity.hurtResistantTime <= 0) {
				entity.attackEntityFrom(DamageSource.wither, 0.5f);
				if(age.entityAge > age.deathAge+1000*age.ageFactor) {
					entity.attackEntityFrom(DamageSource.wither, 1.5f);
				}
				if(age.entityAge > age.deathAge+10000*age.ageFactor) {
					entity.attackEntityFrom(DamageSource.wither, 15f);
				}
			}
			else if(age.entityAge % (150) == 0) {
				List ents = entity.worldObj.getEntitiesWithinAABB(species, getAABB(entity.posX, entity.posY, entity.posZ));
				String bioName = entity.worldObj.getBiomeGenForCoords((int)entity.posX, (int)entity.posZ).biomeName.toLowerCase();
				int extraCountAllowed = 0;
				if(bioName.contains("rainbow") && bioName.contains("forest")) {
					extraCountAllowed = 25;
				}
				if(!entity.isInLove() && entity.getGrowingAge() == 0) {
					if(ents.size() <= 5 && rand.nextInt(200) == 0) {
						entity.func_146082_f(null);
					}
					else if(ents.size() <= 12 && rand.nextInt(450) == 0) {
						entity.func_146082_f(null);
					}
					else if(rand.nextInt(600) == 0) {
						entity.func_146082_f(null);
					}
				}
	
				if(ents.size() > 20+extraCountAllowed) {
					if(entity.isChild()) {
						age.deathAge -= 2 * (ents.size()-20-extraCountAllowed);
					}
					else {
						age.deathAge -= 15 * (ents.size()-20-extraCountAllowed);//20 or 25 again?
					}
				}
				if(HardLibAPI.animalManager.isHerbivore(entity.getClass())) {
					boolean grassNear = false;
					Vec3 close= Vec3.createVectorHelper(0, -10, 0);
					Vec3 here= Vec3.createVectorHelper(0, -10, 0);
					Vec3 animpos= Vec3.createVectorHelper((int)entity.posX, (int)entity.posY, (int)entity.posZ);
					Block b;
					grassNear = this.checkGrass(close, here);
					wasGrassNearRecently = grassNear;
					if(!grassNear) {
						if(milkReabsorbed > 150) {
							milkReabsorbed -= 150;
						}
						else {
							age.deathAge -= AnimalUtil.noFoodAgeAmount;
						}
					}
					else {
						entity.heal(1);
						if(age.entityAge % (AnimalUtil.grassFrequency) == 0 && close.yCoord >= 0) {
							b = entity.worldObj.getBlock((int)close.xCoord, (int)close.yCoord, (int)close.zCoord);
							if(b == Blocks.leaves || b == Blocks.leaves2) {
								entity.worldObj.setBlock((int)close.xCoord, (int)close.yCoord, (int)close.zCoord, Blocks.air);
							}
							else {
								entity.worldObj.setBlock((int)close.xCoord, (int)close.yCoord, (int)close.zCoord, Blocks.dirt);
							}
						}
					}
				}
			}
	
			EntityPlayer player = entity.worldObj.getClosestPlayerToEntity(entity, 32);
			if(entity.isInLove()) {
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
			else if(entity.getAttackTarget() instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity.getAttackTarget();
				double dx = entity.posX - animal.posX;
				double dy = entity.posY - animal.posY;
				double dz = entity.posZ - animal.posZ;
				dx *= dx;
				dy *= dy;
				dz *= dz;
				dx += dy + dz;
				if(dx < 100) {
					this.entity.setTarget(null);
				}
			}
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
		return AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1).expand(48, 16, 48);
	}
	
	public void avoidStarving() {
		milkReabsorbed += AnimalUtil.milkQuanta;
	}
	
	public boolean grassNearby() {
		return wasGrassNearRecently;
	}
	
	public boolean checkGrass(Vec3 close, Vec3 here) {
		boolean grassNear = false;
		//Vec3 close= Vec3.createVectorHelper(0, -10, 0);
		//Vec3 here= Vec3.createVectorHelper(0, -10, 0);
		Vec3 animpos= Vec3.createVectorHelper((int)entity.posX, (int)entity.posY, (int)entity.posZ);
		int d = (entity instanceof EntityGoat?9:3);
		int e = (entity instanceof EntityGoat?4:2);
		Block b;
		for(int ox=(int)entity.posX-d;ox<=(int)entity.posX+d;ox++) {
			for(int oz=(int)entity.posZ-d;oz<=(int)entity.posZ+d;oz++) {
				for(int oy=(int)entity.posY-e;oy<=(int)entity.posY+e;oy++) {
					b = entity.worldObj.getBlock(ox, oy, oz);
					if(b == Blocks.grass) {
						here.xCoord = ox;
						here.yCoord = oy;
						here.zCoord = oz;
						
						grassNear = true;
						if(here.distanceTo(animpos) <= close.distanceTo(animpos)) {
							close.xCoord = ox;
							close.yCoord = oy;
							close.zCoord = oz;
						}
					}
					if(b == Blocks.leaves || b == Blocks.leaves2) {
						here.xCoord = ox;
						here.yCoord = oy;
						here.zCoord = oz;
						grassNear = true;
						if(here.distanceTo(animpos) < close.distanceTo(animpos)) {
							close.xCoord = ox;
							close.yCoord = oy;
							close.zCoord = oz;
						}
					}
					if(entity instanceof EntityMooshroom) {
						if(b == Blocks.mycelium) {
							grassNear = true;
							here.xCoord = ox;
							here.yCoord = oy;
							here.zCoord = oz;
							if(here.distanceTo(animpos) < close.distanceTo(animpos)) {
								close.xCoord = ox;
								close.yCoord = oy;
								close.zCoord = oz;
							}
						}
					}
				}
			}
		}
		return grassNear;
	}
}
