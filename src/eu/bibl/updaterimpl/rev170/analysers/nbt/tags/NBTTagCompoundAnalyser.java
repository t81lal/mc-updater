package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagCompoundAnalyser extends NBTTagAnalyser {
	
	public NBTTagCompoundAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagCompound", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getDataMap", "Ljava/util/Map;", "Ljava/util/Map;") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagCompound").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagCompound", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/util/Map;").get(0)));
	}
}
