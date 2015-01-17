package com.draco18s.hazards;

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
import com.draco18s.hazards.block.*;
import com.draco18s.hazards.block.helper.GasFlowHelper;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.integration.GanyStoneRegistration;

@Mod(modid="HarderUnderground", name="HarderUnderground", version="0.1.3", dependencies = "required-after:HardLib")
public class UndergroundBase {
	public static Block blockVolatileGas;
	public static Block blockThinVolatileGas;
	public static Block blockVolcanicGas;
	//public static Block unstableStone;
	public static Block rockDust;
	
	public static Material gas = (new MaterialLiquid(MapColor.airColor));
	
    @Instance(value = "HarderUnderground")
    public static UndergroundBase instance;
    
    public static Configuration config;
    public static boolean doVolcanicGas;
    public static boolean doSmoke;
    public static boolean doStoneReplace;
    public static boolean doGasSeeping;
   
    @SidedProxy(clientSide="com.draco18s.hazards.client.ClientProxy", serverSide="com.draco18s.hazards.CommonProxy")
    public static CommonProxy proxy;
    public static Random rand = new Random();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	Fluid fluidVolatile = new Fluid("VolatileGas");
    	fluidVolatile.setViscosity(15).setDensity(100).setTemperature(295).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockVolatileGas = new BlockVolatileGas(fluidVolatile);
    	GameRegistry.registerBlock(blockVolatileGas, "volatile_gas");
    	
    	fluidVolatile = new Fluid("ThinVolatileGas");
    	fluidVolatile.setViscosity(15).setDensity(-100).setTemperature(285).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockThinVolatileGas = new BlockThinVolatileGas(fluidVolatile);
    	GameRegistry.registerBlock(blockThinVolatileGas, "thin_volatile_gas");
    	
    	fluidVolatile = new Fluid("VolcanicGas");
    	fluidVolatile.setViscosity(90).setDensity(-10).setTemperature(675).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockVolcanicGas = new BlockVolcanicGas(fluidVolatile);
    	GameRegistry.registerBlock(blockVolcanicGas, "volcanic_gas");
    	
    	//unstableStone = new BlockUnstableStone("stone");
    	//GameRegistry.registerBlock(unstableStone, "unstable_stone");
    	
    	rockDust = new BlockRockDust();
    	GameRegistry.registerBlock(rockDust, "rock_dust");

    	CogConfig.addCogModule("UndergroundHazards.xml");
    	
    	doVolcanicGas = config.getBoolean("spawnVolcanicGas", "HAZARDS", true, "This spawns smoke above lava when the player is nearby and dissipates quickly.");
    	doSmoke = config.getBoolean("spawnSmoke", "HAZARDS", true, "This spawns smoke above fires when the player is nearby and dissipates quickly.");
    	doStoneReplace = config.getBoolean("replaceStone", "HAZARDS", true, "Replaces vanilla stone with a stone that does not float and requires support.\nDrops vanilla cobble which smelts into vanilla stone as normal.");
    	doGasSeeping = config.getBoolean("gasSeeps", "HAZARDS", true, "If true, thin volatile gas will try to move past standard \"water tight\" blocks like doors and signs.");
    	
    	if(doStoneReplace) {
    		//RecipesUtil.RemoveSmelting(Item.getItemFromBlock(Blocks.stone), 1, 0, "Hard Underground");
        	StoneRegistry.addStoneType(Blocks.stone, 0, Blocks.stone.getUnlocalizedName(), "stone");
    	}
    	
    	//UnstableStoneHelper.addSupportBlock(unstableStone);
    	UnstableStoneHelper.addSupportBlock(Blocks.stone);
    	UnstableStoneHelper.addSupportBlock(Blocks.coal_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.iron_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.gold_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.diamond_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.redstone_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.lit_redstone_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.lapis_ore);
    	UnstableStoneHelper.addSupportBlock(Blocks.emerald_ore);
    	
    	if(Loader.isModLoaded("ganyssurface")) {
    		GanyStoneRegistration.registerAPIRecipes();
    	}
    }
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	Blocks.fire.setFireInfo(blockVolatileGas, 60, 1);
    	Blocks.fire.setFireInfo(blockThinVolatileGas, 1000, 25);
    	GasFlowHelper.addWoodenDoor(Blocks.wooden_door);
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.registerEvents();
    	proxy.registerRenderers();
    	CogConfig.unpackConfigs();
    	config.save();
    }
    
    public static boolean needsToBreath(EntityLivingBase ent) {
    	if(ent instanceof EntityZombie || ent instanceof EntitySkeleton || ent instanceof EntityIronGolem) return false;
    	
    	return true;
    }

    @EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
    	for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
            if (imcMessage.key.equalsIgnoreCase("RR-underground-support")) {
            	if(imcMessage.isItemStackMessage()) {
            		UnstableStoneHelper.addSupportBlock(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
            	}
            }
            else if(imcMessage.key.equalsIgnoreCase("RR-underground-isDoor")) {
            	if(imcMessage.isItemStackMessage()) {
            		GasFlowHelper.addWoodenDoor(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
            	}
            }
        }
    }
}