package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S1BPacketEntityAttachAnalyser extends PlayPacketAnalyser {
	
	public S1BPacketEntityAttachAnalyser(ClassContainer container, HookMap hookMap) {
		super("S1BPacketEntityAttach", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getVehicleID", "I", "I"),
				new FieldMappingData("isLeash", "I", "I") };
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
