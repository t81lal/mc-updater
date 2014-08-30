package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagLongAnalyser extends NBTTagAnalyser {
	
	public NBTTagLongAnalyser() {
		super("NBTTagLong");
		hooks = new FieldHook[] { new FieldHook("getData", "J", "J") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagLong").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagLong", INTERFACES + "nbt/INBTPrimitive"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}