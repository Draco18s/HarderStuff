package com.draco18s.wildlife.item;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemBlockRottingWood extends ItemBlock {
	private IIcon[] icons;
	public static final String[] names = new String[] {"Rotting_Log","Compost"};

	public ItemBlockRottingWood(Block b) {
		super(b);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }

	@Override
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }
    
	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
		if((itemStack.getItemDamage()&4) == 0)
			return "item." + names[0];
		else
			return "item." + names[1];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if(stack.getItemDamage() > 5) {
			list.add(StatCollector.translateToLocal("description.turnsToGrass"));
		}
	}
}
