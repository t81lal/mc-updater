package eu.bibl.updaterimpl.rev170.analysers.world.provider;
public class WorldProviderHellAnalyser extends Analyser {
	
	public WorldProviderHellAnalyser(ClassContainer container, HookMap hookMap) {
		super("WorldProviderHell", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return containsLdc(cn, "Nether");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/provider/IWorldProviderHell"));
		
		hookMap.addClass(new ClassMappingData(cn.superName, "WorldProvider"));
		
	}
}
