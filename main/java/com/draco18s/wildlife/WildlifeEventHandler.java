package com.draco18s.wildlife;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import CustomOreGen.Util.CogOreGenEvent;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.events.EntityAnimalInteractEvent;
import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.wildlife.entity.ai.EntityAIAging;
import com.draco18s.wildlife.entity.ai.EntityAgeTracker;
import com.draco18s.wildlife.util.BlockUtils;
import com.draco18s.wildlife.util.TreeDataHooks;
import com.draco18s.wildlife.util.BlockUtils.BlockType;
import com.draco18s.wildlife.util.Tree;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.oredict.OreDictionary;

public class WildlifeEventHandler {
	private ArrayList<EntityAnimal> toConstruct = new ArrayList<EntityAnimal>();
	private HashMap<EntityAnimal, EntityAgeTracker> tracker = new HashMap<EntityAnimal, EntityAgeTracker>();
	//private ArrayList<Chunk> activeChunks = new ArrayList<Chunk>();
	public static boolean autoSaplings;
	public static boolean doYearCycle;
	public static int weekLength;
	public static long yearLength;
	public static boolean doSnowMelt;
	private Random rand = new Random();
	public static Field rains;
	public static Field snows;
	private HashMap<Integer,BiomeWeatherData> biomeTemps = new HashMap<Integer,BiomeWeatherData>();
	
	private int mod = 0;

	@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if(event.entity instanceof EntityAnimal && !event.entity.worldObj.isRemote) {
			toConstruct.add((EntityAnimal) event.entity);
		}
	}
	
	@SubscribeEvent
	public void onEntityTick(LivingUpdateEvent event) {
		if(event.entity instanceof EntityAnimal && !event.entity.worldObj.isRemote) {
			EntityAnimal animal = (EntityAnimal)event.entity;
			if(toConstruct.indexOf(animal) >= 0) {
				EntityAgeTracker t = new EntityAgeTracker();
				animal.tasks.addTask(8, new EntityAIAging(new Random(), animal, event.entity.getClass(), t));
				toConstruct.remove(animal);
				tracker.put(animal, t);
			}
			EntityAgeTracker t = tracker.get(animal);
			if(t != null) {
				t.entityAge++;
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDead(LivingDropsEvent event) {
		if(event.entity.worldObj.isRemote) return;
		//AnimalsBase.logger.log(Level.INFO, "Droppin' stuff");
		ArrayList<FoodDrops> foodItems = new ArrayList<FoodDrops>();
		ArrayList<EntityItem> drps = new ArrayList<EntityItem>(); 
		int totalLeather = -1;
		int totalWool = -1;
		//event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.iron_door)));
		if(event.entity instanceof EntityCow || event.entity instanceof EntityPig) {
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
				if(totalLeather >= 0 && d.getEntityItem().getItem() == Items.leather) {
					totalLeather += d.getEntityItem().stackSize;
				}
				if(totalWool >= 0 && d.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.wool)) {
					totalWool += d.getEntityItem().stackSize;
				}
				drps.add(d);
			}
		}
		event.drops.clear();
		event.drops.addAll(drps);
		ItemStack foodItem;
		if(foodItems.size() > 0) {
			for(FoodDrops dr : foodItems) {
				foodItem = new ItemStack(dr.dropped);
				if(event.entity instanceof EntityCow){
					//at least 6 per cow
					foodItem.stackSize = 6 + rand.nextInt(4) + rand.nextInt(4);
				}
				else if(event.entity instanceof EntitySheep){
					foodItem.stackSize = 1 + rand.nextInt(3);
				}
				else if(event.entity instanceof EntityPig) {
					foodItem.stackSize = 1 + rand.nextInt(3);
				}
				else {
					foodItem.stackSize = dr.num;
				}
				EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem);
				event.drops.add(e);
			}
		}
		if(totalLeather >= 0) {
			//8-14: two cows should cover a person
			foodItem = new ItemStack(Items.leather);
			foodItem.stackSize = 1 + rand.nextInt(3);
			EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
			event.drops.add(e);

			foodItem.stackSize = 1 + rand.nextInt(3);
			e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
			event.drops.add(e);
			
			if(event.entity instanceof EntityCow) {
				foodItem.stackSize = 1 + rand.nextInt(3);
				e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem.copy());
				event.drops.add(e);
			}
			//Pig: 2-6
			//Cow: 3-11
		}
		if(totalWool >= 0) {
			foodItem = new ItemStack(Blocks.wool);
			foodItem.stackSize = 1 + rand.nextInt(3);
			EntityItem e = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, foodItem);
			event.drops.add(e);
		}
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		NBTTagCompound nbt = event.getData();
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
		}
		TreeDataHooks.readData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event) {
		NBTTagCompound nbt = event.getData();
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
		}
		TreeDataHooks.saveData(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData());
	}
	
	@SubscribeEvent
	public void onSaplingItemDead(ItemExpireEvent event) {
		if (autoSaplings) {
			EntityItem ent = event.entityItem;
			//if ((ent.motionX < 0.001D) && (ent.motionZ < 0.001D)) {
				ItemStack item = ent.getEntityItem();
				if ((item != null) && ((item.getItem() instanceof ItemBlock))) {
					Block id = Block.getBlockFromItem(item.getItem());
					World world = ent.worldObj;
					int x = MathHelper.floor_double(ent.posX);
					int y = MathHelper.floor_double(ent.posY);
					int z = MathHelper.floor_double(ent.posZ);
					if((BlockUtils.getType(id) != null) && (id.canPlaceBlockAt(world, x, y, z)) && (item.getItemDamage() == 3 || item.getItemDamage() == 5 || rand.nextInt(10) < 6)) {
						if(item.getItemDamage() == 5 || ((item.getItemDamage() == 3 || item.getItemDamage() == 1) && rand.nextInt(4) == 0)) {
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
												TreeDataHooks.addTree(world, x+ox, y+oy+1, z+oz, rand.nextInt(3000));
												placed = true;
											}
										}											
									}										
								}
							}
							else {
								world.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
								TreeDataHooks.addTree(world, x, y, z, rand.nextInt(3000));
							}
						}
					}
					else if((BlockUtils.getType(id) != null) && item.getItemDamage() == 3) {
						//jungle floor is messy
						if(world.getBlock(x, y-1, z) == Blocks.leaves && world.getBlock(x, y-2, z) == Blocks.grass) {
							world.setBlock(x, y-1, z, id, item.getItemDamage()|8, 3);
							TreeDataHooks.addTree(world, x, y-1, z, rand.nextInt(3000));
						}
					}
				}
			//}
		}
	}
	
	private void handle2x2Placement(EntityItem ent, ItemStack item, Block id, int x, int y, int z) {
		System.out.println("2x2 code:");
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
					TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
				}
				else {
					System.out.println("Not enough entities, placing near others");
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
						System.out.println("Nothing near " + x + "," + y + "," + z);
						ent.worldObj.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
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
					TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
				}
			}
		}
		else if(item.getItemDamage() != 5) {
			//System.out.println("No 2x2 available");
			ent.worldObj.setBlock(x, y, z, id, item.getItemDamage()|8, 3);
			TreeDataHooks.addTree(ent.worldObj, x, y, z, rand.nextInt(3000));
		}
		else {
			System.out.println("No valid 2x2 centered @ (" +x + "," + y + "," + z +")");
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
		System.out.println("Checking 2x2: " + x + "," + y + "," + z);
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
			return;
		}
		
		return;
	}

	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		System.out.println("Adding trees from chunk generation");
		int cx = event.worldX / 16;
		int cz = event.worldZ / 16;
		AnimalsBase.treeCounter.generate(null, cx, cz, event.world);
		System.out.println(" --End chunk generation tree counting--");
	}
	
	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		TreeDataHooks.clearData(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
	}
	
	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent event) {
		if(event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
			mod = (mod +1) % 3;
			if(mod != 0) return;
			
			if (autoSaplings) {
				MinecraftServer.getServer().theProfiler.startSection("WildlifeChunkTick");
				TreeDataHooks.ageTrees(event.world);
				MinecraftServer.getServer().theProfiler.endSection();
			}
			if(doYearCycle && event.world.getTotalWorldTime() % (this.weekLength * 1000) == 0) {
				//Season sea = Season.getSeason(event.world.getTotalWorldTime());
				//System.out.println("In season: " + sea.name());
				if(rains == null || snows == null) {
					setRainSnow();
				}
				BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
				float tempMod = getSeasonTemp(event.world.getWorldTime());
				float rainMod = getSeasonRain(event.world.getWorldTime());
				System.out.println("Time: " + event.world.getWorldTime());
				System.out.println("Temp: " + tempMod);
				System.out.println("Rain: " + rainMod);
				for(BiomeGenBase bio : biomes) {
					if(bio != null) {
						BiomeWeatherData dat = this.biomeTemps.get(bio.biomeID);
						if(dat == null) {
							dat = new BiomeWeatherData(bio.temperature, bio.rainfall);
							biomeTemps.put(bio.biomeID, dat);
						}
						//System.out.println(bio.biomeName + ":" + (bio.temperature + tempMod));
						try {
							bio.temperature = dat.temp + tempMod;
							bio.rainfall = dat.rain + rainMod;
							if(bio.rainfall <= 0 || (bio.temperature > 0.2 && bio.rainfall > 0 && (bio.rainfall*2.5 < bio.temperature))) {
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
							//bio.temperature = -1;//REQUIRED!!!!
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private void setRainSnow() {
		try {
			
		}
		catch(Exception e) {
			System.out.println("Something went wrong changing biome weather settings");
			System.out.println(e);
		}
	}

	private float getSeasonRain(long worldTime) {
		double m = (worldTime + (yearLength/3))/(double)yearLength * 2 * Math.PI;
		m = Math.sin(m)*0.5 + 0.2;
		return (float)m;
	}

	private float getSeasonTemp(long worldTime) {
		double m = worldTime/(double)yearLength * 2 * Math.PI;
		m = Math.sin(m)*0.5 - 0.2;
		return (float)m;
	}
	
	@SubscribeEvent
	public void onSnowUpdate(SpecialBlockEvent.BlockUpdateEvent event) {
		if(event.block != Blocks.snow_layer) return;
		boolean inc = true;
		int mm;
		int wet;
		if(event.biome.temperature < 0.15 && event.world.isRaining()) {
			wet = Math.round(event.biome.rainfall * 2);
			if(wet > 6) wet = 6;
			if(rand.nextInt(8 - wet) == 0 && doSnowMelt) {
				for(int ox = -1; ox <= 1 && inc; ox++) {
					for(int oz = -1; oz <= 1 && inc; oz++) {
						if(event.world.getBlock(event.x+ox, event.y, event.z+oz) == event.block) {
							mm = event.world.getBlockMetadata(event.x+ox, event.y, event.z+oz);
							if(mm < event.blockMetadata) {
								inc = false;
								event.world.setBlockMetadataWithNotify(event.x+ox, event.y, event.z+oz, mm+1, 3);
							}
						}
					}	
				}
				if(inc && event.blockMetadata < 15) {
					event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, event.blockMetadata+1, 3);
				}
			}
		}
		else {
			wet = Math.round(event.biome.temperature * 4);
			if(wet > 12) wet = 12;
			if(event.world.canBlockSeeTheSky(event.x, event.y, event.z) && (event.world.getWorldTime()%24000) < 12000 && rand.nextInt(12) == 0) {
				for(int ox = -1; ox <= 1 && inc; ox++) {
					for(int oz = -1; oz <= 1 && inc; oz++) {
						if(event.world.getBlock(event.x+ox, event.y, event.z+oz) == event.block) {
							mm = event.world.getBlockMetadata(event.x+ox, event.y, event.z+oz);
							if(mm > event.blockMetadata) {
								inc = false;
								event.world.setBlockMetadataWithNotify(event.x+ox, event.y, event.z+oz, mm-1, 3);
							}
						}
					}	
				}
				if(inc) {
					if(event.blockMetadata > 0) {
						event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, event.blockMetadata-1, 3);
					}
					else if(event.biome.temperature > 0.3 && event.blockMetadata == 0) {
						event.world.setBlockToAir(event.x, event.y, event.z);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onAnimalFeed(EntityAnimalInteractEvent.AnimalLoveEvent event) {
		//System.out.println("Fed an animal");
		if(event.animalBred == EntityCow.class) {
			if(rand.nextInt(8) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		if(event.animalBred == EntityPig.class) {
			if(rand.nextInt(4) != 0) {
				//System.out.println("Love Canceled");
				event.loveTime = 0;
			}
		}
		if(event.animalBred == EntitySheep.class) {
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
	}
	
	private class FoodDrops {
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
	
	private class BiomeWeatherData {
		public float temp;
		public float rain;
		
		private BiomeWeatherData(float t, float r) {
			if(t < 0.1) {
				t -= 0.1;
			}
			temp = t;
			rain = r;
		}
	}
}
