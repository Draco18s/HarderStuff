package com.draco18s.ores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.WorldUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.block.ores.BlockHardOreBase;
import com.draco18s.ores.recipes.ChiselIntegration;
import com.draco18s.ores.recipes.GanyIntegration;
import com.draco18s.ores.recipes.IC2Integration;
import com.draco18s.ores.recipes.RecipeManager;
import com.draco18s.ores.util.EnumOreType;
import com.draco18s.ores.util.OreDataHooks;
import com.draco18s.ores.util.StatsAchievements;

import CustomOreGen.Util.CogOreGenEvent;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class OresEventHandler {
	public static boolean poopBonemealFlowers = false;
	
	@SubscribeEvent
	public void harvest(HarvestDropsEvent event) {
		if(event.harvester != null) {
			int level = EnchantmentHelper.getEnchantmentLevel(OresBase.enchPulverize.effectId, event.harvester.getCurrentEquippedItem());
			if(level > 0 && event.blockMetadata <= level*2) {
				Iterator<ItemStack> it = event.drops.iterator();
				ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
				int max = level;
				while(it.hasNext()) {
					ItemStack stk = it.next();
					ItemStack out = HardLibAPI.recipeManager.getMillResult(stk);
					if(out != null) {
						int s = Math.min(stk.stackSize,max);
						ItemStack o = out.copy();
						int n = o.stackSize;
						o.stackSize = 0;
						for(; s > 0; s--) {
							o.stackSize += n;
							max--;
							stk.stackSize--;
						}
						newItems.add(o);
						if(stk.stackSize == 0) {
							it.remove();
						}
					}
				}
				//System.out.println("Max: " + max);
				event.drops.addAll(newItems);
			}
			level = EnchantmentHelper.getEnchantmentLevel(OresBase.enchCracker.effectId, event.harvester.getCurrentEquippedItem());
			if(HardLibAPI.oreManager.isHardOre(event.block)) {
				int max = 0;
				float rollover = 0;
				for(level *= 2;max < 12 && level > 0;max++) {
					ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[event.world.rand.nextInt(6)];
					if(event.block == event.world.getBlock(event.x+dir.offsetX, event.y+dir.offsetY, event.z+dir.offsetZ)) {
						level--;
						ArrayList<ItemStack> drps = HardLibAPI.oreManager.mineHardOreOnce(event.world, event.x+dir.offsetX, event.y+dir.offsetY, event.z+dir.offsetZ, 0);
						//System.out.println(dir + ":" + drps);
						if(drps != null) {
							rollover += 0.75f;
							if(rollover >= 0.75f) {
								event.harvester.getCurrentEquippedItem().damageItem(1, event.harvester);
								rollover -= 1;
							}
							for(ItemStack stack : drps) {
					            dropStack(event.world, event.x, event.y, event.z, stack);
							}
						}
					}
				}
			}
		}
	}

	private void dropStack(World world, int x, int y, int z, ItemStack stack) {
        float f = 0.7F;
		double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        for(ForgeDirection dir : BlockHardOreBase.DROP_SEARCH_DIRECTIONS) {
        	if(!world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).isNormalCube() || dir == ForgeDirection.DOWN) {
        		EntityItem entityitem = new EntityItem(world, (double)x + d0+dir.offsetX, (double)y + d1+dir.offsetY, (double)z + d2+dir.offsetZ, stack);
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
                return;
        	}
        }
	}

	@SubscribeEvent
	public void bonemeal(BonemealEvent event) {
		if(!event.world.isRemote && event.block == Blocks.grass && (event.entityPlayer != null || poopBonemealFlowers)) {
			Map<BlockWrapper,OreFlowerData> list = HardLibAPI.oreManager.getOreList();
			OreFlowerData entry;
			for(BlockWrapper b : list.keySet()) {
				int count = OreDataHooks.getOreData(event.world, event.x, event.y, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-8, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-16, event.z, b)+
						OreDataHooks.getOreData(event.world, event.x, event.y-24, event.z, b);
				//System.out.println(b.block.getUnlocalizedName() + ": " + count);
				if(b.block == OresBase.oreRedstone) { count *= 0.8f; }//change to 0.25? that would be a 20% reduction in flowers
				//System.out.println("   : " + (count > 0));
				if(count > 0) {
					count = (int)Math.min(Math.round(Math.log(count)), 10);
					entry = list.get(b);
					if(count >= entry.highConcentrationThreshold && event.entityPlayer != null) {
						OresBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
					}
					for(;--count >= 0;) {
						if(OresBase.rand.nextBoolean() && (event.entityPlayer != null || OresBase.rand.nextInt(128) == 0)) {
							OresBase.scatterFlowers(event.world, event.x, event.y, event.z, entry.flower, entry.metadata, 0, 1, 7);
						}
					}
					if(event.entityPlayer != null) {
						event.entityPlayer.addStat(StatsAchievements.prospecting, 1);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPickup(PlayerEvent.ItemPickupEvent event) {
		Item item = event.pickedUp.getEntityItem().getItem();
		if(item == OresBase.oreChunks && event.pickedUp.getEntityItem().getItemDamage() == EnumOreType.LIMONITE.value) {
			event.player.addStat(StatsAchievements.mineLimonite, 1);
		}
		if(item == OresBase.oreChunks && event.pickedUp.getEntityItem().getItemDamage() == EnumOreType.IRON.value) {
			event.player.addStat(StatsAchievements.acquireIronChunk, 1);
		}
		if(item == Item.getItemFromBlock(OresBase.blockOreFlowers) || item == Item.getItemFromBlock(OresBase.blockOreFlowers2)) {
			event.player.addStat(StatsAchievements.oreFlowers, 1);
		}
		if(item == Item.getItemFromBlock(OresBase.blockMill)) {
			event.player.addStat(StatsAchievements.craftMill, 1);
		}
		if(item == OresBase.oreChunks && event.pickedUp.getEntityItem().getItemDamage() == EnumOreType.DIAMOND.value){
			event.player.addStat(AchievementList.diamonds, 1);
		}
		if(StatsAchievements.mineDiorite != null) {
			Block b;
			b = ChiselIntegration.getDioriteBlock();
			if(b != null && item == Item.getItemFromBlock(b)) {
				event.player.addStat(StatsAchievements.mineDiorite, 1);
			}
			b = GanyIntegration.getDioriteBlock();
			if(b != null && item == Item.getItemFromBlock(b)) {
				event.player.addStat(StatsAchievements.mineDiorite, 1);
			}
		}
		ItemStack s = HardLibAPI.recipeManager.getSiftResult(event.pickedUp.getEntityItem(), false);
		if(s != null) {
			event.player.addStat(StatsAchievements.grindOre, 1);
		}
	}
	
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		Item item = event.crafting.getItem();
		if(item == Items.iron_ingot){
			if(event.player instanceof EntityPlayerMP && ((EntityPlayerMP)event.player).func_147099_x().canUnlockAchievement(AchievementList.acquireIron)) {
				event.player.addStat(StatsAchievements.fakeIronBar, 1);
				event.player.addStat(AchievementList.acquireIron, 1);
			}
		}
		if(item == Item.getItemFromBlock(OresBase.blockSluice)){
			event.player.addStat(StatsAchievements.craftSluice, 1);
		}
		if(item == Item.getItemFromBlock(OresBase.blockSifter)){
			event.player.addStat(StatsAchievements.craftSifter, 1);
		}
		if(item == Item.getItemFromBlock(OresBase.blockMill)){
			event.player.addStat(StatsAchievements.craftMill, 1);
		}
		if(item instanceof ItemTool){
			ItemTool tool = (ItemTool)item;
			if(tool.getToolMaterialName().equals(OresBase.toolMaterialDiamondStud.name())) {
				event.player.addStat(StatsAchievements.craftDiamondStud, 1);
			}
		}
		if(item instanceof ItemHoe){
			ItemHoe tool = (ItemHoe)item;
			if(tool.getToolMaterialName().equals(OresBase.toolMaterialDiamondStud.name())) {
				event.player.addStat(StatsAchievements.craftDiamondStud, 1);
			}
		}
		ArrayList<ItemStack> items = OreDictionary.getOres("nuggetIron");
		boolean isNugget = false;
		for(ItemStack s : items) {
			isNugget = isNugget | OreDictionary.itemMatches(s, event.crafting, false);
		}
		if(isNugget){
			event.player.addStat(StatsAchievements.acquireNuggets, 1);
		}
	}
	
	@SubscribeEvent
	public void onSmelting(PlayerEvent.ItemSmeltedEvent event) {
		Item item = event.smelting.getItem();
		if(item == OresBase.oreChunks && event.smelting.getItemDamage() == EnumOreType.IRON.value) {
			event.player.addStat(StatsAchievements.acquireIronChunk, 1);
		}
		if(item == Items.iron_ingot){
			if(event.player instanceof EntityPlayerMP && ((EntityPlayerMP)event.player).func_147099_x().canUnlockAchievement(AchievementList.acquireIron)) {
				event.player.addStat(StatsAchievements.fakeIronBar, 1);
				event.player.addStat(AchievementList.acquireIron, 1);
			}
		}
		ArrayList<ItemStack> items = OreDictionary.getOres("nuggetIron");
		boolean isNugget = false;
		for(ItemStack s : items) {
			isNugget = isNugget | OreDictionary.itemMatches(s, event.smelting, false);
		}
		if(isNugget){
			event.player.addStat(StatsAchievements.acquireNuggets, 1);
		}
	}
	
	@SubscribeEvent
	public void onAchievement(AchievementEvent event) {
		if(event.achievement == AchievementList.acquireIron){
			if(event.entityPlayer instanceof EntityPlayerMP && ((EntityPlayerMP)event.entityPlayer).func_147099_x().canUnlockAchievement(StatsAchievements.fakeIronBar)) {
				event.entityPlayer.addStat(StatsAchievements.fakeIronBar, 1);
			}
			else {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if(event.entity instanceof EntityIronGolem && !event.entity.worldObj.isRemote) {
			int count = 0;
			ArrayList<EntityItem> drps = new ArrayList<EntityItem>();
			for(EntityItem b : event.drops) {
				if(b.getEntityItem().getItem() == Items.iron_ingot) {
					//event.drops.remove(b);
					++count;
				}
				else {
					drps.add(b);
				}
			}
			if(!(event.source == DamageSource.lava || event.source == DamageSource.onFire || event.source == DamageSource.inWall || event.source == DamageSource.inFire)) {
				drps.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(OresBase.nuggetIron, count*2)));
			}
			event.drops.clear();
			event.drops.addAll(drps);
		}
	}
	
	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		if(event.world.isRemote || event.world.provider.dimensionId == Integer.MIN_VALUE) return;
		Chunk c = event.world.getChunkFromBlockCoords(event.worldX, event.worldZ);
		int cx = c.xPosition;
		int cz = c.zPosition;
		OresBase.oreCounter.generate(null, cx, cz, event.world);
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		if(!event.world.isRemote)
			OreDataHooks.readData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event) {
		if(!event.world.isRemote)
			OreDataHooks.saveData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		if(!event.world.isRemote) {
			//OresBase.logger.log(Level.INFO, "Is chunk loaded: " + event.getChunk().isChunkLoaded);
			OreDataHooks.clearData(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
			//WorldUtils.isChunkLoaded_noChunkLoading(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
			//OresBase.logger.log(Level.INFO, "Is chunk loaded: " + event.getChunk().isChunkLoaded);
		}
	}
	
	/*private int originalGraphsSize = -1;
	private int removedGraphsSize = -1;
	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			if(event.world.provider.dimensionId == 0) {
				if(event.world.getWorldTime() % 12000 == 0) {
					originalGraphsSize = OreDataHooks.getGraphSize();
				}
				if(event.world.getWorldTime() % 12000 == 1) {
					removedGraphsSize = OreDataHooks.cleanup(event.world);
				}
				if(event.world.getWorldTime() % 12000 == 2) {
					OresBase.logger.log(Level.INFO, "Graphs was: " + originalGraphsSize + ", now: " + OreDataHooks.getGraphSize() + ", down: " + removedGraphsSize);
				}
			}
			else {
				OreDataHooks.cleanup(event.world);
			}
		}
	}*/
}
