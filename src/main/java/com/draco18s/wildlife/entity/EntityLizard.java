package com.draco18s.wildlife.entity;

import java.util.Collections;
import java.util.List;

import com.draco18s.wildlife.entity.ai.*;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityLizard extends EntityMob {
	protected int eatingDelay = 0;
	private EntityAIGetAttackableTarget huntingTask = new EntityAIGetAttackableTarget(this, EntityAgeable.class, 0, false);
	private EntityAIGetFoodItemTarget.Sorter foodSorter;

	public EntityLizard(World p_i1738_1_) {
		super(p_i1738_1_);
		this.setSize(0.8F, 0.5F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIRestrictMoon(this));
		this.tasks.addTask(3, new EntityAIFleeMoon(this, 1.0D));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.3D, false));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityAgeable.class, 1.3D, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWanderRestricted(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		//this.targetTasks.addTask(3, new EntityAIGetFoodItemTarget(this, EntityItem.class, 0, true));
		this.targetTasks.addTask(4, huntingTask );
		this.setEquipmentDropChance(1, 0);
		this.setEquipmentDropChance(2, 0);
		this.setEquipmentDropChance(3, 0);
		this.setEquipmentDropChance(4, 0);
		this.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
		this.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
		this.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
		this.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
		foodSorter = new EntityAIGetFoodItemTarget.Sorter(this);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.18D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return super.getExperiencePoints(p_70693_1_);
	}

	@Override
	protected Item getDropItem() {
		return Items.leather;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		Item item = this.getDropItem();

		if (item != null) {
			int j = this.rand.nextInt(3)+2;

			if (p_70628_2_ > 0) {
				j += this.rand.nextInt(p_70628_2_ + 1);
			}

			for (int k = 0; k < j; ++k) {
				this.dropItem(item, 1);
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public boolean handleWaterMovement()
    {
		this.boundingBox.minY += 0.4D;
        if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, 0.4D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this)) {
            if (!this.inWater) {
                float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.playSound(this.getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                float f1 = (float)MathHelper.floor_double(this.boundingBox.minY);
                int i;
                float f2;
                float f3;

                for (i = 0; (float)i < 1.0F + this.width * 20.0F; ++i) {
                    f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("bubble", this.posX + (double)f2, (double)(f1 + 1.0F), this.posZ + (double)f3, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
                }

                for (i = 0; (float)i < 1.0F + this.width * 20.0F; ++i) {
                    f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("splash", this.posX + (double)f2, (double)(f1 + 1.0F), this.posZ + (double)f3, this.motionX, this.motionY, this.motionZ);
                }
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        }
        else {
            this.inWater = false;
        }
		this.boundingBox.minY -= 0.4D;

        return this.inWater;
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		/*if(this.isWet()) {
			if(this.motionY < 0) {
				this.motionY *= 0.8f;
			}
			else {
				this.motionY *= 1.1f;
			}
		}*/
		if(worldObj.isRemote) {
			Vec3 lp = Vec3.createVectorHelper(lastTickPosX, lastTickPosY, lastTickPosZ);
			Vec3 np = Vec3.createVectorHelper(posX, posY, posZ);
			boolean moved = np.distanceTo(lp) > 0;
		}
		else {
			huntingTask.hungerLevel = Math.max(huntingTask.hungerLevel-1, 0);
			//if(huntingTask.hungerLevel < 60) {
			//this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, false));
			//this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGoat.class, 0, false));
			//this.targetTasks.addTask(4, huntingTask );
			//}
			if(eatingDelay > 0) {
				eatingDelay--;
			}
			else {
				eatingDelay = 20;
				List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, getFoodBox());
				//System.out.println("Num ItemEnts: " + list.size());
				
				Collections.sort(list, foodSorter);
				
				ItemStack s;
				boolean ateSomething = false;
				for(int i = list.size()-1; !ateSomething && i >=0; i--) {
					s = list.get(i).getEntityItem();
					if(s.getItem() instanceof ItemFood) {
						int m = 0;
						if(s.getUnlocalizedName().toLowerCase().indexOf("raw") >= 0) {
							m = 240;
						}
						else if(s.getUnlocalizedName().toLowerCase().indexOf("cooked") >= 0) {
							m = 30;
						}
						else if(s.getUnlocalizedName().toLowerCase().indexOf("flesh") >= 0) {
							m = 200;
						}
						//System.out.println("m: " + m);
						//System.out.println("  - " + s.getUnlocalizedName());
						if(m > 0) {
							huntingTask.hungerLevel += m * ((ItemFood)s.getItem()).func_150905_g(s) * 4;//x4 to last several days
							s.stackSize--;
							if(s.stackSize <= 0)
								list.get(i).setDead();
							ateSomething = true;
							//System.out.println("Ate a thing " + (m * ((ItemFood)s.getItem()).func_150905_g(s)));
							eatingDelay = 60;
						}
					}
				}
			}
			if(hurtResistantTime < 0) {
				hurtResistantTime++;
			}
			else if(huntingTask.hungerLevel > 28000 && hurtResistantTime == 0) {
				this.heal(0.5f);
				this.hurtResistantTime = -80;
			}
			//System.out.println("Food: " + huntingTask.hungerLevel + ", " + huntingTask.hungerTrigger);
		}
	}

	public void onLivingUpdate() {
		if (!this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			if(this.rand.nextFloat() > 0.75f)
				huntingTask.hungerLevel++;
			float f = getBlockPathWeight((int)posX, (int)posY, (int)posZ);
			//System.out.println("Bright: " + f);
			//if (f < 0.3F && this.rand.nextFloat() * 30.0F < (f + 0.1F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.hurtResistantTime <= 0) {
			if(f > 0 && this.hurtResistantTime <= 0 && this.rand.nextFloat() < f-0.2f) {
				this.attackEntityFrom(DamageSource.wither, 0.5f);
			}
		}

		super.onLivingUpdate();
	}

	public AxisAlignedBB getFoodBox() {
		return this.boundingBox.expand(4, 2, 4);
	}

	@Override
	public float getBlockPathWeight(int x, int y, int z) {
		int SkyLight = this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z);
		if(this.worldObj.getBlock(x, y, z).isOpaqueCube()) {
			SkyLight = 15;
		}
		int BlockLight = this.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z);
		float r = 0.5f - ((Math.max(BlockLight-4,0) + (18-SkyLight)) / 15f);
		//System.out.println(BlockLight + "," + SkyLight + ": " + r);
		return r;
		//return -0.5F + this.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof EntityLivingBase) {
				byte b0 = 3;

				if (this.worldObj.difficultySetting == EnumDifficulty.NORMAL) {
					b0 = 7;
				}
				else if (this.worldObj.difficultySetting == EnumDifficulty.HARD) {
					b0 = 11;
				}
				if (b0 > 0)  {
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, b0 * 20, 0));
				}
			}

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		huntingTask.hungerLevel = nbt.getInteger("foodLevel");
		huntingTask.hungerTrigger = nbt.getBoolean("hungerTrigger");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("foodLevel", huntingTask.hungerLevel);
		nbt.setBoolean("hungerTrigger", huntingTask.hungerTrigger);
	}

	@Override
    protected boolean isValidLightLevel() {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        int l = 15-this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k);
        //System.out.println(i +","+j+","+k);
        //System.out.println("L: " + l);
        boolean r = (l <= this.rand.nextInt(8));
        /*if(r) {
        	System.out.println("Lizard near " + i +","+j+","+k);	
        }*/
        return r;
    }
}
