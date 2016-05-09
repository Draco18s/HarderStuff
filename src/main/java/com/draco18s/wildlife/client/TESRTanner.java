package com.draco18s.wildlife.client;

import com.draco18s.hardlib.TileEntityInventoryRenderer;
import com.draco18s.wildlife.client.model.ModelRack;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRTanner extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelRack rack = new ModelRack();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		rack.render(tileentity, d0, d1, d2);
	}
	
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		rack.render(tileentity, d0, d1, d2);
	}
}
