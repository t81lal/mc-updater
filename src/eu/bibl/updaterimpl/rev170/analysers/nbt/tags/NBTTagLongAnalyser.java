package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagLongAnalyser extends NBTTagAnalyser {
	
	public NBTTagLongAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagLong", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "J", "J") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagLong").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagLong", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}
