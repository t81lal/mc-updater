package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagIntAnalyser extends NBTTagAnalyser {
	
	public NBTTagIntAnalyser() {
		super("NBTTagInt");
		hooks = new FieldHook[] { new FieldHook("getData", "I", "I") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagInt").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagInt", INTERFACES + "nbt/INBTPrimitive"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}