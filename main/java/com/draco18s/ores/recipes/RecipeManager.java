package com.draco18s.ores.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.draco18s.hardlib.api.interfaces.IHardRecipes;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityMillstone;
import com.draco18s.ores.entities.TileEntitySifter;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeManager implements IHardRecipes {
	private static Map sifterRecipes = new HashMap();
	private static Map millRecipes = new HashMap();
	private static List<Block> sluiceRecipes = new ArrayList();
	private static Map<Block,OreFlowerData> oreList = new HashMap();

	public void addSiftRecipe(ItemStack input, ItemStack output) {
		sifterRecipes.put(input.copy(), output.copy());
	}

	public void addMillRecipe(ItemStack input, ItemStack output) {
		millRecipes.put(input.copy(), output.copy());
	}

	public void addSluiceRecipe(Block output) {
		sluiceRecipes.add(output);
	}
	
	public void addOreFlowerData(Block ore, OreFlowerData data) {
		oreList.put(ore, data);
	}
	
	public boolean isSluiceSetFull() {
		return sluiceRecipes.size() > 4;
	}
	
	public static int getSiftAmount(ItemStack key) {
		Iterator iterator = sifterRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return 0;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(key, (ItemStack)entry.getKey(), true));
        return ((ItemStack)entry.getKey()).stackSize;
	}
	
	public static ItemStack getSiftResult(ItemStack key, boolean checkSize) {
		Iterator iterator = sifterRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(key, (ItemStack)entry.getKey(), checkSize));

        return (ItemStack)entry.getValue();
	}
	
	private static boolean areSame(ItemStack input, ItemStack against, boolean size) {
		if(input == null || against == null) return false;
        return against.getItem() == input.getItem() && (against.getItemDamage() == OreDictionary.WILDCARD_VALUE || against.getItemDamage() == input.getItemDamage()) && (!size || input.stackSize >= against.stackSize);
    }
	
	public static ItemStack getMillResult(ItemStack key) {
		Iterator iterator = millRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(key, (ItemStack)entry.getKey(), false));

        return (ItemStack)entry.getValue();
	}
	
	public static boolean canInsert(TileEntity device, ItemStack stack) {
		if(device instanceof TileEntityMillstone) {
			return (getMillResult(stack) != null);
		}
		if(device instanceof TileEntitySifter) {
			return (getSiftResult(stack, false) != null);
		}
		return false;
	}
	
	public static Block getRandomSluiceResult(Random rand) {
		return sluiceRecipes.get(rand.nextInt(sluiceRecipes.size()));
	}
	
	public static Map<Block, OreFlowerData> getOreList() {
		return oreList;
	}

	public static Block getRandomSluiceResult(Random rand, Item item) {
		Block b;
		do {
			b = getRandomSluiceResult(rand);
		} while(item == Item.getItemFromBlock(Blocks.sand) && b == Blocks.gravel);
		return b;
	}
}
