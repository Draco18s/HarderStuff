package com.draco18s.cropcore.asm;

import static org.apache.logging.log4j.Level.TRACE;
import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.VOID_TYPE;



import java.util.Iterator;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.FMLLog;

/**
 * A class to help with the use of ASM, and particularly the ASM Tree API.
 */
public class ASMHelper {
	private ASMHelper(){} // No constructor for you!
	
	private static final BiMap<String, Integer> opCodes = HashBiMap.create(166);
	static {
		opCodes.put("NOP", NOP);
        opCodes.put("ACONST_NULL", ACONST_NULL);
        opCodes.put("ICONST_M1", ICONST_M1);
        opCodes.put("ICONST_0", ICONST_0);
        opCodes.put("ICONST_1", ICONST_1);
        opCodes.put("ICONST_2", ICONST_2);
        opCodes.put("ICONST_3", ICONST_3);
        opCodes.put("ICONST_4", ICONST_4);
        opCodes.put("ICONST_5", ICONST_5);
        opCodes.put("LCONST_0", LCONST_0);
        opCodes.put("LCONST_1", LCONST_1);
        opCodes.put("FCONST_0", FCONST_0);
        opCodes.put("FCONST_1", FCONST_1);
        opCodes.put("FCONST_2", FCONST_2);
        opCodes.put("DCONST_0", DCONST_0);
        opCodes.put("DCONST_1", DCONST_1);
        opCodes.put("BIPUSH", BIPUSH);
        opCodes.put("SIPUSH", SIPUSH);
        opCodes.put("LDC", LDC);
        opCodes.put("ILOAD", ILOAD);
        opCodes.put("LLOAD", LLOAD);
        opCodes.put("FLOAD", FLOAD);
        opCodes.put("DLOAD", DLOAD);
        opCodes.put("ALOAD", ALOAD);
        opCodes.put("IALOAD", IALOAD);
        opCodes.put("LALOAD", LALOAD);
        opCodes.put("FALOAD", FALOAD);
        opCodes.put("DALOAD", DALOAD);
        opCodes.put("AALOAD", AALOAD);
        opCodes.put("BALOAD", BALOAD);
        opCodes.put("CALOAD", CALOAD);
        opCodes.put("SALOAD", SALOAD);
        opCodes.put("ISTORE", ISTORE);
        opCodes.put("LSTORE", LSTORE);
        opCodes.put("FSTORE", FSTORE);
        opCodes.put("DSTORE", DSTORE);
        opCodes.put("ASTORE", ASTORE);
        opCodes.put("IASTORE", IASTORE);
        opCodes.put("LASTORE", LASTORE);
        opCodes.put("FASTORE", FASTORE);
        opCodes.put("DASTORE", DASTORE);
        opCodes.put("AASTORE", AASTORE);
        opCodes.put("BASTORE", BASTORE);
        opCodes.put("CASTORE", CASTORE);
        opCodes.put("SASTORE", SASTORE);
        opCodes.put("POP", POP);
        opCodes.put("POP2", POP2);
        opCodes.put("DUP", DUP);
        opCodes.put("DUP_X1", DUP_X1);
        opCodes.put("DUP_X2", DUP_X2);
        opCodes.put("DUP2", DUP2);
        opCodes.put("DUP2_X1", DUP2_X1);
        opCodes.put("DUP2_X2", DUP2_X2);
        opCodes.put("SWAP", SWAP);
        opCodes.put("IADD", IADD);
        opCodes.put("LADD", LADD);
        opCodes.put("FADD", FADD);
        opCodes.put("DADD", DADD);
        opCodes.put("ISUB", ISUB);
        opCodes.put("LSUB", LSUB);
        opCodes.put("FSUB", FSUB);
        opCodes.put("DSUB", DSUB);
        opCodes.put("IMUL", IMUL);
        opCodes.put("LMUL", LMUL);
        opCodes.put("FMUL", FMUL);
        opCodes.put("DMUL", DMUL);
        opCodes.put("IDIV", IDIV);
        opCodes.put("LDIV", LDIV);
        opCodes.put("FDIV", FDIV);
        opCodes.put("DDIV", DDIV);
        opCodes.put("IREM", IREM);
        opCodes.put("LREM", LREM);
        opCodes.put("FREM", FREM);
        opCodes.put("DREM", DREM);
        opCodes.put("INEG", INEG);
        opCodes.put("LNEG", LNEG);
        opCodes.put("FNEG", FNEG);
        opCodes.put("DNEG", DNEG);
        opCodes.put("ISHL", ISHL);
        opCodes.put("LSHL", LSHL);
        opCodes.put("ISHR", ISHR);
        opCodes.put("LSHR", LSHR);
        opCodes.put("IUSHR", IUSHR);
        opCodes.put("LUSHR", LUSHR);
        opCodes.put("IAND", IAND);
        opCodes.put("LAND", LAND);
        opCodes.put("IOR", IOR);
        opCodes.put("LOR", LOR);
        opCodes.put("IXOR", IXOR);
        opCodes.put("LXOR", LXOR);
        opCodes.put("IINC", IINC);
        opCodes.put("I2L", I2L);
        opCodes.put("I2F", I2F);
        opCodes.put("I2D", I2D);
        opCodes.put("L2I", L2I);
        opCodes.put("L2F", L2F);
        opCodes.put("L2D", L2D);
        opCodes.put("F2I", F2I);
        opCodes.put("F2L", F2L);
        opCodes.put("F2D", F2D);
        opCodes.put("D2I", D2I);
        opCodes.put("D2L", D2L);
        opCodes.put("D2F", D2F);
        opCodes.put("I2B", I2B);
        opCodes.put("I2C", I2C);
        opCodes.put("I2S", I2S);
        opCodes.put("LCMP", LCMP);
        opCodes.put("FCMPL", FCMPL);
        opCodes.put("FCMPG", FCMPG);
        opCodes.put("DCMPL", DCMPL);
        opCodes.put("DCMPG", DCMPG);
        opCodes.put("IFEQ", IFEQ);
        opCodes.put("IFNE", IFNE);
        opCodes.put("IFLT", IFLT);
        opCodes.put("IFGE", IFGE);
        opCodes.put("IFGT", IFGT);
        opCodes.put("IFLE", IFLE);
        opCodes.put("IF_ICMPEQ", IF_ICMPEQ);
        opCodes.put("IF_ICMPNE", IF_ICMPNE);
        opCodes.put("IF_ICMPLT", IF_ICMPLT);
        opCodes.put("IF_ICMPGE", IF_ICMPGE);
        opCodes.put("IF_ICMPGT", IF_ICMPGT);
        opCodes.put("IF_ICMPLE", IF_ICMPLE);
        opCodes.put("IF_ACMPEQ", IF_ACMPEQ);
        opCodes.put("IF_ACMPNE", IF_ACMPNE);
        opCodes.put("GOTO", GOTO);
        opCodes.put("JSR", JSR);
        opCodes.put("RET", RET);
        opCodes.put("TABLESWITCH", TABLESWITCH);
        opCodes.put("LOOKUPSWITCH", LOOKUPSWITCH);
        opCodes.put("IRETURN", IRETURN);
        opCodes.put("LRETURN", LRETURN);
        opCodes.put("FRETURN", FRETURN);
        opCodes.put("DRETURN", DRETURN);
        opCodes.put("ARETURN", ARETURN);
        opCodes.put("RETURN", RETURN);
        opCodes.put("GETSTATIC", GETSTATIC);
        opCodes.put("PUTSTATIC", PUTSTATIC);
        opCodes.put("GETFIELD", GETFIELD);
        opCodes.put("PUTFIELD", PUTFIELD);
        opCodes.put("INVOKEVIRTUAL", INVOKEVIRTUAL);
        opCodes.put("INVOKESPECIAL", INVOKESPECIAL);
        opCodes.put("INVOKESTATIC", INVOKESTATIC);
        opCodes.put("INVOKEINTERFACE", INVOKEINTERFACE);
        opCodes.put("INVOKEDYNAMIC", INVOKEDYNAMIC);
        opCodes.put("NEW", NEW);
        opCodes.put("NEWARRAY", NEWARRAY);
        opCodes.put("ANEWARRAY", ANEWARRAY);
        opCodes.put("ARRAYLENGTH", ARRAYLENGTH);
        opCodes.put("ATHROW", ATHROW);
        opCodes.put("CHECKCAST", CHECKCAST);
        opCodes.put("INSTANCEOF", INSTANCEOF);
        opCodes.put("MONITORENTER", MONITORENTER);
        opCodes.put("MONITOREXIT", MONITOREXIT);
        opCodes.put("MULTIANEWARRAY", MULTIANEWARRAY);
        opCodes.put("IFNULL", IFNULL);
        opCodes.put("IFNONNULL", IFNONNULL);
	}
	
	/**
	 * Creates a ClassNode out of the basic bytes of the Class.
	 * @param basicClass - the byte array to construct the ClassNode from.
	 * @return the ClassNode.
	 */
	public static ClassNode createClassNode(byte[] basicClass) {
		ClassReader reader = new ClassReader(basicClass);
		ClassNode classNode = new ClassNode(ASM4);
		reader.accept(classNode, EXPAND_FRAMES);
		FMLLog.log("S1LIB", TRACE, "Beginning to patch %s", classNode.name);
		return classNode;
	}
	
	/**
	 * Turns a ClassNode back to a byte array.
	 * @return the new byte array.
	 */
	public static byte[] makeBytes(ClassNode classNode) {
		return makeBytes(classNode, COMPUTE_FRAMES);
	}
	
	/**
	 * Flag sensitive version of {@link #makeBytes(ClassNode)}.
	 */
	public static byte[] makeBytes(ClassNode classNode, int flags) {
		ClassWriter writer = new ClassWriter(flags);
		classNode.accept(writer);
		FMLLog.log("S1LIB", TRACE, "Completed patching %s", classNode.name);
		return writer.toByteArray();
	}
	
	/**
	 * Finds a MethodNode in a ClassNode.
	 * @param clazz - the ClassNode to find the method in.
	 * @param obf - the obfuscated name of the method
	 * @param deobf - the deobfuscated name of the method
	 * @param returnType - the return type of the method.
	 * @param argumentTypes - the argument types. If none, use an empty Type array.
	 * @return the MethodNode that matches the input, or null if none was found.
	 */
	public static MethodNode findMethod(ClassNode clazz, String obf,
			String deobf, Type returnType, Type... argumentTypes) {
		final String desc = Type.getMethodDescriptor(returnType, argumentTypes);
		for (MethodNode node : clazz.methods) {
			if ((node.name.equals(obf) || node.name.equals(deobf))
					&& node.desc.equals(desc)) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Finds a MethodNode in a ClassNode. Use this version if the method doesn't have two names (obf and deobf). 
	 * @param clazz - the ClassNode to find the method in.
	 * @param name - the name of the method.
	 * @param returnType - the return type of the method.
	 * @param argumentTypes - the argument types. If none, use an empty Type array.
	 * @return the MethodNode that matches the input, or null if none was found.
	 */
	public static MethodNode findMethod(ClassNode clazz, String name, Type returnType, Type... argumentTypes) {
		final String desc = Type.getMethodDescriptor(returnType, argumentTypes);
		for(MethodNode node : clazz.methods) {
			if(node.name.equals(name) && node.desc.equals(desc)) {
				return node;
			}
			else if(node.name.equals(name)) {
				System.out.println("Did you mean '" + node.desc + "' instead of '" + desc + "'?");
			}
		}
		return null;
	}
	
	/**
	 * Finds a constructor MethodNode in a ClassNode.
	 * @param clazz - the ClassNode to find the constructor in.
	 * @param argumentTypes - the argument types. If none, use an empty Type array.
	 * @return the constructor MethodNode that matches the input, or null if none as found.
	 */
	public static MethodNode findConstructor(ClassNode clazz, Type... argumentTypes) {
		return findMethod(clazz, "<init>", VOID_TYPE, argumentTypes);
	}
	
	/**
	 * Finds the first occurrence of an AbstractInsnNode in an InsnList.
	 * @param opcode - the Opcode of the instruction. {@link Opcodes}
	 * @param instructions - this list of instructions to find the instruction in.
	 * @return the first AbstractInsnNode in the list of instructions, or null if none was found.
	 */
	public static AbstractInsnNode findFirst(int opcode, InsnList instructions) {
		Iterator<AbstractInsnNode> it = instructions.iterator();
		while(it.hasNext()) {
			AbstractInsnNode node = it.next();
			if(node.getOpcode() == opcode) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Finds the last occurrence of an AbstractInsnNode in an InsnList.
	 * @param opcode - the Opcode of the instruction. {@link Opcodes}
	 * @param instructions - this list of instructions to find the instruction in.
	 * @return the last AbstractInsnNode in the list of instructions, or null if none was found.
	 */
	public static AbstractInsnNode findLast(int opcode, InsnList instructions) {
		Iterator<AbstractInsnNode> it = instructions.iterator();
		AbstractInsnNode last = null;
		while(it.hasNext()) {
			AbstractInsnNode node = it.next();
			if(node.getOpcode() == opcode) {
				last = node;
			}
		}
		return last;
	}
	
	/**
	 * Prints out all instructions of a method to {@code System.out}.
	 * @param method - the method who's instructions should be printed out.
	 */
	public static void printOpcodes(MethodNode method) {
		if(method == null) {
			System.out.println("Method was null!");
			return;
		}
		Iterator<AbstractInsnNode> it = method.instructions.iterator();
		while(it.hasNext()) {
			AbstractInsnNode node = it.next();
			System.out.println(getOpcode(node.getOpcode()));
			if(node instanceof InvokeDynamicInsnNode) {
				InvokeDynamicInsnNode dyn = (InvokeDynamicInsnNode)node;
				System.out.println("  -->" + dyn.name);
			}
			else if(node instanceof MethodInsnNode) {
				MethodInsnNode dyn = (MethodInsnNode)node;
				System.out.println("  -->" + dyn.name);
			}
			else if(node instanceof IntInsnNode) {
				IntInsnNode dyn = (IntInsnNode)node;
				System.out.println("  -->" + dyn.operand);
			}
			/*else if(node.getOpcode() == -1) {
				System.out.println("  :" + node.getType());
			}*/
			else if(node.getOpcode() == Opcodes.ALOAD || node.getOpcode() == Opcodes.ILOAD) {
				VarInsnNode va = (VarInsnNode)node;
				System.out.println("  :" + va.var);
			}
		}
	}
	
	public static String getOpcode(int opcode) { //TODO javadoc
		String s = opCodes.inverse().get(opcode);
		if(s == null) {
			s = opcode + "";
		}
		return s;
	}
	
	public static int getOpcodeFromString(String string) { //TODO javadoc
		Integer opCode = opCodes.get(string);
		if(opCode == null) {
			opCode = intParseSafe(string);
		}
		return opCode != null ? opCode : 0;
	}
	
	public static Integer intParseSafe(String string) {
		try {
			return Integer.valueOf(string);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
}
