package com.draco18s.flowers;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	public int renderPass;

	public void registerRenderers() {
		
	}
	
	public void registerEventHandlers() {
		OresEventHandler handler = new OresEventHandler();
    	MinecraftForge.EVENT_BUS.register(handler);
    	MinecraftForge.ORE_GEN_BUS.register(handler);
    	FMLCommonHandler.instance().bus().register(handler);
	}
}
