package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S09PacketHeldItemChangeAnalyser extends PlayPacketAnalyser{
	public S09PacketHeldItemChangeAnalyser(ClassContainer container, HookMap hookMap) {
		super("S09PacketHeldItemChange", container, hookMap);
		fieldHooks = new FieldMappingData[]{
			new FieldMappingData("getSlot", "I", "I")	
		};
	}
	@Override
	public void run1() {
		addFieldHook(fieldHooks[0].buildObfFn(fields(cn, "I").get(0)));
	}
}
