package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagFloatAnalyser extends NBTTagAnalyser {
	
	public NBTTagFloatAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagFloat", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "F", "F") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagFloat").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagFloat", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "F").get(0)));
	}
}
