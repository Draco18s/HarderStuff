package com.draco18s.industry.client;

import com.draco18s.industry.entities.TileEntityWoodHopper;
import com.draco18s.industry.inventory.ContainerWoodenHopper;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiContainerWoodenHopper extends GuiContainer {
	TileEntityWoodHopper te;
	
	public GuiContainerWoodenHopper(Container par1Container) {
		super(par1Container);
	}

	public GuiContainerWoodenHopper (InventoryPlayer inventoryPlayer, TileEntityWoodHopper tileEntity) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerWoodenHopper(inventoryPlayer, tileEntity));
		te = tileEntity;
        ySize = 133;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(StatCollector.translateToLocal("container.wooden_hopper"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		ResourceLocation texture = new ResourceLocation("textures/gui/container/hopper.png");
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		/*int progress = Math.round(24 * (40 - te.getTime()) / 40.0f);
		if(te.getTime() == 0) {
			progress = 0;
		}
		this.drawTexturedModalRect(x+79, y+29, 176, 0, 17, progress);*/
	}
}
