package com.draco18s.wildlife.entity;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CowStats implements IExtendedEntityProperties {
	private static final String COW_PROP = "WildsCowStats";
	public int milkLevel;
    public WeakReference<EntityCow> theCow;
    
    public CowStats(EntityCow p) {
    	theCow = new WeakReference<EntityCow>(p);
    }
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		//System.out.println("save milkLevel " + milkLevel);
		compound.setInteger("cowMilkLevel", this.milkLevel);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		milkLevel = compound.getInteger("cowMilkLevel");
		//System.out.println("load milkLevel " + milkLevel);
	}

	@Override
	public void init(Entity entity, World world) {
    	milkLevel = 3000 + world.rand.nextInt(12000);
	}

	public static CowStats get(EntityCow entityLiving) {
		return (CowStats) entityLiving.getExtendedProperties(COW_PROP);
	}
	
	public static void register(EntityCow entity) {
        entity.registerExtendedProperties(CowStats.COW_PROP, new CowStats(entity));
    }
}
