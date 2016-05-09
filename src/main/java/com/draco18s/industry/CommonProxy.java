package com.draco18s.industry;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {

	public void registerRenderers() {
		
	}
	
	public void registerEventHandler() {
		FMLCommonHandler.instance().bus().register(new IndustryEventHandler());
	}

}
