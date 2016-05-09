package com.draco18s.ores.block;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.client.ClientProxy;
import com.draco18s.ores.util.EnumOreType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class BlockDummyOre extends Block {
	public static int renderID;
	private IIcon outlineIcon;

	public BlockDummyOre(EnumOreType oreType) {
		super(Material.iron);
		setBlockName("dummy_"+oreType.name);
		setHardness(1.0f);
		setHarvestLevel("pickaxe", 0);
		setResistance(1.0f);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockTextureName("ores:"+oreType.name+"chunk");
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return renderID;
	}

	public IIcon getIcon(int side, int metadata) {
		if(metadata == 0)
			return outlineIcon;
		return blockIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		super.registerBlockIcons(iconRegister);
		outlineIcon = iconRegister.registerIcon("ores:dummy");
	}

	@Override
	public boolean canRenderInPass(int pass) {
		OresBase.proxy.renderPass = pass;
		return true;
	}
}
