package com.draco18s.industry.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class CtoSMessage implements IMessage {
	private int dim;
	private int posx;
	private int posy;
	private int posz;
	
	private int ordinal;
	
	public CtoSMessage() {
		this(0, 0, 0, 0, 0);
	}
	
	public CtoSMessage(int d, int x, int y, int z, int o) {
		dim = d;
		posx = x;
		posy = y;
		posz = z;
		
		ordinal = o;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		//long uuidLeastBits = buffer.readLong();
		//long uuidMostBits = buffer.readLong();
		dim = buffer.readInt();
		posx = buffer.readInt();
		posy = buffer.readInt();
		posz = buffer.readInt();
		ordinal = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(dim);
		buffer.writeInt(posx);
		buffer.writeInt(posy);
		buffer.writeInt(posz);
		buffer.writeInt(ordinal);
	}
	
	public int dim() {
		return dim;
	}
	
	public int x() {
		return posx;
	}
	
	public int y() {
		return posy;
	}
	
	public int z() {
		return posz;
	}
	
	public int ordinal() {
		return ordinal;
	}
}
