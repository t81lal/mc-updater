package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2BPacketChangeGameStateAnalyser extends PlayPacketAnalyser {
	
	public S2BPacketChangeGameStateAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2BPacketChangeGameState", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getReason", "I", "I"),
				new FieldMappingData("getValue", "F", "F") };
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
