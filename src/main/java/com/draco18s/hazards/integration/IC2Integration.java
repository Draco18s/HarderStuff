package com.draco18s.hazards.integration;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import com.draco18s.hazards.UndergroundBase;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

public class IC2Integration {
	private static ArrayList<RecipeStore> storage = new ArrayList<RecipeStore>();
	
	public static void addMaceratorRecipe(ItemStack input, NBTTagCompound metadata, ItemStack output) {
		storage.add(new RecipeStore(input, metadata, output));
	}
	
	public static void registerMaceRatorRecipes() {
		UndergroundBase.instance.logger.log(Level.INFO, "Registering Maceratpr recipies");
		for(RecipeStore rs : storage) {
			RecipeInputItemStack recipeInput = new RecipeInputItemStack(rs.in);
			Recipes.macerator.addRecipe(recipeInput, rs.dat, rs.out);
		}
		storage.clear();
	}
	
	private static class RecipeStore {
		public ItemStack in;
		public NBTTagCompound dat;
		public ItemStack out;
		
		private RecipeStore(ItemStack input, NBTTagCompound metadata, ItemStack output) {
			in = input;
			dat = metadata;
			out = output;
		}
	}
}
