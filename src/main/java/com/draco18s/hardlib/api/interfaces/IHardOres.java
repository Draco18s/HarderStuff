package com.draco18s.hardlib.api.interfaces;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHardOres {
	
	/**
	 * Registers an ore (with metadata) with its matching indicator plant
	 * @param ore
	 * @param meta
	 * @param data
	 */
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data);

	/**
	 * Gets the hash of all of the registered ore blocks for flower data.
	 * @return
	 */
	public Map<BlockWrapper, OreFlowerData> getOreList();
	
	/**
	 * Returns true if the block passed is a HardOre block
	 * @param b
	 * @return
	 */
	boolean isHardOre(Block b);
	
	/**
	 * Mines the hard ore block at (x,y,z) and returns the resulting ArrayList<ItemStack> drops.<br/>
	 * The replacement block and meta are placed into the world when the ore being mined is fully depleted.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param fortune - fortune enchantment level for miner
	 * @param replacement (optional) - The block to place if completely mined.  Default: air
	 * @param replacementMeta (optional) - the metadata of the block to replace.
	 * @return drops
	 */
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune, Block replacement, int replacementMeta);
	
	/**
	 * Mines the hard ore block at (x,y,z) and returns the resulting ArrayList<ItemStack> drops.<br/>
	 * The replacement block and meta are placed into the world when the ore being mined is fully depleted.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param fortune - fortune enchantment level for miner
	 * @param replacement (optional) - The block to place if completely mined.  Default: air
	 * @param replacementMeta (optional) - the metadata of the block to replace.
	 * @return drops
	 */
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune);
}
