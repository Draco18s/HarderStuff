package com.draco18s.ores.recipes;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.EnumOreType;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MekanismIntegration {

	public static void addMekanismRecipies() {
		OresBase.instance.logger.log(Level.INFO,"Integrating HardOres with Mekanism");
		
		ItemStack rawOreIn = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		ItemStack dustOut = new ItemStack(OresBase.smallDusts, 3, EnumOreType.IRON.value);
		
		addEnrichmentChamberRecipe(rawOreIn, dustOut);
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		dustOut.setItemDamage(EnumOreType.GOLD.value);
		addEnrichmentChamberRecipe(rawOreIn, dustOut);
		rawOreIn.setItemDamage(EnumOreType.TIN.value);
		dustOut.setItemDamage(EnumOreType.TIN.value);
		addEnrichmentChamberRecipe(rawOreIn, dustOut);
		rawOreIn.setItemDamage(EnumOreType.COPPER.value);
		dustOut.setItemDamage(EnumOreType.COPPER.value);
		addEnrichmentChamberRecipe(rawOreIn, dustOut);
		rawOreIn.setItemDamage(EnumOreType.OSMIUM.value);
		dustOut.setItemDamage(EnumOreType.OSMIUM.value);
		addEnrichmentChamberRecipe(rawOreIn, dustOut);
		
		rawOreIn = new ItemStack(OresBase.oreIron);
		ItemStack nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			addEnrichmentChamberRecipe(rawOreIn, nuggetOut);
		}
		rawOreIn = new ItemStack(OresBase.oreGold);
		nuggetOut.setItemDamage(EnumOreType.GOLD.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			addEnrichmentChamberRecipe(rawOreIn, nuggetOut);
		}
		rawOreIn = new ItemStack(OresBase.oreTin);
		nuggetOut.setItemDamage(EnumOreType.TIN.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			addEnrichmentChamberRecipe(rawOreIn, nuggetOut);
		}
		rawOreIn = new ItemStack(OresBase.oreCopper);
		nuggetOut.setItemDamage(EnumOreType.COPPER.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			addEnrichmentChamberRecipe(rawOreIn, nuggetOut);
		}
		rawOreIn = new ItemStack(OresBase.oreOsmium);
		nuggetOut.setItemDamage(EnumOreType.OSMIUM.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			addEnrichmentChamberRecipe(rawOreIn, nuggetOut);
		}
	}

	public static void addEnrichmentChamberRecipe(ItemStack input, ItemStack output) {
		if(input != null && output != null) {
			try {
				Class recipeClass = Class.forName("mekanism.common.recipe.RecipeHandler");
				Method m = recipeClass.getMethod("addEnrichmentChamberRecipe", ItemStack.class, ItemStack.class);
				m.invoke(null, input, output);
			} catch(Exception e) {
				OresBase.instance.logger.log(Level.ERROR,"Error while adding Mekanism recipe: " + e.getMessage());
			}
		}
	}
}
