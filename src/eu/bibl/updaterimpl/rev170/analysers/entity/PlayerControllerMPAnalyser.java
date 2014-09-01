package eu.bibl.updaterimpl.rev170.analysers.entity;
public class PlayerControllerMPAnalyser extends Analyser{
	public PlayerControllerMPAnalyser(ClassContainer container, HookMap hookMap) {
		super("PlayerControllerMP", container, hookMap);
	}
	@Override
public boolean accept() {
		return containsLdc(cn, "Disconnected from server");
	}
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "entity/IPlayerControllerMP"));
		
		
	}
	
//	private void find
}
