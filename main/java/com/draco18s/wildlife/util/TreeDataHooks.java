package com.draco18s.wildlife.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.wildlife.AnimalsBase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TreeDataHooks {
	private static HashMap<ChunkCoordTriplet, ArrayList<Tree>> treeList = new HashMap<ChunkCoordTriplet, ArrayList<Tree>>();
	private static final int VERSION = 1;
	//private static ArrayList<Block> treeBlocks;
	private static ArrayList<Block> logBlocks;
	private static Random rand = new Random();
	private static boolean bbb = false;
	private static boolean concurnetAvoid = false;
	private static HashMap<ChunkCoordTriplet, ArrayList<Tree>> pendingAddList = new HashMap<ChunkCoordTriplet, ArrayList<Tree>>();
	private static ArrayList<ChunkCoordTriplet> pendingRemoveList = new ArrayList<ChunkCoordTriplet>();
	public static int treeLifeMultiplier;
	public static int treeMaxAge;
	
	public static void readData(World world, int cx, int cz, NBTTagCompound data) {
		if(data.hasKey("WildlifeTreeData")) {
    		NBTTagCompound honbt = data.getCompoundTag("WildlifeTreeData");
    		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
			ArrayList<Tree> value = new ArrayList<Tree>();
			int max = honbt.getInteger("numtrees");
    		for(int i=0; i < max; i++) {
	    		if(honbt.hasKey("tree_"+i)) {
    				NBTTagCompound snbt = (NBTTagCompound) honbt.getTag("tree_"+i);
    				Tree t = new Tree(snbt.getInteger("x"),snbt.getInteger("y"),snbt.getInteger("z"),snbt.getInteger("age"));
    				value.add(t);
    				//System.out.println("Read " + t);
    			}
    		}
    		if(value.size() > 0) {
    			if(concurnetAvoid) {
    				pendingAddList.put(key, value);
    			}
    			else {
	    			treeList.put(key, value);
    			}
    		}
    	}
	}

	public static void saveData(World world, int cx, int cz, NBTTagCompound data) {
		NBTTagCompound honbt = new NBTTagCompound();
    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
    	ArrayList<Tree> value = treeList.get(key);
    	if(value != null) {
	    	int i = 0;
	    	for(Tree t : value) {
		    	NBTTagCompound snbt = new NBTTagCompound();
	    		if(t.approximateAge <= 10000) {
		    		snbt.setInteger("x", t.x);
		    		snbt.setInteger("y", t.y);
		    		snbt.setInteger("z", t.z);
		    		snbt.setInteger("age", t.approximateAge);
			    	honbt.setTag("tree_"+i, snbt);
			    	++i;
	    		}
	    	}
	    	honbt.setInteger("numtrees", i);
	    	honbt.setInteger("version", VERSION);
    	}
	    data.setTag("WildlifeTreeData", honbt);
	}

	public static void clearData(World world, int cx, int cz) {
		NBTTagCompound honbt = new NBTTagCompound();
    	ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
	    //treeList.remove(key);
    	if(concurnetAvoid) {
			pendingRemoveList.add(key);
		}
		else {
			treeList.remove(key);
		}
	}

	public static void addTree(World world, int x, int y, int z, int age) {
		int cx = (x - (x%16)) / 16;
    	int cz = (z - (z%16)) / 16;
		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
		ArrayList<Tree> treesl = treeList.get(key);
		if(treesl == null) {
			treesl = new ArrayList<Tree>();
			treeList.put(key, treesl);
		}
		if(age < 0) {
			age = rand.nextInt(treeMaxAge);
		}
		Tree t = new Tree(x, y-1, z, age);
		System.out.println("Adding tree " + t);
		if(treesl.indexOf(t) == -1)
			treesl.add(t);
	}

	public static void ageTrees(World world) {
		if(logBlocks == null) {
			logBlocks = new ArrayList<Block>();
			logBlocks.add(Blocks.log);
			logBlocks.add(Blocks.log2);
		}
		//System.out.println("Current time: " + world.getTotalWorldTime());
		if(world.getTotalWorldTime() % treeLifeMultiplier > 0) return;
		//System.out.println("Current time: " + world.getTotalWorldTime());
		//System.out.println("Num trees: " + treeList.size());
		boolean b = false;
		int numTrees = 0;
		Block blo;
		//int debugCounter = 0;
		
		//HashMap<ChunkCoordTriplet, ArrayList<Tree>> treeList2 = (HashMap<ChunkCoordTriplet, ArrayList<Tree>>) treeList.clone();
		concurnetAvoid = true;
		for(ChunkCoordTriplet cct : treeList.keySet()) {
			//System.out.println(++debugCounter);
			if(cct.dimID == world.provider.dimensionId) {
				ArrayList<Tree> list = treeList.get(cct);
				if(b) {
					//System.out.println(" - " + list.size());
					if(list.size() > 0) {
						Tree t = list.get(0);
						//AnimalsBase.logger.log(Level.INFO, " - " + t);
					}
					//System.out.println(" - " + list.size());
					//System.out.println(" - " + t);
					b = false;
				}
				//System.out.println("Line 123");
				for(int ti = list.size()-1; ti >=0; ti--) {
					numTrees++;
					Tree t = list.get(ti);
					blo = world.getBlock(t.x, t.y+1, t.z);
					if(blo != Blocks.sapling) {
						if(logBlocks.indexOf(blo) == -1) {
							//System.out.println("Line 130");
							list.remove(ti);
						}
						else {
							t.approximateAge += 1;
							if(t.approximateAge >= treeMaxAge) {//make 1 million ticks (41.6 days)
								System.out.println("Killing " + t);
								//System.out.println("Killing " + t + ", Left: " + list.size());
								killTree(world, t);
								list.remove(ti);
							}
							/*else if(t.approximateAge > 10010) {
								System.out.println("Removing tree: " + t);
								destroyTree(world, t);
								list.remove(ti);
								b = true;
							}*/
						}
					}
				}
			}
		}
		treeList.putAll(pendingAddList);
		for(ChunkCoordTriplet key: pendingRemoveList) {
			treeList.remove(key);
		}
		concurnetAvoid = false;
		pendingAddList.clear();
		pendingRemoveList.clear();
	}
	
	private static void killTree(World world, Tree t) {
		int y = t.y+1;
		Tree ttt;
		while(y < 160 && logBlocks.indexOf(world.getBlock(t.x, y, t.z)) >= 0) {
			world.setBlock(t.x, y, t.z, AnimalsBase.rottingWood);
			for(int ox=-1;ox<=1;ox++) {
				for(int oz=-1;oz<=1;oz++) {
					if(logBlocks.indexOf(world.getBlock(t.x+ox, y, t.z+oz)) >= 0) {
						ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
						//System.out.println("Adjacent wood; " + ttt);
						killTree(world, ttt);
					}
				}	
			}
			y++;
		}
		//mostly for acacia trees, but catches any diagonally-above branches
		for(int ox=-1;ox<=1;ox++) {
			for(int oz=-1;oz<=1;oz++) {
				if(logBlocks.indexOf(world.getBlock(t.x+ox, y, t.z+oz)) >= 0) {
					ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
					//System.out.println("Adjacent wood (up 1); " + ttt);
					killTree(world, ttt);
				}
			}	
		}
	}
	
	/*private static void destroyTree(World world, Tree t) {
		int y = t.y+1;
		Tree ttt;
		while(y < 160 && world.getBlock(t.x, y, t.z) == AnimalsBase.rottingWood) {
			AnimalsBase.rottingWood.dropBlockAsItem(world, t.x, y, t.z, 0, 0);
			world.setBlockToAir(t.x, y, t.z);
			for(int ox=-1;ox<=1;ox++) {
				for(int oz=-1;oz<=1;oz++) {
					if(world.getBlock(t.x+ox, y, t.z+oz) == AnimalsBase.rottingWood) {
						ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
						//System.out.println("Adjacent wood; " + ttt);
						destroyTree(world, ttt);
					}
				}	
			}
			y++;
		}
		//mostly for acacia trees, but catches any diagonally-above branches
		for(int ox=-1;ox<=1;ox++) {
			for(int oz=-1;oz<=1;oz++) {
				if(world.getBlock(t.x, y, t.z) == AnimalsBase.rottingWood) {
					ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
					//System.out.println("Adjacent wood (up 1); " + ttt);
					destroyTree(world, ttt);
				}
			}	
		}
	}*/
}
