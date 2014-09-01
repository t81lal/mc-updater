package eu.bibl.updater.analysis.api;

import eu.bibl.banalysis.analyse.MassAnalyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;

public abstract interface AnalysisProvider {
	
	public abstract ProviderInfo retrieveProviderInfo();
	
	public abstract MassAnalyser register(ClassContainer container, HookMap hookMap);
}