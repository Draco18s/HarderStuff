package com.draco18s.hardlib.api;

import net.minecraft.block.Block;

import com.draco18s.hardlib.api.interfaces.*;

public class HardLibAPI {
	/**
	 * Harder Ores machine recipes
	 */
	public static IHardRecipes recipeManager = null;
	/**
	 * Harder Underground stone registration
	 */
	public static IHardStones stoneManager = null;
	/**
	 * Harder Ores ore tracking/registration
	 */
	public static IHardOres oreManager = null;
	/**
	 * Harder Wildlife crop growing conditions
	 */
	public static IHardCrops cropManager = null;
	/**
	 * Harder Wildlife auto-planting registration
	 */
	public static IAutoPlanter plantManager = null;
	/**
	 * Harder Wildlife animal handling
	 */
	public static IAnimals animalManager = null;
}
