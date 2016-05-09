package com.draco18s.wildlife.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class StoCMessage implements IMessage {
	private int entityID;
	private byte[] data;
	
	public StoCMessage() {
		this(0, new byte[]{0});
	}

	public StoCMessage(int i, ByteBuf dataToSet) {
		this(i, dataToSet.array());
	}
	
	public StoCMessage(int entUUID, byte[] dataToSet) {
		if (dataToSet.length > 0x1ffff0) {
			throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
		}
		this.entityID = entUUID;
		this.data = dataToSet;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.entityID = buffer.readInt();//new UUID(uuidMostBits, uuidLeastBits);
		this.data = new byte[buffer.readShort()];
		buffer.readBytes(this.data);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		if (data.length > 0x1ffff0) {
			throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
		}
		buffer.writeInt(entityID);
		buffer.writeShort(this.data.length);
		buffer.writeBytes(this.data);
	}
	
	public byte[] getData() {
		return this.data;
	}
	public int getUUID() {
		return this.entityID;
	}
}
