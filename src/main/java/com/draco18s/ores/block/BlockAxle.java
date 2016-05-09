package com.draco18s.ores.block;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityWindmill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAxle extends BlockContainer {

	public BlockAxle() {
		super(Material.wood);
		setBlockName("axle");
		setHardness(1.5f);
		setHarvestLevel("axe", 0);
		setResistance(1.0f);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockTextureName("ores:axle");
        setLightOpacity(0);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
        return false;
    }
	
	@Override
	public int getRenderType() {
        return -1;
    }
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if(side == UP) {
			return true;
		}
		else {
			return super.isSideSolid(world, x, y, z, side);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityWindmill();
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		world.scheduleBlockUpdate(x, y, z, this, 1);
		return 0;
    }
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		world.scheduleBlockUpdate(x, y, z, this, 1);
		TileEntityWindmill te = (TileEntityWindmill)world.getTileEntity(x, y, z);
		te.recheckCore();
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		TileEntityWindmill te;
		int dir = checkPlacement(world, x, y, z);
		if(dir > 0) {
			int dx = 0;
			int dz = 0;
			switch(dir) {
				case 1:
					dz = 1;
					break;
				case 2:
					dz = -1;
					break;
				case 3:
					dx = 1;
					break;
				case 4:
					dx = -1;
					break;
			}
			for(int i=0; i<4;i++) {
				te = (TileEntityWindmill)world.getTileEntity(x+i*dx, y, z+i*dz);
				te.setCore(x, y, z);
				te.setMill(x+3*dx, y-1, z+3*dz);
				if(i > 0 && i <= 2) {
					te = (TileEntityWindmill)world.getTileEntity(x, y+i, z);
					te.setCore(x, y, z);
					te.setMill(x+3*dx, y-1, z+3*dz);
					te = (TileEntityWindmill)world.getTileEntity(x, y-i, z);
					te.setCore(x, y, z);
					te.setMill(x+3*dx, y-1, z+3*dz);
					
					te = (TileEntityWindmill)world.getTileEntity(x+i*dz, y, z+i*dx);
					te.setCore(x, y, z);
					te.setMill(x+3*dx, y-1, z+3*dz);
					te = (TileEntityWindmill)world.getTileEntity(x-i*dz, y, z-i*dx);
					te.setCore(x, y, z);
					te.setMill(x+3*dx, y-1, z+3*dz);
				}
			}
		}
		else {
			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
			te = (TileEntityWindmill)world.getTileEntity(x, y, z);
			if(te != null) {
				te.invalidatePowerStatus();
				te.setCore(999, 999, 999);
				te.setMill(999, 999, 999);
			}
			Block b;
		}
	}
	
	public int checkPlacement(World world, int x, int y, int z) {
		if(world.getBlock(x, y+1, z) == OresBase.blockVane && world.getBlock(x, y-1, z) == OresBase.blockVane && world.getBlock(x, y+2, z) == OresBase.blockVane && world.getBlock(x, y-2, z) == OresBase.blockVane) {
			if(world.getBlock(x+1, y, z) == OresBase.blockVane && world.getBlock(x-1, y, z) == OresBase.blockVane && world.getBlock(x+2, y, z) == OresBase.blockVane && world.getBlock(x-2, y, z) == OresBase.blockVane) {
				if(world.getBlock(x, y, z+1) == this && world.getBlock(x, y, z+2) == this && world.getBlock(x, y, z+3) == this) {
					return 1;
				}
				else if(world.getBlock(x, y, z-1) == this && world.getBlock(x, y, z-2) == this && world.getBlock(x, y, z-3) == this) {
					return 2;
				}
			}
			else if(world.getBlock(x, y, z+1) == OresBase.blockVane && world.getBlock(x, y, z-1) == OresBase.blockVane && world.getBlock(x, y, z+2) == OresBase.blockVane && world.getBlock(x, y, z-2) == OresBase.blockVane) {
				if(world.getBlock(x+1, y, z) == this && world.getBlock(x+2, y, z) == this && world.getBlock(x+3, y, z) == this) {
					return 3;
				}
				else if(world.getBlock(x-1, y, z) == this && world.getBlock(x-2, y, z) == this && world.getBlock(x-3, y, z) == this) {
					return 4;
				}
			}
		}
		return -1;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		TileEntityWindmill te  = (TileEntityWindmill)world.getTileEntity(x, y, z);
		te.invalidatePowerStatus();
		super.breakBlock(world, x, y, z, block, par6);
	}
}
