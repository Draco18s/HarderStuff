package com.draco18s.ores.entities;

public class TileEntityWindvane extends TileEntityWindmill {
	public TileEntityWindvane() {
	}
	
	@Override
	public boolean canUpdate() {
        return false;
    }
}
