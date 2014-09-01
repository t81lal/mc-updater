package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagStringAnalyser extends NBTTagAnalyser {
	
	public NBTTagStringAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagString", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagString").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagString", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
	}
}
