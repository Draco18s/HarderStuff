package com.draco18s.ores.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.draco18s.ores.OresBase;

/*import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import Reika.DragonAPI.Instantiable.Data.Maps.ItemHashMap;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap;
import Reika.DragonAPI.Interfaces.OreType;
import Reika.DragonAPI.ModRegistry.ModOreList;*/

public enum OreTypeHard /*implements OreType*/ {
	/*IRON("Iron", 0xD8AD91, OreRarity.AVERAGE, "chunkIron", 1, OresBase.oreIron, "oreIronHard"),
	GOLD("Gold", 0xD6A400, OreRarity.SCATTERED, "chunkGold", 1, OresBase.oreGold, "oreGoldHard"),
	DIAMOND("Diamond", 0x68FFE3, OreRarity.SCARCE, "chunkDiamond", 1, OresBase.oreDiamond, "oreDiamondHard"),
	LEAD("Lead", 0x697597, OreRarity.SCATTERED, "chunkLead", 1, OresBase.oreLead, "oreLeadHard"),
	URANIUM("Uranium", 0x4CFF00, OreRarity.SCARCE, "chunkUranium", 1, OresBase.oreUranium, "oreUraniumHard"),
	TIN("Tin", 0xB2D5E9, OreRarity.EVERYWHERE, "chunkTin", 1, OresBase.oreTin, "oreTinHard"),
	COPPER("Copper", 0xBC6C01, OreRarity.COMMON, "chunkCopper", 1, OresBase.oreCopper, "oreCopperHard"),
	SILVER("Silver", 0xA4D0DA, OreRarity.SCATTERED, "chunkSilver", 1, OresBase.oreSilver, "oreSilverHard"),
	NICKEL("Nickel", 0xD0CCAD, OreRarity.SCATTERED, "chunkNickel", 1, OresBase.oreNickel, "oreNickelHard");

	private ArrayList<ItemStack> ores = new ArrayList();
	public final String displayName;
	private String[] oreLabel;
	public final int dropCount;
	public final int oreColor;
	private String product;
	//private boolean init;
	public final OreRarity rarity;
	//private MultiMap<String, ItemStack> perName = new MultiMap();
	//private static final ArrayList<ItemStack> blocks = new ArrayList<ItemStack>();
	public static final OreTypeHard[] oreList = values();
	//private static final ItemHashMap<ModOreList> oreMappings = new ItemHashMap();
	//private static final HashSet<String> oreNames = new HashSet();
	
	private OreTypeHard(String n, int color, OreRarity r, String prod, int count, Block oreBlock, String... ore) {
		dropCount = count;
		oreColor = color;
		displayName = n;
		product = prod;
		oreLabel = new String[ore.length];
		for (int i = 0; i < ore.length; i++) {
			oreLabel[i] = ore[i];
		}
		for(int i=0; i < 16; i++)
			ores.add(new ItemStack(oreBlock, 1, i));
		rarity = r;
	}

	@Override
	public boolean existsInGame() {
		return !ores.isEmpty();
	}

	@Override
	public boolean canGenerateIn(Block b) {
		return b == Blocks.stone;
	}

	@Override
	public Collection<ItemStack> getAllOreBlocks() {
		return Collections.unmodifiableCollection(ores);
	}

	@Override
	public ItemStack getFirstOreBlock() {
		if (!this.existsInGame())
			return null;
		return ores.get(0);
	}

	@Override
	public OreRarity getRarity() {
		return rarity;
	}

	@Override
	public boolean isEnd() {
		return false;
	}

	@Override
	public boolean isNether() {
		return false;
	}*/
}
