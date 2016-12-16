package com.draco18s.flowers.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.flowers.OreFlowersBase;

public class OreCountGenerator {
	private int lastChunkX;
	private int lastChunkZ;

	public void generate(Random random, int chunkX, int chunkZ, World world, boolean shouldPutFlowers) {
		if(lastChunkX == chunkX && lastChunkZ == chunkZ) {
			return;
		}
		lastChunkX = chunkX;
		lastChunkZ = chunkZ;
		int s = HashUtils.hash((int)world.getSeed(), chunkX);
		s = HashUtils.hash(s, 0);
		s = HashUtils.hash(s, chunkZ);
		Random rand = new Random(s);
		List<OreCounter> blockList = new ArrayList<OreCounter>();
		
		Map<BlockWrapper, OreFlowerData> list = HardLibAPI.oreManager.getOreList();
		for(BlockWrapper b : list.keySet()) {
			blockList.add(new OreCounter(b));
		}
		
		ExtendedBlockStorage[] extStore = world.getChunkFromChunkCoords(chunkX, chunkZ).getBlockStorageArray();
		int lastY = 0;
		for(ExtendedBlockStorage ext : extStore) {
			if(ext == null) {
				for(int b=0; b < blockList.size(); ++b) {
					OreCounter c = blockList.get(b);
					OreDataHooks.putOreData(world, chunkX, lastY, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f))/* + c.countC/6*/);
					c.cycleCounts();
					OreDataHooks.putOreData(world, chunkX, lastY + 8, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f))/* + c.countC/6*/);
					c.cycleCounts();
				}
				lastY+=16;
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
				        		int mm = 0;
				        		if(c.meta == -1) {
				        			mm = m+1;
				        		}
				        		else if(c.meta == m){
				        			mm = 16;
				        		}
				        		/*if(c.b.b == OresBase.oreRedstone) {
					        		mm = 16;
					        	}
				        		else {
					        		++mm;
					        	}*/
				        		c.countA += mm;
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				//System.out.println(c.b.block.getUnlocalizedName() + ": " + c.countA);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation(), chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f))/* + c.countC/6*/);
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
				        		int mm = 0;
				        		if(c.meta == -1) {
				        			mm = m+1;
				        		}
				        		else if(c.meta == m){
				        			mm = 16;
				        		}
				        		/*if(c.b.b == OresBase.oreRedstone) {
					        		mm = 16;
					        	}
				        		else {
					        		++mm;
					        	}*/
				        		c.countA += mm;
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				//System.out.println(c.b.block.getUnlocalizedName() + ": " + c.countA);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation()+8, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f))/* + c.countC/6*/);

				if(c.countA > 0) {
					OreFlowersBase.instance.addArbitraryOre(c.b.block);
					if (shouldPutFlowers) {
						OreFlowerData dat = list.get(c.b);
						int ct = Math.min(c.countA + c.countB + c.countC, 75);
						if(ct > 0) { ct += 5; }
						if(rand.nextInt(100) < ct) {
							for(int j=1; lastY+j < 250; j++) {
								if(world.getSavedLightValue(EnumSkyBlock.Sky, chunkX*16 + 8, lastY+j+1, chunkZ*16 + 8) > 7) {
									OreFlowersBase.scatterFlowers(world, chunkX*16 + 8, lastY+j+1, chunkZ*16 + 8, dat.flower, dat.metadata, OreFlowersBase.configWorldgenFlowerRadius, OreFlowersBase.configWorldgenFlowerCount, OreFlowersBase.configWorldgenFlowerSpread);
									//return;
									j = 999;
								}
							}
						}
					}
				}
				
				c.cycleCounts();
			}
			lastY = ext.getYLocation();
		}
	}
	
	private class OreCounter extends Object{
		public BlockWrapper b;
		public int ID;
		public int meta;
		public int countA;
		public int countB;
		public int countC;
		private int fHashCode;
		
		public OreCounter(BlockWrapper block) {
			b = block;
			ID = Block.getIdFromBlock(b.block);
			meta = b.meta;
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
