package com.draco18s.wildlife.integration;

import net.minecraft.block.Block;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.CropWeatherOffsets;
import com.draco18s.wildlife.WildlifeEventHandler;

public class IntegrationHarvestcraft {
	public static void registerCrops() {
		Block b;
		CropWeatherOffsets off;
		//nether
		b = Block.getBlockFromName("harvestcraft:pamblackberryCrop");
		off = new CropWeatherOffsets(-0.3f,0.5f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamblueberryCrop");
		off = new CropWeatherOffsets(0.0f,0.0f,(int) (WildlifeEventHandler.yearLength/4),0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcandleberryCrop");
		off = new CropWeatherOffsets(-0.4f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamraspberryCrop");
		off = new CropWeatherOffsets(0.2f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamstrawberryCrop");
		off = new CropWeatherOffsets(-0.3f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcactusfruitCrop");
		off = new CropWeatherOffsets(-0.6f,0.6f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamasparagusCrop");
		off = new CropWeatherOffsets(0.0f,0.4f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambarleyCrop");
		off = new CropWeatherOffsets(0.4f,0.0f,0,(int) (-WildlifeEventHandler.yearLength/4));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamoatsCrop");
		off = new CropWeatherOffsets(0.4f,-0.6f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamryeCrop");
		off = new CropWeatherOffsets(0.4f,0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcornCrop");
		off = new CropWeatherOffsets(0.0f,0.0f,(int) (-WildlifeEventHandler.yearLength/6),(int) (-WildlifeEventHandler.yearLength/6));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambambooshootCrop");
		off = new CropWeatherOffsets(-0.4f,-0.4f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcantaloupeCrop");
		off = new CropWeatherOffsets(-0.4f,0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcucumberCrop");
		off = new CropWeatherOffsets(-0.1f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamwintersquashCrop");
		off = new CropWeatherOffsets(0.8f,0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamzucchiniCrop");
		off = new CropWeatherOffsets(0.1f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambeetCrop");
		off = new CropWeatherOffsets(0.3f,-0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamonionCrop");
		//-0.2 to offset the season-cycle making summer hot.  Cold season crop
		off = new CropWeatherOffsets(-0.2f,0.0f,(int) (WildlifeEventHandler.yearLength/2),(int) (WildlifeEventHandler.yearLength/2));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamparsnipCrop");
		off = new CropWeatherOffsets(0.2f,0.0f,(int) (WildlifeEventHandler.yearLength/4),(int) (WildlifeEventHandler.yearLength/4));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pampeanutCrop");
		off = new CropWeatherOffsets(-0.6f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamradishCrop");
		off = new CropWeatherOffsets(0.6f,0.0f,0,(int) (-WildlifeEventHandler.yearLength/4));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamrutabagaCrop");
		off = new CropWeatherOffsets(0.0f,-0.2f,(int) (WildlifeEventHandler.yearLength/2),0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamsweetpotatoCrop");
		off = new CropWeatherOffsets(-0.5f,-0.5f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamturnipCrop");
		off = new CropWeatherOffsets(0.2f,0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamrhubarbCrop");
		off = new CropWeatherOffsets(0.25f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamceleryCrop");
		off = new CropWeatherOffsets(0.4f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamgarlicCrop");
		off = new CropWeatherOffsets(0.2f,-0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamgingerCrop");
		off = new CropWeatherOffsets(-0.2f,-0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamspiceleafCrop");
		off = new CropWeatherOffsets(-0.2f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamtealeafCrop");
		off = new CropWeatherOffsets(-0.2f,-0.3f,(int) (-1*WildlifeEventHandler.yearLength/12),0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcoffeebeanCrop");
		off = new CropWeatherOffsets(-0.4f,-0.4f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pammustardseedsCrop");
		off = new CropWeatherOffsets(-0.6f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambroccoliCrop");
		off = new CropWeatherOffsets(0.2f,-0.2f,0,(int) (-WildlifeEventHandler.yearLength/4));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcauliflowerCrop");
		off = new CropWeatherOffsets(0.2f,-0.1f,0,(int) (-WildlifeEventHandler.yearLength/3));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamleekCrop");
		off = new CropWeatherOffsets(0.3f,0.0f,(int) (WildlifeEventHandler.yearLength/6),0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamlettuceCrop");
		off = new CropWeatherOffsets(0.2f,0.2f,0,(int) (-WildlifeEventHandler.yearLength/3));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamscallionCrop");
		off = new CropWeatherOffsets(0.2f,-0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamartichokeCrop");
		off = new CropWeatherOffsets(-0.2f,0.5f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambrusselsproutCrop");
		off = new CropWeatherOffsets(0.3f,0.0f,(int) (-WildlifeEventHandler.yearLength/6),(int) (-WildlifeEventHandler.yearLength/6));
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcabbageCrop");
		off = new CropWeatherOffsets(0.9f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamspinachCrop");
		off = new CropWeatherOffsets(0.7f,-0.2f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamwhitemushroomCrop");
		off = new CropWeatherOffsets(-0.3f,-0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambeanCrop");
		off = new CropWeatherOffsets(0.0f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamsoybeanCrop");
		off = new CropWeatherOffsets(0.0f,-0.25f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pambellpepperCrop");
		off = new CropWeatherOffsets(-0.3f,-0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamchilipepperCrop");
		off = new CropWeatherOffsets(-0.5f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pameggplantCrop");
		off = new CropWeatherOffsets(-0.4f,0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamokraCrop");
		off = new CropWeatherOffsets(-0.2f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pampeasCrop");
		off = new CropWeatherOffsets(0.2f,0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamtomatoCrop");
		off = new CropWeatherOffsets(-0.4f,0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcottonCrop");
		off = new CropWeatherOffsets(-0.6f,0.3f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pampineappleCrop");
		off = new CropWeatherOffsets(-0.6f,0.6f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamgrapeCrop");
		off = new CropWeatherOffsets(-0.3f,-0.1f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamkiwiCrop");
		off = new CropWeatherOffsets(0.3f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamcranberryCrop");
		off = new CropWeatherOffsets(0.0f,-0.5f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamriceCrop");
		off = new CropWeatherOffsets(-0.2f,-0.8f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);
		b = Block.getBlockFromName("harvestcraft:pamseaweedCrop");
		off = new CropWeatherOffsets(0.2f,0.0f,0,0);
		HardLibAPI.cropManager.putCropWeather(b, off);

	}
}