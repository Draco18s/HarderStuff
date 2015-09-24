package com.draco18s.hardlib.api.interfaces;

import com.draco18s.hardlib.api.internal.CropWeatherOffsets;

import net.minecraft.world.World;

/**
 * Interface used to get crop data from abnormal crop types, such as those run by a TileEntity,
 * or crops otherwise not registered via {@link IHardCrops#putCropWeather(net.minecraft.block.Block, CropWeatherOffsets)}.
 * Registered block data will be overwritten by this method.
 * @author Draco18s
 *
 */
public interface ICropDataSupplier {
	CropWeatherOffsets getCropData(World world, int x, int y, int z);
}
