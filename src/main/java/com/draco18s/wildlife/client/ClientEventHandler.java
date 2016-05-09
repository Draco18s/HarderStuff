package com.draco18s.wildlife.client;

import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientEventHandler {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerTextures(TextureStitchEvent.Pre event) {
		if(event.map.getTextureType() == 1 ) {
			event.map.setTextureEntry("wildlife:temp", ClientProxy.thermometer = new TextureAtlasThermometer("wildlife:temp",-1));
			/*for(int f = 0; f < 12; f++) {
				event.map.setTextureEntry("wildlife:temp_"+f, ClientProxy.thermometerNBT[f] = new TextureAtlasThermometer("wildlife:temp",f));
			}*/
			event.map.setTextureEntry("wildlife:humid", ClientProxy.rainmeter = new TextureAtlasRainmeter("wildlife:humid"));
			event.map.setTextureEntry("wildlife:season_calendar", ClientProxy.calendar = new TextureCalendar("wildlife:season_calendar"));
		}
	}
}
