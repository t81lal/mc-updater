package eu.bibl.updaterimpl.rev170.analysers.entity;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class DataWatcherAnalyser extends Analyser {
	
	public DataWatcherAnalyser(ClassContainer container, HookMap hookMap) {
		super("DataWatcher", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "Duplicate id value for ");
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IDataWatcher");
	}
}
