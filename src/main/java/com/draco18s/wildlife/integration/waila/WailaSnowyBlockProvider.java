package com.draco18s.wildlife.integration.waila;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.draco18s.wildlife.entity.TileEntityGrassSnow;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class WailaSnowyBlockProvider implements IWailaDataProvider {
	private Method me;
	private Method me2;
	
	public WailaSnowyBlockProvider() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		Class clz = Class.forName("mcp.mobius.waila.api.impl.ModuleRegistrar");
		me = clz.getDeclaredMethod("instance", new Class[]{});
		//me2 = clz.getDeclaredMethod("getHeadProviders", Block.class);
		for(Method ff : clz.getDeclaredMethods()) {
			if(ff.getName().equals("getHeadProviders")) {
				me2 = ff;
			}
		}
	}
	
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
		if(accessor.getTileEntity() instanceof TileEntityGrassSnow) {
			TileEntityGrassSnow sg = (TileEntityGrassSnow)accessor.getTileEntity();
			List<String> name = new ArrayList<String>();
			ItemStack stack = new ItemStack(sg.oBlock, 1, sg.oMeta);
			try {
				List<IWailaDataProvider> all;// = ModuleRegistrar.instance().getHeadProviders(block);
				all = (List<IWailaDataProvider>) me2.invoke(me.invoke(null), sg.oBlock);
				for (IWailaDataProvider dataProvider : all) {
					name = dataProvider.getWailaHead(stack, name, (IWailaDataAccessor) accessor, config);
				}
				if(name.size() == 0 || name.get(0) == "<ERROR>") {
					currenttip.add("Snow-covered " + stack.getDisplayName());
				}
				else {
					currenttip.add("Snow-covered " + name.get(0));
				}
			}
			catch(Exception ex) {
				currenttip.add("Snow-covered " + stack.getDisplayName());
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
