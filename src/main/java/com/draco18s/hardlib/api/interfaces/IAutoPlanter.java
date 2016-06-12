package com.draco18s.hardlib.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IAutoPlanter {
	@Deprecated
	public BlockType getType(Block id);
	public BlockType getType(ItemStack id);
	@Deprecated
	public void registerBlockType(Block key, BlockType value);
	
	/**
	 * Registers an item with the Autoplanter.
	 * @param key The item must either be an ItemBlock or an Item that implements IPlantable or the autoplant
	 * behavior won't work correctly.
	 * @param value
	 */
	public void registerBlockType(ItemStack key, BlockType value);
	
	/**
	 * SAPLINGS plant sometimes
	 * SAPLING_ALLWAYS_2x2 are for tress that always appear as 2x2 trunks (Dark Oak)
	 * SAPLING_SOMETIMES_2x2 are for trees that CAN be 2x2 (jungle, spruce) and will not always try to make 2x2s
	 * CACTUS, MUSHROOM, NETHERSTALK, and REEDS are all treated identically, but the distinction may be useful
	 * @author Major
	 *
	 */
	public enum BlockType {
		SAPLING,
		SAPLING_ALLWAYS_2x2,
		SAPLING_SOMETIMES_2x2,
		CACTUS,
		MUSHROOM,
		NETHERSTALK,
		REEDS;
	}
}
