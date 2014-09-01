package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.client;
public class C0BPacketEntityActionAnalyser extends PlayPacketAnalyser {
	
	public C0BPacketEntityActionAnalyser(ClassContainer container, HookMap hookMap) {
		super("C0BPacketEntityAction", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getActionID", "I", "I"),
				new FieldMappingData("getHorseJumpBoost", "I", "I") };
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
