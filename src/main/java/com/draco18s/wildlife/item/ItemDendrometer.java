package com.draco18s.wildlife.item;

import java.util.List;

import com.draco18s.wildlife.util.TreeDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDendrometer extends Item {

	public ItemDendrometer() {
		this.setUnlocalizedName("dendrometer");
		this.setTextureName("wildlife:dendrometer");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public ItemStack onItemRightClick(ItemStack s, World world, EntityPlayer p) {
		if(!world.isRemote)
			TreeDataHooks.printChunkInfo(p, world.getChunkFromBlockCoords((int)p.posX, (int)p.posZ));
        return s;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack s, EntityPlayer p, List l, boolean a) {
		l.add("Creative Only");
		l.add("Logs all trees in the player's chunk.");
	}
}
