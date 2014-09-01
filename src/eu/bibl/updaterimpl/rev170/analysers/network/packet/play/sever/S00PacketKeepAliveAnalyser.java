package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S00PacketKeepAliveAnalyser extends PlayPacketAnalyser{
	public S00PacketKeepAliveAnalyser(ClassContainer container, HookMap hookMap) {
		super("S00PacketKeepAlive", container, hookMap);
		fieldHooks = new FieldMappingData[]{
			new FieldMappingData("getRandomInt", "I", "I")	
		};
	}
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}
