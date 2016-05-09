package com.draco18s.ores.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.draco18s.ores.util.EnumOreType;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrushOre extends Item {
	@SideOnly(Side.CLIENT)
    private Map<Integer,IIcon> icons;
    
	public ItemCrushOre()
    {
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
	    	case TIN:
	    	case COPPER:
	    	case LEAD:
	    	case URANIUM:
	    	case SILVER:
	    		return "item.small_crush_"+v.name;
	    	default :
	            return "item.unknown_small_crushed_ore";
    	}
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(item, 1, EnumOreType.IRON.value));
        list.add(new ItemStack(item, 1, EnumOreType.GOLD.value));
        list.add(new ItemStack(item, 1, EnumOreType.TIN.value));
        list.add(new ItemStack(item, 1, EnumOreType.COPPER.value));
        list.add(new ItemStack(item, 1, EnumOreType.LEAD.value));
        list.add(new ItemStack(item, 1, EnumOreType.URANIUM.value));
        list.add(new ItemStack(item, 1, EnumOreType.SILVER.value));
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
		    	case TIN:
		    	case COPPER:
		    	case LEAD:
		    	case URANIUM:
		    	case SILVER:
		    		icons.put(i,iconRegister.registerIcon("ores:"+v[i].name+"crush_sm"));
	    		default:
	    			;
    		}
    	}
    }
}
