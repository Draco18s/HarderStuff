package com.draco18s.hazards;

import java.util.Random;

import net.minecraft.block.Block;
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
import CustomOreGen.Util.CogOreGenEvent;

import com.draco18s.hazards.block.IBreathable;
import com.draco18s.hazards.client.PlayerStatsClient;
import com.draco18s.hazards.entities.PlayerStats;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HazardsEventHandler {
	private int delay = 0;
	private Random rand = new Random();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(UndergroundBase.doVolcanicGas || UndergroundBase.doSmoke && event.phase == Phase.END) {
			++delay;
			if(delay >= 40 && event.player instanceof EntityPlayer && !event.player.worldObj.isRemote) {
				if(event.player.worldObj.provider.isHellWorld) return;
				delay = 0;
				Random rand = new Random();
				int x = (int) event.player.posX;
				int y = (int) event.player.posY;
				int z = (int) event.player.posZ;
				Block b;
				for(int i=-32;i<=32;++i) {
					for(int j=-16;j<=16;++j) {
						for(int k=-32;k<=32;++k) {
							b = event.player.worldObj.getBlock(x+i, y+j, z+k);
							if(UndergroundBase.doVolcanicGas && (b == Blocks.lava || b == Blocks.flowing_lava)) {
								if(rand.nextInt(32) != 0) {
									b = event.player.worldObj.getBlock(x+i, y+j+1, z+k);
									if(b == Blocks.air) {
										event.player.worldObj.setBlock(x+i, y+j+1, z+k, UndergroundBase.blockVolcanicGas, 2, 3);
									}
								}
							}
							else if(UndergroundBase.doSmoke && b == Blocks.fire) {
								if(rand.nextInt(32) != 0) {
									b = event.player.worldObj.getBlock(x+i, y+j+1, z+k);
									if(b == Blocks.air) {
										event.player.worldObj.setBlock(x+i, y+j+1, z+k, UndergroundBase.blockVolcanicGas, 2, 3);
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
										b = event.player.worldObj.getBlock(x+i+rx, y+j, z+k+rz);
										if(b == Blocks.air) {
											event.player.worldObj.setBlock(x+i, y+j+1, z+k, UndergroundBase.blockVolcanicGas, 2, 3);
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
			        
			        int amt = ((IBreathable)block).getAirQuality(event.entityLiving.worldObj.getBlockMetadata(i,j,k));
					
					stats.airRemaining = this.decreaseAirSupply(event.entityLiving, stats.airRemaining, amt);
					if(Math.floor(stats.airRemaining/10f) == 40) {
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 200));
					}
					if (stats.airRemaining <= -200 ) {
						stats.airRemaining = 0;

	                    event.entityLiving.attackEntityFrom(DamageSource.drown, 2.0F);
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 400));
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
			int x = event.worldX / 16;
			int z = event.worldZ / 16;
			replaceStone(x, z, event.world);
		}
	}
	
	public void replaceStone(int chunkX, int chunkZ, World world) {
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		ExtendedBlockStorage[] extStore = chunk.getBlockStorageArray();
		//ExtendedBlockStorage ext = extStore[0];
		int lastY = 0;
		int stoneID = Block.getIdFromBlock(Blocks.stone);
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
				        int i1 = StoneRegistry.getReplacementId(l, 0);
				        
				        if(i1 >= 0) {
				        	//int i1 = Block.getIdFromBlock(StoneRegistry.getReplacement(l, 0));
				        	lsb[y << 8 | z << 4 | x] = (byte)(i1 & 255);

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
				            //ext.setExtBlockMetadata(x, y & 15, z, 0);
				        	//world.setBlock(chunkX*16+x, lastY*16 + y, chunkZ*16+z, UndergroundBase.unstableStone, 0, 2);
				        }
					}
				}
	        }
			ext.setBlockLSBArray(lsb);
			ext.setBlockMSBArray(msb);
			lastY++;
		}
		chunk.setStorageArrays(extStore);
	}
}
