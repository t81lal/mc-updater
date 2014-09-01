package eu.bibl.updaterimpl.rev170.analysers.world;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.analyse.AnalyserCache;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.InterfaceMappingData;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updater.util.InsnUtil;
import eu.bibl.updaterimpl.rev170.analysers.MinecraftAnalyser;

public class DifficultyAnalyser extends Analyser {
	
	public DifficultyAnalyser(ClassContainer container, HookMap hookMap) {
		super("Difficulty", container, hookMap);
	}
	
	@Override
	public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && InsnUtil.containsLdc(cn, "options.difficulty.peaceful");
	}
	
	@Override
	public InterfaceMappingData run() {
		MinecraftAnalyser analyser = (MinecraftAnalyser) AnalyserCache.contextGet("Minecraft");
		analyser.addField(analyser.getFieldHooks()[10].buildObf(InsnUtil.fields(cn, "[L" + cn.name + ";").get(0)));
		return new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IDifficulty");
	}
}
