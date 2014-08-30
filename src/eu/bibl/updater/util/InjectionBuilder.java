package eu.bibl.updater.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import eu.bibl.bytetools.analysis.Analyser;
import eu.bibl.bytetools.analysis.storage.hooks.BytecodeMethodHook;
import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;
import eu.bibl.bytetools.generator.CodeGenerationFactory;
import eu.bibl.bytetools.generator.GetterGenerationFactory;
import eu.bibl.bytetools.util.Access;
import eu.bibl.bytetools.util.OpcodeInfo;

public class InjectionBuilder implements Opcodes {
	
	private HookMap map;
	private HashMap<String, ClassNode> nodes;
	
	public InjectionBuilder(HookMap map, HashMap<String, ClassNode> nodes) {
		this.map = map;
		this.nodes = nodes;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, ClassNode> build() {
		HashMap<String, ClassNode> nodes = new HashMap<String, ClassNode>();
		
		GetterGenerationFactory getterFactory = (GetterGenerationFactory) CodeGenerationFactory.getFactory(GetterGenerationFactory.class);
		for(ClassNode cn : this.nodes.values()) {
			nodes.put(cn.name, cn);
			ClassHook hook = map.getClassByObfuscatedName(cn.name);
			if (hook == null)
				continue;
			if (hook.getInterfaceHook() != null) {
				cn.interfaces.add(hook.getInterfaceHook().getInterfaceName());
			}
			
			HashMap<String, ArrayList<FieldHook>> obfuscatedFields = map.getObfFieldHooks();
			for(String s : obfuscatedFields.keySet()) {
				for(FieldHook f : obfuscatedFields.get(s)) {
					String oName = f.getOwner().getObfuscatedName().split("\\$")[0];
					if (s.startsWith(hook.getObfuscatedName()) || oName.equals(hook.getRefactoredName())) {
						MethodNode getter = new MethodNode(ACC_PUBLIC, f.getRefactoredName(), "()" + f.getRefactoredDesc(), null, null);
						String owner = f.getOwner().getObfuscatedName();
						if (owner.contains("$")) {
							owner = eu.bibl.bytetools.analysis.storage.hooks.HookMap.getNameSecondPart(owner);
						}
						InsnList getterInsns = getterFactory.getInstructions(new Object[] {
								f.isStatic(),
								owner,
								f.getObfuscatedName(),
								f.getObfuscatedDesc(),
								true });
						getter.instructions.add(getterInsns);
						cn.methods.add(getter);
						
						String stripped = f.getRefactoredName().replace("get", "");
						String setterName = "set" + (stripped.charAt(0) + "").toUpperCase() + stripped.substring(1);
						String setterDesc = "(" + f.getRefactoredDesc() + ")V";
						MethodNode setter = new MethodNode(ACC_PUBLIC, setterName, setterDesc, null, null);
						int setIndex = 0;
						if (!f.isStatic())
							setter.instructions.add(new VarInsnNode(ALOAD, setIndex++));
						setter.instructions.add(new VarInsnNode(OpcodeInfo.getLoadOpcode(f.getRefactoredDesc()), setIndex++));
						setter.instructions.add(new FieldInsnNode(f.isStatic() ? PUTSTATIC : PUTFIELD, owner, f.getObfuscatedName(), f.getObfuscatedDesc()));
						setter.instructions.add(new InsnNode(RETURN));
						cn.methods.add(setter);
					}
				}
			}
			
			HashMap<String, ArrayList<MethodHook>> obfuscatedMethods = map.getObfMethodHooks();
			for(String s : obfuscatedMethods.keySet()) {
				for(MethodHook m1 : obfuscatedMethods.get(s)) {
					String oName = m1.getOwner().getObfuscatedName().split("\\$")[0];
					if (s.startsWith(hook.getObfuscatedName()) || oName.equals(hook.getRefactoredName())) {
						MethodNode m = new MethodNode(ACC_PUBLIC, m1.getRefactoredName(), m1.getRefactoredDesc(), null, null);
						if (m1 instanceof BytecodeMethodHook) {
							m.instructions.add(((BytecodeMethodHook) m1).getInstructions());
						} else {
							String owner = m1.getOwner().getObfuscatedName();
							if (owner.contains("$")) {
								owner = eu.bibl.bytetools.analysis.storage.hooks.HookMap.getNameSecondPart(owner);
							}
							MethodNode call = getCall(owner, m1.getObfuscatedName(), m1.getObfuscatedDesc());
							InsnList insns = new InsnList();
							int index = 0;
							if (!Access.isStatic(call.access))
								insns.add(new VarInsnNode(ALOAD, index++));
							for(Type arg : Type.getArgumentTypes(call.desc)) {
								insns.add(new VarInsnNode(OpcodeInfo.getLoadOpcode(arg.toString()), index++));
							}
							insns.add(new MethodInsnNode(Access.isStatic(call.access) ? INVOKESTATIC : INVOKEVIRTUAL, owner, call.name, call.desc));
							insns.add(new InsnNode(OpcodeInfo.getReturnOpcode(Type.getReturnType(m.desc).toString())));
							m.instructions.add(insns);
						}
						cn.methods.add(m);
					}
				}
			}
		}
		return nodes;
	}
	
	private MethodNode getCall(String owner, String name, String desc) {
		for(MethodNode m : Analyser.methods(nodes.get(owner))) {
			if (m.name.equals(name) && m.desc.equals(desc))
				return m;
		}
		return null;
	}
}