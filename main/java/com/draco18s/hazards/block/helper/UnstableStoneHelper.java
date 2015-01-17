package com.draco18s.hazards.block.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.draco18s.hazards.StoneRegistry;
import com.draco18s.hazards.UndergroundBase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class UnstableStoneHelper {
	private static List<Block> supportBlocks = new ArrayList();
	
	public static void addSupportBlock(Block b) {
		supportBlocks.add(b);
	}

	public static void updateUnstable(World world, int x, int y, int z, Random rand, Block block, int meta) {
    	boolean falling = false;
    	Block s;
    	if(world.doesBlockHaveSolidTopSurface(world, x, y-1, z)) {
			return;
		}
        int dir = 0;
        int ox = 0;
        int oz = 0;
        int i = x;
        int j = y;
        int k = z;
        int m;
        boolean a, b;
        if(y > 0) {
	    	do {
	    		switch(dir) {
	    			case 0:
	    				ox++;
	    				break;
	    			case 1:
	    				oz++;
	    				break;
	    			case 2:
	    				ox--;
	    				break;
	    			case 3:
	    				oz--;
	    				break;
	    		}
	    		s = world.getBlock(x+ox, y, z+oz);
	    		m = world.getBlockMetadata(x+ox, y, z+oz);
	    		a = supportBlocks.indexOf(s) >= 0;
	    		if(StoneRegistry.isUnstableBlock(s) && m != 0) {
	    			a = false;
	    		}
	    		//a = a && !(StoneRegistry.isUnstableBlock(s) && m == 0);
	    		s = world.getBlock(x+ox, y-1, z+oz);
	    		m = world.getBlockMetadata(x+ox, y-1, z+oz);
	    		b = supportBlocks.indexOf(s) >= 0;
	    		if(StoneRegistry.isUnstableBlock(s) && m != 0) {
	    			b = false;
	    		}
	    		b = b || (world.isBlockNormalCubeDefault(x+ox, y-1, z+oz, true) && world.doesBlockHaveSolidTopSurface(world, x+ox, y-1, z+oz));
	    		if(!a || (ox > 5 || ox < -5 || oz > 5 || oz < -5)) {
	    			ox = 0;
	    			oz = 0;
	        		dir++;
	    		}
	    		else if(a && b) {
	    			break;
	    		}
	    	} while(dir >= 0 && dir < 4);
	    	if(dir == 4) {
	    		falling = true;
	    	}
        }
        else {
        	falling = true;
        }
        if(falling) {
        	s = world.getBlock(i+1, j, k);
        	m = world.getBlockMetadata(i+1, j, k);
        	if(s == block || s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 1))
        		world.setBlock(i+1, j, k, block, 2, 3);
        	s = world.getBlock(i-1, j, k);
        	m = world.getBlockMetadata(i-1, j, k);
        	if(s == block || s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 1))
        		world.setBlock(i-1, j, k, block, 2, 3);
        	s = world.getBlock(i, j, k+1);
        	m = world.getBlockMetadata(i, j, k+1);
        	if(s == block || s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 1))
        		world.setBlock(i, j, k+1, block, 2, 3);
        	s = world.getBlock(i, j, k-1);
        	m = world.getBlockMetadata(i, j, k-1);
        	if(s == block || s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 1))
        		world.setBlock(i, j, k-1, block, 2, 3);
        	s = world.getBlock(i, j+1, k);
        	m = world.getBlockMetadata(i, j+1, k);
        	if(s == block || s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 1))
        		world.setBlock(i, j+1, k, block, 2, 3);
        	int x2, y2, z2;
        	for(ox = rand.nextInt(3)+1; ox > 0; ox--) {
	        	x2 = i + rand.nextInt(9)-4;
	        	y2 = j + rand.nextInt(4)+2;
	        	z2 = k + rand.nextInt(9)-4;
	        	drawLine3D(world, block, 1, i, j, k, x2, y2, z2, false);
        	}
        	world.setBlock(i, j, k, block, 3, 3);
        	damageNeighbors(world, i, j, k);
        }
    }
	public static void updateFractured(World world, int x, int y, int z, Random rand, Block block, int meta) {
		Block s;
		if(world.doesBlockHaveSolidTopSurface(world, x, y-1, z)) {
			return;
		}
    	s = world.getBlock(x+1, y, z);
    	int m = world.getBlockMetadata(x+1, y, z);
    	boolean px = (s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 0));
    	s = world.getBlock(x-1, y, z);
    	m = world.getBlockMetadata(x-1, y, z);
    	boolean nx = (s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 0));
    	s = world.getBlock(x, y, z+1);
    	m = world.getBlockMetadata(x, y, z+1);
    	boolean pz = (s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 0));
    	s = world.getBlock(x, y, z-1);
    	m = world.getBlockMetadata(x, y, z-1);
    	boolean nz = (s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 0));
    	/*s = world.getBlock(x, y+1, z);
    	m = world.getBlockMetadata(x, y+1, z);
    	boolean py = (s == Blocks.stone || (StoneRegistry.isUnstableBlock(s) && m == 0));*/
    	if(!(px || nx || pz || nz)) {
        	world.setBlock(x, y, z, block, 3, 3);
        	damageNeighbors(world, x, y, z);
        }
	}
	public static void updateBroken(World world, int x, int y, int z, Random rand, Block block, int meta) {
		boolean falling = false;
    	if(world.isSideSolid(x, y-1, z, ForgeDirection.DOWN)) {
    		return;
    	}
        int t = (world.isSideSolid(x+1, y-1, z, ForgeDirection.DOWN)?1:0);
        t += (world.isSideSolid(x-1, y-1, z, ForgeDirection.DOWN)?1:0);
        t += (world.isSideSolid(x, y-1, z+1, ForgeDirection.DOWN)?1:0);
        t += (world.isSideSolid(x, y-1, z-1, ForgeDirection.DOWN)?1:0);
        if(t < 2) {
        	t *= 2;
            t += (world.isSideSolid(x+1, y-1, z+1, ForgeDirection.DOWN)?1:0);
            t += (world.isSideSolid(x-1, y-1, z-1, ForgeDirection.DOWN)?1:0);
            t += (world.isSideSolid(x-1, y-1, z+1, ForgeDirection.DOWN)?1:0);
            t += (world.isSideSolid(x+1, y-1, z-1, ForgeDirection.DOWN)?1:0);
            if(t < 4) {
            	falling = true;
            }
        }
        if(falling) {
        	world.setBlock(x, y, z, block, 3, 3);
        	damageNeighbors(world, x, y, z);
        }
	}
	public static void updateCobble(World world, int x, int y, int z, Random rand, Block block, int meta) {
		int c = canFall(world, x, y - 1, z);
        if (c > 0) {
            if (c % 2 == 1) {
            	if (canFall(world, x, y - 1, z) > 0 && y >= 0) {
                    byte b0 = 32;

                    if (world.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0))
                    {
                        if (!world.isRemote)
                        {
                        	EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block, meta);
                            //this.func_149829_a(entityfallingblock);
                            world.spawnEntityInWorld(entityfallingblock);
                        }
                    }
                }
            	
            	return;
            }
            else {
                world.setBlockToAir(x, y, z);
                c >>= 1;
                List<Long> a = new ArrayList<Long>();
                if (c % 2 == 1) {
                    a.add((long) 1);
                }

                c >>= 1;

                if (c % 2 == 1) {
                    a.add((long) 2);
                }

                c >>= 1;

                if (c % 2 == 1) {
                    a.add((long) 3);
                }

                c >>= 1;

                if (c % 2 == 1) {
                    a.add((long) 4);
                }

                int r = rand.nextInt(a.size());
                r = (int)(a.get(r) & 15);

                switch (r) {
                    case 1:
                        world.setBlock(x + 1, y - 1, z, block, meta, 2);
                        break;

                    case 2:
                        world.setBlock(x - 1, y - 1, z, block, meta, 2);
                        break;

                    case 3:
                        world.setBlock(x, y - 1, z + 1, block, meta, 2);
                        break;

                    case 4:
                        world.setBlock(x, y - 1, z - 1, block, meta, 2);
                        break;
                }
            }
        }
        /*else if (world.getBlockMetadata(x, y, z) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 1, 0);
        }*/
	}
	private static int canFall(World world, int x, int y, int z) {
		Block[] l = {world.getBlock(x, y, z), world.getBlock(x + 1, y, z), world.getBlock(x - 1, y, z), world.getBlock(x, y, z + 1), world.getBlock(x, y, z - 1)};
		int[] l2 = {0, world.isBlockNormalCubeDefault(x + 1, y + 1, z, false) ? 1 : 0, world.isBlockNormalCubeDefault(x - 1, y + 1, z, false) ? 1 : 0, world.isBlockNormalCubeDefault(x, y + 1, z + 1, false) ? 1 : 0, world.isBlockNormalCubeDefault(x, y + 1, z - 1, false) ? 1 : 0};
        int r = 0;

        for (int i = 0; i < 5; i++) {
            if (l[i] == Blocks.air || l[i] == Blocks.fire) {
                if (l2[i] == 0) {
                    r += (1 << i);
                }
            }
        }

        return r;
	}
	
	public static void drawLine3D(World world, Block block, int meta, int x1, int y1, int z1, int x2, int y2, int z2) {
    	drawLine3D(world, block, meta, x1, y1, z1, x2, y2, z2, false, false);
    }

    public static void drawLine3D(World world, Block block, int meta, int x1, int y1, int z1, int x2, int y2, int z2, boolean incremental) {
    	drawLine3D(world, block, meta, x1, y1, z1, x2, y2, z2, incremental, false);
    }
    
    public static void drawLine3D(World world, Block block, int meta, int x1, int y1, int z1, int x2, int y2, int z2, boolean incremental, boolean resist) {
    	int i, dx, dy, dz, m, n, x_inc, y_inc, z_inc,err_1, err_2, dx2, dy2, dz2, l;
    	float ll;
    	int pixel[] = new int[3];
    	pixel[0] = x1;
    	pixel[1] = y1;
    	pixel[2] = z1;
		dx = x2 - x1;
		dy = y2 - y1;
		dz = z2 - z1;
		x_inc = (dx < 0) ? -1 : 1;
		l = Math.abs(dx);
		y_inc = (dy < 0) ? -1 : 1;
		m = Math.abs(dy);
		z_inc = (dz < 0) ? -1 : 1;
		n = Math.abs(dz);
		dx2 = l << 1;
		dy2 = m << 1;
		dz2 = n << 1;
		float res = 0; 
		if ((l >= m) && (l >= n)) {
		    err_1 = dy2 - l;
		    err_2 = dz2 - l;
			ll = l;
		    for (i = 0; i < ll; i++) {
		    	res = PUT_PIXEL(world, block, meta, pixel, incremental);
		        if (err_1 > 0) {
		            pixel[1] += y_inc;
		            err_1 -= dx2;
		        }
		        if (err_2 > 0) {
		            pixel[2] += z_inc;
		            err_2 -= dx2;
		        }
		        err_1 += dy2;
		        err_2 += dz2;
		        pixel[0] += x_inc;
		        ll -= res;
		    }
		} else if ((m >= l) && (m >= n)) {
		    err_1 = dx2 - m;
		    err_2 = dz2 - m;
		    ll = m;
		    for (i = 0; i < ll; i++) {
		    	res = PUT_PIXEL(world, block, meta, pixel, incremental);
		        if (err_1 > 0) {
		            pixel[0] += x_inc;
		            err_1 -= dy2;
		        }
		        if (err_2 > 0) {
		            pixel[2] += z_inc;
		            err_2 -= dy2;
		        }
		        err_1 += dx2;
		        err_2 += dz2;
		        pixel[1] += y_inc;
		        ll -= res;
		    }
		} else {
		    err_1 = dy2 - n;
		    err_2 = dx2 - n;
		    ll = n;
		    for (i = 0; i < ll; i++) {
		    	res = PUT_PIXEL(world, block, meta, pixel, incremental);
		        if (err_1 > 0) {
		            pixel[1] += y_inc;
		            err_1 -= dz2;
		        }
		        if (err_2 > 0) {
		            pixel[0] += x_inc;
		            err_2 -= dz2;
		        }
		        err_1 += dy2;
		        err_2 += dx2;
		        pixel[2] += z_inc;
		        ll -= res;
		    }
		}
		PUT_PIXEL(world, block, meta, pixel, incremental);
    }
    
    private static float PUT_PIXEL(World w, Block b, int m, int[] p) {
    	return PUT_PIXEL(w, b, m, p, false);
    }
    
    private static float PUT_PIXEL(World w, Block b, int m, int[] p, boolean inc) {
    	Block s = w.getBlock(p[0], p[1], p[2]);
    	float r = 1000;
    	if(s == Blocks.stone || StoneRegistry.isUnstableBlock(s)) {
    		r = s.getBlockHardness(w, p[0], p[1], p[2]);
    		w.setBlock(p[0], p[1], p[2], b, m, 3);
    	}
    	if(inc) {
    		if(s == b) {
        		r = s.getBlockHardness(w, p[0], p[1], p[2]);
    			w.setBlock(p[0], p[1], p[2], b, m+1, 3);
    		}
    		else if(s == b/*+1*/) {
        		r = s.getBlockHardness(w, p[0], p[1], p[2]);
    			w.setBlock(p[0], p[1], p[2], b, m+2, 3);
    		}
    	}
    	return r / 10.0F;
    }
    
    public static void damageNeighbors(World world, int x, int y, int z) {
    	Block s = world.getBlock(x+1, y, z);
    	int m = world.getBlockMetadata(x+1, y, z);
    	if(StoneRegistry.isUnstableBlock(s)) {
    		m++;
    	}
    	else {
    		StoneRegistry.getReplacement(s, m);
    	}
    	world.setBlock(x+1, y, z, s, m, 3);
    	
    	s = world.getBlock(x-1, y, z);
    	m = world.getBlockMetadata(x-1, y, z);
    	if(StoneRegistry.isUnstableBlock(s)) {
    		m++;
    	}
    	else {
    		StoneRegistry.getReplacement(s, m);
    	}
    	world.setBlock(x-1, y, z, s, m, 3);
    	
    	s = world.getBlock(x, y, z+1);
    	m = world.getBlockMetadata(x, y, z+1);
    	if(StoneRegistry.isUnstableBlock(s)) {
    		m++;
    	}
    	else {
    		StoneRegistry.getReplacement(s, m);
    	}
    	world.setBlock(x, y, z+1, s, m, 3);
    	
    	s = world.getBlock(x, y, z-1);
    	m = world.getBlockMetadata(x, y, z-1);
    	if(StoneRegistry.isUnstableBlock(s)) {
    		m++;
    	}
    	else {
    		StoneRegistry.getReplacement(s, m);
    	}
    	world.setBlock(x, y, z-1, s, m, 3);
    	
    	s = world.getBlock(x, y+1, z);
    	m = world.getBlockMetadata(x, y+1, z);
    	if(StoneRegistry.isUnstableBlock(s)) {
    		m++;
    	}
    	else {
    		StoneRegistry.getReplacement(s, m);
    	}
    	if(m > 3) {
    		m = 3;
    	}
    	world.setBlock(x, y+1, z, s, m, 3);
    	world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "hazards:random.rock", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
    	
    	Random rand = new Random();
    	for(int i = -3; i <= 3; ++i) {
    		for(int k = -3; k <= 3; ++k) {
        		if(world.isAirBlock(x+i, y-2, z+k)) {
        			if(rand.nextInt(24) == 0)
        				world.setBlock(x+i, y-2, z+k, UndergroundBase.rockDust, rand.nextInt(4), 3);
        		}
        		else if(world.isAirBlock(x+i, y-1, z+k)) {
        			if(rand.nextInt(24) == 0)
        				world.setBlock(x+i, y-1, z+k, UndergroundBase.rockDust, rand.nextInt(4), 3);
        		}
        		else if(world.isAirBlock(x+i, y, z+k) && rand.nextInt(24) == 0) {
        			world.setBlock(x+i, y, z+k, UndergroundBase.rockDust, rand.nextInt(4), 3);
        		}
        	}
    	}
    }

	public static void damageNonNormal(World world, int x, int y, int z, Random rand, Block b) {
		if(rand.nextInt((int)(b.getBlockHardness(world, x, y, z)+2)) == 0) {
			if(b == Blocks.stone) {
				world.setBlock(x, y, z, StoneRegistry.getReplacement(b, world.getBlockMetadata(x, y, z)), 1, 3);
			}
			else if(StoneRegistry.isUnstableBlock(b)) {
				int m = world.getBlockMetadata(x, y, z);
				if(m == 3) {
					world.setBlock(x, y, z, Blocks.gravel, 0, 3);
				}
				else {
					world.setBlockMetadataWithNotify(x, y, z, m+1, 3);
				}
			}
			else if(b == Blocks.cobblestone) {
				world.setBlock(x, y, z, Blocks.gravel, 0, 3);
			}
			else {
				world.setBlockToAir(x, y, z);
			}
		}
	}
}
