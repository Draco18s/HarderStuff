package com.draco18s.wildlife.integration;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType;
import com.draco18s.wildlife.WildlifeBase;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;

public class IntegrationForestry {
	
	public static void registerForestryTrees() {
		Block b = GameRegistry.findBlock("Forestry", "saplingGE");
		
		if(b != null) {
			HardLibAPI.plantManager.registerBlockType(b, BlockType.SAPLING);
		
			for(int i = 1; i <= 8; i++) {
				b = GameRegistry.findBlock("Forestry", "log"+i);
				WildlifeBase.treeCounter.addLogType(b);
				//do these generate as trees?
				/*b = Block.getBlockFromName("fireproofLog"+i);
				WildlifeBase.treeCounter.addLogType(b);*/
			}
		}
	}
}
