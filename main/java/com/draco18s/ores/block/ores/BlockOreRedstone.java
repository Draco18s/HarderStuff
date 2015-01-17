package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.OreDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockOreRedstone extends Block {
	//private IIcon[] icons;
	private boolean avoidGeneration = false;
    private Random rand = new Random();
	
	public BlockOreRedstone() {
		super(Material.rock);
		setBlockName("ore_redstone");
		setHardness(3.0f);
		setHarvestLevel("pickaxe", 2);
		setResistance(5.0f);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockTextureName("redstone_ore");
        setStepSound(soundTypePiston);
        setTickRandomly(true);
	}
	
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z)*9;
    }
	
	public int tickRate(World p_149738_1_) {
        return 30;
    }
	
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.liteUp(world, x, y, z);
        super.onBlockClicked(world, x, y, z, player);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        this.liteUp(world, x, y, z);
        super.onEntityWalking(world, x, y, z, entity);
    }
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.getBlockMetadata(x, y, z)==1) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 3);
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        this.liteUp(world, x, y, z);
        return super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }

    private void liteUp(World world, int x, int y, int z) {
        this.sparkle(world, x, y, z);

        if (world.getBlockMetadata(x, y, z)==0) {
        	world.setBlockMetadataWithNotify(x, y, z, 1, 3);
        }
    }
    
    @Override // World, meta, fortune
    public int getExpDrop(IBlockAccess world, int p_149690_5_, int p_149690_7_) {
        if (this.getItemDropped(p_149690_5_, rand, p_149690_7_) != Item.getItemFromBlock(this)) {
            return 1 + rand.nextInt(5);
        }
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (world.getBlockMetadata(x, y, z)==1) {
            this.sparkle(world, x, y, z);
        }
    }

    private void sparkle(World p_150186_1_, int p_150186_2_, int p_150186_3_, int p_150186_4_) {
        Random random = p_150186_1_.rand;
        double d0 = 0.0625D;

        for (int l = 0; l < 6; ++l) {
            double d1 = (double)((float)p_150186_2_ + random.nextFloat());
            double d2 = (double)((float)p_150186_3_ + random.nextFloat());
            double d3 = (double)((float)p_150186_4_ + random.nextFloat());

            if (l == 0 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ + 1, p_150186_4_).isOpaqueCube()) {
                d2 = (double)(p_150186_3_ + 1) + d0;
            }

            if (l == 1 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ - 1, p_150186_4_).isOpaqueCube()) {
                d2 = (double)(p_150186_3_ + 0) - d0;
            }

            if (l == 2 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ + 1).isOpaqueCube()) {
                d3 = (double)(p_150186_4_ + 1) + d0;
            }

            if (l == 3 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ - 1).isOpaqueCube()) {
                d3 = (double)(p_150186_4_ + 0) - d0;
            }

            if (l == 4 && !p_150186_1_.getBlock(p_150186_2_ + 1, p_150186_3_, p_150186_4_).isOpaqueCube()) {
                d1 = (double)(p_150186_2_ + 1) + d0;
            }

            if (l == 5 && !p_150186_1_.getBlock(p_150186_2_ - 1, p_150186_3_, p_150186_4_).isOpaqueCube()) {
                d1 = (double)(p_150186_2_ + 0) - d0;
            }

            if (d1 < (double)p_150186_2_ || d1 > (double)(p_150186_2_ + 1) || d2 < 0.0D || d2 > (double)(p_150186_3_ + 1) || d3 < (double)p_150186_4_ || d3 > (double)(p_150186_4_ + 1)) {
                p_150186_1_.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
	
	public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }

    public int quantityDropped(Random rand) {
        return 4 + rand.nextInt(2);
    }
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_) {
		return Items.redstone;
    }

	@Override
	public int damageDropped(int meta) {
        return 0;
    }

	/*@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if(entity instanceof EntityPlayer) {
			world.setBlockMetadataWithNotify(x, y, z, 15, 3);
		}
	}*/

	/*@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if (!world.isRemote) {
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, fortune);
            chance = ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, fortune, chance, false, harvesters.get());
            System.out.println("Block (" + meta + ") dropping.");
            if(world.getBlock(x, y, z) != this && meta > 0) {
            	avoidGeneration = true;
    			world.setBlock(x, y, z, this, meta-1, 3);
            }
            
            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem(world, x, y, z, item);
                }
            }
        }
    }*/
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if(player != null && player.capabilities.isCreativeMode) {
        	avoidGeneration = true;
			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
		}
        return removedByPlayer(world, player, x, y, z);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		int s = HashUtils.hash((int)world.getSeed(), x);
		s = HashUtils.hash(s, y);
		s = HashUtils.hash(s, z);
		Random rand = new Random(s);
		if(avoidGeneration) {
			avoidGeneration = false;
			return;
		}
		int m = world.getBlockMetadata(x, y, z);
		if(m < 12 && rand.nextInt(300) == 0) {
			for(int j=1; y+j < 90; j++) {
				if(world.getBlock(x, y+j, z) == Blocks.grass && world.getBlock(x, y+j+1, z) == Blocks.air) {
					OresBase.scatterFlowers(world, x, y+j+1, z, OresBase.blockOreFlowers, 4, 10, 8, 11);
					return;
				}
			}
		}
		//if(OresBase.rand.nextInt(10) == 0) {
		//	OreDataHooks.saveOreData(world, x, y, z, this, 32);
		//}
	}
}
