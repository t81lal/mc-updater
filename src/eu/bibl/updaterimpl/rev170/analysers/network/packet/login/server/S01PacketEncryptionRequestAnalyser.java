package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.server;
public class S01PacketEncryptionRequestAnalyser extends LoginPacketAnalyser {
	
	public S01PacketEncryptionRequestAnalyser(ClassContainer container, HookMap hookMap) {
		super("S01PacketEncryptionRequest", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getString", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getPublicKey", "Ljava/security/PublicKey;", "Ljava/security/PublicKey;"),
				new FieldMappingData("getBytes", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "Ljava/lang/String;").get(0)));
		addFieldHook(fieldHooks[1].buildObfFn(fields(cn, "Ljava/security/PublicKey;").get(0)));
		addFieldHook(fieldHooks[2].buildObfFn(fields(cn, "[B").get(0)));
	}
}
