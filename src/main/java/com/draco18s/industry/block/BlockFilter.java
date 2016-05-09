package com.draco18s.industry.block;

import java.util.Random;

import com.draco18s.industry.IndustryBase;
import com.draco18s.industry.entities.TileEntityFilter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFilter extends BlockHopper implements IHopperExtender {
	private static IIcon outsideIcon;
	private static IIcon insideIcon;
	private static IIcon topIcon;
	private static IIcon topOutIcon;
	private static IIcon bottomIcon;
	public int renderID;

	public BlockFilter() {
		super();
		setBlockName("filter");
		setHardness(2.0F);
		setResistance(4.0F);
		setStepSound(soundTypeMetal);
		setBlockTextureName("industry:filter");
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public int getRenderType() {
        return renderID;
    }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityFilter();
    }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(IndustryBase.instance, 1, world, x, y, z);
			return true;
		}
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? topIcon : topOutIcon;
    }

	@SideOnly(Side.CLIENT)
    public IIcon hopperIcon(String p_149916_0_) {
        return p_149916_0_.equals("hopper_outside") ? outsideIcon : (p_149916_0_.equals("hopper_inside") ? insideIcon : p_149916_0_.equals("hopper_bottom") ? bottomIcon : null);
    }
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
		int one = this.lightOpacity;
		outsideIcon = iconRegister.registerIcon("minecraft:hopper_outside");
        topIcon = iconRegister.registerIcon("minecraft:hopper_top");
        insideIcon = iconRegister.registerIcon("minecraft:hopper_inside");
        bottomIcon = iconRegister.registerIcon("minecraft:gold_block");
        topOutIcon = iconRegister.registerIcon("industry:loader_topoutside_gold");
    }

	@Override
	@SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return "industry:filter";
    }
}
