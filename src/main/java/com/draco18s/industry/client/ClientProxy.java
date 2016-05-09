package com.draco18s.industry.client;

import com.draco18s.industry.CommonProxy;
import com.draco18s.industry.IndustryBase;
import com.draco18s.industry.block.BlockCartLoader;
import com.draco18s.industry.block.BlockDistributor;
import com.draco18s.industry.block.BlockFilter;
import com.draco18s.industry.block.BlockPoweredRailBridge;
import com.draco18s.industry.block.BlockRailBridge;
import com.draco18s.industry.block.BlockWoodHopper;
import com.draco18s.industry.client.SimpleBlockRendererHopper;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public void registerRenderers() {
		int r = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new SimpleBlockRendererHopper(r));
		((BlockDistributor)IndustryBase.blockDistributor).renderID = r;
		((BlockWoodHopper)IndustryBase.blockWoodHopper).renderID = r;
		((BlockCartLoader)IndustryBase.blockCartLoader).renderID = r;
		((BlockFilter)IndustryBase.blockFilter).renderID = r;

		r = RenderingRegistry.getNextAvailableRenderId();
		((BlockRailBridge)IndustryBase.bridgeRail).renderID = r;
		((BlockPoweredRailBridge)IndustryBase.bridgeRailPowered).renderID = r;
		RenderBridgeRail renderer = new RenderBridgeRail();
		renderer.renderID = r;
		RenderingRegistry.registerBlockHandler(renderer);
	}
}
