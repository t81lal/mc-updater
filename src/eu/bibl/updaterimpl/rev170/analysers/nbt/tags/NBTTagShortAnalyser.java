package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagShortAnalyser extends NBTTagAnalyser {
	
	public NBTTagShortAnalyser() {
		super("NBTTagShort");
		hooks = new FieldHook[] { new FieldHook("getData", "S", "S") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagShort").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagShort", INTERFACES + "nbt/INBTPrimitive"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "S").get(0)));
	}
}