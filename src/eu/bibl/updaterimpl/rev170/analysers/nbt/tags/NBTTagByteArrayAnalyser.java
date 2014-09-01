package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagByteArrayAnalyser extends NBTTagAnalyser {
	
	public NBTTagByteArrayAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagByteArray", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "[B", "[B") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagByteArray").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagByteArray", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "[B").get(0)));
	}
}
