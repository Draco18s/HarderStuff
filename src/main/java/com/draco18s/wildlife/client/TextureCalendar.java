package com.draco18s.wildlife.client;

import com.draco18s.wildlife.WildlifeEventHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;

@SideOnly(Side.CLIENT)
public class TextureCalendar extends TextureAtlasSprite {

    public TextureCalendar(String par1Str) {
        super(par1Str);
    }

    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            Minecraft minecraft = Minecraft.getMinecraft();
            
            int i = frameCounter;
            if (minecraft.theWorld != null && minecraft.thePlayer != null) {
            	i = (int) ((minecraft.theWorld.getWorldTime() % WildlifeEventHandler.yearLength)/(WildlifeEventHandler.yearLength/4));
            	i*=2;
            	if(minecraft.theWorld.provider.isHellWorld) {
            		i++;
            	}
            }
            if (i != this.frameCounter) {
	            //System.out.println("Moon: " + i + "," + minecraft.theWorld.getCurrentMoonPhaseFactor());
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
