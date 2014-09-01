package eu.bibl.updaterimpl.rev170.analysers.system;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ShutdownHookAnalyser extends Analyser {
	
	public ShutdownHookAnalyser(ClassContainer container, HookMap hookMap) {
		super("ShutdownHook", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		boolean b = cn.superName.equals("java/lang/Thread");
		if (!b)
			return false;
		for (MethodNode m : cn.methods()) {
			if (m.instructions.size() != 2)
				continue;
			ListIterator<?> it = m.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain.getOpcode() == INVOKESTATIC) {
					MethodInsnNode min = (MethodInsnNode) ain;
					if (min.owner.equals(hookMap.getClassByRefactoredName("Minecraft").getObfuscatedName()))
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode m : cn.methods()) {
			if (!m.name.equals("<init>") && !m.desc.equals("<clinit>")) {
				ListIterator<?> it = m.instructions.iterator();
				while (it.hasNext()) {
					AbstractInsnNode ain = (AbstractInsnNode) it.next();
					if (ain.getOpcode() == INVOKESTATIC) {
						MethodInsnNode min = (MethodInsnNode) ain;
						MinecraftAnalyser analyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
						analyser.addMethod(analyser.getMethodHooks()[2].buildObfMethod(min));
					}
				}
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "system/ShutdownHook");
	}
}
