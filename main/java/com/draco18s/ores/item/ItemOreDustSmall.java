package com.draco18s.ores.item;

import java.util.List;

import com.draco18s.ores.recipes.IC2Integration;

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
    private IIcon[] icons;
    
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
    	switch(stack.getItemDamage()) {
	    	case 0:
	    		return "item.small_iron_dust";
	    	case 1:
	    		return "item.small_gold_dust";
	    	case 2:
	    		return "item.small_diamond_dust";//non-existent.  placeholder to match metadata across several items
	    	case 3:
	    		return "item.small_flour_dust";
	    	case 4:
	    		return "item.small_sugar_dust";
	    	case 5:
	    		return "item.small_limonite_dust";//non-existent.  placeholder to match metadata across several items
	    	case 6:
	    		return "item.small_tin_dust";//implemented by IC2, placeholder
	    	case 7:
	    		return "item.small_copper_dust";//implemented by IC2, placeholder
	    	case 8:
	    		return "item.small_lead_dust";//implemented by IC2, placeholder
	    	case 9:
	    		return "item.small_uranium_dust";//implemented by IC2, placeholder
    	}
        return "item.small_dust";
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
        list.add(new ItemStack(item, 1, 4));
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
    	icons = new IIcon[5];
    	icons[0] = iconRegister.registerIcon("ores:irondust_sm");
        icons[1] = iconRegister.registerIcon("ores:golddust_sm");
        //icons[2] = iconRegister.registerIcon("ores:diamonddust_sm");
        icons[3] = iconRegister.registerIcon("ores:flour_sm");
        icons[4] = iconRegister.registerIcon("ores:sugar_sm");
        //icons[5] = iconRegister.registerIcon("ores:limonite_sm");
    	/*icons[6] = iconRegister.registerIcon("ores:tin_sm");
    	icons[7] = iconRegister.registerIcon("ores:copper_sm");
    	icons[8] = iconRegister.registerIcon("ores:lead_sm");
    	icons[9] = iconRegister.registerIcon("ores:uranium_sm");*/
    }
}
