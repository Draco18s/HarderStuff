package com.draco18s.hazards.client;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.draco18s.hazards.entities.PlayerStats;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HazardsClientEventHandler {
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    
	@SubscribeEvent
	public void onFogDensity(FogDensity event) {
		if(event.entity instanceof EntityPlayer && event.entity.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer)event.entity;
			PlayerStats stats = PlayerStats.get(event.entity);
			if(stats != null) {
				float farPlaneDistance = (float)(Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
				float f1 = farPlaneDistance;
				if(stats.airRemaining < 1000) {
					GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
					f1 = 5.0F + (farPlaneDistance - 5.0F) * (Math.max(stats.airRemaining,0) / 1000.0F);
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25F);
			        GL11.glFogf(GL11.GL_FOG_END, f1);
	
			        if (GLContext.getCapabilities().GL_NV_fog_distance) {
			            GL11.glFogi(34138, 34139);
			        }
			        event.setCanceled(true);
			        event.density = 0.1f;
				}
			}
		}
	}
	
	private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}
