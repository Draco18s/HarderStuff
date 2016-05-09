package com.draco18s.hazards.item;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBlockUnstable extends ItemBlock {
	public ItemBlockUnstable(Block b) {
		super(b);
		setHasSubtypes(true);
	}

	@Override
    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String str ="";
		int m = stack.getItemDamage()&3;
		switch(m) {
			case 0:
			case 3:
				str += StatCollector.translateToLocal("adj.unstable") + " ";
				break;
			case 1:
				str += StatCollector.translateToLocal("adj.fractured") + " ";
				break;
			case 2:
				str += StatCollector.translateToLocal("adj.broken") + " ";
				break;
		}
		//System.out.println(" -> " + stack.getUnlocalizedName());
		//System.out.println(" -> " + StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name"));
		str += StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name");
		
		if(m == 3) {
			str += " " + StatCollector.translateToLocal("adj.cobble");
		}
		
		return str;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		if(metadata != 3) {
			if (!world.setBlock(x, y, z, field_150939_a, metadata, 3)) {
				return false;
			}

			if (world.getBlock(x, y, z) == field_150939_a) {
				field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
				field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
			}

			return true;
		}
		else {
			if (!world.setBlock(x, y, z, field_150939_a, metadata|4, 3)) {
				return false;
			}

			if (world.getBlock(x, y, z) == field_150939_a) {
				field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
				field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
			}
		}
		return true;
	}
}
