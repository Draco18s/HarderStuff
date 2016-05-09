package com.draco18s.ores.recipes.waila;

import java.util.List;

import com.draco18s.ores.entities.TileEntityWindmill;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class WailaWindmillProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if(accessor.getTileEntity() instanceof TileEntityWindmill) {
			TileEntityWindmill windMill = (TileEntityWindmill)accessor.getTileEntity();
			int pow = windMill.getPower();
			currenttip.add("Airflow: " + pow);
			if(pow > 0) {
				float t = (400f/TileEntityWindmill.powerScale(windMill, pow))/20f;
				currenttip.add(TileEntityWindmill.displayString(windMill) + " time: " + String.format("%.1f", t));
			}
			else {
				currenttip.add("Not enough power to grind");
			}
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
		return null;
	}

}
