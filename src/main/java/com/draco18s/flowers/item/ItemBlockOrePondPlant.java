package com.draco18s.flowers.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBlockOrePondPlant extends ItemBlock {
	public static final String[] names = new String[] {"duckweed","unknown","unknown","unknown",
														"unknown","unknown","unknown","unknown"};
	private String[] flowerDescript = {"indicator.copper","indicator.unknown","indicator.unknown","indicator.unknown",
										"indicator.unknown","indicator.unknown","indicator.unknown","indicator.unknown"};

	public ItemBlockOrePondPlant(Block block) {
        super(block);
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
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        
        if (movingobjectposition == null) {
            return stack;
        }
        else {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k)) {
                    return stack;
                }

                if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                    return stack;
                }
                if (world.getBlock(i, j, k).getMaterial() == Material.water && world.getBlockMetadata(i, j, k) == 0 && world.isAirBlock(i, j + 1, k)) {
                    // special case for handling block placement with water lilies
                    net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, i, j + 1, k);
                    world.setBlock(i, j + 1, k, this.field_150939_a, stack.getItemDamage(), 3);
                    if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(player, blocksnapshot, net.minecraftforge.common.util.ForgeDirection.UP).isCanceled()) {
                        blocksnapshot.restore(true, false);
                        return stack;
                    }

                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }
                }
            }

            return stack;
        }
    }
	
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) { 
		onItemRightClick(stack, world, player);
		return false;
	}
}
