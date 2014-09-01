package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C17PacketCustomPayloadAnalyser extends PlayPacketAnalyser {
	
	public C17PacketCustomPayloadAnalyser(ClassContainer container, HookMap hookMap) {
		super("C17PacketCustomPayload", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getChannel", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getPayloadLength", "I", "I"),
				new FieldMappingData("getPayload", "[B", "[B") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
	}
}
