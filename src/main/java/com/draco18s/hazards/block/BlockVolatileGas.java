package com.draco18s.hazards.block;

import java.util.Random;

import com.draco18s.hazards.UndergroundBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockVolatileGas extends BlockGas {

	public BlockVolatileGas(Fluid fluid) {
		super(fluid, 8);
		setDensity(1000);
		setTemperature(295);
		setBlockName("VolatileGas");
		setCreativeTab(CreativeTabs.tabMisc);
		setResistance(0);
	}
	
    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
    	if(world.getBlock(x,  y,  z) == Blocks.fire) return true;
		return super.canDisplace(world, x, y, z);
    }
   
    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
    	if(world.getBlock(x,  y,  z) == Blocks.fire) return true;
		return super.displaceIfPossible(world, x, y, z);
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon("hazards:volatile_still");
		flowingIcon = register.registerIcon("hazards:volatile_flow");
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
    	super.updateTick(world, x, y, z, rand);
    	int m = world.getBlockMetadata(x, y, z);
    	Block[] wID = {world.getBlock(x + 1, y, z), world.getBlock(x - 1, y, z), world.getBlock(x, y + 1, z), world.getBlock(x, y - 1, z), world.getBlock(x, y, z + 1), world.getBlock(x, y, z - 1)};
    	boolean doExplode = false;
    	boolean isFire = false;
    	
    	//EntityPlayer nearby = world.getClosestPlayer(x, y, z, UndergroundBase.catchFireRange);
		
    	for(int i = 0; i < wID.length; i++) {
    		if(/*(nearby != null || UndergroundBase.catchFireRange < 0) && */(wID[i] == Blocks.torch || wID[i] == Blocks.lava)) {
    			doExplode = true;
    		}
    		if(/*(nearby != null || UndergroundBase.catchFireRange < 0) && */wID[i] == Blocks.fire) {
    			doExplode = true;
    	    	isFire = true;
    	    	if(rand.nextDouble() < 0.0625) {
    	    		int meta = world.getBlockMetadata(x, y, z);
        			if(meta > 0)
        				world.setBlockMetadataWithNotify(x, y, z, meta-1, 3);
        			else
        				world.setBlock(x, y, z, Blocks.air, 0, 3);
        		}
    	    	else if(i == 2 && rand.nextDouble() < 0.125) {//above
    	    		world.setBlockMetadataWithNotify(x, y, z, m-1, 3);
        			world.setBlock(x, y+1, z, UndergroundBase.blockThinVolatileGas, 8, 3);
    	    	}
    	    	else {
    	    		world.scheduleBlockUpdate(x, y, z, this, 10);
    	    	}
    		}
    	}
    	if(doExplode) {
    		int v = m + 7;
    		if(!isFire)
    			world.setBlock(x, y, z, Blocks.fire, 0, 3);

        	//perc = -0.00761905 m^2  +  -0.0666667m + 0.841667
    		//perc = -0.00852295 (m+7)^2  +  0.055607(m+7) + 0.879471

        	//pow = -0.036131 x^2  +  -0.028631x + 1.98625
    		//pow = -0.034836 (m+7)^2  +  0.452168(m+7) + 0.526059
    		if (rand.nextDouble() < ((-0.00852295f*v*v  +  0.055607f*v + 0.879471f)/100f)*.1f) {
    			world.newExplosion(null, x, y, z, (-0.034836f*v*v  +  0.452168f*v + 0.526059f)*1.2f, true, true);
    		}
    	}
    	
    	wID[0] = world.getBlock(x, y + 1, z);
    	wID[1] = world.getBlock(x+1, y + 1, z);
    	wID[2] = world.getBlock(x-1, y + 1, z);
    	wID[3] = world.getBlock(x, y + 1, z+1);
    	wID[4] = world.getBlock(x, y + 1, z-1);
    	if((wID[0] == Blocks.air || (wID[0] == UndergroundBase.blockThinVolatileGas && world.getBlockMetadata(x, y + 1, z) < 2)) && wID[1] != this && wID[2] != this && wID[3] != this && wID[4] != this) {
    		if(m > 0) {
    			world.setBlockMetadataWithNotify(x, y, z, m-1, 3);
    			world.setBlock(x, y+1, z, UndergroundBase.blockThinVolatileGas, 8, 3);
    		}
    		else {
    			world.setBlock(x, y, z, UndergroundBase.blockThinVolatileGas, 8, 3);
    		}
    	}
    	else if(m == 0) {
        	wID[1] = world.getBlock(x+1, y, z);
        	wID[2] = world.getBlock(x-1, y, z);
        	wID[3] = world.getBlock(x, y, z+1);
        	wID[4] = world.getBlock(x, y, z-1);
    		if(wID[1] == Blocks.air || wID[2] == Blocks.air || wID[3] == Blocks.air || wID[4] == Blocks.air) {
    			world.setBlock(x, y, z, UndergroundBase.blockThinVolatileGas, 8, 3);
    		}
    	}
    	
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent) {
    	if(ent instanceof EntityBlaze) {
    		int m = world.getBlockMetadata(x, y, z);
    		world.setBlock(x, y, z, Blocks.fire, 0, 3);
    		if (world.rand.nextDouble() < 0.005 * m) {
    			world.newExplosion(null, x, y, z, 0.5F*m, true, true);
    		}
    	}
    }
    
    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion exp) {
    	int m = world.getBlockMetadata(x, y, z);
    	int v = m + 7;
    	super.onBlockExploded(world, x, y, z, exp);
    	if (world.rand.nextDouble() < ((-0.00852295f*v*v  +  0.055607f*v + 0.879471f)/100f)*.1f) {
			world.newExplosion(null, x, y, z, 0.5F*m, true, true);
		}
    	else if(m >= 2) {
    		world.setBlock(x, y, z, this, m-2, 3);
    	}
    }
}
