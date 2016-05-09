package com.draco18s.industry.util;

import java.lang.reflect.Field;

import com.draco18s.industry.IndustryBase;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

public class StatsAchievements {
	public static Achievement craftWoodenHopper;
	public static Achievement craftCartLoader;
	public static Achievement craftDistributor;
	
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
		//[x] wooden hopper (5,1)
		try {
			dispCol.set(AchievementList.snipeSkeleton, 10);
			dispRow.set(AchievementList.snipeSkeleton, -1);
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public static void addCoreAchievements() {
		initAvhievements();
		craftWoodenHopper = new Achievement("craftWoodenHopper", "craftWoodenHopper", 5, 0, IndustryBase.blockWoodHopper, AchievementList.buildWorkBench).registerStat();
		craftCartLoader = new Achievement("craftCartLoader", "craftCartLoader", 7, 1, IndustryBase.blockCartLoader, craftWoodenHopper).registerStat();
		craftDistributor = new Achievement("craftDistributor", "craftDistributor", 9, 1, IndustryBase.blockDistributor, craftCartLoader).registerStat();
	}
}
