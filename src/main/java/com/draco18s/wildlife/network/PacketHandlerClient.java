package com.draco18s.wildlife.network;

import com.draco18s.wildlife.entity.CowStats;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerClient implements IMessageHandler<StoCMessage,IMessage> {
	@Override
	public IMessage onMessage(StoCMessage packet, MessageContext ctx) {
		ByteBuf buff = Unpooled.wrappedBuffer(packet.getData());
		
		//try
		{
			int amt = buff.readInt();
			int uuid = packet.getUUID();
			Entity ent = Minecraft.getMinecraft().theWorld.getEntityByID(uuid);
			
			if(ent instanceof EntityCow) {
				EntityCow cow = (EntityCow)ent;
				CowStats stats = CowStats.get(cow);
				stats.milkLevel = amt;
			}
		}
		
		return null;
	}
}
