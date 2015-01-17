package com.draco18s.ores.block;

import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.entities.TileEntityWindvane;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWindvane extends BlockContainer {
	IIcon[] icons;
	
	public BlockWindvane() {
		super(Material.cloth);
		setBlockName("wind_vane");
		setHardness(1.0f);
		//setHarvestLevel("axe", 0);
		setResistance(0.1f);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockTextureName("ores:windvane");
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
	
	/*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return icons[0];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[1];
		icons[0] = iconRegister.registerIcon("ores:windvane");
    }*/

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityWindvane();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		TileEntityWindmill te  = (TileEntityWindmill)world.getTileEntity(x, y, z);
		te.invalidatePowerStatus();
		super.breakBlock(world, x, y, z, block, par6);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		world.scheduleBlockUpdate(x, y, z, this, 1);
		return 0;
    }

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		Block b;
		for(int i=-2;i<=2;i+=4) {
			b = world.getBlock(x+i, y, z);
			if(b == OresBase.blockaxel) {
				((BlockAxel)b).updateTick(world, x+i, y, z, rand);
			}
			b = world.getBlock(x, y+i, z);
			if(b == OresBase.blockaxel) {
				((BlockAxel)b).updateTick(world, x, y+i, z, rand);
			}
			b = world.getBlock(x, y, z+i);
			if(b == OresBase.blockaxel) {
				((BlockAxel)b).updateTick(world, x, y, z+i, rand);
			}
		}
	}
}
