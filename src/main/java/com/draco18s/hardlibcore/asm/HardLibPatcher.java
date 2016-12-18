package com.draco18s.hardlibcore.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;
import static com.draco18s.hardlibcore.asm.ASMHelper.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.objectweb.asm.tree.*;
import org.objectweb.asm.*;

import scala.collection.concurrent.Debug;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

public class HardLibPatcher implements IClassTransformer {
	private Type RANDOM_TYPE = Type.getType("Ljava/util/Random;");
	private Type BLOCK_TYPE = Type.getType("Lnet/minecraft/block/Block;");
	private Type WORLD_TYPE = Type.getType("Lnet/minecraft/world/World;");
	private Type PLAYER_TYPE = Type.getType("Lnet/minecraft/entity/player/EntityPlayer;");
	private Type ANIMAL_TYPE = Type.getType("Lnet/minecraft/entity/passive/EntityAnimal;");
	private Type COW_TYPE = Type.getType("Lnet/minecraft/entity/passive/EntityCow;");
	private Type ITEMSTACK_TYPE = Type.getType("Lnet/minecraft/item/ItemStack;");
	private Type ITEMFRAME_TYPE = Type.getType("Lnet/minecraft/entity/item/EntityItemFrame;");
	private static Random rand = new Random();
	
	public static long moonPhaseTime = 24000L;
	public static Block unstableStoneBlock;

	private static Constructor blockConstr;
	private static Constructor animalConstr;
	private static Constructor frameConstr;
	private static Constructor cowConstr;
	private static Field evLoveTimeField;
	private static Field evPowerField;
	
	public HardLibPatcher() { }

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(transformedName.equals("net.minecraft.block.BlockSnow")) {
			return transformSnow(bytes, "BlockSnow");
		}
		else if(transformedName.equals("net.minecraft.block.BlockIce")) {
			return transformSnow(bytes, "BlockIce");
		}
		else if(transformedName.equals("net.minecraft.block.BlockCrops")) {
			return transformCrops(bytes);
		}
		else if(transformedName.equals("net.minecraft.entity.passive.EntityAnimal")) {
			return transformAnimals(bytes);
		}
		else if(transformedName.equals("net.minecraft.entity.passive.EntityCow") || transformedName.equals("net.minecraft.entity.passive.EntityMooshroom")) {
			return transformCows(bytes);
		}
		else if(transformedName.equals("net.minecraft.world.WorldProvider")) {
			return transformWorldProvider(bytes);
		}
		else if(transformedName.equals("net.minecraft.block.BlockStem")) {
			return transformStem(bytes);
		}
		else if(transformedName.equals("net.minecraft.block.BlockFalling")) {
			return transformSnow(bytes,"BlockFalling");
		}
		else if(transformedName.equals("net.minecraft.block.BlockSilverfish")) {
			return transformMonsterEgg(bytes);
		}
		else if(transformedName.equals("net.minecraft.block.BlockReed")) {
			return transformReed(bytes);
		}
		else if(transformedName.equals("net.minecraft.block.BlockRedstoneComparator")) {
			return transformComparator(bytes);
		}
		else if(transformedName.equals("net.minecraft.entity.item.EntityItemFrame")) {
			return transformItemFrame(bytes);
		}
		/*else {
			System.out.println("Transforming class '" + transformedName + "'");
		}*/
		return bytes;
	}

	private byte[] transformItemFrame(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchItemFrame(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformComparator(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchComparator(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformReed(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchBlockReed(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformMonsterEgg(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchMonsterEgg(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}
	
	private byte[] transformBlockFalling(byte[] bytes, String name) {
		ClassNode classNode = createClassNode(bytes);
		patchSnow(classNode, name);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}
	
	private byte[] transformSnow(byte[] bytes, String name) {
		ClassNode classNode = createClassNode(bytes);
		patchSnow(classNode, name);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformWorldProvider(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchWorldProvider(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private byte[] transformCows(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchEntityCow(classNode);
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

	private byte[] transformStem(byte[] bytes) {
		ClassNode classNode = createClassNode(bytes);
		patchBlockStem(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}
	
	private void patchMonsterEgg(ClassNode classNode) {
		System.out.println("==Patching BlockSilverfish==");
		MethodNode method = ASMHelper.findMethod(classNode, "func_150196_a", "func_150196_a", BOOLEAN_TYPE, BLOCK_TYPE);
		InsnList patch = new InsnList();
		//AbstractInsnNode absNode = null;
		AbstractInsnNode absNode = findFirst(IF_ACMPEQ, method.instructions);
		LabelNode lbl = ((JumpInsnNode)absNode).label;
		/*AbstractInsnNode absNode2 = null;
		LabelNode lbl = ((JumpInsnNode)absNode).label;
		Iterator<AbstractInsnNode> it = method.instructions.iterator();
		while(it.hasNext()) {
			AbstractInsnNode node = it.next();
			if(node == lbl) {
				absNode = node;
			}
		}*/
		//while(!(absNode instanceof LabelNode)) {
		//	absNode.getNext();
		//}
		/*patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "snowMeltByTemp", getMethodDescriptor(VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
		method.instructions.insertBefore(absNode, patch);*/
		FieldInsnNode fiNode = new FieldInsnNode(GETSTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "unstableStoneBlock", BLOCK_TYPE.getDescriptor());

		patch.add(new VarInsnNode(ALOAD, 0));
		patch.add(fiNode);
		patch.add(new JumpInsnNode(IF_ACMPEQ, (LabelNode) lbl));
		method.instructions.insert(absNode, patch);
		//printOpcodes(method);
	}
	
	private void patchSnow(ClassNode classNode, String name) {
		System.out.println("==Patching "+name+"==");
		MethodNode method = ASMHelper.findMethod(classNode, "func_149674_a", "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		InsnList patch = new InsnList();
		AbstractInsnNode absNode = findLast(RETURN, method.instructions);
		
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "snowMeltByTemp", getMethodDescriptor(VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
		method.instructions.insertBefore(absNode, patch);
		//printOpcodes(method);
	}

	private void patchWorldProvider(ClassNode classNode) {
		System.out.println("==Patching WorldProvider==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "func_76559_b", "getMoonPhase", INT_TYPE, LONG_TYPE);
		
		InsnList patch = new InsnList();
		
		AbstractInsnNode absNode = findFirst(LDC, method.instructions);
		Object cst = ((LdcInsnNode)absNode).cst;
		if ((cst instanceof Long) && (Long) cst == 24000) {
			AbstractInsnNode replacement = new FieldInsnNode(Opcodes.GETSTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "moonPhaseTime", "J");
			method.instructions.set(absNode, replacement);
		}
	}
	
	private void patchEntityAnimal(ClassNode classNode) {
		MethodNode method = ASMHelper.findMethod(classNode, "func_146082_f", VOID_TYPE, PLAYER_TYPE);
		AbstractInsnNode absNode = findFirst(SIPUSH, method.instructions);
		if(absNode != null) {
			System.out.println("==Patching EntityAnimal==");

			InsnList patch = new InsnList();
			patch.add(new InsnNode(DUP));
			patch.add(new VarInsnNode(ALOAD, 1));
			patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "animalLoveMode", getMethodDescriptor(INT_TYPE, ANIMAL_TYPE, PLAYER_TYPE)));
			
			patch.add(new InsnNode(DUP));
			LabelNode label  = new LabelNode();
			patch.add(new JumpInsnNode(IFNE, label));
			patch.add(new InsnNode(RETURN));
			patch.add(label);
			method.instructions.insertBefore(absNode, patch);
			method.instructions.remove(absNode);
		}
		//printOpcodes(method);
	}
	
	private void patchEntityCow(ClassNode classNode) {
		MethodNode method = ASMHelper.findMethod(classNode, "func_70085_c", BOOLEAN_TYPE, PLAYER_TYPE);
		System.out.println("==Patching EntityCow==");
		if(method == null) {
			method = ASMHelper.findMethod(classNode, "interact", BOOLEAN_TYPE, PLAYER_TYPE);
			if(method == null) {
				System.out.println("Could not find method!");
				return;
			}
		}
		AbstractInsnNode absNode = findFirst(IFNE, method.instructions);
		if(absNode != null) {
			absNode = absNode.getNext().getNext();
			InsnList patch = new InsnList();
			//patch.add(new InsnNode(DUP));
			patch.add(new VarInsnNode(ALOAD, 0));
			patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "cowMilking", getMethodDescriptor(BOOLEAN_TYPE, COW_TYPE)));
			
			LabelNode label  = new LabelNode();
			patch.add(new JumpInsnNode(IFNE, label));
			//patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "traceOne", getMethodDescriptor(VOID_TYPE)));
			patch.add(new InsnNode(ICONST_1));
			patch.add(new InsnNode(IRETURN));
			patch.add(label);
			//patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "traceTwo", getMethodDescriptor(VOID_TYPE)));
			//patch.add(new InsnNode(DUP));
			//LabelNode label  = new LabelNode();
			//patch.add(new JumpInsnNode(IFNE, label));
			//patch.add(new InsnNode(RETURN));
			//patch.add(label);
			method.instructions.insert(absNode, patch);
			//method.instructions.remove(absNode);
		}
		//printOpcodes(method);
	}
	
	private void patchBlockStem(ClassNode classNode) {
		System.out.println("==Patching BlockStem==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "func_149674_a", "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		InsnList patch = new InsnList();
		
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "cropSkipGrowth", getMethodDescriptor(INT_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
		LabelNode label  = new LabelNode();
		patch.add(new JumpInsnNode(IFEQ, label));
		patch.add(new InsnNode(RETURN));
		patch.add(label);
		AbstractInsnNode absNode = findFirst(INVOKESPECIAL, method.instructions);
		method.instructions.insert(absNode, patch);
		
		/*Changes bonemeal effectiveness*/
		method = ASMHelper.findMethod(classNode, "func_149874_m", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE);
		absNode = findFirst(INVOKEVIRTUAL, method.instructions);
		absNode = absNode.getNext().getNext().getNext();
		patch = new InsnList();
		patch.add(new InsnNode(ICONST_1));
		patch.add(new InsnNode(ICONST_3));
		method.instructions.insertBefore(absNode, patch);
		method.instructions.remove(absNode.getNext());
		method.instructions.remove(absNode);
	}
	
	private void patchBlockCrops(ClassNode classNode) {
		System.out.println("==Patching BlockCrops==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "func_149674_a", "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		
		InsnList patch = new InsnList();
		
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "cropSkipGrowth", getMethodDescriptor(INT_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
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
	
	private void patchBlockReed(ClassNode classNode) {
		System.out.println("==Patching BlockReed==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "func_149674_a", "updateTick", VOID_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, RANDOM_TYPE);
		InsnList patch = new InsnList();
		
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 2));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "cropSkipGrowth", getMethodDescriptor(INT_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		
		LabelNode label  = new LabelNode();
		patch.add(new JumpInsnNode(IFEQ, label));
		patch.add(new InsnNode(RETURN));
		patch.add(label);
		AbstractInsnNode absNode = findFirst(ALOAD, method.instructions);
		//printOpcodes(method);
		method.instructions.insertBefore(absNode, patch);
		//printOpcodes(method);
	}
	
	private void patchComparator(ClassNode classNode) {
		System.out.println("==Patching BlockRedstoneComparator==");
		
		MethodNode method = ASMHelper.findMethod(classNode, "func_149903_h", "getInputStrength", INT_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE);
		InsnList patch = new InsnList();

		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ILOAD, 8));
		patch.add(new VarInsnNode(ILOAD, 3));
		patch.add(new VarInsnNode(ILOAD, 9));
		patch.add(new VarInsnNode(ILOAD, 7));
		patch.add(new VarInsnNode(ILOAD, 6));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "comparatorItemFrames", getMethodDescriptor(INT_TYPE, WORLD_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE)));
		patch.add(new VarInsnNode(ISTORE, 6));
		//ISTORE
		AbstractInsnNode absNode = findLast(INVOKEVIRTUAL, method.instructions);
		do {
			absNode = absNode.getNext();
		} while(!(absNode instanceof LabelNode));
		
		method.instructions.insert(absNode, patch);
		
		//printOpcodes(method);
	}
	
	private void patchItemFrame(ClassNode classNode) {
		System.out.println("==Patching EntityItemFrame==");
		
		MethodNode method  = ASMHelper.findMethod(classNode, "func_82336_g", "setItemRotation", VOID_TYPE, INT_TYPE);
		MethodNode method2 = ASMHelper.findMethod(classNode, "func_82334_a", "setDisplayedItem", VOID_TYPE, ITEMSTACK_TYPE);
		InsnList patch = new InsnList();
		patch.add(new VarInsnNode(ALOAD, 0));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "updateWorldFromItemFrame", getMethodDescriptor(VOID_TYPE, ITEMFRAME_TYPE)));
		//printOpcodes(method2);
		AbstractInsnNode absNode = findLast(RETURN, method.instructions);
		method.instructions.insertBefore(absNode, patch);
		
		patch = new InsnList();
		patch.add(new VarInsnNode(ALOAD, 0));
		patch.add(new MethodInsnNode(INVOKESTATIC, "com/draco18s/hardlibcore/asm/HardLibPatcher", "updateWorldFromItemFrame", getMethodDescriptor(VOID_TYPE, ITEMFRAME_TYPE)));
		AbstractInsnNode absNode2 = findLast(RETURN, method2.instructions);
		method2.instructions.insertBefore(absNode2, patch);
		//printOpcodes(method2);
	}
	
	public static int animalLoveMode(EntityAnimal anim, EntityPlayer player) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException, SecurityException {
		if(animalConstr == null) {
			Class cls = Class.forName("com.draco18s.hardlib.events.EntityAnimalInteractEvent$AnimalLoveEvent");
			//animalConstr = cls.getConstructors()[0];
			Constructor[] allConstructors = cls.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
		    	Class<?>[] pType  = ctor.getParameterTypes();
				if(pType.length == 3 && pType[0] == EntityLivingBase.class && pType[1].isInstance(anim.getClass())) {
					animalConstr = ctor;
				}
		    }
			evLoveTimeField = cls.getField("loveTime");
		}
		
		//System.out.println("Love mode intercept");
		if(player == null) return 600;
		//EntityAnimalInteractEvent.AnimalLoveEvent ev = new EntityAnimalInteractEvent.AnimalLoveEvent(player, anim.getClass(), 600);
		//try {
			Event ev = (Event) animalConstr.newInstance(player, anim.getClass(), 600);
			MinecraftForge.EVENT_BUS.post(ev);
			return evLoveTimeField.getInt(ev);
		/*} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return 600;*/
		//return ev.loveTime;
	}
	
	public static void snowMeltByTemp(World world, int x, int y, int z) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		if(blockConstr == null) {
			Class cls = Class.forName("com.draco18s.hardlib.events.SpecialBlockEvent$BlockUpdateEvent");
			//blockConstr = cls.getConstructors()[0];
			//this way?
			Constructor[] allConstructors = cls.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
		    	Class<?>[] pType  = ctor.getParameterTypes();
				if(pType.length == 7) {
					blockConstr = ctor;
				}
		    }
		}
		BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		//try {
			Event ev = (Event) blockConstr.newInstance(world, x, y, z, world.getBlockMetadata(x, y, z), world.getBlock(x, y, z), bio);
			MinecraftForge.EVENT_BUS.post(ev);
		/*} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return;*/
	}
	
	/**
	 * This function handles how crops have slower growth.  Replacement for rand.nextInt(n)
	 * and I don't feel like rewriting the bytecode to do something more transparent. 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return if return == 0, the crop grows.
	 * @author Draco18s
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static int cropSkipGrowth(World world, int x, int y, int z) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		if(blockConstr == null) {
			Class cls = Class.forName("com.draco18s.hardlib.events.SpecialBlockEvent$BlockUpdateEvent");
			
			Constructor[] allConstructors = cls.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
		    	Class<?>[] pType  = ctor.getParameterTypes();
				if(pType.length == 7) {
					blockConstr = ctor;
				}
		    }
		}
		BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
		//SpecialBlockEvent ev = new SpecialBlockEvent.BlockUpdateEvent(world, x, y, z, world.getBlockMetadata(x, y, z), world.getBlock(x, y, z), bio);
		//try {
			Event ev = (Event) blockConstr.newInstance(world, x, y, z, world.getBlockMetadata(x, y, z), world.getBlock(x, y, z), bio);
			if(MinecraftForge.EVENT_BUS.post(ev)) {
				return 1;//aborts crop growth
			}
		/*} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
		
		return 0;/**/
	}
	
	public static int comparatorItemFrames(World world, int x, int y, int z, int dir, int currentPower) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
		if(frameConstr == null) {
			Class cls = Class.forName("com.draco18s.hardlib.events.SpecialBlockEvent$ItemFrameComparatorPowerEvent");
			
			Constructor[] allConstructors = cls.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
		    	Class<?>[] pType  = ctor.getParameterTypes();
				if(pType.length == 3) {
					frameConstr = ctor;
				}
		    }
		    evPowerField = cls.getField("power");
		}
		Block block = world.getBlock(x, y, z);
		if (block.getMaterial() == Material.air)
        {
            EntityItemFrame entityitemframe = findItemFrame(world, x, y, z, dir);

            if (entityitemframe != null)
            {
                ItemStack i = entityitemframe.getDisplayedItem();
                //currentPower = entityitemframe.getRotation()*2+1;
                Event ev = (Event) frameConstr.newInstance(entityitemframe, i, currentPower);
    			MinecraftForge.EVENT_BUS.post(ev);
    			currentPower = evPowerField.getInt(ev);
            }
        }
		return currentPower;
	}

	private static EntityItemFrame findItemFrame(World world, int x, int y, int z, int facing) {
		List<EntityItemFrame> list = world.getEntitiesWithinAABB(EntityItemFrame.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
		ArrayList<EntityItemFrame> list2 = new ArrayList<EntityItemFrame>();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			EntityItemFrame frame = (EntityItemFrame) it.next();
			if(frame.hangingDirection == facing) {
				list2.add(frame);
			}
		}
		
        return list2.size() == 1 ? (EntityItemFrame)list.get(0) : null;
	}
	
	public static void updateWorldFromItemFrame(EntityItemFrame frame) {
		StackTraceElement[] causes = Thread.currentThread().getStackTrace();
		if(causes[3].getMethodName().equals("readEntityFromNBT") || causes[3].getMethodName().equals("func_70037_a")) {
			return;
		}
		int ox = -1*Direction.offsetX[frame.hangingDirection];
		int oz = -1*Direction.offsetZ[frame.hangingDirection];
		
		Block block = frame.worldObj.getBlock((int)frame.posX + ox*2, (int)frame.posY, (int)frame.posZ + oz*2);
		block.updateTick(frame.worldObj, (int)frame.posX + ox*2, (int)frame.posY, (int)frame.posZ + oz*2, frame.worldObj.rand);
	}

	/**
	 *
	 * @param theCow
	 * @return if milking is allowed
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static boolean cowMilking(EntityCow theCow) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(cowConstr == null) {
			Class cls = Class.forName("com.draco18s.hardlib.events.EntityAnimalInteractEvent$CowMilkEvent");
			//animalConstr = cls.getConstructors()[0];
			Constructor[] allConstructors = cls.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
		    	Class<?>[] pType  = ctor.getParameterTypes();
				if(pType.length == 1 && pType[0] == EntityCow.class) {
					cowConstr = ctor;
				}
		    }
			//evLoveTimeField = cls.getField("loveTime");
		}
		Event ev = (Event) cowConstr.newInstance(theCow);
		return !MinecraftForge.EVENT_BUS.post(ev);
	}
	
	public static void traceOne() {
		System.out.println("1");
	}
	
	public static void traceTwo() {
		System.out.println("2");
	}
}
