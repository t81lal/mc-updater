package eu.bibl.updaterimpl.rev170.analysers.block;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class BlockAnalyser extends Analyser {
	
	public BlockAnalyser() {
		super("Block");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "dig.glass");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "block/IBlock"));
	}
}