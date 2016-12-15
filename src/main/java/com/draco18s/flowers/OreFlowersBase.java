package com.draco18s.flowers;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.draco18s.flowers.block.*;
import com.draco18s.flowers.item.*;
import com.draco18s.flowers.util.*;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.OreFlowerData;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="OreFlowers", name="OreFlowers", version="1.0.0", dependencies = "required-after:HardLib")
public class OreFlowersBase {
	public static Block blockOreFlowers;
	public static Block blockOreFlowers2;
	public static Block blockOreFlowersWater;
	public static Block blockOreFlowers3;

	@Instance(value = "OreFlowers")
	public static OreFlowersBase instance;

	public static Configuration config;

	public static int configScanDepth = 4;

	@SidedProxy(clientSide="com.draco18s.flowers.client.ClientProxy", serverSide="com.draco18s.flowers.CommonProxy")
	public static CommonProxy proxy;
	public static Random rand = new Random();
	//public static Random worldRand;
	public static OreCountGenerator oreCounter;
	public static Logger logger;
	/*
	 *  [x] indicator plants
	 *      http://en.wikipedia.org/wiki/Indicator_plant
	 *      + iron: Diodella teres (wikipedia)
	 *      + gold: Equisetum arvense (wikipedia), http://en.wikipedia.org/wiki/Indicator_plant#Minerals
	 *      + diamond: Vallozia candida http://en.wikipedia.org/wiki/Indicator_plant#Minerals http://sp0.fotolog.com/photo/0/26/47/nature/1159748636_f.jpg
	 *      + redstone: flame lily http://en.wikipedia.org/wiki/Gloriosa_%28genus%29
	 *      + copper: Haumaniastrum robertii http://www.kew.org/herbarium/keys/lamiales/key/Interactive%20key%20to%20the%20genera%20of%20Lamiaceae/Media/Html/images/Haumaniastrum_P._A._Duvign.__Plancke/Haumaniastrum%20flowers%20copy.jpg
	 *             Ocimum centraliafricanum !? http://en.wikipedia.org/wiki/Ocimum_centraliafricanum
	 *      + tin: tansy http://books.google.com/books?id=EcJAAQAAQBAJ&pg=PA231&dq=Geology+potential+tin+accumulators&hl=en&sa=X&ei=sn16VMKZMsbasASdhYCABw&ved=0CB0Q6AEwAA#v=onepage&q=potential%20tin%20accumulator&f=false
	 *      + lead: leadplant http://archive.news.softpedia.com/news/Geologist-Indicator-Plants-71408.shtml
	 *      + uranium: tufted evening-primrose http://geology.utah.gov/surveynotes/gladasked/gladgeobotany.htm
	 *  [ ] thermal expansion (and other) ores
	 *      + Aluminum: Melastoma affine (blue tongue) http://en.wikipedia.org/wiki/Melastoma_affine, http://en.wikipedia.org/wiki/List_of_hyperaccumulators
	 *        * Has wide useage: GregTech, XyCraft, Galacticraft, TiC
	 *      - Zinc: Trifolium pratense (red clover) http://en.wikipedia.org/wiki/Trifolium_pratense, http://en.wikipedia.org/wiki/List_of_hyperaccumulators
	 *              or Thlaspi_caerulescens (alpine penny-cress) http://en.wikipedia.org/wiki/Thlaspi_caerulescens
	 *        * Useage: GregTech, BluePower, Steam Power, SteamCraft2
	 *      + Silver http://en.wikipedia.org/wiki/Brassica_juncea http://en.wikipedia.org/wiki/List_of_hyperaccumulators
	 *        * Usage: Thermal Expansion
	 *      + Nickel Hybanthus floribundus (shrub violet) http://en.wikipedia.org/wiki/Hyperaccumulators_table_%E2%80%93_2_:_Nickel
	 *        * Usage: Thermal Expansion
	 */

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		config = new Configuration(event.getSuggestedConfigurationFile());

		blockOreFlowers = new BlockOreFlower();
		GameRegistry.registerBlock(blockOreFlowers, ItemBlockOreFlower.class, "ore_flowers");
		blockOreFlowers2 = new BlockOreFlower2();
		GameRegistry.registerBlock(blockOreFlowers2, ItemBlockOreFlower2.class, "ore_flowers2");
		blockOreFlowersWater = new BlockOrePondPlant();
		GameRegistry.registerBlock(blockOreFlowersWater, ItemBlockOrePondPlant.class, "ore_flowers_water");
		blockOreFlowers3 = new BlockOreFlower3();
		GameRegistry.registerBlock(blockOreFlowers3, ItemBlockOreFlower3.class, "ore_flowers3");

		oreCounter = new OreCountGenerator();
		if(HardLibAPI.oreManager == null) {
			HardLibAPI.oreManager = new FlowerManager();
		}
		
		config.save();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
    
		configScanDepth = config.getInt("scanDepthSlices", "GENERAL", configScanDepth, 0, 32,
				"Specify bonemeal scan depth in 8-block slices\n" +
				"When using bonemeal on grass, the chunk is scanned this many 8-block slices down to determine\n" +
				"the ore yield. The default of " + configScanDepth + " for example will scan " + (configScanDepth * 8) + " blocks down. Note that ores in slices\n" +
				"which are closer to the surface will have a greater weight on the chances for their associated\n"+
				"flower to appear. This means that higher scan ranges could dilute the indicator results for\n" +
				"chunks that have significant ore diversity.\n"
		);
		config.getInt("OreExists...", "ORES", 1, 0, 2, "These settings should be auto-detected during worldgen and act as an override.\n0 will prevent flowers, 2 will enforce (set automatically), 1 is default.");
		
		ArrayList<ItemStack> oreDictReq;

		OreFlowerData data;
		oreDictReq = OreDictionary.getOres("oreIron");
		if(config.getInt("OreExistsIron", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers, (EnumOreType.IRON.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreGold");
		if(config.getInt("OreExistsGold", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers, (EnumOreType.GOLD.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreDiamond");
		if(config.getInt("OreExistsDiamond", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers, (EnumOreType.DIAMOND.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreRedstone");
		if(config.getInt("OreExistsRedstone", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers, (EnumOreType.REDSTONE.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreTin");
		if(config.getInt("OreExistsTin", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsTin", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.TIN.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreCopper");
		if(config.getInt("OreExistsCopper", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsCopper", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.COPPER.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreLead");
		if(config.getInt("OreExistsLead", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsLead", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.LEAD.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreUranium");
		if(config.getInt("OreExistsUranium", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsUranium", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.URANIUM.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		/*oreDictReq = OreDictionary.getOres("orePitchblende");
		if(oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.URANIUM.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
			addUranium();
			addedOres++;
		}*/
		oreDictReq = OreDictionary.getOres("oreSilver");
		if(config.getInt("OreExistsSilver", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsSilver", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers2, (EnumOreType.SILVER.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreNickel");
		if(config.getInt("OreExistsNickel", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsNickel", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers2, (EnumOreType.NICKEL.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreAluminum");
		if(config.getInt("OreExistsAluminum", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsAluminum", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers2, (EnumOreType.ALUMINUM.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
		}
		oreDictReq = OreDictionary.getOres("oreAluminium");
		if(config.getInt("OreExistsAluminum", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.ALUMINUM.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		/*There are no accumulators for platinum*/
		/*alum = OreDictionary.getOres("orePlatinum");
		if(alum.size() > 0) {
			data = new OreFlowerData(null, EnumOreType.PLATINUM.value-8)&7, 5);
			for(ItemStack oreDictBlock : alum) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}*/
		oreDictReq = OreDictionary.getOres("oreZinc");
		if(config.getInt("OreExistsZinc", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.ZINC.value-8)&7, 5);
			//data.rarity = 200;
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreFluorite");
		if(config.getInt("OreExistsFlourite", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.FLUORITE.value-8)&7, 5);
			//data.rarity = 400;
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("orePitchblende");
		if(config.getInt("OreExistsUranium", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers, (EnumOreType.URANIUM.value-8)&7, 5);
			//data.rarity = 400;
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreCadmium");
		if(config.getInt("OreExistsCadmium", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.CADMIUM.value-8)&7, 5);
			//data.rarity = 400;
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreThorium");
		if(config.getInt("OreExistsThorium", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.THORIUM.value-8)&7, 5);
			//data.rarity = 400;
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		oreDictReq = OreDictionary.getOres("oreOsmium");
		if(config.getInt("OreExistsOsmium", "ORES", 1, 0, 2, "") != 0 && oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers3, (EnumOreType.OSMIUM.value-8)&7, 5);
			//data.rarity = 400;
			for(ItemStack oreDictBlock : oreDictReq) {
				logger.log(Level.INFO, "Registering " + oreDictBlock.getDisplayName() + " as osmium ore.");
				HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
			}
		}
		config.save();

		proxy.registerEventHandlers();
	}
	
	public void addArbitraryOre(Block ore) {
		Property p;
		ItemStack stack = new ItemStack(ore);
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreTin")) {
			p = config.get("ORES", "OreExistsTin", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreCopper")) {
			p = config.get("ORES", "OreExistsCopper", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreLead")) {
			p = config.get("ORES", "OreExistsLead", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreUranium")) {
			p = config.get("ORES", "OreExistsUranium", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreSilver")) {
			p = config.get("ORES", "OreExistsSilver", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreNickel")) {
			p = config.get("ORES", "OreExistsNickel", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreAluminum")) {
			p = config.get("ORES", "OreExistsAluminum", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreZinc")) {
			p = config.get("ORES", "OreExistsZinc", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreFlourite")) {
			p = config.get("ORES", "OreExistsFlourite", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("orePitchblende")) {
			p = config.get("ORES", "OreExistsPitchblende", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreCadmium")) {
			p = config.get("ORES", "OreExistsCadmium", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreThorium")) {
			p = config.get("ORES", "OreExistsThorium", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals("oreOsmium")) {
			p = config.get("ORES", "OreExistsOsmium", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		config.save();
	}

	public static void scatterFlowers(World world, int x, int y, int z, Block b, int meta, int radius, int num, int clusterRadius) {
		Random r = new Random();
		float[] u = RandomInUnitCircle(r);
		int fails = 0;
		int j, k, l;
		Block ba;
		boolean replaceLeaves = num > 1;
		while(num > 0 && fails < 20) {
			j = x + r.nextInt(clusterRadius) - (clusterRadius/2) + Math.round(u[0]*radius);
			k = y-5;
			l = z + r.nextInt(clusterRadius) - (clusterRadius/2) + Math.round(u[1]*radius);
			for(int f=0; f+k <= y+5; f++) {
				Block wb = world.getBlock(j, f+k+1, l);
				if(b.canBlockStay(world, j, f+k+1, l) && (wb.isReplaceable(world, j, f+k+1, l) || wb.getMaterial() == Material.leaves) && !(wb instanceof BlockLiquid || wb instanceof IFluidBlock)) {
					world.setBlock(j, f+k+1, l, b, meta, 3);
					if(meta == EnumOreType.GOLD.value && r.nextInt(8) == 0) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
					}
					if(meta == EnumOreType.REDSTONE.value && r.nextInt(3) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
						world.setBlock(j, f+k+2, l, b, meta,   3);
					}
					if(meta == EnumOreType.TIN.value && r.nextInt(3) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
						world.setBlock(j, f+k+2, l, b, meta,   3);
					}
					--num;
					k = 100;
				}
				/*ba = world.getBlock(j, f+k+1, l);
				if(world.getBlock(j, f+k, l) == Blocks.grass && (ba == Blocks.air || ba == Blocks.snow_layer || ba.getMaterial() == Material.plants || ba.getMaterial() == Material.grass || ba.getMaterial() == Material.vine || (replaceLeaves && ba.getMaterial() == Material.leaves))) {
					//System.out.println("Placed flower");
					world.setBlock(j, f+k+1, l, b, meta, 3);
					if(meta == EnumOreType.GOLD.value && r.nextInt(8) == 0) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
					}
					if(meta == EnumOreType.REDSTONE.value && r.nextInt(3) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
						world.setBlock(j, f+k+2, l, b, meta,   3);
					}
					if(meta == EnumOreType.TIN.value && r.nextInt(3) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+1, l, b, meta|8, 3);
						world.setBlock(j, f+k+2, l, b, meta,   3);
					}
					--num;
					k = 100;
				}*/
			}
			++fails;
		}
		//System.out.println("Failed " + fails + " times.");
	}

	public static float[] RandomInUnitCircle(Random rn) {
		float t = (float)Math.PI * (2*rn.nextFloat());
		float u = rn.nextFloat()+rn.nextFloat();
		float r = (u>1)?2-u:u;

		return new float[] {r*(float)Math.cos(t), r*(float)Math.sin(t)};
	}
}