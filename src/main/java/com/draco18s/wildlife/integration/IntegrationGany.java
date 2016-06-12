package com.draco18s.wildlife.integration;

import net.minecraft.block.Block;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.CropWeatherOffsets;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.WildlifeEventHandler;

public class IntegrationGany {
	public static void registerNetherCrops() {
		Block b;
		CropWeatherOffsets off;
		//nether
		b = Block.getBlockFromName("ganysnether:spectreWheat");
		off = new CropWeatherOffsets(-2.0f,2.5f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("ganysnether:quarzBerryBush");
		off = new CropWeatherOffsets(-2.2f,3.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("ganysnether:hellBush");
		off = new CropWeatherOffsets(-2.7f,2.9f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		/*b = Block.getBlockFromName("ganysnether:blazingCactoid");
		off = new CropWeatherOffsets(-2.5f,2.8f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);*/
		b = Block.getBlockFromName("ganysnether:witherShrub");
		off = new CropWeatherOffsets(-1.7f,2.7f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
	}
	
	public static void registerSurfaceCrops() {
		Block b;
		CropWeatherOffsets off;
		//surface
		b = Block.getBlockFromName("ganyssurface:camelliaCrop");
		off = new CropWeatherOffsets(-0.2f,-0.3f,(int) (-1*WildlifeEventHandler.yearLength/12),0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		
		b = Block.getBlockFromName("ganyssurface:coarse_dirt");
		HardLibAPI.treeCounter.addDirtType(b);
		//WildlifeBase.treeCounter.addDirtType(b);
	}
}
