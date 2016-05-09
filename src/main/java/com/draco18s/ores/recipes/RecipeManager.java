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

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IHardRecipes;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityMillstone;
import com.draco18s.ores.entities.TileEntityOreProcessor;
import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.util.EnumOreType;

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
	private static Map processorRecipes = new HashMap();
	private static List<Block> sluiceRecipes = new ArrayList();
	//private static Map<BlockWrapper,OreFlowerData> oreList = new HashMap();

	@Override
	public void addSiftRecipe(String input, int stackSize, ItemStack output) {
		addSiftRecipe(input, stackSize, output, true);
	}

	@Override
	public void addSiftRecipe(String input, int stackSize, ItemStack output, boolean registerOutput) {
		ArrayList<ItemStack> stk = OreDictionary.getOres(input);
		for(ItemStack stack : stk) {
			ItemStack s = stack.copy();
			s.stackSize = stackSize;
			addSiftRecipe(s,output,registerOutput);
		}
	}

	@Override
	public void addSiftRecipe(ItemStack input, ItemStack output) {
		addSiftRecipe(input, output, true);
	}

	@Override
	public void addSiftRecipe(ItemStack input, ItemStack output, boolean registerOutput) {
		sifterRecipes.put(input.copy(), output.copy());
		if(registerOutput && input != output) {
			output = output.copy();
			output.stackSize = 1;
			sifterRecipes.put(output, output);
		}
	}

	@Override
	public void addProcessorRecipe(ItemStack input, ItemStack output) {
		processorRecipes.put(input.copy(), output.copy());
	}

	@Override
	public void addMillRecipe(ItemStack input, ItemStack output) {
		millRecipes.put(input.copy(), output.copy());
	}

	@Override
	public void addSluiceRecipe(Block output) {
		//if(output != null)
		//	System.out.println("Added " + output.getUnlocalizedName());
		//else
		//	System.out.println("Added flint");
		sluiceRecipes.add(output);
	}

	@Override
	@Deprecated
	public void addOreFlowerData(Block ore, OreFlowerData data) {
		OresBase.logger.log(Level.WARN, "IHardRecipes.addOreFlowerData deprecated.  Please update API usage: refer to following stack trace.");
		Thread.dumpStack();
		this.addOreFlowerData(ore, -1, data);
	}

	@Override
	@Deprecated
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data) {
		OresBase.logger.log(Level.WARN, "IHardRecipes.addOreFlowerData deprecated.  Please update API usage: refer to following stack trace.");
		Thread.dumpStack();
		HardLibAPI.oreManager.addOreFlowerData(ore, meta, data);
		//if(meta > 15) meta = -1;
		//oreList.put(new BlockWrapper(ore, meta), data);
	}

	@Override
	public boolean isSluiceSetFull() {
		return sluiceRecipes.size() > 4;
	}

	@Override
	public int getSiftAmount(ItemStack key) {
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

	@Override
	public ItemStack getSiftRecipeInput(ItemStack value) {
		Iterator iterator = sifterRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(value, (ItemStack)entry.getValue(), false) || areSame((ItemStack)entry.getValue(), (ItemStack)entry.getKey(), false));

        return ((ItemStack)entry.getKey()).copy();
	}

	@Override
	public ItemStack getSiftResult(ItemStack key, boolean checkSize) {
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

	private boolean areSame(ItemStack input, ItemStack against, boolean size) {
		if(input == null || against == null) return false;
		return against.getItem() == input.getItem() && (against.getItemDamage() == OreDictionary.WILDCARD_VALUE || against.getItemDamage() == input.getItemDamage()) && (!size || input.stackSize >= against.stackSize);
    }

	@Override
	public ItemStack getMillResult(ItemStack key) {
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

	@Override
	public int getProcessorAmount(ItemStack key) {
		Iterator iterator = processorRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return 0;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(key, (ItemStack)entry.getKey(), true));
        return ((ItemStack)entry.getKey()).stackSize;
	}

	@Override
	public ItemStack getProcessorRecipeInput(ItemStack value) {
		Iterator iterator = processorRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(value, (ItemStack)entry.getValue(), false) || areSame((ItemStack)entry.getValue(), (ItemStack)entry.getKey(), false));

        return ((ItemStack)entry.getKey()).copy();
	}

	@Override
	public ItemStack getProcessorResult(ItemStack key, boolean checkSize) {
		Iterator iterator = processorRecipes.entrySet().iterator();
        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }
            entry = (Entry)iterator.next();
        } while (!areSame(key, (ItemStack)entry.getKey(), checkSize));

        return (ItemStack)entry.getValue();
	}

	@Override
	public boolean canInsert(TileEntity device, ItemStack stack) {
		if(device instanceof TileEntityMillstone) {
			return (getMillResult(stack) != null);
		}
		if(device instanceof TileEntitySifter) {
			return (getSiftResult(stack, false) != null);
		}
		if(device instanceof TileEntityOreProcessor) {
			return (getProcessorResult(stack, false) != null);
		}
		return false;
	}

	public Block getRandomSluiceResult(Random rand) {
		return sluiceRecipes.get(rand.nextInt(sluiceRecipes.size()));
	}

	@Override
	@Deprecated
	public Map<BlockWrapper, OreFlowerData> getOreList() {
		OresBase.logger.log(Level.WARN, "IHardRecipes.getOreList deprecated.  Please update API usage: refer to following stack trace.");
		Thread.dumpStack();
		return HardLibAPI.oreManager.getOreList();
	}

	@Override
	public Block getRandomSluiceResult(Random rand, Item item) {
		Block b;
		do {
			b = getRandomSluiceResult(rand);
		} while(item == Item.getItemFromBlock(Blocks.sand) && b == Blocks.gravel);
		return b;
	}

	@Override
	public ArrayList<ItemStack> getMillRecipeInput(ItemStack value) {
		Iterator iterator = millRecipes.entrySet().iterator();
        Entry entry;
        ArrayList<ItemStack> allInputs = new ArrayList<ItemStack>();
        do {
            if (!iterator.hasNext()) {
                return allInputs;
            }
            entry = (Entry)iterator.next();
            if(!(!areSame(value, (ItemStack)entry.getValue(), false) || areSame((ItemStack)entry.getValue(), (ItemStack)entry.getKey(), false))) {
            	allInputs.add((ItemStack)entry.getKey());
            }
        } while (iterator.hasNext());

        return allInputs;
	}
}
