package com.draco18s.industry.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.draco18s.industry.IndustryBase;
import com.draco18s.industry.entities.TileEntityFilter;
import com.draco18s.industry.entities.TileEntityFilter.EnumAcceptType;
import com.draco18s.industry.entities.TileEntityWoodHopper;
import com.draco18s.industry.inventory.ContainerFilter;
import com.draco18s.industry.inventory.ContainerWoodenHopper;
import com.draco18s.industry.inventory.SlotIInventory;
import com.draco18s.industry.network.CtoSMessage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiContainerFilter extends GuiContainer {
	private TileEntityFilter te;
	private static ResourceLocation texture;
	public static GuiContainerFilter self;

	public GuiContainerFilter(Container par1Container) {
		super(par1Container);
		self = this;
	}

	public GuiContainerFilter (InventoryPlayer inventoryPlayer, TileEntityFilter tileEntity) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerFilter(inventoryPlayer, tileEntity));
		texture = new ResourceLocation("industry:textures/gui/filter.png");
		te = tileEntity;
		ySize = 160;
		self = this;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new LogicButton(0, this.guiLeft + 142, this.guiTop + 45, te.getEnumType()));
		buttonList.add(new HintButton(1, this.guiLeft + 157, this.guiTop + 3));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(StatCollector.translateToLocal("container.filter_hopper"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 94, 4210752);

		fontRendererObj.drawString(StatCollector.translateToLocal("gui.rules_header"), 8, 37, 4210752);

		if(!te.doIHaveFilters()) {
			this.mc.renderEngine.bindTexture(texture);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor3f(1, 1, 1);
			for (int i = 0; i < te.getSizeInventory(); ++i) {
				drawTexturedModalRect(8 + i * 18, 17, 176, 0, 16, 16);
			}
			fontRendererObj.drawString(StatCollector.translateToLocal("gui.no_accept1"), 100, 17, 4210752);
			fontRendererObj.drawString(StatCollector.translateToLocal("gui.no_accept2"), 100, 26, 4210752);
			//drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}

		Iterator iterator = this.buttonList.iterator();

		while (iterator.hasNext())
		{
			GuiButton guibutton = (GuiButton)iterator.next();

			if (guibutton.func_146115_a())
			{
				guibutton.func_146111_b(param1 - this.guiLeft, param2 - this.guiTop);
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	public void drawText(String p_146279_1_, int p_146279_2_, int p_146279_3_) {
		this.drawCreativeTabHoveringText(p_146279_1_, p_146279_2_, p_146279_3_);
	}

	public void drawText(List p_146279_1_, int p_146279_2_, int p_146279_3_) {
		this.func_146283_a(p_146279_1_, p_146279_2_, p_146279_3_);
	}

	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			LogicButton lb = (LogicButton)button;
			EnumAcceptType t = lb.cycleType();
			IndustryBase.networkWrapper.sendToServer(new CtoSMessage(te.getWorldObj().provider.dimensionId, te.xCoord, te.yCoord, te.zCoord, t.ordinal()));
			//te.setEnumType(lb.cycleType());
		}
	}

	/*protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long time) {
		int mX = mouseX - this.guiLeft;
		int mY = mouseY - this.guiTop;
		
		Slot slot = getSlotAtPosition(mouseX, mouseY);
		if ((mouseButton == 1) && ((slot instanceof SlotIInventory))) {
			return;
		}
		super.mouseClickMove(mouseX, mouseY, mouseButton, time);
	}*/
	
	private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_) {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);
            if (this.isMouseOverSlot(slot, p_146975_1_, p_146975_2_)) {
                return slot;
            }
        }
        return null;
    }
	
	private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_) {
        return this.func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
    }

	@SideOnly(Side.CLIENT)
	static class LogicButton extends GuiButton {
		private EnumAcceptType type;
		private static final String __OBFID = "CL_00000743";

		protected LogicButton(int buttonID, int posx, int posy, EnumAcceptType t) {
			super(buttonID, posx, posy, 24, 22, "");
			type = t;
		}

		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY) {
			if (this.visible) {
				p_146112_1_.getTextureManager().bindTexture(texture);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				short short1 = 160;
				int k = this.width * type.ordinal() + 48;
				int h = this.getHoverState(this.field_146123_n) - 1;
				this.drawTexturedModalRect(this.xPosition, this.yPosition, this.width*h, short1, this.width, this.height);

				/*if (!texture.equals(this.buttonTexture)) {
					p_146112_1_.getTextureManager().bindTexture(this.buttonTexture);
				}*/

				this.drawTexturedModalRect(this.xPosition, this.yPosition, k, short1, this.width, this.height);
			}
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			List list = new ArrayList<String>();
			list.add(I18n.format("gui.logic_type", new Object[0]));
			list.add(I18n.format("gui."+this.type.name().toLowerCase(), new Object[0]));
			GuiContainerFilter.self.drawText(list, p_146111_1_-180, p_146111_2_+16);
			//GuiContainerFilter.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), p_146111_1_, p_146111_2_);
		}

		public EnumAcceptType cycleType() {
			this.type = newType();
			return this.type;
		}

		private EnumAcceptType newType() {
			return EnumAcceptType.values()[(type.ordinal()+1)%4];
		}
	}



	@SideOnly(Side.CLIENT)
	static class HintButton extends GuiButton {
		private static final String __OBFID = "CL_00000743";

		protected HintButton(int buttonID, int posx, int posy) {
			super(buttonID, posx, posy, 16, 16, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft p_146112_1_, int mouseX, int mouseY) {
			if (this.visible) {
				p_146112_1_.getTextureManager().bindTexture(texture);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				//short short1 = 160;
				//int k = this.width;
				//int h = this.getHoverState(this.field_146123_n) - 1;
				//this.drawTexturedModalRect(this.xPosition, this.yPosition, this.width*h, short1, this.width, this.height);

				//this.drawTexturedModalRect(this.xPosition, this.yPosition, k, short1, this.width, this.height);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 182, this.width, this.height);
			}
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			List list = new ArrayList<String>();
			list.add(I18n.format("gui.filter_explain1", new Object[0]));
			list.add(I18n.format("gui.filter_explain2", new Object[0]));
			list.add(I18n.format("gui.filter_explain3", new Object[0]));
			list.add(I18n.format("gui.filter_explain4", new Object[0]));
			GuiContainerFilter.self.drawText(list, p_146111_1_-200, p_146111_2_+16);
			//GuiContainerFilter.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), p_146111_1_, p_146111_2_);
		}
	}
}
