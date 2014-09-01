package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S14PacketEntityAnalyser extends PlayPacketAnalyser{
	public S14PacketEntityAnalyser(ClassContainer container, HookMap hookMap) {
		super("S14PacketEntity", container, hookMap);
		fieldHooks = new FieldMappingData[]{
			new FieldMappingData("getEntityID", "I", "I"),
			new FieldMappingData("getChangeX", "B", "B"),
			new FieldMappingData("getChangeY", "B", "B"),
			new FieldMappingData("getChangeZ", "B", "B"),
			new FieldMappingData("getRotationPitch", "B", "B"),
			new FieldMappingData("getRotationYaw", "B", "B")
		};
	}
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		addFieldHook(fieldHooks[0].buildObfFin((FieldInsnNode) getNext(m.instructions.getFirst(), PUTFIELD)));
	}
}
