package com.draco18s.industry.block;

import java.util.List;

import net.minecraft.block.BlockRailDetector;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTypeRail extends BlockRailDetector {

	public BlockTypeRail() {
		super();
        this.setTickRandomly(true);
        this.setBlockName("type_rail");
        this.setBlockTextureName("industry:typerail");
	}
	
	/*public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
        return (p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_) & 8) != 0 ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
        return (p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_) & 8) == 0 ? 0 : (p_149748_5_ == 1 ? 15 : 0);
    }*/

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        int best = 0;
		if ((world.getBlockMetadata(x, y, z) & 8) > 0) {
            float f = 0.125F;
            List list1 = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f)));
            //List list1 = world.selectEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f)), IEntitySelector.selectAnything);
            if (list1.size() > 0) {
            	int curr = 0;
            	for(Object o : list1) {
	        		String n = ((EntityMinecart)o).getCartItem().getDisplayName().toLowerCase();
	                //return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
	            	TileEntitySign te = (TileEntitySign)world.getTileEntity(x, y+1, z);
	            	//System.out.println("y+0: " + te);
	            	if(te != null) {
	            		//if(n.equals("empty")) { n = "minecart"; }
	        			//if(n.equals("command")) { n = "commandblock"; }
	            		int strength = 0;
	            		for(String s : te.signText) {
	            			if(s.length() > 0 && s.indexOf('=') > 0) {
	            				curr = parse(s, "=", n);
	            			}
	            			else if(s.length() > 0 && s.indexOf(':') > 0) {
	            				curr = parse(s, ":", n);
	            			}
	            			if(curr > best)
	            				best = curr;
	            		}
	            	}
	        		te = (TileEntitySign)world.getTileEntity(x, y-2, z);
	            	//System.out.println("y-2: " + te);
	            	if(te != null) {
	            		int strength = 0;
	            		for(String s : te.signText) {
	            			if(s.length() > 0 && s.indexOf('=') > 0) {
	            				curr = parse(s, "=", n);
	            			}
	            			else if(s.length() > 0 && s.indexOf(':') > 0) {
	            				curr = parse(s, ":", n);
	            			}
	            			if(curr > best)
	            				best = curr;
	            		}
	            	}
            	}
            }
        }

        return best;
    }
	
	private int parse(String input, String delimeter, String name) {
		String[] s2 = input.split(delimeter);
		if(s2[0].equals(StatCollector.translateToLocal("config.empty"))) { s2[0] = StatCollector.translateToLocal("item.minecart.name"); }
		if(s2[0].equals(StatCollector.translateToLocal("config.command"))) { s2[0] = StatCollector.translateToLocal("tile.commandBlock.name"); }
		if(name.endsWith(s2[0].toLowerCase())) {
			return Integer.parseInt(s2[1]);
		}
		return 0;
	}
}
