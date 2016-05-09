package com.draco18s.ores.client;

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
import com.draco18s.ores.CommonProxy;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.BlockDummyOre;
import com.draco18s.ores.block.BlockSluiceBottom;
import com.draco18s.ores.entities.TileEntityMillstone;
import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.entities.TileEntitySluice;
import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.entities.TileEntityWindvane;
import com.draco18s.ores.recipes.NEIIntegration;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;

public class ClientProxy extends CommonProxy {
	private HashMap<ChunkCoordTriplet, SoundWindmill> sounds = new HashMap<ChunkCoordTriplet,SoundWindmill>();

	@Override
	public void registerRenderers() {
		TileEntitySpecialRenderer render = new TESRWindvane();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindvane.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockVane), new ItemModelRenderer(render, new TileEntityWindvane()));

		render = new TESRAxel();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmill.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockAxle), new ItemModelRenderer(render, new TileEntityWindmill()));

		render = new TESRSifter();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySifter.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockSifter), new ItemModelRenderer(render, new TileEntitySifter()));

		if(OresBase.sluiceVersion) {
			render = new TESRSluice();
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySluice.class, render);
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockSluice), new ItemModelRenderer(render, new TileEntitySluice()));
		}
		else {
			//render = new TESRSluiceBottom();
			//ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySluiceBottom.class, render);
			//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blocksluice), new ItemModelRenderer(render, new TileEntitySluiceBottom()));
			
			int r = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new SimpleBlockRendererSluice(r));
			((BlockSluiceBottom)OresBase.blockSluice).renderID = r;
		}

		BlockDummyOre.renderID = RenderDummyOre.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new RenderDummyOre());
		if(Loader.isModLoaded("NotEnoughItems")) {
			NEIIntegration.registerNEIRecipes();
		}
	}
	
	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
    	FMLCommonHandler.instance().bus().register(new ClientEventHandler());
	}
	
	@Override
	public void startMillSound(TileEntityMillstone te) {
		if(!OresBase.useSounds) return;
		ChunkCoordTriplet tepos = new ChunkCoordTriplet(te.getWorldObj().provider.dimensionId, te.xCoord, te.yCoord, te.zCoord);
		if(!sounds.containsKey(tepos)) {
			SoundWindmill snd = new SoundWindmill(new ResourceLocation("ores:grain-mill-loop"), te);
			ClientEventHandler.soundsToStart.put(snd, 0);
			//Minecraft.getMinecraft().getSoundHandler().playSound(snd);
			sounds.put(tepos, snd);
		}
		else {
			SoundWindmill snd = sounds.get(tepos);
			if(snd.isDonePlaying()) {
				sounds.remove(tepos);
				snd = new SoundWindmill(new ResourceLocation("ores:grain-mill-loop"), te);
				ClientEventHandler.soundsToStart.put(snd, 0);
				//Minecraft.getMinecraft().getSoundHandler().playSound(snd);
				sounds.put(tepos, snd);
			}
		}
	}
}
