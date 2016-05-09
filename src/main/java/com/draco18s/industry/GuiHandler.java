package com.draco18s.industry;

import com.draco18s.industry.client.GuiContainerFilter;
import com.draco18s.industry.client.GuiContainerWoodenHopper;
import com.draco18s.industry.entities.TileEntityFilter;
import com.draco18s.industry.entities.TileEntityWoodHopper;
import com.draco18s.industry.inventory.ContainerFilter;
import com.draco18s.industry.inventory.ContainerWoodenHopper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("This occurrs (client)" + id);
		if(id == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityWoodHopper){
				return new ContainerWoodenHopper(player.inventory, (TileEntityWoodHopper) tileEntity);
			}
		}
		if(id == 1) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityFilter){
				return new ContainerFilter(player.inventory, (TileEntityFilter) tileEntity);
			}
		}
		return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("This occurrs (server)" + id);
    	if(id == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityWoodHopper){
				return new GuiContainerWoodenHopper(player.inventory, (TileEntityWoodHopper) tileEntity);
			}
    	}
    	if(id == 1) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityFilter){
				return new GuiContainerFilter(player.inventory, (TileEntityFilter) tileEntity);
			}
    	}
		return null;
    }
}
