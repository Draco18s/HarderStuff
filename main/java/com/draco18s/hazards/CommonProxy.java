package com.draco18s.hazards;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	public void registerRenderers() {
		
	}
	
	public void registerEvents() {
		HazardsEventHandler handler = new HazardsEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.ORE_GEN_BUS.register(handler);
		//FMLCommonHandler.instance().bus().register(handler);
	}
}
