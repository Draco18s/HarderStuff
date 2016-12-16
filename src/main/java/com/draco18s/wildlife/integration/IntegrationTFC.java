package com.draco18s.wildlife.integration; 


import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.api.HardLibAPI; 
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType; 
import com.draco18s.wildlife.WildlifeBase; 


import cpw.mods.fml.common.registry.GameRegistry; 


import net.minecraft.block.Block; 
import net.minecraft.item.ItemStack;


public class IntegrationTFC { 

	public static void registerTFCTrees() {
		Block sapling = GameRegistry.findBlock("terrafirmacraft", "sapling"); 
		Block b = null;
		if(sapling != null) {

			for(int i = 0; i <= 16; i++) {
				WildlifeBase.logger.log(Level.INFO, "Registering terrafirmacraft:log"+i);
				HardLibAPI.plantManager.registerBlockType(new ItemStack(sapling, 1, i), BlockType.SAPLING);
			}
		}
		b = GameRegistry.findBlock("terrafirmacraft", "log");
		HardLibAPI.treeCounter.addLogType(b);
		
		sapling = GameRegistry.findBlock("terrafirmacraft", "sapling2"); 

		if(sapling != null) {
			for(int i = 0; i <= 1; i++) {
				WildlifeBase.logger.log(Level.INFO, "Registering terrafirmacraft:log2"+i);
				HardLibAPI.plantManager.registerBlockType(new ItemStack(sapling, 1, i), BlockType.SAPLING);
			}
		}
		b = GameRegistry.findBlock("terrafirmacraft", "log2");
		HardLibAPI.treeCounter.addLogType(b);
		

		b = GameRegistry.findBlock("terrafirmacraft", "Grass");
		HardLibAPI.treeCounter.addDirtType(b);
		b = GameRegistry.findBlock("terrafirmacraft", "DryGrass");
		HardLibAPI.treeCounter.addDirtType(b);
		b = GameRegistry.findBlock("terrafirmacraft", "PeatGrass");
		HardLibAPI.treeCounter.addDirtType(b);
		b = GameRegistry.findBlock("terrafirmacraft", "ClayGrass");
		HardLibAPI.treeCounter.addDirtType(b);
		b = GameRegistry.findBlock("terrafirmacraft", "Dirt");
		HardLibAPI.treeCounter.addDirtType(b);
	}
}
