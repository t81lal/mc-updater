package eu.bibl.updater.util;

import java.util.HashMap;
import java.util.ListIterator;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import eu.bibl.bytetools.analysis.storage.hooks.BytecodeMethodHook;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.util.Arrays;
import eu.bibl.bytetools.util.Primitives;
import eu.bibl.updater.analysis.Analyser;

public class Refactorer {
	
	private HookMap map;
	private HashMap<String, ClassNode> nodes;
	
	public Refactorer(HookMap map, HashMap<String, ClassNode> nodes) {
		this.map = map;
		this.nodes = nodes;
	}
	
	public void refactor() {
		Timer timer = new Timer();
		timer.start();
		for(ClassNode cn : nodes.values()) {
			for(FieldNode f : Analyser.fields(cn)) {
				for(FieldHook hook : map.getFieldHooksByObfuscatedName(cn.name)) {
					if (f.name.equals(hook.getObfuscatedName()) && f.desc.equals(hook.getObfuscatedDesc())) {
						f.name = hook.getRefactoredName();
						break;
					}
				}
				f.desc = getDesc(f.desc);
			}
			
			for(MethodNode m : Analyser.methods(cn)) {
				boolean done = false;
				for(MethodHook hook : map.getMethodHooksByObfuscatedName(cn.name)) {
					if (hook instanceof BytecodeMethodHook)
						continue;
					if (m.name.equals(hook.getObfuscatedName()) && m.desc.equals(hook.getObfuscatedDesc())) {
						m.name = hook.getRefactoredName();
						m.desc = hook.getRefactoredDesc();
						done = true;
						break;
					}
				}
				if (!done) {
					Type[] args = Type.getArgumentTypes(m.desc);
					Type ret = Type.getReturnType(m.desc);
					String desc = "(";
					for(Type arg : args) {
						desc += getDesc(arg.toString());
					}
					desc += ")";
					if (ret.toString().equals("V"))
						desc += "V";
					else
						desc += getDesc(ret.toString());
					m.desc = desc;
				}
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain instanceof FieldInsnNode) {
						FieldInsnNode fin = (FieldInsnNode) ain;
						for(FieldHook hook : map.getFieldHooks()) {
							if (isInTree(fin.owner, hook.getOwner()) && fin.name.equals(hook.getObfuscatedName()) && fin.desc.equals(hook.getObfuscatedDesc())) {
								fin.name = hook.getRefactoredName();
								fin.desc = hook.getRefactoredDesc();
								fin.owner = hook.getOwner().getRefactoredName();
								break;
							}
						}
					} else if (ain instanceof MethodInsnNode) {
						MethodInsnNode min = (MethodInsnNode) ain;
						boolean done1 = false;
						for(MethodHook hook : map.getMethodHooks()) {
							if (isInTree(min.owner, hook.getOwner()) && min.name.equals(hook.getObfuscatedName()) && min.desc.equals(hook.getObfuscatedDesc())) {
								min.name = hook.getRefactoredName();
								min.desc = hook.getRefactoredDesc();
								min.owner = hook.getOwner().getRefactoredName();
								done1 = true;
								break;
							}
						}
						if (!done1) {
							min.owner = getClass(min.owner);
						}
					} else if (ain instanceof TypeInsnNode) {
						TypeInsnNode tin = (TypeInsnNode) ain;
						String desc = tin.desc;
						int dims = Arrays.getArrayDimensions(desc);
						desc = desc.replace("[", "");
						if (Primitives.isAbsolutePrimitive(desc))
							continue;
						String newDesc = Arrays.compileArrayString(dims);
						newDesc += getClass(desc);
						tin.desc = newDesc;
					}
				}
			}
		}
		
		for(ClassNode cn : nodes.values()) {
			cn.superName = getClass(cn.superName);
			cn.name = getClass(cn.name);
		}
		
		System.out.println("Refactor took " + timer.stopMs() + "ms.");
	}
	
	private static String fix(String desc) {
		return "L" + desc + ";";
	}
	
	private String getDesc(String desc) {
		int dims = Arrays.getArrayDimensions(desc);
		desc = desc.replace("[", "");
		if (Primitives.isAbsolutePrimitive(desc))
			return desc;
		String newDesc = Arrays.compileArrayString(dims);
		newDesc += fix(getClass(desc.substring(1).replace(";", "")));
		return newDesc;
	}
	
	private String getClass(String obfName) {
		ClassHook hook = map.getClassByObfuscatedName(obfName);
		if (hook != null)
			return hook.getRefactoredName();
		return obfName;
	}
	
	private boolean isInTree(String current, ClassHook check) {
		if (current.contains("java/"))
			return false;
		if (current.equals(check.getObfuscatedName()))
			return true;
		ClassNode currentNode = nodes.get(current);
		if (currentNode == null)
			return false;
		return isInTree(currentNode, check);
	}
	
	private boolean isInTree(ClassNode current, ClassHook check) {
		if (current.name.contains("java/"))
			return false;
		if (current.name.equals(check.getObfuscatedName()))
			return true;
		ClassNode next = nodes.get(current.superName);
		if (next == null)
			return false;
		return isInTree(next, check);
	}
}