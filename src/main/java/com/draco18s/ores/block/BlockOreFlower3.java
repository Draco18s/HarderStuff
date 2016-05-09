package com.draco18s.ores.block;

import java.util.List;
import java.util.Random;

import com.draco18s.ores.util.EnumOreType;

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

public class BlockOreFlower3 extends BlockBush {

	public static final String[] names = new String[] {
		/*0*/ "broadleaf-arrowhead",
		/*1*/ "N-A",
		/*2*/ "N-A",
		/*3*/ "N-A",
		/*4*/ "N-A",
		/*5*/ "N-A",
		/*6*/ "N-A",
		/*7*/ "N-A"};
	
	private int flowerID;
	private IIcon[] icons;
	public BlockOreFlower3() {
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setBlockName("ore_flower");
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		int m = meta&7;
		return icons[m];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[8];

        for (int i = 0; i < names.length; ++i) {
        	icons[i] = iconRegister.registerIcon("ores:"+names[i]);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
    
    @Override
	public int damageDropped (int metadata) {
    	metadata = metadata&7;
		return metadata;
	}
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
    	super.updateTick(world, x, y, z, rand);
    }
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable){
    	return super.canSustainPlant(world, x, y, z, direction, plantable);
    }
    
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
    	return meta;
    }
}
