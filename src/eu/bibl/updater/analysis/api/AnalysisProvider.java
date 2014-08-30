package eu.bibl.updater.analysis.api;

import java.util.List;

import eu.bibl.updater.analysis.Analyser;
import eu.bibl.updater.util.HookMap;

public abstract interface AnalysisProvider {
	
	public abstract ProviderInfo retrieveProviderInfo();
	
	public abstract List<Analyser> register();
	
	public abstract HookMap createMap();
}