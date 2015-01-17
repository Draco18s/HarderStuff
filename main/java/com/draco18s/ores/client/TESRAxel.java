package com.draco18s.ores.client;

import com.draco18s.ores.entities.TileEntityWindmill;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRAxel extends TileEntitySpecialRenderer implements TileEntityInventoryRenderer {
	private ModelAxel axel = new ModelAxel();
	private ModelGears gears = new ModelGears();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileEntityWindmill tw = (TileEntityWindmill)tileentity;
		if(tw.getAxelPosition() == 3) {
			gears.render(tileentity, d0, d1, d2);
		}
		else {
			axel.render(tileentity, d0, d1, d2);
		}
	}

	@Override
	public void renderTileEntityInventory(TileEntity tileentity, double d0, double d1, double d2, float f) {
		axel.render(tileentity, d0, d1, d2);
	}
}
