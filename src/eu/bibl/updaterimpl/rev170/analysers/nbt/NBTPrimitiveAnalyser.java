package eu.bibl.updaterimpl.rev170.analysers.nbt;

import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTPrimitiveAnalyser extends NBTAnalyser {
	
	public NBTPrimitiveAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTPrimitive", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return ((cn.access & ACC_ABSTRACT) != 0) && cn.superName.equals(hookMap.getClassByRefactoredName("NBTBase").getObfuscatedName());
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive");
	}
}
