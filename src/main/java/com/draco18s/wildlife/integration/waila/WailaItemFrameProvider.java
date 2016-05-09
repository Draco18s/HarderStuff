package com.draco18s.wildlife.integration.waila;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
//import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.ICropDataSupplier;
import com.draco18s.hardlib.api.internal.CropWeatherOffsets;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.WildlifeEventHandler;
//import mcp.mobius.waila.utils.WailaExceptionHandler;

public class WailaItemFrameProvider implements IWailaEntityProvider {
	private static boolean useMetric;
	//private static int dayStartsMidnight;
	private Method me;
	private Method me2;
	/*private String deg = String.valueOf(0x00B0);
	private String cen = String.valueOf(0x339D);
	private String inc = String.valueOf(0x2033);*/
	private String deg, cen, inc;

	public WailaItemFrameProvider() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Class clz = Class.forName("mcp.mobius.waila.api.impl.ModuleRegistrar");
		me = clz.getDeclaredMethod("instance", new Class[]{});
		//me2 = clz.getDeclaredMethod("getHeadProviders", Block.class);
		for(Method ff : clz.getDeclaredMethods()) {
			if(ff.getName().equals("getHeadProviders")) {
				me2 = ff;
			}
		}

		int codePoint = 0x1D3C;//0x00B0;
		char[] charPair = Character.toChars(codePoint);
		deg = new String(charPair);
		codePoint = 0x339D;
		charPair = Character.toChars(codePoint);
		cen = new String(charPair);
		codePoint = 0x2033;
		charPair = Character.toChars(codePoint);
		inc = new String(charPair);
		useMetric = WildlifeBase.config.getBoolean("metricUnits", "SEASONS", false, "[WAILA] Set to true to use "+deg+"C and cm.  Temperatures are only approximately equivalent to the real world\nat 10"+deg+"F = 0.1"+deg+"MC");
		//dayStartsMidnight = WildlifeBase.config.getInt("dayStarts", "SEASONS", -6000, -12000, 12000, "[WAILA] Offset in ticks on when a new day starts. 0 = dawn.");
		WildlifeBase.config.save();
	}

	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		NBTTagCompound tag = new NBTTagCompound();
		entity.writeToNBT(tag);
		//currenttip.add("0 " + tag.func_150296_c().toArray()[0]);
		double dt;
		if(tag.hasKey("Item")) {
			NBTTagCompound t = tag.getCompoundTag("Item");
			if(t.hasKey("id")) {
				int id = t.getShort("id");
				Item it = Item.getItemById(id);
				if(it == WildlifeBase.thermometer) {
					int x, y, z;
					long lwt = WildlifeEventHandler.getLastWorldTime(entity.worldObj.provider.dimensionId);
					NBTTagCompound stackTagCompound = null;
					if(t.hasKey("tag")) {
						stackTagCompound = t.getCompoundTag("tag");
						/*x = stackTagCompound.getInteger("posX");
							y = stackTagCompound.getInteger("posY");
							z = stackTagCompound.getInteger("posZ");*/
						x = (int) entity.posX;
						y = (int) entity.posY;
						z = (int) entity.posZ;
					}
					else {
						x = (int) entity.posX;
						y = (int) entity.posY;
						z = (int) entity.posZ;
					}
					BiomeGenBase bio = entity.worldObj.getBiomeGenForCoords(x, z);
					float tt = bio.temperature;
					if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
						tt += WildlifeEventHandler.getSeasonTemp(lwt) * 0.333f;
					}
					if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != entity.worldObj.getPrecipitationHeight(x, z) > y) {
						tt = (tt + 0.8f)/2f;
					}

					float c = tt;
					//tt = 0f;
					if(stackTagCompound != null) {
						Block block = Block.getBlockById(stackTagCompound.getInteger("blockID"));
						CropWeatherOffsets offs = HardLibAPI.cropManager.getCropOffsets(block);
						if(block instanceof ICropDataSupplier && stackTagCompound.hasKey("HasOffsets")) {
							float tf = stackTagCompound.getFloat("tempflat");
							float rf = stackTagCompound.getFloat("tempflat");
							int ot = stackTagCompound.getInteger("raintime");
							int or = stackTagCompound.getInteger("temptime");
							offs = new CropWeatherOffsets(tf,rf,ot,or);
						}
						List<IWailaDataProvider> all;// = ModuleRegistrar.instance().getHeadProviders(block);
						List<String> name = new ArrayList<String>();
						ItemStack itemStack = new ItemStack(block);
						try {
							all = (List<IWailaDataProvider>) me2.invoke(me.invoke(null), block);
	
							if (block == Blocks.wheat){
								itemStack = new ItemStack(Items.wheat);
							}
							if (block == Blocks.pumpkin_stem){
								itemStack = new ItemStack(Blocks.pumpkin);
							}
							if (block == Blocks.melon_stem){
								itemStack = new ItemStack(Blocks.melon_block);
							}
							//name.add(block.getUnlocalizedName()+".name");
							//for (List<IWailaDataProvider> providersList : all.values()){			
							for (IWailaDataProvider dataProvider : all) {
								//try{				
								name = dataProvider.getWailaHead(itemStack, name, (IWailaDataAccessor) accessor, config);
								//} catch (Throwable e){
								//name.add("<ERROR>");
								//name = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), name);
								//}
							}
							//}
							if(name.size() == 0 || name.get(0).equals("<ERROR>")) {
								currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + itemStack.getDisplayName());
							}
							else {
								currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + name.get(0).substring(2));
							}
						}catch (Exception e) {
							currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + itemStack.getDisplayName());
						}
						//currenttip.add(StatCollector.translateToLocal(block.getUnlocalizedName()+".name"));
						dt = tt;
						if(useMetric) {
							dt = (dt-0.32) / 1.8; 
						}
						currenttip.add(StatCollector.translateToLocal("description.temperature") + ": " + Math.round(dt*50)*2 + deg);//actual degree symbol: Â° â„ƒâ„‰
						if(offs.tempTimeOffset != 0) {
							tt = tt - WildlifeEventHandler.getSeasonTemp(lwt) + WildlifeEventHandler.getSeasonTemp(lwt + offs.tempTimeOffset);
						}
						tt += offs.tempFlat;
						currenttip.add(StatCollector.translateToLocal("description.prefers") + ": " + (useMetric?(Math.round((0.8f - (tt - c) - 0.32)/1.8*40)*2.5):(Math.round((0.8f - (tt - c))*20)*5)) + deg);//actual degree symbol: Â°
					}
					else {
						dt = tt;
						if(useMetric) {
							dt = (dt-0.32) / 1.8; 
						}
						currenttip.add(StatCollector.translateToLocal("description.temperature") + ": " + Math.round(dt*50)*2 + deg);//actual degree symbol: Â°
					}
					//System.out.println(t + ":" + offs.tempFlat);
					//int n = Math.round((tt+0.3f) * 4);
					//n = Math.max(Math.min(n, 11),0);
				}
				else if(it == WildlifeBase.rainmeter) {
					int x, y, z;
					long lwt = WildlifeEventHandler.getLastWorldTime(entity.worldObj.provider.dimensionId);
					NBTTagCompound stackTagCompound = null;
					if(t.hasKey("tag")) {
						stackTagCompound = t.getCompoundTag("tag");
						/*x = stackTagCompound.getInteger("posX");
							y = stackTagCompound.getInteger("posY");
							z = stackTagCompound.getInteger("posZ");*/
						x = (int) entity.posX;
						y = (int) entity.posY;
						z = (int) entity.posZ;
					}
					else {
						x = (int) entity.posX;
						y = (int) entity.posY;
						z = (int) entity.posZ;
					}
					BiomeGenBase bio = entity.worldObj.getBiomeGenForCoords(x, z);
					float r = bio.rainfall;
					if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
						r += WildlifeEventHandler.getSeasonRain(lwt) * 0.333f;
					}
					if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != entity.worldObj.getPrecipitationHeight(x, z) > y) {
						r = (r + 1)/2f;
					}
					float c = r;
					if(stackTagCompound != null) {
						Block block = Block.getBlockById(stackTagCompound.getInteger("blockID"));
						CropWeatherOffsets offs = HardLibAPI.cropManager.getCropOffsets(block);
						if(block instanceof ICropDataSupplier && stackTagCompound.hasKey("HasOffsets")) {
							float tf = stackTagCompound.getFloat("tempflat");
							float rf = stackTagCompound.getFloat("tempflat");
							int ot = stackTagCompound.getInteger("raintime");
							int or = stackTagCompound.getInteger("temptime");
							offs = new CropWeatherOffsets(tf,rf,ot,or);
						}
						//Map<Integer, List<IWailaDataProvider>> all = ModuleRegistrar.instance().getHeadProviders(block);
						//Object arg = null;
						List<IWailaDataProvider> all;// = ModuleRegistrar.instance().getHeadProviders(block);
						List<String> name = new ArrayList<String>();
						ItemStack itemStack = new ItemStack(block);
						try {
							all = (List<IWailaDataProvider>) me2.invoke(me.invoke(null), block);
	
							if (block == Blocks.wheat){
								itemStack = new ItemStack(Items.wheat);
							}
							if (block == Blocks.pumpkin_stem){
								itemStack = new ItemStack(Blocks.pumpkin);
							}
							if (block == Blocks.melon_stem){
								itemStack = new ItemStack(Blocks.melon_block);
							}
							//name.add(block.getUnlocalizedName()+".name");
							//for (List<IWailaDataProvider> providersList : all.values()){			
							for (IWailaDataProvider dataProvider : all) {
								//try{				
								name = dataProvider.getWailaHead(itemStack, name, (IWailaDataAccessor) accessor, config);
								//} catch (Throwable e){
								//name.add("<ERROR>");
								//name = WailaExceptionHandler.handleErr(e, dataProvider.getClass().toString(), name);
								//}
							}
							//}
							if(name.size() == 0 || name.get(0) == "<ERROR>") {
								currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + itemStack.getDisplayName());
							}
							else {
								currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + name.get(0).substring(2));
							}
						}catch (Exception e) {
							currenttip.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("description.cropLink") + ": " + itemStack.getDisplayName());
						}
						//currenttip.add(StatCollector.translateToLocal(block.getUnlocalizedName()+".name"));
						dt = r+1;
						if(useMetric) {
							dt = dt / 0.39370;
						}
						currenttip.add(StatCollector.translateToLocal("description.rainfall") + ": " + String.format("%.2f", dt) + (useMetric?cen:inc));
						if(offs.rainTimeOffset != 0) {
							r = r - WildlifeEventHandler.getSeasonRain(lwt) + WildlifeEventHandler.getSeasonRain(lwt + offs.rainTimeOffset);
						}
						r += offs.rainFlat;
						dt = 1 - (r - c);
						dt += 1;
						if(useMetric) {
							dt = dt / 0.39370;
						}
						currenttip.add(StatCollector.translateToLocal("description.prefers") + ": " + String.format("%.2f", dt) + (useMetric?cen:inc));
					}
					else {
						dt = r+1;
						if(useMetric) {
							dt = dt / 0.39370;
						}
						currenttip.add(StatCollector.translateToLocal("description.rainfall") + ": " + String.format("%.2f", dt) + (useMetric?cen:inc));
					}
					//int n = Math.round((r) * 4);
					//n = Math.max(Math.min(n, 8),0);
				}
				else if(it == WildlifeBase.calendar) {
					currenttip.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("description.season"));
					int season = (int) ((entity.worldObj.getWorldTime() % WildlifeEventHandler.yearLength) / (WildlifeEventHandler.yearLength / 4));
					int seasonLength = (int)(WildlifeEventHandler.yearLength / 4);
					int day = (int) (entity.worldObj.getWorldTime() % seasonLength);
					if(entity.worldObj.provider.isHellWorld) {
						season += 4;
					}
					switch(season) {
						case 0:
							currenttip.add(StatCollector.translateToLocal("description.spring"));
							break;
						case 1:
							currenttip.add(StatCollector.translateToLocal("description.summer"));
							break;
						case 2:
							currenttip.add(StatCollector.translateToLocal("description.fall"));
							break;
						case 3:
							currenttip.add(StatCollector.translateToLocal("description.winter"));
							break;
						case 4:
							currenttip.add(StatCollector.translateToLocal("description.blaze"));
							break;
						case 5:
							currenttip.add(StatCollector.translateToLocal("description.fire	"));
							break;
						case 6:
							currenttip.add(StatCollector.translateToLocal("description.rod"));
							break;
						case 7:
							currenttip.add(StatCollector.translateToLocal("description.obsidian"));
							break;
					}
					int left = (int)Math.ceil((seasonLength-day+24000)/24000);
					if(left > 1) {
						currenttip.add((left) + " " + StatCollector.translateToLocal("description.daysleft"));
					}
					else {
						if(left == 1)
							currenttip.add((left) + " " + StatCollector.translateToLocal("description.dayleft"));
						else
							currenttip.add("<" + (left) + " " + StatCollector.translateToLocal("description.dayleft"));
						/*left = (int)Math.ceil((seasonLength-day)/1000);
						if(left > 1)
							currenttip.add(left + " " + StatCollector.translateToLocal("description.hoursleft"));
						else if(left == 1)
							currenttip.add(left + " " + StatCollector.translateToLocal("description.hourleft"));
						else
							currenttip.add("<" + left + " " + StatCollector.translateToLocal("description.hourleft"));*/
					}
				}
			}
		}
	return currenttip;
}

@Override
public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
	return currenttip;
}

@Override
public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
	return null;
}
}
