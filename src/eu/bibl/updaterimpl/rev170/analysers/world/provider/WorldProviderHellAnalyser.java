package eu.bibl.updaterimpl.rev170.analysers.world.provider;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.ClassMappingData;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class WorldProviderHellAnalyser extends Analyser {
	
	public WorldProviderHellAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProviderHell", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "Nether");
	}
	
	@Override
	public InterfaceMappingData run() {
		hookMap.addClass(new ClassMappingData(cn.superName, "WorldProvider", null));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProviderHell");
	}
}
