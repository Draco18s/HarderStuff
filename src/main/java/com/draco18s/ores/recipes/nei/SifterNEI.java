package com.draco18s.ores.recipes.nei;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.draco18s.hardlib.api.HardLibAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class SifterNEI extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Sifter";
	}

	@Override
	public String getOverlayIdentifier() {
		return "sifter";
	}

	@Override
	public String getGuiTexture() {
		return "ores:textures/gui/sifter.png";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object...results) {
		//System.out.println("NEI output ID passed to SifterNEI: " + outputId);
		if ((outputId.equals("item") || outputId.equals("sifter")) && (results.length > 0)) {
			//System.out.println("output: " + ((ItemStack)results[0]).getDisplayName());
			loadCraftingRecipes((ItemStack)results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		ItemStack ingredient = HardLibAPI.recipeManager.getSiftRecipeInput(result);
		if(ingredient!=null && !ItemStack.areItemStacksEqual(ingredient, result)) {
			CachedRecipe recipe = new CachedSifterRecipe(ingredient);
			arecipes.add(recipe);
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		//System.out.println("NEI input ID passed to SifterNEI: " + inputId);
		if(ingredients.length >= 1 && ingredients[0] instanceof ItemStack)
			loadUsageRecipes((ItemStack) ingredients[0]);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		ItemStack output = HardLibAPI.recipeManager.getSiftResult(ingredient, false);
		if(output!=null && !ItemStack.areItemStacksEqual(ingredient, output)) {
			//ingredient.stackSize = HardLibAPI.recipeManager.getSiftRecipeInput().stackSize;
			CachedRecipe recipe = new CachedSifterRecipe(HardLibAPI.recipeManager.getSiftRecipeInput(output));
			arecipes.add(recipe);
		}
	}

	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(74, 20, 176, 0, 17, 24, 40, 1);
	}

	public class CachedSifterRecipe extends TemplateRecipeHandler.CachedRecipe {
		private PositionedStack ingredient;
		private PositionedStack stack;

		public CachedSifterRecipe(Object _ingredient) {
			super();
			if(_ingredient instanceof ItemStack) {
				ItemStack ingred = ((ItemStack)_ingredient).copy();
				ingred.stackSize = 64;
				ingred.stackSize = HardLibAPI.recipeManager.getSiftAmount(ingred);
				//System.out.println("Input stack ("+ingred.getDisplayName()+") size: " + HardLibAPI.recipeManager.getSiftAmount(ingred));
				//System.out.println("Registered input stack size: " + HardLibAPI.recipeManager.getSiftRecipeInput(HardLibAPI.recipeManager.getSiftResult((ItemStack)_ingredient, false)).stackSize);
				//ingred.stackSize = 1;
				ingredient = new PositionedStack(ingred, 66, 2);
				stack = new PositionedStack(HardLibAPI.recipeManager.getSiftResult((ItemStack)_ingredient, false), 75, 47);
			}
			else if(_ingredient instanceof String) {
				ArrayList<ItemStack> list = OreDictionary.getOres((String)_ingredient);

				if (list.size() > 0) {
					//for(ItemStack s:list) {
						//s.stackSize = 64;
						//s.stackSize = HardLibAPI.recipeManager.getSiftAmount(s);
						//System.out.println("OreDict stack size: " + s.stackSize);
					//}
					ingredient = new PositionedStack(list, 66, 2);
					stack = new PositionedStack(HardLibAPI.recipeManager.getSiftResult((ItemStack)_ingredient, false), 75, 47);
					//ingredients.add(ingredient);
				}
			} else {
				ingredient = null;
			}
		}

		@Override
		public PositionedStack getIngredient() {
			return ingredient;
		}

		@Override
		public PositionedStack getResult() {
			return stack;
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof CachedSifterRecipe)) {
				return false;
			}
			CachedSifterRecipe other = (CachedSifterRecipe)obj;
			if (ingredient == null) {
				if (other.ingredient != null)
					return false;
			}
			else if (ingredient.item == null) {
				if (other.ingredient.item != null)
					return false;
			}
			else if (!ItemStack.areItemStacksEqual(ingredient.item, other.ingredient.item)) {
				return false;
			}
			if (stack == null) {
				if (other.stack != null)
					return false;
			}
			else if (stack.item == null) {
				if (other.stack.item != null)
					return false;
			}
			else if (!ItemStack.areItemStacksEqual(stack.item, other.stack.item)) {
				return false;
			}
			return true;
		}
	}
}
