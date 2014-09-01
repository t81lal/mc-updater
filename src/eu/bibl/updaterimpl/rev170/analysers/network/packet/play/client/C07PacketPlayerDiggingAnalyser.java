package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C07PacketPlayerDiggingAnalyser extends PlayPacketAnalyser {
	
	public C07PacketPlayerDiggingAnalyser(ClassContainer container, HookMap hookMap) {
		super("C07PacketPlayerDigging", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getStatus", "I", "I"),
				new FieldMappingData("getX", "I", "I"),
				new FieldMappingData("getY", "I", "I"),
				new FieldMappingData("getZ", "I", "I"),
				new FieldMappingData("getFace", "I", "I") };
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
