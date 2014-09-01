package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S02PacketChatAnalyser extends PlayPacketAnalyser {
	
	public S02PacketChatAnalyser(ClassContainer container, HookMap hookMap) {
		super("S02PacketChat", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getChatComponent", "L" + MinecraftAnalyser.INTERFACES + "chat/IChatComponent;") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("ChatComponent").getObfuscatedName() + ";").get(0)));
	}
}
