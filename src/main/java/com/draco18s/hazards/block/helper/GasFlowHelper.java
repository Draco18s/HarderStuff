package com.draco18s.hazards.block.helper;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class GasFlowHelper {
	private static ArrayList<Block> doors = new ArrayList<Block>();
	private static ArrayList<Block> partials = new ArrayList<Block>();

	public static boolean isDoor(Block block) {
		return (doors.indexOf(block) >= 0);
	}

	public static void addWoodenDoor(Block block) {
		doors.add(block);
		addPartialBlock(block);
	}

	public static boolean isPartialBlock(Block block) {
		return (partials.indexOf(block) >= 0);
	}

	public static void addPartialBlock(Block block) {
		partials.add(block);
	}
}
