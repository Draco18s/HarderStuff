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
	public void addSiftRecipe(String input, int stackSize, ItemStack output, boolean registerOutput);
	public void addSiftRecipe(String input, int stackSize, ItemStack output);

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
	 * @deprecated - Use {@link IHardOres#addOreFlowerData(Block, int, OreFlowerData)}
	 * Registers an ore with its matching indicator plant
	 * @param ore
	 * @param data
	 */
	@Deprecated
	public void addOreFlowerData(Block ore, OreFlowerData data);
	
	/**
	 * @deprecated - Use {@link IHardOres#addOreFlowerData(Block, int, OreFlowerData)}
	 * Registers an ore (with metadata) with its matching indicator plant
	 * @param ore
	 * @param meta
	 * @param data
	 */
	@Deprecated
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data);

	/**
	 * Used to ensure that the sluice has enough valid results.  If not, additional gravel is added.
	 * @return
	 */
	public boolean isSluiceSetFull();

	/**
	 * @deprecated - Use {@link IHardOres#getOreList()}
	 * Gets the hash of all of the registered ore blocks for flower data.
	 * @return
	 */
	@Deprecated
	public Map<BlockWrapper, OreFlowerData> getOreList();

	/**
	 * Determines whether or not a given item stack can be inserted into the given TE.
	 * @param inventory
	 * @param stack
	 * @return
	 */
	public boolean canInsert(TileEntity inventory, ItemStack stack);

	/**
	 * Returns the number items needed for the sifter recipes (minimum input stack size).
	 * @param itemStack
	 * @return
	 */
	public int getSiftAmount(ItemStack itemStack);

	/**
	 * Gets the sifter input (recipe) item for a given output.  Used by NEI integration.
	 * @param key
	 * @return
	 */
	public ItemStack getSiftRecipeInput(ItemStack key);
	
	/**
	 * Gets the item stack output for a given item stack input.
	 * @param itemstack1
	 * @param checkStackSize - whether or not we care if we have enough input or we just need to know if the input stack is valid
	 * @return
	 */
	public ItemStack getSiftResult(ItemStack itemstack1, boolean checkStackSize);

	/**
	 * Returns the item stack result from milling.
	 * @param itemStack
	 * @return
	 */
	public ItemStack getMillResult(ItemStack itemStack);

	/**
	 * Gets the size of the input item stack for the pressure packager.
	 * @param itemStack
	 * @return
	 */
	public int getProcessorAmount(ItemStack itemStack);
	
	/**
	 * Gets the pressure packager input (recipe) item for a given output.  Used by NEI integration.
	 * @param result
	 * @return
	 */
	public ItemStack getProcessorRecipeInput(ItemStack result);
	
	/**
	 * Gets the item stack output for a given item stack input.
	 * @param key
	 * @param checkSize - whether or not we care if we have enough input or we just need to know if the input stack is valid
	 * @return
	 */
	public ItemStack getProcessorResult(ItemStack key, boolean checkSize);

	/**
	 * Returns a random sluice output (ore block) based on the input (sand, gravel, dirt). 
	 * @param rand
	 * @param item
	 * @return
	 */
	public Block getRandomSluiceResult(Random rand, Item item);
	
	/**
	 * Gets the input item for a given milling result.  Used by NEI integration.
	 * @param output
	 * @return
	 */
	public ArrayList<ItemStack> getMillRecipeInput(ItemStack output);
}
