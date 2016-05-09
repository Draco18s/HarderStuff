package com.draco18s.ores;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.draco18s.ores.client.GuiContainerProcessor;
import com.draco18s.ores.client.GuiContainerSifter;
import com.draco18s.ores.client.GuiContainerSluice;
import com.draco18s.ores.client.GuiContainerSluiceB;
import com.draco18s.ores.entities.TileEntityOreProcessor;
import com.draco18s.ores.entities.TileEntitySifter;
import com.draco18s.ores.entities.TileEntitySluice;
import com.draco18s.ores.entities.TileEntitySluiceBottom;
import com.draco18s.ores.inventory.ContainerProcessor;
import com.draco18s.ores.inventory.ContainerSifter;
import com.draco18s.ores.inventory.ContainerSluice;
import com.draco18s.ores.inventory.ContainerSluiceB;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("This occurrs (client)" + id);
		if(id == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntitySifter){
				return new ContainerSifter(player.inventory, (TileEntitySifter) tileEntity);
			}
		}
		else if(id == 1) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntitySluice){
				return new ContainerSluice(player.inventory, (TileEntitySluice) tileEntity);
			}
		}
		else if(id == 2) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntitySluiceBottom){
				return new ContainerSluiceB(player.inventory, (TileEntitySluiceBottom) tileEntity);
			}
		}
		else if(id == 3) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityOreProcessor){
				return new ContainerProcessor(player.inventory, (TileEntityOreProcessor) tileEntity);
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
			if(tileEntity instanceof TileEntitySifter){
				return new GuiContainerSifter(player.inventory, (TileEntitySifter) tileEntity);
			}
    	}
    	else if(id == 1) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntitySluice){
				return new GuiContainerSluice(player.inventory, (TileEntitySluice) tileEntity);
			}
    	}
    	else if(id == 2) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntitySluiceBottom){
				return new GuiContainerSluiceB(player.inventory, (TileEntitySluiceBottom) tileEntity);
			}
    	}
		else if(id == 3) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityOreProcessor){
				return new GuiContainerProcessor(player.inventory, (TileEntityOreProcessor) tileEntity);
			}
		}
		return null;
    }
}
