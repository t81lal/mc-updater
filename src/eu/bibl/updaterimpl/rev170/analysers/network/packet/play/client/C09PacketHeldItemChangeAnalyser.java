package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C09PacketHeldItemChangeAnalyser extends PlayPacketAnalyser {
	
	public C09PacketHeldItemChangeAnalyser(ClassContainer container, HookMap hookMap) {
		super("C09PacketHeldItemChange", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getSlot", "I", "I") };
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
