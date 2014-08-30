package eu.bibl.updaterimpl.rev170.analysers.entity;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.ClassHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.updater.analysis.Analyser;

public class AbstractClientPlayerAnalyser extends Analyser {
	
	public AbstractClientPlayerAnalyser() {
		super("AbstractClientPlayer");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		ClassHook c = map.getClassByObfuscatedName(cn.name);
		if (c != null && c.getRefactoredName().equals("AbstractClientPlayer"))
			return true;
		return false;
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "entity/IAbstractClientPlayer", INTERFACES + "entity/IEntityPlayer"));
		
		map.addClass(new ClassHook(cn.superName, "EntityPlayer"));
	}
}