package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;
public class S00PacketDisconnectAnalyser extends LoginPacketAnalyser {
	
	public S00PacketDisconnectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S00PacketDisconnect", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getChatComponent", "L" + MinecraftAnalyser.INTERFACES + "IChatComponent;") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("ChatComponent").getObfuscatedName() + ";").get(0)));
	}
}
