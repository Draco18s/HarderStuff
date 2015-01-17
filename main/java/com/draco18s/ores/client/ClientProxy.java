package com.draco18s.ores.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.draco18s.ores.CommonProxy;
import com.draco18s.ores.OresBase;
import com.draco18s.ores.block.BlockSluice;
import com.draco18s.ores.block.BlockSluiceBottom;
import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.entities.TileEntitySluice;
import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.entities.TileEntityWindmill;
import com.draco18s.ores.entities.TileEntityWindvane;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		TileEntitySpecialRenderer render = new TESRWindvane();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindvane.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockvane), new ItemModelRenderer(render, new TileEntityWindvane()));

		render = new TESRAxel();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmill.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockaxel), new ItemModelRenderer(render, new TileEntityWindmill()));

		render = new TESRSifter();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySifter.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blockSifter), new ItemModelRenderer(render, new TileEntitySifter()));

		if(OresBase.sluiceVersion) {
			render = new TESRSluice();
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySluice.class, render);
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blocksluice), new ItemModelRenderer(render, new TileEntitySluice()));
		}
		else {
			//render = new TESRSluiceBottom();
			//ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySluiceBottom.class, render);
			//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(OresBase.blocksluice), new ItemModelRenderer(render, new TileEntitySluiceBottom()));
			
			int r = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new SimpleBlockRendererSluice(r));
			((BlockSluiceBottom)OresBase.blocksluice).renderID = r;
		}
	}
}
