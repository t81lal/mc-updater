package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagByteAnalyser extends NBTTagAnalyser {
	
	public NBTTagByteAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagByte", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getData", "B", "B") };
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagByte").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagByte", MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive"));
		
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "B").get(0)));
	}
}
