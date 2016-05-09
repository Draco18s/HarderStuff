package com.draco18s.hazards.network;

import java.util.List;
import java.util.UUID;

import com.draco18s.hazards.HazardsEventHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerServer implements IMessageHandler<CtoSMessage,IMessage> {

	public PacketHandlerServer() { }

	@Override
	public IMessage onMessage(CtoSMessage packet, MessageContext context) {
		ByteBuf buff = Unpooled.wrappedBuffer(packet.getData());
		
		//try {
			boolean keyUpdown = buff.readBoolean();
			int uuid = packet.getUUID();
			EntityPlayerMP p = null;
			List<EntityPlayerMP> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			for(EntityPlayerMP entityPlayer : playerList) {
				if(entityPlayer.getEntityId() == uuid) {
					p = entityPlayer;
					HazardsEventHandler.instance.setPlayerSwimming(p, keyUpdown);
					break;
				}
				/*if(entityPlayer.getUniqueID().equals(uuid)) {
					p = entityPlayer;
					//System.out.println("Effected player with UUID " + entityPlayer.getUniqueID().toString());
					HazardsEventHandler.instance.setPlayerSwimming(p, keyUpdown);
					break;
				}
				else {
					//System.out.println("Player with UUID " + entityPlayer.getUniqueID().toString() + " is connected.");
				}*/
			}
			/*if(p == null) {
				System.out.println("Couldn't find a player with UUID " + uuid);
				return null;
			}*/
		//}
		//catch(Exception e) {
			
		//}

		return null;
	}

}
