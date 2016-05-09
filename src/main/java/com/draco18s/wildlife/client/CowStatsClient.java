package com.draco18s.wildlife.client;

import java.lang.ref.WeakReference;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class CowStatsClient  implements IExtendedEntityProperties {
    public static final String COW_PROP = "WildsCowStatsClient";

    public WeakReference<EntityCow> theCow;
    
    public CowStatsClient(EntityCow p) {
    	theCow = new WeakReference<EntityCow>(p);
    }

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		
	}

	@Override
	public void init(Entity entity, World world) {
		
	}
	
    public static void register(EntityCow cow)
    {
    	cow.registerExtendedProperties(CowStatsClient.COW_PROP, new CowStatsClient(cow));
    }

    public static CowStatsClient get(EntityCow cow)
    {
        return (CowStatsClient)cow.getExtendedProperties(CowStatsClient.COW_PROP);
    }
}