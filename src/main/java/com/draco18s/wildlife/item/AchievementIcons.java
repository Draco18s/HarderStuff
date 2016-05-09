package com.draco18s.wildlife.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class AchievementIcons extends Item {
	@SideOnly(Side.CLIENT)
    private IIcon[] icons;
	
	public AchievementIcons() {
		super();
		this.setUnlocalizedName("achievement_icons");
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
    	if(meta > icons.length) {
    		return icons[0];
    	}
        return icons[meta];
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
		icons = new IIcon[4];
		for(int i=0; i < icons.length; i++) {
			icons[i] = iconRegister.registerIcon("wildlife:achievement_"+i);
		}
	}
}
