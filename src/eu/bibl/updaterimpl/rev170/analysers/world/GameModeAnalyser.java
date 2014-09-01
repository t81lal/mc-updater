package eu.bibl.updaterimpl.rev170.analysers.world;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class GameModeAnalyser extends Analyser {
	
	public GameModeAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameMode", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && InsnUtil.containsLdc(cn, "NOT_SET");
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IGameMode");
	}
}
