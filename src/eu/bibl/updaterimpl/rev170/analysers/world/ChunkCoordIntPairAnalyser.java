package eu.bibl.updaterimpl.rev170.analysers.world;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class ChunkCoordIntPairAnalyser extends Analyser {
	
	public ChunkCoordIntPairAnalyser() {
		super("ChunkCoordIntPair");
		hooks = new FieldHook[] {
				new FieldHook("getChunkXPos", "I", "I"),
				new FieldHook("getChunkZPos", "I", "I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook hook = map.getClassByRefactoredName("ChunkCoordIntPair");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IChunkCoordIntPair"));
		for(MethodNode m : methods(cn, "(II)V")) {
			if (m.name.equals("<init>")) {
				FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
				for(int i = 0; i < hooks.length; i++) {
					addHook(hooks[i].buildObfFin(fins[i]));
				}
				break;
			}
		}
	}
}