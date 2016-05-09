package com.draco18s.hazards.block;

import java.util.Random;

import com.draco18s.hazards.StoneRegistry;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
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
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockThinVolatileGas extends BlockGas {

	public BlockThinVolatileGas(Fluid fluid) {
		super(fluid, 1);
		setDensity(-100);
		setTemperature(285);
		setBlockName("ThinVolatileGas");
		setCreativeTab(CreativeTabs.tabMisc);
		setResistance(0);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon("hazards:thin_volatile_still");
		flowingIcon = register.registerIcon("hazards:thin_volatile_flow");
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		int m = world.getBlockMetadata(x, y, z);
    	Block[] wID = {world.getBlock(x + 1, y, z), world.getBlock(x - 1, y, z), world.getBlock(x, y + 1, z), world.getBlock(x, y - 1, z), world.getBlock(x, y, z + 1), world.getBlock(x, y, z - 1)};
    	boolean doExplode = false;
    	boolean isFire = false;
    	for(int i = 0; i < wID.length; i++) {
    		if(wID[i] == Blocks.torch || wID[i] == Blocks.lava) {
    			doExplode = true;
    		}
    		if(wID[i] == Blocks.fire) {
    			doExplode = true;
    	    	isFire = true;
    		}
    	}
    	
    	/*EntityPlayer nearby = world.getClosestPlayer(x, y, z, UndergroundBase.catchFireRange);
		if(nearby == null && UndergroundBase.catchFireRange >= 0) {
			doExplode = false;
	    	isFire = false;
		}*/
    	
    	if(doExplode) {
    		if(!isFire)
    			world.setBlock(x, y, z, Blocks.fire, 0, 3);
    		
    		//perc = -0.0125 m^2  +  0.0844048m + 0.850833
    		//perc = -0.00852295 m^2  +  0.055607m + 0.879471
    		
    		//pow = -0.0334524 m^2  +  0.44131m + 0.538333
    		//pow = -0.034836 m^2  +  0.452168m + 0.526059
    		if (rand.nextDouble() < ((-0.00852295f*m*m  +  0.055607f*m + 0.879471f)/100f)*.1f) {
    			world.newExplosion(null, x, y, z, (-0.034836f*m*m  +  0.452168f*m + 0.526059f)*1.2f, true, true);
    		}

    		else if(isFire && rand.nextDouble() < 0.5) {
    			int meta = world.getBlockMetadata(x, y, z);
    			if(meta > 0)
    				world.setBlockMetadataWithNotify(x, y, z, meta-1, 3);
    			else
    				world.setBlock(x, y, z, Blocks.air, 0, 3);
    		}
	    	else {
	    		world.scheduleBlockUpdate(x, y, z, this, 10);
	    	}
    	}
    	super.updateTick(world, x, y, z, rand);
    }

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
    	world.scheduleBlockUpdate(x, y, z, this, 10);
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
    
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explo) {
        if (!world.isRemote) {
    		world.setBlock(x, y, z, Blocks.fire, 0, 3);
        }
    }
}
