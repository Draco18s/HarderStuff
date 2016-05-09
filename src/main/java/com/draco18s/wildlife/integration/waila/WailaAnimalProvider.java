package com.draco18s.wildlife.integration.waila;

import java.util.Iterator;
import java.util.List;

import com.draco18s.wildlife.WildlifeEventHandler;
import com.draco18s.wildlife.entity.ai.EntityAIAging;
import com.draco18s.wildlife.entity.ai.EntityAgeTracker;
import com.draco18s.wildlife.util.AnimalUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;

public class WailaAnimalProvider implements IWailaEntityProvider {
	private String infinity;

	public WailaAnimalProvider() {
		int codePoint = 0x221E;
		char[] charPair = Character.toChars(codePoint);
		infinity = new String(charPair);
	}

	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		if(entity instanceof EntityAnimal) {
			EntityAnimal anim = (EntityAnimal)entity;
			EntityAgeTracker ageTrack = WildlifeEventHandler.instance.getTracker(anim);//getAgeTask(anim);
			//currenttip.add(""+ageTaskBase);
			if(ageTrack != null) {
				long ca = ageTrack.entityAge;
				long da = ageTrack.deathAge;
				int relative = (int)Math.round(ca / ((AnimalUtil.animalMaxAge / ageTrack.ageFactor) / 8));
				switch(relative) {
					case 0:
					case 1:
						if(((EntityAnimal) entity).isChild()) {
							currenttip.add(StatCollector.translateToLocal("age.baby") /*+ " ("+da+")"*/);
						}
						else {
							currenttip.add(StatCollector.translateToLocal("age.young") /*+ " ("+da+")"*/);
						}
						break;
					case 2:
					case 3:
						currenttip.add(StatCollector.translateToLocal("age.mature") /*+ " ("+da+")"*/);
						break;
					case 4:
					case 5:
						currenttip.add(StatCollector.translateToLocal("age.adult") /*+ " ("+da+")"*/);
						break;
					case 6:
					case 7:
						currenttip.add(StatCollector.translateToLocal("age.old") /*+ " ("+da+")"*/);
						break;
					case 8:
					case 9:
					case 10:
					case 11:
						currenttip.add(StatCollector.translateToLocal("age.ancient") /*+ " ("+da+")"*/);
						break;
					case 19:
					case 20:
					case 21:
						currenttip.add(StatCollector.translateToLocal("age.unaging") /*+ " ("+infinity+")"*/);
						break;
					default:
						currenttip.add("ERROR: Age " + relative + " unknown");
						break;
				}
			}
		}
		return currenttip;
	}

	/*private EntityAIAging getAgeTask(EntityAnimal anim) {
		Iterator<EntityAITaskEntry> i = anim.tasks.taskEntries.iterator();
		while(i.hasNext()) {
			EntityAITaskEntry task = i.next();
			System.out.println(task.action);
			if(task.action instanceof EntityAIAging) {
				return (EntityAIAging)task.action;
			}
			
		};
		return null;
	}*/

	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
		return null;
	}

}
