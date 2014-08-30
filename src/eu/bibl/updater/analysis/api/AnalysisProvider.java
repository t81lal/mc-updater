package eu.bibl.updater.analysis.api;

import eu.bibl.banalysis.analyse.MassAnalyser;


public abstract interface AnalysisProvider {
	
	public abstract ProviderInfo retrieveProviderInfo();
	
	public abstract MassAnalyser register();
}