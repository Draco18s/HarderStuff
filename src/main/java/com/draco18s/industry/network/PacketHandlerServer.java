package com.draco18s.industry.network;

import java.util.List;
import java.util.UUID;

import com.draco18s.industry.entities.TileEntityFilter;
import com.draco18s.industry.entities.TileEntityFilter.EnumAcceptType;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerServer implements IMessageHandler<CtoSMessage,IMessage> {

	public PacketHandlerServer() { }

	@Override
	public IMessage onMessage(CtoSMessage packet, MessageContext context) {
		//System.out.println("Packet recieved");
		try {
			World world = DimensionManager.getWorld(packet.dim());
			
			TileEntity te = world.getTileEntity(packet.x(), packet.y(), packet.z());
			if(te instanceof TileEntityFilter) {
				//System.out.println("TE updated");
				((TileEntityFilter)te).setEnumType(EnumAcceptType.values()[packet.ordinal()]);
			}
			else {
				//System.out.println("No TE!?");
			}
			
		}
		catch(Exception e) {
			//System.out.println("Something bad :(");
		}
		return null;
	}

}
