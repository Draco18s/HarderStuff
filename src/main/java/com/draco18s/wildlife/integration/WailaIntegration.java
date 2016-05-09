package com.draco18s.wildlife.integration;

import com.draco18s.wildlife.entity.EntityLizard;
import com.draco18s.wildlife.entity.TileEntityGrassSnow;
import com.draco18s.wildlife.integration.waila.*;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaIntegration {

	public static void callbackRegister(IWailaRegistrar registrar) {
		try {
			registrar.registerBodyProvider(new WailaItemFrameProvider(), EntityItemFrame.class);
			registrar.registerBodyProvider(new WailaSnowyBlockProvider(), TileEntityGrassSnow.class);
			registrar.registerBodyProvider(new WailaAnimalProvider(), EntityAnimal.class);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
