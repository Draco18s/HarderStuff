package com.draco18s.ores.client;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import com.draco18s.ores.entities.*;

public class SoundWindmill extends PositionedSound implements ITickableSound{
	protected TileEntityMillstone millstone;
    protected boolean donePlaying = false;

	protected SoundWindmill(ResourceLocation sound, TileEntityMillstone mill) {
		super(sound);
        this.repeat = true;
        millstone = mill;
        this.xPosF = mill.xCoord;
        this.yPosF = mill.yCoord;
        this.zPosF = mill.zCoord;
	}

	public void update() {
		TileEntity te = millstone.getWorldObj().getTileEntity((int)xPosF,(int)yPosF,(int)zPosF);
        if (!(te != null && te instanceof TileEntityMillstone) || !millstone.canGrind() || millstone.getGrindTime() <= 0) {
        	donePlaying = true;
        }
    }
	
	public boolean isDonePlaying() {
        return donePlaying;
    }
}
