package eu.bibl.updaterimpl.rev170.analysers.world;
public class GameModeAnalyser extends Analyser {
	
	public GameModeAnalyser(ClassContainer container, HookMap hookMap) {
		super("GameMode", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "NOT_SET");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "world/IGameMode"));
		
	}
}
