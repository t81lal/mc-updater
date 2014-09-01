package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C0CPacketInputAnalyser extends PlayPacketAnalyser {
	
	public C0CPacketInputAnalyser(ClassContainer container, HookMap hookMap) {
		super("C0CPacketInput", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getSidewaysMotion", "F", "F"),
				new FieldMappingData("getForwardMotion", "F", "F"),
				new FieldMappingData("isJump", "Z", "Z"),
				new FieldMappingData("isUnmount", "Z", "Z") };
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
