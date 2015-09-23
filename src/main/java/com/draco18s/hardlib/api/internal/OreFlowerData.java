package com.draco18s.hardlib.api.internal;

import net.minecraft.block.Block;

public class OreFlowerData {
	public Block flower;
	public int metadata;
	public int highConcentrationThreshold;
	
	/**
	 * Data neccessary to grow ore indicator plants
	 * @param plant - the plant block
	 * @param meta - the plant block's metadata
	 * @param threshold - high concentration threshold, for bonemeal (spawns an extra, guaranteed plant)
	 */
	public OreFlowerData(Block plant, int meta, int threshold) {
		flower = plant;
		metadata = meta;
		highConcentrationThreshold = threshold;
	}
}
