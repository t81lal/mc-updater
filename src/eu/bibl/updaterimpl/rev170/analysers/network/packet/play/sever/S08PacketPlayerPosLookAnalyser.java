package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S08PacketPlayerPosLookAnalyser extends PlayPacketAnalyser {
	
	public S08PacketPlayerPosLookAnalyser(ClassContainer container, HookMap hookMap) {
		super("S08PacketPlayerPosLook", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getX", "D", "D"),
				new FieldMappingData("getY", "D", "D"),
				new FieldMappingData("getZ", "D", "D"),
				new FieldMappingData("getRotationYaw", "F", "F"),
				new FieldMappingData("getRotationPitch", "F", "F"),
				new FieldMappingData("isOnGround", "Z", "Z") };
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
