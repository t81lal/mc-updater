package eu.bibl.updaterimpl.rev170.analysers.world.provider;
public class WorldProviderAnalyser extends Analyser {
	
	public WorldProviderAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProvider", container, hookMap);
	}
	
	@Override
public boolean accept() {
		ClassMappingData hook = hookMap.getClassByRefactoredName("WorldProvider");
		if (hook == null)
			return false;
		return hook.getObfuscatedName().equals(cn.name);
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProvider"));
		
	}
}
