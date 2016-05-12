package com.draco18s.wildlife.integration; 


import com.draco18s.hardlib.api.HardLibAPI; 
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType; 
import com.draco18s.wildlife.WildlifeBase; 


import cpw.mods.fml.common.registry.GameRegistry; 


import net.minecraft.block.Block; 


public class IntegrationTFC { 

	public static void registerTFCTrees() {
		Block b = GameRegistry.findBlock("terrafirmacraft", "ItemSapling"); 

		if(b != null) { 
			HardLibAPI.plantManager.registerBlockType(b, BlockType.SAPLING); 

			for(int i = 1; i <= 8; i++) { 
				b = GameRegistry.findBlock("terrafirmacraft", "BlockLogNatural"+i); 
				WildlifeBase.treeCounter.addLogType(b); 
			} 
		}
		b = GameRegistry.findBlock("terrafirmacraft", "ItemSapling2"); 

		if(b != null) { 
			HardLibAPI.plantManager.registerBlockType(b, BlockType.SAPLING); 

			for(int i = 1; i <= 8; i++) { 
				b = GameRegistry.findBlock("terrafirmacraft", "BlockLogNatural2"+i); 
				WildlifeBase.treeCounter.addLogType(b); 
			} 
		} 
	}
}