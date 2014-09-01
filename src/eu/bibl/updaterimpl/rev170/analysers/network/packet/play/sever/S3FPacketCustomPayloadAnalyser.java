package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S3FPacketCustomPayloadAnalyser extends PlayPacketAnalyser {
	
	public S3FPacketCustomPayloadAnalyser(ClassContainer container, HookMap hookMap) {
		super("S3FPacketCustomPayload", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getChannel", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getData", "[B", "[B") };
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
