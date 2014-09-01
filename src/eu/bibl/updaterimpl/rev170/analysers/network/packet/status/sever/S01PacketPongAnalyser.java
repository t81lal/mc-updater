package eu.bibl.updaterimpl.rev170.analysers.network.packet.status.sever;
public class S01PacketPongAnalyser extends StatusPacketAnalyser {
	
	public S01PacketPongAnalyser(ClassContainer container, HookMap hookMap) {
		super("S01PacketPong", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getTime", "J", "J") };
	}
	
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "J").get(0)));
	}
}
