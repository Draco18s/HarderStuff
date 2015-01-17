package com.draco18s.ores.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockOreFlower extends BlockBush {

	public static final String[] names = new String[] {"poorjoe","horsetail","horsetail1","vallozia","flame-lily","flame-lily1",
													   "tansy","tansy1","hauman","leadplant","primrose"};
	private int flowerID;
	private IIcon[] icons;
	public BlockOreFlower() {
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setBlockName("ore_flower");
		//flowerID = id;
		//setBlockName(names[id]);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		//System.out.println("Meta: " + meta);
		return this.icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[11];

        for (int i = 0; i < this.icons.length; ++i) {
        	icons[i] = iconRegister.registerIcon("ores:"+names[i]);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 5));
        
        list.add(new ItemStack(item, 1, 7));
        list.add(new ItemStack(item, 1, 8));
        list.add(new ItemStack(item, 1, 9));
        list.add(new ItemStack(item, 1, 10));
    }
    
    @Override
	public int damageDropped (int metadata) {
    	if(metadata == 2) {
    		return 1;
    	}
    	if(metadata == 4) {
    		return 5;
    	}
    	if(metadata == 6) {
    		return 7;
    	}
		return metadata;
	}
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
    	super.updateTick(world, x, y, z, rand);
    	int m = world.getBlockMetadata(x, y, z);
    	if(m == 1 && rand.nextInt(100) == 0) {
    		world.setBlockMetadataWithNotify(x, y, z, 2, 3);
    	}
    	else if(m == 2 && rand.nextInt(100) == 0) {
    		world.setBlockMetadataWithNotify(x, y, z, 1, 3);
    	}
        //this.checkAndDropBlock(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
    }
    
    /*@Override
    public boolean canBlockStay(World world, int x, int y, int z) {
    	int meta = world.getBlockMetadata(x, y, z);
    	if(meta != 5 && world.getBlock(x, y - 1, z) == this) {
    		return false;
    	}
    	return  world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }*/
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable){
    	int m = world.getBlockMetadata(x, y, z);
    	if(m == 4 && plantable == this) {
    		return true;
    	}
    	if((m == 7 || m == 6) && plantable == this) {
    		return true;
    	}
    	return super.canSustainPlant(world, x, y, z, direction, plantable);
    }
    
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
    	if(meta == 5) {
    		if(world.getBlock(x, y-1, z) != this) {
    			return 4;
    		}
    	}
    	if(meta == 7) {
    		if(world.getBlock(x, y-1, z) == this && world.getBlockMetadata(x, y-1, z) == 7) {
    			world.setBlockMetadataWithNotify(x, y-1, z, 6, 3);
    		}
    	}
    	return meta;
    }
}
