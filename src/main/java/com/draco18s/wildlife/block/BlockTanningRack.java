package com.draco18s.wildlife.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.entity.TileEntityTanner;
import com.draco18s.wildlife.util.StatsAchievements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTanningRack extends BlockContainer {

	public BlockTanningRack() {
		super(Material.wood);
		setBlockName("tanning_rack");
		setHardness(2.0F);
		setResistance(0.0F);
		setStepSound(soundTypeWood);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockTextureName("planks_oak");
		setBlockBounds(0, 0, 0.1f, 1, 1, 0.9f);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int m = world.getBlockMetadata(x, y, z);
		if(m == 0) {
			setBlockBounds(0, 0, 0.2f, 1, 1, 0.8f);
		}
		else {
			setBlockBounds(0.2f, 0, 0, 0.8f, 1, 1);
		}
	}

	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        this.setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTanner();
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

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz) {
		int m = world.getBlockMetadata(x, y, z) * 2;
		if(side == (2+m) || side == (3+m)) {
			IInventory is = (IInventory)world.getTileEntity(x, y, z);
			ItemStack held = player.getHeldItem();
			if(held != null && held.getItem() == WildlifeBase.itemRawLeather) {
				if(is.getStackInSlot(side-2-m) == null) {
					is.setInventorySlotContents(side-2-m,held.splitStack(1));
					//--held.stackSize;
				}
			}
			else if(held == null) {
				if(is.getStackInSlot(side-2-m) != null) {
					player.inventory.addItemStackToInventory(is.getStackInSlot(side-2-m));
					is.setInventorySlotContents(side-2-m,null);
					player.addStat(AchievementList.killCow, 1);
					player.addStat(StatsAchievements.getLeather, 1);
				}
			}
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		if(!world.doesBlockHaveSolidTopSurface(world, x, y-1, z)) {
			dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.doesBlockHaveSolidTopSurface(world, x, y-1, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
        int l = MathHelper.floor_double((double)(ent.rotationYaw * 4.0F / 360.0F) + 0.5D) & 1;
        world.setBlockMetadataWithNotify(x, y, z, l, 3);
    }
}
