package com.draco18s.hazards.integration;

import com.draco18s.hazards.block.BlockUnstableStone;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaIntegration {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerStackProvider(new WailaUnstableProvider(), BlockUnstableStone.class);
	}
}
