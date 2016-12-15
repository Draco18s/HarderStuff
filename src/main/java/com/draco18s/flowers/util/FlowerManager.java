package com.draco18s.flowers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.draco18s.flowers.OreFlowersBase;
import com.draco18s.hardlib.api.interfaces.IHardOres;
import com.draco18s.hardlib.api.interfaces.IHardRecipes;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;

public class FlowerManager implements IHardOres {
	private static Map<BlockWrapper, OreFlowerData> oreList = new HashMap();

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
}
