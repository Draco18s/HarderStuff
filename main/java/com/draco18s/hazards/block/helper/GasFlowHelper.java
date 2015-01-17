package com.draco18s.hazards.block.helper;

import java.util.ArrayList;

import net.minecraft.block.Block;

public class GasFlowHelper {
	private static ArrayList<Block> doors = new ArrayList<Block>();

	public static boolean isWoodenDoor(Block block) {
		return (doors.indexOf(block) >= 0);
	}

	public static void addWoodenDoor(Block block) {
		doors.add(block);
	}
}
