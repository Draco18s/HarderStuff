package com.draco18s.hardlib.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.imageio.ImageIO;

import com.draco18s.hardlib.HardLib;
import com.draco18s.hazards.UndergroundBase;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasDynamic extends TextureAtlasSprite {
	protected String textureBase;
	protected String textureOverlay;
	protected Color colorMulti;
	protected boolean invert;
	//protected DynamicTexture icon;

	public TextureAtlasDynamic(String p_i1282_1_, String base, String overlay, Color color, boolean inv) {
		super(p_i1282_1_);
		textureBase = base;
		textureOverlay = overlay;
		colorMulti = color;
		invert = inv;
	}

	@Override
	public void updateAnimation() {
		TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(0), this.width, this.height, this.originX, this.originY, false, false);
	}
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }
	
	@Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
		//System.out.println("======Loading Texture Data======");
		//System.out.println("Overlay: " + textureOverlay);
		framesTextureData.clear();
        //this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
        
        //int [] rawTexture = icon.getTextureData();
        
        //IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
    	ResourceLocation resource1 = new ResourceLocation(textureBase);
    	ResourceLocation resource2 = new ResourceLocation(textureOverlay);
        try {
        	TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        	resource1 = this.completeResourceLocation(map, resource1, 0);
        	resource2 = this.completeResourceLocation(map, resource2, 0);
			BufferedImage buff = ImageIO.read(manager.getResource(resource1).getInputStream());
			BufferedImage buff2 = ImageIO.read(manager.getResource(resource2).getInputStream());
			
	        int[] rawBase = new int[buff.getWidth()*buff.getHeight()];
	        int[] rawOverlay = new int[buff2.getWidth()*buff2.getHeight()];
			
	        buff.getRGB(0, 0, buff.getWidth(), buff.getHeight(), rawBase, 0, buff.getWidth());
	        buff2.getRGB(0, 0, buff2.getWidth(), buff2.getHeight(), rawOverlay, 0, buff2.getWidth());
	        
	        int min = Math.min(buff.getWidth(),buff2.getWidth());
	        rawBase = scaleToSmaller(rawBase, buff.getWidth(), min);
	        rawOverlay = scaleToSmaller(rawOverlay, buff2.getWidth(), min);
			
	        width = min;
	        height = min;
	        
	        int r1,g1,b1;
	        int r2,g2,b2;
	        float a1,a2,a3;
	        for(int p=0; p<rawBase.length;p++) {
	        	Color c1 = new Color(rawBase[p],true);
	        	Color c2 = new Color(rawOverlay[p],true);
	        	
	        	c2 = colorize(colorMulti,c2,invert);
	        	
	        	a1 = c1.getAlpha()/255f;
	        	r1 = c1.getRed();
	        	g1 = c1.getGreen();
	        	b1 = c1.getBlue();
	        	a2 = c2.getAlpha()/255f;
	        	r2 = c2.getRed();
	        	g2 = c2.getGreen();
	        	b2 = c2.getBlue();
	        	a3 = a2+a1*(1-a2);
	        	
	        	if(a3 > 0) {
		        	r1 = Math.round(((r2*a2)+(r1*a1*(1-a2)))/a3);
		        	g1 = Math.round(((g2*a2)+(g1*a1*(1-a2)))/a3);
		        	b1 = Math.round(((b2*a2)+(b1*a1*(1-a2)))/a3);
	        	}
	        	else {
	        		r1 = g1 = b1 = 0;
	        	}
	        	Color c3 = new Color(r1,g1,b1,Math.round(a3*255));
	        	rawBase[p] = c3.getRGB();
	        	//rawBase[p] = (rawBase[p] + rawOverlay[p])/2;
	        }
	        
	        int[][] aint = new int[1 + MathHelper.calculateLogBaseTwo(min)][];
	        for (int k = 0; k < aint.length; ++k)
	        {
	        	aint[k] = rawBase;
	        }

	        this.framesTextureData.add(aint);
		} catch (IOException e) {
			//e.printStackTrace();
			UndergroundBase.logger.error("Using missing texture, unable to load " + resource2, e);
			int[][] aint = new int[1][];
	        for (int k = 0; k < 1; ++k)
	        {
	        	aint[k] = new int[16*16];
	        	for(int l=0; l<aint[k].length;l++) {
	        		aint[k][l] = 0xFFFFFFFF;
	        	}
	        }
	        width = 16;
	        height = 16;

	        this.framesTextureData.add(aint);
		}
        return false;
    }

	private static int[] scaleToSmaller(int[] data, int width, int min) {
		if(width == min) { return data; }
		int scale = width / min;
		int[] output = new int[min*min];
		int j = 0;
		for(int i = 0; i < output.length; i++) {
			if(i%min == 0) {
				j += width;
			}
			output[i] = data[i*scale+j];
		}
		return output;
	}

	private static ResourceLocation completeResourceLocation(TextureMap map, ResourceLocation loc, int c)
    {
        try {
			return (ResourceLocation)HardLib.proxy.resourceLocation.invoke(map, loc, c);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
        return null;
    }

    private static Color colorize(Color cm, Color c2, boolean invert) {
    	float[] pix = new float[3];
    	float[] mul = new float[3];
    	Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(),pix);
    	Color.RGBtoHSB(cm.getRed(), cm.getGreen(), cm.getBlue(), mul);
    	float v = (invert?1-pix[2]:pix[2]);// + mul[2];
    	//v /= 2;
    	Color c3 = new Color(Color.HSBtoRGB(mul[0], mul[1], v));
    	return new Color(c3.getRed(),c3.getGreen(),c3.getBlue(),c2.getAlpha());
    }
}
