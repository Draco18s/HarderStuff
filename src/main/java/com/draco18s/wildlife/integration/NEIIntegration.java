package com.draco18s.wildlife.integration;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIIntegration {
	public static void registerNEIRecipes() {
		TemplateRecipeHandler handler = new RackNEI();
		API.registerUsageHandler(handler);
		API.registerRecipeHandler(handler);
		//API.registerGuiOverlay(GuiContainerSifter.class, "millstone");
		//API.registerGuiOverlayHandler(GuiContainerSifter.class, new DefaultOverlayHandler(), "millstone");
	}
}
