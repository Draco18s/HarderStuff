package com.draco18s.hazards.client;

import java.lang.ref.WeakReference;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerStatsClient  implements IExtendedEntityProperties {
    public static final String PLAYER_PROP = "PlayerStatsClient";

    public WeakReference<EntityPlayerSP> player;
    
    public PlayerStatsClient(EntityPlayerSP p) {
        player = new WeakReference<EntityPlayerSP>(p);
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
	
    public static void register(EntityPlayerSP player)
    {
        player.registerExtendedProperties(PlayerStatsClient.PLAYER_PROP, new PlayerStatsClient(player));
    }

    public static PlayerStatsClient get(EntityPlayerSP player)
    {
        return (PlayerStatsClient) player.getExtendedProperties(PlayerStatsClient.PLAYER_PROP);
    }
}
