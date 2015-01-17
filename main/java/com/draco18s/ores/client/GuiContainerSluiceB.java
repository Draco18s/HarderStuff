package com.draco18s.ores.client;

import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.inventory.ContainerSluiceB;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiContainerSluiceB extends GuiContainer {
	TileEntitySluiceBottom te;
	
	public GuiContainerSluiceB(Container par1Container) {
		super(par1Container);
	}

	public GuiContainerSluiceB (InventoryPlayer inventoryPlayer, TileEntitySluiceBottom tileEntity) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerSluiceB(inventoryPlayer, tileEntity));
		te = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString("Sluice", 8, 6, 4210752);
		fontRendererObj.drawString("Material:", 18, 42, 4210752);
		String str = "" + te.getDirt();
		int f = fontRendererObj.getStringWidth(str);
		fontRendererObj.drawString(str, 60-f, 52, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("Inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		ResourceLocation texture = new ResourceLocation("ores:textures/gui/sluice.png");
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		int progress = Math.round(19 * (375 - te.getTime()) / 375.0f);
		if(te.getTime() == 0) {
			progress = 0;
		}
		this.drawTexturedModalRect(x+88, y+56, 176, 0, progress, 8);
		progress = 0;
		int d = te.getDirt();
		if(d <= 320) {
			progress = Math.round(4 * d / 320.0f);
		}
		else if(d <= 640) {
			progress = Math.round(4 * (d-320) / 320.0f) + 4;
		}
		else if(d <= 1280) {
			progress = Math.round(8 * (d-640) / 640.0f) + 8;
		}
		else {
			progress = Math.round(18 * (d-1280) / 8960.0f) + 16;
		}
		if(progress > 34) progress = 34;
		this.drawTexturedModalRect(x+23+progress, y+65, 176, 8, 5, 5);
	}
}
