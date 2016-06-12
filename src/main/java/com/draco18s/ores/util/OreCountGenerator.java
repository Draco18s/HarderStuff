package com.draco18s.ores.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.ores.BlockHardOreBase;
import com.draco18s.ores.recipes.IC2Integration;
import com.draco18s.ores.recipes.RecipeManager;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;

public class OreCountGenerator {
	private int lastChunkX = Integer.MAX_VALUE;
	private int lastChunkZ = Integer.MAX_VALUE;

	public void generate(Random random, int chunkX, int chunkZ, World world) {
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
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		ExtendedBlockStorage[] extStore = chunk.getBlockStorageArray();
		for(int y=0; y < 256; y++) {
			if(y % 8 == 0 && y > 0) {
				for(int b=0; b < blockList.size(); ++b) {
					OreCounter c = blockList.get(b);
					OreDataHooks.putOreData(world, chunkX, y-8, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f)));
					c.cycleCounts();
				}
			}
			for(int x=0; x < 16; ++x) {
				for(int z=0; z < 16; ++z) {
					Block blockAt = chunk.getBlock(x, y, z);
					int meta = chunk.getBlockMetadata(x, y, z);
					if(blockAt != Blocks.air) {
						for(int bl = 0; bl < blockList.size(); bl++) {
							OreCounter c = blockList.get(bl);
							if(blockAt == c.b.block) {
				        		int mm = 0;
				        		if(c.meta == -1) {
				        			mm = meta+1;
				        		}
				        		else if(c.meta == meta){
				        			mm = 16;
				        		}
				        		c.countA += mm;
							}
						}
					}

		        	//if(chunkX == 0 && chunkZ == 0) {
		        	//	if(x == 3 && z == 3 && y < 32) {
		        	//		System.out.println("At 3,"+y+",3: " + blockAt.getUnlocalizedName() + ":" + meta);
		        	//	}
		        	//}
				}
			}
		}
		
		/*ExtendedBlockStorage[] extStore = world.getChunkFromChunkCoords(chunkX, chunkZ).getBlockStorageArray();
		int lastY = 0;
		for(ExtendedBlockStorage ext : extStore) {
			if(ext == null) {
				for(int b=0; b < blockList.size(); ++b) {
					OreCounter c = blockList.get(b);
					OreDataHooks.putOreData(world, chunkX, lastY, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f)));
					c.cycleCounts();
					OreDataHooks.putOreData(world, chunkX, lastY+8, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f)));
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
				        		c.countA += mm;
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation(), chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f)));
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
				        		c.countA += mm;
				        	}
				        	if(c.b.block == OresBase.oreSilver && chunkX == 0 && chunkZ == 0) {
				        		if(x == 3 && z == 3) {
				        			Block bbid = Block.getBlockById(l);
				        			System.out.println("At 3,"+(y + ext.getYLocation())+",3: " + bbid.getUnlocalizedName() + ":" + metas.get(x, y, z));
				        		}
				        	}
				        }
					}
				}
	        }
			for(int b=0; b < blockList.size(); ++b) {
				OreCounter c = blockList.get(b);
				OreDataHooks.putOreData(world, chunkX, ext.getYLocation()+8, chunkZ, c.b, (int)(c.countA*(2f/3f) + c.countB*(1f/3f)));

				if(!(c.b.block instanceof BlockHardOreBase) && c.countA > 0) {
					OreFlowerData dat = list.get(c.b);
					int ct = Math.min(c.countA + c.countB + c.countC, 75);
					if(ct > 0) { ct += 5; }
					if(rand.nextInt(100) < ct) {
						for(int j=1; lastY+j < 250; j++) {
							if(world.getSavedLightValue(EnumSkyBlock.Sky, chunkX*16, lastY+j+1, chunkZ*16) > 7) {
								OresBase.scatterFlowers(world, chunkX*16, lastY+j+1, chunkZ*16, dat.flower, dat.metadata, 16, 6, 9);
								//return;
								j = 999;
							}
						}
					}
				}
				if((c.b.block instanceof BlockHardOreBase) && c.countA > 0) {
					if(c.b.block != OresBase.oreIron && c.b.block != OresBase.oreGold ) {
						OresBase.instance.addArbitraryOre(c.b.block);
					}
					if(chunkX == 0 && chunkZ == 0 && ext.getYLocation() < 90) {
						System.out.println("Post spawn: " + c.b.block.getUnlocalizedName() + ":" + c.b.meta + " => " + c.countA);
					}
				}
				c.cycleCounts();
			}
			lastY = ext.getYLocation();
		}*/
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
