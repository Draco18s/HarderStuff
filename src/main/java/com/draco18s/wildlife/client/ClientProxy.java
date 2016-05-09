package com.draco18s.wildlife.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.draco18s.hardlib.ItemModelRenderer;
import com.draco18s.wildlife.integration.NEIIntegration;
import com.draco18s.wildlife.CommonProxy;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.block.BlockSnowyTallGrass;
import com.draco18s.wildlife.client.model.ModelGoat1;
import com.draco18s.wildlife.client.model.ModelGoat2;
import com.draco18s.wildlife.client.model.ModelLizard;
import com.draco18s.wildlife.entity.EntityGoat;
import com.draco18s.wildlife.entity.EntityLizard;
import com.draco18s.wildlife.entity.TileEntityTanner;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;

public class ClientProxy extends CommonProxy {
	public static TextureAtlasSprite thermometer;
	public static TextureAtlasSprite rainmeter;
	public static TextureAtlasSprite calendar;
	public static IIcon[] thermometerNBT = new IIcon[12];
	public static IIcon[] rainmeterNBT = new IIcon[8];

	@Override
	public void registerRenderers() {
		int r = RenderingRegistry.getNextAvailableRenderId();
		BlockSnowyTallGrass.renderID = r;
		RenderingRegistry.registerBlockHandler(new RenderSnowyGrass(r));
		RenderingRegistry.registerEntityRenderingHandler(EntityGoat.class, new RenderGoat(new ModelGoat2(), new ModelGoat1(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLizard.class, new RenderLizard(new ModelLizard()));
		
		TileEntitySpecialRenderer render = new TESRTanner();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTanner.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(WildlifeBase.blockTanner), new ItemModelRenderer(render, new TileEntityTanner()));
		
		if(Loader.isModLoaded("NotEnoughItems")) {
			NEIIntegration.registerNEIRecipes();
		}
	}

	public void registerEvents() {
		super.registerEvents();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
	@Override
	public World getWorld() {
		if(Minecraft.getMinecraft() != null)
			return Minecraft.getMinecraft().theWorld;
		return null;
	}
}
