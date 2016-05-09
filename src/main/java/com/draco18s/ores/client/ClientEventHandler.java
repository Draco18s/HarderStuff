package com.draco18s.ores.client;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientEventHandler {
	public static ConcurrentHashMap<ITickableSound,Integer> soundsToStart = new ConcurrentHashMap<ITickableSound,Integer>();
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase == Phase.START) {
			Iterator it = soundsToStart.keySet().iterator();
			while(it.hasNext()) {
				ITickableSound snd = (ITickableSound) it.next();
				Minecraft.getMinecraft().getSoundHandler().playSound(snd);
				it.remove();
			}
		}
	}
}
