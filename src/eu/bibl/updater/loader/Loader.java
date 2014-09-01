package eu.bibl.updater.loader;

import java.util.HashMap;
import java.util.Map;

import eu.bibl.banalysis.analyse.MassAnalyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.analysis.MapLink;
import eu.bibl.updater.analysis.api.AnalysisProvider;
import eu.bibl.updater.analysis.api.ProviderInfo;
import eu.bibl.updaterimpl.rev170.Revision170Provider;

public class Loader {
	
	static {
		providers = new HashMap<Integer, AnalysisProvider>();
		registerProvider(new Revision170Provider());
	}
	
	private static Map<Integer, AnalysisProvider> providers;
	
	private int version;
	private MapLink link;
	
	public Loader(int version) {
		this.version = version;
		if (providers.get(version) == null)
			throw new RuntimeException("Analyser does not exist!");
		link = new MapLink(version);
	}
	
	public void load() {
		if (link.successful())
			System.out.println("Loaded map for v" + version);
	}
	
	public void run() {
		HookMap hookMap = new HookMap();
		AnalysisProvider anal /* lol */= providers.get(version);
		ClassContainer container = link.getDownloader().getContents();
		MassAnalyser ma = anal.register(container, hookMap);
		ma.runSingleAnalysers(container, hookMap);
		ma.runSingleAnalysers(container, hookMap); // TODO: wtf r u doin here
	}
	
	public static void registerProvider(AnalysisProvider provider) {
		ProviderInfo info = provider.retrieveProviderInfo();
		for (int rev : info.getProviderGameRevisions()) {
			providers.put(rev, provider);
		}
	}
}