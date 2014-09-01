package eu.bibl.updaterimpl.rev170.analysers.entity;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class PlayerControllerMPAnalyser extends Analyser {
	public PlayerControllerMPAnalyser(ClassContainer container, HookMap hookMap) {
		super("PlayerControllerMP", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "Disconnected from server");
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IPlayerControllerMP");
		
	}
	
	// private void find
}
