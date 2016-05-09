package com.draco18s.flowers.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;

import com.draco18s.hardlib.ItemModelRenderer;
import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.flowers.CommonProxy;
import com.draco18s.flowers.OreFlowersBase;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		
	}
}
