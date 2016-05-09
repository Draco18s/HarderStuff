package com.draco18s.ores.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.draco18s.ores.recipes.IC2Integration;
import com.draco18s.ores.util.EnumOreType;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOreDustSmall extends Item {
	@SideOnly(Side.CLIENT)
    private Map<Integer,IIcon> icons;
    
	public ItemOreDustSmall() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack) {
    	EnumOreType v = EnumOreType.values()[stack.getItemDamage()];
    	switch(v) {
	    	case IRON:
	    	case GOLD:
	    	case FLOUR:
	    	case SUGAR:
	    	case TIN:
	    	case COPPER:
	    	case LEAD:
	    	case SILVER:
	    	case NICKEL:
	    	case ALUMINUM:
	    	case OSMIUM:
	    		return "item.small_"+v.name+"_dust";
	    	default:
	            return "item.unknown_small_dust";
    	}
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, EnumOreType.IRON.value));
        list.add(new ItemStack(item, 1, EnumOreType.GOLD.value));
        list.add(new ItemStack(item, 1, EnumOreType.FLOUR.value));
        list.add(new ItemStack(item, 1, EnumOreType.SUGAR.value));
        list.add(new ItemStack(item, 1, EnumOreType.TIN.value));
        list.add(new ItemStack(item, 1, EnumOreType.COPPER.value));
        list.add(new ItemStack(item, 1, EnumOreType.LEAD.value));
        list.add(new ItemStack(item, 1, EnumOreType.SILVER.value));
        list.add(new ItemStack(item, 1, EnumOreType.NICKEL.value));
        list.add(new ItemStack(item, 1, EnumOreType.ALUMINUM.value));
        list.add(new ItemStack(item, 1, EnumOreType.OSMIUM.value));
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icons.get(meta);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
    	icons = new HashMap<Integer,IIcon>();
    	EnumOreType[] v = EnumOreType.values();
    	for(int i=0; i < v.length; i++) {
    		switch(v[i]) {
		    	case IRON:
		    	case GOLD:
		    	case FLOUR:
		    	case SUGAR:
		    	case TIN:
		    	case COPPER:
		    	case LEAD:
		    	case SILVER:
		    	case NICKEL:
		    	case ALUMINUM:
    	    	case OSMIUM:
		    		icons.put(i,iconRegister.registerIcon("ores:"+v[i].name+"dust_sm"));
		    		break;
		    	default:
		            ;
    		}
    	}
    }
}
