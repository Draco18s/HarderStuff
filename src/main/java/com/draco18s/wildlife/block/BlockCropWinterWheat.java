package com.draco18s.wildlife.block;

import java.util.ArrayList;
import java.util.Random;

import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.wildlife.WildlifeBase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCropWinterWheat extends BlockCrops {

	public BlockCropWinterWheat() {
		setTickRandomly(true);
        float f = 0.5F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        setCreativeTab(null);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        disableStats();
        setBlockTextureName("wildlife:winter_wheat");
        setBlockName("winter_wheat");
	}

    protected Item func_149866_i() {
        return WildlifeBase.winterWheatSeeds;
    }

    protected Item func_149865_P() {
        return Items.wheat;
    }
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
        //super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_5_);
    	BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		SpecialBlockEvent ev = new SpecialBlockEvent.BlockUpdateEvent(world, x, y, z, world.getBlockMetadata(x, y, z), this, bio);
		if(!MinecraftForge.EVENT_BUS.post(ev) && world.getBlockLightValue(x, y + 1, z) >= 9) {
            int l = world.getBlockMetadata(x, y, z);
            float f = func_149864_n(world, x, y, z);
            if(l == 0) {
            	if(bio.temperature <= 0.5) {
            		//if (rand.nextInt((int)(35.0F / f) + 1) == 0) {
                        world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
                    //}
            	}
            }
            else if (l < 7) {
                if (rand.nextInt((int)(40.0F / f) + 1) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
                }
            }
        }
    }
    
    private float func_149864_n(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, ForgeDirection.UP, this)) {
                    f1 = 1.0F;

                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1)) {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public void func_149863_m(World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        int f = l+MathHelper.getRandomIntegerInRange(world.rand, 1, 3);
        if(l == 0) {
        	BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
        	if(bio.temperature > 0.2) {
        		f = 0;
        	}
        }
        else if (f > 7) {
            f = 7;
        }
        world.setBlockMetadataWithNotify(x, y, z, f, 2);
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

        if (metadata >= 7) {
            if (world.rand.nextInt(4) == 0) {
                ret.add(new ItemStack(this.func_149865_P(), 1, 0));
            }
        }

        return ret;
    }
}
