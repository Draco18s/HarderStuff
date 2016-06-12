package com.draco18s.wildlife.integration;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType;
import com.draco18s.wildlife.WildlifeBase;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class IntegrationForestry {
	
	public static void registerForestryTrees() {
		Block b = GameRegistry.findBlock("Forestry", "saplingGE");
		
		if(b != null) {
		
			for(int i = 1; i <= 8; i++) {
				HardLibAPI.plantManager.registerBlockType(new ItemStack(b, 1, i), BlockType.SAPLING);
				b = GameRegistry.findBlock("Forestry", "log"+i);
				HardLibAPI.treeCounter.addLogType(b);
				//WildlifeBase.treeCounter.addLogType(b);
				//do these generate as trees?
				/*b = Block.getBlockFromName("fireproofLog"+i);
				WildlifeBase.treeCounter.addLogType(b);*/
			}
		}
	}
}
