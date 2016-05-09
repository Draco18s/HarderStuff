package com.draco18s.ores.enchantments;

import java.util.Set;

import com.draco18s.hazards.UndergroundBase;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class EnchantPulverize extends Enchantment {
	public EnchantPulverize(int id, int rarity) {
		super(id, rarity, EnumEnchantmentType.digger);
		setName("pulverize");
	}
	
	@Override
	public int getMinEnchantability(int par1) {
		return 5 * par1;
	}
	
	@Override
	public int getMaxEnchantability(int par1) {
		return getMinEnchantability(par1) + 5;
	}
	
	@Override
	public int getMaxLevel() {
        return 5;
    }
	
	public boolean canApplyTogether(Enchantment other) {
		boolean ret = super.canApplyTogether(other);
		String name = other.getName().toLowerCase();
		ret &= !name.contains("smelt");
		ret &= !name.contains("me.heat");
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
					boolean ret = true;
					String name = i.getUnlocalizedName(stack).toLowerCase();
					ret &= !name.contains("smelt");
					ret &= !name.contains("inferno");
					ret &= !name.contains("fiery");
					return ret;
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
