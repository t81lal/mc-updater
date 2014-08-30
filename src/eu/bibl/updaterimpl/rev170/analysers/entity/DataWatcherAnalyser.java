package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class DataWatcherAnalyser extends Analyser{

	public DataWatcherAnalyser() {
		super("DataWatcher");
	}

	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "Duplicate id value for ");
	}

	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IDataWatcher"));
	}
}