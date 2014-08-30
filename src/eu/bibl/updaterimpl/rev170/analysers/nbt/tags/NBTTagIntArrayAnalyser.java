package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagIntArrayAnalyser extends NBTTagAnalyser {
	
	public NBTTagIntArrayAnalyser() {
		super("NBTTagIntArray");
		hooks = new FieldHook[] { new FieldHook("getData", "[I", "[I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagIntArray").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagIntArray", INTERFACES + "nbt/INBTBase"));
		addHook(hooks[0].buildObfFn(fields(cn, "[I").get(0)));
	}
}