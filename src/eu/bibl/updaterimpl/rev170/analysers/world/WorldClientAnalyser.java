package eu.bibl.updaterimpl.rev170.analysers.world;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldClientAnalyser extends Analyser {
	
	public WorldClientAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldClient", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		for (MethodNode mNode : cn.methods()) {
			if (!mNode.name.equals("<init>"))
				continue;
			ListIterator<?> it = mNode.instructions.iterator();
			while (it.hasNext()) {
				AbstractInsnNode ain = (AbstractInsnNode) it.next();
				if (ain instanceof LdcInsnNode) {
					LdcInsnNode ldc = (LdcInsnNode) ain;
					if (ldc.cst != null) {
						if (ldc.cst.toString().equals("MpServer")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "World", null));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IWorldClient");
	}
}
