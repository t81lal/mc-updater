package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S0BPacketAnimationAnalyser extends PlayPacketAnalyser {
	
	public S0BPacketAnimationAnalyser(ClassContainer container, HookMap hookMap) {
		super("S0BPacketAnimation", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getAnimation", "I", "I") };
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
