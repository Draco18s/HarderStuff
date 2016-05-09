package com.draco18s.flowers.item;

import java.util.List;

import com.draco18s.flowers.OreFlowersBase;
import com.draco18s.flowers.block.BlockOreFlower;

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

public class ItemBlockOreFlower extends ItemBlock {
	//private IIcon[] icons;
	public static final String[] names = new String[] {"poorjoe","horsetail","vallozia","flame_lily",
														"tansy","hauman","leadplant","primrose"};
	private String[] flowerDescript = {"indicator.iron","indicator.gold","indicator.diamond","indicator.redstone",
									"indicator.tin","indicator.copper","indicator.lead","indicator.uranium"};

	public ItemBlockOreFlower(Block b) {
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
