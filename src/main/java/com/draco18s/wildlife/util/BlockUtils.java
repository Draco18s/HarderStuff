package com.draco18s.wildlife.util;

import java.util.HashMap;

import net.minecraft.block.Block;

import com.draco18s.hardlib.api.interfaces.IAutoPlanter;

public class BlockUtils implements IAutoPlanter {
	private static HashMap<Block,BlockType> plantables = new HashMap<Block,BlockType>();

	public BlockType getType(Block id) {
		return plantables.get(id);
	}
	
	public void registerBlockType(Block key, BlockType value) {
		if(key == null) {
			RuntimeException e = new RuntimeException("Attempted to define a null block as " + value.name());
			e.printStackTrace();
			return;
		}
		plantables.put(key, value);
	}
}
