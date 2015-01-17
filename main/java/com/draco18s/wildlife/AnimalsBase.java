package com.draco18s.wildlife;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
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
import com.draco18s.hazards.block.helper.GasFlowHelper;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.ores.block.BlockOreFlower;
import com.draco18s.ores.item.ItemBlockOreFlower;
import com.draco18s.wildlife.block.BlockRottingWood;
import com.draco18s.wildlife.item.ItemBlockRottingWood;
import com.draco18s.wildlife.util.BlockUtils;
import com.draco18s.wildlife.util.BlockUtils.BlockType;
import com.draco18s.wildlife.util.TreeCountGenerator;
import com.draco18s.wildlife.util.TreeDataHooks;

@Mod(modid="HarderWildlife", name="HarderWildlife", version="0.1.3", dependencies = "required-after:HardLib")
public class AnimalsBase {
	
    @Instance(value = "HarderWildlife")
    public static AnimalsBase instance;
    
    public static Configuration config;
    public static TreeCountGenerator treeCounter;
	public static Block rottingWood;
   
    @SidedProxy(clientSide="com.draco18s.wildlife.client.ClientProxy", serverSide="com.draco18s.wildlife.CommonProxy")
    public static CommonProxy proxy;
    public static Random rand = new Random();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	WildlifeEventHandler.autoSaplings = config.getBoolean("autoPlantSaplings", "WILDLIFE", true, "Enables/disables auto-replanting of saplings, cactus, mushrooms, netherwart, and reeds.  Also controls automated tree death.\nKind of buggy and imperfect, feel free to disable as its largely a 'nice touch' feature.");
    	TreeDataHooks.treeLifeMultiplier = config.getInt("TreeAgeSpeed", "WILDLIFE", 100, 1, 10000, "Determines how quickly trees die off.  Larger values mean a longer life.\n10 means that trees live on average 1 hour at default max age.");
    	TreeDataHooks.treeMaxAge = config.getInt("TreeMaxAge", "WILDLIFE", 10000, 100, 1000000, "How old trees can get before dying, in ticks.  This is effectively multiplied by the TreeAgeSpeed.");
    	
    	BlockUtils.registerBlockType(Blocks.sapling, BlockType.SAPLING);
    	BlockUtils.registerBlockType(Blocks.cactus, BlockType.CACTUS);
    	BlockUtils.registerBlockType(Blocks.brown_mushroom, BlockType.MUSHROOM);
    	BlockUtils.registerBlockType(Blocks.red_mushroom, BlockType.MUSHROOM);
    	BlockUtils.registerBlockType(Blocks.nether_wart, BlockType.NETHERSTALK);
    	BlockUtils.registerBlockType(Blocks.reeds, BlockType.REEDS);

    	WildlifeEventHandler.weekLength = 2*config.getInt("moonPhaseTime", "SEASONS", 2, 1, 32, "How many Minecraft-Days it takes the moon to change phase (\"1 week\").\n1 is vanilla, 1 or 2 is good for SSP, 8+ is good for SMP.  Effects year length.");
    	WildlifeEventHandler.yearLength = WildlifeEventHandler.weekLength * 768000L;//768000
    	System.out.println("Year length: " + WildlifeEventHandler.yearLength + " (" + (WildlifeEventHandler.yearLength/24000) + " MC days)");
    	
    	WildlifeEventHandler.doSnowMelt = config.getBoolean("EnableSnowAccumulation", "SEASONS", true, "Allows snow to pile up when it snows and melt away again.");
    	/*rottingWood = new BlockRottingWood();
    	GameRegistry.registerBlock(rottingWood, "log_rotting");*/

    	rottingWood = new BlockRottingWood();
    	GameRegistry.registerBlock(rottingWood, ItemBlockRottingWood.class, "log_rotting");
    }
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	treeCounter = new TreeCountGenerator();
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.registerEvents();
    	proxy.registerRenderers();
    	CogConfig.unpackConfigs();
    	config.save();
    	
    	Blocks.fire.setFireInfo(rottingWood, 1, 10);
    	
    	try {
    		WildlifeEventHandler.doYearCycle = config.getBoolean("doSeasons","SEASONS",true,"Toggle for whether or not seasonal variation in weather should occur.");
    		Class clz = Class.forName("com.draco18s.cropcore.asm.CropPatcher");
    		clz.getDeclaredField("moonPhaseTime").setLong(null, 12000L * WildlifeEventHandler.weekLength);
    		
    		clz = BiomeGenBase.class;
    		WildlifeEventHandler.rains = clz.getDeclaredField("enableRain");//field_76765_S
    		WildlifeEventHandler.rains.setAccessible(true);
    		WildlifeEventHandler.snows = clz.getDeclaredField("enableSnow");//field_76766_R
    		WildlifeEventHandler.snows.setAccessible(true);
    	}
    	catch(Exception e ) {
    		WildlifeEventHandler.doYearCycle = false;
    		System.out.println("Failed to modify moon phase time!");
    		System.out.println(e);
    	}
    }
}