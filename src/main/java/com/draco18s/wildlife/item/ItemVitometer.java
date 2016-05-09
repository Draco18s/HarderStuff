package com.draco18s.wildlife.item;

import java.util.List;

import com.draco18s.wildlife.util.TreeDataHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class ItemVitometer extends Item {

	public ItemVitometer() {
		this.setUnlocalizedName("vitometer");
		this.setTextureName("wildlife:vitometer");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack s, World world, EntityPlayer p) {
		BiomeGenBase bio = world.getBiomeGenForCoords((int)p.posX, (int)p.posZ);
		IChatComponent chat = new ChatComponentText("Biome: " + bio.biomeName);
		p.addChatMessage(chat);

		chat = new ChatComponentText("Types: ");
		Type[] ty = BiomeDictionary.getTypesForBiome(bio);
		for(int i=ty.length-1; i >= 0; i--) {
			chat.appendSibling(new ChatComponentText(ty[i].name() + (i==0?"":", ")));
		}
		p.addChatMessage(chat);
		
		if(bio.biomeID >= 128) {
			chat = new ChatComponentText(EnumChatFormatting.ITALIC + "Biome is Mutant");
			p.addChatMessage(chat);
			chat = new ChatComponentText("   Class: "+bio.getClass().toString());
			p.addChatMessage(chat);
			chat = new ChatComponentText("   Biome class: "+bio.getBiomeClass().toString());
			p.addChatMessage(chat);
		}
		
		return s;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack s, EntityPlayer p, List l, boolean a) {
		l.add("Creative Only");
		l.add("Log various biome information.");
	}
}
