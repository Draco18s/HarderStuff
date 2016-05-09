package com.draco18s.ores.recipes;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.recipes.waila.*;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaIntegration {

	public static void callbackRegister(IWailaRegistrar registrar) {
		WailaOreFlowerProvider flowerProvider = new WailaOreFlowerProvider();
		registrar.registerBodyProvider(flowerProvider, OresBase.blockOreFlowers.getClass());
		registrar.registerBodyProvider(flowerProvider, OresBase.blockOreFlowers2.getClass());

		WailaWindmillProvider windProvider = new WailaWindmillProvider();
		registrar.registerBodyProvider(windProvider, TileEntityWindmill.class);
	}
}
