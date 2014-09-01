package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.client;
public class C01PacketPingAnalyser extends StatusPacketAnalyser {
	
	public C01PacketPingAnalyser(ClassContainer container, HookMap hookMap) {
		super("C01PacketPing", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getTime", "J", "J") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}
