package com.draco18s.ores.recipes;

import ic2.api.event.LaserEvent;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.ores.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class IC2Integration {
	public static Block oreTin;
	public static Block oreCopper;
	public static Block oreLead;
	public static Block oreUranium;

	public static void constructBlocks() {
		oreTin = new BlockOreTin();
    	GameRegistry.registerBlock(oreTin, "ore_tin");
		oreCopper = new BlockOreCopper();
    	GameRegistry.registerBlock(oreCopper, "ore_copper");
    	oreLead = new BlockOreLead();
    	GameRegistry.registerBlock(oreLead, "ore_lead");
    	oreUranium = new BlockOreUranium();
    	GameRegistry.registerBlock(oreUranium, "ore_uranium");
	}
	
	public static void registerAPIRecipes() {
		System.out.println("Integrating HardOres with IC2");
		ItemStack rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 0);
		ItemStack nuggetOut = IC2Items.getItem("crushedIronOre");
        GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        GameRegistry.addSmelting(rawOreIn, new ItemStack(OresBase.nuggetIron), 0.1f);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		
		nuggetOut = IC2Items.getItem("crushedGoldOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 1);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        GameRegistry.addSmelting(rawOreIn, new ItemStack(Items.gold_nugget), 0.1f);
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
        
        nuggetOut = IC2Items.getItem("crushedTinOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 6);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedCopperOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 7);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedLeadOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 8);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedUraniumOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 8, 9);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);

		rawOreIn = new ItemStack(OresBase.oreChunks);
		nuggetOut = new ItemStack(OresBase.smallCrushed, 3, 0);//iron
		RecipeInputItemStack input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		
		rawOreIn.setItemDamage(1);
		nuggetOut.setItemDamage(1);//gold
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(6);
		nuggetOut.setItemDamage(6);
		input = new RecipeInputItemStack(rawOreIn);//tin
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(7);
		nuggetOut.setItemDamage(7);
		input = new RecipeInputItemStack(rawOreIn);//copper
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(8);
		nuggetOut.setItemDamage(8);
		input = new RecipeInputItemStack(rawOreIn);//lead
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(9);
		nuggetOut.setItemDamage(9);
		input = new RecipeInputItemStack(rawOreIn);//uranium
		Recipes.macerator.addRecipe(input, null, nuggetOut);
		
		rawOreIn = new ItemStack(OresBase.oreChunks);
        nuggetOut = IC2Items.getItem("smallTinDust");
        rawOreIn.setItemDamage(6);//tin
        HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
        rawOreIn.setItemDamage(7);//copper
        nuggetOut = IC2Items.getItem("smallCopperDust");
        HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
        rawOreIn.setItemDamage(8);//lead
        nuggetOut = IC2Items.getItem("smallLeadDust");
        HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
        
        rawOreIn = IC2Items.getItem("smallTinDust");
        rawOreIn.stackSize = 8;
        nuggetOut = IC2Items.getItem("tinDust");
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
        rawOreIn = IC2Items.getItem("smallCopperDust");
        rawOreIn.stackSize = 8;
        nuggetOut = IC2Items.getItem("copperDust");
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
        rawOreIn = IC2Items.getItem("smallLeadDust");
        rawOreIn.stackSize = 8;
        nuggetOut = IC2Items.getItem("leadDust");
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
	}
	
	@SubscribeEvent
	public void onLaserHitsBlockEvent(LaserEvent.LaserHitsBlockEvent event) {
		System.out.println("Block hit, power: " + event.power);
		Block b = event.world.getBlock(event.x, event.y, event.z);
		int m = event.world.getBlockMetadata(event.x, event.y, event.z);
		float h = b.getBlockHardness(event.world, event.x, event.y, event.z) / 12f;
		if(b == OresBase.oreIron || b == OresBase.oreGold || b == oreTin || b == oreCopper || b == oreLead) {
			while(m >= 0 && event.power > 0) {
				event.power -= h;
				m--;
				b.dropBlockAsItemWithChance(event.world, event.x, event.y, event.z, m, event.dropChance, 0);
			}
			if(m > 0) {
				event.power = 0;
				event.removeBlock = false;
				event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, m, 3);
			}
		}
		else if(b == OresBase.oreDiamond || b == oreUranium) {
			if(m >= 2) {
				while(m >= 2 && event.power > 0) {
					event.power -= h;
					m-=3;
					b.dropBlockAsItemWithChance(event.world, event.x, event.y, event.z, m, event.dropChance, 0);
				}
				if(m > 0) {
					event.power = 0;
					event.removeBlock = false;
					event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, m, 3);
				}
			}
		}
	}
}
