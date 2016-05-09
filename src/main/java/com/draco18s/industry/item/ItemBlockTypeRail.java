package com.draco18s.industry.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemBlockTypeRail extends ItemBlock {

	public ItemBlockTypeRail(Block b) {
		super(b);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		if(GuiScreen.isShiftKeyDown()) {
			list.add("Place a sign above or below to configure.");
			list.add("Use [cartname]=[output] on each line");
			list.add("e.g. 'Furnace=8' for furnace carts");
		}
		else {
			list.add(EnumChatFormatting.DARK_AQUA + "Press shift for details.");
		}
	}
}
