package com.draco18s.hardlib.client;

import java.lang.reflect.Method;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import com.draco18s.hardlib.CommonProxy;

public class ClientProxy extends CommonProxy {
	public void init() {
		Class clz = TextureMap.class;
        Method[] meths = clz.getDeclaredMethods();
        for(Method m : meths) {
        	if(m.getReturnType() == ResourceLocation.class) {
        		m.setAccessible(true);
        		resourceLocation = m;
        	}
        }
	}
	
	public void throwException() {
		throw new CogMissingException();
	}
}
