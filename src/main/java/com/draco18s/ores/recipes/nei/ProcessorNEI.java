package com.draco18s.ores.recipes.nei;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.ores.recipes.nei.SifterNEI.CachedSifterRecipe;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class ProcessorNEI extends TemplateRecipeHandler {

	public ProcessorNEI() {
	}

	@Override
	public String getRecipeName() {
		return "Pressure Packager";
	}

	@Override
	public String getOverlayIdentifier() {
		return "processor";
	}

	@Override
	public String getGuiTexture() {
		return "ores:textures/gui/sifter.png";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object...results) {
		//System.out.println("NEI output ID passed to SifterNEI: " + outputId);
		if ((outputId.equals("item") || outputId.equals("processor")) && (results.length > 0)) {
			//System.out.println("output: " + ((ItemStack)results[0]).getDisplayName());
			loadCraftingRecipes((ItemStack)results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		ItemStack ingredient = HardLibAPI.recipeManager.getProcessorRecipeInput(result);
		if(ingredient!=null && !ItemStack.areItemStacksEqual(ingredient, result)) {
			CachedRecipe recipe = new CachedProcessorRecipe(ingredient);
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
		ItemStack output = HardLibAPI.recipeManager.getProcessorResult(ingredient, false);
		if(output!=null && !ItemStack.areItemStacksEqual(ingredient, output)) {
			CachedRecipe recipe = new CachedProcessorRecipe(ingredient);
			arecipes.add(recipe);
		}
	}
	
    /*public void drawBackground(int recipe) {
    	GL11.glColor4f(1, 1, 1, 1);
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(getGuiTexture()));
    	drawTexturedModalRect(0, 0, 5, 11, 166, 65);
    }

    public void drawForeground(int recipe) {
    	GL11.glColor4f(1, 1, 1, 1);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(getGuiTexture()));
        drawExtras(recipe);
    }*/
	
	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(74, 20, 176, 0, 17, 24, 40, 1);
	}

	public class CachedProcessorRecipe extends TemplateRecipeHandler.CachedRecipe {
		private PositionedStack ingredient;
		private PositionedStack stack;

		public CachedProcessorRecipe(Object _ingredient) {
			super();
			if(_ingredient instanceof ItemStack) {
				ItemStack ingred = ((ItemStack)_ingredient).copy();
				//ingred.stackSize = 64;
				ingred.stackSize = HardLibAPI.recipeManager.getProcessorAmount(ingred);
				//System.out.println("Input stack ("+ingred.getDisplayName()+") size: " + HardLibAPI.recipeManager.getProcessorAmount(ingred));
				//System.out.println("Registered input stack size: " + HardLibAPI.recipeManager.getProcessorRecipeInput(HardLibAPI.recipeManager.getProcessorResult((ItemStack)_ingredient, false)).stackSize);
				ingredient = new PositionedStack(ingred, 66, 2);
				stack = new PositionedStack(HardLibAPI.recipeManager.getProcessorResult((ItemStack)_ingredient, false), 75, 47);
			}
			else if(_ingredient instanceof String) {
				ArrayList<ItemStack> list = OreDictionary.getOres((String)_ingredient);

				if (list.size() > 0) {
					for(ItemStack s:list) {
						s.stackSize = 64;
						s.stackSize = HardLibAPI.recipeManager.getProcessorAmount(s);
						//System.out.println("OreDict stack size: " + s.stackSize);
					}
					ingredient = new PositionedStack(list, 66, 2);
					stack = new PositionedStack(HardLibAPI.recipeManager.getProcessorResult((ItemStack)_ingredient, false), 75, 47);
					//ingredients.add(ingredient);
				}
			} else {
				//System.out.println("Failed to generate recipe for object " + _ingredient.getClass());
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
			if (!(obj instanceof CachedProcessorRecipe)) {
				return false;
			}
			CachedProcessorRecipe other = (CachedProcessorRecipe)obj;
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
