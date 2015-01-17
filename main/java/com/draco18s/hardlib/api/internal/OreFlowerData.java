package com.draco18s.hardlib.api.internal;

import net.minecraft.block.Block;

public class OreFlowerData {
	public Block flower;
	public int metadata;
	public int highConcentrationThreshold;
	
	public OreFlowerData(Block plant, int meta, int threshold) {
		flower = plant;
		metadata = meta;
		highConcentrationThreshold = threshold;
	}
}
