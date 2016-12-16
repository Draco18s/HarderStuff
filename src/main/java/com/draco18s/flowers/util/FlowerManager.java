package com.draco18s.flowers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.draco18s.flowers.OreFlowersBase;
import com.draco18s.hardlib.api.interfaces.IHardOres;
import com.draco18s.hardlib.api.interfaces.IHardRecipes;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;

import cpw.mods.fml.common.registry.GameRegistry;

public class FlowerManager implements IHardOres {
	private static Map<BlockWrapper, OreFlowerData> oreList = new HashMap();
	private static boolean isFiltered = false;
	private static boolean shouldFilter = false;

	@Override
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data) {
		if(meta > 15) meta = -1;
		oreList.put(new BlockWrapper(ore,meta), data);
	}

	@Override
	public Map<BlockWrapper, OreFlowerData> getOreList() {
		if (oreList.size() == 0) {
			OreFlowersBase.logger.info("Processing ore list for flowers...");
			OreFlowersBase.instance.processOreDict();
			OreFlowersBase.logger.info("...done.");
		}
		if (shouldFilter) {
			shouldFilter = false;
			filterOreList();
		}
		return oreList;
	}

	@Override
	public boolean isHardOre(Block b) {
		return false;
	}

	@Override
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune, Block replacement, int replacementMeta) {
		return null;
	}

	@Override
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune) {
		return null;
	}
	
	public static void setShouldFilter(boolean shouldFilter) {
		FlowerManager.shouldFilter = shouldFilter;
	}
	
	public void filterOreList() {
		int ignoredIds = 0;
		int ignoredTags = 0;
		Iterator<BlockWrapper> oreListIterator;
		
		// check blockId exclusions
		oreListIterator = oreList.keySet().iterator();
		while (oreListIterator.hasNext()) {
			BlockWrapper oreListEntry = oreListIterator.next();
			for (String blockId : OreFlowersBase.configExclusionsBlockIds) {
				String[] blockIdSplit = blockId.split(":");
				if (blockIdSplit.length < 2 || blockIdSplit.length > 3 || blockIdSplit[0] == null || blockIdSplit[1] == null) {
					OreFlowersBase.logger.warn(" - Invalid block exclude entry '" + blockId + "', skipping.");
					continue;
				}
				// check if modid:blockname actually exists
				Block b = GameRegistry.findBlock(blockIdSplit[0], blockIdSplit[1]);
				if (b == null) {
					OreFlowersBase.logger.warn(" - Block '" + blockId + "' not found, skipping.");
					continue;
				}
				if (GameRegistry.findUniqueIdentifierFor(oreListEntry.block).toString().equals(blockIdSplit[0] + ":" + blockIdSplit[1])) {
					// Block found! Does the exclusion specify a meta?
					if (blockIdSplit.length == 3) {
						int meta = -1;
						try {
						    meta = Integer.parseInt(blockIdSplit[2]);
						} catch (NumberFormatException e) {
							// meta invalid
							OreFlowersBase.logger.warn(" - Block '" + blockId + "' has an invalid meta value, skipping.");
							continue;
						}
						if (meta == oreListEntry.meta) {
							// has meta, exception is valid and found - remove it
							oreListIterator.remove();
							ignoredIds++;
						}
					} else {
						// no meta, exception is valid and found - remove it
						oreListIterator.remove();
						ignoredIds++;
					}
				}
			}
		}
		
		// check oreDict exclusions
		oreListIterator = oreList.keySet().iterator();
		while (oreListIterator.hasNext()) {
			BlockWrapper oreListEntry = oreListIterator.next();
			for (String oreDictTag : OreFlowersBase.configExclusionsOredictTags) {
				int[] oreIds = OreDictionary.getOreIDs(new ItemStack(oreListEntry.block, oreListEntry.meta));
				for (int oreId : oreIds) {
					String thisOreDictTag = OreDictionary.getOreName(oreId);
					if (thisOreDictTag.equals(oreDictTag)) {
						oreListIterator.remove();
						ignoredTags++;
					}
				}
			}
		}
		
		OreFlowersBase.logger.info("Removed " + ignoredIds + " blocks matching ID's from Ore Flower list.");
		OreFlowersBase.logger.info("Removed " + ignoredTags + " blocks matching oreDict tags from Ore Flower list.");
	}
}
