package com.draco18s.hardlib.api.interfaces;

import com.draco18s.hardlib.api.internal.OreFlowerData;

import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;

public interface IHardStones {
	
	/**
	 * Used to verify Gany's surface stones.
	 * @param name
	 * @return if the supplied name can be localized
	 */
	public boolean checkStoneLocalizedName(String name);
	
	/**
	 * Adds a block to be converted into an unstable, damagable stone type.
	 * @param orig The original block
	 * @param origMeta The original metadata used to distinguish it (if any)
	 * @param name The unlocalized name of the original
	 * @param texName The texture string of the original
	 * @return The new unstable stone block
	 */
	public Block addStoneType(Block orig, int origMeta, String name, String texName);

	/**
	 * Adds a block to be converted into an unstable, damagable stone type.
	 * @param orig The original block
	 * @param origMeta The original metadata used to distinguish it (if any)
	 * @param name The unlocalized name of the original
	 * @param texName The texture string of the original
	 * @param colorMult The color to multiply the overlays by
	 * @return The new unstable stone block
	 */
	public Block addStoneType(Block orig, int origMeta, String name, String texName, int colorMult);
	public Block addStoneType(Block orig, int origMeta, String name, String texName, int colorMult, boolean invertColor);
	
	/**
	 * @deprecated use  {@link IHardStones#isUnstableBlock(Block, int)}
	 * @param b Block to check
	 * @return if the block is a registered unstable block
	 */
	@Deprecated
	public boolean isUnstableBlock(Block b);
	
	/**
	 * 
	 * @param b Block to check
	 * @return if the block is a registered unstable block
	 */
	public boolean isUnstableBlock(Block b, int meta);
	
	/**
	 * Returns the block for the Hard Underground stone version of the supplied block (null if non-existent).
	 * Metadata values of this block are:
	 * <ol start="0"><li>Smooth stone</li>
	 * <li>Fractured stone</li>
	 * <li>Broken stone</li>
	 * <li>Cobblestone</li>
	 * <li value="7">Cobblestone (no rock dust when added to world)</li></ol>
	 * @param orig
	 * @param origMeta
	 * @return
	 */
	public Block getReplacement(Block orig, int origMeta);
}
