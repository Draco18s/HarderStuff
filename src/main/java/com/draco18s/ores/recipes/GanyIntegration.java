package com.draco18s.ores.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.RecipesUtil;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.item.ItemDiamondStudAxe;
import com.draco18s.ores.item.ItemDiamondStudHoe;
import com.draco18s.ores.item.ItemDiamondStudPickaxe;
import com.draco18s.ores.item.ItemDiamondStudShovel;

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

public class GanyIntegration {
	private static Block alternateBlock = null;
	private static Block diorite;

	public static void registerAPIRecipes() {
		OresBase.instance.logger.log(Level.INFO,"Registering Gany's Surface recipes");
		OresBase.config.getBoolean("allowPoopFlowers", "GENERAL", true, "Enables Gany's Surface poop blocks' bonemeal action grow ore flowers.");
		ItemStack toolmat;
		if(alternateBlock == null) {
			diorite = Block.getBlockFromName("ganyssurface:18Stones");
			toolmat = new ItemStack(diorite, 1, 3);
		}
		else {
			diorite = alternateBlock;
			toolmat = new ItemStack(diorite, 1, 4);
		}
		RecipesUtil.RemoveRecipe(Items.stone_axe, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_pickaxe, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_shovel, 1, 0, "Hard Ores");
		RecipesUtil.RemoveRecipe(Items.stone_hoe, 1, 0, "Hard Ores");
    	
        GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe), new Object[] {"III", " s ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_axe), new Object[] {"II ", "Is ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_shovel), new Object[] {" I ", " s ", " s ", 's', Items.stick, 'I', toolmat});
        GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), new Object[] {"II ", " s ", " s ", 's', Items.stick, 'I', toolmat});
	}
	
	private static Block findBlock(Iterator iterator, String blockName) {
		String name;
		do {
            if (!iterator.hasNext()) {
                return null;
            }
            name = (String) iterator.next();
            //System.out.println("Iterating over " + name);
        } while (name != blockName);
		return Block.getBlockFromName(name);
	}

	public static void setAlternateBlock(Block block) {
		alternateBlock = block;
	}

	public static Block getDioriteBlock() {
		return diorite;
	}
}
