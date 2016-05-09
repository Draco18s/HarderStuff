package com.draco18s.wildlife.block;

import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.events.SpecialBlockEvent;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.util.StatsAchievements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public class BlockCropWeeds extends BlockCrops {

	public BlockCropWeeds() {
		setTickRandomly(true);
        float f = 0.5F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        setCreativeTab(null);
        setHardness(2.0F);
        setStepSound(soundTypeGrass);
        disableStats();
        setBlockTextureName("wildlife:weeds");
        setBlockName("weeds");
	}

    protected Item func_149866_i() {
        return null;
    }

    protected Item func_149865_P() {
        return null;
    }
    
    //TODO: does this work?
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
    
    /*@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        IIcon i = super.getIcon(side, 7);
        return i;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return super.getIcon(side, world.getBlockMetadata(x, y, z));
    }*/
    
    protected void weedSpread(World world, int x, int y, int z, Random rand) {
    	int placed = 0;
        world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        for(int ox=-1;ox<=1;ox++) {
			for(int oz=-1;oz<=1;oz++) {
				if(world.getBlock(x+ox, y, z+oz) == Blocks.air && world.getBlock(x+ox, y-1, z+oz) == Blocks.farmland) {
					placed++;
					world.setBlock(x+ox, y, z+oz, this, 0, 3);
				}
				else if(world.getBlock(x+ox, y, z+oz) == Blocks.carpet && world.getBlock(x+ox, y-1, z+oz) == Blocks.farmland && rand.nextInt(4) == 0) {
					placed++;
					Blocks.carpet.dropBlockAsItem(world, x+ox, y, z+oz, world.getBlockMetadata(x+ox, y, z+oz), 0);
					world.setBlock(x+ox, y, z+oz, this, 0, 3);
				}
			}
        }
		if(placed < 2) {
			if(rand.nextInt(4) == 0) {
				world.setBlock(x, y-1, z, Blocks.dirt, 0, 3);
				world.setBlock(x, y, z, Blocks.tallgrass, 0, 3);
			}
			else {
				world.setBlockToAir(x, y, z);
			}
		}
    }
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
        //super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_5_);
    	BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		SpecialBlockEvent ev = new SpecialBlockEvent.BlockUpdateEvent(world, x, y, z, world.getBlockMetadata(x, y, z), this, bio);
		if(!MinecraftForge.EVENT_BUS.post(ev)) {
            int l = world.getBlockMetadata(x, y, z);
            if (l < 7) {
                if (rand.nextInt(100) < 35) {
                    world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
                }
            }
            else {
            	weedSpread(world, x, y, z, rand);
            }
        }
    }
    
    public void func_149863_m(World world, int x, int y, int z) {
    	Random rand = new Random();
        for(int ox=-1;ox<=1;ox++) {
			for(int oz=-1;oz<=1;oz++) {
				if(world.getBlock(x+ox, y, z+oz) == this) {
					int l = world.getBlockMetadata(x+ox, y, z+oz);
		            if (l < 7) {
		                if (rand.nextInt((int)(10.0F) + 1) == 0) {
		                    world.setBlockMetadataWithNotify(x+ox, y, z+oz, l+1, 2);
		                }
		            }
		            else {
		            	weedSpread(world, x, y, z, rand);
		            }
				}
			}
        }
        super.func_149863_m(world, x, y, z);
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int m) {
    	ItemStack s = player.getCurrentEquippedItem();
    	if(s != null) {
    		Item i = s.getItem();
    		if(i instanceof ItemTool) {
    			s.damageItem(2, player);
    		}
    		else if(i instanceof ItemHoe) {
    			s.damageItem(1, player);
    		}
    	}
    	player.addStat(StatsAchievements.killWeeds, 1);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        float f = ForgeHooks.blockStrength(this, player, world, x, y, z);
        ItemStack s = player.getCurrentEquippedItem();
    	if(s != null && s.getItem() instanceof ItemHoe) {
			ToolMaterial m = ToolMaterial.valueOf(((ItemHoe)s.getItem()).getToolMaterialName());
			f += m.getEfficiencyOnProperMaterial();// - 1;
			int i = EnchantmentHelper.getEfficiencyModifier(player);
            //ItemStack s = this.inventory.getCurrentItem();

            if (i > 0 && s != null)
            {
                float f1 = (float)(i * i + 1);

                boolean canHarvest = ForgeHooks.canToolHarvestBlock(this, 0, s);

                if (!canHarvest && f <= 1.0F)
                {
                    f += f1 * 0.08F;
                }
                else
                {
                    f += f1;
                }
            }
			
			return f / this.blockHardness / 30;
		}
        return f;
    }
}
