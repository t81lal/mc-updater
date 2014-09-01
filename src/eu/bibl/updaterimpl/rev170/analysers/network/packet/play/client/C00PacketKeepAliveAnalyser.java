package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C00PacketKeepAliveAnalyser extends PlayPacketAnalyser {
	
	public C00PacketKeepAliveAnalyser(ClassContainer container, HookMap hookMap) {
		super("C00PacketKeepAlive", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getRandomInt", "I", "I") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addFieldHook(fieldHooks[0].buildObfFin(getFieldNodes(m, PUTFIELD)[0]));
	}
}
