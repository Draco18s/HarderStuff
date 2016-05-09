package com.draco18s.hazards.item;

import java.lang.reflect.Field;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemEngGoggles extends ItemArmor {
    
	public ItemEngGoggles() {
		super(ArmorMaterial.CLOTH, 0, 0);
		setUnlocalizedName("engineer_goggles");
		setTextureName("hazards:engineer_goggles");
		//damageReduceAmount = 0;
		/*Class clz = this.getClass();
		Field fld;
		try {//field_77879_b
			fld = clz.getField("field_77879_b");
			fld.setAccessible(true);
			fld.set(this, 0);
		} catch (NoSuchFieldException e) {
			try {
				fld = clz.getField("damageReduceAmount");
				fld.setAccessible(true);
				fld.set(this, 1);
			} catch (NoSuchFieldException e1) {
			} catch (SecurityException e1) {
			} catch (IllegalArgumentException e1) {
			} catch (IllegalAccessException e1) {
			}
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}*/
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        this.itemIcon = p_94581_1_.registerIcon(this.getIconString());
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return "hazards:textures/models/armor/goggles.png";
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("item.goggles.description1"));
		par3List.add(StatCollector.translateToLocal("item.goggles.description2"));
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) { 
		return false;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.itemIcon;
	}
}
