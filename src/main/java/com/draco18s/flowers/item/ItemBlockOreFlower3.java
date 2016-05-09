package com.draco18s.flowers.item;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemBlockOreFlower3 extends ItemBlock {
	private IIcon[] icons;
	public static final String[] names = new String[] {"broadleaf-arrowhead","N/A","N/A","N/A",
														"N/A","N/A","N/A","N/A"};
	private String[] flowerDescript = {"indicator.osmium","N/A","N/A","N/A",
										"N/A","N/A","N/A","N/A"};

	public ItemBlockOreFlower3(Block b) {
		super(b);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		//if(stack.getItemDamage() < 6 || Loader.isModLoaded("IC2")) {
			list.add(StatCollector.translateToLocal(flowerDescript[stack.getItemDamage()&7]));
		//}
	}

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.field_150939_a.getIcon(2, meta);
    }

	@Override
    public int getMetadata(int meta) {
        return meta;
    }
    
	@Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + names[itemStack.getItemDamage()&7];
    }
}
