package com.draco18s.wildlife.integration; 


import com.draco18s.hardlib.api.HardLibAPI; 
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType; 
import com.draco18s.wildlife.WildlifeBase; 


import cpw.mods.fml.common.registry.GameRegistry; 


import net.minecraft.block.Block; 
import net.minecraft.item.ItemStack;


public class IntegrationTFC { 

	public static void registerTFCTrees() {
		Block b = GameRegistry.findBlock("terrafirmacraft", "sapling"); 

		if(b != null) {

			for(int i = 1; i <= 16; i++) {
				HardLibAPI.plantManager.registerBlockType(new ItemStack(b, 1, i), BlockType.SAPLING); 
				b = GameRegistry.findBlock("terrafirmacraft", "log"+i); 
				HardLibAPI.treeCounter.addLogType(b);
				//WildlifeBase.treeCounter.addLogType(b); 
			}
		}
		b = GameRegistry.findBlock("terrafirmacraft", "sapling2"); 

		if(b != null) { 

			for(int i = 1; i <= 1; i++) {
				HardLibAPI.plantManager.registerBlockType(new ItemStack(b, 1, i), BlockType.SAPLING); 
				b = GameRegistry.findBlock("terrafirmacraft", "log2"+i);
				HardLibAPI.treeCounter.addLogType(b);
			}
		}
		
		//TODO: TFC dirts
		//for(int i = 1; i <= 1; i++) {
		//	b = GameRegistry.findBlock("terrafirmacraft", "dirt");
		//	HardLibAPI.treeCounter.addDirtType(b);
		//}
	}
}
