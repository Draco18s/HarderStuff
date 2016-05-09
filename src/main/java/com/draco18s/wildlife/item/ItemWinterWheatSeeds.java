package com.draco18s.wildlife.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemWinterWheatSeeds extends ItemSeeds {

	public ItemWinterWheatSeeds(Block theCrop, Block soilBlock) {
		super(theCrop, soilBlock);
		setUnlocalizedName("winter_seeds");
		setTextureName("wildlife:seeds_winter_wheat");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(StatCollector.translateToLocal("description.growsColdWeather"));
	}
}
