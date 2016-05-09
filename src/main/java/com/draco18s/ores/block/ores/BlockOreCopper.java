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

public class BlockOreCopper extends BlockHardOreBase {
	
	public BlockOreCopper() {
		super(EnumOreType.COPPER, 1, 9, 300, 20, 4, 9);
		setBlockName("ore_copper");
		setHardness(6.0f);
		setHarvestLevel("pickaxe", 1);
        setBlockTextureName("copper_ore");
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+meta/6+1);
    }
}
