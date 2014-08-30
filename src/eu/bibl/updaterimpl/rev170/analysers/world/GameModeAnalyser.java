package eu.bibl.updaterimpl.rev170.analysers.world;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class GameModeAnalyser extends Analyser {
	
	public GameModeAnalyser() {
		super("GameMode");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "NOT_SET");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/IGameMode"));
		
	}
}