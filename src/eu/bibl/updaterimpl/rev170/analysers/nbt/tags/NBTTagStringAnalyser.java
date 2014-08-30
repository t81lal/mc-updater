package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagStringAnalyser extends NBTTagAnalyser {
	
	public NBTTagStringAnalyser() {
		super("NBTTagString");
		hooks = new FieldHook[] { new FieldHook("getData", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagString").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagString", INTERFACES + "nbt/INBTBase"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
	}
}