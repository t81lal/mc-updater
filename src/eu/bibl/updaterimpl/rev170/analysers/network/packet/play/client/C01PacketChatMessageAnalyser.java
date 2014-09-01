package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C01PacketChatMessageAnalyser extends PlayPacketAnalyser {
	
	public C01PacketChatMessageAnalyser(ClassContainer container, HookMap hookMap) {
		super("C01PacketChatMessage", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getMessage", "Ljava/lang/String;", "Ljava/lang/String;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addFieldHook(fieldHooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
	}
}
