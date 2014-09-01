package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagShortAnalyser extends NBTTagAnalyser {
	
	public NBTTagShortAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagShort", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "S", "S") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagShort").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagShort", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "S").get(0)));
	}
}
