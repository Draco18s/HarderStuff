package com.draco18s.hardlib.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import cpw.mods.fml.client.CustomModLoadingErrorDisplayException;

public class CogMissingException extends CustomModLoadingErrorDisplayException {

	public CogMissingException() {
		super();
	}

	public CogMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {

	}

	@Override
	public void drawScreen(GuiErrorScreen errorScreen,
			FontRenderer fontRenderer, int mouseRelX, int mouseRelY,
			float tickTime) {
		errorScreen.drawCenteredString(fontRenderer, "HardLib requires COG version 1.2.18 (released March 2, 2015) or later.", errorScreen.width/2, 95, 0xEEEEEE);
		errorScreen.drawCenteredString(fontRenderer, "Unfortunately COG does not a version number in its @mod declaration", errorScreen.width/2, 105, 0xEEEEEE);
		errorScreen.drawCenteredString(fontRenderer, "for a Forge initialization warning.", errorScreen.width/2, 115, 0xEEEEEE);
	}
}
