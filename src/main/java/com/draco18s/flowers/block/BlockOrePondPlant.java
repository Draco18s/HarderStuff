package com.draco18s.flowers.block;

import java.util.List;

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
import net.minecraftforge.common.EnumPlantType;

public class BlockOrePondPlant extends BlockBush {
	public static final String[] names = new String[] {
		/*0*/ "duckweed",
		/*1*/ "unknown",
		/*2*/ "unknown",
		/*3*/ "unknown",
		/*4*/ "unknown",
		/*5*/ "unknown",
		/*6*/ "unknown",
		/*7*/ "unknown"};

	private IIcon[] icons;

	public BlockOrePondPlant() {
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		//super(p_i45395_1_);
        float f = 0.5F;
        float f1 = 0.015625F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        setCreativeTab(CreativeTabs.tabDecorations);
		setBlockName("ore_flower");
	}
	
    public int getRenderType() {
        return 23;
    }
    
    protected boolean canPlaceBlockOn(Block p_149854_1_) {
        return p_149854_1_ == Blocks.water;
    }

	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[11];

        for (int i = 0; i < names.length; ++i) {
        	if(i < names.length) {
        		icons[i] = iconRegister.registerIcon("flowers:"+names[i]);
        	}
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        /*list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
        list.add(new ItemStack(item, 1, 6));
        list.add(new ItemStack(item, 1, 7));*/
    }
    
    @Override
	public int damageDropped (int metadata) {
    	metadata = metadata&7;
		return metadata;
	}
    
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
    	return EnumPlantType.Water;
    }
}
