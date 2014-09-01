package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagDoubleAnalyser extends NBTTagAnalyser {
	
	public NBTTagDoubleAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagDouble", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "D", "D") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagDouble").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagDouble", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "D").get(0)));
	}
}
