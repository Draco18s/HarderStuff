package com.draco18s.wildlife.entity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGrassSnow extends TileEntity {
	public Block oBlock;
	public int oMeta;

	public TileEntityGrassSnow() { }
	
	public boolean canUpdate() {
        return false;
    }
	
	@Override
    public void readFromNBT(NBTTagCompound tc) {
        super.readFromNBT(tc);
        oBlock = Block.getBlockById(tc.getInteger("blockType"));
        oMeta = tc.getInteger("blockMeta");
    }

    @Override
    public void writeToNBT(NBTTagCompound tc) {
        super.writeToNBT(tc);
        tc.setInteger("blockType", Block.getIdFromBlock(oBlock));
        tc.setInteger("blockMeta", oMeta);
    }
    
    @Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
}
