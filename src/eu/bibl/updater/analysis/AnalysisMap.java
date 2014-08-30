package eu.bibl.updater.analysis;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.jar.ClassParser;
import eu.bibl.bytetools.jar.JarDownloader;
import eu.bibl.bytetools.jar.JarDumper;
import eu.bibl.bytetools.jar.JarType;
import eu.bibl.bytetools.jar.NonMetaJarDumper;
import eu.bibl.updater.analysis.api.AnalysisProvider;
import eu.bibl.updater.loader.Loader;
import eu.bibl.updater.util.APIBuilder;
import eu.bibl.updater.util.HookMap;
import eu.bibl.updater.util.InjectionBuilder;
import eu.bibl.updater.util.Refactorer;
import eu.bibl.updater.util.Timer;

public class AnalysisMap {

	public static boolean logHooks = true;

	protected HookMap hookMap;
	protected AnalysisProvider provider;
	protected MapLink link;

	protected List<Analyser> analysers;

	public AnalysisMap(AnalysisProvider provider, MapLink link) {
		this.provider = provider;
		this.link = link;
		init();
	}

	private void init() {
		analysers = provider.register();
		hookMap = provider.createMap();
	}

	public void analyse() {
		System.out.println("-----------------Running for version " + link.getVersion() + "-------------------");
		Timer timer = new Timer();
		long totalTime = 0;
		for (Analyser a : analysers) {
			a.setAnalysisMap(this);
			a.setHookMap(hookMap);
			timer.reset();
			for (ClassNode cn : link.getDownloader().getParser().getResultantClasses().values()) {
				try {
					a.checkAnalyser(cn);
				} catch (Exception e) {
					System.out.println(a.getName() + "Analyser failed.");
					e.printStackTrace();
				}
			}
			totalTime += timer.stopMs();
		}
		hookMap.print("eu.bibl.mc.accessors", (Loader.VERSIONS_TO_ANALYSE.length == 1) && logHooks, analysers, link.getDownloader().getParser().getResultantClasses());

		System.out.println(" ");
		System.out.println("Analysis took " + totalTime + "ms.");

		File outDir = new File("out/" + link.getVersion());
		if (outDir.exists())
			outDir.delete();
		outDir.mkdir();

		if (!logHooks)
			return;

		hookMap.save(new File(outDir.getAbsolutePath() + "/" + link.getVersion() + ".json"));

		new Refactorer(hookMap, link.getDownloader().getParser().getResultantClasses()).refactor();

		timer.start();
		APIBuilder apiBuilder = new APIBuilder(hookMap);
		HashMap<String, ClassNode> nodes = apiBuilder.build();
		System.out.println("Built API in " + (timer.stop() / 1000000000D) + "ms.");

		timer.start();
		ClassParser.verbose = false;
		JarDownloader redownloader = new JarDownloader(link.getDownloader().filePath, JarType.FILE);
		ClassParser.verbose = true;
		InjectionBuilder injectionBuilder = new InjectionBuilder(hookMap, redownloader.getParser().getResultantClasses());
		HashMap<String, ClassNode> injectedNodes = injectionBuilder.build();
		System.out.println("Built full build in " + (timer.stop() / 1000000000D) + "ms.");
		System.out.println(" ");

		JarDumper apiDumper = new JarDumper(nodes, new HashMap<URL, Map<String, byte[]>>());
		apiDumper.dump(new File("out/api.jar"), true);

		new NonMetaJarDumper(link.getDownloader().getParser()).dump(new File(outDir.getAbsolutePath() + "/" + link.getVersion() + "refac.jar"), true);

		JarDumper buildDumper = new NonMetaJarDumper(injectedNodes, new HashMap<URL, Map<String, byte[]>>());
		buildDumper.addNodes(nodes);
		buildDumper.addResources(redownloader.getParser().getFileBytes());
		buildDumper.dump(new File(outDir.getAbsolutePath() + "/" + link.getVersion() + "build.jar"), true);
	}

	public ClassNode requestNode(String name) {
		if (link.getDownloader().getParser().getResultantClasses().containsKey(name))
			return link.getDownloader().getParser().getResultantClasses().get(name);
		return null;
	}

	public HookMap getMap() {
		return hookMap;
	}
}