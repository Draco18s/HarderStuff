package com.draco18s.hazards.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.draco18s.hazards.StoneRegistry;
import com.draco18s.hazards.UndergroundBase;
import com.draco18s.hazards.block.helper.GasFlowHelper;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class GanyStoneRegistration {

	public static void registerAPIRecipes() {
		if(UndergroundBase.doStoneReplace) {
			Block oneEightStones = Block.getBlockFromName("ganyssurface:18Stones");
			StoneRegistry.addStoneType(oneEightStones, 1, "granite", "minecraft:stone_granite");
			//StoneRegistry.addStoneType(oneEightStones, 2, "granite_smooth");
			StoneRegistry.addStoneType(oneEightStones, 3, "diorite", "minecraft:stone_diorite");
			//StoneRegistry.addStoneType(oneEightStones, 4, "diorite_smooth");
			StoneRegistry.addStoneType(oneEightStones, 5, "andesite", "minecraft:stone_andesite");
			//StoneRegistry.addStoneType(oneEightStones, 6, "andesite_smooth");
			StoneRegistry.addStoneType(oneEightStones, 7, "basalt", "ganyssurface:stone_basalt");
			//StoneRegistry.addStoneType(oneEightStones, 8, "basalt_smooth");
		}
		if(UndergroundBase.doGasSeeping) {
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_acacia"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_birch"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_dark_oak"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_jungle"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_spruce"));
		}
	}
}
