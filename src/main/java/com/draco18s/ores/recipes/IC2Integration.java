package com.draco18s.ores.recipes;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import ic2.api.event.LaserEvent;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.ores.*;
import com.draco18s.ores.util.EnumOreType;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class IC2Integration {
	public static void registerAPIRecipes() {
		OresBase.instance.logger.log(Level.INFO,"Integrating HardOres with IC2");
		ItemStack rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.IRON.value);
		ItemStack nuggetOut = IC2Items.getItem("crushedIronOre");
        GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        GameRegistry.addSmelting(rawOreIn, new ItemStack(OresBase.nuggetIron), 0.1f);
        rawOreIn.stackSize = 8;
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		
		nuggetOut = IC2Items.getItem("crushedGoldOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.GOLD.value);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        GameRegistry.addSmelting(rawOreIn, new ItemStack(Items.gold_nugget), 0.1f);
        rawOreIn.stackSize = 8;
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
        
        nuggetOut = IC2Items.getItem("crushedTinOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.TIN.value);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        rawOreIn.stackSize = 8;
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedCopperOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.COPPER.value);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        rawOreIn.stackSize = 8;
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedLeadOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.LEAD.value);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        rawOreIn.stackSize = 8;
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
		nuggetOut = IC2Items.getItem("crushedUraniumOre");
		rawOreIn = new ItemStack(OresBase.smallCrushed, 1, EnumOreType.URANIUM.value);
		GameRegistry.addShapelessRecipe(nuggetOut,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn,
        		rawOreIn, rawOreIn, rawOreIn);
        rawOreIn.stackSize = 8;
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);

		rawOreIn = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		nuggetOut = new ItemStack(OresBase.smallCrushed, 3, EnumOreType.IRON.value);
		RecipeInputItemStack input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		nuggetOut.setItemDamage(EnumOreType.GOLD.value);
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(EnumOreType.TIN.value);
		nuggetOut.setItemDamage(EnumOreType.TIN.value);
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(EnumOreType.COPPER.value);
		nuggetOut.setItemDamage(EnumOreType.COPPER.value);
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(EnumOreType.LEAD.value);
		nuggetOut.setItemDamage(EnumOreType.LEAD.value);
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);

		rawOreIn.setItemDamage(EnumOreType.URANIUM.value);
		nuggetOut.setItemDamage(EnumOreType.URANIUM.value);
		input = new RecipeInputItemStack(rawOreIn);
		Recipes.macerator.addRecipe(input, null, nuggetOut);
		
		rawOreIn = IC2Items.getItem("smallIronDust");
		rawOreIn.stackSize = 8;
        nuggetOut = IC2Items.getItem("ironDust");
        nuggetOut.stackSize = 1;
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut, true);
		rawOreIn = IC2Items.getItem("smallGoldDust");
		rawOreIn.stackSize = 8;
        nuggetOut = IC2Items.getItem("goldDust");
        nuggetOut.stackSize = 1;
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut, true);
		
		/*rawOreIn = new ItemStack(OresBase.oreChunks);
        nuggetOut = IC2Items.getItem("smallTinDust");
        rawOreIn.setItemDamage(EnumOreType.TIN.value);
        HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
        rawOreIn.setItemDamage(EnumOreType.COPPER.value);
        nuggetOut = IC2Items.getItem("smallCopperDust");
        HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
        rawOreIn.setItemDamage(EnumOreType.LEAD.value);
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
        HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);*/
        
        /*crushing ore blocks directly*/
        
		rawOreIn = new ItemStack(OresBase.oreIron);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = i + (i/5)*2;
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreGold);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.GOLD.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = i + (i/5)*2;
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreTin);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.TIN.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = i + (i/5)*2;
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreCopper);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.COPPER.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = i + (i/5)*2;
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreLead);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.LEAD.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = i + (i/5)*2;
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreUranium);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.URANIUM.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = (int)Math.ceil(i/3f) + (i/5);
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		rawOreIn = new ItemStack(OresBase.oreDiamond);
		nuggetOut = new ItemStack(OresBase.oreChunks, 1, EnumOreType.DIAMOND.value);
		for(int i=0; i<15;i++) {
			rawOreIn.setItemDamage(i);
			input = new RecipeInputItemStack(rawOreIn);
			nuggetOut.stackSize = (int)Math.ceil(i/3f) + (i/5);
			Recipes.macerator.addRecipe(input, null, nuggetOut.copy());
		}
		
		/*if(Loader.isModLoaded("RotaryCraft")) {
			GameRegistry.addShapedRecipe(IC2Items.getItem("copperOre"), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.COPPER.value));
			GameRegistry.addShapedRecipe(IC2Items.getItem("tinOre"), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.TIN.value));
			GameRegistry.addShapedRecipe(IC2Items.getItem("uraniumOre"), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.URANIUM.value));
			GameRegistry.addShapedRecipe(IC2Items.getItem("leadOre"), "ccc","ccc","ccc",'c',new ItemStack(OresBase.oreChunks, 1, EnumOreType.LEAD.value));
		}*/
	}
	
	@SubscribeEvent
	public void onLaserHitsBlockEvent(LaserEvent.LaserHitsBlockEvent event) {
		Block b = event.world.getBlock(event.x, event.y, event.z);
		int m = event.world.getBlockMetadata(event.x, event.y, event.z);
		float h = b.getBlockHardness(event.world, event.x, event.y, event.z) / 12f;
		if(b instanceof BlockHardOreBase) {
			BlockHardOreBase ho = (BlockHardOreBase)b;
			while(m >= 0 && event.power > 0) {
				event.power -= h;
				m-=ho.metaChange;
				if(event.smelt) {
					ArrayList<ItemStack> list = b.getDrops(event.world, event.x, event.y, event.z, m, 0);
					for(ItemStack stack : list) {
						ItemStack newStack = stack;
						if(event.power > 0) {
							newStack = FurnaceRecipes.smelting().getSmeltingResult(stack).copy();
							newStack.stackSize = stack.stackSize;
							event.power -= h;
						}
						float f = 0.7F;
			            double d0 = (double)(event.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            double d1 = (double)(event.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            double d2 = (double)(event.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            EntityItem entityitem = new EntityItem(event.world, (double)event.x + d0, (double)event.y + d1, (double)event.z + d2, newStack);
			            entityitem.delayBeforeCanPickup = 10;
			            event.world.spawnEntityInWorld(entityitem);
					}
				}
				else {
					b.dropBlockAsItemWithChance(event.world, event.x, event.y, event.z, m, event.dropChance, 0);
				}
			}
			if(m >= 0) {
				event.power = 0;
				event.removeBlock = false;
				event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, m, 3);
			}
			else {
				event.world.setBlockToAir(event.x, event.y, event.z);
			}
		}
	}
}
