package eu.bibl.updaterimpl.rev170.analysers.nbt;

import eu.bibl.banalysis.analyse.Analyser;
import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;

public abstract class NBTAnalyser extends Analyser {
	
	public NBTAnalyser(String name, ClassContainer container, HookMap hookMap) {
		super(name, container, hookMap);
	}
}
