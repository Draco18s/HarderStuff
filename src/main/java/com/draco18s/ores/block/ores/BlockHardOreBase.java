package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.IBlockMultiBreak;
import com.draco18s.hardlib.api.internal.BlockWrapper;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.EnumOreType;
import com.draco18s.ores.util.OreDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class BlockHardOreBase extends Block implements IBlockMultiBreak {
	public static final ForgeDirection[] DROP_SEARCH_DIRECTIONS = {ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.DOWN};
	protected IIcon[] icons;
	private boolean avoidGeneration = false;
	protected final EnumOreType thisType;
	private final int scatterRadius;
	private final int scatterNum;
	private final int clusterSize;
	private final int maxMeta;
	private final int flowerProbability;
	public final int metaChange;

	/**
	 * Standard implementation for an ore of the "Hard Ores" variety.
	 * @param t {@link EnumOreType} definition (used for dropped item metadata value; if {@link #damageDropped(int meta)} is overridden, this may be null)
	 * @param metaThreshhold ore meta values above this do not spawn flowers (motherloads don't show on the surface)
	 * @param flowerChance probability per ore block placed to scatter flowers; generally 1:200 is common, 1:300 is rare
	 * @param scatRad how far away from the block (max) to spawn flowers
	 * @param scatNum how many flowers to place each time (this is a maximum, each flower will attempt to place 10 times)
	 * @param clustSize radius size of the cluster
	 */
	public BlockHardOreBase(EnumOreType t, int metaReduction, int metaThreshhold, int flowerChance, int scatRad, int scatNum, int clustSize) {
		super(Material.rock);
		setResistance(5.0f);
		setCreativeTab(CreativeTabs.tabBlock);
		thisType = t;
		metaChange = metaReduction;
		maxMeta = metaThreshhold;
		flowerProbability = flowerChance;
		scatterRadius = scatRad;
		scatterNum = scatNum;
		clusterSize = clustSize;
	}
	
	private int reqCount = 0;
	private int lastIcon = 0;
	
	@Override
	@SideOnly(Side.CLIENT)
    public final IIcon getIcon(int side, int meta) {
		if(meta < icons.length)
			return this.icons[meta];
		//for NEI recipes
		reqCount = (reqCount + 1)%100;
		if(reqCount == 0)
			lastIcon = (lastIcon + 1)%16;
		return this.icons[15-lastIcon];
    }

	@Override
	@SideOnly(Side.CLIENT)
    public final IIcon getIcon(IBlockAccess world, int x, int y, int z, int p) {
        return this.icons[world.getBlockMetadata(x, y, z)];
    }

	/**
	 * Override this to register icons under a different modID
	 */
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[16];
		for(int i=0; i < 16; i++) {
			icons[i] = iconRegister.registerIcon("ores:ores/" + getTextureName() + "_"+i);
		}
    }
	
	@Override
	public abstract int quantityDropped(int meta, int fortune, Random random);
	
	/**
	 * Override this to drop a different item
	 */
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return OresBase.oreChunks;
    }

	@Override
	public final int damageDropped(int meta) {
        return thisType.value;
    }
	
    @Override
    public final int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }
	
	@SideOnly(Side.CLIENT)
    public final void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 15));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    	int count = quantityDropped(metadata, fortune, world.rand);
    	Item item = getItemDropped(metadata, world.rand, fortune);
    	ret.add(new ItemStack(item, count, damageDropped(metadata)));
    	
    	for(int m = metadata-metaChange; m >= 0; m-=metaChange) {
    		ArrayList<ItemStack> extra = getDropsStandard(world, x, y, z, m, fortune);
    		//25% lost if not using API methods
    		//player mining will avoid this, as only the first stack (above) is usex by dropBlockAsItemWithChance
    		for(ItemStack ex : extra) {
    			float f = ex.stackSize * 0.75f;
    			int c = (int) Math.floor(f);
    			f -= c;
    			if(f > 0 && world.rand.nextFloat() < f) {
    				c++;
    			}
    			ex.stackSize = c;
    		}
    		ret.addAll(extra);
    	}
    	
        return ret;
    }
    
    protected ArrayList<ItemStack> getDropsStandard(World world, int x, int y, int z, int metadata, int fortune) {
    	ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    	Item item = getItemDropped(metadata, world.rand, fortune);
        int count = quantityDropped(metadata, fortune, world.rand);
        ret.add(new ItemStack(item, count, damageDropped(metadata)));
        return ret;
    }
	
	protected final void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) { // do not drop items while restoring blockstates, prevents item dupe
            if (captureDrops.get()) {
                capturedDrops.get().add(stack);
                return;
            }
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            for(ForgeDirection dir : DROP_SEARCH_DIRECTIONS) {
            	if(!world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).isNormalCube() || dir == ForgeDirection.DOWN) {
            		EntityItem entityitem = new EntityItem(world, (double)x + d0+dir.offsetX, (double)y + d1+dir.offsetY, (double)z + d2+dir.offsetZ, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    world.spawnEntityInWorld(entityitem);
                    return;
            	}
            }
        }
    }

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if (!world.isRemote) {
            ArrayList<ItemStack> drops = getDrops(world, x, y, z, meta, fortune);
            drops.subList(1, drops.size()).clear();
            //ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            /*ItemStack it = drops.get(0);
            for(int j = it.stackSize-1; j >=0; j--) {
            	drops.add(new ItemStack(it.getItem(),1, it.getItemDamage()));
            }*/
            chance = ForgeEventFactory.fireBlockHarvesting(drops, world, this, x, y, z, meta, fortune, chance, false, harvesters.get());
            if(world.getBlock(x, y, z) != this && meta >= metaChange) {
            	world.setBlock(x, y, z, this, meta-metaChange, 3);
            }
            for (ItemStack item : drops) {
                if (world.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem(world, x, y, z, item);
                }
            }
        }
    }
	
	@Override
	protected final ItemStack createStackedBlock(int meta) {
        Item item = Item.getItemFromBlock(this);
        return new ItemStack(item, 1, meta);
    }
	
	@Override
	public final boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if(player != null && player.capabilities.isCreativeMode) {
        	avoidGeneration = true;
			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
		}
        return removedByPlayer(world, player, x, y, z);
	}
	
	/**
	 * This generates the flowers during worldgen.
	 */
	@Override
	public final void onBlockAdded(World world, int x, int y, int z) {
		if(avoidGeneration) {
			avoidGeneration = false;
			return;
		}
		//insures consistent, repeatable, randomization
		int s = HashUtils.hash((int)world.getSeed(), x);
		s = HashUtils.hash(s, y);
		s = HashUtils.hash(s, z);
		Random rand = new Random(s);
		int m = world.getBlockMetadata(x, y, z);
		BlockWrapper bw = new BlockWrapper(this,-1);
		OreFlowerData dat = HardLibAPI.oreManager.getOreList().get(bw);
		
		if(dat == null) {
			//Exception e = new Exception("Hard Ore block, " + this.getUnlocalizedName() + " has no flower data.  This is a bug!");
			//System.out.println(e.getMessage());
			//e.printStackTrace();
			//System.out.println("Listing all mapped blocks:");
			Map<BlockWrapper,OreFlowerData> map = HardLibAPI.oreManager.getOreList();
			for(BlockWrapper b : map.keySet()) {
				if(b.equals(bw)) {
					dat = map.get(b);
					OresBase.logger.log(Level.INFO, "Issue retrieving data for " + this.getUnlocalizedName() + ". " + b.block.getUnlocalizedName() + ":" + b.meta + " should be equal to " + bw.block.getUnlocalizedName() + ":" + bw.meta);
					break;
				}
				//System.out.println("    " + b.block.getUnlocalizedName()+":" + b.meta + " " + bw.equals(b) + "," + (bw.hashCode() == b.hashCode()));
			}
		}
		if(dat != null) {
			if(m < maxMeta && rand.nextInt(flowerProbability) == 0) {
				for(int j=1; y+j < 250; j++) {
					if(world.getBlock(x, y+j, z) == Blocks.grass) {
						OresBase.scatterFlowers(world, x, y+j+1, z, dat.flower, dat.metadata, scatterRadius, scatterNum, clusterSize);
						return;
					}
				}
			}
		}
		else {
			OresBase.logger.log(Level.ERROR, "Hard Ore block, " + this.getUnlocalizedName() + " has no flower data.  This is a bug!");
		}
	}

	@Override
	public int getMetaChangeOnBreak() {
		return metaChange;
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz) {
		if(player != null && player.capabilities.isCreativeMode && player.getCurrentEquippedItem() == null) {
        	avoidGeneration = true;
        	int m = world.getBlockMetadata(x, y, z);
        	m = m - (player.isSneaking()?1:4);
        	if(m < 0)
        		m += 16;
			world.setBlockMetadataWithNotify(x, y, z, m, 3);
		}
        return false;
    }
}
