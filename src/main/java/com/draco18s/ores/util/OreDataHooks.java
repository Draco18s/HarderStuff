package com.draco18s.ores.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.WorldUtils;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.ores.OresBase;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class OreDataHooks {
	private static final int VERSION = 2;
	//private static LoadingCache<ChunkCoordTriplet, HashMap<String,Integer>> graphs;
	//private static File dir;
	//private static RemovalListener<ChunkCoordTriplet, HashMap<String,Integer>> removalListener;
	private static ConcurrentHashMap<ChunkCoordTriplet, HashMap<String,Integer>> graphs = new ConcurrentHashMap<ChunkCoordTriplet, HashMap<String,Integer>>();
	//private static ConcurrentHashMap<ChunkCoordTriplet, Boolean> chunkList = new ConcurrentHashMap<ChunkCoordTriplet, Boolean>();
    
    public static void putOreData(World world, int x, int y, int z, BlockWrapper b, int count) {
    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId,x,y,z);
    	HashMap<String,Integer> value = graphs.get(key);
    	if(value == null) {
    		value = new HashMap<String,Integer>();
        	//value.put(b.getUnlocalizedName(), count);
    	}
    	//else {
    		//count = Math.min(value.get(b.getUnlocalizedName()), count);
        	value.put(b.block.getUnlocalizedName()+":"+b.meta, count);
        	/*if(x == 5 && z == 13 && y < 90 && b.b.getUnlocalizedName().contains("redstone")) {
        		System.out.println(y + "|" + b.b.getUnlocalizedName()+":"+b.m + "=" + count);
        	}*/
    	//}
    	graphs.put(key, value);
    }
    
    public static int getOreData(World world, int x, int y, int z, BlockWrapper b) {
    	Chunk c = world.getChunkFromBlockCoords(x, z);
    	//x = Math.round(x/16f);
    	x = c.xPosition;//x = x>>4
    	y -= (y%8);
    	//z = Math.round(z/16f);
    	z = c.zPosition;//z = z>>4
    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,y,z);
		HashMap<String,Integer> map = graphs.get(key);
		//System.out.println(graphs);
		//System.out.println(map);
		//System.out.println("Looking:  " + key.toString() + "-> " + ((map==null)?"null":map.containsKey(b.b.getUnlocalizedName()+":"+b.m)));
		if(map == null || !map.containsKey(b.block.getUnlocalizedName()+":"+b.meta)) {
			//return 0;
			//OresBase.oreCounter.generate(null, x, z, world);
			//map = graphs.get(key);
			//if(map == null || !map.containsKey(b.b.getUnlocalizedName()+":"+b.m)) {
				//System.out.println("There's still a problem.");
				return 0;
			//}
		}
		
		boolean flag = true;
		for(int q : map.values()) {
			if(q > 0) {
				flag = false;
			}
		}
		if(flag) {
			OresBase.instance.logger.log(Level.INFO,"No data at all for chunk ["+x+","+z+"]");
			OresBase.oreCounter.generate(null, x, z, world);
			map.put("fake_ore_count", 1);
		}
		/*else {
			for(String s : map.keySet()) {
				System.out.println(s + "=" + map.get(s));
			}
		}*/
		map = graphs.get(key);
		//System.out.println("Map: " + map);
		//System.out.println("BW:  " + b);
		//System.out.println("    :" + map.get(b.getUnlocalizedName()));
    	//System.out.println(b.getUnlocalizedName()+world.provider.dimensionId+","+x+","+y+","+z+"->"+map.get(b.getUnlocalizedName()));
    	return map.get(b.block.getUnlocalizedName()+":"+b.meta);
    }
    
    public static void subOreData(World world, int x, int y, int z, Block b, int amount) {
    	x = Math.round(x/16f);
    	y -= (y%8);
    	z = Math.round(z/16f);
    	int k = 0;
    	do {
	    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,y-8*k,z);
	    	HashMap<String,Integer> map = graphs.get(key);
			//System.out.println("Altering: " + key.toString() + "-> " + ((map==null)?"null":map.containsKey(b.getUnlocalizedName())));
			if(map == null || !map.containsKey(b.getUnlocalizedName())) continue;
			//System.out.println("Altering: " + key + "-> " + map.containsKey(b.getUnlocalizedName()));
			int mm = Math.min(map.get(b.getUnlocalizedName()), amount);
			int n = Math.max(map.get(b.getUnlocalizedName()) - amount, 0);
			if(n == 0) n = -1;
	    	map.put(b.getUnlocalizedName(), n);
	    	amount -= mm;
			//System.out.println("    :" + map.get(b.getUnlocalizedName()));
    	} while((++k) < 2 && amount > 0);
    }
    
    public static void readData(World world, int x, int z, NBTTagCompound nbt) {
    	if(nbt.hasKey("HardOreData")) {
    		NBTTagCompound honbt = nbt.getCompoundTag("HardOreData");
    		for(int y=0; y < 256; y+=16) {
	    		if(honbt.hasKey("slice_"+y)) {
		    		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,y,z);
    				HashMap<String,Integer> value = new HashMap<String,Integer>();
    				NBTTagCompound snbt = (NBTTagCompound) honbt.getTag("slice_"+y);
    				boolean flag = true;
    				String n;
    				for(int i= 0; flag; ++i) {
    					if(snbt.hasKey("name_"+i)) {
    						n = snbt.getString("name_"+i);
    						value.put(n,snbt.getInteger(n));
    					}
    					else {
    						flag = false;
    					}
    				}
    				graphs.put(key, value);
    				//chunkList.put(key, true);
    			}
    		}
    	}
    	else {
    		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,0,z);
			OresBase.logger.log(Level.INFO, "Chunk " + key + " is missing ore data, it will be rescanned.  Chunks way out on the edge of the world may not save and cause this message to repeat next launch; do not be alarmed.");
    		OresBase.oreCounter.generate(null, x, z, world);
    	}
    }
    
    public static void saveData(World world, int x, int z, NBTTagCompound nbt) {
    	NBTTagCompound honbt = new NBTTagCompound();
    	for(int y=0; y < 256; y+=16) {
	    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,y,z);
	    	HashMap<String,Integer> value = graphs.get(key);
	    	if(value != null) {
		    	NBTTagCompound snbt = new NBTTagCompound();
		    	int i = 0;
		    	for(String k : value.keySet()) {
			    	snbt.setInteger(k,value.get(k));
			    	snbt.setString("name_"+i, k);
			    	++i;
		    	}
		    	honbt.setTag("slice_"+y, snbt);
	    	}
			if(!WorldUtils.isChunkLoaded_noChunkLoading(world, x, z)) {
				//chunkList.remove(key);
				graphs.remove(key);
			}
    	}
    	honbt.setInteger("version", VERSION);
    	nbt.setTag("HardOreData", honbt);
    }

	/*public static void clearData(World world, int x, int z) {
		NBTTagCompound honbt = new NBTTagCompound();
    	for(int y=0; y < 256; y+=16) {
			ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, x,y,z);
	    	//graphs.remove(key);
			chunkList.put(key, false);
    	}
	}*/
}
