package com.draco18s.ores.block;

import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.entities.TileEntityMillstone;
import com.draco18s.ores.util.StatsAchievements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMillstone extends BlockContainer {
	private IIcon[] icons;

	public BlockMillstone() {
		super(Material.wood);
		setBlockName("millstone");
		setHardness(2.0f);
		setHarvestLevel("axe", 1);
		setResistance(2.0f);
        setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockTextureName("ores:millstone-top");
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if(side <= 1) {
			return icons[0];
		}
		else {
			return icons[10];
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if(side <= 1) {
			return this.icons[world.getBlockMetadata(x, y, z)];
		}
		return icons[10];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[11];
		icons[0] = iconRegister.registerIcon("ores:mill-generic");
		icons[1] = iconRegister.registerIcon("ores:mill-center");
		icons[2] = iconRegister.registerIcon("ores:mill-ul");
		icons[3] = iconRegister.registerIcon("ores:mill-um");
		icons[4] = iconRegister.registerIcon("ores:mill-ur");
		icons[5] = iconRegister.registerIcon("ores:mill-mr");
		icons[6] = iconRegister.registerIcon("ores:mill-lr");
		icons[7] = iconRegister.registerIcon("ores:mill-bm");
		icons[8] = iconRegister.registerIcon("ores:mill-ll");
		icons[9] = iconRegister.registerIcon("ores:mill-ml");
		icons[10] = iconRegister.registerIcon("ores:mill-side");
    }
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		world.scheduleBlockUpdate(x, y, z, this, 1);
		return 0;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(world.getBlockMetadata(x, y, z) %2 == 1) {
			ISidedInventory is = (ISidedInventory)world.getTileEntity(x, y, z);
			ItemStack held = player.getHeldItem();
			if(held != null && is.canInsertItem(0, held, 1)) {
				ItemStack ii = is.getStackInSlot(0);
				if(ii == null) {
					is.setInventorySlotContents(0,held.splitStack(1));
				}
				else if(ii.stackSize < 64) {
					++ii.stackSize;
					--held.stackSize;
				}
	        	switch(world.getBlockMetadata(x, y, z)) {
		        	case 3:
		        		z++;
		        		break;
		        	case 5:
		        		x--;
		        		break;
		        	case 7:
		        		z--;
		        		break;
		        	case 9:
		        		x++;
		        		break;
	        	}
				
	        	TileEntityMillstone st = (TileEntityMillstone)world.getTileEntity(x, y, z);
	        	//System.out.println(x + "," + y + "," + z + ":" + st.hasPower());
				if(st.hasPower()) {
					player.addStat(StatsAchievements.constructMill, 1);
				}
			}
		}
		return false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		Block b;
		boolean flag = false;
		flag = checkPlacement(world, x, y, z);
		if(!flag) {
			for(int i = -1; i <= 1 && !flag; i++) {
				for(int j = -1; j <= 1 && !flag; j++) {
					b = world.getBlock(x+i, y, z+j);
					if(b == this) {
						flag = ((BlockMillstone)b).checkPlacement(world, x+i, y, z+j);
					}
				}
			}
		}
	}
	
	public boolean checkPlacement(World world, int x, int y, int z) {
		if( world.getBlock(x+1, y, z-1) == this &&
			world.getBlock(x+1, y, z  ) == this &&
			world.getBlock(x+1, y, z+1) == this &&
			world.getBlock(x-1, y, z-1) == this &&
			world.getBlock(x-1, y, z  ) == this &&
			world.getBlock(x-1, y, z+1) == this &&
			world.getBlock(x,   y, z-1) == this &&
			world.getBlock(x,   y, z+1) == this) {
				world.setBlockMetadataWithNotify(x+1, y, z+1, 6, 3);
				world.setBlockMetadataWithNotify(x+1, y, z,   5, 3);
				world.setBlockMetadataWithNotify(x+1, y, z-1, 4, 3);
				world.setBlockMetadataWithNotify(x-1, y, z+1, 8, 3);
				world.setBlockMetadataWithNotify(x-1, y, z,   9, 3);
				world.setBlockMetadataWithNotify(x-1, y, z-1, 2, 3);
				world.setBlockMetadataWithNotify(x,   y, z+1, 7, 3);
				world.setBlockMetadataWithNotify(x,   y, z  , 1, 3);
				world.setBlockMetadataWithNotify(x,   y, z-1, 3, 3);
				return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityMillstone();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		dropItems(world, x, y, z);
		resetMetadata(world, x, y, z);
		super.breakBlock(world, x, y, z, block, par6);
	}
	
	public void resetMetadata(World world, int x, int y, int z) {
		for(int i = -2; i <= 2; i++) {
			for(int j = -2; j <= 2; j++) {
				if(world.getBlock(x+i, y, z+j) == this) {
					world.setBlockMetadataWithNotify(x+i, y, z+j, 0, 3);
				}
			}
		}
	}

	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

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
	}
}
