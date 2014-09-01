package eu.bibl.updaterimpl.rev170.analysers.nbt.tags;
public class NBTTagEndAnalyser extends NBTTagAnalyser {
	
	public NBTTagEndAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTTagEnd", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return hookMap.getClassByRefactoredName("NBTTagEnd").getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/tags/INBTTagEnd", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
	}
}
