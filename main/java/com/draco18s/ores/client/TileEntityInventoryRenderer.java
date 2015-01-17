package com.draco18s.ores.client;

import net.minecraft.tileentity.TileEntity;

public interface TileEntityInventoryRenderer {
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f);
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f);
}
