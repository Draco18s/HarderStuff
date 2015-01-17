package com.draco18s.ores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.recipes.IC2Integration;
import com.draco18s.ores.recipes.RecipeManager;
import com.draco18s.ores.util.OreDataHooks;

import CustomOreGen.Util.CogOreGenEvent;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OresEventHandler {

	@SubscribeEvent
	public void bonemeal(BonemealEvent event) {
		if(!event.world.isRemote && event.block == Blocks.grass) {
			Map<Block,OreFlowerData> list = RecipeManager.getOreList();
			OreFlowerData entry;
			for(Block b : list.keySet()) {
				int count = OreDataHooks.getOreData(event.world, event.x, event.y, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-8, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-16, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-24, event.z, b);
				//System.out.println(b.getUnlocalizedName() + ": " + count);
				if(count > 0) {
					count = (int)Math.min(Math.round(Math.log(count)), 10);
					entry = list.get(b);
					if(count >= entry.highConcentrationThreshold) {
						OresBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
					}
					for(;--count >= 0;) {
						if(OresBase.rand.nextBoolean()) {
							OresBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if(event.entity instanceof EntityIronGolem) {
			int count = 0;
			for(EntityItem b : event.drops) {
				if(b.getEntityItem().getItem() == Items.iron_ingot) {
					event.drops.remove(b);
					++count;
				}
			}
			if(!(event.source == DamageSource.lava || event.source == DamageSource.onFire || event.source == DamageSource.inWall || event.source == DamageSource.inFire)) {
				event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(OresBase.nuggetIron, count*2)));
			}
		}
	}
	
	@SubscribeEvent
	public void worldsave(WorldEvent.Load event) {
		//OresBase.worldRand = new Random(event.world.getSeed());
	}
	
	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		int cx = event.worldX / 16;
		int cz = event.worldZ / 16;
		OresBase.oreCounter.generate(null, cx, cz, event.world);
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		OreDataHooks.readData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event) {
		OreDataHooks.saveData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		OreDataHooks.clearData(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
	}
}
