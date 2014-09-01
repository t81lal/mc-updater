package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S1FPacketSetExperienceAnalyser extends PlayPacketAnalyser {
	
	public S1FPacketSetExperienceAnalyser(ClassContainer container, HookMap hookMap) {
		super("S1FPacketSetExperience", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getExperienceBar", "F", "F"),
				new FieldMappingData("getLevel", "I", "I"),
				new FieldMappingData("getTotalExperience", "I", "I"), };
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
