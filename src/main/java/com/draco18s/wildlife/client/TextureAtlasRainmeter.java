package com.draco18s.wildlife.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

@SideOnly(Side.CLIENT)
public class TextureAtlasRainmeter extends TextureAtlasSprite {

	public TextureAtlasRainmeter(String p_i1282_1_) {
		super(p_i1282_1_);
	}

	@Override
	public void updateAnimation() {
		if (!this.framesTextureData.isEmpty()) {
			Minecraft minecraft = Minecraft.getMinecraft();
			int i = frameCounter;
			if (minecraft.theWorld != null && minecraft.thePlayer != null) {
				BiomeGenBase bio = minecraft.theWorld.getBiomeGenForCoords((int)minecraft.thePlayer.posX, (int)minecraft.thePlayer.posZ);
				float tt = bio.rainfall;
				if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != minecraft.theWorld.getPrecipitationHeight((int)minecraft.thePlayer.posX, (int)minecraft.thePlayer.posZ) > (int)minecraft.thePlayer.posY) {
					tt = (tt + 1f)/2f;
				}
				i = Math.round((tt-0.575f) * 8);
				i = 7 - Math.max(Math.min(i, 7),0);
			}
			if (i != this.frameCounter) {
				this.frameCounter = i;
				TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
			}
		}
	}
}
