package com.draco18s.hazards.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.Twister;
import com.draco18s.hazards.StoneRegistry;
import com.draco18s.hazards.UndergroundBase;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUnstableStone extends Block{
	private IIcon[] icons;
	private IIcon baseTexture;
	private boolean iconsSet = false;
	private int renderPass;
	public static int renderID = 0;
	private boolean dropsSelf = false;

	public BlockUnstableStone(String origName, String textureName) {
		super(Material.rock);
		setTickRandomly(true);
		setHardness(2.0F);
		setHarvestLevel("pickaxe", 0);
		setCreativeTab(CreativeTabs.tabBlock);
		setBlockName(origName);
		setStepSound(soundTypeStone);
		setBlockTextureName(textureName);
	}
	
	public void setDropsSelf() {
		dropsSelf = true;
	}
	
	public int getRenderType() {
        return renderID;
    }
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		TextureAtlasSprite sprite;
		this.icons = new IIcon[12];
		
		baseTexture = iconRegister.registerIcon(this.textureName);

		for(int o = 1; o < 12; o++) {
			icons[o] = iconRegister.registerIcon("hazards:stone_overlay_"+o);
		}
	}
	
	public boolean canRenderInPass(int pass) {
		renderPass = pass;
        return pass <= 1;
    }
	
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }
	
	@Override
	public int tickRate(World p_149738_1_) {
        return 50;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(Item unknown, CreativeTabs tab, List subItems)
    {
		for (int ix = 0; ix < 4; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random par5Random) {
		int meta = world.getBlockMetadata(x, y, z);
		switch(meta) {
			case 0:
				UnstableStoneHelper.updateUnstable(world, x, y, z, par5Random, this, meta);
				break;
			case 1:
				UnstableStoneHelper.updateFractured(world, x, y, z, par5Random, this, meta);
				break;
			case 2:
				UnstableStoneHelper.updateBroken(world, x, y, z, par5Random, this, meta);
				break;
			case 3:
				UnstableStoneHelper.updateCobble(world, x, y, z, par5Random, this, meta);
				break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		if(renderPass == 0 || meta == 0)
			return baseTexture;
		if(meta == 1) {
			if(side > 1) {
				Twister.initialize_generator(2378 + y);
				int ch = 0;
				for(int i=Math.abs(x + z)%64; i>=0;i--) {//512
					ch = Twister.extract_number();
				}
				ch = (ch + side) % 18;
				switch(Math.abs(ch)) {
					case 0:
					case 12:
						return this.icons[2];
					case 1:
					case 8:
						return this.icons[3];
					case 2:
					case 14:
						return this.icons[4];
					case 3:
					case 7:
						return this.icons[5];
					case 9:
					case 11:
						return this.icons[6];
					case 10:
					case 16:
						return this.icons[7];
					case 5:
					case 15:
						return this.icons[8];
					case 13:
					case 17:
						return this.icons[9];
					case 4:
					case 6:
						return this.icons[1];
				}
				
			}
			else {
				return baseTexture;
			}
		}
		return getIcon(side, meta);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int metadata) {
		if(renderPass == 0 || metadata == 0)
			return baseTexture;
		if(metadata != 1) {
			metadata += 8;
		}
		
		return icons[metadata];
    }
	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		if(dropsSelf)
			return Item.getItemFromBlock(this);
    	return Item.getItemFromBlock(Blocks.cobblestone);
    }
	
	public int damageDropped(int p_149692_1_) {
		if(dropsSelf)
			return 3;
        return 0;
    }

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		this.updateTick(world, x, y, z, world.rand);
		//System.out.println(world.getBlock(x, y, z) + ":" + world.getBlockMetadata(x, y, z));
		//world.scheduleBlockUpdate(x, y, z, this, 6 + new Random().nextInt(6));
		if(world.getBlockMetadata(x, y, z) == 3) {
			Random rand = new Random();
			for(int i = -1; i <= 1; ++i) {
	    		for(int k = -1; k <= 1; ++k) {
	        		if(world.isAirBlock(x+i, y, z+k)) {
	        			if(rand.nextInt(3) == 0)
	        				world.setBlock(x+i, y, z+k, UndergroundBase.rockDust, 4, 3);
	        		}
	    		}
			}
			//world.setBlock(x+i, y-2, z+k, UndergroundBase.rockDust, rand.nextInt(6), 3);
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tx, int ty, int tz) {
		this.updateTick((World)world, x, y, z, ((World)world).rand);
		//world.scheduleBlockUpdate(x, y, z, this, 6 + new Random().nextInt(6));
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
		world.scheduleBlockUpdate(x, y, z, this, 6 + new Random().nextInt(6));
	}
	
	public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion expl) {
		int meta = world.getBlockMetadata(i, j, k);
		if(meta == 1) {
	    	if(world.rand.nextBoolean()) {
		    	int x = (int) Math.round(expl.explosionX-.5);
		    	int y = (int) Math.round(expl.explosionY-.5);
		    	int z = (int) Math.round(expl.explosionZ-.5);
		    	int a = world.rand.nextInt(13)-6;
		    	int b = world.rand.nextInt(9)-1;
		    	int c = world.rand.nextInt(13)-6;
		    	Vec3 up = Vec3.createVectorHelper(0, 1, 0);
		    	Vec3 v1 = Vec3.createVectorHelper(a, b, c);
		    	Vec3 v2 = Vec3.createVectorHelper(i-x, j-y, k-z);
		    	
		    	double theta = this.AngleBetween(up, v2);
		    	Vec3 q = up.crossProduct(v2).normalize();
		    	Vec3 f = RotateAboutArbitraryAxis(v1, q, theta);
		    	
		    	UnstableStoneHelper.drawLine3D(world, this, meta+1, i, j, k, (int)Math.round(f.xCoord+i), (int)Math.round(f.yCoord+j), (int)Math.round(f.zCoord+k), true, true);
	    	}
		}
    }
    
    private double AngleBetween(Vec3 v1, Vec3 v2) {
    	double d = v1.dotProduct(v2);
    	double m1 = v1.lengthVector();
    	double m2 = v2.lengthVector();
    	return Math.acos(d/(m1*m2));
	}
    
    private Vec3 RotateAboutArbitraryAxis(Vec3 v1, Vec3 axis, double angle) {
    	double sin = Math.sin(angle), cos = Math.cos(angle);
    	double vx = cos*v1.xCoord+(-sin)*v1.crossProduct(axis).xCoord+(1-cos)*v1.dotProduct(axis)*axis.xCoord;
    	double vy = cos*v1.yCoord+(-sin)*v1.crossProduct(axis).yCoord+(1-cos)*v1.dotProduct(axis)*axis.yCoord;
    	double vz = cos*v1.zCoord+(-sin)*v1.crossProduct(axis).zCoord+(1-cos)*v1.dotProduct(axis)*axis.zCoord;
    	return Vec3.createVectorHelper(vx, vy, vz);
    }
    
    private double[] convertSphericalToCartesian(double lat, double lon, double dist) {
        //double lat = latitude;//Math.toRadians(latitude);
        //double lon = longitude;//Math.toRadians(longitude);
        double x = (dist * Math.cos(lat)*Math.cos(lon));
        double y = (dist * Math.cos(lat)*Math.sin(lon));
        double z = (dist * Math.sin(lat));
        double point[] = new double[3];
        point[0] = x;
        point[1] = y;
        point[2] = z;
        return point;
    }
    
    private double[] convertCartesianToSpherical(int x, int y, int z)
	{
	    double r = Math.sqrt(x * x + y * y + z * z); 
	    double lat = (Math.asin(z/r));
	    double lon = (Math.atan2(y, x));
	    double vec[] = new double[3];
	    vec[0] = lat;
	    vec[1] = lon;
	    vec[2] = r;
	    return vec;
	}
    
    private int[] rotateByAngle(int px, int py, int ox, int oy, double theta) {
    	int[] p = new int[2];
    	p[0] = (int)(Math.cos(theta) * (px-ox) - Math.sin(theta) * (py-oy) + ox);
    	p[1] = (int)(Math.sin(theta) * (px-ox) + Math.cos(theta) * (py-oy) + oy);
    	return p;
    }
}
