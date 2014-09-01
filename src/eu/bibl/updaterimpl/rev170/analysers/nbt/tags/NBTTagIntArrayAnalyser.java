package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagIntArrayAnalyser extends NBTTagAnalyser {
	
	public NBTTagIntArrayAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagIntArray", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "[I", "[I") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagIntArray").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagIntArray", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "[I").get(0)));
	}
}
