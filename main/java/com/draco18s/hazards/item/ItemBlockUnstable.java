package com.draco18s.hazards.item;

import java.util.List;

import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.BlockOreFlower;

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
		
		switch(stack.getItemDamage()) {
			case 0:
				str += StatCollector.translateToLocal("adj.unstable") + " ";
				break;
			case 1:
				str += StatCollector.translateToLocal("adj.fractured") + " ";
				break;
			case 2:
				str += StatCollector.translateToLocal("adj.broken") + " ";
				break;
		}
		
		str += StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name");
		
		if(stack.getItemDamage() == 3) {
			str += " " + StatCollector.translateToLocal("adj.cobble");
		}
		
		return str;
	}
}
