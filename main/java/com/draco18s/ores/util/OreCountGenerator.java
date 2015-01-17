package com.draco18s.ores.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.recipes.IC2Integration;
import com.draco18s.ores.recipes.RecipeManager;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;

public class OreCountGenerator {

	public void generate(Random random, int chunkX, int chunkZ, World world) {
		List<OreCounter> blockList = new ArrayList<OreCounter>();
		
		Map<Block, OreFlowerData> list = RecipeManager.getOreList();
		for(Block b : list.keySet()) {
			blockList.add(new OreCounter(b));
		}
		
		ExtendedBlockStorage[] extStore = world.getChunkFromChunkCoords(chunkX, chunkZ).getBlockStorageArray();
		int lastY = 0;
		for(ExtendedBlockStorage ext : extStore) {
			if(ext == null) {
				for(int b=0; b < blockList.size(); ++b) {
					OreCounter c = blockList.get(b);
					OreDataHooks.putOreData(world, chunkX, lastY+16, chunkZ, c.b, c.countA/2 + c.countB/3 + c.countC/6);
					c.cycleCounts();
					OreDataHooks.putOreData(world, chunkX, lastY+24, chunkZ, c.b, c.countA/2 + c.countB/3 + c.countC/6);
					c.cycleCounts();
				}
				continue;
			}
			byte[] lsb = ext.getBlockLSBArray();
			NibbleArray msb = ext.getBlockMSBArray();
			NibbleArray metas = ext.getMetadataArray();
			for(int x=0; x < 16; ++x) {
				for(int z=0; z < 16; ++z) {
					for(int y=0; y < 8; ++y) {
						int l = lsb[y << 8 | z << 4 | x] & 255;
			
				        if (msb != null) {
				            l |= msb.get(x, y, z) << 8;
				        }
				        OreCounter c;
				        for(int bl = 0; bl < blockList.size(); bl++) {
				        	c = blockList.get(bl);
				        	if(c.ID == l) {
				        		int m = metas.get(x, y, z);
				        		if(c.b == OresBase.oreRedstone) {
					        		m = 16;
					        	}
				        		else {
					        		++m;
					        	}
				        		c.countA += m;
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				//System.out.println(c.b.getUnlocalizedName() + ": " + c.countA);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation(), chunkZ, c.b, c.countA/2 + c.countB/3 + c.countC/6);
				c.cycleCounts();
			}
			for(int x=0; x < 16; ++x) {
				for(int z=0; z < 16; ++z) {
					for(int y=8; y < 16; ++y) {
						int l = lsb[y << 8 | z << 4 | x] & 255;
			
				        if (msb != null) {
				            l |= msb.get(x, y, z) << 8;
				        }
				        OreCounter c;
				        for(int bl = 0; bl < blockList.size(); bl++) {
				        	c = blockList.get(bl);
				        	if(c.ID == l) {
				        		int m = metas.get(x, y, z);
				        		if(c.b == OresBase.oreRedstone) {
					        		m = 16;
					        	}
				        		else {
					        		++m;
					        	}
				        		c.countA += m;
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				//System.out.println(c.b.getUnlocalizedName() + ": " + c.countA);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation()+8, chunkZ, c.b, c.countA/2 + c.countB/3 + c.countC/6);
				c.cycleCounts();
			}
			lastY = ext.getYLocation();
		}
	}
	
	private class OreCounter extends Object{
		public Block b;
		public int ID;
		public int countA;
		public int countB;
		public int countC;
		private int fHashCode;
		
		public OreCounter(Block block) {
			b = block;
			ID = Block.getIdFromBlock(b);
			countA = 0;
			countB = 0;
			countC = 0;
		}
		
		public void cycleCounts() {
			countC = countB;
			countB = countA;
			countA = 0;
		}
		
		@Override
		public int hashCode() {
			if (fHashCode == 0) {
			      int result = HashUtils.SEED;
			      result = HashUtils.hash(result, ID);
			      fHashCode = result;
			}
			return fHashCode;
	    }
		
		@Override
		public boolean equals(Object aThat) {
			OreCounter that = (OreCounter) aThat;
			return that.b == b;
		}
	}
}
