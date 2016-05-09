package com.draco18s.hazards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.api.interfaces.IHardStones;
import com.draco18s.hazards.block.BlockUnstableStone;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.integration.IC2Integration;
import com.draco18s.hazards.item.ItemBlockUnstable;
import com.draco18s.wildlife.WildlifeBase;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class StoneRegistry implements IHardStones {
	private static HashMap <BlockStoneType, BlockStoneType> mapping = new HashMap<BlockStoneType, BlockStoneType>();
	private static ArrayList<int[][]> fracIconOverlays = new ArrayList<int[][]>();
	
	public Block addStoneType(Block orig, int origMeta, String name, String texName) {
		return addStoneType(orig, origMeta, name, texName, 0x737373);
	}
	
	public boolean checkStoneLocalizedName(String name) {
		//UndergroundBase.logger.log(Level.INFO, "Checking " + name + " with " + StatCollector.translateToLocal(name));
		return !(StatCollector.translateToLocal(name).equals(name));
	}
	
	public Block addStoneType(Block orig, int origMeta, String name, String texName, int colorMult) {
		return addStoneType(orig, origMeta, name, texName, colorMult, false);
	}
	
	public Block addStoneType(Block orig, int origMeta, String name, String texName, int colorMult, boolean invertColor) {
		if(orig == null) {
			RuntimeException e = new RuntimeException("Attempted to register a null block ("+name+") as stone");
			e.printStackTrace();
			return null;
		}
		UndergroundBase.logger.log(Level.INFO, "Registering unstable_" + name + " with texture " + texName);
		
		BlockUnstableStone b = new BlockUnstableStone(name, texName, colorMult, invertColor);
		float hard = 1.5f;
		float resist = 10.0f;
		try {
			hard = orig.getBlockHardness(null, 0, 0, 0);
			resist = orig.getExplosionResistance(null);
		}
		catch(NullPointerException e) {	}
		b.setHardness(hard);
		b.setResistance(resist);
		b.setStepSound(orig.stepSound);
		b.setHarvestLevel(orig.getHarvestTool(origMeta), orig.getHarvestLevel(origMeta));

		if(name.indexOf("tile") >= 0) {
			name = name.substring(5);
		}
		GameRegistry.registerBlock(b, ItemBlockUnstable.class, "unstable_"+name);
		
		UnstableStoneHelper.addSupportBlock(b);
		
		GameRegistry.addSmelting(new ItemStack(b, 1, 3), new ItemStack(b, 1, 0), 0.1f);

		BlockStoneType bst = new BlockStoneType(orig, origMeta);
		BlockStoneType rst = new BlockStoneType(b, 0);
		//if(orig != Blocks.stone) {
			b.setDropsSelf();
			//alterRecipes(orig, origMeta, b);
		//}
		mapping.put(bst, rst);
		
		/*int id = OreDictionary.getOreID(new ItemStack(orig, 1, origMeta));
		String orename = "stone";
		if(id != -1) {
			orename = OreDictionary.getOreName(id);
		}
		OreDictionary.registerOre(orename, new ItemStack(b, 1, 0));*/
		OreDictionary.registerOre("cobblestone", new ItemStack(b, 1, 3));
		OreDictionary.registerOre("stone", new ItemStack(b, 1, 0));

		if(Loader.isModLoaded("IC2")){
			IC2Integration.addMaceratorRecipe(new ItemStack(b, 1, 3), null, new ItemStack(Blocks.sand));
		}
		
		return b;
	}
	
	public Block getReplacement(Block b, int m) {
		BlockStoneType r = mapping.get(new BlockStoneType(b, m));
		if(r == null) return null;
		return r.stone;
	}
	
	public boolean isUnstableBlock(Block b) {
		//return mapping.containsValue(b);
		for(BlockStoneType bst : mapping.values()) {
			if(bst.stone == b) return true;
		}
		return false;
	}
	
	public boolean isUnstableBlock(Block b, int meta) {
		BlockStoneType bst = new BlockStoneType(b, meta);
		return mapping.containsValue(bst);
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
