package com.draco18s.hazards.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hazards.StoneRegistry;
import com.draco18s.hazards.UndergroundBase;
import com.draco18s.hazards.block.helper.GasFlowHelper;

import cpw.mods.fml.common.event.FMLInterModComms;
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

public class ModStoneRegistration {

	public static void registerGany() {
		if(UndergroundBase.doStoneReplace) {
			Block oneEightStones = Block.getBlockFromName("ganyssurface:18Stones");
			Block unstableDiorite = null;
			if(HardLibAPI.stoneManager.checkStoneLocalizedName("tile.ganyssurface.18Stones1")) {
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 1, "tile.ganyssurface.18Stones1", "minecraft:stone_granite",0x592512);//lighter: d29e8e, 0x9f867b, 614135
				//StoneRegistry.addStoneType(oneEightStones, 2, "granite_smooth");
				unstableDiorite = HardLibAPI.stoneManager.addStoneType(oneEightStones, 3, "tile.ganyssurface.18Stones3", "minecraft:stone_diorite");
				//StoneRegistry.addStoneType(oneEightStones, 4, "diorite_smooth");
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 5, "tile.ganyssurface.18Stones5", "minecraft:stone_andesite");
				//StoneRegistry.addStoneType(oneEightStones, 6, "andesite_smooth");
				Block b = HardLibAPI.stoneManager.addStoneType(oneEightStones, 7, "tile.ganyssurface.18Stones7", "ganyssurface:stone_basalt", 0xBFBFBF, true);
				if(b != null) {
					b.setBlockName("ganyssurface.basalt");
				}
			}
			else if(HardLibAPI.stoneManager.checkStoneLocalizedName("tile.ganyssurface.18Stones_1")) {
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 1, "tile.ganyssurface.18Stones_1", "minecraft:stone_granite",0x592512);//lighter: d29e8e, 0x9f867b, 614135
				//StoneRegistry.addStoneType(oneEightStones, 2, "granite_smooth");
				unstableDiorite = HardLibAPI.stoneManager.addStoneType(oneEightStones, 3, "tile.ganyssurface.18Stones_3", "minecraft:stone_diorite");
				//StoneRegistry.addStoneType(oneEightStones, 4, "diorite_smooth");
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 5, "tile.ganyssurface.18Stones_5", "minecraft:stone_andesite");
				//StoneRegistry.addStoneType(oneEightStones, 6, "andesite_smooth");
				Block b = HardLibAPI.stoneManager.addStoneType(oneEightStones, 7, "tile.ganyssurface.18Stones_7", "ganyssurface:stone_basalt", 0xBFBFBF, true);
				if(b != null) {
					b.setBlockName("ganyssurface.basalt");
				}
			}
			else {
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 1, "tile.minecraft.granite", "minecraft:stone_granite",0x592512);//lighter: d29e8e, 0x9f867b, 614135
				//StoneRegistry.addStoneType(oneEightStones, 2, "granite_smooth");
				unstableDiorite = HardLibAPI.stoneManager.addStoneType(oneEightStones, 3, "tile.minecraft.diorite", "minecraft:stone_diorite");
				//StoneRegistry.addStoneType(oneEightStones, 4, "diorite_smooth");
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 5, "tile.minecraft.andesite", "minecraft:stone_andesite");
				//StoneRegistry.addStoneType(oneEightStones, 6, "andesite_smooth");
				Block b = HardLibAPI.stoneManager.addStoneType(oneEightStones, 7, "tile.minecraft.basalt", "ganyssurface:stone_basalt", 0xBFBFBF, true);
				if(b != null) {
					b.setBlockName("ganyssurface.basalt");
				}
			}
			//StoneRegistry.addStoneType(oneEightStones, 8, "basalt_smooth");
			
			oneEightStones = Block.getBlockFromName("ganyssurface:basalt");
			if(oneEightStones != null)
				HardLibAPI.stoneManager.addStoneType(oneEightStones, 0, "tile.ganyssurface.basalt", "ganyssurface:stone_basalt", 0xBFBFBF, true);
			
			if(unstableDiorite != null)
				FMLInterModComms.sendMessage("HarderOres", "RR-special-stone-update-gany", new ItemStack(unstableDiorite));
		}
		if(UndergroundBase.doGasSeeping) {
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_acacia"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_birch"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_dark_oak"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_jungle"));
			GasFlowHelper.addWoodenDoor(Block.getBlockFromName("ganyssurface:door_spruce"));
		}
	}

	public static void registerGeostrata() {
	}

	public static void registerEtFuturum() {
		Block block = Block.getBlockFromName("etfuturum:stone");
		block = HardLibAPI.stoneManager.addStoneType(block, 3, "tile.etfuturum.stone_diorite.name", "minecraft:stone_diorite",0xBFBFBF,true);
		
		FMLInterModComms.sendMessage("HarderOres", "RR-special-stone-update-futurum", new ItemStack(block));
		block = HardLibAPI.stoneManager.addStoneType(block, 1, "tile.etfuturum.stone_granite.name", "minecraft:stone_granite",0x592512);
		block = HardLibAPI.stoneManager.addStoneType(block, 5, "tile.etfuturum.stone_andesite.name", "minecraft:stone_andesite");
	}
	
	public static void registerChisel() {
		Block block = Block.getBlockFromName("chisel:diorite");
		block = HardLibAPI.stoneManager.addStoneType(block, 1, "tile.chisel.diorite", "chisel:diorite/diorite",0xBFBFBF,true);
		
		FMLInterModComms.sendMessage("HarderOres", "RR-special-stone-update-chisel", new ItemStack(block));
		
		block = Block.getBlockFromName("chisel:granite");
		HardLibAPI.stoneManager.addStoneType(block, 1, "tile.chisel.granite", "chisel:granite/granite",0x592512);
		block = Block.getBlockFromName("chisel:andesite");
		HardLibAPI.stoneManager.addStoneType(block, 1, "tile.chisel.andesite", "chisel:andesite/andesite");
		block = Block.getBlockFromName("chisel:limestone");
		HardLibAPI.stoneManager.addStoneType(block, 1, "tile.chisel.limestone", "chisel:limestone/limestone");
		block = Block.getBlockFromName("chisel:marble");
		HardLibAPI.stoneManager.addStoneType(block, 1, "tile.chisel.marble", "chisel:marble/marble");
	}
}
