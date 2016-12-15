package com.draco18s.flowers;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import CustomOreGen.Util.CogOreGenEvent;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.flowers.util.OreDataHooks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OresEventHandler {
	public static boolean poopBonemealFlowers = false;

	@SubscribeEvent
	public void bonemeal(BonemealEvent event) {
		if(!event.world.isRemote && event.block == Blocks.grass && (event.entityPlayer != null || poopBonemealFlowers)) {
			Map<BlockWrapper,OreFlowerData> list = HardLibAPI.oreManager.getOreList();
			OreFlowerData entry;
			for(BlockWrapper b : list.keySet()) {
				int count = 0;
				for (int scanLevel = 0; scanLevel < OreFlowersBase.configScanDepth; scanLevel++) {
					int scanY = event.y - (scanLevel * 8);
					count += OreDataHooks.getOreData(event.world, event.x, scanY, event.z, b);
				}
				//System.out.println(b.block.getUnlocalizedName() + ": " + count);
				if(count > 0) {
					count = (int)Math.min(Math.round(Math.log(count)), 10);
					entry = list.get(b);
					if(count >= entry.highConcentrationThreshold && event.entityPlayer != null) {
						OreFlowersBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
					}
					for(;--count >= 0;) {
						if(OreFlowersBase.rand.nextBoolean() && (event.entityPlayer != null || OreFlowersBase.rand.nextInt(128) == 0)) {
							OreFlowersBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		if(event.world.isRemote || event.world.provider.dimensionId == Integer.MIN_VALUE) return;
		Chunk c = event.world.getChunkFromBlockCoords(event.worldX, event.worldZ);
		int cx = c.xPosition;
		int cz = c.zPosition;
		OreFlowersBase.oreCounter.generate(null, cx, cz, event.world);
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		if(!event.world.isRemote)
			OreDataHooks.readData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event) {
		if(!event.world.isRemote)
			OreDataHooks.saveData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		if(!event.world.isRemote) {
			//OresBase.logger.log(Level.INFO, "Is chunk loaded: " + event.getChunk().isChunkLoaded);
			OreDataHooks.clearData(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
			//WorldUtils.isChunkLoaded_noChunkLoading(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
			//OresBase.logger.log(Level.INFO, "Is chunk loaded: " + event.getChunk().isChunkLoaded);
		}
	}
}
