package eu.bibl.updater.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.MethodHook;

public class APIBuilder implements Opcodes {
	
	private HookMap map;
	private ArrayList<ClassHook> hooks;
	
	public APIBuilder(HookMap map) {
		this.map = map;
		hooks = map.getSortedClassHooks();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, ClassNode> build() {
		HashMap<String, ClassNode> nodes = new HashMap<String, ClassNode>();
		for(ClassHook hook : hooks) {
			ClassNode node = new ClassNode();
			if (hook.getInterfaceHook() == null)
				continue;
			node.version = V1_6;
			node.name = hook.getInterfaceHook().getInterfaceName();
			node.access = ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT;
			node.superName = "java/lang/Object";
			if (!hook.getInterfaceHook().getSuperClass().equals("java/lang/Object"))
				node.interfaces.add(hook.getInterfaceHook().getSuperClass());
			
			HashMap<String, ArrayList<FieldHook>> obfuscatedFields = map.getObfFieldHooks();
			for(String s : obfuscatedFields.keySet()) {
				for(FieldHook f : obfuscatedFields.get(s)) {
					String oName = f.getOwner().getObfuscatedName().split("\\$")[0];
					if (s.startsWith(hook.getObfuscatedName()) || oName.equals(hook.getRefactoredName())) {
						MethodNode getter = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT, f.getRefactoredName(), "()" + f.getRefactoredDesc(), null, null);
						node.methods.add(getter);
						String stripped = f.getRefactoredName().replace("get", "");
						String setterName = "set" + (stripped.charAt(0) + "").toUpperCase() + stripped.substring(1);
						String setterDesc = "(" + f.getRefactoredDesc() + ")V";
						MethodNode setter = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT, setterName, setterDesc, null, null);
						node.methods.add(setter);
					}
				}
			}
			
			HashMap<String, ArrayList<MethodHook>> obfuscatedMethods = map.getObfMethodHooks();
			for(String s : obfuscatedMethods.keySet()) {
				for(MethodHook m1 : obfuscatedMethods.get(s)) {
					String oName = m1.getOwner().getObfuscatedName().split("\\$")[0];
					if (s.startsWith(hook.getObfuscatedName()) || oName.equals(hook.getRefactoredName())) {
						MethodNode m = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT, m1.getRefactoredName(), m1.getRefactoredDesc(), null, null);
						node.methods.add(m);
					}
				}
			}
			
			nodes.put(node.name, node);
		}
		return nodes;
	}
}