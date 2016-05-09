package com.draco18s.industry.block;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IHopperExtender {
	@SideOnly(Side.CLIENT)
    public IIcon hopperIcon(String p_149916_0_);
}
