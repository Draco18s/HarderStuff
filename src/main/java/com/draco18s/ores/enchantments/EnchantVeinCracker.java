package com.draco18s.ores.enchantments;

import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class EnchantVeinCracker extends Enchantment {
	public EnchantVeinCracker(int id, int rarity) {
		super(id, rarity, EnumEnchantmentType.digger);
		setName("cracker");
	}
	
	@Override
	public int getMinEnchantability(int par1) {
		return 3 * par1;
	}
	
	@Override
	public int getMaxEnchantability(int par1) {
		return getMinEnchantability(par1) + 6;
	}
	
	@Override
	public int getMaxLevel() {
	    return 3;
	}
	
	public boolean canApplyTogether(Enchantment other) {
		boolean ret = super.canApplyTogether(other);
		ret |= other != Enchantment.efficiency;
		ret |= other != Enchantment.silkTouch;
	    return ret;
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		Item i = stack.getItem();
		if(i instanceof ItemTool) {
			ItemTool tool = (ItemTool)i;
			Set<String> classes = tool.getToolClasses(stack);
			for(String cl : classes) {
				if(cl.equals("pickaxe")) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return canApply(stack);
	}
}
