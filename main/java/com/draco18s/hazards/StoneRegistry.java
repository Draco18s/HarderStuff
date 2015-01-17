package com.draco18s.hazards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.draco18s.hazards.block.BlockUnstableStone;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.item.ItemBlockUnstable;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class StoneRegistry {
	private static HashMap <BlockStoneType, BlockStoneType> mapping = new HashMap<BlockStoneType, BlockStoneType>();
	private static ArrayList<int[][]> fracIconOverlays = new ArrayList<int[][]>();
	
	public static void addStoneType(Block orig, int origMeta, String name, String texName) {
		name = name.substring(5);
		BlockStoneType bst = new BlockStoneType(orig, origMeta);
		BlockUnstableStone b = new BlockUnstableStone(name, texName);
		BlockStoneType rst = new BlockStoneType(b, 0);
		//if(orig != Blocks.stone) {
			b.setDropsSelf();
			//alterRecipes(orig, origMeta, b);
		//}
		
		GameRegistry.registerBlock(b, ItemBlockUnstable.class, "unstable_"+name);
		
		UnstableStoneHelper.addSupportBlock(b);
		
		GameRegistry.addSmelting(new ItemStack(b, 1, 3), new ItemStack(b, 1, 0), 0.1f);
		
		mapping.put(bst, rst);
		int id = OreDictionary.getOreID(new ItemStack(orig, 1, origMeta));
		String orename = "stone";
		if(id != -1) {
			orename = OreDictionary.getOreName(id);
		}
		OreDictionary.registerOre(orename, new ItemStack(b, 1, 0));
		OreDictionary.registerOre("cobblestone", new ItemStack(b, 1, 3));
	}
	
	public static Block getReplacement(Block b, int m) {
		BlockStoneType r = mapping.get(new BlockStoneType(b, m));
		if(r == null) return null;
		return r.stone;
	}
	
	public static Block getReplacement(int id, int m) {
		Block b = Block.getBlockById(id);
		BlockStoneType r = mapping.get(new BlockStoneType(b, m));
		if(r == null) return null;
		return r.stone;
	}
	
	public static int getReplacementId(Block b, int m) {
		BlockStoneType r = mapping.get(new BlockStoneType(b, m));
		if(r == null) return -1;
		return r.blockID;
	}
	
	public static int getReplacementId(int id, int m) {
		Block b = Block.getBlockById(id);
		BlockStoneType r = mapping.get(new BlockStoneType(b, m));
		if(r == null) return -1;
		return r.blockID;
	}
	
	public static boolean isUnstableBlock(Block b) {
		for(BlockStoneType bst : mapping.keySet()) {
			if(bst.stone == b) return true;
		}
		return false;
	}
	
	public static int[][] getIconOverlay(int index) {
		if(index >= fracIconOverlays.size()) {
			return null;
		}
		return fracIconOverlays.get(index);
	}
	
	public static void setIconOverlay(int[][] data) {
		fracIconOverlays.add(data);
	}
}
