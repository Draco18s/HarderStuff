package com.draco18s.ores.recipes;

import com.draco18s.ores.client.GuiContainerProcessor;
import com.draco18s.ores.client.GuiContainerSifter;
import com.draco18s.ores.recipes.nei.*;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIIntegration {
	public static void registerNEIRecipes() {
		TemplateRecipeHandler handler = new SifterNEI();
		API.registerUsageHandler(handler);
		API.registerRecipeHandler(handler);
		API.registerGuiOverlay(GuiContainerSifter.class, "sifter");
		API.registerGuiOverlayHandler(GuiContainerSifter.class, new DefaultOverlayHandler(), "sifter");

		handler = new ProcessorNEI();
		API.registerUsageHandler(handler);
		API.registerRecipeHandler(handler);
		API.registerGuiOverlay(GuiContainerProcessor.class, "processor");
		API.registerGuiOverlayHandler(GuiContainerProcessor.class, new DefaultOverlayHandler(), "processor");

		handler = new MillNEI();
		API.registerUsageHandler(handler);
		API.registerRecipeHandler(handler);
	}
}
