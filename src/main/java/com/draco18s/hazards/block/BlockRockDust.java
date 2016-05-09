package com.draco18s.hazards.block;

import java.util.Random;

import com.draco18s.hazards.HazardsEventHandler;
import com.draco18s.hazards.UndergroundBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRockDust extends Block implements IBreathable {
	//private int airQuality;
	private IIcon thinIcon;

	public BlockRockDust() {
		super(UndergroundBase.gas);
		setBlockName("RockDust");
		setCreativeTab(CreativeTabs.tabMisc);
		setTickRandomly(true);
		setBlockTextureName("hazards:dust");
	}
	
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(this.getTextureName());
		thinIcon = register.registerIcon(this.getTextureName()+"_0");
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if(meta <= 2)
			return this.thinIcon;
        return this.blockIcon;
    }
	
	@Override
	public int tickRate(World p_149738_1_) {
        return 20;
    }

    @Override
	public int getAirQuality(int meta) {
		return (1+meta)*10;
	}

    @Override
	public boolean causesNausea(int meta) {
		return false;
	}
	
	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
        return true;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
        if(world.scheduledUpdatesAreImmediate || !world.doChunksNearChunkExist(x, y, z, 32) || world.provider.dimensionId == Integer.MIN_VALUE) return;
    	super.updateTick(world, x, y, z, rand);
    	HazardsEventHandler.rockDustUpdateCount++;
    	int m = world.getBlockMetadata(x, y, z);
		Block[] wID = {world.getBlock(x + 1, y, z), world.getBlock(x - 1, y, z), world.getBlock(x, y + 1, z), world.getBlock(x, y - 1, z), world.getBlock(x, y, z + 1), world.getBlock(x, y, z - 1)};
        world.scheduleBlockUpdate(x, y, z, this, 20);
		if(rand.nextInt(8) == 0) {
			if(m > 0) {
				world.setBlockMetadataWithNotify(x, y, z, m-1, 2);
			}
			else
				world.setBlockToAir(x, y, z);
		}
		else if(m > 0){
			for(int i = wID.length-1; i>=0; --i) {
				if(wID[i].getMaterial() == Material.air) {
					switch (i) {
						case 0:
							if(rand.nextInt(4) > 0)
								world.setBlock(x+1, y, z, this, m-1, 2);
							break;
						case 1:
							if(rand.nextInt(4) > 0)
								world.setBlock(x-1, y, z, this, m-1, 2);
							break;
						case 2:
							if(rand.nextBoolean())
								world.setBlock(x, y+1, z, this, m-1, 2);
							break;
						case 3:
							world.setBlock(x, y-1, z, this, m-1, 2);
							break;
						case 4:
							if(rand.nextInt(4) > 0)
								world.setBlock(x, y, z+1, this, m-1, 2);
							break;
						case 5:
							if(rand.nextInt(4) > 0)
								world.setBlock(x, y, z-1, this, m-1, 2);
							break;
					}
				}
			}
			if(m > 1)
				world.setBlockMetadataWithNotify(x, y, z, m-2, 2);
			else
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		}
		else if(m == 0 && rand.nextInt(4) == 0){
			world.setBlockToAir(x, y, z);
		}
		else {
			int i = rand.nextInt(wID.length);
			if(wID[i].getMaterial() == Material.air) {
				world.setBlockToAir(x, y, z);
				switch (i) {
					case 0:
						world.setBlock(x+1, y, z, this, m, 2);
						break;
					case 1:
						world.setBlock(x-1, y, z, this, m, 2);
						break;
					case 2:
						if(rand.nextBoolean())
							world.setBlock(x, y+1, z, this, m, 2);
						break;
					case 3:
						world.setBlock(x, y-1, z, this, m, 2);
						break;
					case 4:
						world.setBlock(x, y, z+1, this, m, 2);
						break;
					case 5:
						world.setBlock(x, y, z-1, this, m, 2);
						break;
				}
			}
		}
	}
    
    @Override
    public int onBlockPlaced(World par1World, int x, int y, int z, int par5, float par6, float par7, float par8, int par9) {
    	par1World.scheduleBlockUpdate(x, y, z, this, 20);
	    return 8;
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	//if(HazardsEventHandler.rockDustUpdateCount < 1000) {
    		world.scheduleBlockUpdate(x, y, z, this, 20);
    	//}
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }
    
    /*@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    	if(HazardsEventHandler.rockDustUpdateCount < 1000) {
    		world.scheduleBlockUpdate(x, y, z, this, 1+this.tickRate(world));
    	}
    }*/
    
    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
	public int getRenderType() {
        return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
		return null;
	}
}
