package com.draco18s.wildlife.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

@SideOnly(Side.CLIENT)
public class TextureAtlasThermometer extends TextureAtlasSprite {
	private int frameOverride = -1;

	public TextureAtlasThermometer(String p_i1282_1_, int frm) {
		super(p_i1282_1_);
		frameOverride = frm;
	}

	@Override
	public void updateAnimation() {
		if (!this.framesTextureData.isEmpty()) {
			Minecraft minecraft = Minecraft.getMinecraft();
			int i = frameCounter;
			if(frameOverride > -1) {
				i = frameOverride;
			}
			else if (minecraft.theWorld != null && minecraft.thePlayer != null) {
				BiomeGenBase bio = minecraft.theWorld.getBiomeGenForCoords((int)minecraft.thePlayer.posX, (int)minecraft.thePlayer.posZ);
				float tt = bio.temperature;
				if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != minecraft.theWorld.getPrecipitationHeight((int)minecraft.thePlayer.posX, (int)minecraft.thePlayer.posZ) > (int)minecraft.thePlayer.posY) {
					tt = (tt + 0.8f)/2f;
				}
				i = Math.round((tt+0.35f) * 4);
				i = Math.max(Math.min(i, 11),0);
			}
			if (i != this.frameCounter) {
				this.frameCounter = i;
				TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
			}
		}
	}
}
