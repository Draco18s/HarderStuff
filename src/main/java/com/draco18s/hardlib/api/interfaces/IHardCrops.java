package com.draco18s.hardlib.api.interfaces;

import net.minecraft.block.Block;

import com.draco18s.hardlib.api.internal.CropWeatherOffsets;

public interface IHardCrops {
	public void putCropWeather(Block b, CropWeatherOffsets off);
	public boolean isCropBlock(Block block);
	public CropWeatherOffsets getCropOffsets(Block block);
}
