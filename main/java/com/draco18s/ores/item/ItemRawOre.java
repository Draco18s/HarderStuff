package com.draco18s.ores.item;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRawOre extends Item {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    
	public ItemRawOre() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack) {
    	switch(stack.getItemDamage()) {
	    	case 0:
	    		return "item.iron_ore";
	    	case 1:
	    		return "item.gold_ore";
	    	case 2:
	    		return "item.diamond_ore";
	    	case 3:
	    		return "item.flour_ore";//non-existent.  placeholder to match metadata across several items
	    	case 4:
	    		return "item.sugar_ore";//non-existent.  placeholder to match metadata across several items
	    	case 5:
	    		return "item.limonite_ore";
	    	case 6:
	    		return "item.tin_ore";
	    	case 7:
	    		return "item.copper_ore";
	    	case 8:
	    		return "item.lead_ore";
	    	case 9:
	    		return "item.uranium_ore";
    	}
        return "item.raw_ore";
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 5));
        if(Loader.isModLoaded("IC2")){
            list.add(new ItemStack(item, 1, 6));
            list.add(new ItemStack(item, 1, 7));
            list.add(new ItemStack(item, 1, 8));
            list.add(new ItemStack(item, 1, 9));
    	}
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
    	if(meta > icons.length) {
    		return icons[0];
    	}
        return icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
    	icons = new IIcon[10];
    	icons[0] = iconRegister.registerIcon("ores:ironchunk");
        icons[1] = iconRegister.registerIcon("ores:goldchunk");
        icons[2] = iconRegister.registerIcon("ores:diamondchunk");
        //icons[3] = iconRegister.registerIcon("ores:flourchunk");
    	//icons[4] = iconRegister.registerIcon("ores:sugarchunk");
        icons[5] = iconRegister.registerIcon("ores:limonitechunk");
    	icons[6] = iconRegister.registerIcon("ores:tinchunk");
    	icons[7] = iconRegister.registerIcon("ores:copperchunk");
    	icons[8] = iconRegister.registerIcon("ores:leadchunk");
    	icons[9] = iconRegister.registerIcon("ores:uraniumchunk");
    }
}
