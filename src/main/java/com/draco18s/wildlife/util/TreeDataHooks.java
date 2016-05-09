package com.draco18s.wildlife.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.WorldUtils;
import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.WildlifeEventHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeDataHooks {
	private static ConcurrentHashMap<ChunkCoordTriplet, ArrayList<Tree>> treeList = new ConcurrentHashMap<ChunkCoordTriplet, ArrayList<Tree>>();
	//private static ConcurrentHashMap<ChunkCoordTriplet, Boolean> chunkList = new ConcurrentHashMap<ChunkCoordTriplet, Boolean>();
	
	private static ConcurrentHashMap<ChunkCoordTriplet, Integer> chunksToScan = new ConcurrentHashMap<ChunkCoordTriplet, Integer>();
	
	private static final int VERSION = 2;
	private static ArrayList<Block> logBlocks;
	private static Random rand = new Random();
	private static boolean bbb = false;
	public static int treeLifeMultiplier;
	public static int treeMaxAge;
	private static ArrayList<DeadTree> treesToKill = new ArrayList<DeadTree>();
	private static int treesKillThistick;
	
	public static void addChunkForScan(World world, int cx, int cz) {
		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
		chunksToScan.put(key,0);
	}

	public static void readData(World world, int cx, int cz, NBTTagCompound data) {
		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
		if(data.hasKey("WildlifeTreeData")) {
			NBTTagCompound honbt = data.getCompoundTag("WildlifeTreeData");

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
				treeList.put(key, value);
			}
			if(honbt.hasKey("numdeadtrees")) {
				max = honbt.getInteger("numdeadtrees");
				//System.out.println("Loading remaining dead tree data.");
				for(int i=0; i < max; i++) {
					if(honbt.hasKey("deadtree_"+i)) {
						NBTTagCompound snbt = (NBTTagCompound) honbt.getTag("deadtree_"+i);
						Tree t = new Tree(snbt.getInteger("x"),snbt.getInteger("y"),snbt.getInteger("z"),snbt.getInteger("age"));
						int dim = snbt.getInteger("d");
						DeadTree dt = new DeadTree(DimensionManager.getProvider(dim).worldObj, t);
						if(!treesToKill.contains(dt))
							treesToKill.add(dt);
						//value.add(t);
						//System.out.println("Read " + t);
					}
				}
			}
			
			if(honbt.hasKey("version")) {
				int v = honbt.getInteger("version");
				if(v < VERSION && WildlifeEventHandler.trackTrees) {
					WildlifeBase.logger.log(Level.INFO, "Chunk " + key + " has old tree data version number ("+v+"), current is "+VERSION+".  It will be rescanned for additional trees.  Chunks way out on the edge of the world may not save and cause this message to repeat next launch; do not be alarmed.");
					chunksToScan.put(key,0);
				}
			}
			else {
				WildlifeBase.logger.log(Level.INFO, "Chunk " + key + " is missing tree data version number.  This is likely because it has none, but it will be rescanned to make sure.  Chunks way out on the edge of the world may not save and cause this message to repeat next launch; do not be alarmed.");
				chunksToScan.put(key,0);
			}
		}
		//chunkList.put(key, true);
	}

	public static void saveData(World world, int cx, int cz, NBTTagCompound data) {
		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
		NBTTagCompound honbt = new NBTTagCompound();
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
		}
		if(!WorldUtils.isChunkLoaded_noChunkLoading(world, cx, cz)) {
			//System.out.println("Chunk " + key + " saved and unloaded from memory.");
			//WildlifeBase.logger.log(Level.DEBUG, "Chunk " + key + " saved and unloaded from memory.");
			//chunkList.remove(key);
			treeList.remove(key);
		}
		if(treesToKill.size() > 0) {
			//System.out.println("Saving remaining dead trees.");
			int i = 0;
			for(DeadTree dt : treesToKill) {
				if(dt.world == world && (dt.tree.x >> 4) == cx && (dt.tree.z >> 4) == cz) {
					NBTTagCompound snbt = new NBTTagCompound();
					snbt.setInteger("x", dt.tree.x);
					snbt.setInteger("y", dt.tree.y);
					snbt.setInteger("z", dt.tree.z);
					snbt.setInteger("d", dt.world.provider.dimensionId);
					snbt.setInteger("age", dt.tree.approximateAge);
					honbt.setTag("deadtree_"+i, snbt);
				}
			}
			honbt.setInteger("numdeadtrees", i);
		}
		honbt.setInteger("version", VERSION);
		data.setTag("WildlifeTreeData", honbt);
	}

	/*public static void clearData(World world, int cx, int cz) {
		//NBTTagCompound honbt = new NBTTagCompound();
		ChunkCoordTriplet key = new ChunkCoordTriplet(world.provider.dimensionId, cx,0,cz);
		//WildlifeBase.logger.log(Level.DEBUG, "Marking chunk " + key + " as unloaded.");
		chunkList.put(key, false);
	}*/

	public static void addTree(World world, int x, int y, int z, int age) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
		int cx = chunk.xPosition;
		int cz = chunk.zPosition;
		/*int cx = (x - (x%16)) / 16;
		int cz = (z - (z%16)) / 16;*/
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
		if(treesl.indexOf(t) == -1)
			treesl.add(t);
		//chunkList.put(key, true);
	}

	public static void ageTrees(World world) {
		if(logBlocks == null) {
			logBlocks = new ArrayList<Block>();
			logBlocks.add(Blocks.log);
			logBlocks.add(Blocks.log2);
		}
		if(treesToKill.size() > 0) {
			//System.out.println("Before: " + treesKillThistick);
			//System.out.println("Dead trees remaining to kill: " + treesToKill.size());
			ArrayList<DeadTree> deds = new ArrayList<DeadTree>();
			ListIterator<DeadTree> it = treesToKill.listIterator();
			while(treesKillThistick < 1500 && it.hasNext()) {
				DeadTree dt = it.next();
				killTree(dt.world, dt.tree, it);
				//it.remove();
				deds.add(dt);
			}
			treesToKill.removeAll(deds);
			//System.out.println("Still remaining to kill: " + treesToKill.size());
			//System.out.println("After: " + treesKillThistick);
			//System.out.println("Remain: " + treesToKill.size());
		}
		
		if(!chunksToScan.isEmpty()) {
			Iterator<ChunkCoordTriplet> itt = chunksToScan.keySet().iterator();
			ChunkCoordTriplet cht;
			Chunk ch;
			while(itt.hasNext()) {
				cht = itt.next();
				if(cht.dimID == world.provider.dimensionId) {
					/*ch = world.getChunkFromChunkCoords(cht.posX, cht.posZ);
					if(ch.isChunkLoaded && world.doChunksNearChunkExist(cht.posX*16, 0, cht.posZ*16, 16)) {
						WildlifeBase.treeCounter.rescan(ch.xPosition,ch.zPosition, world);
						itt.remove();
					}*/
					if(WorldUtils.isChunkLoaded_noChunkLoading(world, cht.posX, cht.posZ)) {
						ch = world.getChunkFromChunkCoords(cht.posX, cht.posZ);
						if(world.doChunksNearChunkExist(cht.posX*16, 0, cht.posZ*16, 16)) {
							WildlifeBase.treeCounter.rescan(ch.xPosition,ch.zPosition, world);
							itt.remove();
						}
					}
				}
			}
		}
		
		if(world.getTotalWorldTime() % treeLifeMultiplier > 0) return;
		int numTrees = 0;
		Block blo;

		treesKillThistick = 0;
		Iterator<ChunkCoordTriplet> l = treeList.keySet().iterator();
		while(l.hasNext()) {
			ChunkCoordTriplet cct = l.next();
			//System.out.println(++debugCounter);
			if(cct.dimID == world.provider.dimensionId) {
				if(world.doChunksNearChunkExist(cct.posX*16, cct.posY, cct.posZ*16, 32)) {
					ArrayList<Tree> list = treeList.get(cct);
					Iterator<Tree> it = list.iterator();
					while(it.hasNext()) {
						//for(int ti = list.size()-1; ti >=0; ti--) {
						numTrees++;
						Tree t = it.next();
						//Tree t = list.get(ti);
						blo = world.getBlock(t.x, t.y+1, t.z);
						if(blo != Blocks.sapling) {
							if(logBlocks.indexOf(blo) == -1) {
								//System.out.println("Line 130");
								it.remove();
								//list.remove(ti);
							}
							else {
								t.approximateAge += 1;
								if(t.approximateAge >= treeMaxAge) {//make 1 million ticks (41.6 days)
									//System.out.println("Killing " + t);
									//System.out.println("Killing " + t + ", Left: " + list.size());
									killTree(world, t, null);
									it.remove();
									//list.remove(ti);
								}
								if(blo == Blocks.log && world.getBlockMetadata(t.x, t.y+1, t.z) == 3 && t.approximateAge%2000 == 100 && t.approximateAge < 7000) {
									//System.out.println("Adding coco (" + t.approximateAge + "/" +treeMaxAge+ ")");
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 4);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 5);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 6);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 8);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 10);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 16);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 24);
									}
									if(rand.nextInt(4) == 0) {
										growPod(world, t, 32);
									}
								}
								blo = world.getBlock(t.x, t.y, t.z);
								if(WildlifeEventHandler.doNativeTreeKill && blo != Blocks.grass && blo != Blocks.dirt) {
									t.approximateAge = Math.max(treeMaxAge - 2, t.approximateAge);//(t.approximateAge / 10);
									//killTree(world, t, null);
									//it.remove();
								}
							}
						}
					}
				}
			}
		}
	}

	private static void growPod(World world, Tree t, int offY) {
		if(world.getBlock(t.x, t.y+offY, t.z) != Blocks.log)
			return;
		ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[rand.nextInt(4)+2];
		int m = -1;
		switch(d) {
		case NORTH:
			m = 0;
			break;
		case EAST:
			m = 1;
			break;
		case SOUTH:
			m = 2;
			break;
		case WEST:
			m = 3;
			break;
		default:
			break;
		}
		Block b = world.getBlock(t.x+d.offsetX, t.y+offY, t.z+d.offsetZ);
		if(b == Blocks.air || b == Blocks.vine) {
			world.setBlock(t.x+d.offsetX, t.y+offY, t.z+d.offsetZ, Blocks.cocoa, m, 3);
		}
		else if(b == Blocks.log && world.getBlockMetadata(t.x+d.offsetX, t.y+offY, t.z+d.offsetZ) == 3) {
			b = world.getBlock(t.x+d.offsetX*2, t.y+offY, t.z+d.offsetZ*2);
			if(b == Blocks.air || b == Blocks.vine) {
				world.setBlock(t.x+d.offsetX*2, t.y+offY, t.z+d.offsetZ*2, Blocks.cocoa, m, 3);
			}
		}
	}

	private static int recursionDepth;
	
	public static void itemKillTree(World world, int x, int y, int z) {
		if(world.getBlock(x, y, z) != world.getBlock(x, y-1, z)) {
			Tree t = new Tree(x, y-1, z, 0);
			//System.out.println("Killing: " + t);
			killTree(world, t, null);
		}
	}

	private static void killTree(World world, Tree t, ListIterator it) {
		recursionDepth = 0;
		killTreeDo(world, t, it);
	}
	private static void killTreeDo(World world, Tree t, ListIterator it) {
		recursionDepth++;
		//System.out.println(" --> " + recursionDepth);
		if(treesKillThistick < 2000 && recursionDepth < 35) {
			killTreeDo_do(world, t, it);
		}
		else {
			DeadTree ded = new DeadTree(world, t);
			if(it == null && treesToKill.indexOf(ded) == -1) {
				treesToKill.add(ded);
			}
			else if(treesToKill.indexOf(ded) == -1) {
				it.add(ded);
			}
		}
		recursionDepth--;
	}

	private static void killTreeDo_do(World world, Tree t, ListIterator it) {
		int y = t.y+1;
		Tree ttt;
		//System.out.println(":" + logBlocks.size());
		while(y < 255 && logBlocks.indexOf(world.getBlock(t.x, y, t.z)) >= 0) {
			world.setBlock(t.x, y, t.z, WildlifeBase.rottingWood);
			//System.out.println("Replaced a block");
			treesKillThistick++;
			for(int ox=-1;ox<=1;ox++) {
				for(int oz=-1;oz<=1;oz++) {
					if((ox !=0 || oz != 0) && logBlocks.indexOf(world.getBlock(t.x+ox, y, t.z+oz)) >= 0 && logBlocks.indexOf(world.getBlock(t.x+ox, y-1, t.z+oz)) == -1) {
						ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
						DeadTree ded = new DeadTree(world, ttt);
						if(it == null && treesToKill.indexOf(ded) == -1) {
							treesToKill.add(ded);
						}
						else if(treesToKill.indexOf(ded) == -1) {
							it.add(ded);
						}
					}
				}	
			}
			y++;
		}
		//mostly for acacia trees, but catches any diagonally-above branches
		for(int ox=-1;ox<=1;ox++) {
			for(int oz=-1;oz<=1;oz++) {
				if((ox !=0 || oz != 0) && logBlocks.indexOf(world.getBlock(t.x+ox, y, t.z+oz)) >= 0 && logBlocks.indexOf(world.getBlock(t.x+ox, y-1, t.z+oz)) == -1) {
					ttt = new Tree(t.x+ox, y-1, t.z+oz, t.approximateAge);
					DeadTree ded = new DeadTree(world, ttt);
					if(it == null && treesToKill.indexOf(ded) == -1) {
						treesToKill.add(ded);
					}
					else if(treesToKill.indexOf(ded) == -1) {
						it.add(ded);
					}
				}
			}	
		}
	}

	private static class DeadTree {
		private Tree tree;
		private World world;

		private DeadTree(World w, Tree t) {
			tree = t;
			world = w;
		}

		@Override
		public int hashCode() {
			int h = HashUtils.firstTerm(HashUtils.SEED);
			h = HashUtils.hash(h, (int)world.getSeed());
			h = HashUtils.hash(h, tree.x);
			h = HashUtils.hash(h, tree.y);
			h = HashUtils.hash(h, tree.z);
			return h;
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof DeadTree) {
				DeadTree ot = (DeadTree)o;
				return tree.x == ot.tree.x && tree.y == ot.tree.y && tree.z == ot.tree.z && world == ot.world;
			}
			return false;
		}
	}

	public static void printChunkInfo(EntityPlayer p, Chunk chunk) {
		ChunkCoordTriplet cct = new ChunkCoordTriplet(chunk.worldObj.provider.dimensionId, chunk.xPosition, 0, chunk.zPosition);
		ArrayList<Tree> treesHere = treeList.get(cct);
		IChatComponent chat;
		chat = new ChatComponentText("Trees in chunk " + cct);
		p.addChatMessage(chat);
		if(treesHere != null && treesHere.size() > 0) {
			for(Tree t : treesHere) {
				chat = new ChatComponentText("   " + t);
				p.addChatMessage(chat);
			}
		}
		else {
			chat = new ChatComponentText("   [None]");
			p.addChatMessage(chat);
		}
	}
}
