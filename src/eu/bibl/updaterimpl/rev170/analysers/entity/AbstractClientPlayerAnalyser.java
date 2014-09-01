package eu.bibl.updaterimpl.rev170.analysers.entity;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class AbstractClientPlayerAnalyser extends Analyser {
	
	public AbstractClientPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("AbstractClientPlayer", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		ClassMappingData c = (ClassMappingData) hookMap.getClassByObfuscatedName(cn.name);
		if ((c != null) && c.getRefactoredName().equals("AbstractClientPlayer"))
			return true;
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "EntityPlayer", null));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IAbstractClientPlayer");
	}
}
