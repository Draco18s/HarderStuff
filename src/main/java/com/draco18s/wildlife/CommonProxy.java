package com.draco18s.wildlife;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.draco18s.wildlife.WildlifeEventHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
	public void registerRenderers() {
	}

	public void registerEvents() {
		WildlifeEventHandler handler = new WildlifeEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.ORE_GEN_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
	}

	public World getWorld() {
		return null;
	}
}
