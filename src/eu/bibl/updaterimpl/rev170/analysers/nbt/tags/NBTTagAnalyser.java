package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.banalysis.storage.HookMap;
import eu.bibl.banalysis.storage.classes.ClassContainer;
import eu.bibl.updaterimpl.rev170.analysers.nbt.NBTAnalyser;

public abstract class NBTTagAnalyser extends NBTAnalyser {
	
	public NBTTagAnalyser(String name, ClassContainer container, HookMap hookMap) {
		super(name, container, hookMap);
	}
}
