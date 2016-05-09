package com.draco18s.hazards.client;

import io.netty.buffer.Unpooled;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.draco18s.hazards.HazardsEventHandler;
import com.draco18s.hazards.UndergroundBase;
import com.draco18s.hazards.block.BlockUnstableStone;
import com.draco18s.hazards.block.helper.UnstableStoneHelper;
import com.draco18s.hazards.entities.PlayerStats;
import com.draco18s.hazards.item.ItemBlockUnstable;
import com.draco18s.hazards.network.CtoSMessage;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class HazardsClientEventHandler {
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private Random rand = new Random();
    
	@SubscribeEvent
	public void onFogDensity(FogDensity event) {
		if(event.entity instanceof EntityPlayer && event.entity.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer)event.entity;
			PlayerStats stats = PlayerStats.get(event.entity);
			if(stats != null) {
				float farPlaneDistance = (float)(Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16);
				float f1 = farPlaneDistance;
				if(stats.airRemaining < 1000) {
					GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
					f1 = 5.0F + (farPlaneDistance - 5.0F) * (Math.max(stats.airRemaining,0) / 1000.0F);
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25F);
			        GL11.glFogf(GL11.GL_FOG_END, f1);
	
			        if (GLContext.getCapabilities().GL_NV_fog_distance) {
			            GL11.glFogi(34138, 34139);
			        }
			        event.setCanceled(true);
			        event.density = 0.1f;
				}
			}
		}
	}
	
	private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
	
	/*@SubscribeEvent
	public void mouseUpDown(MouseInputEvent ev) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player != null) {
			int jumpKey = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()+100;
			int k = Mouse.getEventButton();
			if(k == jumpKey) {
				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				out.writeBoolean(Mouse.isButtonDown(jumpKey));
				CtoSMessage packet = new CtoSMessage(player.getUniqueID(), out);
				UndergroundBase.networkWrapper.sendToServer(packet);
				HazardsEventHandler.instance.setPlayerSwimming(player, Mouse.isButtonDown(jumpKey));
			}
			//System.out.println(k + "=?=" + jumpKey);
		}
	}
	
	@SubscribeEvent
	public void keyUpDown(KeyInputEvent ev) {
		//Keyboard.getEventKey();
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player != null) {
			int jumpKey = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode();
			int k = Keyboard.getEventKey();
			System.out.println(Keyboard.isKeyDown(jumpKey));
			if(k == jumpKey) {
				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				out.writeBoolean(Keyboard.isKeyDown(jumpKey));
				CtoSMessage packet = new CtoSMessage(player.getUniqueID(), out);
				UndergroundBase.networkWrapper.sendToServer(packet);
				HazardsEventHandler.instance.setPlayerSwimming(player, Keyboard.isKeyDown(jumpKey));
			}
		}
	}*/
	@SubscribeEvent
	public void tickStart(TickEvent.ClientTickEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player != null) {
			int jumpKey = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode();
			boolean sendPacket = false;
			boolean state = false;
			if(jumpKey >= 0) {
				sendPacket = HazardsEventHandler.instance.setPlayerSwimming(player, Keyboard.isKeyDown(jumpKey));
				state = Keyboard.isKeyDown(jumpKey);
			}
			else {
				jumpKey += 100;
				if(jumpKey >= 0) {
					sendPacket = HazardsEventHandler.instance.setPlayerSwimming(player, Mouse.isButtonDown(jumpKey));
					state = Keyboard.isKeyDown(jumpKey);
				}
			}
			if(sendPacket) {
				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				out.writeBoolean(state);
				CtoSMessage packet = new CtoSMessage(player.getEntityId(), out);
				UndergroundBase.networkWrapper.sendToServer(packet);
			}
		}
	}
	
	@SubscribeEvent
	public void renderEvent(DrawBlockHighlightEvent event) {
		ItemStack s = event.player.getEquipmentInSlot(4);
		ItemStack h = event.player.getEquipmentInSlot(0);
		int sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
		
		boolean isSneaking = false;
		if(sneakKey >= 0) {
			isSneaking = Keyboard.isKeyDown(sneakKey);
		}
		else {
			sneakKey += 100;
			if(sneakKey >= 0) {
				isSneaking = Keyboard.isKeyDown(sneakKey);
			}
		}
		
		if(s != null && (isSneaking || h != null) && s.getItem() == UndergroundBase.goggles && (isSneaking || h.getItem().getToolClasses(h).contains("pickaxe"))) {
			boolean isEnchanted = EnchantmentHelper.getEnchantmentLevel(UndergroundBase.enchStoneStress.effectId, s) > 0;
			boolean isBlock = (h != null && h.getItem() instanceof ItemBlockUnstable);
			World theWorld = event.player.worldObj;
			Vec3 thisBlock;
			Block block;
			int meta;
			if(!isBlock) {
				thisBlock= Vec3.createVectorHelper(event.target.blockX, event.target.blockY, event.target.blockZ);
				block = theWorld.getBlock((int)thisBlock.xCoord, (int)thisBlock.yCoord, (int)thisBlock.zCoord);
				meta = theWorld.getBlockMetadata((int)thisBlock.xCoord, (int)thisBlock.yCoord, (int)thisBlock.zCoord)&7;
			}
			else {
				if(!theWorld.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ).isAir(theWorld, event.target.blockX, event.target.blockY, event.target.blockZ)) {
					block = Block.getBlockFromItem(h.getItem());
					ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[event.target.sideHit];
					thisBlock = Vec3.createVectorHelper(event.target.blockX+dir.offsetX, event.target.blockY+dir.offsetY, event.target.blockZ+dir.offsetZ);
					meta = h.getItemDamage();
				}
				else {
					return;
				}
			}
			if(block instanceof BlockUnstableStone) {
				if(!isEnchanted && event.target.sideHit <= 1 && meta == 1) {
					if(theWorld.isBlockNormalCubeDefault((int)thisBlock.xCoord+1, (int)thisBlock.yCoord, (int)thisBlock.zCoord, true) &&
						theWorld.isBlockNormalCubeDefault((int)thisBlock.xCoord-1, (int)thisBlock.yCoord, (int)thisBlock.zCoord, true) &&
						theWorld.isBlockNormalCubeDefault((int)thisBlock.xCoord, (int)thisBlock.yCoord, (int)thisBlock.zCoord+1, true) &&
						theWorld.isBlockNormalCubeDefault((int)thisBlock.xCoord, (int)thisBlock.yCoord, (int)thisBlock.zCoord-1, true)) {
							meta = 0;
	    			}
				}
				Vec3[] points = UnstableStoneHelper.getSupportByMeta(theWorld, (int)thisBlock.xCoord, (int)thisBlock.yCoord, (int)thisBlock.zCoord, meta, isEnchanted);

				GL11.glPushMatrix();
				GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
				Vec3 pos = event.player.getPosition(event.partialTicks);
				GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				if(points != null) {
					
					if(points.length > 0) {
						if(isBlock) {
							thisBlock.yCoord += 0.1;
						}
						for(int i=points.length-1; i>=0; i--) {
							//System.out.println(thisBlock + "->" + points[i]);
							drawLine(thisBlock, points[i], (meta==2?(points.length<4?0:points.length):points.length), i+1);
						}
					}
					else {
						drawSquare(thisBlock);
					}
					//GL11.glEnable(GL11.GL_LIGHTING);
				}
				else {
					if(event.target.sideHit == 1) {
						thisBlock.yCoord--;
					}
					drawTopGreenSquare(thisBlock);
				}
				GL11.glPopAttrib();
				GL11.glPopMatrix();
			}
		}
	}
	
	private void drawTopGreenSquare(Vec3 thisBlock) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawing(7);
		tess.setBrightness(15728880);
		tess.setColorOpaque_F(0F, 1F, 0F);
		float f = 1f/16f;
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);
		tess.draw();
	}
	
	private void drawSquare(Vec3 thisBlock) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawing(7);
		tess.setBrightness(15728880);
		tess.setColorOpaque_F(1F, 0F, 0F);
		//GL11.glColor3f(1F, 0F, 0F);//red
		float f = 1f/16f;
		//Y-Group
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 - f);
		
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.001, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord - 0.001, thisBlock.zCoord + 0.5 - f);
		
		//X-Group
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord - 0.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		
		//Z-Group
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord - 0.001);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.001);
		
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.001);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord - 0.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord - 0.001);
		
		//Y-Group+1
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);
		
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 1.001, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.999, thisBlock.zCoord + 0.5 - f);
		
		//X-Group+1
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 1.001, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);

		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 - f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 + f);
		tess.addVertex(thisBlock.xCoord + 0.999, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.5 - f);
		
		//Z-Group+1
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.999);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 1.001);
		
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 1.001);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 1.001);

		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 - f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 + f, thisBlock.zCoord + 0.999);
		tess.addVertex(thisBlock.xCoord + 0.5 + f, thisBlock.yCoord + 0.5 - f, thisBlock.zCoord + 0.999);
		
		tess.draw();
	}

	private void drawLine(Vec3 blockA, Vec3 blockB, int num, int f) {
		Tessellator tess = Tessellator.instance;
		blockB.yCoord++;
		int d = Math.round((float)blockA.distanceTo(blockB)+0.2f);
		blockB.yCoord--;
		if(num == 0) {
			blockA.yCoord -= 0.02f;
			drawSquare(blockA);
		}
		tess.startDrawing(7);
		tess.setBrightness(15728880);
		if(num == 0) {
			tess.setColorOpaque_F(1F, 0F, 0F);//red
		}
		else if(num == 1) {
			switch(d) {
				case 7:
				case 6:
				case 5:
					tess.setColorOpaque_F(1F, 0F, 0F);//red
					break;
				case 4:
					tess.setColorOpaque_F(1F, 0.35F, 0F);//red-orange
					break;
				case 3:
					tess.setColorOpaque_F(1F, 0.75F, 0F);//orange
					break;
				default:
					tess.setColorOpaque_F(0.75F, 1F, 0F);//lime
					break;
			}
		}
		else {
			switch(d) {
				case 7:
				case 6:
				case 5:
					tess.setColorOpaque_F(1F, 0.45F, 0F);//red-orange
					break;
				case 4:
					tess.setColorOpaque_F(0.8F, 0.8F, 0F);//orange
					break;
				case 3:
					tess.setColorOpaque_F(0.8F, 1F, 0F);//yellow
					break;
				default:
					tess.setColorOpaque_F(0F, 1F, 0F);//green
					break;
			}
		}
		
		Vec3 recLong = blockA.subtract(blockB);
		Vec3 perpendicular = Vec3.createVectorHelper(recLong.zCoord, recLong.yCoord, -recLong.xCoord);
		perpendicular = perpendicular.normalize();
		
		float Width = 1f/16f;
		
		Vec3 R1 = blockA.subtract(blockB);
		Vec3 R2 = blockA.subtract(blockB);
		Vec3 R3 = blockA.subtract(blockB);
		Vec3 R4 = blockA.subtract(blockB);
		
		R1.xCoord = blockA.xCoord + perpendicular.xCoord * Width;
		R1.zCoord = blockA.zCoord + perpendicular.zCoord * Width;
		R2.xCoord = blockA.xCoord - perpendicular.xCoord * Width;
		R2.zCoord = blockA.zCoord - perpendicular.zCoord * Width;
		R1.yCoord = blockA.yCoord - 0.01;
		R2.yCoord = blockA.yCoord - 0.01;
		
		R3.xCoord = blockB.xCoord + perpendicular.xCoord * Width;
		R3.zCoord = blockB.zCoord + perpendicular.zCoord * Width;
		R4.xCoord = blockB.xCoord - perpendicular.xCoord * Width;
		R4.zCoord = blockB.zCoord - perpendicular.zCoord * Width;
		R3.yCoord = blockB.yCoord + 0.75;
		R4.yCoord = blockB.yCoord + 0.75;
		tess.addVertex(R1.xCoord + 0.5, R1.yCoord, R1.zCoord + 0.5);
		tess.addVertex(R3.xCoord + 0.5, R3.yCoord, R3.zCoord + 0.5);
		tess.addVertex(R4.xCoord + 0.5, R4.yCoord, R4.zCoord + 0.5);
		tess.addVertex(R2.xCoord + 0.5, R2.yCoord, R2.zCoord + 0.5);
		
		tess.draw();

		drawTopGreenSquare(blockA);
	}

	/*private void drawLineWithGL(Vec3 blockA, Vec3 blockB, int num, int f) {
		int d = Math.round((float)blockA.distanceTo(blockB)+0.2f);
		if(num == 0) {
			GL11.glColor3f(1F, 0F, 0F);//red
		}
		else if(num == 1) {
			switch(d) {
				case 7:
				case 6:
				case 5:
					GL11.glColor3f(1F, 0F, 0F);//red
					break;
				case 4:
					GL11.glColor3f(1F, 0.35F, 0F);//red-orange
					break;
				case 3:
					GL11.glColor3f(1F, 0.75F, 0F);//orange
					break;
				default:
					GL11.glColor3f(0.75F, 1F, 0F);//lime
					break;
			}
		}
		else {
			switch(d) {
				case 7:
				case 6:
				case 5:
					GL11.glColor3f(1F, 0.45F, 0F);//red-orange
					break;
				case 4:
					GL11.glColor3f(0.8F, 0.8F, 0F);//orange
					break;
				case 3:
					GL11.glColor3f(0.8F, 1F, 0F);//yellow
					break;
				default:
					GL11.glColor3f(0F, 1F, 0F);//green
					break;
			}
		}
		
		float oz = (blockA.xCoord - blockB.xCoord == 0?0:-1f/16f);
		float ox = (blockA.zCoord - blockB.zCoord == 0?0:1f/16f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		if(blockA.xCoord - blockB.xCoord > 0 || blockA.zCoord - blockB.zCoord > 0) {
			GL11.glVertex3d(blockA.xCoord + 0.5,blockA.yCoord - 0.01,blockA.zCoord + 0.5);
			GL11.glVertex3d(blockB.xCoord + 0.5,blockB.yCoord + 0.99,blockB.zCoord + 0.5);
		}
		else {
			GL11.glVertex3d(blockA.xCoord + 0.5,blockA.yCoord - 0.01,blockA.zCoord + 0.5);
			GL11.glVertex3d(blockB.xCoord + 0.5,blockB.yCoord + 0.99,blockB.zCoord + 0.5);
		}
		GL11.glEnd();
	}*/
}
