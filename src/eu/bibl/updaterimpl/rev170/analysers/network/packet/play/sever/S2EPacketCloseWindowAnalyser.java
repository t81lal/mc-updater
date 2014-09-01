package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S2EPacketCloseWindowAnalyser extends PlayPacketAnalyser {
	
	public S2EPacketCloseWindowAnalyser(ClassContainer container, HookMap hookMap) {
		super("S2EPacketCloseWindow", container, hookMap);
		fieldHooks = new FieldMappingData[] { new FieldMappingData("getWindowID", "I", "I") };
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
