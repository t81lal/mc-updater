package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S3BPacketScoreboardObjectiveAnalyser extends PlayPacketAnalyser {
	
	public S3BPacketScoreboardObjectiveAnalyser(ClassContainer container, HookMap hookMap) {
		super("S3BPacketScoreboardObjective", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getObjectiveName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getObjectiveValue", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getMode", "I", "I") };
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
