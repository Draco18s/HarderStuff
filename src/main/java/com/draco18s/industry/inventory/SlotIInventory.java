package com.draco18s.industry.inventory;

import com.draco18s.industry.entities.TileEntityFilter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotIInventory extends Slot {

	public SlotIInventory(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if(inventory.isItemValidForSlot(slotNumber, stack)) {
			ItemStack ss = stack.copy();
			ss.stackSize = 1;
			inventory.setInventorySlotContents(slotNumber, ss);
		}
		//return inventory.isItemValidForSlot(slotNumber, stack);
		return false;
    }

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		inventory.setInventorySlotContents(slotNumber, null);
		return false;
	}
}
