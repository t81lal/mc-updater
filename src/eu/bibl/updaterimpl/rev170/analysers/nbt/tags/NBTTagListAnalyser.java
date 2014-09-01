package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagListAnalyser extends NBTTagAnalyser {
	
	public NBTTagListAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagList", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getData", "Ljava/util/List;", "Ljava/util/List;"),
				new FieldMappingData("getTagType", "B", "B") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagList").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagList", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/util/List;").get(0)));
		addFieldHook(fieldHooks[1].buildObfFn(fields(cn, "B").get(0)));
	}
}
