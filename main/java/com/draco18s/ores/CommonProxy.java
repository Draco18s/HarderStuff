package com.draco18s.ores;

import com.draco18s.ores.recipes.IC2Integration;

import cpw.mods.fml.common.Loader;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	public int renderPass;

	public void registerRenderers() {
		
	}
	
	public void registerEventHandlers() {
		if(Loader.isModLoaded("IC2")){
			MinecraftForge.EVENT_BUS.register(new IC2Integration());
		}
		//OreDataHooks.createCache();
		OresEventHandler handler = new OresEventHandler();
    	MinecraftForge.EVENT_BUS.register(handler);
    	MinecraftForge.ORE_GEN_BUS.register(handler);
	}
}
