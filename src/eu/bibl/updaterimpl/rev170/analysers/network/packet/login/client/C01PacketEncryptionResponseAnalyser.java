package eu.bibl.updaterimpl.rev170.analysers.network.packet.login.client;
public class C01PacketEncryptionResponseAnalyser extends LoginPacketAnalyser {
	
	public C01PacketEncryptionResponseAnalyser(ClassContainer container, HookMap hookMap) {
		super("C01PacketEncryptionResponse", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getData1", "[B", "[B"),
				new FieldMappingData("getData2", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		PacketAnalyser packetAnalyser = (PacketAnalyser) analysers.get("Packet");
		CallbackMappingData readCallbackMappingData = packetAnalyser.getCallbackMappingDatas()[0];
		for(MethodNode m : methods(cn)) {
			if (m.name.equals(readCallbackMappingData.getObfuscatedName()) && m.desc.equals(readCallbackMappingData.getObfuscatedDesc())) {
				InsnSearcher is = new InsnSearcher(m.instructions, 0, new int[] { PUTFIELD });
				if (is.match()) {
					addFieldHook(fieldHooks[0].buildObfFin((FieldInsnNode) is.getMatches().get(0)[0]));
					addFieldHook(fieldHooks[1].buildObfFin((FieldInsnNode) is.getMatches().get(1)[0]));
					break;
				}
			}
		}
	}
}
