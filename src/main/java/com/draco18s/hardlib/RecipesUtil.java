package com.draco18s.hardlib;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipesUtil {
	public static void RemoveRecipe(Item resultItem, int stacksize, int meta, String modID) {
		ItemStack resultStack = new ItemStack(resultItem, stacksize, meta);
		ItemStack recipeResult = null;
		ArrayList recipes = (ArrayList) CraftingManager.getInstance().getRecipeList();
		for (int scan = 0; scan < recipes.size(); scan++) {
			 IRecipe tmpRecipe = (IRecipe) recipes.get(scan);
			 /*if (tmpRecipe instanceof ShapedRecipes) {
				 ShapedRecipes recipe = (ShapedRecipes)tmpRecipe;
				 recipeResult = recipe.getRecipeOutput();
			 }
			 else if (tmpRecipe instanceof ShapelessRecipes) {
				 ShapelessRecipes recipe = (ShapelessRecipes)tmpRecipe;
				 recipeResult = recipe.getRecipeOutput();
			 }*/
			 recipeResult = tmpRecipe.getRecipeOutput();
			 if (ItemStack.areItemStacksEqual(resultStack, recipeResult)) {
				 System.out.println(modID + " Removed Recipe: " + recipes.get(scan) + " -> " + recipeResult);
				 recipes.remove(scan);
			 }
		}
	}
	
	public static void RemoveSmelting(Item resultItem, int stacksize, int meta, String modID) {
		ItemStack resultStack = new ItemStack(resultItem, stacksize, meta);
		ItemStack recipeResult = null;
		Map recipes = FurnaceRecipes.smelting().getSmeltingList();
		for (int scan = 0; scan < recipes.size(); scan++) {
			 ItemStack tmpRecipe = (ItemStack) recipes.get(scan);
			 if (ItemStack.areItemStacksEqual(resultStack, recipeResult)) {
				 System.out.println(modID + " Removed Smelting: " + recipes.get(scan) + " -> " + recipeResult);
				 recipes.remove(scan);
			 }
		}
	}
}
