package eu.bibl.updaterimpl.rev170.analysers.system;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ShutdownHookAnalyser extends Analyser {
	
	public ShutdownHookAnalyser() {
		super("ShutdownHook");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		boolean b = cn.superName.equals("java/lang/Thread");
		if (!b)
			return false;
		for(MethodNode m : methods(cn)) {
			if (m.instructions.size() != 2)
				continue;
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain.getOpcode() == INVOKESTATIC) {
					MethodInsnNode min = (MethodInsnNode) ain;
					if (min.owner.equals(map.getClassByRefactoredName("Minecraft").getObfuscatedName()))
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "system/ShutdownHook"));
		
		for(MethodNode m : methods(cn)) {
			if (!m.name.equals("<init>") && !m.desc.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == INVOKESTATIC) {
						MethodInsnNode min = (MethodInsnNode) ain;
						MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
						analyser.addMinecraftHook(analyser.getMethodHooks()[2].buildObfMin(min));
					}
				}
			}
		}
	}
}