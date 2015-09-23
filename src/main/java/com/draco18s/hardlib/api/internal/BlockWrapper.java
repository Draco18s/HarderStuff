package com.draco18s.hardlib.api.internal;

import com.draco18s.hardlib.HashUtils;

import net.minecraft.block.Block;

public class BlockWrapper {
	public Block block;
	public int meta;
	
	public BlockWrapper(Block block, int meta) {
		this.block = block;
		this.meta = meta;
	}
	
	@Override
	public int hashCode() {
		return block.hashCode();
    }
	
	@Override
	public boolean equals(Object aThat) {
		if(aThat instanceof BlockWrapper) {
			BlockWrapper other = (BlockWrapper)aThat;
			return other.block == this.block && (other.meta == -1 || this.meta == -1 || other.meta == this.meta);
		}
		return false;
	}
}
