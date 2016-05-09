package com.draco18s.hazards.client;

import net.minecraftforge.common.MinecraftForge;

import com.draco18s.hazards.CommonProxy;
import com.draco18s.hazards.block.BlockGas;
import com.draco18s.hazards.block.BlockUnstableStone;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		int r = RenderingRegistry.getNextAvailableRenderId();
		RenderFluidBlock.renderID = r;
		RenderingRegistry.registerBlockHandler(RenderFluidBlock.instance);
		BlockGas.renderID = r;
		
		r = RenderingRegistry.getNextAvailableRenderId();
		RenderUnstableBlock.renderID = r;
		RenderingRegistry.registerBlockHandler(RenderUnstableBlock.instance);
		BlockUnstableStone.renderID = r;
	}

	@Override
	public void registerEvents() {
		super.registerEvents();
		HazardsClientEventHandler eventHandle = new HazardsClientEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandle);
		
		FMLCommonHandler.instance().bus().register(eventHandle);
	}
}
