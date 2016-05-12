package com.draco18s.wildlife.entity.ai;

import io.netty.buffer.Unpooled;

import java.util.Random;

import com.draco18s.hardlib.api.HardLibAPI;
import com.draco18s.wildlife.WildlifeBase;
import com.draco18s.wildlife.entity.CowStats;
import com.draco18s.wildlife.entity.EntityGoat;
import com.draco18s.wildlife.network.StoCMessage;
import com.draco18s.wildlife.util.AnimalUtil;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Vec3;

public class EntityAIMilking extends EntityAIBase {
	private EntityCow entity;
	private EntityAIAging agingTask;
	//private int foodLevel = -1;
	private CowStats stats;

	public EntityAIMilking(EntityCow ent) {
		entity = ent;
		//stats.milkLevel = entity.getEntityData().getInteger("MilkLevel");
		//if(stats.milkLevel <= 0) {
		//	if(!ent.isChild()) {
		//		stats.milkLevel = AnimalUtil.milkQuanta + (new Random()).nextInt(4000);
		//	}
		//	else {
		//		stats.milkLevel = 1;
		//	}
		//}
		//stats.milkLevel = foodLevel;
	}
	
	public void startExecuting() {
		stats = CowStats.get((EntityCow)entity);
		for(Object obj : entity.tasks.taskEntries) {
			EntityAITasks.EntityAITaskEntry task = (EntityAITasks.EntityAITaskEntry)obj;
			if(task.action instanceof EntityAIAging) {
				agingTask = (EntityAIAging) task.action;
			}
		}
		sendUpdatePacket(entity, stats);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public void updateTask() {
		boolean hasGrass = false;
		if(agingTask.shouldExecute()) {
			hasGrass = agingTask.grassNearby();
		}
		else {
			Vec3 close= Vec3.createVectorHelper(0, -10, 0);
			Vec3 here= Vec3.createVectorHelper(0, -10, 0);
			hasGrass = agingTask.checkGrass(close, here);
		}
		if(hasGrass) {
			if(entity.getGrowingAge() == 0)
				stats.milkLevel++;
		}
		else {
			stats.milkLevel--;
			if(stats.milkLevel % AnimalUtil.milkQuanta == 0)
				agingTask.avoidStarving();
		}
		
		stats.milkLevel = Math.max(Math.min(stats.milkLevel, 3 * AnimalUtil.milkQuanta + 5000), 0);
		if(stats.milkLevel % (600) == 0) {
			//System.out.println("" + stats.milkLevel);
			sendUpdatePacket(entity, stats);
		}
		//stats.milkLevel = foodLevel;
		//entity.getEntityData().setInteger("MilkLevel", stats.milkLevel);
	}
	
	public static void sendUpdatePacket(EntityCow self, CowStats stats) {
		//System.out.println("Sending update packet (" + stats.milkLevel +")");
		PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		out.writeInt(stats.milkLevel);
		StoCMessage packet = new StoCMessage(self.getEntityId(), out);
		WildlifeBase.networkWrapper.sendToAllAround(packet, new TargetPoint(self.worldObj.provider.dimensionId, self.posX, self.posY, self.posZ, 64));
	}

	public boolean getMilkable() {
		if(stats == null) {
			stats = CowStats.get((EntityCow)entity);
			if(stats == null) {
				return false;
			}
		}
		//System.out.println("Milk available: " + stats.milkLevel + ", " + (stats.milkLevel >= AnimalUtil.milkQuanta + 4000));
		return stats.milkLevel >= AnimalUtil.milkQuanta + 4000;
	}
	
	public void doMilking() {
		stats.milkLevel -= AnimalUtil.milkQuanta;
		sendUpdatePacket(entity, stats);
	}
}
