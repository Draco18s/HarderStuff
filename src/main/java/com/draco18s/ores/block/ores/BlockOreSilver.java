package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.draco18s.ores.util.EnumOreType;

public class BlockOreSilver extends BlockHardOreBase {

	public BlockOreSilver() {
		super(EnumOreType.SILVER, 1, 7, 250, 20, 11, 5);
		setBlockName("ore_silver");
		setHardness(4.0f);
		setHarvestLevel("pickaxe", 2);
        this.setBlockTextureName("silver_ore");
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+meta/6+1);
    }
}
