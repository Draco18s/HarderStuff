package com.draco18s.hardlib.api.interfaces;

import net.minecraft.block.Block;

public interface IAutoPlanter {
	public BlockType getType(Block id);
	public void registerBlockType(Block key, BlockType value);
	
	public enum BlockType {
		SAPLING,
		CACTUS,
		MUSHROOM,
		NETHERSTALK,
		REEDS;
	}
}
