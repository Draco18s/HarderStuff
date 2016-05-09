package com.draco18s.industry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.draco18s.industry.network.CtoSMessage;
import com.draco18s.industry.network.PacketHandlerServer;
import com.draco18s.industry.block.*;
import com.draco18s.industry.entities.*;
import com.draco18s.industry.item.ItemBlockTypeRail;
import com.draco18s.industry.util.StatsAchievements;
import com.draco18s.industry.GuiHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="ExpandedIndustry", name="ExpandedIndustry", version="{@version:ind}"/*, dependencies = "required-after:HardLib"*/)
public class IndustryBase {
	public static Block blockDistributor;
	public static Block blockWoodHopper;
	public static Block blockCartLoader;
	public static Block blockRailType;
	public static Block blockFilter;
	public static Block bridgeRail;
	public static Block bridgeRailPowered;

	@Instance(value = "ExpandedIndustry")
	public static IndustryBase instance;

	public static Configuration config;
	public static boolean sluiceVersion;

	@SidedProxy(clientSide="com.draco18s.industry.client.ClientProxy", serverSide="com.draco18s.industry.CommonProxy")
	public static CommonProxy proxy;

    public static SimpleNetworkWrapper networkWrapper;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		blockDistributor = new BlockDistributor();
		GameRegistry.registerBlock(blockDistributor, "machine_distributor");
		blockWoodHopper = new BlockWoodHopper();
		GameRegistry.registerBlock(blockWoodHopper, "machine_wood_hopper");
		blockCartLoader = new BlockCartLoader();
		GameRegistry.registerBlock(blockCartLoader, "machine_cart_loader");
		blockRailType = new BlockTypeRail();
		GameRegistry.registerBlock(blockRailType, ItemBlockTypeRail.class, "rail_type_detector");
		blockFilter = new BlockFilter();
		GameRegistry.registerBlock(blockFilter, "machine_filter");
		bridgeRail = new BlockRailBridge();
		GameRegistry.registerBlock(bridgeRail, "RailBridge");
		bridgeRailPowered = new BlockPoweredRailBridge();
		GameRegistry.registerBlock(bridgeRailPowered, "PoweredRailBridge");
		
		GameRegistry.registerTileEntity(TileEntityDistributor.class, "industry.distributor");
		GameRegistry.registerTileEntity(TileEntityWoodHopper.class, "industry.wooden_hopper");
		GameRegistry.registerTileEntity(TileEntityCartLoader.class, "industry.cart_loader");
		GameRegistry.registerTileEntity(TileEntityFilter.class, "industry.filter");
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		proxy.registerEventHandler();
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWoodHopper),"p p","p p"," p ",'p',"plankWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDistributor)," c "," i ","ppp",'i',"ingotIron",'c',Blocks.hopper,'p',"plankWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockCartLoader),"i i","rhc"," i ",'i',"ingotIron",'h',Blocks.hopper, 'c',Items.comparator, 'r', Blocks.redstone_block));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRailType,6),"i i","ipi","iqi",'i',"ingotIron",'p',Blocks.stone_pressure_plate, 'q', Items.quartz));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFilter),"g g","ghg"," g ",'g',"ingotGold",'h',Blocks.hopper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bridgeRail), "R", "P", 'R', new ItemStack(Blocks.rail), 'P', "plankWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bridgeRailPowered), "R", "P", 'R', new ItemStack(Blocks.golden_rail), 'P', "plankWood"));
		
    	byte serverMessageID = 2;
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("ExpandedIndustry");
    	networkWrapper.registerMessage(PacketHandlerServer.class, CtoSMessage.class, serverMessageID, Side.SERVER);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		StatsAchievements.addCoreAchievements();
	}
}