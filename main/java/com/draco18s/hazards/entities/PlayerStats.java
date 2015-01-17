package com.draco18s.hazards.entities;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerStats implements IExtendedEntityProperties {
	private static final String PLAYER_PROP = "HazPlayerStats";
	public int airRemaining;
	public int maxAir;
    public WeakReference<EntityLivingBase> living;
    
    public PlayerStats(EntityLivingBase p) {
    	living = new WeakReference<EntityLivingBase>(p);
    	airRemaining = 3000;
    	maxAir = -1;
    }
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("playerAirRemaining", this.airRemaining);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		airRemaining = compound.getInteger("playerAirRemaining");
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public static PlayerStats get(EntityLivingBase entityLiving) {
		return (PlayerStats) entityLiving.getExtendedProperties(PLAYER_PROP);
	}
	
	public static void register(EntityLivingBase entity) {
        entity.registerExtendedProperties(PlayerStats.PLAYER_PROP, new PlayerStats(entity));
    }
}
