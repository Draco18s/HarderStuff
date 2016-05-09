package com.draco18s.hazards.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class CtoSMessage implements IMessage {
	private int playerID;
	private byte[] data;
	
	public CtoSMessage() {
		this(0, new byte[]{0});
	}
	
	public CtoSMessage(int i, ByteBuf dataToSet) {
		this(i, dataToSet.array());
	}
	
	public CtoSMessage(int playerUUID, byte[] dataToSet) {
		if (dataToSet.length > 0x1ffff0) {
			throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
		}
		this.playerID = playerUUID;
		this.data = dataToSet;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		//long uuidLeastBits = buffer.readLong();
		//long uuidMostBits = buffer.readLong();
		this.playerID = buffer.readInt();//new UUID(uuidMostBits, uuidLeastBits);
		this.data = new byte[buffer.readShort()];
		buffer.readBytes(this.data);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		if (data.length > 0x1ffff0) {
			throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
		}
		buffer.writeInt(playerID);
		//buffer.writeLong(playerID.getLeastSignificantBits());
		//buffer.writeLong(playerID.getMostSignificantBits());
		buffer.writeShort(this.data.length);
		buffer.writeBytes(this.data);
	}
	
	public byte[] getData() {
		return this.data;
	}
	public int getUUID() {
		return this.playerID;
	}
}
