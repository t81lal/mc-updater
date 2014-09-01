package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagIntAnalyser extends NBTTagAnalyser {
	
	public NBTTagIntAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagInt", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "I", "I") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagInt").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagInt", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}
