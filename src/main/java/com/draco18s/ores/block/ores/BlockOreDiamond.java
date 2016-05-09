package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.draco18s.hardlib.HashUtils;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockOreDiamond extends BlockHardOreBase {
	private IIcon[] icons;
	private boolean avoidGeneration = false;
	
	public BlockOreDiamond() {
		super(EnumOreType.DIAMOND, 3, 9, 400, 20, 8, 11);
		setBlockName("ore_diamond");
		setHardness(12.0f);
		setHarvestLevel("pickaxe", 2);
        this.setBlockTextureName("diamond_ore");
	}
	
	public int quantityDropped(int meta, int fortune, Random random) {
		float f = 0;
		if(fortune > 0) {
			f = random.nextInt(fortune+meta/5+1);
		}
		return 1 + Math.round(f/1.0f);//fortune 3 ~16 per block
    }
}
