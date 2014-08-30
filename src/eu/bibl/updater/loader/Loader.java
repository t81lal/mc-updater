package eu.bibl.updater.loader;

import eu.bibl.banalysis.analyse.MassAnalyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.updater.analysis.MapLink;
import eu.bibl.updater.analysis.api.AnalysisProvider;
import eu.bibl.updater.analysis.api.ProviderInfo;
import eu.bibl.updaterimpl.rev170.Revision170Provider;

import java.util.HashMap;
import java.util.Map;

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
        HookMap classMapping = new HookMap();
        AnalysisProvider anal /* lol */ = providers.get(this.version);
        MassAnalyser ma = anal.register();
        ma.runSingleAnalysers(link.getDownloader().getContents(), classMapping);
        ma.runSingleAnalysers(link.getDownloader().getContents(), classMapping);
    }

	public static void registerProvider(AnalysisProvider provider) {
		ProviderInfo info = provider.retrieveProviderInfo();
		for (int rev : info.getProviderGameRevisions()) {
			providers.put(rev, provider);
		}
	}
}