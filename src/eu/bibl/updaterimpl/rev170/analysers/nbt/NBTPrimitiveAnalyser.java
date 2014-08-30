package eu.bibl.updaterimpl.rev170.analysers.nbt;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;
import eu.bibl.bytetools.util.Access;

public class NBTPrimitiveAnalyser extends NBTAnalyser {
	
	public NBTPrimitiveAnalyser() {
		super("NBTPrimitive");
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return Access.isAbstract(cn.access) && cn.superName.equals(map.getClassByRefactoredName("NBTBase").getObfuscatedName());
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/INBTPrimitive", INTERFACES + "nbt/INBTBase"));
	}
}