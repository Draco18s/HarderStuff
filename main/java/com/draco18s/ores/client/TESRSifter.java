package com.draco18s.ores.client;

import com.draco18s.ores.entities.TileEntitySifter;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRSifter extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelSifter sluice = new ModelSifter();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		sluice.render(tileentity, d0, d1, d2);
	}
	
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		sluice.render(tileentity, d0, d1, d2);
	}
}
