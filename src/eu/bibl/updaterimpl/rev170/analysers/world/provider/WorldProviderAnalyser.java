package eu.bibl.updaterimpl.rev170.analysers.world.provider;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldProviderAnalyser extends Analyser {
	
	public WorldProviderAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProvider", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProvider");
	}
}
