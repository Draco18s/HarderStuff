package com.draco18s.ores.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlockWithMetadata {
	
	public ItemBlockOre(Block b) {
		super(b, b);
	}

	public ItemBlockOre(Block b1, Block b2) {
		super(b1, b2);
	}
}
