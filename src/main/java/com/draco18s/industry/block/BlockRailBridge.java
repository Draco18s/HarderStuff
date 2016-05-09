package com.draco18s.industry.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRailBridge extends BlockRail {
	public int renderID;
    
	public BlockRailBridge() {
		super();
		setCreativeTab(CreativeTabs.tabTransport);
		setBlockName("railbridge");
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
        blockIcon = par1IconRegister.registerIcon("industry:rail_bridge");
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
        }
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (l >= 2 && l <= 5)
        {
            this.setBlockBounds(0.0F, 0, 0.0F, 1.0F, 0.5625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0, 0.0F, 1.0F, 0.125F, 1.0F);
        }
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
