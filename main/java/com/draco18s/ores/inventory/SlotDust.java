package com.draco18s.ores.inventory;

import com.draco18s.ores.item.ItemOreDustSmall;
import com.draco18s.ores.recipes.RecipeManager;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class SlotDust extends Slot {

	public SlotDust(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}
	
	public boolean isItemValid(ItemStack stack) {
		return RecipeManager.canInsert((TileEntity)this.inventory, stack);
    }
}
