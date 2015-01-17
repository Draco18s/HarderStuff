package com.draco18s.hardlib;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="HardLib", name="HardLib", version="1.1.0", dependencies = "required-after:CustomOreGen")
public class HardLib {
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		try {
			Class.forName("CustomOreGen.Util.CogOreGenEvent");
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("HardLib requires COG version 1.2.14 (released Dec 5, 2014) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.", e);
		}
	}
}
