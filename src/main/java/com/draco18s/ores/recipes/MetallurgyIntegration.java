package com.draco18s.ores.recipes;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.EnumOreType;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MetallurgyIntegration {
	public static void addMetallurgyRecipes() {
		OresBase.instance.logger.log(Level.INFO,"Integrating HardOres with Mekanism");
		
		ItemStack dustOut = new ItemStack(OresBase.smallDusts, 3, EnumOreType.IRON.value);
		int id = Item.getIdFromItem(OresBase.oreChunks);
		
		addCrusherRecipe(id, EnumOreType.IRON.value, dustOut);
		dustOut.setItemDamage(EnumOreType.GOLD.value);
		addCrusherRecipe(id, EnumOreType.GOLD.value, dustOut);
		dustOut.setItemDamage(EnumOreType.TIN.value);
		addCrusherRecipe(id, EnumOreType.TIN.value, dustOut);
		dustOut.setItemDamage(EnumOreType.COPPER.value);
		addCrusherRecipe(id, EnumOreType.COPPER.value, dustOut);
		dustOut.setItemDamage(EnumOreType.SILVER.value);
		addCrusherRecipe(id, EnumOreType.SILVER.value, dustOut);
		
		ItemStack nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		id = Item.getIdFromItem(Item.getItemFromBlock(OresBase.oreIron));
		for(int i=0; i<15;i++) {
			nuggetOut.stackSize = i + (i/5)*2;
			addCrusherRecipe(id, i, nuggetOut);
		}
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.GOLD.value);
		id = Item.getIdFromItem(Item.getItemFromBlock(OresBase.oreGold));
		for(int i=0; i<15;i++) {
			nuggetOut.stackSize = i + (i/5)*2;
			addCrusherRecipe(id, i, nuggetOut);
		}
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.TIN.value);
		id = Item.getIdFromItem(Item.getItemFromBlock(OresBase.oreTin));
		for(int i=0; i<15;i++) {
			nuggetOut.stackSize = i + (i/5)*2;
			addCrusherRecipe(id, i, nuggetOut);
		}
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.COPPER.value);
		id = Item.getIdFromItem(Item.getItemFromBlock(OresBase.oreCopper));
		for(int i=0; i<15;i++) {
			nuggetOut.stackSize = i + (i/5)*2;
			addCrusherRecipe(id, i, nuggetOut);
		}
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.SILVER.value);
		id = Item.getIdFromItem(Item.getItemFromBlock(OresBase.oreSilver));
		for(int i=0; i<15;i++) {
			nuggetOut.stackSize = i + (i/5)*2;
			addCrusherRecipe(id, i, nuggetOut);
		}
	}
	
	public static void addCrusherRecipe(int itemID, int metadata, ItemStack result) {
		try {
			Class crusherRecipes = Class.forName("shadow.mods.metallurgy.BC_CrusherRecipes");
			Method addCrushing = crusherRecipes.getDeclaredMethod("addCrushing", new Class[] {Integer.TYPE, Integer.TYPE, ItemStack.class});
			addCrushing.invoke(crusherRecipes, new Object[] {(Integer)itemID, (Integer)metadata, result});
		} catch (Exception e) {
			OresBase.instance.logger.log(Level.ERROR,"Metallurgy API: Failed to add crusher recipe" + e);
		}
	}
}
