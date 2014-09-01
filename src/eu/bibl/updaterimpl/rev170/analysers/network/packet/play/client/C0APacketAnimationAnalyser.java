package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C0APacketAnimationAnalyser extends PlayPacketAnalyser {
	
	public C0APacketAnimationAnalyser(ClassContainer container, HookMap hookMap) {
		super("C0APacketAnimation", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getAnimationID", "I", "I") };
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
