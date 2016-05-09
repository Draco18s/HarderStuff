package com.draco18s.wildlife.block;

import java.util.ArrayList;
import java.util.Random;

import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.WildlifeEventHandler;
import com.draco18s.wildlife.entity.TileEntityGrassSnow;
import com.draco18s.wildlife.util.TreeDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public class BlockSnowyTallGrass extends BlockContainer {
	public static int renderID;

	public BlockSnowyTallGrass() {
		super(Material.snow);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		setTickRandomly(true);
		//setCreativeTab(CreativeTabs.tabDecorations);
		func_150154_b(0);
		setHardness(0.1F);
		setStepSound(soundTypeSnow);
		setBlockName("snowy_grass");
		setLightOpacity(0);
		setBlockTextureName("snow");
		//setHarvestLevel("shovel", 0);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		int l = p_149668_1_.getBlockMetadata(p_149668_2_, p_149668_3_, p_149668_4_) & 7;
		float f = 0.125F;
		return AxisAlignedBB.getBoundingBox((double)p_149668_2_ + minX, (double)p_149668_3_ + minY, (double)p_149668_4_ + minZ, (double)p_149668_2_ + maxX, (double)((float)p_149668_3_ + (float)l * f), (double)p_149668_4_ + maxZ);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
        return renderID;
    }

	@Override
	public void setBlockBoundsForItemRender() {
		func_150154_b(0);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		func_150154_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
	}

	protected void func_150154_b(int p_150154_1_) {
		int j = p_150154_1_ & 7;
		float f = (float)(2 * (1 + j)) / 16.0F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		Block block = p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_);
		return block != Blocks.ice && block != Blocks.packed_ice ? (block.isLeaves(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) ? true : (block == this && (p_149742_1_.getBlockMetadata(p_149742_2_, p_149742_3_ - 1, p_149742_4_) & 7) == 7 ? true : block.isOpaqueCube() && block.getMaterial().blocksMovement())) : false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		func_150155_m(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	private boolean func_150155_m(World p_150155_1_, int p_150155_2_, int p_150155_3_, int p_150155_4_) {
		if (!canPlaceBlockAt(p_150155_1_, p_150155_2_, p_150155_3_, p_150155_4_)) {
			p_150155_1_.setBlockToAir(p_150155_2_, p_150155_3_, p_150155_4_);
			return false;
		}
		else {
			return true;
		}
	}
	
	@Override
	//public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		willHarvest = ((player.getCurrentEquippedItem() == null)?false:(player.getCurrentEquippedItem().getItem() instanceof ItemSpade));
		dropItems(world, x, y, z, willHarvest, willHarvest?EnchantmentHelper.getFortuneModifier(player):0);
		//super.breakBlock(world, x, y, z, block, par6);
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}

	private void dropItems(World world, int x, int y, int z, boolean willHarvest, int fortune){
		TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x, y, z);
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret = (te == null)?ret:te.oBlock.getDrops(world, x, y, z, te.oMeta, fortune);
		if(willHarvest) {
			//Blocks.snow_layer.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), fortune);
			ret.addAll(Blocks.snow_layer.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), fortune));
			//ret.add(new ItemStack(Items.snowball, 1, 0));
		}
		Random rand = new Random();
		for (ItemStack item : ret) {
			
			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, item);

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
			}
		}
	}
	
	public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

	/*@Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (world.rand.nextInt(8) != 0) return ret;
        ItemStack seed = ForgeHooks.getGrassSeed(world);
        if (seed != null) {
        	BiomeWeatherData bioDat = WildlifeEventHandler.getBiomeData(world.getBiomeGenForCoords(x, z));
        	if(bioDat.temp <= 0.3 && seed.getItem() == Items.wheat_seeds) {
        		ret.add(new ItemStack(WildlifeBase.winterWheatSeeds, seed.stackSize));
        	}
        	else {
        		ret.add(seed);
        	}
        }
		TileEntityGrassSnow te = (TileEntityGrassSnow)world.getTileEntity(x, y, z);
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret = (te == null)?ret:te.oBlock.getDrops(world, x, y, z, te.oMeta, fortune);
		System.out.println(te + ":" + ret);
        return ret;
    }*/

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		TileEntity te2 = world.getTileEntity(x, y, z);
		if(te2 instanceof TileEntityGrassSnow) {
			TileEntityGrassSnow te = (TileEntityGrassSnow)te2;
			int blockMetadata = world.getBlockMetadata(x, y, z);
			SpecialBlockEvent.BlockUpdateEvent ev = new SpecialBlockEvent.BlockUpdateEvent(world, x, y, z, blockMetadata, this, world.getBiomeGenForCoords(x, z));
			MinecraftForge.EVENT_BUS.post(ev);
			if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
				if(te != null) {
					if(te.oBlock == null) {
						te.oBlock = Blocks.tallgrass;
						if(blockMetadata >= 8) {
							te.oMeta = 2;
						}
						else {
							te.oMeta = 1;
						}
					}
					world.setBlock(x, y, z, te.oBlock, te.oMeta, 3);
					if(te.oBlock == Blocks.sapling) {
						TreeDataHooks.addTree(world, x, y, z, -1);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return p_149646_5_ == 1 ? true : super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		/*int meta = world.getBlockMetadata(x, y, z);
		return meta >= 7 ? false : blockMaterial.isReplaceable();*/
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityGrassSnow();
	}
	
	@Override
	public Item getItem(World world, int x, int y, int z) {
		return Blocks.snow_layer.getItem(world, x, y, z);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		ItemStack s = player.getCurrentEquippedItem();
		if(s != null && s.getItem() == Item.getItemFromBlock(Blocks.snow_layer)) {
			int m = world.getBlockMetadata(x, y, z);
			if(m < 7) {
				if(!player.capabilities.isCreativeMode) {
					s.stackSize--;
				}
				world.setBlockMetadataWithNotify(x, y, z, m+1, 3);
			}
		}
		return false;
	}
}
