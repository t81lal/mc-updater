package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S3EPacketTeamsAnalyser extends PlayPacketAnalyser {
	
	public S3EPacketTeamsAnalyser(ClassContainer container, HookMap hookMap) {
		super("S3EPacketTeams", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getTeamName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getMode", "I", "I"),
				new FieldMappingData("getTeamDisplayName", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getTeamPrefix", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("getTeamSuffix", "Ljava/lang/String;", "Ljava/lang/String;"),
				new FieldMappingData("isFriendlyFire", "I", "I"),
				new FieldMappingData("getPlayers", "Ljava/util/Collection;", "Ljava/util/Collection;") };
	}
	
	@Override
	public void run1() {
		MethodNode m = getReadMethod(cn);
		FieldInsnNode[] fins = getFieldNodes(m, PUTFIELD);
		for(int i = 0; i < hooks.length - 1; i++) {
			addFieldHook(fieldHooks[i].buildObfFin(fins[i]));
		}
		addFieldHook(fieldHooks[6].buildObfFn(fields(cn, "Ljava/util/Collection;").get(0)));
	}
}
