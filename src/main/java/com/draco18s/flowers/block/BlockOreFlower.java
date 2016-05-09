package com.draco18s.flowers.block;

import java.util.List;
import java.util.Random;

import com.draco18s.flowers.util.EnumOreType;

import cpw.mods.fml.common.Loader;
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

	public static final String[] names = new String[] {
		/*0*/ "poorjoe",
		/*1*/ "horsetail",//8
		/*2*/ "vallozia",
		/*3*/ "flame-lily",//9
		/*4*/ "tansy",//10
		/*5*/ "hauman",
		/*6*/ "leadplant",
		/*7*/ "primrose"};
	public static final String[] bottoms = new String[] {
		"horsetail1",
		"flame-lily1",
		"tansy1"
	};
	
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
		int m = meta&7;
		if(meta != m) {
			switch(m) {
				case 1:
					m = 8;
					break;
				case 3:
					m = 9;
					break;
				case 4:
					m = 10;
					break;
			}
		}
		return icons[m];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[11];

        for (int i = 0; i < names.length + bottoms.length; ++i) {
        	if(i < names.length) {
        		icons[i] = iconRegister.registerIcon("flowers:"+names[i]);
        	}
        	else {
        		icons[i] = iconRegister.registerIcon("flowers:"+bottoms[i-names.length]);
        	}
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
        list.add(new ItemStack(item, 1, 6));
        list.add(new ItemStack(item, 1, 7));
    }
    
    @Override
	public int damageDropped (int metadata) {
    	metadata = metadata&7;
		return metadata;
	}
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
    	super.updateTick(world, x, y, z, rand);
    	int m = world.getBlockMetadata(x, y, z);
    	if((m&7) == 1 && rand.nextInt(100) == 0) {
    		world.setBlockMetadataWithNotify(x, y, z, m^8, 3);
    	}
    }
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable){
    	int m = world.getBlockMetadata(x, y, z);
    	boolean f = world.getBlock(x, y-1, z) != this;
    	if((m&7) == EnumOreType.TIN.value && plantable == this) {
    		return true&f;
    	}
    	if(m == (EnumOreType.REDSTONE.value|8) && plantable == this) {
    		return true&f;
    	}
    	return super.canSustainPlant(world, x, y, z, direction, plantable);
    }
    
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
    	if(meta == EnumOreType.TIN.value) {
    		if(world.getBlock(x, y-1, z) == this && world.getBlockMetadata(x, y-1, z) == meta) {
    			world.setBlockMetadataWithNotify(x, y-1, z, meta|8, 3);
    		}
    	}
    	if(meta == EnumOreType.REDSTONE.value) {
    		if(world.getBlock(x, y-1, z) == this && world.getBlockMetadata(x, y-1, z) == (meta|8)) {
    			return meta;
    		}
    		else {
    			return meta|8;
    		}
    	}
    	return meta;
    }
}
