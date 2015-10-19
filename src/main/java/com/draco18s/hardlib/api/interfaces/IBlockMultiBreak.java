package com.draco18s.hardlib.api.interfaces;

/**
 * Used to denote a block that needs to be broken multiple times.  Implemented by BlockHardOreBase.<br/>
 * (block instanceof IBlockMultiBreak) == {@link IHardOres#isHardOre(net.minecraft.block.Block)}<br/>
 * The intended use of this interface is to mark a block class as a Hard Ore so that other mods recognize the ore
 * as one that needs to be treated specially for auto-miners, etc.
 * @author 
 *
 */
public interface IBlockMultiBreak {
	int getMetaChangeOnBreak();
}
