package com.draco18s.hazards.enchantments;

import com.draco18s.hazards.UndergroundBase;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantmentStoneStress extends Enchantment {

	public EnchantmentStoneStress(int id, int rarity) {
		super(id, rarity, EnumEnchantmentType.armor_head);
		setName("stone_stress");
	}
	
	@Override
	public int getMinEnchantability(int par1) {
		return 5 * par1;
	}
	
	@Override
	public int getMaxEnchantability(int par1) {
		return getMinEnchantability(par1) + 10;
	}
	
	@Override
	public int getMaxLevel() {
        return 1;
    }
	
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() == UndergroundBase.goggles;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return canApply(stack);
	}
}
