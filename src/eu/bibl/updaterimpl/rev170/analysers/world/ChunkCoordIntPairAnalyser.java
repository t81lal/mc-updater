package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.FieldMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.MappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class ChunkCoordIntPairAnalyser extends Analyser {
	
	public ChunkCoordIntPairAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChunkCoordIntPair", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				/* 0 */new FieldMappingData(new MappingData("getChunkXPos"), new MappingData("I", "I"), false),
				/* 1 */new FieldMappingData(new MappingData("getChunkZPos"), new MappingData("I", "I"), false), };
	}
	
	@Override
	public boolean accept() {
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByRefactoredName("ChunkCoordIntPair");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		for (MethodNode m : InsnUtil.methods(cn, "(II)V")) {
			if (m.name.equals("<init>")) {
				FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
				for (int i = 0; i < fieldHooks.length; i++) {
					addField(fieldHooks[i].buildObf(fins[i]));
				}
				break;
			}
		}
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IChunkCoordIntPair");
	}
	
	private static FieldInsnNode[] getFieldNodes(MethodNode m, int opcode) {
		List<FieldInsnNode> fins = new ArrayList<FieldInsnNode>();
		ListIterator<?> it = m.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) it.next();
			if (ain.getOpcode() == opcode) {
				fins.add((FieldInsnNode) ain);
			}
		}
		return fins.toArray(new FieldInsnNode[fins.size()]);
	}
}