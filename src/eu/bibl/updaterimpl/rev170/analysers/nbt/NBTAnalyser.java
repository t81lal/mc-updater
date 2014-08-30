package eu.bibl.updaterimpl.rev170.analysers.nbt;

import eu.bibl.updater.analysis.Analyser;

public abstract class NBTAnalyser extends Analyser {
	
	public static String INTERFACES = "eu/bibl/mc/accessors/";
	
	public NBTAnalyser(String name) {
		super(name);
	}
}