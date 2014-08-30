package eu.bibl.updater.loader;

import java.util.HashMap;
import java.util.Map;

import eu.bibl.updater.analysis.AnalysisMap;
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
	public static final int[] VERSIONS_TO_ANALYSE = { 179 };

	private Map<Integer, AnalysisMap> maps;

	public Loader() {
		maps = new HashMap<Integer, AnalysisMap>();
	}

	public void load() {
		for (int i = 0; i < VERSIONS_TO_ANALYSE.length; i++) {
			int version = VERSIONS_TO_ANALYSE[i];
			MapLink link = new MapLink(version);
			if (link.successful()) {
				AnalysisMap map1 = new AnalysisMap(providers.get(version), link);
				maps.put(i, map1);
			} else {
				System.out.println("Could not load version " + version);
			}
		}
		System.out.println("Loaded " + maps.size() + " maps.");
	}

	public void run() {
		for (Integer version : maps.keySet()) {
			maps.get(version).analyse();
		}
	}

	public static void registerProvider(AnalysisProvider provider) {
		ProviderInfo info = provider.retrieveProviderInfo();
		for (int rev : info.getProviderGameRevisions()) {
			providers.put(rev, provider);
		}
	}
}