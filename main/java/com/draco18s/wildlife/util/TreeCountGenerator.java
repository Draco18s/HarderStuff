package com.draco18s.wildlife.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

public class TreeCountGenerator {
	//private static ArrayList<Block> treeBlocks;
	//private static ArrayList<Block> logBlocks;
	private List<Block> blockList = new ArrayList<Block>();
	//private int dirtID;
	
	public TreeCountGenerator() {
		blockList.add(Blocks.log);
		blockList.add(Blocks.log2);
		//dirtID = Block.getIdFromBlock(Blocks.dirt);
	}

	public void generate(Random random, int chunkX, int chunkZ, World world) {
		Block b,b2;
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				for(int y = 60; y < 160; ++y) {
					b = chunk.getBlock(x, y, z);
					//b = world.getBlock(chunkX*16 + x, y, chunkZ*16 + z);
					if(blockList.indexOf(b) >= 0) {
						b2 = chunk.getBlock(x, y-1, z);
						//b2 = world.getBlock(chunkX*16 + x, y-1, chunkZ*16 + z);
						if(b2 == Blocks.dirt) {
							if(world.getBlock(x+1, y, z+1) != Blocks.farmland && world.getBlock(x-1, y, z-1) != Blocks.farmland && world.getBlock(x+1, y, z-1) != Blocks.farmland && world.getBlock(x-1, y, z+1) != Blocks.farmland) {
								TreeDataHooks.addTree(world, chunkX*16 + x, y, chunkZ*16 + z, -1);
							}
						}
					}
				}
			}
		}
	}
}
