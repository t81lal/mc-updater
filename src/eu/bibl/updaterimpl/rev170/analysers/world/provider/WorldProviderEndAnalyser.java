package eu.bibl.updaterimpl.rev170.analysers.world.provider;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldProviderEndAnalyser extends Analyser {
	
	public WorldProviderEndAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProviderEnd", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		ClassMappingData hook = (ClassMappingData) hookMap.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		if (hook.getObfuscatedName().equals(cn.superName))
			return InsnUtil.containsLdc(cn, "The End");
		return false;
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProviderEnd");
	}
}
