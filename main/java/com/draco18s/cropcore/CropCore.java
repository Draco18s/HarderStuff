package com.draco18s.cropcore;

import java.util.Map;

import net.minecraft.item.ItemStack;

import com.draco18s.cropcore.asm.CropPatcher;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "CropCore")
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.TransformerExclusions(value = "com.draco18s.cropcore")
@IFMLLoadingPlugin.SortingIndex(value = 1001)
public class CropCore implements IFMLLoadingPlugin {
	public CropCore() {
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
				CropPatcher.class.getName()
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
