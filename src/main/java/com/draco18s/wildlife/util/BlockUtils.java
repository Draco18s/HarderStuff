package com.draco18s.wildlife.util;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.draco18s.hardlib.api.interfaces.IAutoPlanter;
import com.draco18s.hardlib.api.internal.ItemWrapper;

public class BlockUtils implements IAutoPlanter {
	private static HashMap<ItemWrapper,BlockType> plantables = new HashMap<ItemWrapper,BlockType>();

	public BlockType getType(Block id) {
		return getType(new ItemStack(Item.getItemFromBlock(id)));
	}

	@Override
	public BlockType getType(ItemStack stack) {
		ItemWrapper key = new ItemWrapper(stack.getItem(), stack.getItemDamage());
		return plantables.get(key);
	}
	
	public void registerBlockType(Block key, BlockType value) {
		this.registerBlockType(new ItemStack(Item.getItemFromBlock(key)), value);
	}

	@Override
	public void registerBlockType(ItemStack stack, BlockType value) {
		if(stack.getItem() == null) {
			RuntimeException e = new RuntimeException("Attempted to define a null block as " + value.name());
			e.printStackTrace();
			return;
		}
		ItemWrapper key = new ItemWrapper(stack.getItem(), stack.getItemDamage());
		plantables.put(key, value);
	}
}
