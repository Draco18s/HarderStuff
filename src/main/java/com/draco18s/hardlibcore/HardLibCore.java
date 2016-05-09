package com.draco18s.hardlibcore;

import java.util.Map;

import net.minecraft.item.ItemStack;

import com.draco18s.hardlibcore.asm.HardLibPatcher;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "HardLibCore")
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.TransformerExclusions(value = {"com.draco18s.hardlibcore"/*,"com.draco18s.hardlib.events"*/})
@IFMLLoadingPlugin.SortingIndex(value = 1001)
public class HardLibCore implements IFMLLoadingPlugin {
	public HardLibCore() {
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
				HardLibPatcher.class.getName()
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
