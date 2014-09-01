package eu.bibl.updaterimpl.rev170.analysers.world.provider;
public class WorldProviderOverworldAnalyser extends Analyser {
	
	public WorldProviderOverworldAnalyser(ClassContainer container, HookMap hookMap) {
		super("Overworld", container, hookMap);
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		if (hook.getObfuscatedName().equals(cn.superName))
			return containsLdc(cn, "Overworld");
		return false;
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProviderOverworld"));
	}
}
