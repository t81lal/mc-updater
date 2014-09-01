package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S3APacketTabCompleteAnalyser extends PlayPacketAnalyser {
	
	public S3APacketTabCompleteAnalyser(ClassContainer container, HookMap hookMap) {
		super("S3APacketTabComplete", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getMatches", "[Ljava/lang/String;", "[Ljava/lang/String;") };
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
