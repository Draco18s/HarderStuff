package com.draco18s.hazards.block;

import java.util.Random;

import com.draco18s.hazards.UndergroundBase;
import com.draco18s.hazards.block.helper.GasFlowHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockGas extends BlockFluidFinite implements IBreathable {
	public static int renderID;
	protected int airQuality;//higher -> faster loss of oxygen
	protected IIcon stillIcon;
	protected IIcon flowingIcon;

	public BlockGas(Fluid fluid, int quality) {
		super(fluid, UndergroundBase.gas);
		airQuality = quality;
	}

    @Override
	public int getAirQuality(int meta) {
		return airQuality * (meta+1);
	}

    @Override
	public boolean causesNausea(int meta) {
		return true;
	}

    @Override
	public int getRenderType() {
        return renderID;
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return 13290706;
	}
	
	@Override
    public IIcon getIcon(int side, int meta) {
			return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }
	
	public boolean canCollideCheck(int meta, boolean liquids) {
        return false;
    }
   
    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
    	if(world.getBlock(x,  y,  z) == Blocks.lava) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.water) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.flowing_lava) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.flowing_water) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.fire) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.torch) return false;
		return super.canDisplace(world, x, y, z);
    }
   
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
    	/*Block b = world.getBlock(x,  y,  z);
    	if (b instanceof BlockFluidBase) {
    		BlockFluidBase bl = (BlockFluidBase)b;
    		return bl.getFluid().getDensity() < this.getFluid().getDensity();
    	}*/
    	if(world.getBlock(x,  y,  z) == Blocks.lava) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.water) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.flowing_lava) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.flowing_water) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.fire) return false;
    	if(world.getBlock(x,  y,  z) == Blocks.torch) return false;
    	if(world.getBlock(x,  y,  z).getMaterial() == Material.circuits) return false;
		return super.displaceIfPossible(world, x, y, z);
    }
    
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
    	world.scheduleBlockUpdate(x, y, z, this, 10);
	}
    
    @Override
    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
    	
    }
    
    @Override
    public int onBlockPlaced(World par1World, int x, int y, int z, int par5, float par6, float par7, float par8, int par9) {
	    return 8;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
    	Block block = world.getBlock(x, y, z);
        if (block != this) {
            return true;
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        /*
         * Let's see if this fixes the chunkloading issue.
         */
        if(!world.doChunksNearChunkExist(x, y, z, 16) || world.provider.dimensionId == Integer.MIN_VALUE) return;
        
        super.updateTick(world, x, y, z, rand);
        
        int quantaRemaining = world.getBlockMetadata(x, y, z) + 1;
        int north = getQuantaValue(world, x,     y, z - 1);
        int south = getQuantaValue(world, x,     y, z + 1);
        int west  = getQuantaValue(world, x - 1, y, z);
        int east  = getQuantaValue(world, x + 1, y, z);
		int newnorth = north;
		int newsouth = south;
		int neweast = east;
		int newwest = west;
		boolean changed = false;
        if (north >= 0 && south >= 0){
        	if(Math.abs(north - south) >= 1) {
        		changed = true;
        		if(north > south && north > 1) {
        			if(south == 0 && (canDisplace(world, x, y, z + 1) || world.getBlock(x, y, z+1) == this)) {
	        			newnorth--;
	        			newsouth++;
        			}
        		}
        		else if(south > 1){
        			if(north == 0 && (canDisplace(world, x, y, z - 1) || world.getBlock(x, y, z-1) == this)) {
	        			newnorth++;
	        			newsouth--;
        			}
        		}
        		else {
            		changed = false;
        		}
        	}
        }
        if (west >= 0 && east >= 0){
        	if(Math.abs(west - east) >= 1) {
        		changed = true;
        		if(west > east && west > 1) {
        			if(east == 0 && (canDisplace(world, x + 1, y, z) || world.getBlock(x+1, y, z) == this)) {
	        			newwest--;
	        			neweast++;
        			}
        		}
        		else if(east > 1){
        			if(west == 0 && (canDisplace(world, x - 1, y, z) || world.getBlock(x-1, y, z) == this)) {
	        			newwest++;
	        			neweast--;
        			}
        		}
        		else {
            		changed = false;
        		}
        	}
        }
        if(changed) {
        	if(newnorth > 0)
        		world.setBlock(x, y, z-1, this, newnorth-1, 3);
        	if(newsouth > 0)
        		world.setBlock(x, y, z+1, this, newsouth-1, 3);
        	if(newwest > 0)
        		world.setBlock(x-1, y, z, this, newwest-1, 3);
        	if(neweast > 0)
        		world.setBlock(x+1, y, z, this, neweast-1, 3);
        }

    	if(UndergroundBase.doGasSeeping && quantaRemaining > 1 && world.doChunksNearChunkExist(x, y, z, 32)) {
    		Block[] wID = {world.getBlock(x + 1, y, z), world.getBlock(x, y, z + 1), world.getBlock(x - 1, y, z), world.getBlock(x, y, z - 1), world.getBlock(x, y + densityDir, z)};
	    	for(int i = 0; i < wID.length; i++) {
	    		if(GasFlowHelper.isPartialBlock(wID[i])) {
	    			flowPastDoors(world, x, y, z, quantaRemaining-1, rand, i);
	    		}
	    	}
	    	world.scheduleBlockUpdate(x, y, z, this, 100);
    	}
    }
    
    protected void flowPastDoors(World world, int x, int y, int z, int meta, Random rand, int dir) {
    	//int dir = 0;
    	int ox = 0;
    	int oy = 0;
    	int oz = 0;
    	Block s;
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
    			case 4:
    				oy += densityDir;
    				break;
    		}
    		
    		s = world.getBlock(x+ox, y+oy, z+oz);
    		if(GasFlowHelper.isPartialBlock(s)) {
    			int bm = world.getBlockMetadata(x+ox, y+oy, z+oz);
    			if(dir < 4 && GasFlowHelper.isDoor(s)) {
    				if((bm&8) > 0) {
    					bm = world.getBlockMetadata(x+ox, y+oy-1, z+oz);
    				}
    				if((dir & 1) == (bm & 1) && (bm & 4) == 0) {
    					//right direction, door closed
    					//stop looking
            	    	return;
    				}
    				else if((dir & 1) != (bm & 1) && (bm & 4) != 0){
    					//wrong direction, door open
    					//stop looking
            	    	return;
    				}
    			}
    			else if(dir > 3 && s == Blocks.trapdoor && (bm&4) == 0) {
    				//trapdoor above/below and its closed
        	    	return;
    			}
    			else {
	    			//keep going
	    			if(Math.abs(ox) + Math.abs(oy/4) + Math.abs(oz) >= meta) {
	        	    	return;
	    			}
    			}
    		}
    		else if(this.canDisplace(world, x+ox, y+oy, z+oz)) {
    			world.setBlock(x+ox, y+oy, z+oz, this, meta/2, 3);
    			world.setBlock(x, y, z, this, meta - (meta/2), 3);
    	    	return;
    		}
    		else if(s == this) {
    			int mm = world.getBlockMetadata(x+ox, y+oy, z+oz)+meta;
    			if(mm - (mm/2) != meta  && (mm/2) != meta) {
        			world.setBlock(x+ox, y+oy, z+oz, this, mm/2, 3);
        			world.setBlock(x, y, z, this, mm - (mm/2), 3);
        	    	return;
    			}
    		}
    		else if(s.isOpaqueCube()) {
    			return;
    		}
    	} while(Math.abs(ox) + Math.abs(oy/4) + Math.abs(oz) < 8);
	}
}
