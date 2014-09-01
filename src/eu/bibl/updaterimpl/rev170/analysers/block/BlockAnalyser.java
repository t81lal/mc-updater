package eu.bibl.updaterimpl.rev170.analysers.block;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class BlockAnalyser extends Analyser {
	
	public BlockAnalyser(ClassContainer container, HookMap hookMap) {
		super("Block", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return InsnUtil.containsLdc(cn, "dig.glass");
	}
	
	@Override
	public InterfaceMappingData run() {
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "block/IBlock");
	}
}
