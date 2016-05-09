package com.draco18s.hazards;

import com.draco18s.hardlib.HashUtils;

import net.minecraft.block.Block;

public class BlockStoneType {
	public final Block stone;
	public final int meta;
	public final int blockID;
	
	public BlockStoneType(Block b, int m) {
		stone = b;
		meta = m;
		blockID = Block.getIdFromBlock(stone);
	}
	
	public int hashCode() {
		int h = HashUtils.firstTerm(HashUtils.SEED);
		h = HashUtils.hash(h, blockID);
		h = HashUtils.hash(h, meta);
		return 0;
	}
	
	public boolean equals(Object o) {
		if(o instanceof BlockStoneType) {
			BlockStoneType ot = (BlockStoneType)o;
			return ot.stone == this.stone && ot.meta == this.meta;
		}
		return false;
	}
}
