package com.draco18s.ores;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.draco18s.hardlib.CogConfig;
import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.RecipesUtil;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.hazards.enchantments.EnchantmentStoneStress;
import com.draco18s.ores.block.*;
import com.draco18s.ores.block.ores.*;
import com.draco18s.ores.enchantments.*;
import com.draco18s.ores.entities.*;
import com.draco18s.ores.item.*;
import com.draco18s.ores.recipes.*;
import com.draco18s.ores.util.*;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="HarderOres", name="HarderOres", version="{@version:ore}", dependencies = "required-after:HardLib")
public class OresBase {
	public static Block oreLimonite;
	public static Block oreIron;
	public static Block oreGold;
	public static Block oreDiamond;
	public static Block oreRedstone;
	public static Block oreTin;
	public static Block oreCopper;
	public static Block oreLead;
	public static Block oreUranium;
	public static Block oreSilver;
	public static Block oreNickel;
	public static Block oreAluminum;
	public static Block oreOsmium;

	public static Block dummyOreIron;
	public static Block dummyOreGold;
	public static Block dummyOreDiamond;
	public static Block dummyOreTin;
	public static Block dummyOreCopper;
	public static Block dummyOreLead;
	public static Block dummyOreUranium;
	public static Block dummyOreSilver;
	public static Block dummyOreNickel;
	public static Block dummyOreAluminum;
	public static Block dummyOreOsmium;

	public static Block blockSifter;
	public static Block blockMill;
	public static Block blockVane;
	public static Block blockAxle;
	public static Block blockSluice;
	public static Block blockProcessor;

	public static Block blockOreFlowers;
	public static Block blockOreFlowers2;
	public static Block blockOreFlowers3;

	public static Item oreChunks;
	public static Item nuggetIron;
	public static Item smallDusts;
	public static Item largeDusts;
	public static Item smallCrushed;
	/*diamond studded tools*/
	public static Item diaStudPick;
	public static Item diaStudAxe;
	public static Item diaStudShovel;
	public static Item diaStudHoe;
	public static Item itemAchievementIcons;
	/*diorite tools
		public static Item dioStudPick;
		public static Item dioStudAxe;
		public static Item dioStudShovel;
		public static Item dioStudHoe;*/
	public static Material materialSluiceBox = (new Material(MapColor.woodColor));
	public static Material materialGas = (new MaterialLiquid(MapColor.airColor));

	public static ToolMaterial toolMaterialDiamondStud;

	@Instance(value = "HarderOres")
	public static OresBase instance;

	public static Configuration config;
	public static boolean sluiceEnabled;
	public static boolean sluiceVersion;
	public static boolean sluiceAllowDirt;

	@SidedProxy(clientSide="com.draco18s.ores.client.ClientProxy", serverSide="com.draco18s.ores.CommonProxy")
	public static CommonProxy proxy;
	public static Random rand = new Random();
	//public static Random worldRand;
	public static OreCountGenerator oreCounter;
	public static Logger logger;
	public static boolean useSounds;
	public static boolean sluicePlacesWaterSource;
	
	public static Enchantment enchPulverize;
	public static Enchantment enchCracker;

	/*  Notes
	 *  [ ] Advanced sifter (4?:1)
	 *  [x] Retexture end of axle to look like a log
	 *  [x] Tool to find ore?
	 *      - Ironically, its bonemeal
	 *  [ ] Tool to crush ore?
	 *  [x] diamond studded tools
	 *  [x] lignite
	 *  [ ] pulverizer?
	 *  [x] redo how the sifter and windmill convert items: use a dictionary
	 *  [x] millstone now accepts items directly...one at a time
	 *  [x] added sound clip to mill action
	 *  [x] shorten sound clip slighty (7 seconds?)
	 *  [x] bones -> bone meal
	 *  [x] seeds -> flour
	 *  [x] reeds -> sugar
	 *  [x] sifter won't accept items from hopper
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
	 *  [x] config options:
	 *      + sluice version
	 *      + hard flour grinding (remove wheat recipes)
	 *      + hard bonemeal grinding (remove vanilla bonemeal recipe)
	 */

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		HardLibAPI.recipeManager = new RecipeManager();
		HardLibAPI.oreManager = new OreManager();
		/*if(!Loader.isModLoaded("CustomOreGen")) {
        		throw new RuntimeException("Harder Ores requires Custom Ore Generation to work properly");
        	}*/
		logger = event.getModLog();
		config = new Configuration(event.getSuggestedConfigurationFile());

		oreLimonite = new BlockLimonite();
		GameRegistry.registerBlock(oreLimonite, "ore_limonite");
		oreIron = new BlockOreIron();
		GameRegistry.registerBlock(oreIron, ItemBlockOre.class, "ore_iron");
		oreGold = new BlockOreGold();
		GameRegistry.registerBlock(oreGold, ItemBlockOre.class, "ore_gold");
		oreDiamond = new BlockOreDiamond();
		GameRegistry.registerBlock(oreDiamond, ItemBlockOre.class, "ore_diamond");
		oreRedstone = new BlockOreRedstone();
		GameRegistry.registerBlock(oreRedstone, ItemBlockOre.class, "ore_redstone");

		oreTin = new BlockOreTin();
		GameRegistry.registerBlock(oreTin, ItemBlockOre.class, "ore_tin");
		oreCopper = new BlockOreCopper();
		GameRegistry.registerBlock(oreCopper, ItemBlockOre.class, "ore_copper");
		oreLead = new BlockOreLead();
		GameRegistry.registerBlock(oreLead, ItemBlockOre.class, "ore_lead");
		oreUranium = new BlockOreUranium();
		GameRegistry.registerBlock(oreUranium, ItemBlockOre.class, "ore_uranium");
		oreSilver = new BlockOreSilver();
		GameRegistry.registerBlock(oreSilver, ItemBlockOre.class, "ore_silver");
		oreNickel = new BlockOreNickel();
		GameRegistry.registerBlock(oreNickel, ItemBlockOre.class, "ore_nickel");
		oreAluminum = new BlockOreAluminum();
		GameRegistry.registerBlock(oreAluminum, ItemBlockOre.class, "ore_bauxite");
		oreOsmium = new BlockOreOsmium();
		GameRegistry.registerBlock(oreOsmium, ItemBlockOre.class, "ore_osmium");

		//if(Loader.isModLoaded("RotaryCraft")) {
			/*dummy ores for RotaryCraft*/
			dummyOreIron = new BlockDummyOre(EnumOreType.IRON);
			dummyOreGold = new BlockDummyOre(EnumOreType.GOLD);
			GameRegistry.registerBlock(dummyOreIron, "dummyOreIron");
			GameRegistry.registerBlock(dummyOreGold, "dummyOreGold");
			OreDictionary.registerOre("oreIron", new ItemStack(dummyOreIron));
			OreDictionary.registerOre("oreGold", new ItemStack(dummyOreGold));
			GameRegistry.addSmelting(dummyOreIron, new ItemStack(Items.iron_ingot), extraOres);
			GameRegistry.addSmelting(dummyOreGold, new ItemStack(Items.gold_ingot), extraOres);
		//}

		blockOreFlowers = new BlockOreFlower();
		GameRegistry.registerBlock(blockOreFlowers, ItemBlockOreFlower.class, "ore_flowers");
		blockOreFlowers2 = new BlockOreFlower2();
		GameRegistry.registerBlock(blockOreFlowers2, ItemBlockOreFlower2.class, "ore_flowers2");
		blockOreFlowers3 = new BlockOreFlower3();
		GameRegistry.registerBlock(blockOreFlowers3, ItemBlockOreFlower3.class, "ore_flowers3");

		blockSifter = new BlockSifter();
		GameRegistry.registerBlock(blockSifter, "machine_sifter");
		blockProcessor= new BlockOreProcessor();
		GameRegistry.registerBlock(blockProcessor, "machine_processor");
		blockMill = new BlockMillstone();
		GameRegistry.registerBlock(blockMill, "machine_millstone");
		blockVane = new BlockWindvane();
		GameRegistry.registerBlock(blockVane, "machine_windvane");
		blockAxle = new BlockAxle();
		GameRegistry.registerBlock(blockAxle, "machine_axle");
		config.addCustomCategoryComment("SLUICE", "Various settings for the sluice.");
		config.get("SLUICE", "canFind...", false, "These setting indicate if the sluice can filter for the given mineral. Relative weights are handled\ninternally. Even if all settings are false, gravel can still be filtered to flint.");
		sluiceVersion = config.getBoolean("useContainerSluice", "SLUICE", false, "The Container-Sluice holds items, has a GUI, and supports comparator output. Non-container-sluice\nuses faux water flow mechanics, item entities, does not store items, and does not support the\ncomparator.");
		sluiceEnabled = config.getBoolean("enableSluice","SLUICE", true, "Set to false to disable the sluice recipe.");
		sluiceAllowDirt = config.getBoolean("sluiceAllowsDirt","SLUICE", false, "Set to true to allow dirt to be used in the sluice.  Dirt acts like sand.");
		int cycle = config.getInt("sluiceCycleTime", "SLUICE", 2, 1, 20, "Time it takes for the sluice to make 1 operation.  This value is multiplied by 75 ticks.");
		
		TileEntitySluiceBottom.cycleLength = cycle * 15;
		TileEntitySluice.cycleLength = cycle * 15;
		
		if(sluiceVersion) {
			blockSluice = new BlockSluice();
			GameRegistry.registerBlock(blockSluice, "machine_sluice");
		}
		else {
			blockSluice = new BlockSluiceBottom();
			GameRegistry.registerBlock(blockSluice, "machine_sluice");
		}
		/*TileEntitySluiceBottom.findIron = TileEntitySluice.findIron = config.get("canFindIron", "SLUICE", true).getBoolean();
        	TileEntitySluiceBottom.findGold = TileEntitySluice.findGold = config.get("canFindGold", "SLUICE", true).getBoolean();
        	TileEntitySluiceBottom.findDiam = TileEntitySluice.findDiam = config.get("canFindDiamond", "SLUICE", false).getBoolean();
        	TileEntitySluiceBottom.findReds = TileEntitySluice.findReds = config.get("canFindRedstone", "SLUICE", true).getBoolean();*/
		useSounds = config.getBoolean("useSounds", "GENERAL", true, "Toggle to enable/disable sounds.");
		sluicePlacesWaterSource = config.getBoolean("sluicePlacesWaterSource", "GENERAL", true, "Toggle to enable/disable asthetic water placement.");

    	int id = config.getInt("PulverizerEnchantmentID", "GENERAL", 81, 64, 255, "Enchantment ID for the Pulverizer enchantment.");
    	enchPulverize = new EnchantPulverize(id,7);
    	id = config.getInt("VeinCrackerEnchantmentID", "GENERAL", 82, 64, 255, "Enchantment ID for the Vein Cracker enchantment.");
    	enchCracker = new EnchantVeinCracker(id,7);
		
		config.save();

		GameRegistry.registerTileEntity(TileEntitySifter.class, "ores.sifter");
		GameRegistry.registerTileEntity(TileEntityOreProcessor.class, "ores.processor");
		GameRegistry.registerTileEntity(TileEntityMillstone.class, "ores.millstone");
		GameRegistry.registerTileEntity(TileEntityWindmill.class, "ores.windmill");
		GameRegistry.registerTileEntity(TileEntityWindvane.class, "ores.windvane");
		if(sluiceVersion) {
			GameRegistry.registerTileEntity(TileEntitySluice.class, "ores.sluice");
		}
		else {
			GameRegistry.registerTileEntity(TileEntitySluiceBottom.class, "ores.sluice");
		}

		oreChunks = new ItemRawOre();
		GameRegistry.registerItem(oreChunks, "ore_chunk");
		nuggetIron = (new Item()).setUnlocalizedName("iron_nugget").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("ores:iron_nugget");
		GameRegistry.registerItem(nuggetIron, "iron_nugget");
		smallDusts = new ItemOreDustSmall();
		GameRegistry.registerItem(smallDusts, "smallDust");
		largeDusts = new ItemOreDustLarge();
		GameRegistry.registerItem(largeDusts, "largeDust");
		if(Loader.isModLoaded("IC2")){
			smallCrushed = new ItemCrushOre();
			GameRegistry.registerItem(smallCrushed, "smallCrusedOre");
		}

		toolMaterialDiamondStud = EnumHelper.addToolMaterial("DIAMOND_STUD", 3, 750, 7.0F, 2.0F, 5);
		toolMaterialDiamondStud.customCraftingMaterial = oreChunks;

		diaStudPick = new ItemDiamondStudPickaxe(toolMaterialDiamondStud);
		diaStudAxe = new ItemDiamondStudAxe(toolMaterialDiamondStud);
		diaStudShovel = new ItemDiamondStudShovel(toolMaterialDiamondStud);
		diaStudHoe = new ItemDiamondStudHoe(toolMaterialDiamondStud);
		GameRegistry.registerItem(diaStudPick, "diam_stud_pickaxe");
		GameRegistry.registerItem(diaStudAxe, "diam_stud_axe");
		GameRegistry.registerItem(diaStudHoe, "diam_stud_hoe");
		GameRegistry.registerItem(diaStudShovel, "diam_stud_shovel");

		itemAchievementIcons = new AchievementIcons();
		GameRegistry.registerItem(itemAchievementIcons, "itemAchievementIcons");
		
		ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.TIN.value);
		dummyOreTin = new BlockDummyOre(EnumOreType.TIN);
		GameRegistry.registerBlock(dummyOreTin, "dummyOreTin");
		OreDictionary.registerOre("oreTin", new ItemStack(dummyOreTin));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreTin));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.COPPER.value);
		dummyOreCopper = new BlockDummyOre(EnumOreType.COPPER);
		GameRegistry.registerBlock(dummyOreCopper, "dummyOreCopper");
		OreDictionary.registerOre("oreCopper", new ItemStack(dummyOreCopper));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreCopper));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.LEAD.value);
		dummyOreLead = new BlockDummyOre(EnumOreType.LEAD);
		GameRegistry.registerBlock(dummyOreLead, "dummyOreLead");
		OreDictionary.registerOre("oreLead", new ItemStack(dummyOreLead));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreLead));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.URANIUM.value);
		dummyOreUranium = new BlockDummyOre(EnumOreType.URANIUM);
		GameRegistry.registerBlock(dummyOreUranium, "dummyOreUranium");
		OreDictionary.registerOre("oreUranium", new ItemStack(dummyOreUranium));
		rawOreIn = new ItemStack(this.oreChunks, 9, EnumOreType.URANIUM.value);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreUranium));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.SILVER.value);
		dummyOreSilver = new BlockDummyOre(EnumOreType.SILVER);
		GameRegistry.registerBlock(dummyOreSilver, "dummyOreSilver");
		OreDictionary.registerOre("oreSilver", new ItemStack(dummyOreSilver));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreSilver));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.NICKEL.value);
		dummyOreNickel = new BlockDummyOre(EnumOreType.NICKEL);
		GameRegistry.registerBlock(dummyOreNickel, "dummyOreNickel");
		OreDictionary.registerOre("oreNickel", new ItemStack(dummyOreNickel));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreNickel));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.ALUMINUM.value);
		dummyOreAluminum = new BlockDummyOre(EnumOreType.ALUMINUM);
		GameRegistry.registerBlock(dummyOreAluminum, "dummyOreAluminum");
		OreDictionary.registerOre("oreAluminum", new ItemStack(dummyOreAluminum));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreAluminum));
		
		rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.OSMIUM.value);
		dummyOreOsmium = new BlockDummyOre(EnumOreType.OSMIUM);
		GameRegistry.registerBlock(dummyOreOsmium, "dummyOreOsmium");
		OreDictionary.registerOre("oreOsmium", new ItemStack(dummyOreOsmium));
		rawOreIn.stackSize = 9;
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreOsmium));

		oreCounter = new OreCountGenerator();
		//GameRegistry.registerWorldGenerator(oreCounter, Integer.MAX_VALUE);
		CogConfig.addCogModule("HarderVanillaOres.xml");
		CogConfig.addCogModule("HarderExtraOres.xml");
		CogConfig.addCogModule("HarderLimonite.xml");

		FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreIron));
		FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreGold));
		FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreDiamond));
		FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreRedstone));
		FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-isPartialBlock", new ItemStack(blockSluice));
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();

		//if(Loader.isModLoaded("IC2")){
		//	IC2Integration.constructBlocks();
		//}
		/*if(Loader.isModLoaded("RotaryCraft")) {
        		RotaryIntegration.registerAPIRecipes();
        	}
        	if(Loader.isModLoaded("ganyssurface")) {
        		GanyIntegration.registerAPIRecipes();
        	}*/

		ItemStack rawOreIn = new ItemStack(oreChunks);
		ItemStack nuggetOut = new ItemStack(oreChunks);
		rawOreIn.setItemDamage(EnumOreType.LIMONITE.value);
		nuggetOut.setItemDamage(EnumOreType.IRON.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);

		rawOreIn = new ItemStack(oreChunks);
		nuggetOut = new ItemStack(nuggetIron);
		rawOreIn.setItemDamage(EnumOreType.IRON.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
		rawOreIn = new ItemStack(oreChunks);
		nuggetOut = new ItemStack(Items.gold_nugget);
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);

		rawOreIn = new ItemStack(smallDusts);
		nuggetOut = new ItemStack(nuggetIron);
		rawOreIn.setItemDamage(EnumOreType.IRON.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
		rawOreIn = new ItemStack(smallDusts);
		nuggetOut = new ItemStack(Items.gold_nugget);
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);

		rawOreIn = new ItemStack(largeDusts);
		nuggetOut = new ItemStack(Items.iron_ingot);
		rawOreIn.setItemDamage(EnumOreType.IRON.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
		rawOreIn = new ItemStack(largeDusts);
		nuggetOut = new ItemStack(Items.gold_ingot);
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);

		rawOreIn = new ItemStack(oreRedstone);
		nuggetOut = new ItemStack(Items.redstone);
		GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.7f);

		OreDictionary.registerOre("dustIron", new ItemStack(largeDusts, 1, EnumOreType.IRON.value));
		OreDictionary.registerOre("dustGold", new ItemStack(largeDusts, 1, EnumOreType.GOLD.value));
		OreDictionary.registerOre("dustFlour", new ItemStack(largeDusts, 1, EnumOreType.FLOUR.value));
		OreDictionary.registerOre("dustTinyIron", new ItemStack(smallDusts, 1, EnumOreType.IRON.value));
		OreDictionary.registerOre("dustTinyGold", new ItemStack(smallDusts, 1, EnumOreType.GOLD.value));
		OreDictionary.registerOre("dustTinyFlour", new ItemStack(smallDusts, 1, EnumOreType.FLOUR.value));
		OreDictionary.registerOre("dustTinySugar", new ItemStack(smallDusts, 1, EnumOreType.SUGAR.value));
		OreDictionary.registerOre("dustTinyTin", new ItemStack(smallDusts, 1, EnumOreType.TIN.value));
		OreDictionary.registerOre("dustTinyCopper", new ItemStack(smallDusts, 1, EnumOreType.COPPER.value));
		OreDictionary.registerOre("dustTinyLead", new ItemStack(smallDusts, 1, EnumOreType.LEAD.value));
		OreDictionary.registerOre("dustTinySilver", new ItemStack(smallDusts, 1, EnumOreType.SILVER.value));
		OreDictionary.registerOre("dustTinyNickel", new ItemStack(smallDusts, 1, EnumOreType.NICKEL.value));
		OreDictionary.registerOre("dustTinyAluminum", new ItemStack(smallDusts, 1, EnumOreType.ALUMINUM.value));
		OreDictionary.registerOre("dustTinyOsmium", new ItemStack(smallDusts, 1, EnumOreType.OSMIUM.value));

		OreDictionary.registerOre("nuggetIron", new ItemStack(nuggetIron));

		OreDictionary.registerOre("rawOreChunkLimonite", new ItemStack(oreChunks, 1, EnumOreType.LIMONITE.value));
		OreDictionary.registerOre("rawOreChunkIron", new ItemStack(oreChunks, 1, EnumOreType.IRON.value));
		OreDictionary.registerOre("rawOreChunkGold", new ItemStack(oreChunks, 1, EnumOreType.GOLD.value));
		OreDictionary.registerOre("rawOreChunkDiamond", new ItemStack(oreChunks, 1, EnumOreType.DIAMOND.value));
		OreDictionary.registerOre("rawOreChunkTin", new ItemStack(oreChunks, 1, EnumOreType.TIN.value));
		OreDictionary.registerOre("rawOreChunkCopper", new ItemStack(oreChunks, 1, EnumOreType.COPPER.value));
		OreDictionary.registerOre("rawOreChunkLead", new ItemStack(oreChunks, 1, EnumOreType.LEAD.value));
		OreDictionary.registerOre("rawOreChunkUranium", new ItemStack(oreChunks, 1, EnumOreType.URANIUM.value));
		OreDictionary.registerOre("rawOreChunkSilver", new ItemStack(oreChunks, 1, EnumOreType.SILVER.value));
		OreDictionary.registerOre("rawOreChunkNickel", new ItemStack(oreChunks, 1, EnumOreType.NICKEL.value));
		OreDictionary.registerOre("rawOreChunkAluminum", new ItemStack(oreChunks, 1, EnumOreType.ALUMINUM.value));
		OreDictionary.registerOre("rawOreChunkOsmium", new ItemStack(oreChunks, 1, EnumOreType.OSMIUM.value));

		OreDictionary.registerOre("oreIronHard", new ItemStack(oreIron, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreGoldHard", new ItemStack(oreGold, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreDiamondHard", new ItemStack(oreDiamond, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreTinHard", new ItemStack(oreTin, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreCopperHard", new ItemStack(oreCopper, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreLeadHard", new ItemStack(oreLead, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreUraniumHard", new ItemStack(oreUranium, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreSilverHard", new ItemStack(oreSilver, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreNickelHard", new ItemStack(oreNickel, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreAluminumHard", new ItemStack(oreAluminum, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreOsmium", new ItemStack(oreOsmium, 1, OreDictionary.WILDCARD_VALUE));

		rawOreIn = new ItemStack(oreChunks);
		rawOreIn.setItemDamage(EnumOreType.DIAMOND.value);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.diamond),
				rawOreIn, rawOreIn, rawOreIn,
				rawOreIn, rawOreIn, rawOreIn,
				rawOreIn, rawOreIn, rawOreIn);
		String oreIn = "nuggetIron";
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.iron_ingot),
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nuggetIron,9),Items.iron_ingot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.iron_bars),"iii","iii",'i',oreIn));
		oreIn = "dustTinyIron";
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,EnumOreType.IRON.value),
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn));
		oreIn = "dustTinyGold";
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,EnumOreType.GOLD.value),
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn));
		oreIn = "dustTinyFlour";
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,EnumOreType.FLOUR.value),
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn));
		oreIn = "dustTinySugar";
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.sugar),
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn,
				oreIn, oreIn, oreIn));

		oreIn = "slabWood";
		if(sluiceEnabled)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSluice, 2),"sss","ppp",'s',Items.stick,'p',oreIn));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMill,9), true, new Object[] {"SSS","SWS","SSS", 'S', "stone", 'W', "logWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSifter), true, new Object[] {"PBP","PbP", 'b', Items.bucket, 'P', "plankWood", 'B', Blocks.iron_bars}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockVane, 2), true, new Object[] {"SW","SW", "SW", 'S', Items.stick, 'W', Blocks.wool}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockAxle, 2), true, new Object[] {"WWW", 'W', "logWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockProcessor), "sps", "i i", "iii",'i',Items.iron_ingot,'s',"stone",'p',Blocks.piston));
		
		rawOreIn = new ItemStack(Items.redstone, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.redstone_block));
		rawOreIn = new ItemStack(Items.wheat, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.hay_block));
		rawOreIn = new ItemStack(Items.snowball, 4);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.snow));
		rawOreIn = new ItemStack(Items.iron_ingot, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.iron_block));
		rawOreIn = new ItemStack(Items.gold_ingot, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.gold_block));
		rawOreIn = new ItemStack(Items.clay_ball, 4);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.clay));
		rawOreIn = new ItemStack(Items.quartz, 4);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.quartz_block));
		rawOreIn = new ItemStack(Blocks.sand, 4);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.sandstone));
		rawOreIn = new ItemStack(Items.coal, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.coal_block));
		rawOreIn = new ItemStack(Blocks.ice, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.packed_ice));
		rawOreIn = new ItemStack(Blocks.snow, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.ice));
		rawOreIn = new ItemStack(Items.dye, 9, 4);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.lapis_block));
		rawOreIn = new ItemStack(Items.melon, 9);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(Blocks.melon_block));
		
		addDyeRecipes();
		
		oreIn = "ingotIron";
		rawOreIn = new ItemStack(oreChunks);
		rawOreIn.setItemDamage(EnumOreType.DIAMOND.value);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudPick), true, new Object[] {"dId", " s ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudAxe), true, new Object[] {"dI ", "Is ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudShovel), true, new Object[] {" d ", " I ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudHoe), true, new Object[] {"dI ", " s ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));

		/*rawOreIn = new ItemStack(largeDusts,1,3);
            GameRegistry.addShapedRecipe(new ItemStack(Items.cookie, 8), "wcw", 'w', rawOreIn, 'c', new ItemStack(Items.dye, 1, 3));
            GameRegistry.addShapedRecipe(new ItemStack(Items.bread), "www", 'w', rawOreIn);
            GameRegistry.addShapedRecipe(new ItemStack(Items.cake), "mmm", "ses", "www", 'w', rawOreIn, 's', Items.sugar, 'e', Items.egg, 'm', Items.milk_bucket);*/

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		//rawOreIn = new ItemStack(smallDusts, 8);
		nuggetOut = new ItemStack(largeDusts, 1);
		//rawOreIn.setItemDamage(EnumOreType.IRON.value);
		nuggetOut.setItemDamage(EnumOreType.IRON.value);
		HardLibAPI.recipeManager.addSiftRecipe("dustTinyIron", 8, nuggetOut);
		//rawOreIn.stackSize = 8;
		//rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		nuggetOut.setItemDamage(EnumOreType.GOLD.value);
		HardLibAPI.recipeManager.addSiftRecipe("dustTinyGold", 8, nuggetOut);
		//rawOreIn.stackSize = 8;
		//rawOreIn.setItemDamage(EnumOreType.FLOUR.value);
		nuggetOut.setItemDamage(EnumOreType.FLOUR.value);
		HardLibAPI.recipeManager.addSiftRecipe("dustTinyFlour", 8, nuggetOut);
		//rawOreIn.setItemDamage(EnumOreType.SUGAR.value);
		nuggetOut = new ItemStack(Items.sugar);
		HardLibAPI.recipeManager.addSiftRecipe("dustTinySugar", 8, nuggetOut);

		rawOreIn = new ItemStack(oreChunks);
		nuggetOut = new ItemStack(smallDusts, 2);
		rawOreIn.setItemDamage(EnumOreType.IRON.value);
		nuggetOut.setItemDamage(EnumOreType.IRON.value);
		HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		nuggetOut.setItemDamage(EnumOreType.GOLD.value);
		HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		rawOreIn.stackSize = 9;
		rawOreIn.setItemDamage(EnumOreType.IRON.value);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreIron));
		rawOreIn.setItemDamage(EnumOreType.GOLD.value);
		HardLibAPI.recipeManager.addProcessorRecipe(rawOreIn, new ItemStack(dummyOreGold));
		
		RecipesUtil.RemoveRecipe(Items.blaze_powder, 2, 0, "Hard Ores");
		rawOreIn = new ItemStack(Items.blaze_rod);
		nuggetOut = new ItemStack(Items.blaze_powder,2);
		HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);

		//sugar ore to orange dye!?
		/*nuggetOut = new ItemStack(Items.dye);
            nuggetOut.setItemDamage(14);
            nuggetOut.stackSize = 2;Ore is over specified
            rawOreIn = new ItemStack(oreChunks);
            rawOreIn.setItemDamage(4);
            HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);*/

		config.addCustomCategoryComment("MILLING", "Enable (hard mode) these to remove vanilla recipes for items and instead require the millstone. In general,\neasy means the millstone doubles resources, while hard is near-vanilla.");
		boolean hardOption = config.getBoolean("flourRequireMilling", "MILLING", false, "");

		oreIn = "dustFlour";
		if(hardOption) {
			RecipesUtil.RemoveRecipe(Items.bread, 1, 0, "Hard Ores");
			RecipesUtil.RemoveRecipe(Items.cookie, 8, 0, "Hard Ores");
			RecipesUtil.RemoveRecipe(Items.cake, 1, 0, "Hard Ores");
			
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.bread, 3), "www", 'w', oreIn)); //works out to 1:1 vanilla

			rawOreIn = new ItemStack(Items.wheat);
			nuggetOut = new ItemStack(smallDusts, 4); //hard: wheat is ground to "4/9th flour"
			nuggetOut.setItemDamage(EnumOreType.FLOUR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);

			rawOreIn = new ItemStack(Items.wheat_seeds); //hard: seeds are ground to "1/9ths flour"
			nuggetOut = new ItemStack(smallDusts, 1);
			nuggetOut.setItemDamage(EnumOreType.FLOUR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		else {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.bread), "www", 'w', oreIn));

			rawOreIn = new ItemStack(Items.wheat);
			nuggetOut = new ItemStack(smallDusts, 18); //easy: wheat is ground to "2 flour"
			nuggetOut.setItemDamage(EnumOreType.FLOUR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);

			rawOreIn = new ItemStack(Items.wheat_seeds); //easy: seeds are ground to "2/9ths flour"
			nuggetOut = new ItemStack(smallDusts, 2);
			nuggetOut.setItemDamage(EnumOreType.FLOUR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.cookie, 8), "wcw", 'w', oreIn, 'c', new ItemStack(Items.dye, 1, 3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.cake), "mmm", "ses", "www", 'w', oreIn, 's', Items.sugar, 'e', Items.egg, 'm', Items.milk_bucket));
		
		hardOption = config.getBoolean("sugarRequireMilling", "MILLING", false, "If enabled, sugarcane cannot be crafted into sugar");
		int sugarMulti = config.getInt("sugarMillingMultiplier", "MILLING", 6, 1, 12, "Sugar is a easy-to-get resource and rare-to-use, so it may be desirable to reduce the production.\nOutput of milling sugar (in tiny piles) is this value in hard-milling and 2x this value in\neasy-milling.\nVanilla Equivalence is 9.");

		if(hardOption) {
			RecipesUtil.RemoveRecipe(Items.sugar, 1, 0, "Hard Ores");
			rawOreIn = new ItemStack(Items.reeds); //hard
			nuggetOut = new ItemStack(smallDusts, sugarMulti);
			nuggetOut.setItemDamage(EnumOreType.SUGAR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		else {
			rawOreIn = new ItemStack(Items.reeds); //easy
			nuggetOut = new ItemStack(smallDusts, 2*sugarMulti);
			nuggetOut.setItemDamage(EnumOreType.SUGAR.value);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		hardOption = config.getBoolean("bonemealRequireMilling", "MILLING", false, "");
		//config.get("bonemealMillingMultiplier", "MILLING", 6).comment = "Output of milling sugar (in tiny piles) is this value in hard-milling and 2x this value in easy-milling.  Max 9, Min 1, Default 6.";
		//sugarMulti = Math.min(Math.max(config.get("MILLING", "bonemealMillingMultiplier", 6).getInt(), 1), 9);
		if(hardOption) {
			RecipesUtil.RemoveRecipe(Items.dye, 3, 15, "Hard Ores");
			rawOreIn = new ItemStack(Items.bone); //hard
			nuggetOut = new ItemStack(Items.dye, 2, 15);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		else {
			rawOreIn = new ItemStack(Items.bone); //easy
			nuggetOut = new ItemStack(Items.dye, 4, 15);
			HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
		}
		config.save();

		/*Fake sift recipe for bonemeal, so it doesn't clog*/
		rawOreIn = new ItemStack(Items.dye, 1, 15);
		HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, rawOreIn, false);

		FMLInterModComms.sendMessage("Waila", "register", "com.draco18s.ores.recipes.WailaIntegration.callbackRegister");
	}

	private void addDyeRecipes() {
		ItemStack flower = new ItemStack(blockOreFlowers, 16, 0);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 12));
		flower.setItemDamage(1);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 10));
		flower.setItemDamage(2);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 10));
		flower.setItemDamage(3);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 1));
		flower.setItemDamage(4);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 11));
		flower.setItemDamage(5);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 5));
		flower.setItemDamage(6);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 10));
		flower.setItemDamage(7);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 13));
		flower.setItemDamage(8);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 7));

		flower = new ItemStack(blockOreFlowers2, 16, 0);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 11));
		flower.setItemDamage(1);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 5));
		flower.setItemDamage(2);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 13));
		flower.setItemDamage(4);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 9));
		flower.setItemDamage(5);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 2));
		flower.setItemDamage(6);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 14));
		flower.setItemDamage(7);
		HardLibAPI.recipeManager.addProcessorRecipe(flower, new ItemStack(Items.dye, 1, 13));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		StatsAchievements.addCoreAchievements();
		OreFlowerData data;
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.IRON.value-8)&7, 7);
		HardLibAPI.oreManager.addOreFlowerData(oreIron,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.GOLD.value-8)&7, 6);
		HardLibAPI.oreManager.addOreFlowerData(oreGold,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.DIAMOND.value-8)&7, 5);
		HardLibAPI.oreManager.addOreFlowerData(oreDiamond,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.REDSTONE.value-8)&7, 999);
		HardLibAPI.oreManager.addOreFlowerData(oreRedstone,0, data);
		HardLibAPI.oreManager.addOreFlowerData(oreRedstone,1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.TIN.value-8)&7, 6);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreTin,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.COPPER.value-8)&7, 6);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreCopper,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.LEAD.value-8)&7, 5);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreLead,-1, data);
		data = new OreFlowerData(blockOreFlowers, (EnumOreType.URANIUM.value-8)&7, 4);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreUranium,-1, data);
		data = new OreFlowerData(blockOreFlowers2, (EnumOreType.SILVER.value-8)&7, 5);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreSilver,-1, data);
		data = new OreFlowerData(blockOreFlowers2, (EnumOreType.NICKEL.value-8)&7, 5);
		HardLibAPI.oreManager.addOreFlowerData(OresBase.oreNickel,-1, data);
		data = new OreFlowerData(blockOreFlowers2, (EnumOreType.ALUMINUM.value-8)&7, 5);
		HardLibAPI.oreManager.addOreFlowerData(oreAluminum, -1, data);
		
		if(Loader.isModLoaded("IC2")){
			StatsAchievements.addIC2Achievements();
			IC2Integration.registerAPIRecipes();
		}
		if(Loader.isModLoaded("ThermalExpansion")) {
			ThermalExpansionHelper.addTERecipes();
		}
		if(Loader.isModLoaded("Metallurgy")) {
			MetallurgyIntegration.addMetallurgyRecipes();
		}
		if(Loader.isModLoaded("Mekanism")) {
			MekanismIntegration.addMekanismRecipies();
		}
		if(Loader.isModLoaded("RotaryCraft")) {
			RotaryIntegration.registerAPIRecipes();
		}
		//if(Loader.isModLoaded("ReactorCraft")) {
			//RotaryIntegration.registerAPIRecipes();
		//}
		config.getInt("OreExists...", "ORES", 1, 0, 2, "These settings should be auto-detected during worldgen and act as an override.\n0 will prevent flowers, 2 will enforce (set automatically), 1 is default.");
		
		ArrayList<ItemStack> oreDictReq;
		int addedOres = 0;
		oreDictReq = OreDictionary.getOres("oreTin");
		if(config.getInt("OreExistsTin", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsTin", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.TIN.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addTin();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreCopper");
		if(config.getInt("OreExistsCopper", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsCopper", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.COPPER.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addCopper();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreLead");
		if(config.getInt("OreExistsLead", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsLead", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.LEAD.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addLead();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreUranium");
		if(config.getInt("OreExistsUranium", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsUranium", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers, (EnumOreType.URANIUM.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addUranium();
			addedOres++;
		}
		/*oreDictReq = OreDictionary.getOres("orePitchblende");
		if(oreDictReq.size() > 0) {
			data = new OreFlowerData(blockOreFlowers2, (EnumOreType.URANIUM.value-8)&7, 5);
			for(ItemStack oreDictBlock : oreDictReq) {
				HardLibAPI.recipeManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
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
			addSilver();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreNickel");
		if(config.getInt("OreExistsNickel", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsNickel", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers2, (EnumOreType.NICKEL.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addNickel();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreAluminum");
		if(config.getInt("OreExistsAluminum", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsAluminum", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers2, (EnumOreType.ALUMINUM.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addAluminum();
			addedOres++;
		}
		oreDictReq = OreDictionary.getOres("oreOsmium");
		if(config.getInt("OreExistsOsmium", "ORES", 1, 0, 2, "") == 2 || oreDictReq.size() > 0) {
			if(config.getInt("OreExistsOsmium", "ORES", 1, 0, 2, "") != 0) {
				data = new OreFlowerData(blockOreFlowers3, (EnumOreType.OSMIUM.value-8)&7, 5);
				for(ItemStack oreDictBlock : oreDictReq) {
					HardLibAPI.oreManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
				}
			}
			addOsmium();
			//addedOres++;
		}
		if(addedOres > 0) {
			HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
			HardLibAPI.recipeManager.addSluiceRecipe(null);
			if(addedOres > 3) {
				HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
				HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
				if(addedOres > 5) {
					HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
					HardLibAPI.recipeManager.addSluiceRecipe(null);
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
				HardLibAPI.recipeManager.addOreFlowerData(Block.getBlockFromItem(oreDictBlock.getItem()), oreDictBlock.getItemDamage(), data);
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
		

		if(config.get("SLUICE", "canFindIron", true).getBoolean()) {
			HardLibAPI.recipeManager.addSluiceRecipe(oreIron);
			HardLibAPI.recipeManager.addSluiceRecipe(oreIron);
			HardLibAPI.recipeManager.addSluiceRecipe(oreIron);
		}
		if(config.get("SLUICE", "canFindGold", true).getBoolean()) {
			HardLibAPI.recipeManager.addSluiceRecipe(oreGold);
			HardLibAPI.recipeManager.addSluiceRecipe(oreGold);
			HardLibAPI.recipeManager.addSluiceRecipe(oreGold);
		}
		if(config.get("SLUICE", "canFindDiamond", false).getBoolean()) {
			HardLibAPI.recipeManager.addSluiceRecipe(oreDiamond);
		}
		if(config.get("SLUICE", "canFindRedstone", true).getBoolean()) {
			HardLibAPI.recipeManager.addSluiceRecipe(oreRedstone);
			HardLibAPI.recipeManager.addSluiceRecipe(oreRedstone);
		}
		HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
		HardLibAPI.recipeManager.addSluiceRecipe(null);

		boolean p = config.getBoolean("useDioriteStoneTools", "GENERAL", true, "If Gany's Surface, Et Futurum, or Chisel is installed and this is true, cobblestone cannot be used to create stone tools,\ninstead diorite is used. This prolongs the life of wood tools so it isn't \"make a wood pickaxe to\nmine 3 stone and upgrade.\"");
		if(Loader.isModLoaded("ganyssurface") && p) {
			//System.out.println("Registered only diorite tools");
			GanyIntegration.registerAPIRecipes();
			StatsAchievements.addGanyAchievements();
		}
		if(Loader.isModLoaded("chisel") && p) {
			//System.out.println("Registered only diorite tools");
			ChiselIntegration.registerAPIRecipes();
			StatsAchievements.addChiselAchievements();
		}
		if(Loader.isModLoaded("etfuturum") && p) {
			//System.out.println("Registered only diorite tools");
			EtFuturumIntegration.registerAPIRecipes();
			StatsAchievements.addEtFuturumAchievements();
		}
		if(!HardLibAPI.recipeManager.isSluiceSetFull()) {
			HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
			HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
			HardLibAPI.recipeManager.addSluiceRecipe(null);
		}
		proxy.registerEventHandlers();
		CogConfig.unpackDefaultConfig();
		
		Property verConf = config.get("configVersion", "GENERAL", "1.0", "Do not touch"); 
		boolean b = HashUtils.versionCompare(verConf.getString(),"3.0") < 0;
		CogConfig.unpackConfigs(b);
		verConf.set("4.10");
		config.save();
	}

	@EventHandler
	public void imcCallback(FMLInterModComms.IMCEvent event) {
		for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
			if (imcMessage.key.equalsIgnoreCase("RR-special-stone-update-gany")) {
				if(imcMessage.isItemStackMessage()) {
					GanyIntegration.setAlternateBlock(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
				}
			}
			else if (imcMessage.key.equalsIgnoreCase("RR-special-stone-update-chisel")) {
				if(imcMessage.isItemStackMessage()) {
					ChiselIntegration.setAlternateBlock(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
				}
			}
			else if (imcMessage.key.equalsIgnoreCase("RR-special-stone-update-futurum")) {
				if(imcMessage.isItemStackMessage()) {
					EtFuturumIntegration.setAlternateBlock(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
				}
			}
		}
	}

	private int extraOres = 0;
	private void addTin() {
		if((extraOres&1) == 0) {
			if(config.get("SLUICE", "canFindTin", true).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreTin);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreTin);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreTin);
			}
			String oreIn = "dustTinyTin";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.TIN.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustTin");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetTin");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0).copy();
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 1;
		}
	}

	private void addCopper() {
		if((extraOres&2) == 0) {
			if(config.get("SLUICE", "canFindCopper", true).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreCopper);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreCopper);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreCopper);
			}
			String oreIn = "dustTinyCopper";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.COPPER.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustCopper");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetCopper");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0).copy();
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 2;
		}
	}

	private void addLead() {
		if((extraOres&4) == 0) {
			if(config.get("SLUICE", "canFindLead", true).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreLead);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreLead);
			}
			String oreIn = "dustTinyLead";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.LEAD.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustLead");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetLead");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0);
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 4;
		}
	}

	private void addUranium() {
		if((extraOres&8) == 0) {
			if(config.get("SLUICE", "canFindUranium", false).getBoolean() && (extraOres&8) == 0) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreUranium);
			}
			extraOres |= 8;
		}
	}

	private void addSilver() {
		if((extraOres&16) == 0) {
			if(config.get("SLUICE", "canFindSilver", true).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreSilver);
			}
			String oreIn = "dustTinySilver";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.SILVER.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustSilver");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetSilver");
			ItemStack instk;
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				instk = tstk.get(0);
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 16;
		}
	}

	private void addNickel() {
		if((extraOres&32) == 0) {
			if(config.get("SLUICE", "canFindNickel", true).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreNickel);
			}
			String oreIn = "dustTinyNickel";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.NICKEL.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustNickel");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetNickel");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0);
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 32;
		}
	}
	
	private void addAluminum() {
		if((extraOres&64) == 0) {
			if(config.get("SLUICE", "canFindAluminum", false).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreAluminum);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreAluminum);
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreAluminum);
			}
			String oreIn = "dustTinyAluminum";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.ALUMINUM.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustAluminum");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetAluminum");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0);
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 64;
		}
	}
	
	private void addOsmium() {
		if((extraOres&128) == 0) {
			/*if(config.get("SLUICE", "canFindOsmium", false).getBoolean()) {
				HardLibAPI.recipeManager.addSluiceRecipe(OresBase.oreOsmium);
			}*/
			String oreIn = "dustTinyOsmium";
			ItemStack rawOreIn = new ItemStack(this.oreChunks, 1, EnumOreType.OSMIUM.value);
			ArrayList<ItemStack> stk = OreDictionary.getOres("dustOsmium");
			ArrayList<ItemStack> tstk = OreDictionary.getOres(oreIn);
			ArrayList<ItemStack> nstk = OreDictionary.getOres("nuggetOsmium");
			if(stk.size() > 0 && tstk.size() > 0) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(stk.get(0),
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn,
						oreIn, oreIn, oreIn));
				ItemStack instk = tstk.get(0);
				if(nstk.size() > 0) {
					GameRegistry.addSmelting(instk, nstk.get(0), 0.1f);
					GameRegistry.addSmelting(rawOreIn, nstk.get(0), 0.1f);
				}
				instk.stackSize = 2;
				HardLibAPI.recipeManager.addMillRecipe(rawOreIn, instk);
				HardLibAPI.recipeManager.addSiftRecipe(oreIn, 8, stk.get(0));
			}
			extraOres |= 128;
		}
	}
	
	public void addArbitraryOre(Block ore) {
		Property p;
		if(ore == oreTin) {
			addTin();
			p = config.get("ORES", "OreExistsTin", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreCopper) {
			addCopper();
			p = config.get("ORES", "OreExistsCopper", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreLead) {
			addLead();
			p = config.get("ORES", "OreExistsLead", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreUranium) {
			addUranium();
			p = config.get("ORES", "OreExistsUranium", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreSilver) {
			addSilver();
			p = config.get("ORES", "OreExistsSilver", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreNickel) {
			addNickel();
			p = config.get("ORES", "OreExistsNickel", 1, "", 0, 2);
			if(p.getInt() == 1)
				p.set(2);
		}
		if(ore == oreAluminum) {
			addAluminum();
			p = config.get("ORES", "OreExistsAluminum", 1, "", 0, 2);
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