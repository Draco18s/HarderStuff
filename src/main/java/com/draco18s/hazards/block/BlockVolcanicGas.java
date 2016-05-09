package com.draco18s.hazards.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockVolcanicGas extends BlockGas {

	public BlockVolcanicGas(Fluid fluid) {
		super(fluid, 5);
		setDensity(-10);
		setTemperature(675);
		setBlockName("VolcanicGas");
		setCreativeTab(CreativeTabs.tabMisc);
	}

    @Override
	public boolean causesNausea(int meta) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon("hazards:volcanic_still");
		flowingIcon = register.registerIcon("hazards:volcanic_flow");
    }

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);
		if(world.getBlock(x, y, z) != this) {
			if(world.getBlock(x, y+1, z) == this)
				smokeDecay(world, x, y+1, z, rand);
		}
		smokeDecay(world, x, y, z, rand);
	}
	
	private void smokeDecay(World world, int x, int y, int z, Random rand) {
		int m = world.getBlockMetadata(x, y, z);
		if(rand.nextInt(24) == 0) {
			if(m > 0) {
				world.setBlockMetadataWithNotify(x, y, z, m-1, 3);
			}
			else {
				world.setBlockToAir(x, y, z);
				return;
			}
		}
		else {
			Block b;
			boolean f = false;
			for(int j=-8;j<0&&!f;++j) {
				for(int i=-8;i<=8&&!f;++i) {
					for(int k=-8;k<=8&&!f;++k) {
						b = world.getBlock(x+i, y+j, z+k);
						if(b == Blocks.lava || b == Blocks.flowing_lava || b == Blocks.fire) {
							f = true;
						}
					}
				}	
			}
			if(!f) {
				if(m > 0)
					world.setBlockMetadataWithNotify(x, y, z, m-1, 3);
				else {
					world.setBlockToAir(x, y, z);
					return;
				}
			}
		}
		world.scheduleBlockUpdate(x, y, z, this, tickRate*4);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
    	world.scheduleBlockUpdate(x, y, z, this, tickRate);
	}
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent) {
    	if(ent instanceof EntityLivingBase) {
    		((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.blindness.id, 19));
    	}
    }
}
