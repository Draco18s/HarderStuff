package com.draco18s.wildlife.integration;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.wildlife.WildlifeBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RackNEI extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Tanning Rack";
	}

	@Override
	public String getOverlayIdentifier() {
		return "Tanning Rack";
	}

	@Override
	public String getGuiTexture() {
		return "wildlife:textures/gui/rack.png";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object...results) {
		//System.out.println("NEI output ID passed to RackNEI: " + outputId + ", len " + results.length);
		if ((outputId.equals("item") || outputId.equals("tanning")) && (results.length > 0)) {
			//System.out.println("output: " + ((ItemStack)results[0]).getDisplayName());
			loadCraftingRecipes((ItemStack)results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(result.getItem() == Items.leather) {
			CachedRecipe recipe = new CachedSifterRecipe(new ItemStack(WildlifeBase.itemRawLeather));
			arecipes.add(recipe);
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		//System.out.println("NEI input ID passed to RackNEI: " + inputId);
		if(ingredients.length >= 1 && ingredients[0] instanceof ItemStack)
			loadUsageRecipes((ItemStack) ingredients[0]);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient.getItem() == WildlifeBase.itemRawLeather) {
			CachedRecipe recipe = new CachedSifterRecipe(ingredient);
			arecipes.add(recipe);
		}
	}

	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 20, 5, 11, 166, 65);
	}

	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(59, 21, 176, 0, 47, 64, 100, 1);
	}

	public class CachedSifterRecipe extends TemplateRecipeHandler.CachedRecipe {
		private PositionedStack ingredient;
		private PositionedStack stack;

		public CachedSifterRecipe(Object _ingredient) {
			super();
			if(_ingredient instanceof ItemStack) {
				ItemStack ingred = (ItemStack)_ingredient;
				ingredient = new PositionedStack(ingred, 43, 45);
				stack = new PositionedStack(new ItemStack(Items.leather), 107, 45);
			}
			else {
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
	
	@Override
	public int recipiesPerPage() {
		return 1;
	}
}
