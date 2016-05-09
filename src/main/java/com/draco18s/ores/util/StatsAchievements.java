package com.draco18s.ores.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.recipes.ChiselIntegration;
import com.draco18s.ores.recipes.EtFuturumIntegration;
import com.draco18s.ores.recipes.GanyIntegration;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;

public class StatsAchievements {
	public static Achievement mineDiorite;
	public static Achievement mineLimonite;
	public static Achievement acquireIronChunk;
	public static Achievement constructMill;
	public static Achievement grindOre;
	public static Achievement craftMill;
	public static Achievement craftSifter;
	public static Achievement oreFlowers;
	public static Achievement prospecting;
	public static Achievement acquireNuggets;
	public static Achievement fakeIronBar;
	public static Achievement craftSluice;
	public static Achievement craftDiamondStud;
	public static Achievement thingThing;
	
	public static Field parentAch = null;
	public static Field dispCol = null;
	public static Field dispRow = null;

	private static void initAvhievements() {
		try {
			Class clz = Achievement.class;
			parentAch = clz.getDeclaredField("field_75992_c");//parentAchievement
			parentAch.setAccessible(true);
			dispCol = clz.getDeclaredField("field_75993_a");//displayColumn
			dispCol.setAccessible(true);
			dispRow = clz.getDeclaredField("field_75991_b");//displayRow
			dispRow.setAccessible(true);
		}
		catch (NoSuchFieldException e) {
			Class clz = Achievement.class;
			try {
				parentAch = clz.getDeclaredField("parentAchievement");//func_70628_a
				parentAch.setAccessible(true);
				dispCol = clz.getDeclaredField("displayColumn");//field_75993_a
				dispCol.setAccessible(true);
				dispRow = clz.getDeclaredField("displayRow");//field_75991_b
				dispRow.setAccessible(true);
			}
			catch (NoSuchFieldException e2) {
				//System.out.println("NoSuchFieldException!\n" + e2);
			}
			catch (Exception e2) {
				//System.out.println("Failed to modify Achievements!\n" + e2);
			}
		}
		catch (Exception e) {
			//System.out.println("Failed to modify Achievements!\n" + e);
		}
		//[ ] move sniper duel up 1 and right 3
		//[ ] wooden hopper (5,1)
		//[x] move getting an upgrade right 2
		//[x] move getting wood up 2
		//[x] move taking inventory up 0
		try {
			//parentAch.set(AchievementList.buildBetterPickaxe, mineDiorite);
			//dispCol.set(AchievementList.buildBetterPickaxe, 7);
			//dispRow.set(AchievementList.buildBetterPickaxe, 7);
			dispRow.set(AchievementList.mineWood, -1);
			dispCol.set(AchievementList.onARail, 0);
			dispRow.set(AchievementList.onARail, 6);
			//dispRow.set(AchievementList.openInventory, 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addCoreAchievements() {
		initAvhievements();
		craftMill = new Achievement("craftMill", "craftMill", 7, 3, new ItemStack(OresBase.itemAchievementIcons,1,0), AchievementList.buildBetterPickaxe).registerStat();
		constructMill = new Achievement("constructMill", "constructMill", 7, 5, OresBase.blockVane, craftMill).registerStat();
		grindOre = new Achievement("grindOre", "grindOre", 9, 5, new ItemStack(OresBase.smallDusts, 1, EnumOreType.GOLD.value), constructMill).registerStat();
		craftSifter = new Achievement("craftSifter", "craftSifter", 9, 7, new ItemStack(OresBase.itemAchievementIcons,1,1), grindOre).registerStat();
		
		mineLimonite = new Achievement("mineLimonite", "mineLimonite", 3, 0, new ItemStack(OresBase.oreChunks, 1, EnumOreType.LIMONITE.value), AchievementList.buildWorkBench).registerStat();
		acquireIronChunk = new Achievement("acquireIronChunk", "acquireIronChunk", 2, 1, new ItemStack(OresBase.oreChunks, 1, EnumOreType.IRON.value), mineLimonite).registerStat();
		acquireNuggets = new Achievement("acquireNuggets", "acquireNuggets", 1, 2, OresBase.nuggetIron, acquireIronChunk).registerStat();
		//hope this works
		ArrayList list = (ArrayList)AchievementList.achievementList;
		list.remove(AchievementList.acquireIron);
		fakeIronBar = new Achievement("fakeIronBar", "fakeIronBar", 1, 4, Items.iron_ingot, acquireNuggets).registerStat();
		list.add(AchievementList.acquireIron);
		craftDiamondStud = new Achievement("craftDiamondStud", "craftDiamondStud", -2, 3, OresBase.diaStudAxe, AchievementList.diamonds).registerStat();
		
		oreFlowers = new Achievement("oreFlowers", "oreFlowers", -2, -1, new ItemStack(OresBase.blockOreFlowers, 1, EnumOreType.REDSTONE.value), AchievementList.openInventory).registerStat();
		prospecting = new Achievement("prospecting", "prospecting", -4, -1, new ItemStack(Items.dye, 1, 15), oreFlowers).registerStat();
		craftSluice = new Achievement("craftSluice", "craftSluice", -4, -3, OresBase.blockSluice, prospecting).registerStat();
	}

	public static void addChiselAchievements() {
		alterAndAddStoneTools(ChiselIntegration.getDioriteBlock());
	}

	public static void addGanyAchievements() {
		alterAndAddStoneTools(GanyIntegration.getDioriteBlock());
	}
	
	public static void addEtFuturumAchievements() {
		alterAndAddStoneTools(EtFuturumIntegration.getDioriteBlock());
	}

	public static void addIC2Achievements() {

	}

	private static void alterAndAddStoneTools(Block b) {
		if(b == null || mineDiorite != null) return;
		mineDiorite = new Achievement("mineDiorite", "mineDiorite", 6, 2, b, AchievementList.buildPickaxe).registerStat();


		try {
			parentAch.set(AchievementList.buildBetterPickaxe, mineDiorite);
			dispCol.set(AchievementList.buildBetterPickaxe, 8);
			//dispRow.set(AchievementList.buildBetterPickaxe, 7);
		}
		catch (Exception e) {

		}
	}
}
