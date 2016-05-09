package com.draco18s.wildlife.item;

import com.draco18s.wildlife.client.ClientProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemCalendar extends Item {
	public ItemCalendar() {
		super();
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("seasonal_calendar");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister iconReg)
    {
		itemIcon = ClientProxy.calendar;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return ClientProxy.calendar;
	}
}
