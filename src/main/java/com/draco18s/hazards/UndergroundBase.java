package com.draco18s.hazards;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.draco18s.hardlib.CogConfig;
import com.draco18s.hardlib.HashUtils;
import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hazards.block.*;
import com.draco18s.hazards.block.helper.GasFlowHelper;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.enchantments.EnchantmentStoneStress;
import com.draco18s.hazards.integration.IC2Integration;
import com.draco18s.hazards.integration.IntegrationForestry;
import com.draco18s.hazards.integration.ModStoneRegistration;
import com.draco18s.hazards.item.ItemEngGoggles;
import com.draco18s.hazards.network.CtoSMessage;
import com.draco18s.hazards.network.PacketHandlerServer;

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
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="HarderUnderground", name="HarderUnderground", version="{@version:haz}", dependencies = "required-after:HardLib")
public class UndergroundBase {
	public static Block blockVolatileGas;
	public static Block blockThinVolatileGas;
	public static Block blockVolcanicGas;
	//public static Block unstableStone;
	public static Block rockDust;

	public static Item goggles;
	
	public static Material gas = (new MaterialLiquid(MapColor.airColor));
	
    @Instance(value = "HarderUnderground")
    public static UndergroundBase instance;
    
    public static Configuration config;
    public static boolean doVolcanicGas;
    public static boolean doSmoke;
    public static boolean doStoneReplace;
    public static boolean doGasSeeping;
    public static SimpleNetworkWrapper networkWrapper;
   
    @SidedProxy(clientSide="com.draco18s.hazards.client.ClientProxy", serverSide="com.draco18s.hazards.CommonProxy")
    public static CommonProxy proxy;
    public static Random rand = new Random();
    
    public static int catchFireRange;
	public static Logger logger;
	
	public static Enchantment enchStoneStress;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = event.getModLog();
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	Fluid fluidVolatile = new Fluid("VolatileGas");
    	fluidVolatile.setViscosity(200).setDensity(100).setTemperature(295).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockVolatileGas = new BlockVolatileGas(fluidVolatile);
    	GameRegistry.registerBlock(blockVolatileGas, "volatile_gas");
    	
    	fluidVolatile = new Fluid("ThinVolatileGas");
    	fluidVolatile.setViscosity(200).setDensity(-100).setTemperature(285).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockThinVolatileGas = new BlockThinVolatileGas(fluidVolatile);
    	GameRegistry.registerBlock(blockThinVolatileGas, "thin_volatile_gas");
    	
    	fluidVolatile = new Fluid("VolcanicGas");
    	fluidVolatile.setViscosity(200).setDensity(-100).setTemperature(675).setGaseous(true);
    	FluidRegistry.registerFluid(fluidVolatile);
    	blockVolcanicGas = new BlockVolcanicGas(fluidVolatile);
    	GameRegistry.registerBlock(blockVolcanicGas, "volcanic_gas");
    	
    	//unstableStone = new BlockUnstableStone("stone");
    	//GameRegistry.registerBlock(unstableStone, "unstable_stone");
    	
    	rockDust = new BlockRockDust();
    	GameRegistry.registerBlock(rockDust, "rock_dust");
    	
    	goggles = new ItemEngGoggles();
    	GameRegistry.registerItem(goggles, "engineer_goggles");
    	
    	int id = config.getInt("StressEnchantmentID", "GENERAL", 80, 64, 255, "Enchantment ID for the Stress Analyser enchantment.");
    	enchStoneStress = new EnchantmentStoneStress(id,10);

    	CogConfig.addCogModule("UndergroundHazards.xml");
    	
    	doVolcanicGas = config.getBoolean("spawnVolcanicGas", "HAZARDS", true, "This spawns smoke above lava when the player is nearby and dissipates quickly.\n");
    	doSmoke = config.getBoolean("spawnSmoke", "HAZARDS", true, "This spawns smoke above fires when the player is nearby and dissipates quickly.\n");
    	doStoneReplace = config.getBoolean("replaceStone", "HAZARDS", true, "Replaces vanilla stone with a stone that does not float and requires support.\nDrops unstable cobble which smelts into unstable stone.\nThere is a crafting recipe to convert to vanilla.\n");
    	//doStoneReplace = false;
    	doGasSeeping = config.getBoolean("gasSeeps", "HAZARDS", true, "If true, thin volatile gas will try to move past standard \"water tight\" blocks\nlike doors and signs.\n");
    	catchFireRange = config.getInt("gasCatchFireRange", "HAZARDS", 96, -1, 512, "Lag preventative, this limits the range that a player needs to be within\nin order for volatile gas to ignite when coming in contact with\nfire sources other than Fire. -1 effectively disables the effect (infinite range).\nIf you are seeing root.tick.gameMode.checkLight (shift-F3) taking most of the\nframe time (>10% of total), lower this value.\n");
    	
    	HazardsEventHandler.sidwaysFallPhysics = config.getBoolean("fallingBlocksShiftSideways", "HAZARDS", true, "If enabled, falling block type blocks will attempt to move\nsideways one block if there is open space there, forming pyramids.\n");
    	HazardsEventHandler.enableRockDust = config.getBoolean("enableRockDust", "HAZARDS", true, "If enabled, collapsing stone will spawn a cloud of choking dust.");
    	if(catchFireRange == 0) catchFireRange = 1;
    	
    	HardLibAPI.stoneManager = new StoneRegistry();
    	Block unst = HardLibAPI.stoneManager.addStoneType(Blocks.stone, 0, Blocks.stone.getUnlocalizedName(), "stone");
    	if(doStoneReplace) {
    		//RecipesUtil.RemoveSmelting(Item.getItemFromBlock(Blocks.stone), 1, 0, "Hard Underground");
    		ItemStack in = new ItemStack(unst, 0, 3);
        	GameRegistry.addShapedRecipe(new ItemStack(Blocks.cobblestone_wall), "sss","sss",'s',in);
        	GameRegistry.addShapedRecipe(new ItemStack(Blocks.stone_stairs), "  s"," ss","sss",'s',in);
        	GameRegistry.addShapedRecipe(new ItemStack(Blocks.cobblestone),"scs",'s',new ItemStack(Items.stick),'c',in);
        	GameRegistry.addShapedRecipe(new ItemStack(goggles), "iri","lgl",'i',new ItemStack(Items.iron_ingot),'l',new ItemStack(Items.leather),'r',new ItemStack(Items.redstone),'g',new ItemStack(Blocks.glass_pane));
        	try {
        		Class clz = Class.forName("com.draco18s.hardlibcore.asm.HardLibPatcher");
        		clz.getDeclaredField("unstableStoneBlock").set(null, unst);
        	}
        	catch (ClassNotFoundException ex) {
        	} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
			}
        	 
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
    	UnstableStoneHelper.addSupportBlock(Blocks.netherrack);
    	UnstableStoneHelper.addSupportBlock(Blocks.quartz_ore);
    	
    	/*if(Loader.isModLoaded("??!?")) {
    		ModStoneRegistration.registerGeostrata();
    	}*/
    	//These have to be unique
    	byte serverMessageID = 1;
    	//byte clientMessageID = 2;
    	
    	networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("HarderUnderground");
    	networkWrapper.registerMessage(PacketHandlerServer.class, CtoSMessage.class, serverMessageID, Side.SERVER);
    	//networkWrapper.registerMessage(PacketHandlerClient.class, StoCMessage.class, clientMessageID, Side.CLIENT);
    }
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	if(Loader.isModLoaded("ganyssurface")) {
    		ModStoneRegistration.registerGany();
    	}
    	
    	Blocks.fire.setFireInfo(blockVolatileGas, 60, 1);
    	Blocks.fire.setFireInfo(blockThinVolatileGas, 1000, 25);
    	GasFlowHelper.addWoodenDoor(Blocks.wooden_door);
    	GasFlowHelper.addWoodenDoor(Blocks.iron_door);
    	
    	GasFlowHelper.addPartialBlock(Blocks.trapdoor);
    	GasFlowHelper.addPartialBlock(Blocks.ladder);
    	GasFlowHelper.addPartialBlock(Blocks.wall_sign);
    	GasFlowHelper.addPartialBlock(Blocks.flower_pot);
    	GasFlowHelper.addPartialBlock(Blocks.rail);
    	GasFlowHelper.addPartialBlock(Blocks.activator_rail);
    	GasFlowHelper.addPartialBlock(Blocks.detector_rail);
    	GasFlowHelper.addPartialBlock(Blocks.golden_rail);
    	GasFlowHelper.addPartialBlock(Blocks.redstone_torch);
    	GasFlowHelper.addPartialBlock(Blocks.unlit_redstone_torch);
    	GasFlowHelper.addPartialBlock(Blocks.redstone_wire);
    	GasFlowHelper.addPartialBlock(Blocks.cake);
    	GasFlowHelper.addPartialBlock(Blocks.fence);
    	GasFlowHelper.addPartialBlock(Blocks.fence_gate);
    	GasFlowHelper.addPartialBlock(Blocks.iron_bars);
    	GasFlowHelper.addPartialBlock(Blocks.heavy_weighted_pressure_plate);
    	GasFlowHelper.addPartialBlock(Blocks.light_weighted_pressure_plate);
    	GasFlowHelper.addPartialBlock(Blocks.stone_button);
    	GasFlowHelper.addPartialBlock(Blocks.stone_pressure_plate);
    	GasFlowHelper.addPartialBlock(Blocks.lever);
    	GasFlowHelper.addPartialBlock(Blocks.nether_brick_fence);
    	GasFlowHelper.addPartialBlock(Blocks.reeds);
    	GasFlowHelper.addPartialBlock(Blocks.wooden_button);
    	GasFlowHelper.addPartialBlock(Blocks.wooden_pressure_plate);
    	GasFlowHelper.addPartialBlock(Blocks.tripwire);
    	GasFlowHelper.addPartialBlock(Blocks.tripwire_hook);
    	
		FMLInterModComms.sendMessage("Waila", "register", "com.draco18s.hazards.integration.WailaIntegration.callbackRegister");
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("BlockName", "volatile_gas");
		nbt.setFloat("Accessibility", 0.05f);
		nbt.setFloat("Flat", -0.25f);
		FMLInterModComms.sendMessage("Mystcraft", "blockinstability", nbt);
		/*nbt = new NBTTagCompound();
		nbt.setString("BlockName", "thin_volatile_gas");
		nbt.setFloat("Accessibility", -1f);
		nbt.setFloat("Flat", -0.1f);
		FMLInterModComms.sendMessage("Mystcraft", "blockinstability", nbt);*/
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.registerEvents();
    	proxy.registerRenderers();
    	

		if(Loader.isModLoaded("IC2")){
			IC2Integration.registerMaceRatorRecipes();
		}
    	
    	Property verConf = config.get("configVersion", "GENERAL", "1.0", "Do not touch"); 
		boolean b = HashUtils.versionCompare(verConf.getString(),"3.0") < 0;
		CogConfig.unpackConfigs(b);
		verConf.set("4.10");
    	
    	boolean hardShovel = config.getBoolean("modifyBlockHardness", "GENERAL", true, "This setting increases the block hardness of all (vanilla) shovel-harvest blocks,\nincreasing the value of a shovel, as dig speed is reduced by ~60% for most blocks.\nAlso increases the hardness of logs (33%) and huge mushrooms (62%).\n");
    	float shovelMulti = config.getFloat("multiplyBlockHardness", "GENERAL", 1, 1, 3, "Not hard enough?  Change this.\nThis value is divided by 0.6 for most blocks and 0.75 for the remainder,\nand multiplied by the blocks' original hardness.\nA value of 1.5 will make grass as hard as stone (using a tool), 1.8 will make dirt and sand that hard.\n");
    	if(hardShovel) {
	    	float f = shovelMulti / 0.6f;
	    	float f2 = shovelMulti / 0.75f;
	    	Blocks.dirt.setHardness(Blocks.dirt.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.grass.setHardness(Blocks.grass.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.farmland.setHardness(Blocks.farmland.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.sand.setHardness(Blocks.sand.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.gravel.setHardness(Blocks.gravel.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.clay.setHardness(Blocks.clay.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.mycelium.setHardness(Blocks.mycelium.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.soul_sand.setHardness(Blocks.soul_sand.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.snow.setHardness(Blocks.snow.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.snow_layer.setHardness(Blocks.snow_layer.getBlockHardness(null, 0, 0, 0) * f2);
	    	
	    	Blocks.log.setHardness(Blocks.log.getBlockHardness(null, 0, 0, 0) * f2);
	    	Blocks.log2.setHardness(Blocks.log2.getBlockHardness(null, 0, 0, 0) * f2);
	    	Blocks.brown_mushroom_block.setHardness(Blocks.brown_mushroom_block.getBlockHardness(null, 0, 0, 0) * f);
	    	Blocks.red_mushroom_block.setHardness(Blocks.red_mushroom_block.getBlockHardness(null, 0, 0, 0) * f);
	    	if(Loader.isModLoaded("Forestry")) {
	    		IntegrationForestry.modifyForestryTrees();
	    	}
	    }
    	config.save();
    }
    
    public static boolean needsToBreath(EntityLivingBase ent) {
    	if(ent instanceof EntityIronGolem || ent.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) return false;
    	
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
            else if(imcMessage.key.equalsIgnoreCase("RR-underground-isPartialBlock")) {
            	if(imcMessage.isItemStackMessage()) {
            		GasFlowHelper.addWoodenDoor(Block.getBlockFromItem(imcMessage.getItemStackValue().getItem()));
            	}
            }
        }
    }
}