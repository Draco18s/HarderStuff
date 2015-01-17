package com.draco18s.ores.client;

import com.draco18s.ores.entities.TileEntitySluice;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRSluice extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelSluice sluice = new ModelSluice();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		sluice.render(tileentity, d0, d1, d2, 0, ((TileEntitySluice)tileentity).getRotationY());
	}
	
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		sluice.render(tileentity, d0, d1, d2, 0, f);
	}
}
