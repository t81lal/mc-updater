package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C03PacketPlayerAnalyser extends PlayPacketAnalyser {
	
	public C03PacketPlayerAnalyser(ClassContainer container, HookMap hookMap) {
		super("C03PacketPlayer", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("isOnGround", "Z", "Z"),
				new FieldMappingData("getX", "D", "D"),
				new FieldMappingData("getFeetY", "D", "D"),
				new FieldMappingData("getHeadY", "D", "D"),
				new FieldMappingData("getZ", "D", "D"),
				new FieldMappingData("getRotationPitch", "F", "F"),
				new FieldMappingData("getRotationYaw", "F", "F") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		addFieldHook(fieldHooks[0].buildObfFin(fins[0]));
	}
}
