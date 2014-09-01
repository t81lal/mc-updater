package eu.bibl.updaterimpl.rev170.analysers.nbt;
public class NBTPrimitiveAnalyser extends NBTAnalyser {
	
	public NBTPrimitiveAnalyser(ClassContainer container, HookMap hookMap) {
		super("NBTPrimitive", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return Access.isAbstract(cn.access) && cn.superName.equals(hookMap.getClassByRefactoredName("NBTBase").getObfuscatedName());
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "nbt/INBTPrimitive", MinecraftAnalyser.INTERFACES + "nbt/INBTBase"));
	}
}
