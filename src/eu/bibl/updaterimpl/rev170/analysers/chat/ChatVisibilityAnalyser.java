package eu.bibl.updaterimpl.rev170.analysers.chat;
public class ChatVisibilityAnalyser extends Analyser {
	
	public ChatVisibilityAnalyser(ClassContainer container, HookMap hookMap) {
		super("ChatVisibility", container, hookMap);
	}
	
	@Override
public boolean accept() {
		return ((cn.access & ACC_ENUM) != 0) && containsLdc(cn, "FULL");
	}
	
	@Override
public InterfaceMappingData run() {
		classHook.setInterfaceHook(new InterfaceMappingData(MinecraftAnalyser.INTERFACES + "chat/IChatVisibility"));
		
		MinecraftAnalyser analyser = (MinecraftAnalyser) analysers.get("Minecraft");
		addMinecraftHook(analyser.getHooks()[9].buildObfFn(fields(cn, "[L" + cn.name + ";").get(0)));
	}
}
