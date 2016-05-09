package com.draco18s.ores.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;

import java.util.Iterator;
import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntitySluice;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSluice extends BlockContainer {

	public BlockSluice() {
		super(Material.carpet);
		setBlockName("sluice");
		setHardness(2.0f);
		setHarvestLevel("axe", 1);
		setResistance(2.0f);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockTextureName("minecraft:planks_birch");
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
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntitySluice();
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        setMeta(world, x, y, z);
    }

    private void setMeta(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j()) {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j()) {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j()) {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j()) {
                b0 = 4;
            }
            //System.out.println("Meta is: " + b0);
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            tileEntity.blockMetadata = b0;
            world.setBlockMetadataWithNotify(x, y, z, b0, 3);
        }
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, par6);
	}

	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		TileEntitySluice ts = (TileEntitySluice) tileEntity;
		ts.setNoWater();
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			//System.out.println(item + " : " + ((item!=null)?item.stackSize:"0"));
			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
		int q = ts.getGravel() / 5;
		while(q > 0) {
			int s = Math.min(q, 64);
			q -= 64;
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world,
					x + rx, y + ry, z + rz,
					new ItemStack(Blocks.gravel, s));

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntityInWorld(entityItem);
		}
		q = ts.getSand() / 5;
		while(q > 0) {
			int s = Math.min(q, 64);
			q -= 64;
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world,
					x + rx, y + ry, z + rz,
					new ItemStack(Blocks.sand, s));

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntityInWorld(entityItem);
		}
		q = ts.getDirt() / 5;
		while(q > 0) {
			int s = Math.min(q, 64);
			q -= 64;
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world,
					x + rx, y + ry, z + rz,
					new ItemStack(Blocks.dirt, s));

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntityInWorld(entityItem);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(OresBase.instance, 1, world, x, y, z);
			return true;
		}
	}
	
	public boolean hasComparatorInputOverride() {
        return true;
    }
	
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
    	TileEntitySluice ts = (TileEntitySluice)world.getTileEntity(x, y, z);
    	if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
    		//output buffer
    		//return ts.getComparatorValue();
    		if(ts.canFilter()) {
    			return Container.calcRedstoneFromInventory(ts);
    		}
    		else {
    			return 15;
    		}
    	}
    	else {
    		//input buffer
    		/*0: empty
			1-4: Blue (16 blocks per)
			5-8: green (also 16 blocks per)
			9-12: yellow (32 blocks per)
			13-14: orange
			15: red.*/
    		int d = ts.getGravel() + ts.getSand() + ts.getDirt();
    		if(d <= 640) {
    			return d/80;//blue, green
    		}
    		else if(d <= 1280) {
    			d -= 640;
    			return (d / 160)+8;//yellow
    		}
    		else if(d <= 5120) {
    			d -= 640;
    			return (d / 1920)+12;//orange
    		}
    		else {
    			return 15;//red
    		}
    	}
        //return 0;//Container.calcRedstoneFromInventory(this.func_149951_m(world, x, y, z));
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		super.onNeighborBlockChange(world, x, y, z, b);
		if(!canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
		}
	}
    
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return world.getBlock(x, y-1, z).isSideSolid(world, x, y, z, ForgeDirection.UP);
    }
}
