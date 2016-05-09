package com.draco18s.ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;

import scala.collection.concurrent.Debug;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IBlockMultiBreak;
import com.draco18s.hardlib.api.interfaces.IHardOres;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;

public class OreManager implements IHardOres {
	private Map<BlockWrapper, OreFlowerData> oreList = new HashMap();

	@Override
	public void addOreFlowerData(Block ore, int meta, OreFlowerData data) {
		if(meta > 15) meta = -1;
		if(OresBase.config.getBoolean("ShowFlowerDebug", "GENERAL", false, "Enables debug messages")) {
			ItemStack flowerStack = new ItemStack(data.flower, data.metadata);
			ItemStack oreStack = new ItemStack(ore, meta);
			OresBase.instance.logger.log(Level.DEBUG,"[Flower Debug]: " + oreStack.getUnlocalizedName() + "(" + ore.getUnlocalizedName() + ":" + meta + ")" + " registered with " + flowerStack.getUnlocalizedName());
		}
		oreList.put(new BlockWrapper(ore, meta), data);
	}

	@Override
	public Map<BlockWrapper, OreFlowerData> getOreList() {
		return oreList;
	}

	@Override
	public boolean isHardOre(Block b) {
		return b instanceof IBlockMultiBreak;
	}

	@Override
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune) {
		return this.mineHardOreOnce(world, x, y, z, fortune, Blocks.air, 0);
	}

	@Override
	public ArrayList<ItemStack> mineHardOreOnce(World world, int x, int y, int z, int fortune, Block replacement, int replacementMeta) {
		Block b = world.getBlock(x, y, z);
		//System.out.println(b.getUnlocalizedName());
		if(b instanceof IBlockMultiBreak) {
			IBlockMultiBreak bl = (IBlockMultiBreak)b;
			int meta = world.getBlockMetadata(x, y, z);
			//System.out.println("Multibreak! " + meta);
			if(meta > bl.getMetaChangeOnBreak()) {
				world.setBlock(x, y, z, b, meta-bl.getMetaChangeOnBreak(), 3);
			}
			else {
				world.setBlock(x, y, z, replacement,replacementMeta,3);
			}
			ArrayList<ItemStack> allDrops = b.getDrops(world, x, y, z, meta, fortune);
			allDrops.subList(1, allDrops.size()).clear();
			return allDrops;
		}
		return null;
	}
}
