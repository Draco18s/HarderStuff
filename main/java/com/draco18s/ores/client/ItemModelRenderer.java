package com.draco18s.ores.client;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class ItemModelRenderer implements IItemRenderer {
	TileEntityInventoryRenderer render;
	private TileEntity dummytile;
	
	public ItemModelRenderer(TileEntitySpecialRenderer render, TileEntity dummy) {
		this.render = (TileEntityInventoryRenderer) render;
		this.dummytile = dummy;
	}
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == IItemRenderer.ItemRenderType.ENTITY) {
			GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
			this.render.renderTileEntityInventory(this.dummytile, 0.0D, 0.0D, 0.0D, 0.0F);
		}
		else if(type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
			((TileEntityInventoryRenderer) this.render).renderTileEntityInventory(this.dummytile, 0.0D, 0.0D, 0.0D, (float)(Math.PI/2));
		}
		else {
			this.render.renderTileEntityInventory(this.dummytile, 0.0D, 0.0D, 0.0D, (float)Math.PI);
		}
	}
}