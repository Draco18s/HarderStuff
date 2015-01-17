package com.draco18s.cropcore.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;
import static com.draco18s.cropcore.asm.ASMHelper.*;

import java.util.Random;

import org.objectweb.asm.tree.*;
import org.objectweb.asm.*;

import com.draco18s.hardlib.events.*;

import net.minecraft.block.BlockCrops;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

public class CropPatcher implements IClassTransformer {
	private Type RANDOM_TYPE = Type.getType("Ljava/util/Random;");
	private Type BLOCK_TYPE = Type.getType("Lnet/minecraft/block/Block;");
	private Type WORLD_TYPE = Type.getType("Lnet/minecraft/world/World;");
	private Type PLAYER_TYPE = Type.getType("Lnet/minecraft/entity/player/EntityPlayer;");
	private Type ANIMAL_TYPE = Type.getType("Lnet/minecraft/entity/passive/EntityAnimal;");
	private static Random rand = new Random();
	
	public static long moonPhaseTime = 24000L;

	public CropPatcher() {
		
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(name.equals("net.minecraft.block.BlockSnow")) {
			return transformSnow(bytes);
		}
		else if(name.equals("net.minecraft.block.BlockCrops")) {
			return transformCrops(bytes);
		}
		else if(name.equals("net.minecraft.entity.passive.EntityAnimal")) {
			return transformAnimals(bytes);
		}
		else if(name.equals("net.minecraft.world.WorldProvider")) {
			return transformWorldProvider(bytes);
		}
		return bytes;
	}
	
	private byte[] transformSnow(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchSnow(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformWorldProvider(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchWorldProvider(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformAnimals(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchEntityAnimal(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformCrops(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchBlockCrops(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}
	
	private void patchSnow(ClassNode classNode) {
		System.out.println("==Patching BlockSnow==");
		MethodNode method = ASMHelper.findMethod(classNode, "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		InsnList patch = new InsnList();
		AbstractInsnNode absNode = findFirst(RETURN, method.instructions);
		
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/cropcore/asm/CropPatcher", "snowMeltByTemp", getMethodDescriptor(VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
		method.instructions.insertBefore(absNode, patch);
		//printOpcodes(method);
	}

	private void patchWorldProvider(ClassNode classNode) {
		System.out.println("==Patching WorldProvider==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "getMoonPhase", INT_TYPE, LONG_TYPE);
		
		InsnList patch = new InsnList();
		
		AbstractInsnNode absNode = findFirst(LDC, method.instructions);
		LdcInsnNode ldc = (LdcInsnNode)absNode;
		ldc.cst = moonPhaseTime;
	}
	
	private void patchEntityAnimal(ClassNode classNode) {
		System.out.println("==Patching EntityAnimal==");
		//goal: make this patch an event, making the function return early
		MethodNode method = ASMHelper.findMethod(classNode, "func_146082_f", VOID_TYPE, PLAYER_TYPE);
		InsnList patch = new InsnList();
		patch.add(new InsnNode(DUP));
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/cropcore/asm/CropPatcher", "animalLoveMode", getMethodDescriptor(INT_TYPE, ANIMAL_TYPE, PLAYER_TYPE)));
		
		patch.add(new InsnNode(DUP));
		LabelNode label  = new LabelNode();
		patch.add(new JumpInsnNode(IFNE, label));
		patch.add(new InsnNode(RETURN));
		patch.add(label);
		
		AbstractInsnNode absNode = findFirst(SIPUSH, method.instructions);
		method.instructions.insertBefore(absNode, patch);
		method.instructions.remove(absNode);
		//printOpcodes(method);
	}
	
	private void patchBlockCrops(ClassNode classNode) {
		BlockCrops l;
		System.out.println("==Patching BlockCrops==");
		
		/*Slows down growth by a factor of 10*/
		MethodNode method = ASMHelper.findMethod(classNode, "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		InsnList patch = new InsnList();
		patch.add(new VarInsnNode(ALOAD, 5));
		patch.add(new LdcInsnNode(10));
		patch.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/Random", "nextInt", getMethodDescriptor(INT_TYPE, INT_TYPE)));
		LabelNode label  = new LabelNode();
		patch.add(new JumpInsnNode(IFEQ, label));
		patch.add(new InsnNode(RETURN));
		patch.add(label);
		AbstractInsnNode absNode = findFirst(INVOKESPECIAL, method.instructions);
		method.instructions.insert(absNode, patch);
		
		/*Changes bonemeal effectiveness*/
		method = ASMHelper.findMethod(classNode, "func_149863_m", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE);
		absNode = findFirst(INVOKEVIRTUAL, method.instructions);
		absNode = absNode.getNext().getNext().getNext();
		patch = new InsnList();
		patch.add(new InsnNode(ICONST_1));
		patch.add(new InsnNode(ICONST_3));
		method.instructions.insertBefore(absNode, patch);
		method.instructions.remove(absNode.getNext());
		method.instructions.remove(absNode);
	}
	
	public static int animalLoveMode(EntityAnimal anim, EntityPlayer player) {
		//System.out.println("Love mode intercept");
		if(player == null) return 600;
		EntityAnimalInteractEvent.AnimalLoveEvent ev = new EntityAnimalInteractEvent.AnimalLoveEvent(player, anim.getClass(), 600);
		MinecraftForge.EVENT_BUS.post(ev);
		return ev.loveTime;
	}
	
	public static void snowMeltByTemp(World world, int x, int y, int z) {
		BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		SpecialBlockEvent ev = new SpecialBlockEvent.BlockUpdateEvent(world, x, y, z, world.getBlockMetadata(x, y, z), Blocks.snow_layer, bio);
		MinecraftForge.EVENT_BUS.post(ev);
		return;
	}
}
