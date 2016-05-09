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

public class ItemDendricide extends Item {

	public ItemDendricide() {
		this.setUnlocalizedName("dendricide");
		this.setTextureName("wildlife:dendricide");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int side, float hitx, float hity, float hitz) {
		if(!world.isRemote) {
			//System.out.println("Killing a tree, killing a tree...");
			TreeDataHooks.itemKillTree(world, x, y, z);
			//TreeDataHooks.printChunkInfo(p, world.getChunkFromBlockCoords((int)p.posX, (int)p.posZ));
		}
        return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack s, EntityPlayer p, List l, boolean a) {
		l.add("Creative Only");
		l.add("Immediately kills the indicated tree.");
	}
}
