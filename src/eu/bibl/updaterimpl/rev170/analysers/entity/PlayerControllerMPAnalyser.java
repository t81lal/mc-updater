package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class PlayerControllerMPAnalyser extends Analyser{

	public PlayerControllerMPAnalyser() {
		super("PlayerControllerMP");
	}

	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "Disconnected from server");
	}

	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IPlayerControllerMP"));
		
		
	}
	
//	private void find
}