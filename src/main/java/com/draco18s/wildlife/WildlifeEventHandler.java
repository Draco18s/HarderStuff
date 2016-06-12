package com.draco18s.wildlife;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

/*{uncomment}import squeek.applecore.api.plants.PlantGrowthEvent;{uncomment}*/

import CustomOreGen.Util.CogOreGenEvent;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IAutoPlanter.BlockType;
import com.draco18s.hardlib.api.interfaces.ICropDataSupplier;
import com.draco18s.hardlib.api.internal.CropWeatherOffsets;
import com.draco18s.hardlib.events.EntityAnimalInteractEvent;
import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.hardlib.events.SpecialBlockEvent.BlockUpdateEvent;
import com.draco18s.hardlib.events.SpecialBlockEvent.ItemFrameComparatorPowerEvent;
import com.draco18s.wildlife.entity.CowStats;
import com.draco18s.wildlife.entity.EntityGoat;
import com.draco18s.wildlife.entity.EntityLizard;
import com.draco18s.wildlife.entity.TileEntityGrassSnow;
import com.draco18s.wildlife.entity.ai.EntityAIAging;
import com.draco18s.wildlife.entity.ai.EntityAIMilking;
import com.draco18s.wildlife.entity.ai.EntityAgeTracker;
import com.draco18s.wildlife.util.AnimalUtil;
import com.draco18s.wildlife.util.BlockUtils;
import com.draco18s.wildlife.util.StatsAchievements;
import com.draco18s.wildlife.util.TreeDataHooks;
import com.draco18s.wildlife.util.Tree;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.TupleIntJsonSerializable;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.oredict.OreDictionary;

public class WildlifeEventHandler {
	//private ArrayList<EntityAnimal> toConstruct = new ArrayList<EntityAnimal>();
	//private HashMap<EntityAnimal, EntityAgeTracker> tracker = new HashMap<EntityAnimal, EntityAgeTracker>();
	//private ArrayList<Chunk> activeChunks = new ArrayList<Chunk>();
	public static boolean doWeatherLogging;
	public static boolean trackTrees;
	public static boolean autoSaplings;
	public static boolean doYearCycle;
	public static int weekLength;
	public static long yearLength;
	public static boolean doSnowMelt;
	public static boolean doSlowCrops;
	public static boolean doBiomeCrops;
	public static boolean doRawLeather;
	public static boolean doNativeTreeKill;
	public static int cropsWorst;
	public static boolean modifyAnimalDrops;
	public static int[] dimensionBlacklist;
	private Random rand = new Random();
	public static Field rains;
	public static Field snows;
	private HashMap<Integer,BiomeWeatherData> biomeTemps = new HashMap<Integer,BiomeWeatherData>();
	BiomeGenBase[] allBiomes;
	private float tempStatic;
	private float rainStatic;
	private final int WEEK_MODULO = 2400;

	//private int mod = 0;
	//private boolean initialSet = false;
	private HashMap<Integer,Long> lastWorldTime = new HashMap<Integer,Long>();
	private long lastClientWorldTime = 0;

	public static WildlifeEventHandler instance;

	public WildlifeEventHandler() {
		tempStatic = WildlifeBase.config.getFloat("staticTempModifier", "SEASONS", getSeasonTemp(0), -2, 2, "Takes the place of the season modifier when seasons are off.\n");
		rainStatic = WildlifeBase.config.getFloat("staticRainModifier", "SEASONS", getSeasonRain(0), -2, 2, "Takes the place of the season modifier when seasons are off.\n");
		WildlifeBase.config.save();
		allBiomes = FluentIterable.from(Arrays.asList(BiomeGenBase.getBiomeGenArray())).filter(Predicates.notNull()).toArray(BiomeGenBase.class);
		for(BiomeGenBase bio : allBiomes) {
			BiomeWeatherData dat = biomeTemps.get(bio.biomeID);
			BiomeGenBase bio2 = bio;
			if(bio.biomeID >= 128 && BiomeGenBase.getBiomeGenArray()[bio.biomeID - 128] != null && bio.isEqualTo(BiomeGenBase.getBiomeGenArray()[bio.biomeID - 128])) {
				bio2 = BiomeGenBase.getBiomeGenArray()[bio.biomeID - 128];
			}
			if(BiomeDictionary.isBiomeOfType(bio2, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio2, Type.RIVER)) {
				//System.out.println(" " + bio.biomeName);
				//System.out.println("  -- Ocean/River " + bio.temperature);
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.667f, 1);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.BEACH)) {
				bio.temperature -= 0.3f;
				//System.out.println(" " + bio.biomeName);
				//System.out.println("  -- Beach " + bio.temperature);
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.667f, 1);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.NETHER)) {
				bio.temperature += 1f;
				bio.rainfall -= 2f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 1, 0.25f);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.SAVANNA)) {
				bio.temperature -= 0.1f;
				bio.rainfall += 0.1f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.4f, 4);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.SANDY)) {
				if(bio.temperature > 1.5f) {
					bio.temperature -= 0.8f;
				}
				bio.rainfall -= 0.3f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.4f, 2.5f);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.SWAMP)) {
				bio.temperature += 0.1f;
				bio.rainfall += 0.3f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.667f, 0.5f);
			}
			if(BiomeDictionary.isBiomeOfType(bio2, Type.JUNGLE) || BiomeDictionary.isBiomeOfType(bio2, Type.LUSH)) {
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.667f, 0.5f);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.END)) {
				bio.temperature -= 0.5f;
				bio.rainfall -= 2.5f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 1.5f, 1f);
			}
			else if(BiomeDictionary.isBiomeOfType(bio2, Type.COLD)) {
				if(bio.temperature >= 0.3) {
					bio.temperature -= 0.2f;
				}
				bio.temperature -= 0.1f;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 0.5f, 1);
			}
			else {
				bio.temperature -= 0.2;
				dat = new BiomeWeatherData(bio.temperature, bio.rainfall, 1, 1);
			}
			if(BiomeDictionary.isBiomeOfType(bio2, Type.MAGICAL)) {
				if(bio.temperature > dat.temp)
					dat.temp += 0.1f;
				else if (bio.temperature < dat.temp)
					dat.temp -= 0.1f;
				dat.rainScale /= 5;
				dat.tempScale /= 5;
			}
			biomeTemps.put(bio.biomeID, dat);
		}
		instance = this;
	}

	/*@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if(event.entity != null) {
			if(event.entity.worldObj != null) {
				if(event.entity instanceof EntityAnimal && !event.entity.worldObj.isRemote) {
					toConstruct.add((EntityAnimal) event.entity);
				}
			}
		}
	}*/

	@SubscribeEvent
	public void onEntityAdded(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityAnimal && !event.entity.worldObj.isRemote) {
			EntityAnimal animal = (EntityAnimal)event.entity;
			EntityAgeTracker t = new EntityAgeTracker();
			animal.tasks.addTask(8, new EntityAIAging(new Random(), animal, event.entity.getClass(), t));
			//tracker.put(animal, t);
		}
		if(event.entity instanceof EntityCow) {
			((EntityCow)event.entity).tasks.addTask(0, new EntityAIMilking((EntityCow)event.entity));
		}
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			List cows = event.entity.worldObj.getEntitiesWithinAABB(EntityCow.class, AxisAlignedBB.getBoundingBox(event.entity.posX-64, event.entity.posY-64, event.entity.posZ-64, event.entity.posX+64, event.entity.posY+64, event.entity.posZ+64));
			for(Object c : cows) {
				EntityCow cow = (EntityCow)c;
				CowStats stats = CowStats.get(cow);
				EntityAIMilking.sendUpdatePacket(cow, stats);
			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if(event.entity instanceof EntityCow && CowStats.get((EntityCow)event.entity) == null) {
			CowStats.register((EntityCow) event.entity);
		}
	}

	/*@SideOnly(Side.CLIENT)
    public void onEntityConstructingClient(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityCow && CowStatsClient.get((EntityCow) event.entity) == null)
        {
            CowStatsClient.register((EntityCow) event.entity);
        }
    }*/

	@SubscribeEvent
	public void onEntityTick(LivingUpdateEvent event) {
		/*if(event.entity instanceof EntityAnimal && !event.entity.worldObj.isRemote) {
			EntityAnimal animal = (EntityAnimal)event.entity;
			if(toConstruct.indexOf(animal) >= 0) {
				EntityAgeTracker t = new EntityAgeTracker();
				animal.tasks.addTask(8, new EntityAIAging(new Random(), animal, event.entity.getClass(), t));
				toConstruct.remove(animal);
				tracker.put(animal, t);
			}
		}*/
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;

			player.addStat(StatsAchievements.playTime, 1);
			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP p = (EntityPlayerMP)player;
				int val = p.func_147099_x().writeStat(StatsAchievements.playTime);
				//System.out.println("Player has played for " + val + " ticks.");
				if(val > yearLength) {
					player.addStat(StatsAchievements.playAYear, 1);
				}
			}
		}
	}

	public EntityAgeTracker getTracker(EntityAnimal animal) {
		EntityAIAging agingTask = null;
		for(Object obj : animal.tasks.taskEntries) {
			EntityAITasks.EntityAITaskEntry task = (EntityAITasks.EntityAITaskEntry)obj;
			if(task.action instanceof EntityAIAging) {
				agingTask = (EntityAIAging) task.action;
			}
		}
		if(agingTask != null) {
			return agingTask.getTracker();
		}
		return null;
		//return tracker.get(animal);
	}

	@SubscribeEvent
	public void onEntityDead(LivingDropsEvent event) {
		if(event.entity.worldObj.isRemote) return;
		if(event.entity instanceof EntityAgeable && ((EntityAgeable)event.entity).isChild()) return;

		ItemStack foodItem;
		ArrayList<EntityItem> drps = new ArrayList<EntityItem>(); 
		if(modifyAnimalDrops) {
			//WildlifeBase.logger.log(Level.INFO, "Droppin' stuff");
			ArrayList<FoodDrops> foodItems = new ArrayList<FoodDrops>();
			int totalLeather = -1;
			int totalWool = -1;
			//event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.iron_door)));
			if(event.entity instanceof EntityCow || event.entity instanceof EntityPig || event.entity instanceof EntityHorse) {
				totalLeather = 0;
			}
			if(event.entity instanceof EntitySheep) {
				totalWool = 0;
			}
			for(EntityItem d:event.drops) {
				//EntityItem d = event.drops.get(di);
				FoodDrops thisDrop = new FoodDrops(d.getEntityItem().getItem(), d.getEntityItem().stackSize);
				if(d.getEntityItem().getItem() instanceof ItemFood) {
					if(!foodItems.contains(thisDrop)) {
						foodItems.add(thisDrop);
					}
					else {
						thisDrop = foodItems.get(foodItems.indexOf(thisDrop));
						thisDrop.num += d.getEntityItem().stackSize;
					}
					//event.drops.remove(di);
				}
				else {
					if(d.getEntityItem().getItem() == Items.leather) {
						if(totalLeather >= 0) {
							totalLeather += d.getEntityItem().stackSize;
						}
						if(doRawLeather) {
							foodItem = new ItemStack(WildlifeBase.itemRawLeather);
							foodItem.stackSize = d.getEntityItem().stackSize;
							EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
							drps.add(e);
						}
						else {
							drps.add(d);
						}
					}
					else if(totalWool >= 0 && d.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.wool)) {
						totalWool += d.getEntityItem().stackSize;
						drps.add(d);
					}
					else {
						drps.add(d);
					}
				}
			}
			event.drops.clear();
			event.drops.addAll(drps);
			if(event.source == DamageSource.wither) return;
			if(foodItems.size() > 0) {
				for(FoodDrops dr : foodItems) {
					foodItem = new ItemStack(dr.dropped);
					if(event.entity instanceof EntityCow){
						//at least 6 per cow
						foodItem.stackSize = 4 + rand.nextInt(7);
					}
					else if(event.entity instanceof EntityGoat) {
						if(((ItemFood)dr.dropped).func_150906_h(foodItem) > 0.5) {
							foodItem = new ItemStack(WildlifeBase.cookedChevon);
						}
						else {
							foodItem = new ItemStack(WildlifeBase.rawChevon);
						}
						foodItem.stackSize = 2 + rand.nextInt(4);
					}
					else if(event.entity instanceof EntitySheep){
						foodItem.stackSize = 2 + rand.nextInt(4);
					}
					else if(event.entity instanceof EntityPig) {
						foodItem.stackSize = 3 + rand.nextInt(3);
					}
					else {
						foodItem.stackSize = dr.num;
					}
					//Pig:   3-5
					//Sheep: 2-5
					//Cow:   4-10
					//other: -as default-
					EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
					event.drops.add(e);
				}
			}
			if(totalLeather >= 0) {
				//8-14: two cows should cover a person
				if(doRawLeather) {
					foodItem = new ItemStack(WildlifeBase.itemRawLeather);
				}
				else {
					foodItem = new ItemStack(Items.leather);
				}
				foodItem.stackSize = 1 + rand.nextInt(2);
				EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);

				foodItem.stackSize = 1 + rand.nextInt(2);
				e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);

				if(event.entity instanceof EntityCow) {
					foodItem.stackSize = 4 + rand.nextInt(3);
					e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
					event.drops.add(e);
				}
				else if(event.entity instanceof EntityHorse) {
					foodItem.stackSize = 1;
					e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
					event.drops.add(e);
				}
				//Pig: 2-4
				//Cow: 6-10
				//Horse: 3-5
			}
			if(totalWool >= 0) {
				if(event.entity instanceof EntitySheep) {
					EntitySheep s = (EntitySheep) event.entity;
					foodItem = new ItemStack(Blocks.wool, 1, s.getFleeceColor());
				}
				else {
					foodItem = new ItemStack(Blocks.wool);
				}
				foodItem.stackSize = 1 + rand.nextInt(3);
				EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);
				//Sheep: 1-3 wool
			}
			if(event.entity instanceof EntitySkeleton) {
				foodItem = new ItemStack(Items.arrow);
				foodItem.stackSize = 3 + rand.nextInt(2);
				EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);
				//Skeletons: 3-6 arrows
				foodItem = new ItemStack(Items.bone);
				foodItem.stackSize = 1;
				e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);
				//Skeletons: 1-3 bones
			}
			if(event.entity instanceof EntitySpider) {
				foodItem = new ItemStack(Items.string);
				foodItem.stackSize = 2;
				EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);
				//Spiders: 2-4
			}
		}
		else if(doRawLeather) {
			for(EntityItem d:event.drops) {
				if(d.getEntityItem().getItem() == Items.leather) {
					foodItem = new ItemStack(WildlifeBase.itemRawLeather);
					foodItem.stackSize = d.getEntityItem().stackSize;
					EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
					drps.add(e);
				}
				else {
					drps.add(d);
				}
			}
			event.drops.clear();
			event.drops.addAll(drps);
		}
		if(event.entity instanceof EntityLizard && event.source.getSourceOfDamage() instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)event.source.getSourceOfDamage();
			p.addStat(StatsAchievements.killLizard, 1);
		}
	}

	@SubscribeEvent
	public void onPickup(PlayerEvent.ItemPickupEvent event) {
		Item item = event.pickedUp.getEntityItem().getItem();
		if(item == WildlifeBase.itemRawLeather) {
			event.player.addStat(StatsAchievements.collectRawhide, 1);
		}
		if(item == WildlifeBase.winterWheatSeeds) {
			event.player.addStat(StatsAchievements.collectWinterWheat, 1);
		}
		if(item == Item.getItemFromBlock(WildlifeBase.rottingWood)) {
			event.player.addStat(StatsAchievements.collectCompost, 1);
		}
	}

	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		Item item = event.crafting.getItem();
		if(item == Item.getItemFromBlock(WildlifeBase.blockTanner)) {
			event.player.addStat(StatsAchievements.craftTanner, 1);
		}
		if(item == WildlifeBase.thermometer || item == WildlifeBase.rainmeter) {
			event.player.addStat(StatsAchievements.craftThermometer, 1);
		}
	}

	@SubscribeEvent
	public void harvestGrass(BlockEvent.HarvestDropsEvent event) {
		if(event.block instanceof BlockTallGrass) {
			BiomeGenBase bio = event.world.getBiomeGenForCoords(event.x, event.z);
			if(biomeTemps.get(bio.biomeID).temp <= 0.3) {
				ArrayList<ItemStack> drps = new ArrayList<ItemStack>();
				for(ItemStack is:event.drops) {
					if(is.getItem() == Items.wheat_seeds) {
						drps.add(new ItemStack(WildlifeBase.winterWheatSeeds, is.stackSize));
					}
					else {
						drps.add(is);
					}
				}
				event.drops.clear();
				event.drops.addAll(drps);
			}
		}
		if(event.block instanceof BlockCrops) {
			Block block = event.world.getBlock(event.x, event.y, event.z - 1);
			Block block1 = event.world.getBlock(event.x, event.y, event.z + 1);
			Block block2 = event.world.getBlock(event.x - 1, event.y, event.z);
			Block block3 = event.world.getBlock(event.x + 1, event.y, event.z);
			Block block4 = event.world.getBlock(event.x - 1, event.y, event.z - 1);
			Block block5 = event.world.getBlock(event.x + 1, event.y, event.z - 1);
			Block block6 = event.world.getBlock(event.x + 1, event.y, event.z + 1);
			Block block7 = event.world.getBlock(event.x - 1, event.y, event.z + 1);

			boolean flag = block2 == Blocks.air || block2 == WildlifeBase.weeds
					|| block3 == Blocks.air || block3 == WildlifeBase.weeds
					|| block == Blocks.air || block == WildlifeBase.weeds
					|| block1 == Blocks.air || block1 == WildlifeBase.weeds
					|| block4 == Blocks.air || block4 == WildlifeBase.weeds
					|| block5 == Blocks.air || block5 == WildlifeBase.weeds
					|| block6 == Blocks.air || block6 == WildlifeBase.weeds
					|| block7 == Blocks.air || block7 == WildlifeBase.weeds;

			boolean flag3 = block2 == Blocks.carpet || block1 == Blocks.carpet
					|| block2 == Blocks.carpet || block3 == Blocks.carpet
					|| block4 == Blocks.carpet || block5 == Blocks.carpet
					|| block6 == Blocks.carpet || block7 == Blocks.carpet;
			if(!flag && !flag3) {
				boolean flag0 = block2 == event.block || block3 == event.block;
				boolean flag1 = block == event.block || block1 == event.block;
				boolean flag2 = block4 == event.block || block5 == event.block || block6 == event.block || block7 == event.block;
				if (!(flag2 || flag0 && flag1)) {
					EntityPlayer p = event.world.getClosestPlayer(event.x, event.y, event.z, 5);
					if(p != null)
						p.addStat(StatsAchievements.cropRotation, 1);
				}
			}
			else if(flag3) {
				EntityPlayer p = event.world.getClosestPlayer(event.x, event.y, event.z, 5);
				if(p != null)
					p.addStat(StatsAchievements.weedSuppressor, 1);
			}
		}
	}

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		if(!event.world.isRemote) {
			/*NBTTagCompound nbt = event.getData();
			List[] entlists = event.getChunk().entityLists;
			for(List l : entlists) {
				for(Object e : l) {
					if(e instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal)e;
						EntityAgeTracker t = tracker.get(animal);//.writeEntityToNBT(nbt);
						if(t == null) {
							t = new EntityAgeTracker();
							t.readEntityFromNBT(nbt, animal);
							tracker.put(animal, t);
						}
						else {
							t.readEntityFromNBT(nbt, animal);
						}
					}
				}
			}*/
			TreeDataHooks.readData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
		}
	}

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event) {
		if(!event.world.isRemote /*&& event.getChunk().isChunkLoaded*/) {
			/*NBTTagCompound nbt = event.getData();
			List[] entlists = event.getChunk().entityLists;
			for(List l : entlists) {
				for(Object e : l) {
					if(e instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal)e;
						EntityAgeTracker t = tracker.get(animal);//
						if(t != null) {
							t.writeEntityToNBT(nbt, animal);
						}
					}
				}
			}*/
			TreeDataHooks.saveData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
		}
	}

	//@SubscribeEvent
	//public void chunkGen(DecorateBiomeEvent.Post event) {
	//	if (trackTrees && !event.world.isRemote && event.world.provider.dimensionId > Integer.MIN_VALUE) {
	//		Chunk c = event.world.getChunkFromBlockCoords(event.chunkX, event.chunkZ);
	//		int cx = c.xPosition;
	//		int cz = c.zPosition;
	/*//int cx = event.worldX / 16;
			//int cz = event.worldZ / 16;
			//System.out.println("Adding trees from DIM" + event.world.provider.dimensionId + ", chunk["+cx+","+cz+"] generation");
			//long start = System.nanoTime();
			if(cx == 15 && cz == 16)
				System.out.println("Generated chunk ["+ event.world.provider.dimensionId +":"+cx+","+cz+"]");
			WildlifeBase.treeCounter.generate(null, cx, cz, event.world);
			//System.out.println(" --End chunk generation tree counting (" + (((System.nanoTime() - start)/1000)/1000f) + "ms)--");*/
	//		TreeDataHooks.addChunkForScan(event.world, cx, cz);
	//	}
	//}

	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		if (trackTrees && !event.world.isRemote && event.world.provider.dimensionId > Integer.MIN_VALUE) {
			Chunk c = event.world.getChunkFromBlockCoords(event.worldX, event.worldZ);
			int cx = c.xPosition;
			int cz = c.zPosition;
			//int cx = event.worldX / 16;
			//int cz = event.worldZ / 16;
			//System.out.println("Adding trees from DIM" + event.world.provider.dimensionId + ", chunk["+cx+","+cz+"] generation");
			//long start = System.nanoTime();
			WildlifeBase.treeCounter.generate(null, cx, cz, event.world);
			//System.out.println(" --End chunk generation tree counting (" + (((System.nanoTime() - start)/1000)/1000f) + "ms)--");
		}
	}

	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		if(!event.world.isRemote) {
			//System.out.println("Clear data ["+event.getChunk().xPosition+","+ event.getChunk().zPosition+"]");
			TreeDataHooks.clearData(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
		}
	}

	@SubscribeEvent
	public void onSaplingItemDead(ItemExpireEvent event) {
		if (autoSaplings) {
			EntityItem ent = event.entityItem;
			ItemStack item = ent.getEntityItem();
			BlockType saplingtype = HardLibAPI.plantManager.getType(item);
			System.out.println("Item " + item.getDisplayName() + " : " + saplingtype);
			if(item.stackSize > 1) {
				ItemStack i2 = item.copy();
				i2.stackSize -= 1;
				EntityItem e = new EntityItem(event.entityItem.worldObj, event.entityItem.posX, event.entityItem.posY+0.25, event.entityItem.posZ, i2);
				e.age = event.entityItem.age - 10;
				event.entityItem.worldObj.spawnEntityInWorld(e);
				item.stackSize = 1;
			}
			if(saplingtype == BlockType.SAPLING || saplingtype == BlockType.SAPLING_ALLWAYS_2x2 || saplingtype == BlockType.SAPLING_SOMETIMES_2x2) {
				Block id = Block.getBlockFromItem(item.getItem());
				World world = ent.worldObj;
				int x = MathHelper.floor_double(ent.posX);
				int y = MathHelper.floor_double(ent.posY);
				int z = MathHelper.floor_double(ent.posZ);
				int probability = 10;
				if(item.getItemDamage() == 0) {
					probability = 14;
				}
				if(item.getItemDamage() == 2) {
					probability = 12;
				}
				if((saplingtype != null)) {
					if((id.canPlaceBlockAt(world, x, y, z)) && (saplingtype == BlockType.SAPLING_ALLWAYS_2x2 || rand.nextInt(probability) < 6)) {
						boolean biomeFlag = true;
						//vanilla spruce tree biome check
						if(item.getItem() == Item.getItemFromBlock(Blocks.sapling) && item.getItemDamage() == 1) {
							biomeFlag = world.getBiomeGenForCoords(x, z).getBiomeClass() == BiomeGenTaiga.class;
						}
						if(saplingtype == BlockType.SAPLING_ALLWAYS_2x2 || (saplingtype == BlockType.SAPLING_SOMETIMES_2x2 && rand.nextInt(4) == 0 && biomeFlag)) {
							handle2x2Placement(ent, item, id, x, y, z);
						}
						else {
							//handle swamp biome issues
							if(BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(x, z), BiomeDictionary.Type.SWAMP) && world.getBlock(x, y, z).getMaterial() == Material.water) {
								boolean placed = false;
								for(int oy = 0; oy <= 3&&!placed; oy++) {//4, 11, 11?
									for(int ox = -5; ox <= 5&&!placed; ox++) {
										for(int oz = -5; oz <= 5&&!placed; oz++) {
											if(world.getBlock(x+ox, y+oy, z+oz) == Blocks.grass) {
												world.setBlock(x+ox, y+oy+1, z+oz, id, item.getItemDamage()|8, 3);
												if(trackTrees)
													TreeDataHooks.addTree(world, x+ox, y+oy+1, z+oz, rand.nextInt(3000));
												placed = true;
											}
										}											
									}										
								}
							}
							else { //standard trees
								world.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
								if(trackTrees)
									TreeDataHooks.addTree(world, x, y, z, rand.nextInt(3000));
							}
						}
					}
					//jungle floor is messy
					else if(item.getItem() == Item.getItemFromBlock(Blocks.sapling) && item.getItemDamage() == 3) {
						if(world.getBlock(x, y-1, z) == Blocks.leaves && world.getBlock(x, y-2, z) == Blocks.grass) {
							world.setBlock(x, y-1, z, id, item.getItemDamage()|8, 3);
							if(trackTrees)
								TreeDataHooks.addTree(world, x, y-1, z, rand.nextInt(3000));
						}
					}
				}
			}
			if(saplingtype == BlockType.REEDS || saplingtype == BlockType.MUSHROOM || saplingtype == BlockType.CACTUS || saplingtype == BlockType.NETHERSTALK) {
				
				Block id = Block.getBlockFromItem(item.getItem());
				World world = ent.worldObj;
				int x = MathHelper.floor_double(ent.posX);
				int y = MathHelper.floor_double(ent.posY);
				int z = MathHelper.floor_double(ent.posZ);

				if(id == Blocks.air) {
					System.out.println("Item " + item.getDisplayName() + " : " + (item.getItem() instanceof IPlantable));
					if(item.getItem() instanceof IPlantable) {
						id = ((IPlantable)item.getItem()).getPlant(world, x, y, z);
					}
					else if(saplingtype == BlockType.REEDS) {
						id = Blocks.reeds;
					}
					else if(saplingtype == BlockType.NETHERSTALK) {
						id = Blocks.nether_wart;
					}
				}
				System.out.println(id);
				if(id != Blocks.air) {
					if(id.canPlaceBlockAt(world, x, y, z) && world.isAirBlock(x, y, z)) {
						world.setBlock(x, y, z, id, item.getItemDamage(), 3);
					}
					else {
						boolean placed = false;
						for(int oy = 0; oy <= 2&&!placed; oy++) {
							for(int ox = -3; ox <= 3&&!placed; ox++) {
								for(int oz = -3; oz <= 3&&!placed; oz++) {
									if(id.canPlaceBlockAt(world, x+ox, y+oy, z+oz) && world.isAirBlock(x+ox, y+oy, z+oz)) {
										world.setBlock(x+ox, y+oy, z+oz, id, item.getItemDamage(), 3);
										//if(--item.stackSize == 0)
										placed = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void handle2x2Placement(EntityItem ent, ItemStack item, Block id, int x, int y, int z) {
		//System.out.println("2x2 code:");
		int area = canMake2x2(ent.worldObj, id, x, y, z);
		if(area >= 1) {
			List<EntityItem> nearbyItems = ent.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x-5, y-2, z-5, x+5, y+2, z+5));//7, 3, 7?
			int count = ent.getEntityItem().stackSize;
			if(nearbyItems.size() > 0) {
				List<EntityItem> sameType = new ArrayList<EntityItem>();
				for(EntityItem ee : nearbyItems) {
					ItemStack eitem = ee.getEntityItem();
					if ((eitem != null) && ((eitem.getItem() instanceof ItemBlock)) && eitem.getItemDamage() == item.getItemDamage() && ee != ent) {
						sameType.add(ee);
						count +=  eitem.stackSize;
					}
				}
				if(count >= 3) {
					//System.out.println("Group-at-once placement");
					int c = ent.getEntityItem().stackSize;
					EntityItem eii;
					Vec3 pos = Vec3.createVectorHelper(x, y, z);
					int bestLight = ent.worldObj.getBlockLightValue(x, y, z);
					for(int i = sameType.size() - 1; i >= 0 && c < 3; i--) {
						eii = sameType.get(i);
						int lightAt = ent.worldObj.getBlockLightValue((int)eii.posX, (int)eii.posY, (int)eii.posZ);
						if(lightAt > bestLight) {
							int atArea = canMake2x2(ent.worldObj, id, (int)eii.posX, (int)eii.posY, (int)eii.posZ);
							if(atArea > 0) {
								pos.xCoord = eii.posX;
								pos.yCoord = eii.posY;
								pos.zCoord = eii.posZ;
							}
						}
						while(eii.getEntityItem().stackSize > 0 && c < 3) {
							c++;
							eii.getEntityItem().stackSize--;
						}
						if(eii.getEntityItem().stackSize == 0) {
							eii.setDead();
						}
					}
					x = (int) pos.xCoord;
					y = (int) pos.yCoord;
					z = (int) pos.zCoord;
					area = canMake2x2(ent.worldObj, id, x, y, z);
					switch(area) {
					case 1:
						ent.worldObj.setBlock(x,   y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x+1, y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x,   y, z+1, id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x+1, y, z+1, id, item.getItemDamage()|8, 3);
						break;
					case 2:
						ent.worldObj.setBlock(x,   y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x+1, y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x,   y, z-1, id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x+1, y, z-1, id, item.getItemDamage()|8, 3);
						break;
					case 3:
						ent.worldObj.setBlock(x,   y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x-1, y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x,   y, z-1, id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x-1, y, z-1, id, item.getItemDamage()|8, 3);
						break;
					case 4:
						ent.worldObj.setBlock(x,   y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x-1, y, z,   id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x,   y, z+1, id, item.getItemDamage()|8, 3);
						ent.worldObj.setBlock(x-1, y, z+1, id, item.getItemDamage()|8, 3);
						break;
					}
					if(trackTrees) {
						TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x+1, y, z, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x-1, y, z, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x, y, z+1, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x, y, z-1, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x+1, y, z+1, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x+1, y, z-1, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x-1, y, z+1, rand.nextInt(3000));
						TreeDataHooks.addTree(ent.worldObj, x-1, y, z-1, rand.nextInt(3000));
					}
				}
				else {
					//System.out.println("Not enough entities, placing near others");
					boolean placed = false;
					for(int oy = -2; oy <= 2&&!placed; oy++) {//5, 11, 11?
						for(int ox = -5; ox <= 5&&!placed; ox++) {
							for(int oz = -5; oz <= 5&&!placed; oz++) {
								if(ent.worldObj.getBlock(x+ox, y+oy, z+oz) instanceof BlockSapling) {
									placed = placeAdjacent(ent.worldObj, id, item.getItemDamage(), x+ox, y+oy, z+oz);
									complete2x2(ent.worldObj, id, item.getItemDamage(), x+ox, y+oy, z+oz);
								}
							}											
						}										
					}
					if(!placed) {
						//System.out.println("Nothing near " + x + "," + y + "," + z);
						ent.worldObj.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
						if(trackTrees)
							TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
					}
				}
			}
			else {
				//System.out.println("Placing near others B");
				boolean placed = false;
				for(int oy = -2; oy <= 2&&!placed; oy++) {//5, 11, 11?
					for(int ox = -5; ox <= 5&&!placed; ox++) {
						for(int oz = -5; oz <= 5&&!placed; oz++) {
							if(ent.worldObj.getBlock(x+ox, y+oy, z+oz) instanceof BlockSapling) {
								placed = placeAdjacent(ent.worldObj, id, item.getItemDamage(), x+ox, y+oy, z+oz);
								complete2x2(ent.worldObj, id, item.getItemDamage(), x+ox, y+oy, z+oz);
							}
						}											
					}										
				}
				if(!placed) {
					ent.worldObj.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
					if(trackTrees)
						TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
				}
			}
		}
		else if(item.getItemDamage() != 5) {
			//System.out.println("No 2x2 available");
			ent.worldObj.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
			if(trackTrees)
				TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
		}
		else {
			//System.out.println("No valid 2x2 centered @ (" +x + "," + y + "," + z +")");
		}
	}

	private boolean placeAdjacent(World worldObj, Block id, int itemDamage, int x, int y, int z) {
		if((id.canPlaceBlockAt(worldObj, x+1, y, z) || worldObj.getBlock(x+1, y, z) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x, y, z+1) || worldObj.getBlock(x, y, z+1) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x+1, y, z+1) || worldObj.getBlock(x+1, y, z+1) instanceof BlockSapling)) {
			//System.out.println("Place adjacent A");
			if(worldObj.getBlock(x+1, y, z) == Blocks.air) {
				worldObj.setBlock(x+1, y, z, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x, y, z+1) == Blocks.air) {
				worldObj.setBlock(x, y, z+1, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x+1, y, z+1) == Blocks.air) {
				worldObj.setBlock(x+1, y, z+1, id, itemDamage|8, 3);
				return true;
			}
		}
		if((id.canPlaceBlockAt(worldObj, x-1, y, z) || worldObj.getBlock(x-1, y, z) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x, y, z+1) || worldObj.getBlock(x, y, z+1) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x-1, y, z+1) || worldObj.getBlock(x-1, y, z+1) instanceof BlockSapling)) {
			//System.out.println("Place adjacent B");
			if(worldObj.getBlock(x-1, y, z) == Blocks.air) {
				worldObj.setBlock(x-1, y, z, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x, y, z+1) == Blocks.air) {
				worldObj.setBlock(x, y, z+1, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x-1, y, z+1) == Blocks.air) {
				worldObj.setBlock(x-1, y, z+1, id, itemDamage|8, 3);
				return true;
			}
		}
		if((id.canPlaceBlockAt(worldObj, x-1, y, z) || worldObj.getBlock(x-1, y, z) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x, y, z-1) || worldObj.getBlock(x, y, z-1) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x-1, y, z-1) || worldObj.getBlock(x-1, y, z-1) instanceof BlockSapling)) {
			//System.out.println("Place adjacent C");
			if(worldObj.getBlock(x-1, y, z) == Blocks.air) {
				worldObj.setBlock(x-1, y, z, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x, y, z-1) == Blocks.air) {
				worldObj.setBlock(x, y, z-1, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x-1, y, z-1) == Blocks.air) {
				worldObj.setBlock(x-1, y, z-1, id, itemDamage|8, 3);
				return true;
			}
		}
		if((id.canPlaceBlockAt(worldObj, x+1, y, z) || worldObj.getBlock(x+1, y, z) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x, y, z-1) || worldObj.getBlock(x, y, z-1) instanceof BlockSapling) && (id.canPlaceBlockAt(worldObj, x+1, y, z-1) || worldObj.getBlock(x+1, y, z-1) instanceof BlockSapling)) {
			//System.out.println("Place adjacent D");
			if(worldObj.getBlock(x+1, y, z) == Blocks.air) {
				worldObj.setBlock(x+1, y, z, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x, y, z-1) == Blocks.air) {
				worldObj.setBlock(x, y, z-1, id, itemDamage|8, 3);
				return true;
			}
			else if(worldObj.getBlock(x+1, y, z-1) == Blocks.air) {
				worldObj.setBlock(x+1, y, z-1, id, itemDamage|8, 3);
				return true;
			}
		}
		return false;
	}

	private int canMake2x2(World worldObj, Block id, int x, int y, int z) {
		boolean xp = id.canPlaceBlockAt(worldObj, x+1, y, z) || worldObj.getBlock(x+1, y, z) == id;
		boolean xn = id.canPlaceBlockAt(worldObj, x-1, y, z) || worldObj.getBlock(x-1, y, z) == id;
		boolean zp = id.canPlaceBlockAt(worldObj, x, y, z+1) || worldObj.getBlock(x, y, z+1) == id;
		boolean zn = id.canPlaceBlockAt(worldObj, x, y, z-1) || worldObj.getBlock(x, y, z-1) == id;
		boolean xpzp = id.canPlaceBlockAt(worldObj, x+1, y, z+1) || worldObj.getBlock(x+1, y, z+1) == id;
		boolean xpzn = id.canPlaceBlockAt(worldObj, x+1, y, z-1) || worldObj.getBlock(x+1, y, z-1) == id;
		boolean xnzp = id.canPlaceBlockAt(worldObj, x-1, y, z+1) || worldObj.getBlock(x-1, y, z+1) == id;
		boolean xnzn = id.canPlaceBlockAt(worldObj, x-1, y, z-1) || worldObj.getBlock(x-1, y, z-1) == id;

		if(xp && zp && xpzp) {
			return 1;
		}
		if(xp && zn && xpzn) {
			return 2;
		}
		if(xn && zn && xnzn) {
			return 3;
		}
		if(xn && zp && xnzp) {
			return 4;
		}
		return 0;
	}

	private void complete2x2(World world, Block id, int meta, int x, int y, int z) {
		//System.out.println("Checking 2x2: " + x + "," + y + "," + z);
		boolean xp = world.getBlock(x+1, y, z) == Blocks.sapling;
		boolean xn = world.getBlock(x-1, y, z) == Blocks.sapling;
		boolean zp = world.getBlock(x, y, z+1) == Blocks.sapling;
		boolean zn = world.getBlock(x, y, z-1) == Blocks.sapling;
		boolean xpzp = world.getBlock(x+1, y, z+1) == Blocks.sapling;
		boolean xpzn = world.getBlock(x+1, y, z-1) == Blocks.sapling;
		boolean xnzp = world.getBlock(x-1, y, z+1) == Blocks.sapling;
		boolean xnzn = world.getBlock(x-1, y, z-1) == Blocks.sapling;

		int t = (xp?1:0) + (zp?1:0) + (xpzp?1:0);
		//System.out.println("aCompleting with " + t);
		if(t == 2) {
			/*System.out.println(" - " + xp);
			System.out.println(" - " + zp);
			System.out.println(" - " + xpzp);*/

			if(id.canPlaceBlockAt(world,x+1, y, z))
				world.setBlock(x+1, y, z,   id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x,   y, z+1))
				world.setBlock(x,   y, z+1, id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x+1, y, z+1))
				world.setBlock(x+1, y, z+1, id, meta|8, 3);
			if(trackTrees) {
				TreeDataHooks.addTree(world, x+1, y, z, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x, y, z+1, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x+1, y, z+1, rand.nextInt(3000));
			}
			return;
		}
		t = (xn?1:0) + (zp?1:0) + (xnzp?1:0);
		//System.out.println("bCompleting with " + t);
		if(t == 2) {
			/*System.out.println(" - " + xn);
			System.out.println(" - " + zp);
			System.out.println(" - " + xnzp);*/
			if(id.canPlaceBlockAt(world,x-1, y, z))
				world.setBlock(x-1, y, z,   id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x,   y, z+1))
				world.setBlock(x,   y, z+1, id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x-1, y, z+1))
				world.setBlock(x-1, y, z+1, id, meta|8, 3);
			if(trackTrees) {
				TreeDataHooks.addTree(world, x-1, y, z, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x, y, z+1, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x-1, y, z+1, rand.nextInt(3000));
			}
			return;
		}
		t = (xp?1:0) + (zn?1:0) + (xpzn?1:0);
		//System.out.println("cCompleting with " + t);
		if(t == 2) {
			/*System.out.println(" - " + xp);
			System.out.println(" - " + zn);
			System.out.println(" - " + xpzn);*/
			if(id.canPlaceBlockAt(world,x+1, y, z))
				world.setBlock(x+1, y, z,   id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x,   y, z-1))
				world.setBlock(x,   y, z-1, id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x+1, y, z-1))
				world.setBlock(x+1, y, z-1, id, meta|8, 3);
			if(trackTrees) {
				TreeDataHooks.addTree(world, x+1, y, z, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x, y, z-1, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x+1, y, z-1, rand.nextInt(3000));
			}
			return;
		}
		t = (xn?1:0) + (zn?1:0) + (xnzn?1:0);
		//System.out.println("dCompleting with " + t);
		if(t == 2) {
			/*System.out.println(" - " + xn);
			System.out.println(" - " + zn);
			System.out.println(" - " + xnzn);*/
			if(id.canPlaceBlockAt(world,x-1, y, z))
				world.setBlock(x-1, y, z,   id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x,   y, z-1))
				world.setBlock(x,   y, z-1, id, meta|8, 3);
			if(id.canPlaceBlockAt(world,x-1, y, z-1))
				world.setBlock(x-1, y, z-1, id, meta|8, 3);
			if(trackTrees) {
				TreeDataHooks.addTree(world, x-1, y, z, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x, y, z-1, rand.nextInt(3000));
				TreeDataHooks.addTree(world, x-1, y, z-1, rand.nextInt(3000));
			}
			return;
		}

		return;
	}

	//private float sunHeightVal;


	@SubscribeEvent
	public void clientTickStart(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			World w = WildlifeBase.proxy.getWorld();
			//mod = (mod +1) % 3;
			if(w == null) return;

			//if(lastClientWorldTime == w.getWorldTime()) return;

			//sunHeightVal = (float)Math.sin((w.getCelestialAngle(0)+0.25f)*2*Math.PI);
			boolean initialSet = true;
			if(Math.abs(lastClientWorldTime - w.getWorldTime()) > 500) {
				initialSet = false;
			}
			lastClientWorldTime = w.getWorldTime();

			if((!initialSet || w.getTotalWorldTime() % (this.weekLength * WEEK_MODULO) == 0)) {
				//initialSet = true;
				//Season sea = Season.getSeason(event.world.getTotalWorldTime());
				//System.out.println("In season: " + sea.name());

				float tempMod;
				float rainMod;
				if(doYearCycle) {
					if(Arrays.binarySearch(dimensionBlacklist, w.provider.dimensionId) >= 0) {
						tempMod = 0;
						rainMod = 0;
					}
					else {
						tempMod = getSeasonTemp(lastClientWorldTime);
						rainMod = getSeasonRain(lastClientWorldTime);
					}
				}
				else {
					if(Arrays.binarySearch(dimensionBlacklist, w.provider.dimensionId) >= 0) {
						tempMod = 0;
						rainMod = 0;
					}
					else {
						tempMod = tempStatic;
						rainMod = rainStatic;
					}
				}
				//System.out.println("Time: " + lastClientWorldTime);
				//System.out.println("Temp: " + tempMod);
				//System.out.println("Rain: " + rainMod);
				for(BiomeGenBase bio : allBiomes) {
					BiomeWeatherData dat = biomeTemps.get(bio.biomeID);
					try {
						if(!BiomeDictionary.isBiomeOfType(bio, Type.NETHER)) {
							bio.temperature = dat.temp + (tempMod * dat.tempScale);
							bio.rainfall = dat.rain + (rainMod * dat.rainScale);
							if(bio.rainfall <= 0/* || (bio.temperature > 0.2 && bio.rainfall > 0 && (bio.rainfall*2.5 < bio.temperature))*/) {
								rains.set(bio, false);
								snows.set(bio, false);
							}
							else if(bio.temperature <= 0.2) {
								rains.set(bio, false);
								snows.set(bio, true);
							}
							else {
								rains.set(bio, true);
								snows.set(bio, false);
							}
						}
						else {
							bio.temperature = dat.temp + tempMod;
							bio.rainfall = dat.rain + rainMod/5f;
						}
						if(bio.getClass().getName().contains("twilightforest")) {
							bio.temperature = MathHelper.clamp_float(bio.temperature, 0, 1);
							bio.rainfall = MathHelper.clamp_float(bio.rainfall, 0, 1);
						}
						//System.out.println(bio.getClass().getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			//mod = (mod +1) % 3;
			if(lastWorldTime.containsKey(event.world.provider.dimensionId) && lastWorldTime.get(event.world.provider.dimensionId) == event.world.getWorldTime()) return;

			float sunHeightVal = (float)Math.sin((event.world.getCelestialAngle(0)+0.25f)*2*Math.PI);

			if (autoSaplings && trackTrees) {
				TreeDataHooks.ageTrees(event.world);
			}
			boolean initialSet = true;
			if(!lastWorldTime.containsKey(event.world.provider.dimensionId) || Math.abs(lastWorldTime.get(event.world.provider.dimensionId) - event.world.getWorldTime()) > 500) {
				initialSet = false;
			}
			event.world.getTotalWorldTime();
			long lwt = event.world.getWorldTime();
			lastWorldTime.put(event.world.provider.dimensionId,lwt);

			/*if(!doYearCycle && !initialSet) {
				float tempMod = getSeasonTemp(0);
				float rainMod = getSeasonRain(0);
				for(BiomeGenBase bio : allBiomes) {
					BiomeWeatherData dat = biomeTemps.get(bio.biomeID);
					try {
						if(!BiomeDictionary.isBiomeOfType(bio, Type.NETHER)) {
							bio.temperature = dat.temp + (tempMod * dat.tempScale);
							bio.rainfall = dat.rain + (rainMod * dat.rainScale);
							if(bio.rainfall <= 0) {
								rains.set(bio, false);
								snows.set(bio, false);
							}
							else if(bio.temperature <= 0.2) {
								rains.set(bio, false);
								snows.set(bio, true);
							}
							else {
								rains.set(bio, true);
								snows.set(bio, false);
							}
						}
						else {
							bio.temperature = dat.temp + (tempMod * dat.tempScale);
							bio.rainfall = dat.rain + (rainMod * dat.rainScale);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}*/

			if((!initialSet || event.world.getTotalWorldTime() % (this.weekLength * WEEK_MODULO) == 0)) {
				//initialSet = true;
				//Season sea = Season.getSeason(event.world.getTotalWorldTime());
				//System.out.println("In season: " + sea.name());
				float tempMod;
				float rainMod;
				if(doYearCycle) {
					if(Arrays.binarySearch(dimensionBlacklist, event.world.provider.dimensionId) >= 0) {
						tempMod = 0;
						rainMod = 0;
					}
					else {
						tempMod = getSeasonTemp(lwt);
						rainMod = getSeasonRain(lwt);
					}
				}
				else {
					if(Arrays.binarySearch(dimensionBlacklist, event.world.provider.dimensionId) >= 0) {
						tempMod = 0;
						rainMod = 0;
					}
					else {
						tempMod = tempStatic;
						rainMod = rainStatic;
					}
				}
				/*System.out.println("Time: " + lwt);
				System.out.println("Temp: " + tempMod);
				System.out.println("Rain: " + rainMod);*/
				if(doWeatherLogging) {
					WildlifeBase.logger.log(Level.INFO, "Time: " + lwt);
					WildlifeBase.logger.log(Level.INFO, "Temp: " + tempMod);
					WildlifeBase.logger.log(Level.INFO, "Rain: " + rainMod);
				}
				for(BiomeGenBase bio : allBiomes) {
					BiomeWeatherData dat = biomeTemps.get(bio.biomeID);
					try {
						if(!BiomeDictionary.isBiomeOfType(bio, Type.NETHER)) {
							bio.temperature = dat.temp + (tempMod * dat.tempScale);
							bio.rainfall = dat.rain + (rainMod * dat.rainScale);
							if(bio.rainfall <= 0/* || (bio.temperature > 0.2 && bio.rainfall > 0 && (bio.rainfall*2.5 < bio.temperature))*/) {
								rains.set(bio, false);
								snows.set(bio, false);
							}
							else if(bio.temperature <= 0.2) {
								rains.set(bio, false);
								snows.set(bio, true);
							}
							else {
								rains.set(bio, true);
								snows.set(bio, false);
							}
						}
						else {
							bio.temperature = dat.temp + (tempMod * dat.tempScale);
							bio.rainfall = dat.rain + (rainMod * dat.rainScale);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static float getSeasonRain(long worldTime) {
		double m = (worldTime + (yearLength/3))/(double)yearLength * 2 * Math.PI;
		m = Math.sin(m)*0.4 + 0.2;
		return (float)m;
	}

	public static float getSeasonTemp(long worldTime) {
		double m = worldTime/(double)yearLength * 2 * Math.PI;
		m = Math.sin(m)*0.6/* - 0.2*/;
		return (float)m;
	}

	@SubscribeEvent
	public void onBlockUpdate(SpecialBlockEvent.BlockUpdateEvent event) {
		if(event.block == Blocks.snow_layer || event.block == WildlifeBase.snowyGrass) handleSnow(event);
		else if(event.block == Blocks.ice) handleIce(event);
		else if(event.block instanceof IGrowable || event.block instanceof BlockReed) handleCrops(event);
	}

	public static long getLastWorldTime(int dim) {
		/*if(!doYearCycle) {
			return 0;
		}*/
		if(instance.lastWorldTime.containsKey(dim)) {
			return instance.lastWorldTime.get(dim);
		}
		else {
			return instance.lastClientWorldTime;
		}
	}

	private void handleCrops(SpecialBlockEvent.BlockUpdateEvent event) {
		if(doSlowCrops) {
			World world = event.world;
			int x = event.x;
			int y = event.y;
			int z = event.z;
			int blockMetadata = event.blockMetadata;
			Block block = event.block;
			BiomeGenBase bio = event.biome;
			int rr = 0;
			int inc = 0;
			Block bb;
			if(blockMetadata < 7 && block != Blocks.reeds) {
				for(int ox=-1;ox<=1;ox++) {
					for(int oz=-1;oz<=1;oz++) {
						bb = world.getBlock(x+ox, y, z+oz);
						if(((ox == 0 || oz == 0) && ox != oz) && ((inc == 0 && bb == block) || bb == WildlifeBase.weeds) && world.getBiomeGenForCoords(x+ox, z+oz) == bio) {
							int om = world.getBlockMetadata(x+ox, y, z+oz);
							if(om+1 == blockMetadata || om+2 == blockMetadata) {
								inc += 1;
								world.scheduleBlockUpdate(x+ox, y, z+oz, block, 1);
							}
							if(block != WildlifeBase.weeds && bb == WildlifeBase.weeds) {
								inc += 2;
								world.scheduleBlockUpdate(x+ox, y, z+oz, block, 1);
							}
						}
						else if(block != WildlifeBase.weeds && world.getBlock(x+ox, y-1, z+oz) == Blocks.farmland) {
							if(bb == Blocks.air) {
								if(rand.nextInt(1500) == 0) {
									world.setBlock(x+ox, y, z+oz, WildlifeBase.weeds);
								}
							}
							else if(bb == Blocks.carpet && rand.nextInt(4) == 0) {
								if(rand.nextInt(1500) == 0) {
									Blocks.carpet.dropBlockAsItem(world, x+ox, y, z+oz, world.getBlockMetadata(x+ox, y, z+oz), 0);
									world.setBlock(x+ox, y, z+oz, WildlifeBase.weeds, 0, 3);
								}
							}
						}
						else if(block != WildlifeBase.weeds && (bb == Blocks.tallgrass || bb instanceof BlockCrops)) {
							inc += 1;
						}
					}
				}
			}

			/*if(inc) {
				event.setCanceled(true);
			}
			else {*/
			if(doBiomeCrops) {
				float t = bio.temperature;
				float r = bio.rainfall;
				if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
					t += getSeasonTemp(getLastWorldTime(world.provider.dimensionId)) * 0.333f;
				}

				if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != world.getPrecipitationHeight(x, z) > y) {
					//if the crop is inside, halve the effects of climate.
					//nether is treated in reverse
					t = (t + 0.8f)/2f;
					r = (r + 1)/2f;
				}
				if(block instanceof IPlantable) {
					Block block2 = ((IPlantable)block).getPlant(world, x, y, z);
					if(block != block2)
						block = block2;
				}
				CropWeatherOffsets o = HardLibAPI.cropManager.getCropOffsets(block);
				if(block instanceof ICropDataSupplier) {
					o = ((ICropDataSupplier)block).getCropData(world, x, y, z);
				}
				if(o != null) {
					if(o.tempTimeOffset != 0) {
						if(doYearCycle)
							t = t - getSeasonTemp(getLastWorldTime(world.provider.dimensionId)) + getSeasonTemp(getLastWorldTime(world.provider.dimensionId) + o.tempTimeOffset);
						//else
						//	t = t + getSeasonTemp(o.tempTimeOffset);
					}
					t += o.tempFlat;
					if(o.rainTimeOffset != 0) {
						if(doYearCycle)
							r = r - getSeasonRain(getLastWorldTime(world.provider.dimensionId)) + getSeasonRain(getLastWorldTime(world.provider.dimensionId) + o.rainTimeOffset);
						//else
						//	r = r + getSeasonRain(o.tempTimeOffset);
					}
					r += o.rainFlat;
				}

				//simplifies the following equation
				t -= 0.4f;
				r -= 1.75;

				//rr = (int) Math.round((Math.pow(t-1.5f,4)*0.6f+(t*t*2))-((2.2*r*r)+0.3f*Math.pow(r+2,4)))+7;
				rr = (int) Math.round((Math.pow(t-1.5f,4)*0.6f)+(t*t*2)-((r*r*-2.2)-0.3f*Math.pow(r+2f,4)+7));
				if(rr < 0) rr--;
				if(rr < -8) rr = -8;
				if(rr > cropsWorst) rr = cropsWorst;
				//if(inc) {
				rr += 2*inc;
				//}
				//t += 0.4f;
				//r += 1.75;
				//System.out.println("Crop "+block.getUnlocalizedName()+" chance: 1/" + (10+rr) + "[t="+bio.temperature+"("+t+"),r=" + bio.rainfall + "(" + r + ")]");
			}
			if(block == WildlifeBase.weeds) {
				if(rr >= 0) rr /= 2;
				else rr -= 4;
				if(rr < -9) rr = -9;
			}
			if(block == Blocks.reeds) {
				rr -= 3;
				if(rr < -8) rr = -8;
			}

			if(rand.nextInt(10+rr) != 0) {
				event.setCanceled(true);
			}
			//}
	}
	}

	private void handleIce(SpecialBlockEvent.BlockUpdateEvent event) {
		if(doSnowMelt) {
			int light = event.world.getSavedLightValue(EnumSkyBlock.Sky, event.x, event.y, event.z);
			if(light <= 7) return;

			float temp = event.biome.temperature;
			float hot = 0;
			int adj = 0;
			int r = 0;
			if(event.biome.temperature > 0.3) {
				hot = 0.5f;
			}
			else {
				Block b;
				for(int ox=-2; ox <= 2; ox++) {
					for(int oz=-2; oz <= 2; oz++) {
						b = event.world.getBlock(event.x+ox, event.y, event.z+oz);
						if(b == Blocks.ice) { //ice is cold, stay frozen
							hot -= 0.0020f;
							if(Math.abs(ox) <= 1 && Math.abs(oz) <= 1) { //nearby counts for more
								hot -= 0.005f;
								adj++;
							}
							//cold++;
						}
						else if(b.isOpaqueCube()) {
							if(temp < 0.3) { //dirt is cold if the biome is cold, stay frozen
								hot -= 0.01f;
							}
							//cold++;
						}
						else { //things are warm, melt a little
							hot += 0.0015f;
							//hotb++;
							if(Math.abs(ox) <= 1 && Math.abs(oz) <= 1) { //nearby counts for more
								hot += 0.015f;
							}
						}
					}	
				}
				hot = hot/16f*9f;

				//if outside/near outside, take into account biome weather
				if(light > 7) {
					hot += (temp);
				}
				//if the sun can shine directly on it, melt based on the sun
				float sunHeightVal = (float)Math.sin((event.world.getCelestialAngle(0)+0.25f)*2*Math.PI);
				if(event.world.canBlockSeeTheSky(event.x, event.y+1, event.z)) {
					hot += 0.01f + 0.25f*(sunHeightVal*(0.3f - (Math.min(event.biome.rainfall, 1f) / 4)));
				}
				r = (int)Math.min((hot - 0.2f) * 24, 6) + (adj == 8?2:0) - (adj > 8?256:0) + (adj <= 1?8:0);
				if(r > 9) r = 9;
				//if((hot >= 0.19f))
				//System.out.println(event.biome.temperature + "," + hot + "," + adj + ":" + r);
				//if(hot > 1) System.out.println(" - " + event.x + " " + event.y + " " + event.z);
				//might be too slow
			}
			if(hot > 0.4 || (hot >= 0.175f && rand.nextInt(10 - r) == 0)) {
				event.world.setBlock(event.x, event.y, event.z, Blocks.water);
				if(adj >= 8) {
					for(int ox=-1; ox <= 1; ox++) {
						for(int oz=-1; oz <= 1; oz++) {
							event.world.scheduleBlockUpdate(event.x+ox, event.y, event.z+oz, Blocks.water, 20);
						}
					}
				}
			}
			else if(event.biome.temperature >= 0.2) {
				int m = event.world.getBlockMetadata(event.x, event.y, event.z);
				if(m < 5 && adj >= 3) {
					event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, m+1, 3);
				}
				else {
					event.world.setBlock(event.x, event.y, event.z, Blocks.water);
					if(adj >= 8) {
						for(int ox=-1; ox <= 1; ox++) {
							for(int oz=-1; oz <= 1; oz++) {
								event.world.scheduleBlockUpdate(event.x+ox, event.y, event.z+oz, Blocks.water, 20);
							}
						}
					}
				}
			}
		}
	}

	private void handleSnow(SpecialBlockEvent.BlockUpdateEvent event) {
		boolean inc = true;
		int mm;
		int wet;
		Block b;
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		int blockMetadata = event.blockMetadata;
		BiomeGenBase biome = event.biome;
		Block block = event.block;
		if(biome.temperature < 0.15f && world.isRaining()) {
			wet = Math.round(biome.rainfall * 2);
			if(wet > 6) wet = 6;
			if(rand.nextInt(8 - wet) == 0 && doSnowMelt) {
				for(int ox = -1; ox <= 1 && inc; ox++) {
					for(int oz = -1; oz <= 1 && inc; oz++) {
						b = world.getBlock(x+ox, y, z+oz);
						if(b == Blocks.snow_layer || b == WildlifeBase.snowyGrass) {
							mm = world.getBlockMetadata(x+ox, y, z+oz);
							if(mm < blockMetadata) {
								inc = false;
								world.setBlockMetadataWithNotify(x+ox, y, z+oz, mm+1, 3);
							}
						}
						else if(b.getRenderType() == 1 || b == Blocks.carpet) {
							inc = false;
							mm = world.getBlockMetadata(x+ox, y, z+oz);
							if(doYearCycle || !(b instanceof BlockSapling)) {
								world.setBlock(x+ox, y, z+oz, WildlifeBase.snowyGrass, 0, 3);
								TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x+ox, y, z+oz);
								if(te != null) {
									te.oBlock = b;
									te.oMeta = mm;
									world.markBlockForUpdate(x+ox, y, z+oz);
								}
								else {
									world.setBlock(x+ox, y, z+oz, b, mm, 3);
								}
							}
						}
						else if(b == Blocks.air && world.getBlock(x+ox, y-1, z+oz).isSideSolid(world, x+ox, y-1, z+oz, ForgeDirection.UP)) {
							inc = false;
							world.setBlock(x+ox, y, z+oz, Blocks.snow_layer);
						}
						/*else if(b == Blocks.fence) {
							inc = false;
							mm = world.getBlockMetadata(x+ox, y, z+oz);
							boolean flag = (16777215 != b.getRenderColor(mm));
							world.setBlock(x+ox, y, z+oz, WildlifeBase.snowyFence, 0, 3);
							TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x+ox, y, z+oz);
							te.oBlock = b;
							te.oMeta = mm;
							te.biomeColor = flag;
							world.markBlockForUpdate(x+ox, y, z+oz);
						}*/
					}	
				}
				if(inc && blockMetadata < 7) {
					world.setBlockMetadataWithNotify(x, y, z, blockMetadata+1, 3);
				}
			}
		}
		else {
			wet = Math.round((world.isRaining()?0.1f:0f) + (biome.temperature + ((float)Math.sin((world.getCelestialAngle(0)+0.25f)*2*Math.PI)*(0.3f - (Math.min(biome.rainfall, 1f) / 4)))) * 4);
			if(wet > 10)/**/ wet = 10;
			BiomeWeatherData dat = biomeTemps.get(biome.biomeID);
			if(rand.nextInt(12-wet) == 0 && (doSnowMelt || (blockMetadata&7) > 0 || biome.temperature > 0.2)) {
				for(int ox = -1; ox <= 1 && inc; ox++) {
					for(int oz = -1; oz <= 1 && inc; oz++) {
						b = world.getBlock(x+ox, y, z+oz);
						if(b == Blocks.snow_layer || b == WildlifeBase.snowyGrass) {
							mm = world.getBlockMetadata(x+ox, y, z+oz);
							if(mm > blockMetadata) {
								inc = false;
								world.setBlockMetadataWithNotify(x+ox, y, z+oz, mm-1, 3);
							}
						}
					}
				}
				if(inc) {
					if(blockMetadata > 0) {
						world.setBlockMetadataWithNotify(x, y, z, blockMetadata-1, 3);
					}
					else if(biome.temperature > 0.3f && (blockMetadata) == 0) {
						if(block == WildlifeBase.snowyGrass) {
							TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x, y, z);
							if(te.oBlock == null) {
								te.oBlock = Blocks.tallgrass;
								if(blockMetadata >= 8) {
									te.oMeta = 2;
								}
								else {
									te.oMeta = 1;
								}
							}
							world.setBlock(x, y, z, te.oBlock, te.oMeta, 3);
							if(te.oBlock == Blocks.sapling) {
								TreeDataHooks.addTree(world, x, y, z, -1);
							}
						}
						else {
							world.setBlockToAir(x, y, z);
						}
					}
					else if(block == WildlifeBase.snowyGrass && blockMetadata == 0) {
						TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x, y, z);
						if(te.oBlock == null) {
							te.oBlock = Blocks.tallgrass;
							if(blockMetadata >= 8) {
								te.oMeta = 2;
							}
							else {
								te.oMeta = 1;
							}
						}
						world.setBlock(x, y, z, te.oBlock, te.oMeta, 3);
						if(te.oBlock == Blocks.sapling) {
							TreeDataHooks.addTree(world, x, y, z, -1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onMilkCow(EntityAnimalInteractEvent.CowMilkEvent event) {
		if(event.entity instanceof EntityCow) {
			EntityAIMilking milkingTask = null;
			EntityCow theCow = (EntityCow) event.entity;
			for(Object obj : theCow.tasks.taskEntries) {
				EntityAITasks.EntityAITaskEntry task = (EntityAITasks.EntityAITaskEntry)obj;
				if(task.action instanceof EntityAIMilking) {
					milkingTask = (EntityAIMilking) task.action;
				}
			}
			//System.out.println(milkingTask);
			//if(milkingTask == null) {
			//	milkingTask = new EntityAIMilking(theCow);
			//	theCow.tasks.addTask(0, milkingTask);
			//}
			boolean valid = milkingTask.getMilkable();
			//WildlifeBase.instance.logger.log(Level.INFO, "Event is valid? " + valid);
			//System.out.println(valid);
			if(valid) {
				milkingTask.doMilking();
			}
			else {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onAnimalFeed(EntityAnimalInteractEvent.AnimalLoveEvent event) {
		//System.out.println("Fed an animal");
		event.loveTime *= 2;
		if(event.animalBred == EntityCow.class) {
			if(rand.nextInt(8) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		else if(event.animalBred == EntityPig.class) {
			if(rand.nextInt(4) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		else if(event.animalBred == EntityGoat.class) {
			if(rand.nextInt(8) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		else if(event.animalBred == EntitySheep.class) {
			//gany's surface makes sheep drop mutton
			if(Loader.isModLoaded("ganyssurface")) {
				if(rand.nextInt(8) != 0) {
					//System.out.println("Love Canceled");
					event.loveTime = 0;
				}
			}
			else {
				if(rand.nextInt(2) != 0) {
					//System.out.println("Love Canceled");
					event.loveTime = 0;
				}
			}
		}
		else if(event.animalBred == EntityChicken.class) {
			//you can get chickens from eggs and seeds are cheap
			if(rand.nextInt(12) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		else {//1/6th rate for unknown breedable animals.
			if(rand.nextInt(6) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void onBreakCrops(BreakEvent event) {
		if((event.block instanceof BlockCrops || event.block instanceof BlockStem) &&  event.block != WildlifeBase.weeds) {
			ItemStack s = event.getPlayer().getCurrentEquippedItem();
			if(s != null && s.getItem() instanceof ItemHoe) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void itemFrameCompare(ItemFrameComparatorPowerEvent event) {
		if(event.stack == null) return;
		if(event.stack.getItem() == WildlifeBase.thermometer) {
			float f = getThermometerReading(event.entity.worldObj, (int)event.entity.posX, (int)event.entity.posY, (int)event.entity.posZ, event.stack);
			f = Math.round((f+1.35f) * 4);
			f = Math.max(Math.min(f, 15),1);
			event.power = Math.round(f);
		}
		if(event.stack.getItem() == WildlifeBase.rainmeter) {
			float f = getRainmeterReading(event.entity.worldObj, (int)event.entity.posX, (int)event.entity.posY, (int)event.entity.posZ, event.stack);
			f = Math.round((f+1.15f) * 4);
			f = Math.max(Math.min(f, 15),1);
			event.power = Math.round(f);
		}
		if(event.entity instanceof EntityItemFrame) {
			EntityItemFrame entityitemframe = (EntityItemFrame)event.entity;
			int x = (int)entityitemframe.posX;
			int y = (int)entityitemframe.posY;
			int z = (int)entityitemframe.posZ;
			int ox = Direction.offsetX[entityitemframe.hangingDirection] * -2;
			int oz = Direction.offsetZ[entityitemframe.hangingDirection] * -2;
			entityitemframe.worldObj.scheduleBlockUpdate(x+ox, y, z+oz, Blocks.powered_comparator, (this.weekLength * WEEK_MODULO / 2));//recheck twice a week
		}
	}

	public static float getThermometerReading(World world, int x, int y, int z, ItemStack stack) {
		long lwt = getLastWorldTime(world.provider.dimensionId);
		NBTTagCompound stackTagCompound = stack.stackTagCompound;
		BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		float tt = bio.temperature;
		if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
			tt += getSeasonTemp(lwt) * 0.333f;
		}
		if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != world.getPrecipitationHeight(x, z) > y) {
			tt = (tt + 0.8f)/2f;
		}
		if(stackTagCompound != null) {
			Block block = Block.getBlockById(stackTagCompound.getInteger("blockID"));
			CropWeatherOffsets offs = HardLibAPI.cropManager.getCropOffsets(block);
			if(block instanceof ICropDataSupplier && stackTagCompound.hasKey("HasOffsets")) {
				float tf = stackTagCompound.getFloat("tempflat");
				float rf = stackTagCompound.getFloat("tempflat");
				int ot = stackTagCompound.getInteger("raintime");
				int or = stackTagCompound.getInteger("temptime");
				offs = new CropWeatherOffsets(tf,rf,ot,or);
			}
			if(offs.tempTimeOffset != 0) {
				tt = tt - getSeasonTemp(lwt) + getSeasonTemp(lwt + offs.tempTimeOffset);
			}
			tt += offs.tempFlat;
		}
		return tt;
	}

	public static float getRainmeterReading(World world, int x, int y, int z, ItemStack stack) {
		long lwt = getLastWorldTime(world.provider.dimensionId);
		NBTTagCompound stackTagCompound = stack.stackTagCompound;
		BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		float tt = bio.rainfall;
		if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
			tt += WildlifeEventHandler.getSeasonRain(lwt) * 0.333f;
		}
		if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != world.getPrecipitationHeight(x, z) > y) {
			tt = (tt + 1)/2f;
		}
		if(stackTagCompound != null) {
			Block block = Block.getBlockById(stackTagCompound.getInteger("blockID"));
			CropWeatherOffsets offs = HardLibAPI.cropManager.getCropOffsets(block);
			if(block instanceof ICropDataSupplier && stackTagCompound.hasKey("HasOffsets")) {
				float tf = stackTagCompound.getFloat("tempflat");
				float rf = stackTagCompound.getFloat("tempflat");
				int ot = stackTagCompound.getInteger("raintime");
				int or = stackTagCompound.getInteger("temptime");
				offs = new CropWeatherOffsets(tf,rf,ot,or);
			}

			if(offs.rainTimeOffset != 0) {
				tt = tt - getSeasonRain(lwt) + getSeasonRain(lwt + offs.rainTimeOffset);
			}
			tt += offs.rainFlat;
		}
		return tt;
	}

	public static BiomeWeatherData getBiomeData(BiomeGenBase bio) {
		return instance.biomeTemps.get(bio.biomeID);
	}

	private static class FoodDrops {
		public Item dropped;
		public int num;
		private int itemID;

		public FoodDrops(Item i, int n) {
			dropped = i;
			num = n;
			itemID = Item.getIdFromItem(i);
		}

		@Override
		public int hashCode() {
			return itemID;
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof FoodDrops) {
				FoodDrops f = (FoodDrops)o;
				return itemID == f.itemID;
			}
			return false;
		}
	}

	private static class BiomeWeatherData {
		public float temp;
		public float rain;
		public float tempScale;
		public float rainScale;

		/*
		 * TODO: Figure out what mushrooms I was on and fix this.
		 */
		private BiomeWeatherData(float t, float r, float s, float q) {
			if(t < -0.1 && t >= -0.3) {
				t -= 0.1;
			}
			else if(t < -0.3) {
				t = ((t+0.3f)*3f/4f)-0.4f;
			}
			temp = t;
			rain = r;
			tempScale = s;
			rainScale = q;
		}
	}
}
