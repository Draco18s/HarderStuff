package com.draco18s.hazards;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fluids.IFluidBlock;
import CustomOreGen.Util.CogOreGenEvent;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.hazards.block.IBreathable;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.client.PlayerStatsClient;
import com.draco18s.hazards.entities.PlayerStats;
import com.draco18s.wildlife.WildlifeBase;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HazardsEventHandler {
	private int delay = 0;
	private Random rand = new Random();
	public static HazardsEventHandler instance;
	public static boolean sidwaysFallPhysics;
	public static int rockDustUpdateCount;
	public static boolean enableRockDust;
	private HashMap<EntityPlayer,Boolean> playersSwimming = new HashMap<EntityPlayer,Boolean>();
	
	public HazardsEventHandler() {
		instance = this;
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		int x = (int) event.player.posX;
		int y = (int) event.player.posY;
		int z = (int) event.player.posZ;
		World world = event.player.worldObj;
		Block b;
		IFluidBlock f;
		if(/*UndergroundBase.doVolcanicGas &&*/ event.phase == Phase.START /*&& !event.player.worldObj.isRemote*/) {
			/*boolean flag = false;
			b = world.getBlock(x, y, z);
			if(b == Blocks.lava || b == Blocks.flowing_lava) {
				if(((b = world.getBlock(x, y+1, z)) instanceof IFluidBlock && (f = (IFluidBlock)b).getFluid().isGaseous()) || b == Blocks.air) {
					flag = true;
				}
				
				if(flag && (b = world.getBlock(x, y+2, z)) instanceof IFluidBlock && (f = (IFluidBlock)b).getFluid().isGaseous()) {
					event.player.moveEntity(0, 0.1, 0);
					//flag = true;
					//System.out.println("Lava " + flag);
				}
			}*/
			//System.out.println(flag + "&&" + event.player.handleLavaMovement());
			//if(flag && event.player.handleLavaMovement()) {
			//	event.player.moveEntity(0, 0.4, 0);
			//}
			//System.out.println(event.player.motionY + "," + event.player.isAirBorne);
			
			boolean flag = false;
			if(playersSwimming.containsKey(event.player)) {
				flag = playersSwimming.get(event.player);
			}
			if(flag) {
				boolean ingas = event.player.worldObj.isMaterialInBB(event.player.boundingBox.expand(-0.10000000149011612D, 0, -0.10000000149011612D), UndergroundBase.gas);
				//System.out.println("Swimming? " + flag);
				if(ingas){
					if(event.player.worldObj.isMaterialInBB(event.player.boundingBox.expand(-0.10000000149011612D, -0.125, -0.10000000149011612D), Material.lava)) {
						//System.out.println("A " + event.player.motionY);
						event.player.motionY += 0.105;
						//event.player.setPositionAndUpdate(event.player.posX, event.player.posY+0.105f, event.player.posZ);
						//System.out.println("B " + event.player.motionY);
						//event.player.setVelocity(event.player.motionX, event.player.motionY + 0.02, event.player.motionZ);
					}
					else if(event.player.worldObj.isMaterialInBB(event.player.boundingBox.expand(-0.10000000149011612D, -0.125, -0.10000000149011612D), Material.water)) {
						//System.out.println("A " + event.player.motionY);
						event.player.motionY += 0.105;
						//event.player.setPositionAndUpdate(event.player.posX, event.player.posY+0.105f, event.player.posZ);
						//System.out.println("B " + event.player.motionY);
						//event.player.setVelocity(event.player.motionX, event.player.motionY + 0.02, event.player.motionZ);
					}
				}
			}
		}
		
		if((UndergroundBase.doVolcanicGas || UndergroundBase.doSmoke) && event.phase == Phase.END) {
			++delay;
			if(delay >= 40 && event.player instanceof EntityPlayer && !world.isRemote) {
				if(world.provider.isHellWorld) return;
				delay = 0;
				for(int i=-32;i<=32;++i) {
					for(int j=-16;j<=16;++j) {
						for(int k=-32;k<=32;++k) {
							b = world.getBlock(x+i, y+j, z+k);
							if(UndergroundBase.doVolcanicGas && (b == Blocks.lava || b == Blocks.flowing_lava)) {
								if(rand.nextInt(16) == 0) {
									b = world.getBlock(x+i, y+j+1, z+k);
									if(b == Blocks.air) {
										world.setBlock(x+i, y+j+1, z+k, UndergroundBase.blockVolcanicGas, 3, 3);
									}
									else if(b == UndergroundBase.blockVolcanicGas) {
										world.setBlockMetadataWithNotify(x+i, y+j+1, z+k, Math.min(world.getBlockMetadata(x+i, y+j+1, z+k)+2, 7), 3);
										world.scheduleBlockUpdate(x+i, y+j+1, z+k, b, 1);
									}
								}
							}
							else if(UndergroundBase.doSmoke && b == Blocks.fire) {
								if(rand.nextInt(16) == 0) {
									b = world.getBlock(x+i, y+j+1, z+k);
									if(b == Blocks.air) {
										world.setBlock(x+i, y+j+1, z+k, UndergroundBase.blockVolcanicGas, 3, 3);
									}
									else if(b == UndergroundBase.blockVolcanicGas) {
										world.setBlockMetadataWithNotify(x+i, y+j+1, z+k, Math.min(world.getBlockMetadata(x+i, y+j+1, z+k)+2, 7), 3);
										world.scheduleBlockUpdate(x+i, y+j+1, z+k, b, 1);
									}
									else {
										int rx = 0;
										int rz = 0;
										switch(rand.nextInt(4)) {
											case 0:
												rx = 1;
												break;
											case 1:
												rx = -1;
												break;
											case 2:
												rz = 1;
												break;
											case 3:
												rz = -1;
												break;
										}
										b = world.getBlock(x+i+rx, y+j, z+k+rz);
										if(b == Blocks.air) {
											world.setBlock(x+i+rx, y+j, z+k+rz, UndergroundBase.blockVolcanicGas, 3, 3);
										}
										else if(b == UndergroundBase.blockVolcanicGas) {
											world.setBlockMetadataWithNotify(x+i+rx, y+j, z+k+rz, Math.min(world.getBlockMetadata(x+i+rx, y+j, z+k+rz)+2, 7), 3);
											world.scheduleBlockUpdate(x+i+rx, y+j, z+k+rz, b, 1);
										}
									}
								}
							}
						}					
					}	
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityTick(LivingUpdateEvent event) {
		if(UndergroundBase.needsToBreath(event.entityLiving)) {
			PlayerStats stats = PlayerStats.get(event.entityLiving);
			if(stats != null) {
				if(stats.maxAir <= 0) {
					AxisAlignedBB aabb = event.entityLiving.boundingBox;
					if(aabb != null) {
						double volume = (aabb.maxX - aabb.minX)*(aabb.maxY - aabb.minY)*(aabb.maxZ - aabb.minZ);
						int max = (int) Math.max(Math.floor(volume / 0.00216d)*10, 100);
						stats.airRemaining = Math.min(stats.airRemaining,max);
						stats.maxAir = max;
						//System.out.println(event.entityLiving + " (" + aabb + "):" + max);
					}
					else {
						//System.out.println(event.entityLiving + ":unknown");
					}
				}
				if(event.entityLiving.isInsideOfMaterial(UndergroundBase.gas)) {
					double d0 = event.entityLiving.posY + (double)event.entityLiving.getEyeHeight();
			        int i = MathHelper.floor_double(event.entityLiving.posX);
			        int j = MathHelper.floor_float((float)MathHelper.floor_double(d0));
			        int k = MathHelper.floor_double(event.entityLiving.posZ);
			        Block block = event.entityLiving.worldObj.getBlock(i, j, k);

			        if(event.entityLiving instanceof EntityPlayer && ((EntityPlayer)event.entityLiving).capabilities.isCreativeMode) {
			        	return;
			        }
			        
			        int meta = event.entityLiving.worldObj.getBlockMetadata(i,j,k);
			        int amt = ((IBreathable)block).getAirQuality(meta);
					
					stats.airRemaining = this.decreaseAirSupply(event.entityLiving, stats.airRemaining, amt);
					if(Math.floor(stats.airRemaining/10f) == 40) {
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 100));
					}
					if (stats.airRemaining <= -200 ) {
						stats.airRemaining = 0;

	                    event.entityLiving.attackEntityFrom(DamageSource.drown, 1.0F);
	                    if(((IBreathable)block).causesNausea(meta)) {
	                    	event.entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 100));
	                    }
	                }
				}
				else {
					stats.airRemaining = Math.min(stats.airRemaining+50,Math.max(stats.maxAir, 100));
				}
			}
		}
	}
	
	@SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerMP && PlayerStats.get((EntityPlayerMP) event.entity) == null) {
            //PlayerStats.register((EntityPlayerMP) event.entity);
        	if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                this.onEntityConstructingClient(event);
            }
        }
        if(event.entity instanceof EntityLivingBase && PlayerStats.get((EntityLivingBase) event.entity) == null) {
        	PlayerStats.register((EntityLivingBase) event.entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public void onEntityConstructingClient(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityClientPlayerMP && PlayerStatsClient.get((EntityClientPlayerMP) event.entity) == null)
        {
            PlayerStatsClient.register((EntityClientPlayerMP) event.entity);
        }
    }
	
	protected int decreaseAirSupply(EntityLivingBase ent, int air, int amt) {
        for(;amt > 0; amt--) {
        	air = decreaseAirSupply(ent, air);
        }
        return air;
    }
	
	protected int decreaseAirSupply(EntityLivingBase ent, int air) {
        int j = EnchantmentHelper.getRespiration(ent);
        return j > 0 && rand .nextInt(j + 1) > 0 ? air : air - 1;
    }
	
	@SubscribeEvent
	public void chunkGen(CogOreGenEvent event) {
		if(UndergroundBase.doStoneReplace) {
			replaceStone(event.worldX, event.worldZ, event.world);
		}
	}
	
	@SubscribeEvent
	public void onBlockUpdate(SpecialBlockEvent.BlockUpdateEvent event) {
		if(sidwaysFallPhysics && event.block instanceof BlockFalling && !BlockFalling.fallInstantly) {
			UnstableStoneHelper.updateCobble(event.world, event.x, event.y, event.z, rand, event.block, event.blockMetadata);
		}
	}
	
	public void replaceStone(int blockx, int blockz, World world) {
		Block b, bl;
		int m;
		Chunk chunk = world.getChunkFromBlockCoords(blockx, blockz);
		boolean flag;
		for(int x=0; x < 16; ++x) {
			for(int z=0; z < 16; ++z) {
				for(int y=0; y < 256; ++y) {
			        b = chunk.getBlock(x, y, z);
			        m = chunk.getBlockMetadata(x, y, z);
					//b = world.getBlock(chunkX*16+x, y, chunkZ*16+z);
					//m = world.getBlockMetadata(chunkX*16+x, y, chunkZ*16+z);
					bl = HardLibAPI.stoneManager.getReplacement(b, m);
					if(bl != null) {
						flag = (!world.doesBlockHaveSolidTopSurface(world, blockx+x, y-1, blockz+z)) && UnstableStoneHelper.isBlockUnsupported(world, blockx+x, y, blockz+z);
						if(flag) {
							//boolean onlyAir = true;
							for(int oy = 2; flag && oy <= 16; oy++) {
								if(y-oy >= 0 && chunk.getBlock(x, y-oy, z).getMaterial() != Material.air) {
									flag = false;
								}
							}
						}
						if(flag) {
							//leave as vanilla stone
							//chunk.func_150807_a(x, y, z, bl, 8);
						}
						else {
							chunk.func_150807_a(x, y, z, bl, 0);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent event) {
		rockDustUpdateCount = 0;
	}
	
	/*public void replaceStone(int chunkX, int chunkZ, World world) {
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		ExtendedBlockStorage[] extStore = chunk.getBlockStorageArray();
		//ExtendedBlockStorage ext = extStore[0];
		int lastY = 0;
		int stoneID = Block.getIdFromBlock(Blocks.stone);
		Block bl;
		for(ExtendedBlockStorage ext : extStore) {
			if(ext == null) {
				lastY++;
				continue;
			}
			byte[] lsb = ext.getBlockLSBArray();
			NibbleArray msb = ext.getBlockMSBArray();
			for(int x=0; x < 16; ++x) {
				for(int z=0; z < 16; ++z) {
					for(int y=0; y < 16; ++y) {
						int l = lsb[y << 8 | z << 4 | x] & 255;
			
				        if (msb != null) {
				            l |= msb.get(x, y, z) << 8;
				        }
				        int m = ext.getExtBlockMetadata(x, y, z);
				        //int i1 = StoneRegistry.getReplacementId(l, m);
				        bl = StoneRegistry.getReplacement(l, m);
				        
				        if(bl != null) {
				        	//int i1 = Block.getIdFromBlock(StoneRegistry.getReplacement(l, 0));
				        	/*lsb[y << 8 | z << 4 | x] = (byte)(i1 & 255);

				            if (i1 > 255) {
				                if (msb == null) {
				                    msb = new NibbleArray(lsb.length, 4);
				                }
				                msb.set(x, y, z, (i1 & 3840) >> 8);
				            }
				            else if (msb != null) {
				                msb.set(x, y, z, 0);
				            }
				        	//ext.func_150818_a(x, y & 15, z, UndergroundBase.unstableStone);
				            //ext.setExtBlockMetadata(x, y & 15, z, 1);
				        	world.setBlock(chunkX*16+x, lastY*16 + y, chunkZ*16+z, bl, 0, 2);
				        }
					}
				}
	        }
			ext.setBlockLSBArray(lsb);
			ext.setBlockMSBArray(msb);
			lastY++;
		}
		chunk.setStorageArrays(extStore);
	}*/

	public boolean setPlayerSwimming(EntityPlayer player, boolean keyUpdown) {
		//System.out.println("Key down? " + keyUpdown);
		boolean ret = false;
		if(playersSwimming.containsKey(player)) {
			ret = playersSwimming.get(player);
		}
		playersSwimming.put(player, keyUpdown);
		return ret != keyUpdown;
	}
}
