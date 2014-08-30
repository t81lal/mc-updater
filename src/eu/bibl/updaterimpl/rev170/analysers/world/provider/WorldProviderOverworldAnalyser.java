package eu.bibl.updaterimpl.rev170.analysers.world.provider;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class WorldProviderOverworldAnalyser extends Analyser {
	
	public WorldProviderOverworldAnalyser() {
		super("Overworld");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook hook = map.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		if (hook.getObfuscatedName().equals(cn.superName))
			return containsLdc(cn, "Overworld");
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/provider/IWorldProviderOverworld"));
	}
}