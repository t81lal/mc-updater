package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class NBTTagEndAnalyser extends NBTTagAnalyser {
	
	public NBTTagEndAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagEnd", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagEnd").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagEnd");
	}
}
