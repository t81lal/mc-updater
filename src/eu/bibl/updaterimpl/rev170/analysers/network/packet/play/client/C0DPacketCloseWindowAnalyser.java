package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C0DPacketCloseWindowAnalyser extends PlayPacketAnalyser {
	
	public C0DPacketCloseWindowAnalyser(ClassContainer container, HookMap hookMap) {
		super("C0DPacketCloseWindow", container, hookMap);
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
