package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.OreDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
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

public class BlockOreIron extends Block {
	private IIcon[] icons;
	private boolean avoidGeneration = false;
	
	public BlockOreIron() {
		super(Material.rock);
		setBlockName("ore_iron");
		setHardness(6.0f);
		setHarvestLevel("pickaxe", 1);
		setResistance(5.0f);
        setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockTextureName("iron_ore");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[15];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int p) {
        return this.icons[world.getBlockMetadata(x, y, z)];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[16];
		for(int i=0; i < 16; i++) {
			icons[i] = iconRegister.registerIcon("ores:iron_ore_"+i);
		}
    }
	
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+meta/6+1);
		
		//return 1 + fortune + random.nextInt(fortune+1);
    }
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return OresBase.oreChunks;
    }

	@Override
	public int damageDropped(int meta) {
        return 0;
    }

	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if(entity instanceof EntityPlayer) {
			world.setBlockMetadataWithNotify(x, y, z, 15, 3);
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if (!world.isRemote) {
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, fortune);
            chance = ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, fortune, chance, false, harvesters.get());
            //System.out.println("Block (" + meta + ") dropping.");
            if(world.getBlock(x, y, z) != this && meta > 0) {
            	//avoidGeneration = true;
    			world.setBlock(x, y, z, this, meta-1, 3);
            }
            /*else {
            	//mining laser hack
            	for(int i = meta-1; i >= 0; i--) {
            		items.addAll(getDrops(world, x, y, z, i, fortune));
            	}
            }*/
            
            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem(world, x, y, z, item);
                }
            }
        }
    }
	
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
		if(m < 4 && rand.nextInt(300) == 0) {
			for(int j=1; y+j < 90; j++) {
				if(world.getBlock(x, y+j, z) == Blocks.grass && world.getBlock(x, y+j+1, z) == Blocks.air) {
					OresBase.scatterFlowers(world, x, y+j+1, z, OresBase.blockOreFlowers, 0, 25, 8, 11);
					return;
				}
			}
		}
		//if(OresBase.rand.nextInt(20) == 0) {
		//	OreDataHooks.saveOreData(world, x, y, z, this, (m+1)*20);
		//}
	}
}
