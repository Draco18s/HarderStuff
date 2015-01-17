package com.draco18s.wildlife.block;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.List;
import java.util.Random;

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

	public BlockRottingWood() {
		super(Material.wood);
		setHardness(1.0f);
		setHarvestLevel("axe", 0);
		setResistance(0.5f);
        setCreativeTab(CreativeTabs.tabBlock);
        setTickRandomly(true);
        setBlockName("rotting_wood");
        fallInstantly = false;
        
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if(meta > 5) return compostIcon;
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
		if(rand.nextInt(3) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, m+1, 3);
		}
		if (m == 10 || world.getBlock(x, y-1, z) == Blocks.leaves || world.getBlock(x, y-1, z) == Blocks.leaves2) {
			world.setBlockToAir(x, y, z);
		}
		else if(m == 15) {
			world.setBlock(x, y, z, Blocks.grass);
			if(world.getBlock(x, y+1, z) == Blocks.air && rand.nextInt(8) == 0) {
				world.setBlock(x, y+1, z, Blocks.tallgrass, rand.nextInt(3), 3);
			}
		}
		
		if(m >= 5) {
			super.updateTick(world, x, y, z, rand);
		}
    }
	
	 @Override
		public int damageDropped (int metadata) {
		 if(metadata > 5) return 11;
		 return 0;
	 }
	 
	 @SideOnly(Side.CLIENT)
	 @Override
	 public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		 list.add(new ItemStack(item, 1, 0));
		 list.add(new ItemStack(item, 1, 11));
	 }
	
	 /*public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		 super.onNeighborBlockChange(world, x, y, z, b);
		 if(b == this) {
			 int m = world.getBlockMetadata(x, y, z);
			 if(m < 15) {
				 world.setBlockMetadataWithNotify(x, y, z, m+1, 6);
			 }
			 else {
				 world.setBlockToAir(x, y, z);
			 }
		 }
	 }
	 
	 public int tickRate() {
		return this instanceof BlockFalling?100:10;
	 }*/
}
