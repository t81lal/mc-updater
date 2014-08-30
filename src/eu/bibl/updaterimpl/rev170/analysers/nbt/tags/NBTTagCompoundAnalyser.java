package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;

import org.objectweb.asm.tree.ClassNode;

import eu.bibl.bytetools.analysis.storage.hooks.FieldHook;
import eu.bibl.bytetools.analysis.storage.hooks.InterfaceHook;

public class NBTTagCompoundAnalyser extends NBTTagAnalyser {
	
	public NBTTagCompoundAnalyser() {
		super("NBTTagCompound");
		hooks = new FieldHook[] { new FieldHook("getDataMap", "Ljava/util/Map;", "Ljava/util/Map;") };
	}
	
	@Override
	public boolean accept(ClassNode cn) {
		return map.getClassByRefactoredName("NBTTagCompound").getObfuscatedName().equals(cn.name);
	}
	
	@Override
	public void run() {
		classHook.setInterfaceHook(new InterfaceHook(classHook, INTERFACES + "nbt/tags/INBTTagCompound", INTERFACES + "nbt/INBTBase"));
		
		addHook(hooks[0].buildObfFn(fields(cn, "Ljava/util/Map;").get(0)));
	}
}