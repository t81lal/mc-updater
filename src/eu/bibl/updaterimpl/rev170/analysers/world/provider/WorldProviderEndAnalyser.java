package eu.bibl.updaterimpl.rev170.analysers.world.provider;
public class WorldProviderEndAnalyser extends Analyser {
	
	public WorldProviderEndAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProviderEnd", container, hookMap);
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		if (hook.getObfuscatedName().equals(cn.superName))
			return containsLdc(cn, "The End");
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProviderEnd"));
	}
}
