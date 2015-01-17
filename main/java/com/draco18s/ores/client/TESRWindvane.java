package com.draco18s.ores.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRWindvane extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelWindSail vane = new ModelWindSail();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		vane.render(tileentity, d0, d1, d2);
	}

	@Override
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		vane.render(tileentity, d0, d1, d2);
	}
}
