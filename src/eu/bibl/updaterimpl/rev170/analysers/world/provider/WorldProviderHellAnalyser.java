package eu.bibl.updaterimpl.rev170.analysers.world.provider;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class WorldProviderHellAnalyser extends Analyser {
	
	public WorldProviderHellAnalyser() {
		super("WorldProviderHell");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return containsLdc(cn, "Nether");
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "world/provider/IWorldProviderHell"));
		
		map.addClass(new ClassHook(cn.superName, "WorldProvider"));
		
	}
}