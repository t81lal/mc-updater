package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagListAnalyser extends NBTTagAnalyser {
	
	public NBTTagListAnalyser() {
		super("NBTTagList");
		hooks = new FieldHook[] {
				new FieldHook("getData", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldHook("getTagType", "B", "B") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagList").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagList", INTERFACES + "nbt/INBTBase"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
		addHook(hooks[1].buildObfFn(fields(cn, "B").get(0)));
	}
}