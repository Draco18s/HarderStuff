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

public class ItemCrushOre extends Item {
	@SideOnly(Side.CLIENT)
    private IIcon[] icons;
    
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
    public String getUnlocalizedName(ItemStack stack)
    {
    	switch(stack.getItemDamage()) {
	    	case 0:
	    		return "item.small_crush_iron";
	    	case 1:
	    		return "item.small_crush_gold";
	    	case 2:
	    		return "item.small_crush_diamond";
	    	case 3:
	    		return "item.small_crush_flour";
	    	case 4:
	    		return "item.small_crush_sugar";
	    	case 5:
	    		return "item.small_crush_limonite";
	    	case 6:
	    		return "item.small_crush_tin";
	    	case 7:
	    		return "item.small_crush_copper";
	    	case 8:
	    		return "item.small_crush_lead";
	    	case 9:
	    		return "item.small_crush_uranium";
    	}
        return "item.small_crushed_ore";
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        //list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 6));
        list.add(new ItemStack(item, 1, 7));
        list.add(new ItemStack(item, 1, 8));
        list.add(new ItemStack(item, 1, 9));
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
    	icons[0] = iconRegister.registerIcon("ores:ironcrush_sm");
        icons[1] = iconRegister.registerIcon("ores:goldcrush_sm");
        //icons[2] = iconRegister.registerIcon("ores:diamondcrush_sm");
        //icons[3] = iconRegister.registerIcon("ores:flourcrush_sm");
    	icons[6] = iconRegister.registerIcon("ores:tincrush_sm");
    	icons[7] = iconRegister.registerIcon("ores:coppercrush_sm");
    	icons[8] = iconRegister.registerIcon("ores:leadcrush_sm");
    	icons[9] = iconRegister.registerIcon("ores:uraniumcrush_sm");
        
    }
}
