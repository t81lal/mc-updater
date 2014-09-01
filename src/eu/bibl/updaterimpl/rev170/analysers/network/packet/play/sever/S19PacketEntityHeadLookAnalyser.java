package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S19PacketEntityHeadLookAnalyser extends PlayPacketAnalyser {
	
	public S19PacketEntityHeadLookAnalyser(ClassContainer container, HookMap hookMap) {
		super("S19PacketEntityHeadLook", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getRotationHeadYaw", "B", "B") };
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
