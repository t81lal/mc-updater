package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import eu.bibl.updaterimpl.rev170.analysers.nbt.NBTAnalyser;

public abstract class NBTTagAnalyser extends NBTAnalyser {
	
	public static String INTERFACES = "eu/bibl/mc/accessors/";
	
	public NBTTagAnalyser(String name) {
		super(name);
	}
}