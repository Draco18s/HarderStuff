package com.draco18s.wildlife.block;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.api.internal.ChunkCoordTriplet;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.util.StatsAchievements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRottingWood extends BlockFalling {
	private IIcon topIcon;
	private IIcon compostIcon;
	private HashMap<ChunkCoordTriplet,EntityPlayer> players = new HashMap<ChunkCoordTriplet,EntityPlayer>();

	public BlockRottingWood() {
		super(Material.wood);
		setHardness(1.0f);
		setHarvestLevel("shovel", 0, 4);
		setHarvestLevel("shovel", 0, 5);
		setHarvestLevel("shovel", 0, 6);
		setHarvestLevel("shovel", 0, 7);
		setHarvestLevel("shovel", 0, 4|8);
		setHarvestLevel("shovel", 0, 5|8);
		setHarvestLevel("shovel", 0, 6|8);
		setHarvestLevel("shovel", 0, 7|8);
		setHarvestLevel("axe", 0, 0);
		setHarvestLevel("axe", 0, 1);
		setHarvestLevel("axe", 0, 2);
		setHarvestLevel("axe", 0, 3);
		setHarvestLevel("axe", 0, 0|8);
		setHarvestLevel("axe", 0, 1|8);
		setHarvestLevel("axe", 0, 2|8);
		setHarvestLevel("axe", 0, 3|8);
		setResistance(0.5f);
		setCreativeTab(CreativeTabs.tabBlock);
		setTickRandomly(true);
		setBlockName("rotting_wood");
		fallInstantly = false;

	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if((meta&4) != 0) return compostIcon;
		return (side <= 1)?topIcon:blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		topIcon =   iconRegister.registerIcon("wildlife:log_rotting_top");
		blockIcon = iconRegister.registerIcon("wildlife:log_rotting");
		compostIcon = iconRegister.registerIcon("wildlife:compost");
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		int m = world.getBlockMetadata(x, y, z);
		boolean playerPlaced = (m&8) != 0;
		boolean isCompost = (m&4) != 0;
		int mm = m&3;
		if(mm < 3 && rand.nextInt(4) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, m+1, 3);
		}
		else if(mm == 3) {
			if(!isCompost) {
				if (world.getBlock(x, y-1, z) == Blocks.leaves || world.getBlock(x, y-1, z) == Blocks.leaves2) {
					world.setBlockToAir(x, y, z);
				}
				else {
					world.setBlockMetadataWithNotify(x, y, z, 4|(m&8), 3);
					isCompost = true;
				}
			}
			else if(isCompost && playerPlaced) {
				world.setBlock(x, y, z, Blocks.grass);
				if(world.getBlock(x, y+1, z) == Blocks.air && rand.nextInt(4) == 0) {
					world.setBlock(x, y+1, z, Blocks.tallgrass, rand.nextInt(3), 3);
				}

				ChunkCoordTriplet cct = new ChunkCoordTriplet(world.provider.dimensionId, x, y, z);
				EntityPlayer player = players.get(cct);
				if(player != null) {
					player.addStat(StatsAchievements.growGrass, 1);
				}
			}
			else {
				world.setBlockToAir(x, y, z);
			}
		}

		if(isCompost) {
			super.updateTick(world, x, y, z, rand);
		}
	}

	@Override
	public int damageDropped (int metadata) {
		return (metadata&4)|8;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 8));
		list.add(new ItemStack(item, 1, 12));
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		int m = world.getBlockMetadata(x, y, z);
		if((m&4) != 0) {
			super.onNeighborBlockChange(world, x, y, z, b);
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
		if(ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			ChunkCoordTriplet cct = new ChunkCoordTriplet(world.provider.dimensionId, x, y, z);
			players.put(cct, player);
		}
	}
}
