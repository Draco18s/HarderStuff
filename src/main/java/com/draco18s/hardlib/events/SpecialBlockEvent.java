package com.draco18s.hardlib.events;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

public class SpecialBlockEvent extends BlockEvent {
	public BiomeGenBase biome;
	
	public SpecialBlockEvent(World _w, int _x, int _y, int _z, int _m, Block _b, BiomeGenBase bio) {
		super(_x, _y, _z, _w, _b, _m);
		biome = bio;
	}
	
	/**
	 * Event inserted into Block classes to intercept or modify their behavior.  Each insert has slightly different usage.<br>
	 * For snow and ice, nothing is done with the event result.  For crops, if the event is canceled, the crop update will exit early.<br>
	 * See: {@link com.draco18s.hardlibcore.asm.HardLibPatcher}
	 * @author Draco18s
	 *
	 */
	@Cancelable
	public static class BlockUpdateEvent extends SpecialBlockEvent {
		public BlockUpdateEvent(World _w, int _x, int _y, int _z, int _m, Block _b, BiomeGenBase bio) {
			super(_w, _x, _y, _z, _m, _b, bio);
		}
	}
}
