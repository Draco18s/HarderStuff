package com.draco18s.ores.inventory;

import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.entities.TileEntitySluice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerSluice extends Container {
	protected TileEntitySluice tileEntity;

	public ContainerSluice(InventoryPlayer inventoryPlayer, TileEntitySluice te) {
		tileEntity = te;
		addSlotToContainer(new SlotGravel(tileEntity, 0, 16, 18));
		for(int i=1;i<=3;i++) {
			for(int j=0;j<3;j++) {
				addSlotToContainer(new SlotOutput(tileEntity, j*3+i, 95+18*i, 11+18*j));
			}
		}
		bindPlayerInventory(inventoryPlayer);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
	
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntity.isUseableByPlayer(entityplayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNum == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (slotNum != 1 && slotNum != 0) {
                if (itemstack1.getItem() == Item.getItemFromBlock(Blocks.gravel) || itemstack1.getItem() == Item.getItemFromBlock(Blocks.sand)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                }
                else if (slotNum >= 3 && slotNum < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                }
                else if (slotNum >= 30 && slotNum < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
	
	@Override
	protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
		// The default implementation in Slot doesn't take into account the Slot.isItemValid() and Slot.getSlotStackLimit() values.
		// So here is a modified implementation. I have only modified the parts with a comment.
	
		boolean flag1 = false;
		int k = i;
		if (flag) {
			k = j - 1;
		}
		if (itemstack.isStackable()) {
			while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i)) {
				Slot slot = (Slot)inventorySlots.get(k);
				ItemStack itemstack1 = slot.getStack();
	
				if (flag) {
					k--;
				}
				else {
					k++;
				}
	
				// Check if item is valid:
				if (!slot.isItemValid(itemstack)) {
					continue;
				}
	
				if (itemstack1 != null && itemstack1.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage())
						&& ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					//ItemStack.areItemStacksEqual(par0ItemStack, par1ItemStack)
					//ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack)
					int i1 = itemstack1.stackSize + itemstack.stackSize;
	
					// Don't put more items than the slot can take:
					int maxItemsInDest = Math.min(itemstack1.getMaxStackSize(), slot.getSlotStackLimit());
	
					if (i1 <= maxItemsInDest) {
						itemstack.stackSize = 0;
						itemstack1.stackSize = i1;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < maxItemsInDest) {
						itemstack.stackSize -= maxItemsInDest - itemstack1.stackSize;
						itemstack1.stackSize = maxItemsInDest;
						slot.onSlotChanged();
						flag1 = true;
					}
				}
	
			}
		}
		if (itemstack.stackSize > 0) {
			int l;
			if (flag) {
				l = j - 1;
			}
			else {
				l = i;
			}
			do {
				if ((flag || l >= j) && (!flag || l < i)) {
					break;
				}
				Slot slot1 = (Slot)inventorySlots.get(l);
				ItemStack itemstack2 = slot1.getStack();
	
				if (flag) {
					l--;
				}
				else {
					l++;
				}
	
				// Check if item is valid:
				if (!slot1.isItemValid(itemstack)) {
					continue;
				}
	
				if (itemstack2 == null) {
	
					// Don't put more items than the slot can take:
					int nbItemsInDest = Math.min(itemstack.stackSize, slot1.getSlotStackLimit());
					ItemStack itemStack1 = itemstack.copy();
					itemstack.stackSize -= nbItemsInDest;
					itemStack1.stackSize = nbItemsInDest;
	
					slot1.putStack(itemStack1);
					slot1.onSlotChanged();
					// itemstack.stackSize = 0;
					flag1 = true;
					break;
				}
			} while (true);
		}
		return flag1;
	}
}
