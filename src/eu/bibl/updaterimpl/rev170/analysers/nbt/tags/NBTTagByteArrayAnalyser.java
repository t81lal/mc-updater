package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagByteArrayAnalyser extends NBTTagAnalyser {
	
	public NBTTagByteArrayAnalyser() {
		super("NBTTagByteArray");
		hooks = new FieldHook[] { new FieldHook("getData", "[B", "[B") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagByteArray").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagByteArray", INTERFACES + "nbt/INBTBase"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "[B").get(0)));
	}
}