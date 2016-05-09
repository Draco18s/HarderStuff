package com.draco18s.wildlife.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.hardlib.api.interfaces.ICropDataSupplier;
import com.draco18s.hardlib.api.internal.CropWeatherOffsets;
import com.draco18s.wildlife.WildlifeEventHandler;
import com.draco18s.wildlife.client.ClientProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRainMeter extends Item{

	public ItemRainMeter() {
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("rainmeter");
		this.setMaxDamage(0);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconReg) {
		itemIcon = ClientProxy.rainmeter;
		for(int f = 0; f < 8; f++) {
			ClientProxy.rainmeterNBT[f] = iconReg.registerIcon("wildlife:humid_"+f);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return ClientProxy.rainmeter;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
		if(stack.stackTagCompound != null) {
			//long time = stack.stackTagCompound.getLong("updateTime");
			World world = Minecraft.getMinecraft().theWorld;
			int dimID = world.provider.dimensionId;
			long lwt = WildlifeEventHandler.getLastWorldTime(dimID);
			/*if(Math.abs(time - lwt) < 1000) {
				return ClientProxy.rainmeterNBT[stack.stackTagCompound.getInteger("saved")];
			}*/
			
			stack.stackTagCompound.setLong("updateTime",lwt);
			int n = 0;
			//int x = stack.stackTagCompound.getInteger("posX");
			//int y = stack.stackTagCompound.getInteger("posY");
			//int z = stack.stackTagCompound.getInteger("posZ");
			
			//double dx = Minecraft.getMinecraft().thePlayer.posX-x;
			//double dz = Minecraft.getMinecraft().thePlayer.posZ-z;
			int x = (int) Minecraft.getMinecraft().thePlayer.posX;
			int y = (int) Minecraft.getMinecraft().thePlayer.posY;
			int z = (int) Minecraft.getMinecraft().thePlayer.posZ;
			
			/*if(dx*dx + dz*dz > 256) {
				stack.stackTagCompound = null;
				return getIconFromDamage(0);
			}*/
			
			BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
			float r = bio.rainfall;
			if(BiomeDictionary.isBiomeOfType(bio, Type.OCEAN) || BiomeDictionary.isBiomeOfType(bio, Type.RIVER)) {
				r += WildlifeEventHandler.getSeasonRain(lwt) * 0.333f;
			}
			if(BiomeDictionary.isBiomeOfType(bio, Type.NETHER) != world.getPrecipitationHeight(x, z) > y) {
				r = (r + 1)/2f;
			}

			Block block = Block.getBlockById(stack.stackTagCompound.getInteger("blockID"));
			CropWeatherOffsets offs = HardLibAPI.cropManager.getCropOffsets(block);
			if(block instanceof ICropDataSupplier) {
			}
			if(offs == null) {
				offs = new CropWeatherOffsets(0,0,0,0);
			}
			if(offs.rainTimeOffset != 0) {
				r = r - WildlifeEventHandler.getSeasonRain(lwt) + WildlifeEventHandler.getSeasonRain(lwt + offs.rainTimeOffset);
			}
			r += offs.rainFlat;
			//System.out.println(t + ":" + offs.tempFlat);
			n = Math.round((r-0.575f) * 8);
			n = 7 - Math.max(Math.min(n, 7),0);
			//System.out.println("Return icon " + n);
			stack.stackTagCompound.setInteger("saved",n);
			return ClientProxy.rainmeterNBT[n];
		}
		else {
			return getIconFromDamage(0);
		}
    }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int side, float hitx, float hity, float hitz) {
		Block b = p.worldObj.getBlock(x, y, z);
		if(HardLibAPI.cropManager.isCropBlock(b)) {
			NBTTagCompound tag = stack.stackTagCompound;
			if(tag == null) {
				tag = new NBTTagCompound();
			}
			//tag.setInteger("posX", x);
			//tag.setInteger("posY", y);
			//tag.setInteger("posZ", z);
			if(b instanceof IPlantable) {
				b = ((IPlantable)b).getPlant(world, x, y, z);
			}
			tag.setInteger("blockID", Block.getIdFromBlock(b));
			tag.setLong("updateTime",-1000);
			
			if(b instanceof ICropDataSupplier) {
				CropWeatherOffsets offs = ((ICropDataSupplier)b).getCropData(world, x, y, z);
				tag.setBoolean("HasOffsets", true);
				tag.setFloat("rainflat", offs.rainFlat);
				tag.setFloat("tempflat", offs.tempFlat);
				tag.setInteger("raintime", offs.rainTimeOffset);
				tag.setInteger("temptime", offs.tempTimeOffset);
			}
			
			Item item = Item.getItemFromBlock(b);
			//ItemStack itemStack = new ItemStack(block);
			if (b == Blocks.wheat){
				item = (Items.wheat);
			}
			if (b == Blocks.pumpkin_stem){
				item = Item.getItemFromBlock(Blocks.pumpkin);
			}
			if (b == Blocks.melon_stem){
				item = Item.getItemFromBlock(Blocks.melon_block);
			}
			if(b == Blocks.reeds) {
				item = Items.reeds;
			}
			tag.setString("linkedCropName", StatCollector.translateToLocal(item.getUnlocalizedName()+".name"));
			stack.stackTagCompound = tag;
		}
		else {
			stack.stackTagCompound = null;
		}
        return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
		NBTTagCompound tag = stack.stackTagCompound;
		if(tag != null) {
			tooltip.add(EnumChatFormatting.ITALIC + "Linked Crop: " + tag.getString("linkedCropName"));
			tooltip.add("Right click to unlink.");
		}
		else {
			tooltip.add("Right click on a crop to link.");
		}
	}
}
