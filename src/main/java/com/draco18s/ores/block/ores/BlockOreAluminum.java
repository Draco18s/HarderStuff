package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.draco18s.ores.util.EnumOreType;

public class BlockOreAluminum extends BlockHardOreBase {

	public BlockOreAluminum() {
		super(EnumOreType.ALUMINUM, 1, 9, 350, 30, 5, 9);
		setBlockName("ore_aluminum");
		setHardness(3.0f);
		setHarvestLevel("shovel", 1);
        setBlockTextureName("bauxite_ore");
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+meta/6+1);
	}
}
