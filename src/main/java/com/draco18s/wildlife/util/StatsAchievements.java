package com.draco18s.wildlife.util;

import java.lang.reflect.Field;

import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.WildlifeEventHandler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class StatsAchievements {
	public static Achievement killWeeds;
	public static Achievement collectWinterWheat;
	public static Achievement craftThermometer;
	public static Achievement playAYear;
	public static Achievement collectRawhide;
	public static Achievement craftTanner;
	public static Achievement killLizard;
	public static Achievement getLeather;
	public static Achievement collectCompost;
	public static Achievement growGrass;
	public static Achievement cropRotation;
	public static Achievement weedSuppressor;
	public static StatBasic playTime;
	
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
		//[x] move sniper duel up 1 and right 3
		//[ ] wooden hopper (5,1)
		//[x] move time to farm, bake bread, bake cake up 2 
		//[x] move cow tipper, pigs fly, repopulation right 1
		try {
			//AchievementList.achievementList.remove(AchievementList.killCow);
			//dispCol.set(AchievementList.killCow, 10);
			dispCol.set(AchievementList.flyPig, 11);
			dispCol.set(AchievementList.field_150962_H, 11);
			dispCol.set(AchievementList.killCow, 9);
			//dispCol.set(AchievementList.snipeSkeleton, 10);
			//dispRow.set(AchievementList.buildBetterPickaxe, 7);
			//dispRow.set(AchievementList.mineWood, -1);
			//dispCol.set(AchievementList.onARail, -1);
			//dispRow.set(AchievementList.onARail, 4);
			//dispRow.set(AchievementList.openInventory, 0);
			dispRow.set(AchievementList.buildHoe, -5);
			dispRow.set(AchievementList.bakeCake, -7);
			dispRow.set(AchievementList.makeBread, -5);
			dispRow.set(AchievementList.killCow, -4);
			//dispRow.set(AchievementList.snipeSkeleton, -1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addCoreAchievements() {
		initAvhievements();
		
		IChatComponent cht = new ChatComponentText("timePlayed");
		playTime = new StatBasic("playTime", cht, StatBase.simpleStatType);
		
		if(!WildlifeEventHandler.doRawLeather) {
			collectRawhide = new Achievement("collectRawhide", "collectRawhide", 6, -3, WildlifeBase.itemRawLeather, AchievementList.buildSword).registerStat();
			craftTanner = new Achievement("craftTanner", "craftTanner", 7, -4, WildlifeBase.blockTanner, collectRawhide).registerStat();
			getLeather = new Achievement("getLeather", "getLeather", 9, -4, Items.leather, craftTanner).registerStat();

			try {
				parentAch.set(AchievementList.flyPig, getLeather);
				parentAch.set(AchievementList.field_150962_H, getLeather);
			}
			catch (Exception e) {
				
			}
		}
		
		collectCompost = new Achievement("getCompost", "getCompost", 1, -2, new ItemStack(WildlifeBase.rottingWood, 1, 4), AchievementList.mineWood).registerStat();
		growGrass = new Achievement("growGrass", "growGrass", -1, -2, Blocks.grass, collectCompost).registerStat();
		
		if(WildlifeBase.allowLizards)
			killLizard = new Achievement("killLizard", "killLizard", 9, -2, new ItemStack(WildlifeBase.itemAchievementIcons, 1, 2), AchievementList.killEnemy).registerStat();
		
		killWeeds = new Achievement("killWeeds", "killWeeds", 0, -3, new ItemStack(WildlifeBase.itemAchievementIcons, 1, 0), AchievementList.buildHoe).registerStat();
		collectWinterWheat = new Achievement("collectWinterWheat", "collectWinterWheat", 3, -4, WildlifeBase.winterWheatSeeds, AchievementList.buildHoe).registerStat();
		craftThermometer = new Achievement("craftThermometer", "craftThermometer", 4, -7, new ItemStack(WildlifeBase.itemAchievementIcons, 1, 3), AchievementList.buildHoe).registerStat();
		playAYear = new Achievement("playAYear", "playAYear", 6, -7, Items.clock, craftThermometer).setSpecial().registerStat();
		
		cropRotation = new Achievement("cropRotation", "cropRotation", -1, -4, new ItemStack(WildlifeBase.itemAchievementIcons, 1, 1), killWeeds).registerStat();
		weedSuppressor = new Achievement("weedSuppressor", "weedSuppressor", -2, -3, new ItemStack(Blocks.carpet, 1, 12), killWeeds).registerStat();
		
	}
}
