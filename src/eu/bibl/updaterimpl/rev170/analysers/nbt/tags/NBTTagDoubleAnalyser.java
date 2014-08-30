package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagDoubleAnalyser extends NBTTagAnalyser {
	
	public NBTTagDoubleAnalyser() {
		super("NBTTagDouble");
		hooks = new FieldHook[] { new FieldHook("getData", "D", "D") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagDouble").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagDouble", INTERFACES + "nbt/INBTPrimitive"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "D").get(0)));
	}
}