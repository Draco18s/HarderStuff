package com.draco18s.industry.block;

import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.draco18s.industry.IndustryBase;
import com.draco18s.industry.entities.TileEntityWoodHopper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWoodHopper extends BlockHopper implements IHopperExtender {
	private static IIcon outsideIcon;
	private static IIcon insideIcon;
	private static IIcon topIcon;
	public int renderID;

	public BlockWoodHopper() {
		super();
		setBlockName("wooden_hopper");
		setHardness(2.0F);
		setResistance(4.0F);
		setStepSound(soundTypeWood);
		setBlockTextureName("industry:hopper");
		setHarvestLevel("axe", 0);
	}
	
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityWoodHopper();
    }
	
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
        return 0;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(IndustryBase.instance, 0, world, x, y, z);
			return true;
		}
	}/**/

	@Override
	public int getRenderType() {
        return renderID;
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_*1 == 1 ? topIcon : outsideIcon;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon hopperIcon(String p_149916_0_) {
        return p_149916_0_.equals("hopper_outside") ? outsideIcon : (p_149916_0_.equals("hopper_inside") ? insideIcon : outsideIcon);
    }
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
		outsideIcon = p_149651_1_.registerIcon("minecraft:planks_oak");
        topIcon = p_149651_1_.registerIcon("industry:hopper_top");
        insideIcon = p_149651_1_.registerIcon("industry:hopper_inside");
    }
	
	@SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return "industry:hopper";
    }
}
