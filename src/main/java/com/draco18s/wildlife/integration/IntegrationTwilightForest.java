package com.draco18s.wildlife.integration;

import net.minecraft.entity.passive.EntityAnimal;

import com.draco18s.hardlib.api.HardLibAPI;

public class IntegrationTwilightForest {
	public static void registerAnimals() {
		Class animal;
		try {
			animal = Class.forName("twilightforest.entity.passive.EntityTFBoar");
			HardLibAPI.animalManager.addHerbivore(animal);
			animal = Class.forName("twilightforest.entity.passive.EntityTFDeer");
			HardLibAPI.animalManager.addHerbivore(animal);
			animal = Class.forName("twilightforest.entity.passive.EntityTFBighorn");
			HardLibAPI.animalManager.addHerbivore(animal);

			animal = Class.forName("twilightforest.entity.passive.EntityTFQuestRam");
			HardLibAPI.animalManager.addUnaging(animal);
			animal = Class.forName("twilightforest.entity.passive.EntityTFRaven");
			HardLibAPI.animalManager.addUnaging(animal);
		} catch (ClassNotFoundException e) { }
	}
}
