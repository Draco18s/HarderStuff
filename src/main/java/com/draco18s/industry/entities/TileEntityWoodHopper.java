package com.draco18s.industry.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;

public class TileEntityWoodHopper extends TileEntityHopper {
	private int delay = 0;

	public TileEntityWoodHopper() {
		super();
		func_145886_a("Wooden Hopper");
	}
	
	public int getInventoryStackLimit() {
        return 16;
    }

	@Override
	public void updateEntity() {
		if(getBlockMetadata() != 0) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
		}
		super.updateEntity();
	}
}
