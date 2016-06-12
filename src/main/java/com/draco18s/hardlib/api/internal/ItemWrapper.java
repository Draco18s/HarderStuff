package com.draco18s.hardlib.api.internal;

import com.draco18s.hardlib.HashUtils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ItemWrapper {
	public Item item;
	public int meta;
	
	public ItemWrapper(Item block, int meta) {
		this.item = block;
		this.meta = meta;
	}
	
	@Override
	public int hashCode() {
		return item.hashCode();
    }
	
	@Override
	public boolean equals(Object aThat) {
		if(aThat instanceof ItemWrapper) {
			ItemWrapper other = (ItemWrapper)aThat;
			return other.item == this.item && (other.meta == -1 || this.meta == -1 || other.meta == this.meta);
		}
		return false;
	}
}
