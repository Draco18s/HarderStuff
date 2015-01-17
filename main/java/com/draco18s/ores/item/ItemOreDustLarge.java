package com.draco18s.ores.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOreDustLarge extends Item {
	@SideOnly(Side.CLIENT)
    private IIcon[] icons;
    
	public ItemOreDustLarge() {
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
	    		return "item.large_iron_dust";
	    	case 1:
	    		return "item.large_gold_dust";
	    	case 2:
	    		return "item.large_diamond_dust";//non-existent.  placeholder to match metadata across several items
	    	case 3:
	    		return "item.flour_dust";
	    	case 4:
	    		return "item.sugar_dust";//non-existent.  placeholder to match metadata across several items
	    	case 5:
	    		return "item.large_limonite_dust";//non-existent.  placeholder to match metadata across several items
	    	case 6:
	    		return "item.large_tin_dust";//implemented by IC2, placeholder
	    	case 7:
	    		return "item.large_copper_dust";//implemented by IC2, placeholder
	    	case 8:
	    		return "item.large_lead_dust";//implemented by IC2, placeholder
	    	case 9:
	    		return "item.large_uranium_dust";//implemented by IC2, placeholder
    	}
        return "item.large_dust";
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        //list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        /*if(Loader.isModLoaded("IC2")){
            list.add(new ItemStack(item, 1, 6));
            list.add(new ItemStack(item, 1, 7));
            list.add(new ItemStack(item, 1, 8));
            list.add(new ItemStack(item, 1, 9));
    	}*/
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
    	icons = new IIcon[4];
    	icons[0] = iconRegister.registerIcon("ores:irondust_lg");
        icons[1] = iconRegister.registerIcon("ores:golddust_lg");
        //icons[2] = iconRegister.registerIcon("ores:diamonddust_lg");
        icons[3] = iconRegister.registerIcon("ores:flour_lg");
        //icons[4] = iconRegister.registerIcon("ores:sugart_lg");
        //icons[5] = iconRegister.registerIcon("ores:limonite_lg");
    	/*icons[6] = iconRegister.registerIcon("ores:tin_lg");
    	icons[7] = iconRegister.registerIcon("ores:copper_lg");
    	icons[8] = iconRegister.registerIcon("ores:lead_lg");
    	icons[9] = iconRegister.registerIcon("ores:uranium_lg");*/
    }
}
