package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C16PacketClientStatusAnalyser extends PlayPacketAnalyser {
	
	public C16PacketClientStatusAnalyser(ClassContainer container, HookMap hookMap) {
		super("C16PacketClientStatus", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getClientState", "L" + MinecraftAnalyser.INTERFACES + "client/IClientState;") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "L" + hookMap.getClassByRefactoredName("ClientState").getObfuscatedName() + ";").get(0)));
	}
}
