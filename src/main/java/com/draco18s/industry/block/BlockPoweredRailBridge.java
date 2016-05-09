package com.draco18s.industry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPoweredRailBridge extends BlockRailPowered {
	public int renderID;
    
	public BlockPoweredRailBridge() {
		super();
		setCreativeTab(CreativeTabs.tabTransport);
		setBlockName("railbridgepowered");
		setHardness(0.7F);
		setStepSound(soundTypeMetal);
	}
	
	@Override
	public boolean isFlexibleRail(IBlockAccess world, int y, int x, int z)
    {
        return false;
    }
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        //blockIcon = Blocks.rail.getIcon(0, 0);
        //theIcon = Blocks.planks.getBlockTextureFromSide(0);
        blockIcon = par1IconRegister.registerIcon("industry:rail_bridge_powered");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return blockIcon;
		//return inventoryIcon;
        //return par1 == 2 ? this.theIcon : this.blockIcon;
    }
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
		if(World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || this.isRailBlock(world.getBlock(x, y-1, z)) || this.isRailBlock(world.getBlock(x, y+1, z))) {
			return false;
		}
		return true;
    }
	
	private boolean isRailBlock(Block block) {
		if(block instanceof BlockRailBase) return true;
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
    {
        if (!world.isRemote)
        {
            //this.refreshTrackShape(world, x, y, z, false);

    		if(!canPlaceBlockAt(world, x, y, z)) {
    			this.dropBlockAsItem(world, x, y, z, 0, 0);
                world.setBlockToAir(x, y, z);
    		}
    		else {
    			int l = world.getBlockMetadata(x, y, z);
    			int i1 = l & 7;
    			this.func_150048_a(world, x, y, z, l, i1, par5);
    		}
        }
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4)&7;

        if (l >= 2 && l <= 5)
        {
            this.setBlockBounds(0.0F, 0, 0.0F, 1.0F, 0.5625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }
	
	private static final int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	@Override
	public void onMinecartPass(World world, EntityMinecart cart, int x, int y, int z)
    {
		int v = this.getBasicRailMetadata(world, cart, x,y,z)&7;
		
		int[][] aint = matrix[v];
        double d2 = (double)(aint[1][0] - aint[0][0]);
        double d3 = (double)(aint[1][2] - aint[0][2]);
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        double d5 = cart.motionX * d2 + cart.motionZ * d3;
        boolean flag = (world.getBlockMetadata(x, y, z) & 8) != 0;
        if (d5 < 0.0D)
        {
            d2 = -d2;
            d3 = -d3;
        }

        double d6 = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);

        if (d6 > 2.0D)
        {
            d6 = 2.0D;
        }
        cart.motionX = d6 * d2 / d4;
        cart.motionZ = d6 * d3 / d4;
        //System.out.println("flag: " + flag + "," + world.getBlock(x, y, z));
        if(flag) {
        	double d15 = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                cart.motionX += cart.motionX / d15 * d16;
                cart.motionZ += cart.motionZ / d15 * d16;
            }
            else if (v == 1)
            {
                if (cart.worldObj.getBlock(x - 1, y, z).isNormalCube())
                {
                	cart.motionX = 0.02D;
                }
                else if (cart.worldObj.getBlock(x + 1, y, z).isNormalCube())
                {
                	cart.motionX = -0.02D;
                }
            }
            else if (v == 0)
            {
                if (cart.worldObj.getBlock(x, y, z - 1).isNormalCube())
                {
                	cart.motionZ = 0.02D;
                }
                else if (cart.worldObj.getBlock(x, y, z + 1).isNormalCube())
                {
                	cart.motionZ = -0.02D;
                }
            }
        }
        else {
        	cart.motionX = 0;
        	cart.motionY = 0;
        	cart.motionZ = 0;
        }
    	cart.setVelocity(cart.motionX,cart.motionY,cart.motionZ);
    }
	
	@Override
	public int getRenderType()
    {
        return this.renderID;
    }
	
	@Override
	public void setRenderType(int value) {
        this.renderID = value;
    }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		EntityPlayer p = world.getClosestPlayer(x+0.5, y, z+0.5, 1.5);
		if(p == null) {
			p = world.getClosestPlayer(x+0.5, y+1, z+0.5, 1.5);
		}
		if(p == null) {
			p = world.getClosestPlayer(x+0.5, y+2, z+0.5, 1.5);
		}
		if(p != null && p.isSneaking()) {
			if(this.maxY > 0.5) {
				this.maxY = 0.5;
			}
			else {
				this.maxY = 0.0;
			}
			return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
		}
		else {
			return null;
		}
    }
	
	@Override
    public boolean getBlocksMovement(IBlockAccess blockAccess, int x, int y, int z) {
		return true;
    }
}
