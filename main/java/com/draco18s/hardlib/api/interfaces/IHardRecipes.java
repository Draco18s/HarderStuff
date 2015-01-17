package com.draco18s.hardlib.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.draco18s.hardlib.api.internal.OreFlowerData;

public interface IHardRecipes {
	/*
	 * The sifter "autocrafts" tiny dust piles to large dust piles.  Typically the crafting table recipe will be a
	 * full grid (9 tiny dusts) and craft into 1 large dust and the sifter will sift 8 to 1.
	 * @param input ItemStack including metadata and size
	 * @param output ItemStack including metadata and size
	 */
	public void addSiftRecipe(ItemStack input, ItemStack output);

	/*
	 * The millstone will grind "raw" materials down into "dust" materials, typically tiny dust piles.
	 * @param input ItemStack to be ground
	 */
	public void addMillRecipe(ItemStack input, ItemStack output);

	public void addSluiceRecipe(Block output);
	
	public void addOreFlowerData(Block ore, OreFlowerData data);

	public boolean isSluiceSetFull();
}
