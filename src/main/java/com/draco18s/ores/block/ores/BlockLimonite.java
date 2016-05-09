package com.draco18s.ores.block.ores;

import java.util.Random;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.util.EnumOreType;

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

public class BlockLimonite extends Block {
	private IIcon[] icons;
	
	public BlockLimonite() {
		super(Material.ground);
		setBlockName("ore_limonite");
		setHardness(3.0f);
		setHarvestLevel("shovel", 0);
		setResistance(1.0f);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockTextureName("ores:ores/limonite");
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		int r = 1;
		if(fortune > 0) r += random.nextInt(1);
		r += random.nextInt(1);
		return r;
    }
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return OresBase.oreChunks;
    }

	@Override
	public int damageDropped(int meta) {
        return EnumOreType.LIMONITE.value;
    }
}
