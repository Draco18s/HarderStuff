package com.draco18s.hardlib;

import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

public class WorldUtils {
	static public boolean isChunkLoaded_noChunkLoading(IBlockAccess world, final int x, final int z) {
		// skip unloaded worlds
		if (world == null) {
			return false;
		}
		if (world instanceof WorldServer) {
			boolean isLoaded = false;
			if (((WorldServer)world).getChunkProvider() instanceof ChunkProviderServer) {
				ChunkProviderServer chunkProviderServer = (ChunkProviderServer) ((WorldServer)world).getChunkProvider();
				try {
					Chunk chunk = (Chunk)chunkProviderServer.loadedChunkHashMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4));
					if (chunk == null) {
						isLoaded = false;
					} else {
						isLoaded = chunk.isChunkLoaded;
					}
				} catch (NoSuchFieldError exception) {
					isLoaded = chunkProviderServer.chunkExists(x >> 4, z >> 4);
				}
			} else {
				isLoaded = ((WorldServer)world).getChunkProvider().chunkExists(x >> 4, z >> 4);
			}
			// skip unloaded chunks
			if (!isLoaded) {
				return false;
			}
		}
		return true;
	}
}
