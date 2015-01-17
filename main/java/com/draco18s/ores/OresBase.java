package com.draco18s.ores;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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
import cpw.mods.fml.common.registry.GameData;

import com.draco18s.hardlib.CogConfig;
import com.draco18s.hardlib.RecipesUtil;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.internal.OreFlowerData;
import com.draco18s.hazards.block.BlockVolatileGas;
import com.draco18s.ores.block.*;
import com.draco18s.ores.block.ores.BlockLimonite;
import com.draco18s.ores.block.ores.BlockOreDiamond;
import com.draco18s.ores.block.ores.BlockOreGold;
import com.draco18s.ores.block.ores.BlockOreIron;
import com.draco18s.ores.block.ores.BlockOreRedstone;
import com.draco18s.ores.entities.*;
import com.draco18s.ores.item.*;
import com.draco18s.ores.recipes.*;
import com.draco18s.ores.util.OreCountGenerator;

@Mod(modid="HarderOres", name="HarderOres", version="0.2.6", dependencies = "required-after:HardLib")
public class OresBase {
		public static Block oreLimonite;
		public static Block oreIron;
		public static Block oreGold;
		public static Block oreDiamond;
		public static Block oreRedstone;
		public static Block blockSifter;
		public static Block blockmill;
		public static Block blockvane;
		public static Block blockaxel;
		public static Block blocksluice;
		public static Block blockOreFlowers;
		
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
		/*diorite tools
		public static Item dioStudPick;
		public static Item dioStudAxe;
		public static Item dioStudShovel;
		public static Item dioStudHoe;*/
		public static Material sluiceBox = (new Material(MapColor.woodColor));
		public static Material gas = (new MaterialLiquid(MapColor.airColor));
		
		public static ToolMaterial materialDiamondStud;

        @Instance(value = "HarderOres")
        public static OresBase instance;
        
        public static Configuration config;
        public static boolean sluiceVersion;
       
        @SidedProxy(clientSide="com.draco18s.ores.client.ClientProxy", serverSide="com.draco18s.ores.CommonProxy")
        public static CommonProxy proxy;
        public static Random rand = new Random();
        //public static Random worldRand;
		public static OreCountGenerator oreCounter;
       
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
         *  [/] indicator plants
         *      http://en.wikipedia.org/wiki/Indicator_plant
         *      + iron: Diodella teres (wikipedia)
         *      + gold: Equisetum arvense (wikipedia)
         *      + diamond: Vallozia candida http://sp0.fotolog.com/photo/0/26/47/nature/1159748636_f.jpg
         *      + redstone: flame lily
         *      - copper: Haumaniastrum robertii http://www.kew.org/herbarium/keys/lamiales/key/Interactive%20key%20to%20the%20genera%20of%20Lamiaceae/Media/Html/images/Haumaniastrum_P._A._Duvign.__Plancke/Haumaniastrum%20flowers%20copy.jpg
         *      - tin: tansy
         *      - lead: leadplant
         *      - uranium: tufted evening-primrose
         *  [x] config options:
         *      + sluice version
         *      + hard flour grinding (remove wheat recipes)
         *      + hard bonemeal grinding (remove vanilla bonemeal recipe)
         */
        
        @EventHandler
        public void preInit(FMLPreInitializationEvent event) {
        	HardLibAPI.recipeManager = new RecipeManager();
        	/*if(!Loader.isModLoaded("CustomOreGen")) {
        		throw new RuntimeException("Harder Ores requires Custom Ore Generation to work properly");
        	}*/
        	
        	config = new Configuration(event.getSuggestedConfigurationFile());
        	
        	oreLimonite = new BlockLimonite();
        	GameRegistry.registerBlock(oreLimonite, "ore_limonite");
        	oreIron = new BlockOreIron();
        	GameRegistry.registerBlock(oreIron, "ore_iron");
        	oreGold = new BlockOreGold();
        	GameRegistry.registerBlock(oreGold, "ore_gold");
        	oreDiamond = new BlockOreDiamond();
        	GameRegistry.registerBlock(oreDiamond, "ore_diamond");
        	oreRedstone = new BlockOreRedstone();
        	GameRegistry.registerBlock(oreRedstone, "ore_redstone");
        	blockOreFlowers = new BlockOreFlower();
        	GameRegistry.registerBlock(blockOreFlowers, ItemBlockOreFlower.class, "ore_flowers");
        	blockSifter = new BlockSifter();
        	GameRegistry.registerBlock(blockSifter, "machine_sifter");
        	blockmill = new BlockMillstone();
        	GameRegistry.registerBlock(blockmill, "machine_millstone");
        	blockvane = new BlockWindvane();
        	GameRegistry.registerBlock(blockvane, "machine_windvane");
        	blockaxel = new BlockAxel();
        	GameRegistry.registerBlock(blockaxel, "machine_axel");
        	config.addCustomCategoryComment("SLUICE", "Various settings for the sluice.");
        	config.get("SLUICE", "canFind...", false, "These setting indicate if the sluice can filter for the given mineral. Relative weights are handled\ninternally. Even if all settings are false, gravel can still be filtered to flint.");
        	Property p = config.get("SLUICE", "useContainerSluice", false);
        	sluiceVersion = p.getBoolean();
        	p.comment = "The Container-Sluice holds items, has a GUI, and supports comparator output. Non-container-sluice\nuses faux water flow mechanics, item entities, does not store items, and does not support the\ncomparator.";
        	if(sluiceVersion) {
        		blocksluice = new BlockSluice();
        		GameRegistry.registerBlock(blocksluice, "machine_sluice");
        	}
        	else {
        		blocksluice = new BlockSluiceBottom();
        		GameRegistry.registerBlock(blocksluice, "machine_sluice");
        	}
        	
        	/*TileEntitySluiceBottom.findIron = TileEntitySluice.findIron = config.get("SLUICE", "canFindIron", true).getBoolean();
        	TileEntitySluiceBottom.findGold = TileEntitySluice.findGold = config.get("SLUICE", "canFindGold", true).getBoolean();
        	TileEntitySluiceBottom.findDiam = TileEntitySluice.findDiam = config.get("SLUICE", "canFindDiamond", false).getBoolean();
        	TileEntitySluiceBottom.findReds = TileEntitySluice.findReds = config.get("SLUICE", "canFindRedstone", true).getBoolean();*/
    		config.save();

            GameRegistry.registerTileEntity(TileEntitySifter.class, "ores.sifter");
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
        	
        	materialDiamondStud = EnumHelper.addToolMaterial("DIAMOND_STUD", 3, 750, 7.0F, 2.0F, 5);
        	materialDiamondStud.customCraftingMaterial = oreChunks;
        	
			diaStudPick = new ItemDiamondStudPickaxe(materialDiamondStud);
			diaStudAxe = new ItemDiamondStudAxe(materialDiamondStud);
			diaStudShovel = new ItemDiamondStudShovel(materialDiamondStud);
			diaStudHoe = new ItemDiamondStudHoe(materialDiamondStud);
        	GameRegistry.registerItem(diaStudPick, "diam_stud_pickaxe");
        	GameRegistry.registerItem(diaStudAxe, "diam_stud_axe");
        	GameRegistry.registerItem(diaStudHoe, "diam_stud_hoe");
        	GameRegistry.registerItem(diaStudShovel, "diam_stud_shovel");

        	oreCounter = new OreCountGenerator();
        	//GameRegistry.registerWorldGenerator(oreCounter, Integer.MAX_VALUE);
        	CogConfig.addCogModule("HarderOres.xml");
        	CogConfig.addCogModule("HarderIC2.xml");
        	
        	FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreIron));
        	FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreGold));
        	FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreDiamond));
        	FMLInterModComms.sendMessage("HarderUnderground", "RR-underground-support", new ItemStack(oreRedstone));
        }
       
        @EventHandler
        public void load(FMLInitializationEvent event) {
            proxy.registerRenderers();
            
            if(Loader.isModLoaded("IC2")){
        		IC2Integration.constructBlocks();
        	}
        	/*if(Loader.isModLoaded("RotaryCraft")) {
        		RotaryIntegration.registerAPIRecipes();
        	}
        	if(Loader.isModLoaded("ganyssurface")) {
        		GanyIntegration.registerAPIRecipes();
        	}*/
            
            ItemStack rawOreIn = new ItemStack(oreChunks);
            ItemStack nuggetOut = new ItemStack(oreChunks);
            rawOreIn.setItemDamage(4);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);

            rawOreIn = new ItemStack(oreChunks);
            nuggetOut = new ItemStack(nuggetIron);
            rawOreIn.setItemDamage(0);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            rawOreIn = new ItemStack(oreChunks);
            nuggetOut = new ItemStack(Items.gold_nugget);
            rawOreIn.setItemDamage(1);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            
            rawOreIn = new ItemStack(smallDusts);
            nuggetOut = new ItemStack(nuggetIron);
            rawOreIn.setItemDamage(0);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            rawOreIn = new ItemStack(smallDusts);
            nuggetOut = new ItemStack(Items.gold_nugget);
            rawOreIn.setItemDamage(1);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            
            rawOreIn = new ItemStack(largeDusts);
            nuggetOut = new ItemStack(Items.iron_ingot);
            rawOreIn.setItemDamage(0);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            rawOreIn = new ItemStack(largeDusts);
            nuggetOut = new ItemStack(Items.gold_ingot);
            rawOreIn.setItemDamage(1);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.1f);
            
            rawOreIn = new ItemStack(oreRedstone);
            nuggetOut = new ItemStack(Items.redstone);
            GameRegistry.addSmelting(rawOreIn, nuggetOut, 0.7f);
            
			OreDictionary.registerOre("dustIron", new ItemStack(largeDusts, 1, 0));
			OreDictionary.registerOre("dustGold", new ItemStack(largeDusts, 1, 1));
			OreDictionary.registerOre("dustFlour", new ItemStack(largeDusts, 1, 3));
			OreDictionary.registerOre("dustTinyIron", new ItemStack(smallDusts, 1, 0));
			OreDictionary.registerOre("dustTinyGold", new ItemStack(smallDusts, 1, 1));
			OreDictionary.registerOre("dustTinyFlour", new ItemStack(smallDusts, 1, 3));
			OreDictionary.registerOre("dustTinySugar", new ItemStack(smallDusts, 1, 4));
			OreDictionary.registerOre("nuggetIron", new ItemStack(nuggetIron));

            rawOreIn = new ItemStack(oreChunks);
            rawOreIn.setItemDamage(2);
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
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,0),
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn));
            oreIn = "dustTinyGold";
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,1),
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn));
            oreIn = "dustTinyFlour";
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(largeDusts,1,3),
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn));
            oreIn = "dustTinySugar";
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.sugar),
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn,
            		oreIn, oreIn, oreIn));
            oreIn = "slabWood";
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blocksluice, 2),"sss","ppp",'s',Items.stick,'p',oreIn));
            
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockmill,9), true, new Object[] {"SSS","SWS","SSS", 'S', "stone", 'W', "logWood"}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSifter), true, new Object[] {"PBP","PbP", 'b', Items.bucket, 'P', "plankWood", 'B', Blocks.iron_bars}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockvane, 2), true, new Object[] {"SW","SW", "SW", 'S', Items.stick, 'W', Blocks.wool}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockaxel, 2), true, new Object[] {"WWW", 'W', "logWood"}));
            
            oreIn = "ingotIron";
            rawOreIn = new ItemStack(oreChunks);
            rawOreIn.setItemDamage(2);
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudPick), true, new Object[] {"dId", " s ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudAxe), true, new Object[] {"dI ", "Is ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudShovel), true, new Object[] {" d ", " I ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diaStudHoe), true, new Object[] {"dI ", " s ", " s ", 's', Items.stick, 'I', oreIn, 'd', rawOreIn}));
            
            /*rawOreIn = new ItemStack(largeDusts,1,3);
            GameRegistry.addShapedRecipe(new ItemStack(Items.cookie, 8), "wcw", 'w', rawOreIn, 'c', new ItemStack(Items.dye, 1, 3));
            GameRegistry.addShapedRecipe(new ItemStack(Items.bread), "www", 'w', rawOreIn);
            GameRegistry.addShapedRecipe(new ItemStack(Items.cake), "mmm", "ses", "www", 'w', rawOreIn, 's', Items.sugar, 'e', Items.egg, 'm', Items.milk_bucket);*/
            
            NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
            rawOreIn = new ItemStack(smallDusts, 8);
            nuggetOut = new ItemStack(largeDusts, 1);
            rawOreIn.setItemDamage(0);
            nuggetOut.setItemDamage(0);
            HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
            rawOreIn.setItemDamage(1);
            nuggetOut.setItemDamage(1);
            HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
            rawOreIn.setItemDamage(3);
            nuggetOut.setItemDamage(3);
            HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
            rawOreIn.setItemDamage(4);
            nuggetOut = new ItemStack(Items.sugar);
            HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, nuggetOut);
            
            rawOreIn = new ItemStack(oreChunks);
            nuggetOut = new ItemStack(smallDusts, 2);
            rawOreIn.setItemDamage(0);
            nuggetOut.setItemDamage(0);
            HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            rawOreIn.setItemDamage(1);
            nuggetOut.setItemDamage(1);
            HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            nuggetOut = new ItemStack(Items.dye);
            nuggetOut.setItemDamage(14);
            nuggetOut.stackSize = 2;
            rawOreIn = new ItemStack(oreChunks);
            rawOreIn.setItemDamage(4);
            HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            
            config.addCustomCategoryComment("MILLING", "Enable (hard mode) these to remove vanilla recipes for items and instead require the millstone. In general,\neasy means the millstone to double resources, while hard is near-vanilla.");
            boolean hardFlour = config.get("MILLING", "flourRequireMilling", false).getBoolean();
            
            oreIn = "dustFlour";
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.cookie, 8), "wcw", 'w', oreIn, 'c', new ItemStack(Items.dye, 1, 3)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.cake), "mmm", "ses", "www", 'w', oreIn, 's', Items.sugar, 'e', Items.egg, 'm', Items.milk_bucket));
            
            if(hardFlour) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.bread, 3), "www", 'w', oreIn));
                
                RecipesUtil.RemoveRecipe(Items.bread, 1, 0, "Hard Ores");
            	RecipesUtil.RemoveRecipe(Items.cookie, 8, 0, "Hard Ores");
            	RecipesUtil.RemoveRecipe(Items.cake, 1, 0, "Hard Ores");
            	
            	rawOreIn = new ItemStack(Items.wheat);
                nuggetOut = new ItemStack(smallDusts, 4); //easy: wheat is ground to "4/9th flour"
                nuggetOut.setItemDamage(3);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
                
                rawOreIn = new ItemStack(Items.wheat_seeds); //hard: seeds are ground to "1/9ths flour"
                nuggetOut = new ItemStack(smallDusts, 1);
                nuggetOut.setItemDamage(3);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            }
            else {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.bread), "www", 'w', oreIn));
                
            	rawOreIn = new ItemStack(Items.wheat);
                nuggetOut = new ItemStack(smallDusts, 18); //easy: wheat is ground to "2 flour"
                nuggetOut.setItemDamage(3);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
                
                rawOreIn = new ItemStack(Items.wheat_seeds); //easy: seeds are ground to "2/9ths flour"
                nuggetOut = new ItemStack(smallDusts, 2);
                nuggetOut.setItemDamage(3);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            }
            hardFlour = config.get("MILLING", "sugarRequireMilling", false).getBoolean();
            Property p = config.get("MILLING", "sugarMillingMultiplier", 6);
            p.comment = "Sugar is a easy-to-get resource and rare-to-use, so it may be desirable to reduce the production.\nOutput of milling sugar (in tiny piles) is this value in hard-milling and 2x this value in\neasy-milling.\nMin 1, Max 12, Default 6, Vanilla Equivalence 9.";
            int sugarMulti = Math.min(Math.max(p.getInt(), 1), 12);
        	config.save();
            if(hardFlour) {
            	RecipesUtil.RemoveRecipe(Items.sugar, 1, 0, "Hard Ores");
            	rawOreIn = new ItemStack(Items.reeds); //hard
            	nuggetOut = new ItemStack(smallDusts, sugarMulti);
                nuggetOut.setItemDamage(4);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            }
            else {
            	rawOreIn = new ItemStack(Items.reeds); //easy
            	nuggetOut = new ItemStack(smallDusts, 2*sugarMulti);
                nuggetOut.setItemDamage(4);
                HardLibAPI.recipeManager.addMillRecipe(rawOreIn, nuggetOut);
            }
            hardFlour = config.get("MILLING", "bonemealRequireMilling", false).getBoolean();
            //config.get("MILLING", "bonemealMillingMultiplier", 6).comment = "Output of milling sugar (in tiny piles) is this value in hard-milling and 2x this value in easy-milling.  Max 9, Min 1, Default 6.";
            //sugarMulti = Math.min(Math.max(config.get("MILLING", "bonemealMillingMultiplier", 6).getInt(), 1), 9);
            if(hardFlour) {
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
            HardLibAPI.recipeManager.addSiftRecipe(rawOreIn, rawOreIn);
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	OreFlowerData data;
    	data = new OreFlowerData(blockOreFlowers, 0, 7);
		HardLibAPI.recipeManager.addOreFlowerData(oreIron, data);
    	data = new OreFlowerData(blockOreFlowers, 2, 6);
		HardLibAPI.recipeManager.addOreFlowerData(oreGold, data);
    	data = new OreFlowerData(blockOreFlowers, 3, 5);
		HardLibAPI.recipeManager.addOreFlowerData(oreDiamond, data);
    	data = new OreFlowerData(blockOreFlowers, 4, 999);
		HardLibAPI.recipeManager.addOreFlowerData(oreRedstone, data);
    	
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
    	if(Loader.isModLoaded("IC2")){
    		IC2Integration.registerAPIRecipes();
    		
        	data = new OreFlowerData(blockOreFlowers, 7, 6);
    		HardLibAPI.recipeManager.addOreFlowerData(IC2Integration.oreTin, data);
        	data = new OreFlowerData(blockOreFlowers, 8, 6);
    		HardLibAPI.recipeManager.addOreFlowerData(IC2Integration.oreCopper, data);
        	data = new OreFlowerData(blockOreFlowers, 9, 5);
    		HardLibAPI.recipeManager.addOreFlowerData(IC2Integration.oreLead, data);
        	data = new OreFlowerData(blockOreFlowers, 10, 4);
    		HardLibAPI.recipeManager.addOreFlowerData(IC2Integration.oreUranium, data);
        	
    		if(config.get("SLUICE", "canFindTin", true).getBoolean()) {
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreTin);
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreTin);
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreTin);
        	}
        	if(config.get("SLUICE", "canFindCopper", true).getBoolean()) {
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreCopper);
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreCopper);
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreCopper);
        	}
        	if(config.get("SLUICE", "canFindLead", true).getBoolean()) {
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreLead);
	        	HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreLead);
        	}
        	if(config.get("SLUICE", "canFindUranium", false).getBoolean()) {
        		HardLibAPI.recipeManager.addSluiceRecipe(IC2Integration.oreUranium);
        	}
        	HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
        	HardLibAPI.recipeManager.addSluiceRecipe(null);
    	}
    	if(!HardLibAPI.recipeManager.isSluiceSetFull()) {
        	HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
        	HardLibAPI.recipeManager.addSluiceRecipe(Blocks.gravel);
        	HardLibAPI.recipeManager.addSluiceRecipe(null);
    	}
    	if(Loader.isModLoaded("RotaryCraft")) {
    		RotaryIntegration.registerAPIRecipes();
    	}
    	Property p = config.get("GENERAL", "useDioriteStoneTools", true); 
    	p.comment = "If Gany's Surface is installed and this is true, cobblestone cannot be used to create stone tools,\ninstead diorite is used. This prolongs the life of wood tools so it isn't \"make a wood pickaxe to\nmine 3 stone and upgrade.\"";
    	if(Loader.isModLoaded("ganyssurface") && p.getBoolean()) {
    		GanyIntegration.registerAPIRecipes();
    	}
    	proxy.registerEventHandlers();
    	CogConfig.unpackConfigs();
    	config.save();
    }
    
    public static void scatterFlowers(World world, int x, int y, int z, Block b, int meta, int radius, int num, int clusterRadius) {
    	Random r = new Random();
    	float[] u = RandomInUnitCircle(r);
    	int fails = 0;
    	int j, k, l;
    	while(num > 0 && fails < 20) {
    		j = x + r.nextInt(clusterRadius) - (clusterRadius/2) + Math.round(u[0]*radius);
    		k = y-5;
    		l = z + r.nextInt(clusterRadius) - (clusterRadius/2) + Math.round(u[1]*radius);
    		for(int f=0; f+k <= y+5; f++) {
				if(world.getBlock(j, f+k, l) == Blocks.grass && (world.getBlock(j, f+k+1, l) == Blocks.air || world.getBlock(j, f+k+1, l) == Blocks.tallgrass)) {
					//System.out.println("Placed flower");
					world.setBlock(j, f+k+1, l, b, meta, 3);
					if(meta == 1 && r.nextInt(8) == 0) {
						world.setBlock(j, f+k+1, l, b, 2, 3);
					}
					if(meta == 4 && r.nextInt(4) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+2, l, b, 5, 3);
					}
					if(meta == 7 && r.nextInt(4) == 0 && world.getBlock(j, f+k+2, l) == Blocks.air) {
						world.setBlock(j, f+k+1, l, b, 6, 3);
						world.setBlock(j, f+k+2, l, b, 7, 3);
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