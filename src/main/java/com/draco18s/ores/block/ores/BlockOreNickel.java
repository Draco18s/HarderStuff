package com.draco18s.ores.block.ores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.draco18s.ores.util.EnumOreType;

public class BlockOreNickel extends BlockHardOreBase {

	public BlockOreNickel() {
		super(EnumOreType.NICKEL, 1, 9, 200, 25, 6, 8);
		setBlockName("ore_nickel");
		setHardness(4.0f);
		setHarvestLevel("pickaxe", 2);
        this.setBlockTextureName("nickel_ore");
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1 + fortune + random.nextInt(fortune+meta/6+1);
    }
}
