package com.draco18s.hardlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CustomOreGen.Util.PDist;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="HardLib", name="HardLib", version="{@version:lib}", dependencies = "required-after:CustomOreGen")
public class HardLib {
	@SidedProxy(clientSide="com.draco18s.hardlib.client.ClientProxy", serverSide="com.draco18s.hardlib.CommonProxy")
    public static CommonProxy proxy;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		/*try {
			Class.forName("CustomOreGen.Util.CogOreGenEvent");
		} catch(ClassNotFoundException e) {
			//throw new IllegalStateException("HardLib requires COG version 1.2.14 (released Dec 5, 2014) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.", e);
			throw new CogMissingException();
		}*/
		
		/*Class clz = CustomOreGen.Util.PDist.Type.class;
		Object[] o = clz.getEnumConstants();
		if(o.length == 2) {
			throw new CogMissingException();
			//Logger logger = event.getModLog();
			//logger.warn("HardLib works better using COG version 1.2.18 or later (Feb 21, 2015).  It will still run, but the vein distributions will not be as intended.");
			//throw new IllegalStateException("HardLib requires COG version 1.2.18 (released Feb 25, 2015) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.");
		}*/
		//try get class BlockArrangement
		/*if(PDist.Type.values().length == 2) {
			proxy.throwException();
		}*/
		try {
			Class.forName("CustomOreGen.Util.BlockArrangement");
		} catch(ClassNotFoundException e) {
			//throw new IllegalStateException("HardLib requires COG version 1.2.14 (released Dec 5, 2014) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.", e);
			proxy.throwException();
		}
		proxy.init();
	}
}
