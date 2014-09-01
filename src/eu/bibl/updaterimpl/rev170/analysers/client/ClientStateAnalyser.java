package eu.bibl.updaterimpl.rev170.analysers.client;
public class ClientStateAnalyser extends Analyser {
	
	public ClientStateAnalyser(ClassContainer container, HookMap hookMap) {
		super("ClientState", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "PERFORM_RESPAWN");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "client/IClientState"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[11].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}
