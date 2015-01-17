package com.draco18s.wildlife.util;

import java.util.HashMap;

import net.minecraft.block.Block;

public class BlockUtils {
	private static HashMap<Block,BlockType> plantables = new HashMap<Block,BlockType>();

	public static BlockType getType(Block id) {
		return plantables.get(id);
	}
	
	public static void registerBlockType(Block key, BlockType value) {
		plantables.put(key, value);
	}
	
	public enum BlockType {
		SAPLING,
		CACTUS,
		MUSHROOM,
		NETHERSTALK,
		REEDS;
	}
}
