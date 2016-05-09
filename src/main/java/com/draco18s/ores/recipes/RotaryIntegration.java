package com.draco18s.ores.recipes;

import org.apache.logging.log4j.Level;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.EnumOreType;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/*import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Extras.ModVersion;
import Reika.DragonAPI.Interfaces.OreType;
import Reika.DragonAPI.Interfaces.OreType.OreRarity;
import Reika.DragonAPI.Libraries.Registry.ReikaOreHelper;
import Reika.DragonAPI.ModRegistry.ModOreList;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.API.GrinderAPI;
import Reika.RotaryCraft.API.ExtractAPI;*/

public class RotaryIntegration {

	public static void registerAPIRecipes() {
		/*System.out.println("Integrating HardOres with RotaryCraft");
		ItemStack rawOreIn = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		ItemStack nuggetOut = new ItemStack(OresBase.smallDusts, 3, EnumOreType.IRON.value);
		GrinderAPI.addRecipe(rawOreIn.copy(), nuggetOut.copy());
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		nuggetOut.setItemDamage(EnumOreType.GOLD.value);
		GrinderAPI.addRecipe(rawOreIn.copy(), nuggetOut.copy());
		
		rawOreIn = new ItemStack(OresBase.oreIron);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			GrinderAPI.addRecipe(rawOreIn.copy(), nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreGold);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.GOLD.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = i + (i/5)*2;
			GrinderAPI.addRecipe(rawOreIn.copy(), nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreDiamond);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.DIAMOND.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			nuggetOut.stackSize = (int)Math.ceil(i/3f) + (i/5);
			GrinderAPI.addRecipe(rawOreIn.copy(), nuggetOut.copy());
		}
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.iron_ore), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value));
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.gold_ore), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.GOLD.value));
		
		ModVersion m = ((DragonAPIMod)RotaryCraft.instance).getModVersion();
		System.out.println("Hard Ores detected RotaryCraft version " + m);
		
		boolean b = OresBase.config.getBoolean("attempt_AddExtractorAlternative", "DEBUG", false, "");
		if(b)
			ExtractAPI.addExtractorAlternative(ReikaOreHelper.IRON, "dummyIron");
		
				 //addCustomExtractEntry(String name, OreRarity rarity, String productType, String productName, int number, int c1,
		b = OresBase.config.getBoolean("attempt_addCustomExtractEntry", "DEBUG", false, "");
		if(b)
			ExtractAPI.addCustomExtractEntry("Gold", OreTypeHard.GOLD.rarity, "Dust", "Gold", 1, OreTypeHard.GOLD.oreColor, 
					OreTypeHard.GOLD.oreColor, ReikaOreHelper.IRON, "dummyGold");
				//int c2, OreType nativeOre, String... oreDict) {
		
		//System.out.println("b: " + ModVersion.getFromString("v6a"));
		//System.out.println("a.compareTo(b): " + m.compareTo(ModVersion.getFromString("v6a")));
		//System.out.println("b.compareTo(a): " + ModVersion.getFromString("v6a").compareTo(m));
		/*if(m.compareTo(ModVersion.getFromString("v6b")) > 0) {
			//System.out.println("ExtractorAPI currently non-functional or unimplemented.");
			//try {
				//Class.forName("Reika.RotaryCraft.API.ExtractAPI");
				//int meta = 1;
				//for(int meta = 0; meta < 16; meta++) {
					//ExtractAPI.addCustomExtractEntry("Iron", OreTypeHard.IRON.rarity, "Chunk", "Iron", meta+1, OreTypeHard.IRON.oreColor, OreTypeHard.IRON.oreColor, ReikaOreHelper.IRON, "oreIronHard");
					//ExtractAPI.addCustomExtractEntry("Gold", OreTypeHard.GOLD.rarity, "Chunk", "Gold", meta+1, OreTypeHard.GOLD.oreColor, OreTypeHard.GOLD.oreColor, ReikaOreHelper.GOLD, "oreGoldHard");
					//ExtractAPI.addCustomExtractEntry("Diamond", OreTypeHard.DIAMOND.rarity, "Chunk", "Diamond", meta+1, OreTypeHard.DIAMOND.oreColor, OreTypeHard.DIAMOND.oreColor, ReikaOreHelper.DIAMOND, "oreDiamondHard");
					//ExtractAPI.addCustomExtractEntry("Lead", OreTypeHard.LEAD.rarity, "Chunk", "Lead", meta+1, OreTypeHard.LEAD.oreColor, OreTypeHard.LEAD.oreColor, ModOreList.LEAD, "oreLeadHard");
					//ExtractAPI.addCustomExtractEntry("Uranium", OreTypeHard.URANIUM.rarity, "Chunk", "Uranium", meta+1, OreTypeHard.URANIUM.oreColor, OreTypeHard.URANIUM.oreColor, ModOreList.URANIUM, "oreUraniumHard");
					//ExtractAPI.addCustomExtractEntry("Tin", OreTypeHard.TIN.rarity, "Chunk", "Tin", meta+1, OreTypeHard.TIN.oreColor, OreTypeHard.TIN.oreColor, ModOreList.TIN, "oreTinHard");
					//ExtractAPI.addCustomExtractEntry("Copper", OreTypeHard.COPPER.rarity, "Chunk", "Copper", meta+1, OreTypeHard.COPPER.oreColor, OreTypeHard.COPPER.oreColor, ModOreList.COPPER, "oreCopperHard");
					//ExtractAPI.addCustomExtractEntry("Silver", OreTypeHard.SILVER.rarity, "Chunk", "Silver", meta+1, OreTypeHard.SILVER.oreColor, OreTypeHard.SILVER.oreColor, ModOreList.SILVER, "oreSilverHard");
					//ExtractAPI.addCustomExtractEntry("Nickel", OreTypeHard.NICKEL.rarity, "Chunk", "Nickel", meta+1, OreTypeHard.NICKEL.oreColor, OreTypeHard.NICKEL.oreColor, ModOreList.NICKEL, "oreNickelHard");
				//}
				//ExtractAPI.addExtractorAlternative(ReikaOreHelper.IRON, "chunkIron");
				//ExtractAPI.addExtractorAlternative(ReikaOreHelper.IRON, "oreIronHard");
				//ExtractAPI.addCustomExtractEntry("Iron", OreTypeHard.IRON.rarity, "Dust", "Iron", 1, OreTypeHard.IRON.oreColor, 
						//OreTypeHard.IRON.oreColor, ReikaOreHelper.IRON, "chunkIron");
				//////ExtractAPI.addCustomExtractEntry("Iron", OreTypeHard.IRON.rarity, "Item", "Iron", 1, OreTypeHard.IRON.oreColor, 
						//////OreTypeHard.IRON.oreColor, ReikaOreHelper.IRON, "oreIronHard");
			//} catch (ClassNotFoundException e) { }
		}
		else {
			System.out.println("Unable to find RotaryCraft ExtractorAPI: requires RotaryCraft v6c or later");
		}*/
		/*{comment-start}*/OresBase.instance.logger.log(Level.ERROR,"== HARD ORES WAS COMPILED WITHOUT ROTARY INTEGRATION: Reika's API is a mess. ==");/*{comment-end}*/
		//System.out.println("== WARNING: HARD ORES WAS COMPILED WITHOUT ROTARY INTEGRATION!  THIS IS A BUG! ==");
	}
}
