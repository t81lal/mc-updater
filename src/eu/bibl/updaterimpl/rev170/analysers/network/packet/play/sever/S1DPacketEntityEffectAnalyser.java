package eu.bibl.updaterimpl.rev170.analysers.network.packet.play.sever;
public class S1DPacketEntityEffectAnalyser extends PlayPacketAnalyser {
	
	public S1DPacketEntityEffectAnalyser(ClassContainer container, HookMap hookMap) {
		super("S1DPacketEntityEffect", container, hookMap);
		fieldHooks = new FieldMappingData[] {
				new FieldMappingData("getEntityID", "I", "I"),
				new FieldMappingData("getEffectID", "B", "B"),
				new FieldMappingData("getAmplifier", "B", "B"),
				new FieldMappingData("getDuration", "S", "S") };
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
