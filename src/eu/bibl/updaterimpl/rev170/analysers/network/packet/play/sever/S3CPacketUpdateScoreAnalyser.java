package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S3CPacketUpdateScoreAnalyser extends PlayPacketAnalyser {
	
	public S3CPacketUpdateScoreAnalyser(ClassContainer container, HookMap hookMap) {
		super("S3CPacketUpdateScore", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getItemName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getMode", "I", "I"),
				new FieldMappingData("getScoreName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getValue", "I", "I") };
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
