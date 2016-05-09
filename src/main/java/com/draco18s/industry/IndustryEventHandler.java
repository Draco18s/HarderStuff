package com.draco18s.industry;

import com.draco18s.hardlib.events.SpecialBlockEvent.ItemFrameComparatorPowerEvent;
import com.draco18s.industry.util.StatsAchievements;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.AchievementList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.Optional;

public class IndustryEventHandler {
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		Item item = event.crafting.getItem();
		if(item == Item.getItemFromBlock(IndustryBase.blockWoodHopper)){
			event.player.addStat(StatsAchievements.craftWoodenHopper, 1);
		}
		if(item == Item.getItemFromBlock(IndustryBase.blockCartLoader)){
			event.player.addStat(StatsAchievements.craftCartLoader, 1);
		}
	}
	
	@SubscribeEvent
	@Optional.Method(modid = "HardLib")
	public void itemFrameCompare(ItemFrameComparatorPowerEvent event) {
		if(event.stack == null || !(event.entity instanceof EntityItemFrame) || event.power > 0) return;

        event.power = ((EntityItemFrame)event.entity).getRotation()*2+1;
	}
}
