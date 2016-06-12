package com.draco18s.wildlife.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.api.interfaces.ITreeTracker;
import com.draco18s.wildlife.WildlifeEventHandler;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.IWorldGenerator;

public class TreeCountGenerator implements ITreeTracker {
	//private static ArrayList<Block> treeBlocks;
	//private static ArrayList<Block> logBlocks;
	private List<Block> logBlockList = new ArrayList<Block>();
	private List<Block> dirtBlockList = new ArrayList<Block>();
	//private int dirtID;
	
	public TreeCountGenerator() {
		logBlockList.add(Blocks.log);
		logBlockList.add(Blocks.log2);
		dirtBlockList.add(Blocks.dirt);
		dirtBlockList.add(Blocks.grass);
		//dirtID = Block.getIdFromBlock(Blocks.dirt);
	}
	
	public void addLogType(Block b) {
		logBlockList.add(b);
	}
	
	public void addDirtType(Block b) {
		dirtBlockList.add(b);
	}

	public void generate(Random random, int chunkX, int chunkZ, World world) {
		Block b,b2;
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		//boolean debug = (chunkX == -17 && chunkZ == -3);
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				for(int y = world.provider.getAverageGroundLevel()/2; y < world.provider.getAverageGroundLevel()+150; ++y) {
					b = chunk.getBlock(x, y, z);
					//b = world.getBlock(chunkX*16 + x, y, chunkZ*16 + z);
					if(logBlockList.indexOf(b) >= 0) {
						//if(debug) {
							//System.out.println("Found log, checking for dirt.");
						//}
						b2 = chunk.getBlock(x, y-1, z);
						//b2 = world.getBlock(chunkX*16 + x, y-1, chunkZ*16 + z);
						if(WildlifeEventHandler.doNativeTreeKill && b2 != Blocks.air) {
							if(x > 0 && x < 15 && z > 0 && z < 15) {
								if((b2 == chunk.getBlock(x+1, y-1, z) || b2 == chunk.getBlock(x-1, y-1, z) || b2 == chunk.getBlock(x, y-1, z+1) || b2 == chunk.getBlock(x, y-1, z-1))) {
									b2 = Blocks.dirt;
								}
							}
							else {
								if((b2 == world.getBlock(x+1, y-1, z) || b2 == world.getBlock(x-1, y-1, z) || b2 == world.getBlock(x, y-1, z+1) || b2 == world.getBlock(x, y-1, z-1))) {
									b2 = Blocks.dirt;
								}
							}
						}
						if(dirtBlockList.indexOf(b2) >= 0) {
							//if(debug) {
								//System.out.println("Dirt below.");
							//}
							if(x > 0 && x < 15 && z > 0 && z < 15) {
								if(chunk.getBlock(x+1, y, z+1) != Blocks.farmland && chunk.getBlock(x-1, y, z-1) != Blocks.farmland && chunk.getBlock(x+1, y, z-1) != Blocks.farmland && chunk.getBlock(x-1, y, z+1) != Blocks.farmland) {
									TreeDataHooks.addTree(world, chunkX*16 + x, y, chunkZ*16 + z, -1);
									//if(debug) {
										//System.out.println("Added tree");
									//}
								}
							}
							else if(world.getBlock(chunkX*16 + x+1, y, chunkZ*16 + z+1) != Blocks.farmland && world.getBlock(chunkX*16 + x-1, y, chunkZ*16 + z-1) != Blocks.farmland && world.getBlock(chunkX*16 + x+1, y, chunkZ*16 + z-1) != Blocks.farmland && world.getBlock(chunkX*16 + x-1, y, chunkZ*16 + z+1) != Blocks.farmland) {
								TreeDataHooks.addTree(world, chunkX*16 + x, y, chunkZ*16 + z, -1);
								//if(debug) {
									//System.out.println("Added tree");
								//}
							}
						}
					}
				}
			}
		}
	}

	public void rescan(int chunkX, int chunkZ, World world) {
		Block b,b2;
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		//boolean debug = (chunkX == -15 && chunkZ == 13);
		//if(debug) {
		//	System.out.println("Rescanning debug chunk.");
		//}
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				for(int y = world.provider.getAverageGroundLevel()/2; y < world.provider.getAverageGroundLevel()+150; ++y) {
					b = chunk.getBlock(x, y, z);
					//b = world.getBlock(chunkX*16 + x, y, chunkZ*16 + z);
					if(logBlockList.indexOf(b) >= 0) {
						//if(debug && x == 0) {
							//System.out.println("Found log, checking for dirt.");
						//}
						b2 = chunk.getBlock(x, y-1, z);
						//b2 = world.getBlock(chunkX*16 + x, y-1, chunkZ*16 + z);
						if(WildlifeEventHandler.doNativeTreeKill && b2 != Blocks.air && logBlockList.indexOf(b2) == -1) {
							/*if(x > 0 && x < 15 && z > 0 && z < 15) {
								if((b2 == chunk.getBlock(x+1, y-1, z) || b2 == chunk.getBlock(x-1, y-1, z) || b2 == chunk.getBlock(x, y-1, z+1) || b2 == chunk.getBlock(x, y-1, z-1))) {
									b2 = Blocks.dirt;
								}
							}
							else {
								if((b2 == world.getBlock(x+1, y-1, z) || b2 == world.getBlock(x-1, y-1, z) || b2 == world.getBlock(x, y-1, z+1) || b2 == world.getBlock(x, y-1, z-1))) {
									b2 = Blocks.dirt;
								}
							}*/
							b2 = Blocks.dirt;
						}
						if(b2 == Blocks.dirt || b2 == Blocks.grass) {
							//if(debug) {
								//System.out.println("Dirt below.");
							//}
							if(x > 0 && x < 15 && z > 0 && z < 15) {
								if(chunk.getBlock(x+1, y, z+1) != Blocks.farmland && chunk.getBlock(x-1, y, z-1) != Blocks.farmland && chunk.getBlock(x+1, y, z-1) != Blocks.farmland && chunk.getBlock(x-1, y, z+1) != Blocks.farmland) {
									TreeDataHooks.addTree(world, chunkX*16 + x, y, chunkZ*16 + z, -1);
									//if(debug) {
									//	System.out.println("Added tree " + (chunkX*16 + x) + "," + (chunkZ*16 + z));
									//}
								}
							}
							else if(world.getBlock(chunkX*16 + x+1, y, chunkZ*16 + z+1) != Blocks.farmland && world.getBlock(chunkX*16 + x-1, y, chunkZ*16 + z-1) != Blocks.farmland && world.getBlock(chunkX*16 + x+1, y, chunkZ*16 + z-1) != Blocks.farmland && world.getBlock(chunkX*16 + x-1, y, chunkZ*16 + z+1) != Blocks.farmland) {
								TreeDataHooks.addTree(world, chunkX*16 + x, y, chunkZ*16 + z, -1);
								//if(debug) {
								//	System.out.println("Added tree " + (chunkX*16 + x) + "," + (chunkZ*16 + z));
								//}
							}/**/
						}
					}
				}
			}
		}
	}
}
