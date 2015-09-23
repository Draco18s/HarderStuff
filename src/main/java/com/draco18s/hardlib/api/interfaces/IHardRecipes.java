package com.draco18s.hardlib.api.interfaces;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;

public interface IHardRecipes {
	/**
	 * The sifter "autocrafts" tiny dust piles to large dust piles.  Typically the crafting table recipe will be a
	 * full grid (9 tiny dusts) and craft into 1 large dust and the sifter will sift 8 to 1.
	 * @param input ItemStack including metadata and size
	 * @param output ItemStack including metadata and size
	 * @param registerOutput Pass true to register the output stack as a 1:1 sift 
	 */
	public void addSiftRecipe(ItemStack input, ItemStack output, boolean registerOutput);
	public void addSiftRecipe(ItemStack input, ItemStack output);

	/**
	 * The millstone will grind "raw" materials down into "dust" materials, typically tiny dust piles.
	 * @param input ItemStack to be ground
	 */
	public void addMillRecipe(ItemStack input, ItemStack output);

	/**
	 * The sluice "searches" the nearby chunks for possible ores, this registers a block as being an ore that can drop.
	 * @param output
	 */
	public void addSluiceRecipe(Block output);
	
	/**
	 * The ore processor exists to convert ore chunks into "ore blocks" to support industry mods like RotaryCraft
	 * that can't handle item input.
	 * @param input
	 * @param output
	 * @param registerOutput Pass true to register the output stack as a 1:1 process
	 */
	public void addProcessorRecipe(ItemStack input, ItemStack output);
	
	/**
	 * Registers an ore with its matching indicator plant
	 * @param ore
	 * @param data
	 */
	@Deprecated
	public void addOreFlowerData(Block ore, OreFlowerData data);
	
	/**
	 * Registers an ore (with metadata) with its matching indicator plant
	 * @param ore
	 * @param meta
	 * @param data
	 */
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data);

	public boolean isSluiceSetFull();

	public Map<BlockWrapper, OreFlowerData> getOreList();

	public boolean canInsert(TileEntity inventory, ItemStack stack);

	public int getSiftAmount(ItemStack itemStack);

	public ItemStack getSiftRecipeInput(ItemStack key);
	
	public ItemStack getSiftResult(ItemStack itemstack1, boolean checkStackSize);

	public ItemStack getMillResult(ItemStack itemStack);

	public int getProcessorAmount(ItemStack itemStack);
	
	public ItemStack getProcessorRecipeInput(ItemStack result);
	
	public ItemStack getProcessorResult(ItemStack key, boolean checkSize);

	public Block getRandomSluiceResult(Random rand, Item item);
	
	public ArrayList<ItemStack> getMillRecipeInput(ItemStack output);
}
